package net.tinyportal.service.portlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tinyportal.exception.NoSuchPortletException;
import net.tinyportal.javax.portlet.TpActionRequest;
import net.tinyportal.javax.portlet.TpActionResponse;
import net.tinyportal.javax.portlet.TpPortletConfig;
import net.tinyportal.javax.portlet.TpRenderRequest;
import net.tinyportal.javax.portlet.TpRenderResponse;
import net.tinyportal.service.Service;
import net.tinyportal.service.ServicePortlet;
import net.tinyportal.tools.TpHttpServletResponse;
import net.tinyportal.tools.TpHttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

public class PortletExecutor {

	@Autowired
	private HttpServletRequest httpRequest;

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private TpRenderRequest portletRequest;

	@Autowired
	private TpRenderResponse portletResponse;

	@Autowired
	private TpActionRequest actionRequest;

	@Autowired
	private TpActionResponse actionResponse;

	@Autowired
	private PortletLoader portletLoader;

	@Autowired
	private PortletPool portletPool;

	@Autowired
	private PortletTools portletTools;

	public Map<String, String> executePortlets(PortletName[] portlets) {
		for (PortletName portlet : portlets) {
			try {
				initPortlet(portlet.getContext(), portlet.getName(), portlet.getInstance());
			} catch (NoSuchPortletException | PortletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (PortletName portlet : portlets) {
			try {
				String portletFullName = portletTools.getPortletFullName(portlet.getContext(), portlet.getName(), portlet.getInstance());
				Portlet p = ServicePortlet.getInstance(portletFullName);
				doAction(portlet.getContext(), portlet.getName(), portlet.getInstance(), p);
			} catch (IOException | NoSuchPortletException | PortletException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Map<String, String> render = new HashMap<String, String>();
		for (PortletName portlet : portlets) {
			try {
				String portletFullName = portletTools.getPortletFullName(portlet.getContext(), portlet.getName(), portlet.getInstance());
				Portlet p = ServicePortlet.getInstance(portletFullName);
				String portletRender = doRender(portlet.getContext(), portlet.getName(), portlet.getInstance(), p);
				render.put(portlet.getInstance(), portletRender);
			} catch (ServletException | NoSuchPortletException | PortletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return render;
		
	}

	/**
	 * Charge et initialise l'instance du portlet, si nécessaire
	 * @param portletContext ServletContext dans lequel se trouve le portlet
	 * @param portletName Nom du portlet tel que définie dans le portlet.xml avec la balise <code>portlet-name</code>
	 * @param portletInstance Nom de l'instance du portlet
	 * @throws NoSuchPortletException
	 * @throws PortletException
	 */
	private void initPortlet(String portletContext, String portletName,
			String portletInstance) throws NoSuchPortletException,
			PortletException {
		ServletContext context = httpRequest.getServletContext().getContext(portletContext);
		String portletFullName = portletTools.getPortletFullName(portletContext, portletName, portletInstance);

		/*
		 * On vérifie que l'on a déjà charger le context et le portlet 
		 */
		if (!portletPool.hasPortlet(portletContext, portletName) || !Service.hasService(portletFullName)) {
			if (!portletPool.hasPortletContext(portletContext)){
				/*
				 * On charge le contexte
				 */
				portletLoader.load(context);				
			}
			
			/*
			 * Une fois fait, on vérifie que le portlet est présent
			 */
			if (!portletPool.hasPortlet(portletContext, portletName)) {
				//TODO lever une exception: le contexte est chargé mais le portlet n'est pas présent
				throw new NoSuchPortletException();
			}

			/*
			 * On initialise le portlet
			 */
			TpPortletConfig portletConfig = portletPool.getPortletConfig(context.getContextPath(), portletName);
			Service.addService(portletFullName, portletConfig.getPortletClass(), context);
			Portlet p = ServicePortlet.getInstance(portletFullName);
			p.init((PortletConfig)portletConfig);
		}
	}

	/**
	 * Effectue la methode processAction du portlet si besoin
	 * @param portletContext ServletContext dans lequel se trouve le portlet
	 * @param portletName Nom du portlet tel que définie dans le portlet.xml avec la balise <code>portlet-name</code>
	 * @param portletInstance Nom de l'instance du portlet
	 * @param p
	 * @throws NoSuchPortletException
	 * @throws PortletException
	 * @throws IOException
	 */
	private void doAction(String portletContext, String portletName,
			String instance, Portlet p) throws NoSuchPortletException,
			PortletException, IOException {
		/*
		 * Dans le cas ou l'on doit traiter l'action pour le portlet
		 */
		StringBuffer sbParam = new StringBuffer(portletTools.getPortletFullName(portletContext, portletName, instance)).append("_action");
		String parameter = httpRequest.getParameter(sbParam.toString());
		if ("action".equalsIgnoreCase(parameter)) {
			actionRequest.init(portletContext, portletName, instance);
			actionResponse.init(portletContext, portletName, instance);
			p.processAction(actionRequest, actionResponse);
		}
	}

	/**
	 * Effectue la methode render du portlet si besoin
	 * @param portletContext ServletContext dans lequel se trouve le portlet
	 * @param portletName Nom du portlet tel que définie dans le portlet.xml avec la balise <code>portlet-name</code>
	 * @param portletInstance Nom de l'instance du portlet
	 * @param p
	 * @return le rendu du portlet
	 * @throws NoSuchPortletException
	 * @throws PortletException
	 * @throws IOException
	 * @throws ServletException
	 */
	private String doRender(String portletContext,
			String portletName, String instance, Portlet p)
			throws NoSuchPortletException, PortletException, IOException,
			ServletException {
		/*
		 * Dans tous les cas, on effectue le rendu du portlet
		 */
		portletRequest.init(portletContext, portletName, instance);
		portletResponse.init(portletContext, portletName, instance);
		p.render((RenderRequest)portletRequest, (RenderResponse)portletResponse);
		RequestDispatcher servletDispatcher = portletResponse.getDispatcher();

		httpRequest.setAttribute("renderRequest", (RenderRequest)portletRequest);
		httpRequest.setAttribute("renderResponse", (RenderResponse)portletResponse);
		HttpServletResponse newResponse = new TpHttpServletResponse((HttpServletResponse) httpResponse, portletResponse);

		TpHttpServletRequest requestWrapper = new TpHttpServletRequest(httpRequest, portletRequest);
		servletDispatcher.include(requestWrapper, newResponse);
		return newResponse.toString();
	}
}

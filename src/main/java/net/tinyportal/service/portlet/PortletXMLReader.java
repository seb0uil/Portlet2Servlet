package net.tinyportal.service.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;

import net.tinyportal.javax.portlet.TpPortletConfig;
import net.tinyportal.javax.portlet.TpPortletPreference;
import net.tinyportal.tools.PortletXML;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

public class PortletXMLReader implements ServletContextAware {

	
	@Autowired
	ServletContext servletContext;
	
	public List<TpPortletConfig> reader(InputStream xmlDocument) {
		// Liste des configs contenu dans le portletxml
		List<TpPortletConfig> portletsConfig = new ArrayList<TpPortletConfig>();
		Document document;
		try {
			SAXBuilder sxb = new SAXBuilder();
			document = sxb.build(xmlDocument);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			return portletsConfig;
		}

		//On initialise un nouvel élément racine avec l'élément racine du document.
		Element racine = document.getRootElement();

		Namespace portletNS = racine.getNamespace();
		List<Element> portletsElement = racine.getChildren("portlet",portletNS);

		for (Element portletElement : portletsElement) {
			PortletXML portletXml = new PortletXML(portletElement, portletNS);
			String portletName  = portletXml.getPortletName();
			TpPortletConfig tpPortletConfig = new TpPortletConfig();

			tpPortletConfig.setSecurity(portletXml.getSecurity());
			tpPortletConfig.setBundleBaseName(portletXml.getBundle(), servletContext.getClassLoader());
			tpPortletConfig.setMimeType(portletXml.getMimeType());
			tpPortletConfig.setPortletInfo(portletXml.getPortletInfo());
			tpPortletConfig.setPortletModes(portletXml.getPortletModes());
			tpPortletConfig.setWindowStates(portletXml.getWindowsStates());
			tpPortletConfig.setPorletName(portletName);

			/*
			 * On ajoute les modes Obligatoires 
			 */
			tpPortletConfig.getWindowsStates().add(WindowState.NORMAL);
			tpPortletConfig.getPortletModes().add(PortletMode.VIEW);

			/*
			 * On gere les préférences
			 */
			TpPortletPreference p = new TpPortletPreference(portletXml.getPortletPreference(),portletXml.getReadOnlyPreferences());
			tpPortletConfig.setPortletPreference(p);

			tpPortletConfig.setPortletClass(portletXml.getPortletClass());
			portletsConfig.add(tpPortletConfig);
		}
		return portletsConfig;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}

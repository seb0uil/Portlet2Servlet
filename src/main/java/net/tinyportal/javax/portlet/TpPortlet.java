package net.tinyportal.javax.portlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tinyportal.exception.NoSuchPortletException;
import net.tinyportal.service.portlet.PortletTools;

import org.springframework.beans.factory.annotation.Autowired;

public class TpPortlet {
	@Autowired
	protected HttpServletRequest httpRequest;
	
	@Autowired
	protected HttpServletResponse httpServletResponse;
	
	@Autowired
	private PortletTools portletTools;
	
	protected String portletContext;
	
	protected String portletName;

	private String instance;
	
	public static final String PORTLET_MODE = "PortletMode";
	
	public static final String WINDOW_STATE = "WindowState";
	
	public static final String PORTLET_SESSION = "PortletSession";
	
	
	public void init(String portletContext, String portletName, String instance) throws NoSuchPortletException {
		this.portletContext = portletContext;
		this.portletName = portletName;
		this.instance = instance;
	}
	
	/**
	 * Positionne un object en session pour cette instance de portlet
	 * @param value nom de l'object a placer en session
	 * @param o Objet a placer en session
	 */
	protected void setValue(String value, Object o) {
		httpRequest.getSession(true).setAttribute(getPortletFullName().concat(value), o);
	}
	
	/**
	 * Recupère un objet placer en session pour cette instance de portlet
	 * @param value nom de l'object a placer en session
	 * @return l'objet s'il existe, null sinon
	 */
	protected Object getValue(String value) {
		return httpRequest.getSession(true).getAttribute(getPortletFullName().concat(value));
	}
	
	/* Tools */
	protected String getPortletFullName() {
		return portletTools.getPortletFullName(this.portletContext, this.portletName, this.instance);
	}
}

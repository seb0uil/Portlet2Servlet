/*
    This file is part of tPortal.

    tPortal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    tPortal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with tPortal.  If not, see <http://www.gnu.org/licenses/>.

    The original code was written by Sebastien Bettinger <sebastien.bettinger@gmail.com>

 */

package net.tinyportal.javax.portlet;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

import net.tinyportal.exception.NoSuchPortletException;
import net.tinyportal.service.portlet.PortletPool;
import net.tinyportal.tools.TpEnumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Implémentation de portletRequest.
 * Nécessite d'être initialisé par {@link #init(String, String)} avant l'utilisation
 * @author SBE10599
 *
 */
public class TpPortletRequest extends TpPortlet implements PortletRequest {

	
	@Autowired
	PortletPool portletPool;
	
	@Autowired
	TpPortalContext portalContext;
	
	@Autowired
	ApplicationContext applicationContext;
	

	
	Map<String, Object> attributes = new HashMap<String, Object>();
	
	
	private Map<String, String[]> parameters = new HashMap<String, String[]>();

	/**
	 * Configuration du porlet, setté dans la méthode {@link #init(String, String)}
	 */
	private TpPortletConfig portletConfig;
	
	/**
	 * Initialise les informations de la request pour le portlet passé en paramètre
	 * @param portletContext Context du portlet pour lequel initialisé la request
	 * @param portletName Nom du portlet pour lequel initialisé la request
	 * @throws NoSuchPortletException Si le portlet passé en paramètre n'existe pas
	 */
	public void init(String portletContext, String portletName, String instance) throws NoSuchPortletException {
		if (!portletPool.hasPortlet(portletContext, portletName)) {
			throw new NoSuchPortletException();
		}
		super.init(portletContext, portletName, instance);
		portletConfig = portletPool.getPortletConfig(portletContext, portletName);


		/*
		 * A partir des paramètres positionnés pour la requete http, on récupère les paramètres
		 * liés au portlet.
		 * Pour cela, on retire le préfix [portletFullName]_param_ lorsqu'il est présent
		 */
		Enumeration<String> parameterEnum = httpRequest.getParameterNames();
		StringBuffer sbParam = new StringBuffer(getPortletFullName()).append("_param_");
		parameters = new HashMap<String, String[]>(); //On réinitialise la map parametre
		while (parameterEnum.hasMoreElements()) {
			String parameters = (String) parameterEnum.nextElement();
			if (parameters.startsWith(sbParam.toString())) {
				String value = httpRequest.getParameter(parameters);
				String parameter = parameters.substring(sbParam.length());
				this.parameters.put(parameter, new String[] {value});
			} else if (parameters.equalsIgnoreCase(getPortletFullName().concat("_mode"))){
				String value = httpRequest.getParameter(parameters);
				PortletMode portletMode = new PortletMode(value);
				//TODO lever une exception si portletModeAllowed retourne false
				if(isPortletModeAllowed(portletMode)) {
					setPortletMode(portletMode);
				}
				
			} else{
				String value = httpRequest.getParameter(parameters);
				this.parameters.put(parameters, new String[] {value});
			}
		}
	}

	@Override
	public boolean isWindowStateAllowed(WindowState state) {
		return portletConfig.getWindowsStates().contains(state);
	}

	@Override
	public boolean isPortletModeAllowed(PortletMode mode) {
		return portletConfig.getPortletModes().contains(mode);
	}

	@Override
	public PortletMode getPortletMode() {
		PortletMode portletMode = (PortletMode)getValue(PORTLET_MODE);
		return (portletMode!=null)?portletMode:PortletMode.VIEW;
	}
	
	public void setPortletMode(PortletMode portletMode) {
		setValue("PortletMode", portletMode);
	}

	@Override
	public WindowState getWindowState() {
		WindowState windowState = (WindowState)getValue(WINDOW_STATE);
		return (windowState!=null)?windowState:WindowState.NORMAL;
	}

	@Override
	public PortletPreferences getPreferences() {
		return portletConfig.getPortletPreference();
	}

	@Override
	public PortletSession getPortletSession() {
		return getPortletSession(true);
	}

	@Override
	public PortletSession getPortletSession(boolean create) {
		TpPortletSession portletSession = (TpPortletSession)getValue(PORTLET_SESSION);
		if (create && portletSession == null) {
				portletSession = new TpPortletSession(this.httpRequest.getSession(true), portletContext, portletName);
				applicationContext.getAutowireCapableBeanFactory().autowireBean(portletSession);
				setValue(PORTLET_SESSION, portletSession);
		}
		return portletSession;
	}

	@Override
	public String getProperty(String name) {
		Enumeration<String> enumeration = getProperties(name);
		if (enumeration.hasMoreElements())
			return enumeration.nextElement();
		else
			return null;
	}

	/**
	 * On va concaténer différentes valeurs afin de construire l'énumération
	 * de retour.<br/>
	 * On regarde donc: </br>
	 * <ul>
	 * 	<li>Dans les entêtes http</li>
	 *  <li>Dans le contextes du portail</li>
	 *  <li>Dans les paramètres de la requête http</li>
	 * </ul>
	 */
	@Override
	public Enumeration<String> getProperties(String name) {
		if (name == null) throw new IllegalArgumentException("Null value");
		
		List<String> l = new ArrayList<String>();
		
		/*
		 * On charge l'éventuel entête avec ce nom
		 */
		String property = this.httpRequest.getHeader(name);
		if (property != null) 
			l.add(property);
		
		Enumeration<String> properties = portalContext.getProperties(name);
		if (properties != null) 
			while (properties.hasMoreElements()) {
				l.add(properties.nextElement());
			}
		
		String[] propertiesArray = parameters.get(name);
		if (propertiesArray != null)
			l.addAll(Arrays.asList(propertiesArray));
		
		Enumeration<String> enumeration = new TpEnumeration<String>(l);
		return enumeration;
	}

	@Override
	public Enumeration getPropertyNames() {
		return portalContext.getPropertyNames();
	}

	@Override
	public PortalContext getPortalContext() {
		return portalContext;
	}

	@Override
	public String getAuthType() {
		return this.httpRequest.getAuthType();
	}

	@Override
	public String getContextPath() {
		return portletContext;
	}

	@Override
	public String getRemoteUser() {
		return this.httpRequest.getRemoteUser();
	}

	@Override
	public Principal getUserPrincipal() {
//		return (Principal) this.portletSession.getAttribute(UserService.SESSION_PRINCIPAL);
		return this.httpRequest.getUserPrincipal();
	}

	/**
	 * Le rôle est récupéré après avoir éventuellement été
	 * mappé sur ceux géré dans la définition XML du portlet
	 */
	@Override
	public boolean isUserInRole(String role) {
		if (this.portletConfig.getSecurity().containsKey(role)) {
			role =  portletConfig.getSecurity().get(role);
		}
		return this.httpRequest.isUserInRole(role);
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}
	
	@Override
	public String getParameter(String name) {
		if (this.parameters.containsKey(name))
			return this.parameters.get(name)[0];
		else
			return null;
	}

	@Override
	public Enumeration getParameterNames() {
		return Collections.enumeration(this.parameters.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
			return this.parameters.get(name);
	}

	@Override
	public Map getParameterMap() {
		return this.parameters;
	}

	@Override
	public boolean isSecure() {
		return this.httpRequest.isSecure();
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public String getRequestedSessionId() {
		return this.httpRequest.getRequestedSessionId();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return this.httpRequest.isRequestedSessionIdValid();
	}

	@Override
	public String getResponseContentType() {
		while (getResponseContentTypes().hasMoreElements()) {
			return (String) getResponseContentTypes().nextElement();
		}
		return null;
	}

	@Override
	public Enumeration getResponseContentTypes() {
//		return Collections.enumeration(portletHolder.getMimeType());
		//TODO
		return null;
	}

	@Override
	public Locale getLocale() {
		return this.httpRequest.getLocale();
	}

	@Override
	public Enumeration getLocales() {
		return this.httpRequest.getLocales();
	}

	@Override
	public String getScheme() {
		return this.httpRequest.getScheme();
	}

	@Override
	public String getServerName() {
		return this.httpRequest.getServerName();
	}

	@Override
	public int getServerPort() {
		return this.httpRequest.getServerPort();
	}
	
	/*
	 * Getter & Setter
	 */
	public void setParameter(String name, String value) {
		this.parameters.put(name, new String[] {value});
	}
	
	public void setParameters(String name, String[] values) {
		this.parameters.put(name, values);
	}


	

}

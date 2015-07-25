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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;

import net.tinyportal.Constant;
import net.tinyportal.service.portlet.PortletPool;
import net.tinyportal.tools.TpEnumeration;

import org.springframework.beans.factory.annotation.Autowired;


public class TpPortletSession implements PortletSession {
	@Autowired
	PortletPool portletPool;
	
	/**
	 * Session http sur laquelle s'appuie la session du portlet
	 */
	HttpSession session;
		
	/**
	 * Discriminant pour le portlet afin de gérer les 
	 * objets en session en fonction du portlet
	 */
	String portletScopeName;
	
	/**
	 * Discriminant pour l'application afin de gérer les 
	 * objets en session en fonction du portail
	 */
	String applicationScopeName;
	
	String portletContext;
	
	String portletName;
	
	/**
	 * Flag indiquant si la session est valide
	 */
	private boolean validSession= true;
	
	protected TpPortletSession(HttpSession session, String portletContext, String portletName) {
		this.session = session;
		this.applicationScopeName = Constant.javax_portlet_session + portletContext;
		this.portletScopeName = applicationScopeName + portletName;
		
		this.portletContext = portletContext;
		this.portletName = portletName;
	}

	@Override
	public Object getAttribute(String name) {
		return getAttribute(name, PortletSession.PORTLET_SCOPE);
	}

	@Override
	public Object getAttribute(String name, int scope) {
		if (scope == PortletSession.PORTLET_SCOPE)
			name = portletScopeName + name;
		else
			name = applicationScopeName + name;
		return session.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return getAttributeNames(PortletSession.PORTLET_SCOPE);
	}

	@Override
	public Enumeration<String> getAttributeNames(int scope) {
		List<String> l = new ArrayList<String> ();
		Enumeration<String> AttributeNames = session.getAttributeNames();
		while (AttributeNames.hasMoreElements()) {
			String attributeName = AttributeNames.nextElement();
			if ((attributeName.startsWith(portletScopeName)) && (scope == PortletSession.PORTLET_SCOPE)) {
				l.add(attributeName.substring(portletScopeName.length()));
			} else if ((attributeName.startsWith(applicationScopeName)) && (scope == PortletSession.APPLICATION_SCOPE)) { 
				l.add(attributeName.substring(applicationScopeName.length()));
			}
		}
		return new TpEnumeration<String>(l);
	}

	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	@Override
	public String getId() {
		return session.getId();
	}

	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	@Override
	public void invalidate() {
		session.invalidate();
		validSession = false;
	}
	
	/**
	 * Indique si la session en cours est valide ou non
	 * @return Vrai si la session est valide
	 */
	public boolean isValidSession() {
		return validSession;
	}
	

	@Override
	public boolean isNew() {
		return session.isNew();
	}

	@Override
	public void removeAttribute(String name) {
		removeAttribute(name,PortletSession.PORTLET_SCOPE);
	}

	@Override
	public void removeAttribute(String name, int scope) {
		if (scope == PortletSession.PORTLET_SCOPE) 
			name = portletScopeName + name;
		else 
			name = applicationScopeName + name;
		
		session.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		setAttribute(name, value, PortletSession.PORTLET_SCOPE);
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		if (scope == PortletSession.PORTLET_SCOPE)
			name = portletScopeName + name;
		else 
			name = applicationScopeName + name;
		session.setAttribute(name,value);
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}

	@Override
	public PortletContext getPortletContext() {
		return portletPool.getPortletConfig(portletContext, portletName).getPortletContext();
	}

}

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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import net.tinyportal.Constant;

import org.apache.log4j.Logger;

public class TpPortalContext implements PortalContext {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(TpPortalContext.class);

	/**
	 * Ensemble des mode autorisés par le portail
	 */
	private static Vector<PortletMode> portletMode = new Vector<PortletMode>();
	
	/**
	 * Ensemble des état autorisés par le portail
	 */
	private static Vector<WindowState> windowState = new Vector<WindowState>();
	
	/*
	 * On initialize les modes et les états autorisés par le portail 
	 */
	public TpPortalContext() {
		portletMode.add(PortletMode.VIEW);
		portletMode.add(PortletMode.EDIT);
		portletMode.add(PortletMode.HELP);
		
		windowState.add(WindowState.MAXIMIZED);
		windowState.add(WindowState.NORMAL);
		windowState.add(WindowState.MINIMIZED);
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		java.io.InputStream is = cl.getResourceAsStream(Constant.PORTAL_PROPERTY);
		if (is != null) {
			property = new Properties();
			try {
				property.load(is);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	private Properties property;

	@Override
	public String getProperty(String name) {
		return property.getProperty(name);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getPropertyNames() {
		return property.elements();
	}

	public Enumeration<String> getProperties(String name) {
		String[] properties = ((String)property.get(name)).split(",");
		return Collections.enumeration(Arrays.asList(properties));
	}

	@Override
	public Enumeration<PortletMode> getSupportedPortletModes() {
		return TpPortalContext.portletMode.elements();		
	}

	@Override
	public Enumeration<WindowState> getSupportedWindowStates() {
		return TpPortalContext.windowState.elements();
	}

	@Override
	public String getPortalInfo() {
		return getProperty(Constant.portal_version);
	}
	
	/**
	 * Vérifie si le mode passé en paramètre est autorisé
	 * par le portail
	 * @param mode Mode à tester
	 * @return Vrai si le mode est autorisé par le portail
	 */
	public boolean isPortletModeAllowed(PortletMode mode) {
		return TpPortalContext.portletMode.contains(mode);	
	}
	
	/**
	 * Vérifie si l'état passé en paramètre est autorisé
	 * par le portail
	 * @param state Etat à tester
	 * @return Vrai si l'état est autorisé par le portail
	 */
	public boolean isWindowStateAllowed(WindowState state) {
		return TpPortalContext.windowState.contains(state);
	}
}

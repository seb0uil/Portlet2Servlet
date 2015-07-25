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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;

import net.tinyportal.Constant;
import net.tinyportal.tools.TpResourceBundle;

public class TpPortletConfig implements PortletConfig , Cloneable{


	TpPortletContext portletContext;

	/**
	 * Paramètres positionnés dans le portlet.xml
	 * dans le noeud <init-param>
	 */
	Map<String, String> initParameter;

	/**
	 * Liste des mode supportés par le portlet
	 */
	private List<PortletMode> portletMode = new ArrayList<PortletMode>();

	/**
	 * Liste des types Mime supportés par le portlet
	 */
	private List<String> MimeType = new ArrayList<String>();

	/**
	 * Liste des windowState supportés par le portlet
	 */
	private List<WindowState> windowState = new ArrayList<WindowState>();

	/**
	 * ClassLoader utilisé pour le portlet
	 */
	ClassLoader loader;

	/**
	 * Class definissant le portlet
	 */
	String portletClass;

	/**
	 * Nom du bundle de message utilisé par le portlet
	 */
	String bundleBaseName;
	ClassLoader bundleClassLoader;

	/**
	 * Map clé/valeur de l'ensemble des éléments <code>portlet-info</code>
	 * de la configuration xml
	 */
	Map<String, String> portletInfo;

	/**
	 * Nom du portlet issue de la balise <code>portlet-name</code>
	 */
	String porletName;

	/**
	 * Map contenant les équivalences pour les groupes
	 */
	private Map<String, String> security = new HashMap<String, String>();

	/**
	 * Ensemble des préférences du portlet
	 */
	TpPortletPreference portletPreference;

	public TpPortletConfig() {}

	@Override
	public String getPortletName() {
		return porletName;
	}

	@Override
	public PortletContext getPortletContext() {
		return this.portletContext;
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		ResourceBundle messages = ResourceBundle.getBundle(bundleBaseName, locale, bundleClassLoader);
		return  new TpResourceBundle(messages, portletInfo);
	}

	@Override
	public String getInitParameter(String name) {
		if (name == null) throw new IllegalArgumentException();
		return (String) this.initParameter.get(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		Set<String> set;
		if (this.initParameter == null) set = new HashSet<String>();
		else set = this.initParameter.keySet();
		return Collections.enumeration(set);

	}

	/* Getter & Setter */

	public void setBundleBaseName(String bundleBaseName, ClassLoader portalClassloader) {
		/*
		 * Si le portlet ne déclare pas de bundle, on utilise celui du portail
		 */
		//TODO gérer la lecture de Constant.portal_bundle depuis un fichier properties
		if (this.bundleBaseName == null) {
			this.bundleBaseName = Constant.portal_bundle;
			ClassLoader defaultLoader = portalClassloader;
			this.bundleClassLoader = defaultLoader;
		} else {
			this.bundleBaseName = bundleBaseName;
			this.bundleClassLoader = this.loader;
		}
		

	}

	public void setPortletInfo(Map<String, String> portletInfo) {
		this.portletInfo = portletInfo;
	}

	public void setPortletContext( TpPortletContext portletContext) {
		this.portletContext = portletContext;
		this.loader = portletContext.getServletContext().getClassLoader();
	}

	public List<PortletMode> getPortletModes() {
		return portletMode;
	}

	public void setPortletModes(List<PortletMode> portletMode) {
		this.portletMode = portletMode;
	}

	public List<String> getMimeType() {
		return MimeType;
	}

	public void setMimeType(List<String> mimeType) {
		MimeType = mimeType;
	}

	public List<WindowState> getWindowsStates() {
		return windowState;
	}

	public void setWindowStates(List<WindowState> windowState) {
		this.windowState = windowState;
	}

	/* Gestion des informations de correspondances de groupes */
	public Map<String, String> getSecurity() {
		return security;
	}

	public void setSecurity(Map<String, String> security) {
		this.security = security;
	}

	/* */
	public void addSecurity(String roleName, String roleLink) {
		this.security.put(roleName, roleLink);
	}

	public void setPorletName(String porletName) {
		this.porletName = porletName;
	}

	public TpPortletPreference getPortletPreference() {
		return portletPreference;
	}

	public void setPortletPreference(TpPortletPreference portletPreference) {
		this.portletPreference = portletPreference;
	}

	public String getPortletClass() {
		return portletClass;
	}

	public void setPortletClass(String portletClass) {
		this.portletClass = portletClass;
	}

}

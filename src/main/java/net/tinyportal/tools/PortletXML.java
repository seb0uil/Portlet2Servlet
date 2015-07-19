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

package net.tinyportal.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import net.tinyportal.javax.portlet.TpPortletConfig;
import net.tinyportal.javax.portlet.TpPortletPreference;

import org.jdom.Element;
import org.jdom.Namespace;

public class PortletXML{
	/**
	 * Nom du portlet
	 */
	private String PortletName;

	/**
	 * Titre du portlet
	 */
	private Map<String, String> portletInfo = new HashMap<String, String>();

	/**
	 * Classe du portlet
	 */
	private String PortletClass;

	/**
	 * Liste des prÃ©fÃ©rences dÃ©clarÃ©es dans le portlet.xml
	 */
	private Map<String, String[]> portletPreference = new HashMap<String, String[]>();

	/**
	 * Map contenant les Ã©quivalences pour les groupes
	 */
	private Map<String, String> security = new HashMap<String, String>();

	public Map<String, String> getSecurity() {
		return security;
	}

	private List<String> readOnlyPreferences = new ArrayList<String>();

	/**
	 * Liste des mode supportÃ©s par le portlet
	 */
	private List<PortletMode> portletMode = new ArrayList<PortletMode>();

	/**
	 * Liste des types Mime supportÃ©s par le portlet
	 */
	private List<String> MimeType = new ArrayList<String>();

	/**
	 * Liste des windowState supportÃ©s par le portlet
	 */
	private List<WindowState> windowState = new ArrayList<WindowState>();

	private String bundle;

//	private File portletRealPath;

	private Map<String, String> initParameters = new HashMap<String, String>();


	public TpPortletConfig getPortletConfig() {
		TpPortletConfig portletConfig = new TpPortletConfig();
		portletConfig.setSecurity(security);
		portletConfig.setBundleBaseName(bundle);
		portletConfig.setMimeType(MimeType);
		portletConfig.setPortletInfo(portletInfo);
		portletConfig.setPortletModes(portletMode);
		portletConfig.setWindowStates(windowState);
		portletConfig.setPorletName(PortletName);
		TpPortletPreference p = new TpPortletPreference(portletPreference,readOnlyPreferences);
		portletConfig.setPortletPreference(p);
		portletConfig.setPortletClass(PortletClass);
		return portletConfig;
	}

	public PortletXML(Element portletElement, Namespace portletNS){
		try {
			//portlet_jp_ActionRequest_web/CheckIsPortletModeAllowedTestPortlet
			portletMode.add(PortletMode.VIEW);

			//portlet_jp_ActionRequest_web/CheckIsWindowStateAllowedTestPortlet
			windowState.add(WindowState.NORMAL);
			windowState.add(WindowState.MINIMIZED);
			windowState.add(WindowState.MAXIMIZED);

//			this.portletRealPath = portletRealPath;

			Element portletNameElement = portletElement.getChild("portlet-name",portletNS);
			PortletName = portletNameElement.getValue();

			Element portletClassElement = portletElement.getChild("portlet-class",portletNS);
			PortletClass = portletClassElement.getValue();

			Element portletInfoElements = portletElement.getChild("portlet-info",portletNS);
			if (portletInfoElements != null) {
				List<Element> portletInfoElement = portletInfoElements.getChildren();
				for (Element infoElements : portletInfoElement) {
					portletInfo.put(infoElements.getName(), infoElements.getText());
				}
			}

			List<Element> portletInitParamElements = portletElement.getChildren("init-param",portletNS);
			for (Element portletInitParamElement : portletInitParamElements) {
				String name = portletInitParamElement.getChild("name", portletNS).getText();
				String value = portletInitParamElement.getChild("value", portletNS).getText();
				initParameters.put(name, value);
			}

			/*
			 * Gestion des prÃ©fÃ©rences de portlet
			 */
			Element portletPreferencesElement = portletElement.getChild("portlet-preferences",portletNS);
			if(portletPreferencesElement!=null) {
				List<Element> preferencesElement = portletPreferencesElement.getChildren("preference",portletNS);
				if (preferencesElement!=null)
					for (Element preferenceElement : preferencesElement) {
						List<String> prefValues = new ArrayList<String>();
						String prefName = null;
						for (int i=0; i<preferenceElement.getChildren().size(); i++) {
							String name  = ((Element)preferenceElement.getChildren().get(i)).getName();
							String value = ((Element)preferenceElement.getChildren().get(i)).getValue();
							if ("value".equalsIgnoreCase(name))
								prefValues.add(value);
							else if ("name".equalsIgnoreCase(name))
								prefName = value;
							else if ("read-only".equalsIgnoreCase(name))	/* on l'ajoute le cas Ã©chÃ©ant Ã  la liste readOnly*/
								if ("true".equalsIgnoreCase(value))
									readOnlyPreferences.add(name);
						}
						portletPreference.put(prefName, prefValues.toArray(new String[0]));
					}
			}
			Element portletModesElement = portletElement.getChild("supports",portletNS);
			if (portletModesElement != null) {
				List<Element> ModesElement = portletModesElement.getChildren("portlet-mode",portletNS);
				for (Element ModeElement : ModesElement) {
					String prefName = ModeElement.getText();
					portletMode.add(new PortletMode(prefName));
				}

				List<Element> MimesElement = portletModesElement.getChildren("mime-type",portletNS);
				for (Element MimeElement : MimesElement) {
					String prefName = MimeElement.getText();
					MimeType.add(prefName);
				}
			}

			Element portletBundleElement = portletElement.getChild("resource-bundle",portletNS);
			if (portletBundleElement!=null)
				this.bundle = portletBundleElement.getText();

			String name;
			String link;
			List<Element> portletSecurityElements = portletElement.getChildren("security-role-ref",portletNS);
			for(Element portletSecurityElement : portletSecurityElements) {
				name = null;
				link = null;
				Element portletSecurityNameElement = portletSecurityElement.getChild("role-name",portletNS);
				if (portletSecurityNameElement!=null)
					name = portletSecurityNameElement.getText();

				Element portletSecurityLinkElement = portletSecurityElement.getChild("role-link",portletNS);
				if (portletSecurityLinkElement!=null)
					link = portletSecurityLinkElement.getText();

				if (name!=null & link!=null) {
					security.put(name, link);
				}
			}


		}
		catch(Exception e){
			e.printStackTrace();
		}	      
	}

	public String getPortletTitle() {
		return portletInfo.get("title");
	}

	public String getBundle() {
		return bundle;
	}

	public Map<String, String[]> getPortletPreference() {
		return portletPreference;
	}

	public String getPortletName() {
		return PortletName;
	}

	public String getPortletClass() {
		return PortletClass;
	}

	public List<PortletMode> getPortletModes() {
		return this.portletMode;
	}

	public List<WindowState> getWindowsStates() {
		return this.windowState;
	}

	public List<String> getReadOnlyPreferences() {
		return this.readOnlyPreferences;
	}

	public List<String> getMimeType() {
		return MimeType;
	}

	public Map<String, String> getInitParameters() {
		return initParameters;
	}

	public Map<String, String> getPortletInfo() {
		return portletInfo;
	}

//	public File getPortletRealPath() {
//		return portletRealPath;
//	}
//
//	public String getPortletPath() {
//		return portletRealPath.getAbsolutePath().substring(
//				portletRealPath.getParentFile().getParentFile().getParent().length()
//				).replaceAll("\\\\", "/");
//	}
}
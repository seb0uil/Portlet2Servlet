package net.tinyportal.service.portlet;

import java.util.HashMap;
import java.util.Map;

import net.tinyportal.javax.portlet.TpPortletConfig;

public class PortletPool {

	private Map<String, Map<String, TpPortletConfig>> portletConfigs = new HashMap<String, Map<String, TpPortletConfig>>();//	private Map<String, Map<String, TpPortletPreference>> portletPreferences = new HashMap<String, Map<String, TpPortletPreference>>();

	public void addPortletConfig(String portletContextName, String portletName, TpPortletConfig portletConfig) {
		Map<String, TpPortletConfig> portletsConfig;
		if (portletConfigs.containsKey(portletContextName)) {
			portletsConfig = portletConfigs.get(portletContextName);
		} else {
			portletsConfig = new HashMap<String, TpPortletConfig>();
			portletConfigs.put(portletContextName, portletsConfig);
		}
		portletsConfig.put(portletName, portletConfig);
	}

	public TpPortletConfig getPortletConfig(String portletContext, String portletName) {
		Map<String, TpPortletConfig> portletsConfig;
		if (portletConfigs.containsKey(portletContext)) {
			portletsConfig = portletConfigs.get(portletContext);
			return portletsConfig.get(portletName);
		}
		return null;
	}

	public boolean hasPortletContext(String portletContext) {
		return portletConfigs.containsKey(portletContext);
	}
	
	public boolean hasPortlet(String portletContext, String portletName) {
		return (portletConfigs.containsKey(portletContext) && portletConfigs.get(portletContext).containsKey(portletName));
	}
}

package net.tinyportal.service.portlet;

import java.util.HashMap;
import java.util.Map;

import net.tinyportal.javax.portlet.TpPortletConfig;

public class PortletPool {

	Map<String, Map<String, TpPortletConfig>> pool = new HashMap<String, Map<String, TpPortletConfig>>();

	public void addPortletConfig(String portletContextName, String portletName, TpPortletConfig portletConfig) {
		Map<String, TpPortletConfig> portletsConfig;
		if (pool.containsKey(portletContextName)) {
			portletsConfig = pool.get(portletContextName);
		} else {
			portletsConfig = new HashMap<String, TpPortletConfig>();
			pool.put(portletContextName, portletsConfig);
		}
		portletsConfig.put(portletName, portletConfig);
	}

	public TpPortletConfig getPortletConfig(String portletContextName, String portletName) {
		Map<String, TpPortletConfig> portletsConfig;
		if (pool.containsKey(portletContextName)) {
			portletsConfig = pool.get(portletContextName);
			return portletsConfig.get(portletName);
		}
		return null;
	}

	boolean hasPortletContext(String portletContext) {
		return pool.containsKey(portletContext);
	}
}

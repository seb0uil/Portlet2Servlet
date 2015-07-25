package net.tinyportal.service;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.Portlet;

/**
 * Exemple de service
 * @author seb0uil
 *
 */
public class ServicePortlet extends Service<Portlet> {

	/**
	 * 
	 */
	static Map<String,Service<Portlet>> servicePortlets = new HashMap<String, Service<Portlet>>();
	
	/**
	 * Récupération du singleton
	 * @return L'interface Test
	 */
	synchronized public static Portlet getInstance(String portletInstance) {
		if (!servicePortlets.containsKey(portletInstance)) {
			ServicePortlet servicePortlet = new ServicePortlet();
			servicePortlet.setClass(Portlet.class, portletInstance);
			servicePortlets.put(portletInstance, servicePortlet);
		}
		return (Portlet) servicePortlets.get(portletInstance).getService();
	}
	
}

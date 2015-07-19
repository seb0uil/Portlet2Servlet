package net.tinyportal.service;

import javax.portlet.Portlet;

/**
 * Exemple de service
 * @author seb0uil
 *
 */
public class ServicePortlet extends Service<Portlet> {
	
	/**
	 * Nom du service implémenté
	 */
	public static final String PORTLET = "portlet";

	/**
	 * 
	 */
	static Service servicePortlet;
	
	/**
	 * Récupération du singleton
	 * @return L'interface Test
	 */
	synchronized public static Portlet getInstance() {
		if (servicePortlet == null) {
			servicePortlet = new ServicePortlet();
			servicePortlet.setClass(Portlet.class, PORTLET);
		}
		return (Portlet) servicePortlet.getService();
	}
	
	/**
	 * Réinitialise le singleton, nécessaire afin de changer l'implémentation
	 * de celui-ci
	 */
	public static void init() {
		servicePortlet = null;
	}
}

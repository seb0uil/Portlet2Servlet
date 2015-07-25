package net.tinyportal.service.portlet;

public class PortletTools {

	/**
	 * Retourne un nom unique du portlet en fonction des éléments passés en paramètre
	 * @param context ServletContext dans lequel se trouve le portlet
	 * @param name Nom du portlet tel que définie dans le portlet.xml avec la balise <code>portlet-name</code>
	 * @param instance Nom de l'instance du portlet
	 * @return le nom complet du portlet
	 */
	public String getPortletFullName(String context, String name, String instance) {
		return context.concat(name).concat("@").concat(instance).replace("/", "_");
	}
}

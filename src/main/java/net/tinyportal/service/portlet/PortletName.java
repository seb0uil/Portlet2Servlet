package net.tinyportal.service.portlet;

public class PortletName {

	/**
	 * ServletContext dans lequel se trouve le portlet
	 */
	private final String context;

	/**
	 * Nom du portlet tel que définie dans le portlet.xml avec la balise <code>portlet-name</code>
	 */
	private final String name;
	
	/**
	 * Nom de l'instance du portlet
	 */
	private final String instance;
	
	/**
	 * Constructeur du bean
	 * @param context ServletContext dans lequel se trouve le portlet
	 * @param name Nom du portlet tel que définie dans le portlet.xml avec la balise <code>portlet-name</code>
	 * @param instance Nom de l'instance du portlet
	 */
	public PortletName(String context, String name, String instance) {
		this.context = context;
		this.name = name;
		this.instance = instance;
	}

	/* Getter */
	public String getContext() {
		return context;
	}

	public String getName() {
		return name;
	}

	public String getInstance() {
		return instance;
	}
}

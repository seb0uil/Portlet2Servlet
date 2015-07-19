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

package net.tinyportal;

import net.tinyportal.javax.portlet.TpPortalContext;

public class Constant {

	/**
	 * Chemin du fichier de propriété du portail
	 */
	public static final String PORTAL_PROPERTY = "/portal.properties";
	
	/**
	 * Répertoire de dépot des portlets, relatif depuis
	 * le répertoire de la webapp
	 */
	static public final String portlets_path = "/WEB-INF/portlets";
	
	/**
	 * Chemin du fichier portlet.xml relatif par rapport au répertoire
	 * du portlet
	 */
	static public final String portlet_xml = "/WEB-INF/portlet.xml";
	
	/**
	 * Chemin du répertoire de classes relatif par rapport au répertoire
	 * du portlet
	 */
	static public final String portlet_classes = "/WEB-INF/classes/";
	
	/**
	 * Chemin du répertoire de librairie relatif par rapport au répertoire
	 * du portlet
	 */
	static public final String portlet_lib = "/WEB-INF/lib/";
	
	/**
	 * extension des fichiers .jar
	 */
	static public final String jar_extension = ".jar";

	/**
	 * variable de session utilisée pour stocker le 
	 * portletHolder en cours de traitement
	 */
	static public final String session_portlet_bean = "net.tinyportal.portlet";

	/**
	 * variable de session utilisée pour stocker la map
	 * des portletHolder affichable par la page
	 */
	static public final String session_portlet_bean_map = "net.tinyportal.map.portlets";

	/**
	 * Context du portail
	 */
	static public final TpPortalContext portal_context = new TpPortalContext();
	
	/**
	 * clé du fichier de propriété du portail référencant le bundle du portail
	 */
	static public final String portal_bundle = "net.tinyportal.bundle";
	
	/**
	 * clé du fichier de propriété du portail référencant la version du portail
	 */
	static public final String portal_version = "net.tinyportal.portal.version";
	
	/**
	 * Prefixe normalisé pour gérer les objets en session
	 */
	static public final String javax_portlet_session = "javax.portlet.p.";
	
}
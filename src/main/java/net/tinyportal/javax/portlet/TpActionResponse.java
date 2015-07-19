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
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;

public class TpActionResponse extends TpPortletResponse implements ActionResponse {



	@Autowired
	private HttpServletRequest request;

	Map<String, String[]> parameters = new HashMap<String, String[]>();

	private boolean isRedirectAllowed = true;
	private boolean isRedirected = false;
	private String redirection;

	@Override
	public void setWindowState(WindowState windowState)
			throws WindowStateException {
		if (!isRedirectAllowed) throw new IllegalStateException();
		isRedirectAllowed=false; //on interdit la redirection cf spec. portlet
	}

	@Override
	public void setPortletMode(PortletMode portletMode)
			throws PortletModeException {
		if (!isRedirectAllowed) throw new IllegalStateException();
		isRedirectAllowed=false; //on interdit la redirection cf spec. portlet
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		if (!location.contains(":") && !location.startsWith("/"))
			throw new IllegalArgumentException();
		if (!isRedirectAllowed) throw new IllegalStateException();

		isRedirectAllowed=false;
		isRedirected = true;
		redirection = location;
	}

	@Override
	public void setRenderParameters(Map parameters) {
		if (parameters==null) throw new IllegalArgumentException();
		isRedirectAllowed=false; //on interdit la redirection cf spec. portlet
		this.parameters = parameters;
	}

	@Override
	public void setRenderParameter(String key, String value) {
		if (key== null) throw new IllegalArgumentException();
		isRedirectAllowed=false; //on interdit la redirection cf spec. portlet
		String[] values = {value};
		setRenderParameter(key, values);
	}

	@Override
	public void setRenderParameter(String key, String[] values) {
		if (key== null) throw new IllegalArgumentException();
		isRedirectAllowed=false; //on interdit la redirection cf spec. portlet
		this.parameters.put(key, values);
	}

	/**
	 * Récupère la map des paramètres de rendu
	 * positionnés pour cette réponse
	 * @return Paramètres de rendu positionnés
	 */
	public Map<String, String[]> getRenderParametersMap() {
		return this.parameters;
	}

	/**
	 * Indique si une redirection a été positionnée
	 * @return Vrai si une redirection est positionnée
	 */
	public boolean isRedirected() {
		return isRedirected;
	}

	/**
	 * Annule la redirection en cours
	 */
	public void cancelRedirected() {
		isRedirected = false;
		redirection = null;
	}

	/**
	 * Retourne la valeur positionnée pour la redirection
	 * @return la valeur de la redirection
	 */
	public String getRedirection() {
		return redirection;
	}

}

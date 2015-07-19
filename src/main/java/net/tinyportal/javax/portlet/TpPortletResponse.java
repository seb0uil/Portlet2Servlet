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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class TpPortletResponse implements PortletResponse {

	@Autowired
	private HttpServletResponse httpServletResponse;

	private Map<String, List<String>> property = new HashMap<String, List<String>>();
		
	/**
	 * Ajoute une valeur à une liste déjà existante,
	 * on crée la liste si elle n'existe pas
	 */
	@Override
	public void addProperty(String key, String value) {
		if (property.containsKey(key)) {
			List<String> lValue = property.get(key);
			lValue.add(value);
			property.put(key, lValue);			
		} else {
			setProperty(key, value);
		}
	}

	/**
	 * Positionne une valeur en remplacement de
	 * celles déjà existante pour la clé correspondante
	 */
	@Override
	public void setProperty(String key, String value) {
		List<String> lValue = new ArrayList<String>();
		lValue.add(value);
		property.put(key, lValue);
	}
	
	@Override
	public String encodeURL(String path) {
		return this.httpServletResponse.encodeURL(path);
	}

	/* Getter & Setter */
	public HttpServletResponse getHttpServletResponse() {
		return httpServletResponse;
	}

}

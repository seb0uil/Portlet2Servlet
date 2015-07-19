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

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

public class TpResourceBundle extends ResourceBundle {

	ResourceBundle message;
	String default_title;
	Map<String, String> portletInfo;
	static final StringBuffer javax_portlet = new StringBuffer("javax.portlet.");

	public TpResourceBundle(ResourceBundle message, Map<String, String> portletInfo) {
		this.message = message;
		this.portletInfo = portletInfo;
	}

	@Override
	protected Object handleGetObject(String key) {
		if (message.containsKey(key)) return message.getObject(key);
		else 
			{
			String key_to_find = key.substring(javax_portlet.length());
			if (portletInfo.containsKey(key_to_find))
				return (portletInfo.get(key_to_find)==null)?"":portletInfo.get(key_to_find);
			else
				return "";
			}
	}

	@Override
	public Enumeration<String> getKeys() {
		return message.getKeys();
	}
}

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

public class TpPortletURL implements PortletURL {

	private WindowState windowState = null;
	private PortletMode portletMode = null;
	private Map<String, String[]> parameters = new HashMap<String, String[]>();
	private boolean secure = false;
	private boolean useSecure = false;
	private String portletId;
	private String action;
	private String prefix;

	public TpPortletURL(String prefix, String portletId, String action) {
		this.portletId = portletId;
		this.action = action;
		this.prefix = prefix;
	}

	@Override
	public void setWindowState(WindowState windowState) throws WindowStateException {
		this.windowState = windowState;
	}

	@Override
	public void setPortletMode(PortletMode portletMode) throws PortletModeException {
		this.portletMode = portletMode;
	}

	@Override
	public void setParameter(String name, String value) {
		this.setParameter(name, new String[]{value});
	}

	@Override
	public void setParameter(String name, String[] values) {
		this.parameters.put(name, values);		
	}
	public void addParameter(String name, String[] values) {
		this.setParameter(name, values);
	}


	@Override
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	@Override
	public void setSecure(boolean secure) throws PortletSecurityException {
		this.useSecure = true;
		this.secure = secure;		
	}

	public String toString () {
		StringBuffer sb = new StringBuffer(prefix + "?");
		if (this.windowState != null) {
			sb.append(this.portletId).append("_").append("state").append("=");
			sb.append(this.windowState.toString()).append("&");
		}
		if (this.portletMode != null) {
			sb.append(this.portletId).append("_").append("mode").append("=");
			sb.append(this.portletMode.toString()).append("&");
		}

		if (this.useSecure) {
			sb.append(this.portletId).append("_").append("secure").append("=");
			sb.append(this.secure).append("&");
		}

		sb.append(this.portletId).append("_").append("action").append("=");
		sb.append(this.action).append("&");

		String key = null;
		//		String[] value = null;
		for (Iterator<String> i = parameters.keySet().iterator() ; i.hasNext() ; ){
			key = i.next();
			for (String value : parameters.get(key)) {
				sb.append(this.portletId).append("_").append("param").append("_").append(key).append("=");
				sb.append(value).append("&");
			}
		}
		return sb.toString();		 
	}
}

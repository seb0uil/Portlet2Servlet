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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

public class TpPortletPreference implements PortletPreferences, Cloneable {
	
	private Map<String, String[]> portletPreference = new HashMap<String, String[]>();
	private Map<String, String[]> portletPreferenceDefaultValue = new HashMap<String, String[]>();
	private List<String> readOnlyPreferences;

	public TpPortletPreference( Map<String, String[]> portletPreference, List<String> readOnlyPreferences) {
		this.portletPreference = new HashMap<String, String[]>(portletPreference);
		this.portletPreferenceDefaultValue = new HashMap<String, String[]>(portletPreference);
		this.readOnlyPreferences = readOnlyPreferences;
	}

	@Override
	public boolean isReadOnly(String key) {
		return readOnlyPreferences.contains(key);
	}

	@Override
	public String getValue(String key, String def) {
		if (portletPreference.containsKey(key))
			return portletPreference.get(key)[0];
		return def;
	}

	@Override
	public String[] getValues(String key, String[] def) {
		if (portletPreference.containsKey(key))
			return portletPreference.get(key);
		return def;
	}

	@Override
	public void setValue(String key, String value) throws ReadOnlyException {
		if (key != null) {
			if (readOnlyPreferences.contains(key)) throw new ReadOnlyException("Values not writable");
			portletPreference.put(key, new String[] {value});
		}
	}

	@Override
	public void setValues(String key, String[] values) throws ReadOnlyException {
		if (key != null) {
			if (readOnlyPreferences.contains(key)) throw new ReadOnlyException("Values not writable");
			portletPreference.put(key, values);
		}
	}

	@Override
	public Enumeration getNames() {
		Vector<String> v  = new Vector<String>(); 
		Iterator<String> it = portletPreference.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (portletPreference.get(key).length!=0)
				v.add(key);
		}
		return v.elements();
	}

	@Override
	public Map getMap() {
		return portletPreference;
	}

	@Override
	public void reset(String key) throws ReadOnlyException {
		if (portletPreferenceDefaultValue.containsKey(key)) {
			portletPreference.put(key, 
					portletPreferenceDefaultValue.get(key)
					);
		} else {
			portletPreference.remove(key);
		}
	}

	@Override
	public void store() throws IOException, ValidatorException {
		//TODO
	}

}

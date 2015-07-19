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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class TpPortletContext implements PortletContext, ServletContextAware  {
//	@Autowired
	ServletContext servletContext;
		
	Map<String, Object> attribute = new HashMap<String, Object>();
	Properties initParameter = new Properties();

//	String portletPath;
	String portletName;
	
	public TpPortletContext() {}

    public void setServletContext(ServletContext context) {
    	this.servletContext = context;
    }
    
	public ServletContext getServletContext() {
		return servletContext;
	}

//	/**
//	 * Positionne le chemin du portlet
//	 * @param portletPath Chemin relatif du portlet
//	 */
//	public void setPortletPath(String portletPath) {
//		File f = new File(servletContext.getRealPath(portletPath));
//		if (!f.isDirectory()) {
//			
//		}
////		File contextPath = new File(servletContext.getRealPath("/"));
////		this.portletPath = f.toString().substring(contextPath.toString().length()).replaceAll("\\\\", "/");
//	}

	/**
	 * Positionne le nom du portlet
	 * @param portletName
	 */
	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}


    @Override
	public PortletRequestDispatcher getRequestDispatcher(String path) {
		return new TpPortletRequestDispatcher(servletContext.getRequestDispatcher(path));
	}
	
	@Override
	public PortletRequestDispatcher getNamedDispatcher(String name) {
		return new TpPortletRequestDispatcher(servletContext.getNamedDispatcher(name));
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return servletContext.getResourceAsStream(path);
		
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

    @Override
	public String getServerInfo() {
		return servletContext.getServerInfo();
	}
    
	@Override
	public String getMimeType(String file) {
		return servletContext.getMimeType(file);
	}

	@Override
	public String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	@Override
	public Set getResourcePaths(String path) {
		return servletContext.getResourcePaths(path);
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return servletContext.getResource(path);
	}

	@Override
	public Object getAttribute(String name) {
		return attribute.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attribute.keySet());
	}

	@Override
	public String getInitParameter(String name) {
		return (String)initParameter.getProperty(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return initParameter.elements();
	}

	@Override
	public void log(String msg) {
		servletContext.log(msg);		
	}

	@Override
	public void log(String message, Throwable throwable) {
		servletContext.log(message, throwable);
	}

	@Override
	public void removeAttribute(String name) {
		attribute.remove(name);
	}

	@Override
	public void setAttribute(String name, Object object) {
		attribute.put(name, object);
	}

	@Override
	public String getPortletContextName() {
		return portletName;
	}
	
	

}

package net.tinyportal.tools;

import java.io.BufferedReader;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.tinyportal.javax.portlet.TpRenderRequest;

public class TpHttpServletRequest extends HttpServletRequestWrapper{

	private TpRenderRequest renderRequest;

	public TpHttpServletRequest(HttpServletRequest request, TpRenderRequest renderRequest) {
		super(request);
		this.renderRequest = renderRequest;
	}

	/* Correction pour retourner ce qui est attendu dans le cas d'un portlet
	 * cf. spec. p67
	 */
	/* The following methods of the HttpServletRequest must return null : 
	 * getProtocol, getRemoteAddr, getRemoteHost, getRealPath, and getRequestURL 
	 */
	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public String getRealPath(String path) {
		return null;
	}

	@Override
	public StringBuffer getRequestURL() {
		return null;
	}


	/*
	 * The following methods of the HttpServletRequest must be equivalent to the methods 
	 * of the PortletRequest of similar name: 
	 * getScheme, getServerName, getServerPort, getAttribute, getAttributeNames,
	 * setAttribute, removeAttribute, getLocale, getLocales, isSecure, getAuthType, 
	 * getContextPath, getRemoteUser, getUserPrincipal, getRequestedSessionId, isRequestedSessionIdValid.
	 */
	//TODO
	

	/* The following methods of the HttpServletRequest must be equivalent to the methods 
	 * of the PortletRequest of similar name: 
	 * getScheme,   getServerName, getServerPort, getAttribute, getAttributeNames, 
	 * setAttribute, removeAttribute, getLocale, getLocales, isSecure, getAuthType, 
	 * getContextPath, getRemoteUser, getUserPrincipal, getRequestedSessionId, isRequestedSessionIdValid.	
	 */
	@Override
	public String getScheme() {
		return this.renderRequest.getScheme();
	}

	@Override
	public String getServerName() {
		return this.renderRequest.getServerName();
	}

	@Override
	public int getServerPort() {
		return this.renderRequest.getServerPort();
	}

	@Override
	public Object getAttribute(String name) {
		return this.renderRequest.getAttribute(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return this.renderRequest.getAttributeNames();
	}

	@Override
	public void setAttribute(String name, Object o) {
		this.renderRequest.setAttribute(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		this.renderRequest.removeAttribute(name);
	}

	@Override
	public Locale getLocale() {
		return this.renderRequest.getLocale();
	}

	@Override
	public Enumeration getLocales() {
		return this.renderRequest.getLocales();
	}

	@Override
	public boolean isSecure() {
		return this.renderRequest.isSecure();
	}

	@Override
	public String getAuthType() {
		return this.renderRequest.getAuthType();
	}

	@Override
	public String getContextPath() {
		return this.renderRequest.getContextPath();
	}

	@Override
	public String getRemoteUser() {
		return this.renderRequest.getRemoteUser();
	}

	@Override
	public Principal getUserPrincipal() {
		return this.renderRequest.getUserPrincipal();
	}

	@Override
	public String getRequestedSessionId() {
		return this.renderRequest.getRequestedSessionId();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return this.renderRequest.isRequestedSessionIdValid();
	}


	/*
	 * The following methods of the HttpServletRequest must be equivalent to the methods of the 
	 * PortletRequest of similar name with the provision defined in 
	 * PLT.16.1.1 Query Strings in Request Dispatcher Paths Section: 
	 * getParameter, getParameterNames, getParameterValues and getParameterMap.
	 */
	@Override
	public String getParameter(String name) {
		return this.renderRequest.getParameter(name);
	}

	@Override
	public Enumeration getParameterNames() {
		return this.renderRequest.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.renderRequest.getParameterValues(name);
	}

	@Override
	public Map getParameterMap() {
		return this.renderRequest.getParameterMap();
	}

	/*
	 * The following methods of the HttpServletRequest must do no operations and return null : 
	 * getCharacterEncoding, setCharacterEncoding, getContentType, getInputStream and getReader.
	 * The getContentLength method of the HttpServletRequest must return 0
	 */
	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public void setCharacterEncoding(String enc) {
		//
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public ServletInputStream getInputStream() {
		return null;
	}

	@Override
	public BufferedReader getReader() {
		return null;
	}

	@Override
	public int getContentLength() {
		return 0;
	}

	/*
	 * The following methods of the HttpServletRequest must be based on the properties 
	 * provided by the getProperties method of the PortletRequest interface: 
	 * getHeader, getHeaders, getHeaderNames, getCookies, getDateHeader and getIntHeader.
	 */
	//TODO

	/*
	 * The getMethod method of the HttpServletRequest must always return ‘GET’
	 */
	@Override
	public String getMethod() {
		return "GET";
	}
}

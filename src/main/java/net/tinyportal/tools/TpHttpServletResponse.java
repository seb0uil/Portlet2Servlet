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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.tinyportal.javax.portlet.TpRenderResponse;

public class TpHttpServletResponse extends HttpServletResponseWrapper {
	private StringWriter replacementWriter;
	private TpRenderResponse renderResponse;


	public TpHttpServletResponse(HttpServletResponse response, TpRenderResponse tpRenderResponse) {
		super(response);
		replacementWriter = new StringWriter();
		this.renderResponse = tpRenderResponse;
	}

	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(replacementWriter);
	}

	public String toString() {
		return replacementWriter.toString();
	}

	/* Correction pour retourner ce qui est attendu dans le cas d'un portlet
	 * cf. spec. p67
	 */

	/*
	 * The following methods of the HttpServletResponse must return null: 
	 * encodeRedirectURL and encodeRedirectUrl.
	 */
	@Override
	public String encodeRedirectURL(String url) {
		return null;
	}
	
	@Override
	public String encodeRedirectUrl(String url) {
		return null;
	}
	
	/* The following methods of the HttpServletResponse must be equivalent 
	 * to the methods of the RenderResponse of similar name: 
	 * getCharacterEncoding, setBufferSize, flushBuffer, resetBuffer, reset, 
	 * getBufferSize, isCommitted, getOutputStream, getWriter, encodeURL and encodeUrl
	 */
	@Override
	public String getCharacterEncoding() {
		return this.renderResponse.getCharacterEncoding();
	}
	
	@Override
	public void setBufferSize(int size) {
		this.renderResponse.setBufferSize(size);
	}
	
	@Override
	public void flushBuffer() throws IOException {
		this.renderResponse.flushBuffer();
	}
	
	@Override
	public void reset() {
		this.renderResponse.reset();
	}
	
	@Override
	public int getBufferSize() {
		return this.renderResponse.getBufferSize();
	}
	
	@Override
	public boolean isCommitted() {
		return this.renderResponse.isCommitted();
	}
	
	//TODO
//	@Override
//	public ServletOutputStream getOutputStream() {		
//		return this.tpRenderResponse.getPortletOutputStream();
//	}
	
	@Override
	public String encodeURL (String path) {
		return this.renderResponse.encodeURL(path);
	}
	
	/*
	 * The following methods of the HttpServletResponse must perform no operations: 
	 * setContentType, setContentLength, setLocale, addCookie, sendError, sendRedirect, 
	 * setDateHeader, addDateHeader, setHeader, addHeader, setIntHeader, addIntHeader and setStatus. 
	 * The containsHeader method of the HttpServletResponse must return false
	 */
	@Override
	public void setContentType(String type) {
		//
	}
	
	@Override
	public void setContentLength(int len) {
		//
	}
	
	@Override
	public void setLocale(Locale loc) {
		//
	}
	
	@Override
	public void addCookie(Cookie cookie) {
		//
	}
	
	@Override
	public void sendError(int sc) {
		//
	}
	
	@Override
	public void sendRedirect(String location) {
		//
	}
	
	@Override
    public void setDateHeader(String name, long date) {
		//
	}
	
	@Override
    public void addDateHeader(String name, long date) {
		//
	}
	
	@Override
    public void setHeader(String name, String value) {
		//
	}
	
	@Override
    public void addHeader(String name, String value) {
		//
	}
		
	@Override
    public void setIntHeader(String name, int value) {
		//
	}
	
	@Override
    public void addIntHeader(String name, int value) {
		//
	}
	
	@Override
    public void setStatus(int sc) {
		//
	}
	
	@Override
    public void setStatus(int sc, String sm) {
		//
	}
	
	@Override
    public boolean containsHeader(String name) {
		return false;
	}
	
	/*
	 * The getLocale method of the HttpServletResponse must be based on the getLocale method of the RenderResponse.
	 */
	@Override
    public Locale getLocale() {
		return renderResponse.getLocale();
	}
}

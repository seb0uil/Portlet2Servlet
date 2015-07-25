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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;

import net.tinyportal.tools.CommitBufferedOutputStream;
import net.tinyportal.tools.TpPrintWriter;

public class TpRenderResponse extends TpPortletResponse implements RenderResponse{

	/**
	 * Titre du portlet
	 */
	private String title = null;

	/**
	 * type de la réponse
	 */
	private String contentType = null;

	private CommitBufferedOutputStream portletStream = new CommitBufferedOutputStream(new ByteArrayOutputStream());
	private TpPrintWriter printWriter = new TpPrintWriter(portletStream);
	
	/**
	 * Dispatcher du portlet
	 */
	private RequestDispatcher dispatcher = null;

	/*
	 * On ne peut pas lancer getWriter et getPortletOutputStream
	 * c'est l'un ou l'autre
	 */
	/**
	 * Flag pour indiquer si getWriter a été utilisé
	 */
	private boolean flagGetWriter = false;
	
	/**
	 * Flag pour indiquer si getStream a été utilisé
	 */
	private boolean flagGetStream = false;

	public TpRenderResponse() {
		ByteArrayOutputStream portletStream_ = new ByteArrayOutputStream();
		portletStream = new CommitBufferedOutputStream(portletStream_);
		printWriter = new TpPrintWriter(portletStream);
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public PortletURL createRenderURL() {
		//TODO
		String prefix = httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" +  httpRequest.getServerPort() + httpRequest.getServletContext().getContextPath() + httpRequest.getServletPath();
		return new TpPortletURL(prefix, getPortletFullName(), "render");
	}

	@Override
	public PortletURL createActionURL() {
		//TODO
		String prefix = httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" +  httpRequest.getServerPort() + httpRequest.getServletContext().getContextPath() + httpRequest.getServletPath();
		return new TpPortletURL(prefix, getPortletFullName(), "action");
	}

	@Override
	public String getNamespace() {
		return this.toString();
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public void setContentType(String type) throws IllegalArgumentException {
		if (type.length()==1 | type.indexOf('/')==-1) 
			throw new IllegalArgumentException();
		else {
			this.contentType = type;
			getHttpServletResponse().setContentType(type);
		}
	}

	@Override
	public String getCharacterEncoding() {
		return getHttpServletResponse().getCharacterEncoding();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (contentType == null) throw new IllegalStateException();
		if (flagGetStream) throw new IllegalStateException();
		flagGetWriter= true;
		return this.printWriter;
	}

	@Override
	public Locale getLocale() {
		return getHttpServletResponse().getLocale();
	}

	@Override
	public void setBufferSize(int size) {
			portletStream.setBufferSize(size);
	}

	@Override
	public int getBufferSize() {
		return portletStream.getBufferSize();
	}

	@Override
	public void flushBuffer() throws IOException {
		portletStream.flush();
	}

	@Override
	public void resetBuffer() {
		if (isCommitted()) throw new IllegalStateException();
		portletStream = new CommitBufferedOutputStream(new ByteArrayOutputStream());
		printWriter = new TpPrintWriter(portletStream);
	}

	@Override
	public boolean isCommitted() {
		return portletStream.isCommitted();
	}

	@Override
	public void reset() {
		if (isCommitted()) throw new IllegalStateException();
		resetBuffer();
	}

	/**
	 * On retourne portletStream, si l'on a bien défini
	 * le type Mime, et si l'on a pas déjà récupérer
	 * le getWriter
	 */
	@Override
	public OutputStream getPortletOutputStream() throws IOException {
		if (contentType == null) throw new IllegalStateException();
		if (flagGetWriter) throw new IllegalStateException();
		flagGetStream = true;
		return this.portletStream;
	}

	public String getStringContent() throws IOException {
		if (flagGetStream) {
			this.portletStream.flush();
			return this.portletStream.toString();
		} else
			return this.printWriter.getOutputStream().toString()	;
	}

	public RequestDispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(RequestDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

}

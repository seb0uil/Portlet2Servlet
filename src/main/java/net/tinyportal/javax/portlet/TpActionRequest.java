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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.portlet.ActionRequest;

import org.apache.log4j.Logger;

public class TpActionRequest extends TpPortletRequest implements ActionRequest {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(TpActionRequest.class);
	
	/**
	 * Flag indiquant si la méthode getReader a été appelé<br/>
	 * Cela permet de lever un exception lors de le récupération
	 * de l'InputStream si nécessaire
	 */
	private boolean flagGetReader=false;
	
	/**
	 * Flag indiquant si la méthode getPortletInputStream a été appelé<br/>
	 * Cela permet de lever un exception lors de le récupération
	 * du BufferedReader si nécessaire
	 */
	private boolean flagInputStream=false;
	
	@Override
	public InputStream getPortletInputStream() throws IOException {
		if (flagGetReader) throw new IllegalStateException();
		flagInputStream=true;
		return httpRequest.getInputStream();
	}

	@Override
	public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
		if (flagGetReader) throw new IllegalStateException();
		httpRequest.setCharacterEncoding(enc);		
	}

	@Override
	public BufferedReader getReader() throws UnsupportedEncodingException, IOException {
		if (flagInputStream) throw new IllegalStateException();
		flagGetReader = true;
		return httpRequest.getReader();
	}

	@Override
	public String getCharacterEncoding() {
		return httpRequest.getCharacterEncoding();
	}

	@Override
	public String getContentType() {
		return httpRequest.getContentType();
	}

	@Override
	public int getContentLength() {
		return httpRequest.getContentLength();
	}

}

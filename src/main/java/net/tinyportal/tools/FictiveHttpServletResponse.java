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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.tinyportal.javax.portlet.TpRenderResponse;

public class FictiveHttpServletResponse extends HttpServletResponseWrapper {
	private StringWriter replacementWriter;

 
	public FictiveHttpServletResponse(HttpServletResponse response, TpRenderResponse tpRenderResponse) {
	    super(response);
	    replacementWriter = new StringWriter();
	  }
	 
	  public PrintWriter getWriter() throws IOException {
	    return new PrintWriter(replacementWriter);
	  }
	 
	  public String toString() {
	    return replacementWriter.toString();
	  }
}

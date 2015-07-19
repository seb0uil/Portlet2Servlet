package net.tinyportal.service.portlet;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;

import net.tinyportal.Constant;
import net.tinyportal.javax.portlet.TpPortletConfig;
import net.tinyportal.javax.portlet.TpPortletContext;

import org.springframework.beans.factory.annotation.Autowired;

public class PortletLoader {

	@Autowired
	PortletXMLReader portletXMLReader;
	
	@Autowired
	PortletPool portletPool;

	public void load(ServletContext portletContext) {
		if (!portletPool.hasPortletContext(portletContext.getContextPath())) {
			
			InputStream portletXml = portletContext.getResourceAsStream(Constant.portlet_xml);

			List<TpPortletConfig> portletsConfig = portletXMLReader.reader(portletXml);

			for (TpPortletConfig portletConfig : portletsConfig) {
				TpPortletContext tpPortletContext = new TpPortletContext();
				tpPortletContext.setServletContext(portletContext);
				tpPortletContext.setPortletName(portletConfig.getPortletName());
				portletConfig.setPortletContext(tpPortletContext);
				portletPool.addPortletConfig(portletContext.getContextPath(), portletConfig.getPortletName(), portletConfig);
			}

		}
	}
}
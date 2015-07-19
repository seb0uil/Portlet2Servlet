package net.tinyportal.controller;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tinyportal.javax.portlet.TpPortletConfig;
import net.tinyportal.javax.portlet.TpRenderRequest;
import net.tinyportal.javax.portlet.TpRenderResponse;
import net.tinyportal.service.Service;
import net.tinyportal.service.ServicePortlet;
import net.tinyportal.service.portlet.PortletLoader;
import net.tinyportal.service.portlet.PortletPool;
import net.tinyportal.tools.FictiveHttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("test")
@RequestMapping(value = "/")
public class TestController {

	@Autowired
	TpRenderRequest portletRequest;
	
	@Autowired
	TpRenderResponse portletResponse;
	
	@Autowired
	PortletLoader portletLoader;
	
	@Autowired
	PortletPool portletPool;
	
	   @RequestMapping(method = RequestMethod.GET)
	   public ModelAndView handleRenderRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			ServletContext portletContext = request.getServletContext().getContext("/samplePortlet-0.0.1-SNAPSHOT");
			portletLoader.load(portletContext);
			TpPortletConfig portletConfig = portletPool.getPortletConfig(portletContext.getContextPath(), "samplePortlet");

			Service.addService(ServicePortlet.PORTLET, portletConfig.getPortletClass(), portletContext);
			Portlet p = ServicePortlet.getInstance();

			
			p.init((PortletConfig)portletConfig);
			p.render((RenderRequest)portletRequest, (RenderResponse)portletResponse);

			RequestDispatcher servletDispatcher = portletResponse.getDispatcher();
			HttpServletResponse newResponse = new FictiveHttpServletResponse((HttpServletResponse) response, portletResponse);
			servletDispatcher.include(request, newResponse);
			System.out.println(newResponse.toString());
			
		return new ModelAndView("/test", null);
	}
}

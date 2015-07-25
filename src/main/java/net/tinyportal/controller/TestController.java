package net.tinyportal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tinyportal.service.portlet.PortletExecutor;
import net.tinyportal.service.portlet.PortletName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("test")
@RequestMapping(value = "/")
public class TestController {

	@Autowired
	PortletExecutor portletExecutor;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRenderRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		String portletName = "samplePortlet";
		String portletContext = "/samplePortlet-0.0.1-SNAPSHOT";

		PortletName portlet1 = new PortletName(portletContext, portletName, "node1");
		PortletName portlet2 = new PortletName(portletContext, portletName, "node2");
		PortletName[] names = {portlet1, portlet2};
		
		Map<String, String> render = portletExecutor.executePortlets(names);
		
		model.put("content1", render.get("node1"));
		model.put("content2", render.get("node2"));
		return new ModelAndView("/test", model);
	}
}

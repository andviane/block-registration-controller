package com.smartvalor.hl.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Redirects the requests from the context root.
 */
@Controller
@RequestMapping(value = "/")
public class RootRedirectController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectRoot() {
		return "redirect:/user/";
	}

}

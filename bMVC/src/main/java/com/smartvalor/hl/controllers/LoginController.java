package com.smartvalor.hl.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartvalor.db.Persistence;

@Controller
@PropertySource("classpath:application.properties")
@ComponentScan(basePackageClasses = { Persistence.class })
@Transactional
@RequestMapping(value = "/user")
public class LoginController {

	private static final Logger logger = Logger.getLogger(LoginController.class);

	@ExceptionHandler(Exception.class)
	public String handleError(HttpServletRequest req, Exception exception) {
		logger.error("Request: " + req.getRequestURL() + " raised  exception", exception);
		return "login";
	}

	/**
	 * Simple mappings for login, logout and failed.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) throws IOException {
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model, HttpServletRequest request) throws IOException {
		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().invalidate();
		logger.info("Session invalidated");
		return "login";
	}

	@RequestMapping(value = "/sorry", method = RequestMethod.GET)
	public String files(Model model) throws IOException {
		return "sorry";
	}

}

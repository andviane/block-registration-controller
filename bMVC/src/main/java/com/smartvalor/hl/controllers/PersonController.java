package com.smartvalor.hl.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartvalor.db.Address;
import com.smartvalor.db.Persistence;
import com.smartvalor.db.Person;
import com.smartvalor.db.PersonRepository;

@Controller
@PropertySource("classpath:application.properties")
@ComponentScan(basePackageClasses = { Persistence.class, PersonRepository.class })
@Transactional
@RequestMapping(value = "/user")
public class PersonController {

	private static final Logger logger = Logger.getLogger(PersonController.class);

	@Autowired
	protected PersonRepository persons;

	@Autowired
	protected HttpSession session;

	/**
	 * List of persons
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, @RequestParam(value = "showOld", required = false) Boolean showOld)
			throws IOException, URISyntaxException {
		if (showOld != null) {
			session.setAttribute("showOld", showOld);
		}
		return listPersons(model);
	}

	/**
	 * List of persons
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String update(Locale locale, Model model, @RequestParam(value = "old", required = false) UUID[] old,
			@RequestParam(value = "all", required = false) UUID[] all) throws IOException, URISyntaxException {
		if (all != null && old != null) {
			HashSet<UUID> hOld = new HashSet<>(Arrays.asList(old));
			for (UUID id : all) {
				Person e = persons.findOne(id);
				if (e != null) {
					if (hOld.contains(id) != e.isReviewed()) {
						e.setReviewed(hOld.contains(id));
						persons.save(e);
					}
				}
			}
		}
		persons.flush();
		return listPersons(model);
	}

	private String listPersons(Model model) {
		Boolean showOld = (Boolean) session.getAttribute("showOld");
		if (showOld == null) {
			showOld = Boolean.FALSE;
		}

		List<Person> exp;
		if (showOld) {
			exp = persons.findAll();
		} else {
			exp = persons.findByReviewed(Boolean.FALSE);
		}
		
		model.addAttribute("showOld", showOld);
		model.addAttribute("persons", exp);
		model.addAttribute("total_persons", exp.size());
		return "persons";
	}

	/**
	 * List of files for persons
	 */
	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	public String files(Locale locale, @PathVariable String id, Model model, HttpServletRequest request)
			throws IOException, URISyntaxException {
		Person person = persons.getOne(UUID.fromString(id));
		String acc = request.getParameter("acc");
		if (acc != null && person != null) {
			person.setReviewed("true".equals(acc));
			persons.save(person);
		}
		model.addAttribute("person_id", id);
		if (person == null) {
			return "not_found";
		}
		
		logger.info("Addr "+person.getAddress().size());
		for (Address a: person.getAddress()) {
			logger.info(a.getCountry()+" "+a.getCity());
		}
		
		model.addAttribute("person", person);
		return "person_details";
	}

}

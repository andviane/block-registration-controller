package com.smartvalor.hl.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartvalor.db.Account;
import com.smartvalor.db.AccountRepository;
import com.smartvalor.db.Contract;
import com.smartvalor.db.ContractRepository;
import com.smartvalor.db.Persistence;

@Controller
@PropertySource("classpath:application.properties")
@ComponentScan(basePackageClasses = { Persistence.class, ContractRepository.class })
@Transactional
@RequestMapping(value = "/user")
public class ContractController {

	private static final Logger logger = Logger.getLogger(ContractController.class);

	@Autowired
	protected ContractRepository contracts;

	@Autowired
	protected AccountRepository files;

	@Autowired
	protected HttpSession session;

	/**
	 * List of contracts
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, @RequestParam(value = "showOld", required = false) Boolean showOld)
			throws IOException, URISyntaxException {
		if (showOld != null) {
			session.setAttribute("showOld", showOld);
		}
		return listContracts(model);
	}

	/**
	 * List of contracts
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String update(Locale locale, Model model, @RequestParam(value = "old", required = false) UUID[] old,
			@RequestParam(value = "all", required = false) UUID[] all) throws IOException, URISyntaxException {
		if (all != null && old !=null) {
			HashSet<UUID> hOld = new HashSet<>(Arrays.asList(old));
			for (UUID id : all) {
				Contract e = contracts.findOne(id);
				if (e != null) {
					if (hOld.contains(id) != e.isReviewed()) {
						e.setReviewed(hOld.contains(id));
						contracts.save(e);
					}
				}
			}
		}
		contracts.flush();
		return listContracts(model);
	}

	private String listContracts(Model model) {
		Boolean showOld = (Boolean) session.getAttribute("showOld");
		if (showOld == null) {
			showOld = Boolean.FALSE;
		}

		List<Contract> exp;
		if (showOld) {
			exp = contracts.findAll();
		} else {
			exp = contracts.findByReviewed(Boolean.FALSE);
		}

		ArrayList<ContractRecord> list = new ArrayList<>();
		for (Contract e : exp) {
			list.add(new ContractRecord(e));
		}
		
		// Sort most recent first.
		Collections.sort(list, new Comparator<ContractRecord>() {

			@Override
			public int compare(ContractRecord o1, ContractRecord o2) {
				Contract e1 = o1.getContract();
				Contract e2 = o2.getContract();
				return - e1.getLastUpdated().compareTo(e2.getLastUpdated());
			}
		});

		model.addAttribute("showOld", showOld);
		model.addAttribute("contracts", list);
		model.addAttribute("total_contracts", list.size());
		return "contracts";
	}

	/**
	 * List of files for contracts
	 */
	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	public String files(Locale locale, @PathVariable String id, Model model, HttpServletRequest request)
			throws IOException, URISyntaxException {
		Contract contract = contracts.getOne(UUID.fromString(id));
		String acc = request.getParameter("acc");
		if (acc != null && contract != null) {
			contract.setReviewed("true".equals(acc));
			contracts.save(contract);
		}
		model.addAttribute("contract_id", id);
		if (contract == null) {
			return "not_found";
		}
		addContractDetails(model, contract, false);
		return "contract_details_accounts";
	}

	private void addContractDetails(Model model, Contract contract, boolean keysRequired) {
		model.addAttribute("contract", new ContractRecord(contract));

		ArrayList<AccountRecord> records = new ArrayList<>();
		for (Account account : contract.getAccounts()) {
			AccountRecord f = new AccountRecord(account);
			records.add(f);
		}

		Collections.sort(records, new Comparator<AccountRecord>() {

			@Override
			public int compare(AccountRecord o1, AccountRecord o2) {
				return - Long.compare(o1.account.getAmount(), o2.account.getAmount());
			}
		});

		model.addAttribute("total_accounts", records.size());
		model.addAttribute("accounts", records);
	}

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

package com.smartvalor.hl.controllers;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartvalor.db.Account;
import com.smartvalor.db.Contract;
import com.smartvalor.db.ContractRepository;
import com.smartvalor.db.Persistence;
import com.smartvalor.hl.logic.Registrar;
import com.smartvalor.hl.logic.UniqueUUIDGenerator;
import com.smartvalor.json.rq.RegistrationRq;
import com.smartvalor.json.rs.RegistrationRs;

@Controller
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:/smartValor.properties")})
@RequestMapping(value = "/api", produces = "application/json")
@ComponentScan(basePackageClasses = { Persistence.class, ContractRepository.class, Registrar.class })
@Transactional
public class JsonController {
	private static final Logger logger = Logger.getLogger(JsonController.class);

	@Autowired
	protected Registrar registrar;
	
	@Autowired
	protected UniqueUUIDGenerator uuids;
	
	@Value("${api.token}")
	protected String token;

	// Convert to JSON for logging all requests.
	private final ObjectMapper json = new ObjectMapper();

	@RequestMapping(value = "/register", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> addContract(@RequestBody RegistrationRq registration) {
		RegistrationRs response = new RegistrationRs();
		if (!token.equals(registration.getToken())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		try {
			logger.info("Registration: " + json.writeValueAsString(registration));
			// TODO No field validator so far

			Contract contract = new Contract();

			contract.setTitle(registration.getTitle());
			contract.setAddress(registration.getAddress());
			contract.seteMail(registration.geteMail());
			contract.setIpAddress(registration.getIpAddress());
			contract.setPhone(registration.getPhone());
			contract.setDescription(registration.getDescription());

			contract.setLastUpdated(contract.getCreated());
			contract.setCreated(now());

			// It is probably not time to create the account now, we create two
			// anyway in this prototype to test.
			Set<Account> accounts = new HashSet<>();

			Account chfAccount = new Account();
			chfAccount.setContract(contract);

			chfAccount.setAmount(0);
			chfAccount.setCurrency("CHF");
			
			// We can use any algorithm to name the accounts.
			chfAccount.setNo(uuids.nextUUID().toString());

			Account ccAccount = new Account();
			ccAccount.setContract(contract);

			ccAccount.setAmount(0);
			ccAccount.setCurrency("XBT"); // Bitcoin code

			accounts.add(chfAccount);
			accounts.add(ccAccount);

			contract.setAccounts(accounts);

			registrar.storeContract(contract);

			response.setContractNo(contract.getNo());
		} catch (Exception e) {
            logger.error("Failed to register", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}

		return new ResponseEntity<RegistrationRs>(response, HttpStatus.OK);
	}

	// We may need to mock this method for tests.
	protected Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * Produce the sample registration request.
	 */
	public static void main(String [] args) throws Exception {
		RegistrationRq r = new RegistrationRq();
		
		r.setToken("123456"); // not this!
		r.setAddress("CH-8117, Unterdorfwaeg 11, Fallanden, Switzerland");
		r.setDescription("My first registration");
		r.seteMail("me@myserver.com");
		r.setIpAddress("100.200.300.400");
		r.setPhone("+41762614348");
		r.setTitle("Dr John Doe");
		
		ObjectMapper json = new ObjectMapper();
		
		System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(r));
	}

}

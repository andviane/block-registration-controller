package com.smartvalor.hl.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.smartvalor.db.Persistence;
import com.smartvalor.db.Person;
import com.smartvalor.db.PersonRepository;
import com.smartvalor.hl.logic.Registrar;
import com.smartvalor.hl.logic.UniqueUUIDGenerator;
import com.smartvalor.json.Address;
import com.smartvalor.json.rq.BusinessRegistrationRq;
import com.smartvalor.json.rq.PersonRegistrationRq;
import com.smartvalor.json.rs.RegistrationRs;

@Controller
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:/smartValor.properties")})
@RequestMapping(value = "/api", produces = "application/json")
@ComponentScan(basePackageClasses = { Persistence.class, PersonRepository.class, Registrar.class })
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
	
	@RequestMapping(value = "/registerBusiness", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> registerBusiness(@RequestBody BusinessRegistrationRq registration) {
		RegistrationRs response = new RegistrationRs();
		if (!token.equals(registration.getToken())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		try {
			logger.info("Registration: " + json.writeValueAsString(registration));
			
            // TODO not implemented yet.
			response.setRegisteredEntityNo("NOT IMPLEMENTED");
			
		} catch (Exception e) {
            logger.error("Failed to register", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}		
		
		return new ResponseEntity<RegistrationRs>(response, HttpStatus.OK);		
	}	
	
	@RequestMapping(value = "/registerPerson", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> registerPerson(@RequestBody PersonRegistrationRq registration) {
		RegistrationRs response = new RegistrationRs();
		if (!token.equals(registration.getToken())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		try {
			logger.info("Registration: " + json.writeValueAsString(registration));
			
			Person person = new Person();
			
			person.setBirthDate(parseDate(registration.getBirthDate()));
			person.setFullName(registration.getFullName());
			person.setIdNumber(registration.getIdNumber());
			person.setIdType(registration.getIdType());
			person.setNationality(registration.getNationality());
			person.setPlaceOfBirth(registration.getPlaceOfBirth());
			
			Address ra = registration.getAddress();
			com.smartvalor.db.Address a = new com.smartvalor.db.Address();

			a.setCountry(registration.getCountry());			
			a.setCity(ra.getCity());
			a.setEmail(ra.getEmail());
			a.setHouseNumber(ra.getHouseNumber());
			a.setPhone(ra.getPhone());
			a.setStreetName(ra.getStreetName());
			a.setZipCode(ra.getZipCode());			
			
			person.getAddress().add(a);
			
			registrar.storePerson(person);
			
			response.setRegisteredEntityNo(person.getNo());			
		} catch (Exception e) {
            logger.error("Failed to register", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}

		return new ResponseEntity<RegistrationRs>(response, HttpStatus.OK);
	}			
			

	private Timestamp parseDate(String birthDate) throws ParseException {
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return new Timestamp(simpleDateFormat.parse(birthDate).getTime());
	}


	// We may need to mock this method for tests.
	protected Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * Produce the sample registration request.
	 */
	public static void main(String [] args) throws Exception {
		PersonRegistrationRq r = new PersonRegistrationRq();		
		r.setToken("123456"); // not this!
		
		Address addr = new Address();
		addr.setCity("Faellanden");
		addr.setHouseNumber("24A");
		addr.setStreetName("Unterdorfwaeg");
		addr.setZipCode("CH-8117");
		addr.setEmail("me@myServer");
		addr.setPhone("+4123456789");
		
		r.setAddress(addr);
		
		r.setFullName("John Doe");
		r.setBirthDate("04.10.1968");
		r.setNationality("Swiss");
		r.setPlaceOfBirth("Zurich");
		r.setIdType("passport");
		r.setIdNumber("L1234567896");
		
		ObjectMapper json = new ObjectMapper();
		
		System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(r));
	}

}

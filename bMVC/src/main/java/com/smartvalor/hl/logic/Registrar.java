package com.smartvalor.hl.logic;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartvalor.db.Address;
import com.smartvalor.db.AddressRepository;
import com.smartvalor.db.Person;
import com.smartvalor.db.PersonRepository;

@Service
@Transactional
@PropertySource("classpath:/smartValor.properties") // can also be file
public class Registrar {
	private static final Logger logger = Logger.getLogger(Registrar.class);
	
	@Autowired
	PersonRepository persons;
	
	@Autowired
	AddressRepository addresses;
	
	@Autowired
	UniqueUUIDGenerator uuids;
	
	@Autowired
	EntityManager entityManager;
	
	/**
	 * Store person. 
	 * 
	 * @param person
	 */
	public void storePerson(Person person) {
		Person existing = person.getId() == null ? null: persons.findOne(person.getId());
		
		if (existing != null) {

			if (person.getAddress() != null) {
				// Delete addresses that are no longer in the person's list.
				Set<Address> newAddresses = new LinkedHashSet<>(person.getAddress());
				for (Address a : existing.getAddress()) {
					if (!newAddresses.contains(a)) {
						addresses.delete(a);
					}
				}
				
				// Save or save overwriting existing addresses.
				for (Address a: person.getAddress()) {
					a.setPerson(person);
					mergeAndSave(a);
				}
				existing.setAddress(newAddresses);
			}
		} else {
			// new
			person.setNo(toString(uuids.nextUUID()));
			
			UUID uuid = uuids.nextUUID();
			person.setId(uuid);
			
			persons.save(person);
			for (Address a : person.getAddress()) {
				a.setId(uuids.nextUUID());
				a.setPerson(person);
				addresses.save(a);
			}
		}
	}	

	private void mergeAndSave(Address f) {
		Address existing = f.getId() == null ? null: addresses.findOne(f.getId());

		if (existing != null) {
			existing.setUpdated(now());
			
			existing.setStreetName(f.getStreetName());
			existing.setHouseNumber(f.getHouseNumber());			
			existing.setCity(f.getCity());
			existing.setCountry(f.getCountry());
			existing.setEmail(f.getEmail());
			existing.setPhone(f.getPhone());
		} else {
			f.setCreated(now());
			f.setUpdated(f.getCreated());
			
			UUID uuid = uuids.nextUUID();
			f.setId(uuid);
		}
		addresses.save(f);		
	}

	private String toString(UUID uuid) {
		String n = "SM-"+Long.toString(Math.abs(uuid.getMostSignificantBits()), Character.MAX_RADIX)+'.'+
				Long.toString(Math.abs(uuid.getLeastSignificantBits()), Character.MAX_RADIX);
		return n.toUpperCase();
	}

	// We may need to mock this method for tests.
	protected Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
}
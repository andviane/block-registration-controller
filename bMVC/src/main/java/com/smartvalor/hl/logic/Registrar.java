package com.smartvalor.hl.logic;

import java.sql.Timestamp;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartvalor.db.Account;
import com.smartvalor.db.AccountRepository;
import com.smartvalor.db.Contract;
import com.smartvalor.db.ContractRepository;

@Service
@Transactional
@PropertySource("classpath:/smartValor.properties") // can also be file
public class Registrar {
	private static final Logger LOG = Logger.getLogger(Registrar.class);

	@Autowired
	protected ContractRepository contracts;

	@Autowired
	protected AccountRepository accounts;
	
	@Autowired
	UniqueUUIDGenerator uuids;
	
	// Conceptual - pick value from configuration
	@Value("${contract.default.type}")
	protected String defaultContractType;

	public void storeContract(Contract e) {
		LOG.debug("Saving " + e);

		mergeAndSave(e);
		
		accounts.flush();
		contracts.flush();
	}

	// Some fields may be immutable, others may require logic to merge.
	private void mergeAndSave(Contract ew) {
		Contract existing = ew.getId() == null ? null: contracts.findOne(ew.getId());
		if (existing != null) {
			existing.setDeleted(ew.isDeleted());
			existing.setReviewed(ew.isReviewed());
			existing.setPhone(ew.getPhone());
			existing.setDescription(ew.getDescription());
			existing.seteMail(ew.geteMail());
			existing.setAddress(ew.getAddress());
			existing.setNo(ew.getNo());
			existing.setTitle(ew.getTitle());
			existing.setType(defaultContractType);

			if (ew.getAccounts() != null) {
				for (Account a : ew.getAccounts()) {
					mergeAndSave(a);
				}

				existing.getAccounts().addAll(ew.getAccounts());
			}
		} else {
			// new
			ew.setCreated(now());
			ew.setLastUpdated(ew.getCreated());
			
			UUID uuid = uuids.nextUUID();
			ew.setId(uuid);
			ew.setNo(toString(uuid));
			
			contracts.save(ew);
			
			if (ew.getAccounts() != null) {
				for (Account a : ew.getAccounts()) {
					mergeAndSave(a);
				}
			}
		}
	}

	// Some fields may be immutable, others may require logic to merge.
	private void mergeAndSave(Account f) {
		Account existing = f.getId() == null ? null: accounts.findOne(f.getId());

		if (existing != null) {
			existing.setLastProcessed(now());
		} else {
			f.setCreated(now());
			f.setLastProcessed(f.getCreated());
			
			UUID uuid = uuids.nextUUID();
			f.setId(uuid);
			f.setNo(toString(uuid));
			accounts.save(f);
		}
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
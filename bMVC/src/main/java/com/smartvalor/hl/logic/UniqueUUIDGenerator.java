package com.smartvalor.hl.logic;

import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Generated random unique UUIDs. 
 */
@Service
public class UniqueUUIDGenerator {
	private SecureRandom random = new SecureRandom();
	
	/**
	 * Node number, only the lowest 2 bytes taken into consideration.
	 */
	@Value("${node.number}")	
	protected long node;
	
	// UUIDs generated on different machines should never overlap. We reserve 2 bytes that allow 65536 nodes.
	// We do not need to follow UUID standard syntax as UUIDs are strictly internal.
	public UUID nextUUID() {
		long msb = random.nextLong();
		
		// Set the lowest 2 bytes to the node value.
		msb = (msb & ~(0x0FFFF) | node);
        
        return new UUID(msb, random.nextLong());		
	}

}

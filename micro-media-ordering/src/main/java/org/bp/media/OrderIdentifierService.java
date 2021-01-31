package org.bp.media;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class OrderIdentifierService {

	public String getOrderIdentifier() {
		return UUID.randomUUID().toString();
	}

}
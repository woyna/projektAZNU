package org.bp.types;

import java.math.BigDecimal;

public class TVService {

	private String provider;
	private TVType type;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public TVType getType() {
		return type;
	}

	public void setType(TVType type) {
		this.type = type;
	}

	public BigDecimal calcCost() {
		switch (type) {
			case CABLE:
				return new BigDecimal(400);
			case SATELLITE:
				return new BigDecimal(250);
			case TERRESTRIAL:
				return new BigDecimal(150);
			default:
				return null;
		}
	}
}

package org.bp.types;

import java.math.BigDecimal;

public class InternetService {

	private String provider;
	private InternetBroadband broadband;
	private int speed;
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public InternetBroadband getBroadband() {
		return broadband;
	}
	public void setBroadband(InternetBroadband broadband) {
		this.broadband = broadband;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public BigDecimal calcCost() {
		int cost = 0;
		
		switch (broadband) {
			case ADSL:
				cost += 400;
			case FIBRE_OPTIC:
				cost += 600;
			case CABLE:
				cost += 250;
			case MOBILE:
				cost += 100;
		}
		
		if (speed < 100) {
			cost += 50;
		}
		else if (speed < 300) {
			cost += 100;
		}
		else {
			cost += 150;
		}
		
		return new BigDecimal(cost);
	}
}

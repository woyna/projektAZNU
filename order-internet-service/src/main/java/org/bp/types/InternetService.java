package org.bp.types;


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
	
}

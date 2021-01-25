package org.bp.types;

import javax.xml.datatype.XMLGregorianCalendar;

public class InternetOrder {
	
	private Household household;
	private InternetService internetService;
	private XMLGregorianCalendar installmentDate;
	
	public Household getHousehold() {
		return household;
	}
	public void setHousehold(Household household) {
		this.household = household;
	}
	public InternetService getInternetService() {
		return internetService;
	}
	public void setInternetService(InternetService internetService) {
		this.internetService = internetService;
	}
	public XMLGregorianCalendar getInstallmentDate() {
		return installmentDate;
	}
	public void setInstallmentDate(XMLGregorianCalendar installmentDate) {
		this.installmentDate = installmentDate;
	}

}

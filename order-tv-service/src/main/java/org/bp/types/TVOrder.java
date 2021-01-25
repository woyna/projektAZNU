package org.bp.types;

import javax.xml.datatype.XMLGregorianCalendar;

public class TVOrder {
	
	private Household household;
	private TVService tvService;
	private XMLGregorianCalendar installmentDate;
	
	public Household getHousehold() {
		return household;
	}
	public void setHousehold(Household household) {
		this.household = household;
	}
	public TVService getTVService() {
		return tvService;
	}
	public void setTVService(TVService tvService) {
		this.tvService = tvService;
	}
	public XMLGregorianCalendar getInstallmentDate() {
		return installmentDate;
	}
	public void setInstallmentDate(XMLGregorianCalendar installmentDate) {
		this.installmentDate = installmentDate;
	}

}

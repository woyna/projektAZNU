package org.bp.mediaorderingui.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Amount {
	private BigDecimal value;
	private String currency;

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Amount {\n");
	    
	    sb.append("    value: ").append(value.toString()).append("\n");
	    sb.append("    currency: ").append(currency).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

}

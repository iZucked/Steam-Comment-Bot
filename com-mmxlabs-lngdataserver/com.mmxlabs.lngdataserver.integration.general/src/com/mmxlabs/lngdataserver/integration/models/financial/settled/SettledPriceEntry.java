package com.mmxlabs.lngdataserver.integration.models.financial.settled;

import java.time.LocalDate;

public class SettledPriceEntry {

	private LocalDate date;
	private double value;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}

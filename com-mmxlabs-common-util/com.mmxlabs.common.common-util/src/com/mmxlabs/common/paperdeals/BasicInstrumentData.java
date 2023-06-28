/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.paperdeals;

/**
 * Basic data equivalent of SettleStrategy
 * @author FM
 *
 */
public class BasicInstrumentData {
	private String name;
	private int dayOfTheMonth;
	private boolean lastDayOfTheMonth;
	
	private BasicInstrumentPeriod pricingPeriod;
	private BasicInstrumentPeriod hedgingPeriod;
	
	public BasicInstrumentData(String name, int dayOfTheMonth, boolean lastDayOfTheMonth, //
			final BasicInstrumentPeriod pricingPeriod, final BasicInstrumentPeriod hedgingPeriod) {
		this.name = name;
		this.dayOfTheMonth = dayOfTheMonth;
		this.lastDayOfTheMonth = lastDayOfTheMonth;
		this.pricingPeriod = pricingPeriod;
		this.hedgingPeriod = hedgingPeriod;
	}

	public String getName() {
		return name;
	}

	public int getDayOfTheMonth() {
		return dayOfTheMonth;
	}

	public boolean isLastDayOfTheMonth() {
		return lastDayOfTheMonth;
	}
	
	public BasicInstrumentPeriod getPricingPeriod() {
		return pricingPeriod;
	}
	
	public BasicInstrumentPeriod getHedgingPeriod() {
		return hedgingPeriod;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
	private int settlePeriod;
	private String settlePeriodUnit;
	private int settleStartMonthsPrior;
	
	public BasicInstrumentData(String name, int dayOfTheMonth, boolean lastDayOfTheMonth, int settlePeriod, String settlePeriodUnit, int settleStartMonthsPrior) {
		super();
		this.name = name;
		this.dayOfTheMonth = dayOfTheMonth;
		this.lastDayOfTheMonth = lastDayOfTheMonth;
		this.settlePeriod = settlePeriod;
		this.settlePeriodUnit = settlePeriodUnit;
		this.settleStartMonthsPrior = settleStartMonthsPrior;
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

	public int getSettlePeriod() {
		return settlePeriod;
	}

	public String getSettlePeriodUnit() {
		return settlePeriodUnit;
	}

	public int getSettleStartMonthsPrior() {
		return settleStartMonthsPrior;
	}
}

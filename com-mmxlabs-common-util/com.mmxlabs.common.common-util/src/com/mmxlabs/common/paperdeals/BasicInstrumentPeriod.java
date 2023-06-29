/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.paperdeals;

public class BasicInstrumentPeriod {
	
	private int period;
	private String unit;
	private int offsetMonth;
	
	public BasicInstrumentPeriod(int period, String unit, int offsetMonth) {
		this.period = period;
		this.unit = unit;
		this.offsetMonth = offsetMonth;
	}

	public int getPeriod() {
		return period;
	}

	public String getUnit() {
		return unit;
	}

	public int getOffsetMonth() {
		return offsetMonth;
	}
	
}

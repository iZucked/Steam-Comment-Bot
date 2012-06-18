/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.annotations.impl;

import java.util.Collection;

import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossEntry;

/**
 * @author hinton
 * 
 */
public class ProfitAndLossAnnotation implements IProfitAndLossAnnotation {
	private final int bookingTime;
	private final Collection<IProfitAndLossEntry> entries;

	public ProfitAndLossAnnotation(final int bookingTime, final Collection<IProfitAndLossEntry> entries) {
		super();
		this.bookingTime = bookingTime;
		this.entries = entries;
	}

	@Override
	public int getBookingTime() {
		return bookingTime;
	}

	@Override
	public Collection<IProfitAndLossEntry> getEntries() {
		return entries;
	}
}

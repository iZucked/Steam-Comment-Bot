/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;

/**
 * A P&L annotation which goes in an {@link IAnnotatedSolution}. Each of these guys corresponds to a single P&L entry.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public interface IProfitAndLossAnnotation {
	/**
	 * Returns the time at which this profit/loss bundle is booked
	 */
	int getBookingTime();

	/**
	 * Returns a bunch of {@link IProfitAndLossEntry} instances, each pertaining to a single entity involved in this transaction.
	 * 
	 * @return
	 */
	Collection<IProfitAndLossEntry> getEntries();
}

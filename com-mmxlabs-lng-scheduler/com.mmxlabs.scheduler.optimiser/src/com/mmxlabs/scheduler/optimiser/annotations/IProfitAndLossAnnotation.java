/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotation;

/**
 * A P&L annotation which goes in an {@link IAnnotatedSolution}. Each of these guys corresponds to a single P&L entry.
 * 
 * @author hinton
 * 
 */
public interface IProfitAndLossAnnotation extends IElementAnnotation {
	/**
	 * Returns a bunch of {@link IProfitAndLossEntry} instances, each pertaining to a single entity involved in this transaction.
	 * 
	 * @return
	 */
	Collection<IProfitAndLossEntry> getEntries();
}

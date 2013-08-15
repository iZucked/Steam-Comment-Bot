/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import java.util.Collection;

import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;

/**
 * @author hinton
 * @since 2.0
 * 
 */
public class ProfitAndLossAnnotation implements IProfitAndLossAnnotation {
	private final Collection<IProfitAndLossEntry> entries;

	public ProfitAndLossAnnotation(final Collection<IProfitAndLossEntry> entries) {
		this.entries = entries;
	}

	@Override
	public Collection<IProfitAndLossEntry> getEntries() {
		return entries;
	}
}

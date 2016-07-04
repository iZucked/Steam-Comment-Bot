/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;

/**
 * A list item for {@link IProfitAndLossAnnotation} storing the details for a specific {@link IEntityBook}.
 * 
 */
public class ProfitAndLossEntry implements IProfitAndLossEntry {
	private final IEntityBook entityBook;
	private final long groupValue;
	private final long groupValuePreTax;
	private final IDetailTree details;

	/**
	 */
	public ProfitAndLossEntry(final IEntityBook entityBook, final long groupValue, final long groupValuePreTax, final IDetailTree details) {
		super();
		this.entityBook = entityBook;
		this.groupValue = groupValue;
		this.groupValuePreTax = groupValuePreTax;
		this.details = details;
	}

	@Override
	public IEntityBook getEntityBook() {
		return entityBook;
	}

	@Override
	public long getFinalGroupValue() {
		return groupValue;
	}

	/**
	 */
	@Override
	public long getFinalGroupValuePreTax() {
		return groupValuePreTax;
	}

	@Override
	public IDetailTree getDetails() {
		return details;
	}
}

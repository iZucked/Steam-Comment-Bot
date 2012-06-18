/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.annotations.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.trading.optimiser.IEntity;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossEntry;

/**
 * @author hinton
 * 
 */
public class ProfitAndLossEntry implements IProfitAndLossEntry {
	private final IEntity entity;
	private final long groupValue;
	private final IDetailTree details;

	public ProfitAndLossEntry(final IEntity entity, final long groupValue, final IDetailTree details) {
		super();
		this.entity = entity;
		this.groupValue = groupValue;
		this.details = details;
	}

	@Override
	public IEntity getEntity() {
		return entity;
	}

	@Override
	public long getFinalGroupValue() {
		return groupValue;
	}

	@Override
	public IDetailTree getDetails() {
		return details;
	}
}

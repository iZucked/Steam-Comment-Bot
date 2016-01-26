/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.filters;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SlotVisit;

/**
 * Filter to filter out events which are not associated with one of a specified list of contracts.
 * 
 * @author mmxlabs
 * 
 */
public class ContractEventFilter extends FieldEventFilter<Contract> {
	public ContractEventFilter(@Nullable final EventFilter filter, @NonNull final List<Contract> values) {
		super(filter, values);
	}

	public ContractEventFilter(@NonNull final List<Contract> values) {
		this(null, values);
	}

	@Override
	protected @Nullable Contract getEventField(@NonNull final Event event) {
		if (event instanceof SlotVisit) {
			return ((SlotVisit) event).getSlotAllocation().getContract();
		}
		return null;
	}
}
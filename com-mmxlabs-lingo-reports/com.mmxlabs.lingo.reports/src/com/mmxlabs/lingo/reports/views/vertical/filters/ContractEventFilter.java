package com.mmxlabs.lingo.reports.views.vertical.filters;

import java.util.List;

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
	public ContractEventFilter(final EventFilter filter, final List<Contract> values) {
		super(filter, values);
	}

	public ContractEventFilter(final List<Contract> values) {
		this(null, values);
	}

	@Override
	Contract getEventField(final Event event) {
		if (event instanceof SlotVisit) {
			return ((SlotVisit) event).getSlotAllocation().getContract();
		}
		return null;
	}
}
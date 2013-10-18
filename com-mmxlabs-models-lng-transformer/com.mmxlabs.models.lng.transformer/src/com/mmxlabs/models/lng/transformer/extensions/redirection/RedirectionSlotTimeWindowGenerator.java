/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Date;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.inject.ISlotTimeWindowGenerator;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;

public class RedirectionSlotTimeWindowGenerator implements ISlotTimeWindowGenerator {

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;

	@Inject
	private IRedirectionContractDetailsProvider redirectionContractDetailsProvider;

	@Override
	public ITimeWindow generateTimeWindow(final ISchedulerBuilder builder, final Slot slot, final Date earliestTime, final ITimeWindow defaultTimeWindow) {

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isSetContract() && loadSlot.getContract().getPriceInfo() instanceof RedirectionContract) {
				// Redirection contracts can go to anywhere, so need larger window for compatibility
				final int extraTime = redirectionContractDetailsProvider.getWindow(loadSlot);
				Date originalDate = redirectionContractDetailsProvider.getOriginalDate(loadSlot);

				final Date startTime = originalDate == null ? loadSlot.getWindowStart() : originalDate;

				return builder.createTimeWindow(dateAndCurveHelper.convertTime(earliestTime, startTime), dateAndCurveHelper.convertTime(earliestTime, startTime) + extraTime);
			}
		}
		return defaultTimeWindow;
	}

}

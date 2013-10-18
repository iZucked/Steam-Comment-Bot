/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EObject;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.inject.ISlotTimeWindowGenerator;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;

public class RedirectionSlotTimeWindowGenerator implements ISlotTimeWindowGenerator {

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;

	@Override
	public ITimeWindow generateTimeWindow(final ISchedulerBuilder builder, final Slot slot, final Date earliestTime, final ITimeWindow defaultTimeWindow) {

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isSetContract() && loadSlot.getContract().getPriceInfo() instanceof RedirectionContract) {
				// Redirection contracts can go to anywhere, so need larger window for compatibility
				final int extraTime = 24 * 60; // approx 2 months
				Date originalDate = null;
				for (final EObject ext : loadSlot.getExtensions()) {
					if (ext instanceof RedirectionContractOriginalDate) {
						final RedirectionContractOriginalDate redirectionContractOriginalDate = (RedirectionContractOriginalDate) ext;
						final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(loadSlot.getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart())));
						final Date originalDateExt = redirectionContractOriginalDate.getDate();
						calendar.setTime(originalDateExt);
						originalDate = calendar.getTime();
					}
				}
				final Date startTime = originalDate == null ? loadSlot.getWindowStart() : originalDate;

				return builder.createTimeWindow(dateAndCurveHelper.convertTime(earliestTime, startTime), dateAndCurveHelper.convertTime(earliestTime, startTime) + extraTime);
			}
		}
		return defaultTimeWindow;
	}
}

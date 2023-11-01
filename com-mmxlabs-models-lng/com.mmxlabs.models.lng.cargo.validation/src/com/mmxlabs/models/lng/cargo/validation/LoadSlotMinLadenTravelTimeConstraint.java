/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

@NonNullByDefault
public class LoadSlotMinLadenTravelTimeConstraint extends AbstractModelMultiConstraint {

	private static final int MAX_MIN_LADEN_DAYS = 60;

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof LoadSlot loadSlot && loadSlot.isSetMinLadenTime()) {
			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(loadSlot), ScenarioElementNameHelper.getNonNullString(loadSlot.getName()));
			final int minimumLadenTime = loadSlot.getMinLadenTime();
			if (minimumLadenTime < 0) {
				baseFactory.copyName() //
						.withMessage("Min laden time must greater than zero") //
						.withObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_MinLadenTime()) //
						.make(ctx, statuses);
			} else if (minimumLadenTime > MAX_MIN_LADEN_DAYS) {
				baseFactory.copyName() //
						.withFormattedMessage("Min laden must be at most %d days", MAX_MIN_LADEN_DAYS) //
						.withObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_MinLadenTime()) //
						.make(ctx, statuses);
			} else {
				final int minimumLadenTimeInHours = minimumLadenTime * 24;
				final @Nullable DischargeSlot nextDischargeSlot = getFollowingDischargeSlot(loadSlot);
				if (nextDischargeSlot != null) {
					loadSlot.getSchedulingTimeWindow().getDuration();
					final ZonedDateTime latestLadenStart = loadSlot.getSchedulingTimeWindow().getStart().plusHours(loadSlot.getSchedulingTimeWindow().getDuration());
					final ZonedDateTime latestDischargeStart = nextDischargeSlot.getSchedulingTimeWindow().getEnd();
					final long hoursDifference = Hours.between(latestLadenStart, latestDischargeStart);
					if (hoursDifference < minimumLadenTimeInHours) {
						baseFactory.copyName() //
								.withMessage("Min laden time conflicts with cargo windows") //
								.withObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_MinLadenTime()) //
								.make(ctx, statuses);
					}
				}
			}
		}
	}

	private @Nullable DischargeSlot getFollowingDischargeSlot(final LoadSlot loadSlot) {
		final Cargo cargo = loadSlot.getCargo();
		if (cargo == null) {
			return null;
		}
		final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
		final List<DischargeSlot> dischargeSlots = sortedSlots.stream().filter(DischargeSlot.class::isInstance).map(DischargeSlot.class::cast).toList();
		if (dischargeSlots.isEmpty()) {
			return null;
		}
		final Iterator<DischargeSlot> dischargeIter = dischargeSlots.iterator();
		DischargeSlot earliestDischargeSlot = dischargeIter.next();
		while (dischargeIter.hasNext()) {
			final DischargeSlot nextDischargeSlot = dischargeIter.next();
			final ZonedDateTime currentWindowStart = earliestDischargeSlot.getSchedulingTimeWindow().getStart();
			final ZonedDateTime nextWindowStart = nextDischargeSlot.getSchedulingTimeWindow().getStart();
			if (nextWindowStart.isBefore(currentWindowStart)) {
				earliestDischargeSlot = nextDischargeSlot;
			} else if (currentWindowStart.toInstant().equals(nextWindowStart.toInstant())) {
				final ZonedDateTime currentWindowEnd = earliestDischargeSlot.getSchedulingTimeWindow().getEnd();
				final ZonedDateTime nextWindowEnd = nextDischargeSlot.getSchedulingTimeWindow().getEnd();
				if (nextWindowEnd.isBefore(currentWindowEnd)) {
					earliestDischargeSlot = nextDischargeSlot;
				}
			}
		}
		return earliestDischargeSlot;
	}
}

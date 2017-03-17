/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MissingCurveValueConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof Sequence) {
			final Sequence sequence = (Sequence) target;
			if (sequence.getEvents().isEmpty()) {
				return Activator.PLUGIN_ID;
			}
			final Event startEvent = sequence.getEvents().get(0);

			final YearMonth startDate = YearMonth.from(startEvent.getStart());

			final VesselAvailability vesselAvailability = sequence.getVesselAvailability();

			if (vesselAvailability != null) {
				final String charterInRate = vesselAvailability.getTimeCharterRate();
				if (charterInRate != null && !charterInRate.trim().isEmpty()) {

					final Vessel vessel = vesselAvailability.getVessel();

					final String vesselName = vessel == null ? "<Unknown>" : vessel.getName();

					for (final NamedIndexContainer<?> index : PriceExpressionUtils.getLinkedCurves(charterInRate)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(startDate)) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("[Evaluated schedule][Availability|%s] There is no charter cost pricing data before %s %04d for curve %s",
											vesselName, date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())));

							dcsd.addEObjectAndFeature(vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__TIME_CHARTER_RATE);
							statuses.add(dcsd);
						}
					}
				}
			}
			final CharterInMarket charterInMarket = sequence.getCharterInMarket();
			if (charterInMarket != null) {
				final String charterInRate = charterInMarket.getCharterInRate();
				if (charterInRate != null && !charterInRate.trim().isEmpty()) {
					for (final NamedIndexContainer<?> index : PriceExpressionUtils.getLinkedCurves(charterInRate)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(startDate)) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
									.createFailureStatus(String.format("[Evaluated schedule][Charter-in Market|%s] There is no charter cost pricing data before %s %04d for curve %s",
											charterInMarket.getName(), date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())));

							dcsd.addEObjectAndFeature(charterInMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE);
							statuses.add(dcsd);
						}
					}
				}
			}

		}

		return Activator.PLUGIN_ID;
	}
}

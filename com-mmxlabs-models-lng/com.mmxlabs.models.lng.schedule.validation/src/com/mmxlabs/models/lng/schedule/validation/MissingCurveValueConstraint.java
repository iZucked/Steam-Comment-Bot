/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MissingCurveValueConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (!ScheduleModelValidationHelper.isMainScheduleModel(target)) {
			return;
		}
		if (target instanceof Sequence sequence) {
			if (sequence.getEvents().isEmpty()) {
				return;
			}
			final Event startEvent = sequence.getEvents().get(0);

			final YearMonth startDate = YearMonth.from(startEvent.getStart());

			final VesselAvailability vesselAvailability = sequence.getVesselAvailability();

			if (vesselAvailability != null) {
				final String charterInRate = vesselAvailability.getTimeCharterRate();
				if (charterInRate != null && !charterInRate.trim().isEmpty()) {

					final Vessel vessel = vesselAvailability.getVessel();

					final String vesselName = vessel == null ? "<Unknown>" : vessel.getName();

					for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(charterInRate)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(startDate)) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("Charter |'%s': There is no charter cost pricing data before %s %04d for curve %s", vesselName,
											date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())));

							dcsd.addEObjectAndFeature(vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__TIME_CHARTER_RATE);
							dcsd.setTag(ValidationConstants.TAG_EVALUATED_SCHEDULE);

							statuses.add(dcsd);
						}
					}
				}
			}
			final CharterInMarket charterInMarket = sequence.getCharterInMarket();
			if (charterInMarket != null) {
				final String charterInRate = charterInMarket.getCharterInRate();
				if (charterInRate != null && !charterInRate.trim().isEmpty()) {
					for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(charterInRate)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(startDate)) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter-in Market|%s] There is no charter cost pricing data before %s %04d for curve %s",
											charterInMarket.getName(), date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())));

							dcsd.addEObjectAndFeature(charterInMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE);
							dcsd.setTag(ValidationConstants.TAG_EVALUATED_SCHEDULE);

							statuses.add(dcsd);
						}
					}
				}
			}
		}
	}
}

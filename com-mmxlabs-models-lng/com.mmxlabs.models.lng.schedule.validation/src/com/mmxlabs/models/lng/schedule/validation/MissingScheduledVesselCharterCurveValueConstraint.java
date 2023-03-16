/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MissingScheduledVesselCharterCurveValueConstraint extends AbstractModelMultiConstraint {
	public static final Object KEY_CHARTER_COST = new Object();
	public static final Object KEY_MARKET_CHARTER_COST = new Object();
	public static final Object KEY_BUNKER_COST = new Object();

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (!ScheduleModelValidationHelper.isMainScheduleModel(target)) {
			return;
		}
		if (target instanceof final Sequence sequence) {
			if (sequence.getEvents().isEmpty()) {
				return;
			}
			final Event startEvent = sequence.getEvents().get(0);

			final YearMonth startDate = YearMonth.from(startEvent.getStart());

			final VesselCharter vesselCharter = sequence.getVesselCharter();
			final CharterInMarket charterInMarket = sequence.getCharterInMarket();
			Vessel vessel = null;
			EStructuralFeature feature = null;
			EObject fuelTarget = null;
			DetailConstraintStatusFactory baseFactory = null;
			if (vesselCharter != null) {
				// Check start bounds, we should have validated already if there is a start condition
				if (vesselCharter.getStartAfter() == null && vesselCharter.getStartBy() == null) {
					fuelTarget = vesselCharter;
					feature = CargoPackage.Literals.VESSEL_CHARTER__VESSEL;
					vessel = vesselCharter.getVessel();
					baseFactory = DetailConstraintStatusFactory.makeStatus().withName(ScenarioElementNameHelper.getName(vesselCharter));

					final String charterInRate = vesselCharter.getTimeCharterRate();
					if (charterInRate != null && !charterInRate.trim().isEmpty()) {

						for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(charterInRate, PriceIndexType.CHARTER)) {
							final @Nullable YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
							if (date == null) {
								baseFactory.withFormattedMessage("There is no charter cost pricing data for curve %s", index.getName()) //

										.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__TIME_CHARTER_RATE) //
										.withTag(ValidationConstants.TAG_EVALUATED_SCHEDULE) //
										.withConstraintKey(KEY_CHARTER_COST) //
										.make(ctx, statuses);
							} else if (date.isAfter(startDate)) {

								baseFactory
										.withFormattedMessage("There is no charter cost pricing data before %s %04d for curve %s", date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()),
												date.getYear(), index.getName()) //

										.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__TIME_CHARTER_RATE) //
										.withTag(ValidationConstants.TAG_EVALUATED_SCHEDULE) //
										.withConstraintKey(KEY_CHARTER_COST) //
										.make(ctx, statuses);
							}
						}
					}
				}
			} else if (charterInMarket != null) {
				vessel = charterInMarket.getVessel();
				fuelTarget = charterInMarket;
				feature = SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL;
				final String charterInRate = charterInMarket.getCharterInRate();
				baseFactory = DetailConstraintStatusFactory.makeStatus().withName("Charter In: " + ScenarioElementNameHelper.getNonNullString(charterInMarket.getName()));

				if (charterInRate != null && !charterInRate.trim().isEmpty()) {
					for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(charterInRate, PriceIndexType.CHARTER)) {
						final @Nullable YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null) {

							baseFactory.withFormattedMessage("There is no charter cost pricing data for curve %s", index.getName())//

									.withObjectAndFeature(charterInMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE)//
									.withTag(ValidationConstants.TAG_EVALUATED_SCHEDULE) //
									.withConstraintKey(KEY_MARKET_CHARTER_COST) //
									.make(ctx, statuses);
						} else if (date.isAfter(startDate)) {
							baseFactory
									.withFormattedMessage("There is no charter cost pricing data before %s %04d for curve %s", date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()),
											date.getYear(), index.getName()) //
									.withObjectAndFeature(charterInMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE)//
									.withTag(ValidationConstants.TAG_EVALUATED_SCHEDULE) //
									.withConstraintKey(KEY_MARKET_CHARTER_COST) //
									.make(ctx, statuses);
						}
					}
				}
			}
			if (vessel != null && fuelTarget != null && feature != null) {
				final Set<BaseFuel> fuels = new LinkedHashSet<>();

				fuels.add(vessel.getVesselOrDelegateBaseFuel());
				fuels.add(vessel.getVesselOrDelegateIdleBaseFuel());
				fuels.add(vessel.getVesselOrDelegateInPortBaseFuel());
				fuels.add(vessel.getVesselOrDelegatePilotLightBaseFuel());
				fuels.remove(null);

				final MMXRootObject rootObject = extraContext.getRootObject();
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

				final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);

				for (final BaseFuel baseFuel : fuels) {
					for (final BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
						if (baseFuelCost.getFuel() == baseFuel) {

							final YearMonth key = startDate;
							final String priceExpression = baseFuelCost.getExpression();

							if (priceExpression != null && !priceExpression.trim().isEmpty()) {
								for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(priceExpression, PriceIndexType.BUNKERS)) {
									final @Nullable YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
									if (date == null) {
										statuses.add(baseFactory.copyName() //
												.withObjectAndFeature(fuelTarget, feature) //
												.withMessage(String.format("There is no base fuel cost pricing data for curve %s", index.getName())) //
												.withConstraintKey(KEY_BUNKER_COST) //

												.make(ctx));
									} else if (date.isAfter(key)) {
										statuses.add(baseFactory.copyName() //
												.withObjectAndFeature(fuelTarget, feature) //
												.withMessage(String.format("There is no base fuel cost pricing data before %s %04d for curve %s",
														date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())) //
												.withConstraintKey(KEY_BUNKER_COST) //

												.make(ctx));
									}
								}
							}
						}
					}
				}
			}
		}
	}
}

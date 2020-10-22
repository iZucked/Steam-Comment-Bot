/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseCaseRowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof BaseCaseRow) {
			final BaseCaseRow baseCaseRow = (BaseCaseRow) target;

			PortModel portModel = null;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
			}

			final ShippingType nonShipped = AnalyticsBuilder.isNonShipped(baseCaseRow);
			if (nonShipped == ShippingType.Shipped) {
				if (baseCaseRow.getBuyOption() != null && baseCaseRow.getSellOption() != null) {
					if (baseCaseRow.getShipping() == null) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Starting point - no shipping option defined."));
						deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
					if (!(baseCaseRow.getShipping() instanceof SimpleVesselCharterOption || baseCaseRow.getShipping() instanceof RoundTripShippingOption
							|| baseCaseRow.getShipping() instanceof FullVesselCharterOption || baseCaseRow.getShipping() instanceof ExistingVesselCharterOption
							|| baseCaseRow.getShipping() instanceof ExistingCharterMarketOption)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Starting point - incompatible shipping option defined."));
						deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
					if (baseCaseRow.getShipping() instanceof RoundTripShippingOption) {
						final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) baseCaseRow.getShipping();
						final Vessel vessel = roundTripShippingOption.getVessel();

						validateTravelTime(ctx, extraContext, statuses, baseCaseRow, portModel, vessel);
					} else if (baseCaseRow.getShipping() instanceof SimpleVesselCharterOption) {
						final SimpleVesselCharterOption roundTripShippingOption = (SimpleVesselCharterOption) baseCaseRow.getShipping();
						final Vessel vessel = roundTripShippingOption.getVessel();
						if (vessel != null) {
							validateTravelTime(ctx, extraContext, statuses, baseCaseRow, portModel, vessel);
						}
					}

				}
			} else if (nonShipped == ShippingType.Mixed) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Starting point - incompatible slot combination."));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
				statuses.add(deco);
			}

			if (baseCaseRow.getShipping() != null && baseCaseRow.getShipping().eContainer() == null) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Starting point - uncontained shipping"));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
				statuses.add(deco);
			}
			if (baseCaseRow.getShipping() != null && !SandboxConstraintUtils.vesselPortRestrictionsValid(baseCaseRow.getBuyOption(), baseCaseRow.getSellOption(), baseCaseRow.getShipping())) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("Starting point - shipping option cannot visit a port in this cargo"));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
				statuses.add(deco);
			}
			if (baseCaseRow.getShipping() != null && !SandboxConstraintUtils.portRestrictionsValid(baseCaseRow.getBuyOption(), baseCaseRow.getSellOption())) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("Starting point - buy or sell does not have a compatible port in this cargo"));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
				statuses.add(deco);
			}
			if (baseCaseRow.getShipping() != null && !SandboxConstraintUtils.vesselRestrictionsValid(baseCaseRow.getBuyOption(), baseCaseRow.getSellOption(), baseCaseRow.getShipping())) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Starting point - load not permitted with this shipping option"));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
				statuses.add(deco);
			}
			if (!SandboxConstraintUtils.checkVolumeAgainstBuyAndSell(baseCaseRow.getBuyOption(), baseCaseRow.getSellOption())) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Starting point - load and discharge volumes do not match"));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
//				statuses.add(deco);
			}
			if (baseCaseRow.getShipping() != null && !SandboxConstraintUtils.checkVolumeAgainstVessel(baseCaseRow.getBuyOption(), baseCaseRow.getSellOption(), baseCaseRow.getShipping())) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Starting point - load and discharge volumes do not match"));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
//				statuses.add(deco);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void validateTravelTime(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses, final BaseCaseRow baseCaseRow, final PortModel portModel,
			final Vessel vessel) {
		final Port fromPort = AnalyticsBuilder.getPort(baseCaseRow.getBuyOption());
		final Port toPort = AnalyticsBuilder.getPort(baseCaseRow.getSellOption());
		if (fromPort != null && toPort != null && vessel != null) {

			final ZonedDateTime windowStartDate = AnalyticsBuilder.getWindowStartDate(baseCaseRow.getBuyOption());
			final ZonedDateTime windowEndDate = AnalyticsBuilder.getWindowEndDate(baseCaseRow.getSellOption());

			final double speed = vessel.getVesselOrDelegateMaxSpeed();

			ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

			final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vessel, speed, portModel.getRoutes(), modelDistanceProvider);

			if (windowStartDate != null && windowEndDate != null) {
				final int optionDuration = AnalyticsBuilder.getDuration(baseCaseRow.getBuyOption());
				final int availableTime = Hours.between(windowStartDate, windowEndDate) - optionDuration;

				if (travelTime > availableTime) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Target - not enough travel time."),
							IConstraintStatus.WARNING);
					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					statuses.add(deco);
				}
			}
		}
	}

}

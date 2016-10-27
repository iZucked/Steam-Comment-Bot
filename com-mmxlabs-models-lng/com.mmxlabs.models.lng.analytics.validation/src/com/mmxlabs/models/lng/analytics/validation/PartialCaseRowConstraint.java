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
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder.ShippingType;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PartialCaseRowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PartialCaseRow) {
			final PartialCaseRow partialCaseRow = (PartialCaseRow) target;
			PortModel portModel = null;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
			}
			ShippingType nonShipped = AnalyticsBuilder.isNonShipped(partialCaseRow);
			if (nonShipped == ShippingType.Shipped) {
				if (partialCaseRow.getShipping() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Partial case - no shipping option defined."));
					deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				for (ShippingOption opt : partialCaseRow.getShipping()) {
					if (!(opt instanceof FleetShippingOption || opt instanceof RoundTripShippingOption)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Partial case - incompatible shipping option defined."));
						deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}

					if (opt instanceof RoundTripShippingOption) {
						final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) opt;
						final VesselClass vesselClass = roundTripShippingOption.getVesselClass();

						for (BuyOption buy : partialCaseRow.getBuyOptions()) {
							for (SellOption sell : partialCaseRow.getSellOptions()) {
								validateTravelTime(ctx, statuses, partialCaseRow, portModel, vesselClass, buy, sell);
							}
						}
					} else if (opt instanceof FleetShippingOption) {
						final FleetShippingOption roundTripShippingOption = (FleetShippingOption) opt;
						final Vessel vessel = roundTripShippingOption.getVessel();
						if (vessel != null) {
							final VesselClass vesselClass = vessel.getVesselClass();
							for (BuyOption buy : partialCaseRow.getBuyOptions()) {
								for (SellOption sell : partialCaseRow.getSellOptions()) {
									validateTravelTime(ctx, statuses, partialCaseRow, portModel, vesselClass, buy, sell);
								}
							}
						}
					}
				}
			}

			for (ShippingOption opt : partialCaseRow.getShipping()) {
				if (opt.eContainer() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Partial case - uncontained shipping"));
					deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void validateTravelTime(final IValidationContext ctx, final List<IStatus> statuses, final PartialCaseRow baseCaseRow, final PortModel portModel, final VesselClass vesselClass,
			BuyOption buyOption, SellOption sellOption) {
		final Port fromPort = AnalyticsBuilder.getPort(buyOption);
		final Port toPort = AnalyticsBuilder.getPort(sellOption);
		if (fromPort != null && toPort != null && vesselClass != null) {

			final ZonedDateTime windowStartDate = AnalyticsBuilder.getWindowStartDate(buyOption);
			final ZonedDateTime windowEndDate = AnalyticsBuilder.getWindowEndDate(sellOption);

			final double speed = vesselClass.getMaxSpeed();

			final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vesselClass, speed, portModel.getRoutes());

			if (windowStartDate != null && windowEndDate != null) {
				final int optionDuration = AnalyticsBuilder.getDuration(buyOption);
				final int availableTime = Hours.between(windowStartDate, windowEndDate) - optionDuration;

				if (travelTime > availableTime) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Partial case - not enough travel time."));
					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
					statuses.add(deco);
				}
			}
		}
	}
}

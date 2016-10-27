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
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
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
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case - no shipping option defined."));
						deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
					if (!(baseCaseRow.getShipping() instanceof FleetShippingOption || baseCaseRow.getShipping() instanceof RoundTripShippingOption)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Base case - incompatible shipping option defined."));
						deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
					if (baseCaseRow.getShipping() instanceof RoundTripShippingOption) {
						final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) baseCaseRow.getShipping();
						final VesselClass vesselClass = roundTripShippingOption.getVesselClass();

						validateTravelTime(ctx, statuses, baseCaseRow, portModel, vesselClass);
					} else if (baseCaseRow.getShipping() instanceof FleetShippingOption) {
						final FleetShippingOption roundTripShippingOption = (FleetShippingOption) baseCaseRow.getShipping();
						final Vessel vessel = roundTripShippingOption.getVessel();
						if (vessel != null) {
							final VesselClass vesselClass = vessel.getVesselClass();
							validateTravelTime(ctx, statuses, baseCaseRow, portModel, vesselClass);
						}
					}

				}
			} else if (nonShipped == ShippingType.Mixed) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case - incompatible slot combination."));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
				statuses.add(deco);
			}

			if (baseCaseRow.getShipping() != null && baseCaseRow.getShipping().eContainer() == null) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case - uncontained shipping"));
				deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
				statuses.add(deco);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void validateTravelTime(final IValidationContext ctx, final List<IStatus> statuses, final BaseCaseRow baseCaseRow, final PortModel portModel, final VesselClass vesselClass) {
		final Port fromPort = AnalyticsBuilder.getPort(baseCaseRow.getBuyOption());
		final Port toPort = AnalyticsBuilder.getPort(baseCaseRow.getSellOption());
		if (fromPort != null && toPort != null && vesselClass != null) {

			final ZonedDateTime windowStartDate = AnalyticsBuilder.getWindowStartDate(baseCaseRow.getBuyOption());
			final ZonedDateTime windowEndDate = AnalyticsBuilder.getWindowEndDate(baseCaseRow.getSellOption());

			final double speed = vesselClass.getMaxSpeed();

			final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vesselClass, speed, portModel.getRoutes());

			if (windowStartDate != null && windowEndDate != null) {
				final int optionDuration = AnalyticsBuilder.getDuration(baseCaseRow.getBuyOption());
				final int availableTime = Hours.between(windowStartDate, windowEndDate) - optionDuration;

				if (travelTime > availableTime) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case - not enough travel time."));
					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					statuses.add(deco);
				}
			}
		}
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;

public class ProvisionalCargoConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof ProvisionalCargo) {
			final ProvisionalCargo shippingCostPlan = (ProvisionalCargo) target;

			// if (shippingCostPlan.getBaseFuelPrice() < 0.0001) {
			// final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base fuel price must be non-zero"));
			// deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_BaseFuelPrice());
			// statuses.add(deco);
			// }

			if (shippingCostPlan.eContainer() != null) {
				// DestinationType lastType = null;
				// int idx = 0;
				// for (final ShippingCostRow row : shippingCostPlan.getRows()) {
				// final DestinationType type = row.getDestinationType();
				// if (idx == 0) {
				// if (type != DestinationType.START) {
				// final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("First row must be a START row"));
				// deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
				// deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
				// statuses.add(deco);
				// }
				// } else if (idx == shippingCostPlan.getRows().size() - 1) {
				// if (type != DestinationType.END) {
				// final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Last row must be a END row"));
				// deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
				// deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
				// statuses.add(deco);
				// }
				// } else {
				// if (type == DestinationType.START || type == DestinationType.END) {
				// final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Waypoint rows cannot be START or END types"));
				// deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
				// deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
				// statuses.add(deco);
				// }
				// }
				// if (lastType == DestinationType.LOAD && type != DestinationType.DISCHARGE) {
				// final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("LOAD rows must be followed by DISCHARGE rows"));
				// deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
				// deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
				// statuses.add(deco);
				// }
				// if (lastType != DestinationType.LOAD && type == DestinationType.DISCHARGE) {
				// final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("LOAD rows must be followed by DISCHARGE rows"));
				// deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
				// deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
				// statuses.add(deco);
				// }
				//
				// lastType = type;
				// idx++;
				// }

				validateSlotTravelTime(ctx, extraContext, shippingCostPlan, statuses);
			}
		}
		return Activator.PLUGIN_ID;
	}

	/**
	 * Validate that the available time is enough to get from A to B, if it's not negative
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private void validateSlotTravelTime(final IValidationContext ctx, final IExtraValidationContext extraContext, final ProvisionalCargo plan, final List<IStatus> statuses) {

		final MMXRootObject rootObject = extraContext.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			LNGScenarioModel scenario = (LNGScenarioModel) rootObject;
			final VesselClass vesselClass = plan.getVessel().getVesselClass();
			if (plan.getVessel() == null || vesselClass == null) {
				return;
			}

			final double maxSpeedKnots = vesselClass.getMaxSpeed();

			@SuppressWarnings("unchecked")
			Map<Pair<Port, Port>, Integer> minTimes = (Map<Pair<Port, Port>, Integer>) ctx.getCurrentConstraintData();
			if (minTimes == null) {
				minTimes = new HashMap<Pair<Port, Port>, Integer>();

				for (final VesselClassRouteParameters parameters : vesselClass.getRouteParameters()) {
					collectMinTimes(minTimes, parameters.getRoute(), parameters.getExtraTransitTime(), maxSpeedKnots);
				}

				for (final Route route : scenario.getReferenceModel().getPortModel().getRoutes()) {
					if (route.getRouteOption() == RouteOption.DIRECT) {
						collectMinTimes(minTimes, route, 0, maxSpeedKnots);
					}
				}

				ctx.putCurrentConstraintData(minTimes);
			}

			// ShippingCostRow lastRow = null;
			// for (final ShippingCostRow row : plan.getRows()) {

			// if (lastRow != null && lastRow.getPort() != null && row.getPort() != null) {

			BuyOpportunity lastRow = plan.getBuy();
			SellOpportunity row = plan.getSell();

			if (row.getDate() != null && lastRow.getDate() != null) {

				int visitDuration = 0;
				// if (lastRow.getDestinationType() == DestinationType.LOAD ) {
				visitDuration = lastRow.getPort().getLoadDuration();
				// } else if(lastRow.getDestinationType() == DestinationType.DISCHARGE) {
				// visitDuration = lastRow.getPort().getDischargeDuration();
				// } else if(lastRow.getDestinationType() == DestinationType.OTHER) {
				// visitDuration = 24;
				// }
				final int availableTime = Hours.between(lastRow.getDate(), row.getDate()) - visitDuration;

				if (availableTime < 0) {
					final String msg = String.format("Date is before the previous date.");
					final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(msg);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
					dsd.addEObjectAndFeature(lastRow, AnalyticsPackage.eINSTANCE.getBuyOpportunity_Date());
					dsd.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getSellOpportunity_Date());
					statuses.add(dsd);
				}

				else if (!lastRow.getPort().equals(row.getPort())) {
					final Pair<Port, Port> key = new Pair<Port, Port>(lastRow.getPort(), row.getPort());
					final Integer time = minTimes.get(key);

					if (time == null) {
						// distance line is missing
						final String msg = String.format("Impossible journey between %s and %s.", lastRow.getPort().getName(), row.getPort().getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(msg);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
						dsd.addEObjectAndFeature(lastRow, AnalyticsPackage.eINSTANCE.getBuyOpportunity_Date());
						dsd.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getSellOpportunity_Date());
						statuses.add(dsd);
					} else {
						if (time > availableTime) {

							final String msg = String.format("Row does not have enough travel time - %s is required for the journey, but only %s is available.", formatHours(time),
									formatHours(availableTime));

							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
							dsd.addEObjectAndFeature(lastRow, AnalyticsPackage.eINSTANCE.getBuyOpportunity_Date());
							dsd.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getSellOpportunity_Date());
							statuses.add(dsd);
						}
					}
				}
			}
		}
		// lastRow = row;
		// }
		// }
	}

	private void collectMinTimes(final Map<Pair<Port, Port>, Integer> minTimes, final Route d, final int extraTime, final double maxSpeed) {
		for (final RouteLine dl : d.getLines()) {
			final Pair<Port, Port> p = new Pair<Port, Port>(dl.getFrom(), dl.getTo());
			final int time = Calculator.getTimeFromSpeedDistance(OptimiserUnitConvertor.convertToInternalSpeed(maxSpeed), dl.getFullDistance()) + extraTime;
			if (!minTimes.containsKey(p) || (minTimes.get(p) > time)) {
				minTimes.put(p, time);
			}
		}
	}

	private String formatHours(final int hours) {
		if (hours < 24) {
			if (hours == 1) {
				return hours + " hour";
			} else {
				return hours + " hours";
			}
		} else {
			final int remainderHours = hours % 24;
			final int days = hours / 24;
			return days + " day" + (days > 1 ? "s" : "") + (remainderHours > 0 ? (", " + remainderHours + " hour" + (remainderHours > 1 ? "s" : "")) : "");
		}
	}

}

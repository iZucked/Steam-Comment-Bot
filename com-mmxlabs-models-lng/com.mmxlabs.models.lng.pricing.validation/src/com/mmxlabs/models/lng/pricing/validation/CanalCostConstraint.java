/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Generates a warning if some canal costs are not set
 * 
 * @author Tom Hinton
 * 
 */
public class CanalCostConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();

		final EObject target = ctx.getTarget();

		if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final MMXRootObject scenario = extraValidationContext.getRootObject();

			if (scenario != null) {
				final StringBuffer missingCanalNames = new StringBuffer();

				final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
				final PortModel portModel = scenario.getSubModel(PortModel.class);
				for (final Route route : portModel.getRoutes()) {
					if (route.isCanal()) {
						boolean seenCanalCost = false;
						routeCosts: for (final RouteCost routeCost : pricingModel.getRouteCosts()) {
							if (routeCost.getVesselClass() == vesselClass && routeCost.getRoute() == route) {
								seenCanalCost = true;
								break routeCosts;
							}

						}

						boolean seenCanalParameters = false;
						canalParameters: for (final VesselClassRouteParameters routeParameters : vesselClass.getRouteParameters()) {
							if (route == routeParameters.getRoute()) {
								seenCanalParameters = true;
								break canalParameters;
							}
						}

						if (!(seenCanalParameters && seenCanalCost)) {
							missingCanalNames.append(missingCanalNames.length() > 0 ? ", " : "");
							missingCanalNames.append(route.getName());
						}
					}
				}

				if (missingCanalNames.length() > 0) {
					final String message = String.format("The vessel class %s has invalid canal costs set for canal %s", vesselClass.getName(), missingCanalNames.toString());
					final DetailConstraintStatusDecorator result = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					result.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_RouteParameters());
					return result;
				}
			}
		} else if (target instanceof RouteCost) {

			final RouteCost vesselClassCost = (RouteCost) target;

			if (vesselClassCost.getLadenCost() == 0 || vesselClassCost.getBallastCost() == 0) {
				final String message = String.format("The vessel class %s has invalid canal costs set for canal %s", vesselClassCost.getVesselClass().getName(), vesselClassCost.getRoute().getName());
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				if (vesselClassCost.getLadenCost() == 0) {
					dcsd.addEObjectAndFeature(vesselClassCost, PricingPackage.eINSTANCE.getRouteCost_LadenCost());
				}
				if (vesselClassCost.getBallastCost() == 0) {
					dcsd.addEObjectAndFeature(vesselClassCost, PricingPackage.eINSTANCE.getRouteCost_BallastCost());
				}
				return dcsd;
			}

			final EObject original = extraValidationContext.getOriginal(vesselClassCost);
			final EObject replacement = extraValidationContext.getReplacement(vesselClassCost);

			final List<EObject> objects = extraValidationContext.getSiblings(target);
			for (final EObject obj : objects) {
				if (obj instanceof RouteCost) {
					final RouteCost routeCost = (RouteCost) obj;

					if (routeCost == original || routeCost == replacement) {
						continue;
					}
					if (routeCost.getVesselClass() == vesselClassCost.getVesselClass()) {

						final String message = String.format("Vessel class %s has multiple Route Costs", vesselClassCost.getVesselClass().getName());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(vesselClassCost, PricingPackage.eINSTANCE.getRouteCost_VesselClass());
						return dcsd;
					}

				}
			}

		}

		return ctx.createSuccessStatus();
	}
}

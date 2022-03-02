/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Generates a warning if some canal costs are not set
 * 
 * @author Tom Hinton
 * 
 */
public class CanalCostConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Vessel) {
			final Vessel vessel = (Vessel) target;
			final MMXRootObject rootObject = extraContext.getRootObject();

			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

				final EObject original = extraContext.getOriginal(vessel);
				final EObject replacement = extraContext.getReplacement(vessel);

				final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);

				boolean seenSuezCost = false;
				LOOP_ROUTE_COSTS: for (final RouteCost routeCost : costModel.getRouteCosts()) {
					if (routeCost.getRouteOption() == RouteOption.SUEZ) {
						final Collection<Vessel> vessels = SetUtils.getObjects(routeCost.getVessels());
						seenSuezCost |= vessels.contains(original);
						seenSuezCost |= vessels.contains(replacement);
						if (seenSuezCost) {
							break LOOP_ROUTE_COSTS;
						}
					}
				}

				if (!seenSuezCost && (costModel.getSuezCanalTariff() == null || vessel.getVesselOrDelegateSCNT() == 0)) {
					final String message = String.format("The vessel %s has no Suez canal costs", vessel.getName());
					DetailConstraintStatusFactory.makeStatus()//
							.withMessage(message)//
							.withObjectAndFeature(vessel, MMXCorePackage.eINSTANCE.getNamedObject_Name())//
							.make(ctx, statuses);
				}
			}
		} else if (target instanceof RouteCost) {
			final RouteCost routeCost = (RouteCost) target;
			if (routeCost.getVessels().isEmpty()) {
				final String message = "Route costs are not linked to any vessel";
				DetailConstraintStatusFactory.makeStatus() //
						.withMessage(message) //
						.withObjectAndFeature(routeCost, PricingPackage.eINSTANCE.getRouteCost_Vessels()) //
						.make(ctx, statuses);
			} else {
				final String vesselSetName = ScenarioElementNameHelper.getName(routeCost.getVessels(), "<No vessels>");
				final DetailConstraintStatusFactory factoryBase = DetailConstraintStatusFactory.makeStatus().withName(vesselSetName);

				if (routeCost.getLadenCost() == 0 || routeCost.getBallastCost() == 0) {
					final String message = String.format("Invalid canal costs set for canal %s", routeCost.getRouteOption() == null ? "unknown" : routeCost.getRouteOption().getName());

					final DetailConstraintStatusFactory factory = factoryBase.copyName()//
							.withMessage(message);

					if (routeCost.getLadenCost() == 0) {
						factory.withObjectAndFeature(routeCost, PricingPackage.eINSTANCE.getRouteCost_LadenCost());
					}
					if (routeCost.getBallastCost() == 0) {
						factory.withObjectAndFeature(routeCost, PricingPackage.eINSTANCE.getRouteCost_BallastCost());
					}
					factory.make(ctx, statuses);
				}

				final List<EObject> objects = extraContext.getSiblings(target);
				final Set<AVesselSet<Vessel>> explicit = new HashSet<>();
				final Set<Vessel> implicit = new HashSet<>();
				for (final EObject obj : objects) {
					if (obj instanceof RouteCost) {

						final RouteCost otherRouteCost = (RouteCost) obj;
						if (otherRouteCost == routeCost) {
							continue;
						}

						if (otherRouteCost.getRouteOption() != routeCost.getRouteOption()) {
							continue;
						}
						for (final AVesselSet<Vessel> vesselSet : otherRouteCost.getVessels()) {
							explicit.add(vesselSet);
							if (vesselSet instanceof Vessel) {
							} else {
								implicit.addAll(SetUtils.getObjects(vesselSet));
							}
						}
					}
				}

				boolean warnedExplcit = false;
				final boolean warnedImplcit = false;
				for (final AVesselSet<Vessel> vesselSet : routeCost.getVessels()) {
					if (explicit.contains(vesselSet) && !warnedExplcit) {
						final DetailConstraintStatusFactory factory = factoryBase.copyName()//
								.withMessage(String.format("Vessel %s already contained explicitly in another route cost entry", vesselSet.getName()));
						factory.withObjectAndFeature(routeCost, PricingPackage.eINSTANCE.getRouteCost_Vessels());
						factory.make(ctx, statuses);
						warnedExplcit = true;
					}
					if (vesselSet instanceof Vessel) {
						// No extra check here
					} else {
						for (final Vessel vessel : SetUtils.getObjects(vesselSet)) {
							if (implicit.contains(vessel) && !warnedImplcit) {
								final DetailConstraintStatusFactory factory = factoryBase.copyName()//
										.withMessage(String.format("Vessel %s in group %s already contained in another route cost entry", vessel.getName(), vesselSet.getName()));
								factory.withObjectAndFeature(routeCost, PricingPackage.eINSTANCE.getRouteCost_Vessels());
								factory.make(ctx, statuses);
								// warnedImplcit = true;
							}
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}

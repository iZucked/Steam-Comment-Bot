/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.ValidationSupport;

/**
 * Generates a warning if some canal costs are not set
 * 
 * @author Tom Hinton
 * 
 */
public class CanalCostConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final MMXRootObject scenario = ValidationSupport.getInstance().getParentObjectType(MMXRootObject.class, vesselClass);

			if (scenario != null) {
				final StringBuffer missingCanalNames = new StringBuffer();
				
				PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
				
				
				for (RouteCost routeCost : pricingModel.getRouteCosts()) {
				}
				final CanalModel canalModel = scenario.getCanalModel();
				for (final Canal c : canalModel.getCanals()) {
					boolean seenCanal = false;

					for (final VesselClassCost vcc : vesselClass.getCanalCosts()) {
						if (vcc.getCanal() == c) {
							seenCanal = true;
							break;
						}
					}

					if (!seenCanal) {
						missingCanalNames.append(missingCanalNames.length() > 0 ? ", " : "");
						missingCanalNames.append(c.getName());
					}
				}
				if (missingCanalNames.length() > 0) {
					final DetailConstraintStatusDecorator result = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselClass.getName(), missingCanalNames.toString()));

					result.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_CanalCosts());
					return result;
				}
			}
		} else if (target instanceof VesselClassCost) {
			final VesselClassCost vesselClassCost = (VesselClassCost) target;
			final VesselClass vesselClass = (VesselClass) vesselClassCost.eContainer();

			if (vesselClassCost.getLadenCost() == 0 || vesselClassCost.getUnladenCost() == 0) {

				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselClass.getName(), vesselClassCost.getCanal()
						.getName()));

				dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_CanalCosts());

				if (vesselClassCost.getLadenCost() == 0)
					dcsd.addEObjectAndFeature(vesselClassCost, FleetPackage.eINSTANCE.getVesselClassCost_LadenCost());

				if (vesselClassCost.getUnladenCost() == 0)
					dcsd.addEObjectAndFeature(vesselClassCost, FleetPackage.eINSTANCE.getVesselClassCost_UnladenCost());

				return dcsd;
			}
		}

		return ctx.createSuccessStatus();
	}
}

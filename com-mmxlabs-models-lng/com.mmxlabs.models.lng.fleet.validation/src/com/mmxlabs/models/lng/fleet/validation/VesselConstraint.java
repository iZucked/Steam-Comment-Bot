/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class VesselConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Vessel) {
			final Vessel vessel = (Vessel) target;

			DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(vessel), ScenarioElementNameHelper.getNonNullString(vessel.getName()));

			double effectiveCapacity = vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity();
			if (vessel.getVesselOrDelegateSafetyHeel() > effectiveCapacity) {
				final String message = String.format("Minimum heel should be less than fill capacity");

				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_SafetyHeel()) //
						.make(ctx, statuses);
			}

			if (vessel.getReference() != null) {
				if (vessel.getReference().getReference() != null) {
					String message = "Reference vessel can not have a reference vessel";
					baseFactory.copyName() //
							.withMessage(message) //
							.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_Reference()) //
							.make(ctx, statuses);
				}
			}
//			if (vessel.isPrototype()) {
//				String message = "Prototype vessels cannot inherit attributes";
//				DetailConstraintStatusFactory factory = baseFactory.copyName() //
//						.withMessage(message);
//
//				boolean foundFault = false;
//
//				if (vessel.getBasedOn() != null) {
//					foundFault = true;
//					factory.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_Reference());
//				}
//
//				if (foundFault) {
//					factory.make(ctx, statuses);
//				}
//			}
		}

		return Activator.PLUGIN_ID;
	}
}

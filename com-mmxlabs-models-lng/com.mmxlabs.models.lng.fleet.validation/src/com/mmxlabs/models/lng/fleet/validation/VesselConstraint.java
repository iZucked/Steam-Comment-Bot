/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.lng.port.RouteOption;
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

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(vessel), ScenarioElementNameHelper.getNonNullString(vessel.getName()));

			final double effectiveCapacity = vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity();
			if (vessel.getVesselOrDelegateSafetyHeel() > effectiveCapacity) {
				final String message = String.format("Minimum heel should be less than fill capacity");

				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_SafetyHeel()) //
						.make(ctx, statuses);
			}

			if (vessel.getVesselOrDelegateBaseFuel() == null) {
				final String message = "Vessel has no travel base fuel";
				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_BaseFuel()) //
						.make(ctx, statuses);
			}
			if (vessel.getVesselOrDelegateIdleBaseFuel() == null) {
				final String message = "Vessel has no idle base fuel";
				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_IdleBaseFuel()) //
						.make(ctx, statuses);
			}
			if (vessel.getVesselOrDelegatePilotLightBaseFuel() == null) {
				final String message = "Vessel has no pilot light base fuel";
				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_PilotLightBaseFuel()) //
						.make(ctx, statuses);
			}
			if (vessel.getVesselOrDelegateInPortBaseFuel() == null) {
				final String message = "Vessel has no in port base fuel";
				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_InPortBaseFuel()) //
						.make(ctx, statuses);
			}
			if (vessel.getReference() != null) {
				if (vessel.getReference().getReference() != null) {
					final String message = "Reference vessel can not have a reference vessel";
					baseFactory.copyName() //
							.withMessage(message) //
							.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_Reference()) //
							.make(ctx, statuses);
				}
			}
			final EList<VesselClassRouteParameters> routeParameters = vessel.getVesselOrDelegateRouteParameters();
			if (routeParameters == null || routeParameters.isEmpty()) {
				final String message = "Vessel has no canal parameters";
				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_RouteParameters()) //
						.make(ctx, statuses);
			} else {
				boolean foundSuez = false;
				boolean foundPanama = false;
				for (final VesselClassRouteParameters rp : routeParameters) {
					foundSuez |= rp.getRouteOption() == RouteOption.SUEZ;
					foundPanama |= rp.getRouteOption() == RouteOption.PANAMA;
				}
				if (!foundSuez) {
					final String message = "Vessel has no Suez canal parameters";
					baseFactory.copyName() //
							.withMessage(message) //
							.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_RouteParameters()) //
							.make(ctx, statuses);
				}
				if (!foundPanama) {
					final String message = "Vessel has no Panama canal parameters";
					baseFactory.copyName() //
							.withMessage(message) //
							.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_RouteParameters()) //
							.make(ctx, statuses);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}

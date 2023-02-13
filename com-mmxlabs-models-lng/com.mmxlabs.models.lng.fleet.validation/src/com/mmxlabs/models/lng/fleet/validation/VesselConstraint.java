/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselRouteParameters;
import com.mmxlabs.models.lng.fleet.util.VesselConstants;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
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
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Vessel vessel) {

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(vessel), ScenarioElementNameHelper.getNonNullString(vessel.getName()));

			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MMX_REFERENCE_VESSELS)) {
				validateMmxVesselProperties(vessel, baseFactory, ctx, statuses);
			}

			final double effectiveCapacity = vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity();
			if (vessel.getVesselOrDelegateSafetyHeel() > effectiveCapacity) {
				final String message = "Minimum heel should be less than fill capacity";

				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_SafetyHeel()) //
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
				if (vessel.getReference().isMarker() && !vessel.isMarker()) {
					final String message = "Non-marker vessel cannot have marker vessel as reference";
					baseFactory.copyName() //
							.withMessage(message) //
							.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_Marker()) //
							.make(ctx, statuses);
				}
			}

			if (!vessel.isMarker()) {
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
				final EList<VesselRouteParameters> routeParameters = vessel.getVesselOrDelegateRouteParameters();
				if (routeParameters == null || routeParameters.isEmpty()) {
					final String message = "Vessel has no canal parameters";
					baseFactory.copyName() //
							.withMessage(message) //
							.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_RouteParameters()) //
							.make(ctx, statuses);
				} else {
					boolean foundSuez = false;
					boolean foundPanama = false;
					for (final VesselRouteParameters rp : routeParameters) {
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
					final List<VesselRouteParameters> nonDelegateRouteParameters = vessel.getRouteParameters();
					for (final VesselRouteParameters routeParameter : nonDelegateRouteParameters) {
						final String canalName;
						if (routeParameter.getRouteOption() == RouteOption.SUEZ) {
							canalName = "Suez";
						} else if (routeParameter.getRouteOption() == RouteOption.PANAMA) {
							canalName = "Panama";
						} else {
							canalName = routeParameter.getRouteOption().getName();
						}
						if (routeParameter.getBallastConsumptionRate() > 0 && routeParameter.getBallastConsumptionRate() >= routeParameter.getBallastNBORate()) {
							baseFactory.copyName() //
									.withMessage(String.format("%s ballast base fuel rate should be less than ballast NBO rate.", canalName)) //
									.withObjectAndFeature(routeParameter, FleetPackage.eINSTANCE.getVesselRouteParameters_BallastConsumptionRate()) //
									.withObjectAndFeature(routeParameter, FleetPackage.eINSTANCE.getVesselRouteParameters_BallastNBORate()) //
									.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_RouteParameters()) //
									.make(ctx, statuses);
						}
						if (routeParameter.getLadenConsumptionRate() > 0 && routeParameter.getLadenConsumptionRate() >= routeParameter.getLadenNBORate()) {
							baseFactory.copyName() //
									.withMessage(String.format("%s laden base fuel rate should be less than laden NBO rate.", canalName)) //
									.withObjectAndFeature(routeParameter, FleetPackage.eINSTANCE.getVesselRouteParameters_LadenConsumptionRate()) //
									.withObjectAndFeature(routeParameter, FleetPackage.eINSTANCE.getVesselRouteParameters_LadenNBORate()) //
									.withObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_RouteParameters()) //
									.make(ctx, statuses);
						}
					}
				}
			}
		}
	}

	private void validateMmxVesselProperties(Vessel vessel, DetailConstraintStatusFactory baseFactory, @NonNull IValidationContext ctx, @NonNull List<IStatus> statuses) {
		if (!vessel.isMmxReference()) {
			final String vesselName = vessel.getName();
			if (vesselName != null && vesselName.matches(VesselConstants.REGEXP_MMX_PROVIDED_VESSEL_NAME)) {
				final String message = "'<' and '>' are reserved for LiNGO maintained vessels";
				baseFactory.copyName() //
						.withMessage(message) //
						.withObjectAndFeature(vessel, MMXCorePackage.eINSTANCE.getNamedObject_Name()) //
						.make(ctx, statuses);
			}
		}
	}
}

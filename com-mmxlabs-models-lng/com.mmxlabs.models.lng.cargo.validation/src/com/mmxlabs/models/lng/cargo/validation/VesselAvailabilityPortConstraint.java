/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Check that start/end requirement matches port constraint
 * 
 * @author hinton
 * 
 */
public class VesselAvailabilityPortConstraint extends AbstractModelMultiConstraint {
	/**
	 */
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			final VesselAvailability availablility = (VesselAvailability) target;
			final Vessel vessel = availablility.getVessel();

			if (vessel == null) {
				return Activator.PLUGIN_ID;
			}

			final VesselClass vesselClass = vessel.getVesselClass();
			if (vesselClass == null) {
				return Activator.PLUGIN_ID;
			}
			final Set<Port> inaccessiblePortSet = vessel.getInaccessiblePorts().isEmpty() ? SetUtils.getObjects(vesselClass.getInaccessiblePorts()) : SetUtils
					.getObjects(vessel.getInaccessiblePorts());
			if (!availablility.getStartAt().isEmpty()) {

				final Set<Port> availabilityPortSet = SetUtils.getObjects(availablility.getStartAt());

				if (availabilityPortSet.size() > 1) {
					final String message = String.format("Vessel %s's start requirement has multiple ports. Only one can be specified.", vessel.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					dcsd.addEObjectAndFeature(availablility, CargoPackage.eINSTANCE.getVesselAvailability_StartAt());
					statuses.add(dcsd);
				}

				for (final Port p : availabilityPortSet) {

					if (inaccessiblePortSet.contains(p)) {
						final String message = String.format("Vessel %s's %s requirement is set for port %s, but the vessel is of class %s which cannot dock at %s.", vessel.getName(), "start",
								p.getName(), vessel.getVesselClass().getName(), p.getName());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						dcsd.addEObjectAndFeature(availablility, CargoPackage.eINSTANCE.getVesselAvailability_StartAt());
						statuses.add(dcsd);

					}
				}
			}
			if (!availablility.getEndAt().isEmpty()) {
				final Set<Port> availabilityPortSet = SetUtils.getObjects(availablility.getEndAt());
				for (final Port p : availabilityPortSet) {

					if (inaccessiblePortSet.contains(p)) {
						final String message = String.format("Vessel %s's %s requirement is set for port %s, but the vessel is of class %s which cannot dock at %s.", vessel.getName(), "end",
								p.getName(), vessel.getVesselClass().getName(), p.getName());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						dcsd.addEObjectAndFeature(availablility, CargoPackage.eINSTANCE.getVesselAvailability_EndAt());
						statuses.add(dcsd);

					}
				}
			}
		} else if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {

				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

				final HashSet<String> badPorts = new HashSet<String>();
				final List<String> badVessels = new LinkedList<String>();
				for (final VesselAvailability availability : lngScenarioModel.getCargoModel().getVesselAvailabilities()) {
					final Vessel v = availability.getVessel();
					if (v != null && extraContext.getReplacement(vesselClass) == v.getVesselClass()) {

						final Set<Port> inaccessiblePortSet = SetUtils.getObjects(vesselClass.getInaccessiblePorts());

						boolean bad = false;
						{
							final Set<Port> availabilityPortSet = SetUtils.getObjects(availability.getStartAt());
							for (final Port p : availabilityPortSet) {

								// No port match
								if (inaccessiblePortSet.contains(p)) {
									badPorts.add(p.getName());
									if (!bad) {

										bad = true;
										badVessels.add(v.getName());
									}
								}
							}
						}
						{
							final Set<Port> availabilityPortSet = SetUtils.getObjects(availability.getEndAt());
							for (final Port p : availabilityPortSet) {

								// No port match
								if (inaccessiblePortSet.contains(p)) {
									badPorts.add(p.getName());
									bad = true;
									badVessels.add(v.getName());
								}
							}
						}
					}
				}

				if (badVessels.isEmpty() == false) {
					final String message = String.format("The vessels %s have start / end requirements at the ports %s, which are in the inaccessible port list.", badVessels, badPorts);
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_InaccessiblePorts());
					statuses.add(dcsd);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}

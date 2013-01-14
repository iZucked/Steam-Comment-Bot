/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.lng.types.APort;
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
	 * @since 2.0
	 */
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		final IExtraValidationContext extraContext = Activator.getDefault().getExtraValidationContext();
		if (target instanceof VesselAvailability) {
			final VesselAvailability availablility = (VesselAvailability) target;

			final EObject container = extraContext.getContainer(availablility);
			if (container instanceof Vessel) {
				final Vessel vessel = (Vessel) container;
				final VesselClass vesselClass = (VesselClass) vessel.getVesselClass();
				if (vesselClass == null) {
					return Activator.PLUGIN_ID;
				}
				final Set<APort> inaccessiblePortSet = SetUtils.getPorts(vesselClass.getInaccessiblePorts());
				if (!availablility.getStartAt().isEmpty()) {

					final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getStartAt());

					if (availabilityPortSet.size() > 1) {
						final String message = String.format("Vessel %s's start requirement has multiple ports. Only one can be specified.", vessel.getName());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						dcsd.addEObjectAndFeature(availablility, FleetPackage.eINSTANCE.getVesselAvailability_StartAt());
						statuses.add(dcsd);
					}

					for (final APort p : availabilityPortSet) {

						if (inaccessiblePortSet.contains(p)) {
							final String message = String.format("Vessel %s's %s requirement is set for port %s, but the vessel is of class %s which cannot dock at %s.", vessel.getName(), "start",
									p.getName(), vessel.getVesselClass().getName(), p.getName());
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

							dcsd.addEObjectAndFeature(availablility, FleetPackage.eINSTANCE.getVesselAvailability_StartAt());
							statuses.add(dcsd);

						}
					}
				}
				if (!availablility.getEndAt().isEmpty()) {
					final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getEndAt());
					for (final APort p : availabilityPortSet) {

						if (inaccessiblePortSet.contains(p)) {
							final String message = String.format("Vessel %s's %s requirement is set for port %s, but the vessel is of class %s which cannot dock at %s.", vessel.getName(), "end",
									p.getName(), vessel.getVesselClass().getName(), p.getName());
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

							dcsd.addEObjectAndFeature(availablility, FleetPackage.eINSTANCE.getVesselAvailability_EndAt());
							statuses.add(dcsd);

						}
					}
				}
			}
		} else if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject == null) {
				return Activator.PLUGIN_ID;
			}

			final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);

			final HashSet<String> badPorts = new HashSet<String>();
			final List<String> badVessels = new LinkedList<String>();
			for (final Vessel v : fleetModel.getVessels()) {
				if (extraContext.getReplacement(vesselClass) == v.getVesselClass()) {
					final VesselAvailability availablility = v.getAvailability();

					final Set<APort> inaccessiblePortSet = SetUtils.getPorts(vesselClass.getInaccessiblePorts());

					boolean bad = false;
					{
						final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getStartAt());
						for (final APort p : availabilityPortSet) {

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
						final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getEndAt());
						for (final APort p : availabilityPortSet) {

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
		return Activator.PLUGIN_ID;
	}
}

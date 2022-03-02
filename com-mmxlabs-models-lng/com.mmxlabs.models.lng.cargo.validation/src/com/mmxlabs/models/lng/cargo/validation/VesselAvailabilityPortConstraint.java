/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
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

			final Set<Port> inaccessiblePortSet = vessel.getVesselOrDelegateInaccessiblePorts().isEmpty() ? SetUtils.getObjects(vessel.getVesselOrDelegateInaccessiblePorts())
					: SetUtils.getObjects(vessel.getVesselOrDelegateInaccessiblePorts());
			if (availablility.getStartAt() != null) {

				final Port p = availablility.getStartAt();
				{
					if (inaccessiblePortSet.contains(p)) {
						final String message = String.format("Vessel %s's %s requirement is set for port %s, but the vessel %s cannot dock at %s.", vessel.getName(), "start", p.getName(),
								vessel.getName(), p.getName());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						dcsd.addEObjectAndFeature(availablility, CargoPackage.eINSTANCE.getVesselAvailability_StartAt());
						statuses.add(dcsd);

					}
				}
			}
			if (!availablility.getEndAt().isEmpty()) {

				for (APortSet<Port> entry : availablility.getEndAt()) {
					// Check explicit ports
					if (entry instanceof Port) {
						Port p = (Port) entry;
						if (inaccessiblePortSet.contains(p)) {
							final String message = String.format("Vessel %s's %s requirement is set for port %s, but the vessel %s cannot dock at %s.", vessel.getName(), "end", p.getName(),
									vessel.getName(), p.getName());
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

							dcsd.addEObjectAndFeature(availablility, CargoPackage.eINSTANCE.getVesselAvailability_EndAt());
							statuses.add(dcsd);

						}
					}
				}

				// Check for any valid ports
				final Set<Port> availabilityPortSet = SetUtils.getObjects(availablility.getEndAt());
				availabilityPortSet.removeAll(inaccessiblePortSet);

				if (availabilityPortSet.isEmpty()) {
					final String message = String.format("Vessel %s's %s requirement has no valid ports to dock at.", vessel.getName(), "end", vessel.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					dcsd.addEObjectAndFeature(availablility, CargoPackage.eINSTANCE.getVesselAvailability_EndAt());
					statuses.add(dcsd);

				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class InaccessiblePortsConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> statues) {
		final EObject target = ctx.getTarget();
		if (target instanceof ElementAssignment) {
			final ElementAssignment elementAssignment = (ElementAssignment) target;

			final AVesselSet<Vessel> assignment = elementAssignment.getAssignment();
			if (assignment != null) {

				List<APortSet<Port>> inaccessiblePorts = null;

				if (assignment instanceof Vessel) {
					final Vessel vessel = (Vessel) assignment;
					inaccessiblePorts = vessel.getInaccessiblePorts();
					if (inaccessiblePorts.isEmpty()) {
						inaccessiblePorts = vessel.getVesselClass().getInaccessiblePorts();
					}
				} else if (assignment instanceof VesselClass) {
					final VesselClass vesselClass = (VesselClass) assignment;
					inaccessiblePorts = vesselClass.getInaccessiblePorts();
				}

				if (inaccessiblePorts != null) {
					final Set<Port> inaccessiblePortSet = SetUtils.getObjects(inaccessiblePorts);
					if (!inaccessiblePortSet.isEmpty()) {

						final UUIDObject object = elementAssignment.getAssignedObject();
						if (object instanceof Cargo) {
							final Cargo cargo = (Cargo) object;
							for (final Slot slot : cargo.getSlots()) {
								if (inaccessiblePortSet.contains(slot.getPort())) {
									final String msg = String.format("The port %s is not an accessible port for the assigned vessel", slot.getPort().getName());
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
									dsd.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
									dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
									statues.add(dsd);
								}
							}
						} else if (object instanceof VesselEvent) {
							final VesselEvent vesselEvent = (VesselEvent) object;
							if (inaccessiblePortSet.contains(vesselEvent.getPort())) {
								final String msg = String.format("The port %s is not an accessible port for the assigned vessel", vesselEvent.getPort().getName());
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
								dsd.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
								dsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_Port());
								statues.add(dsd);
							}
							if (vesselEvent instanceof CharterOutEvent) {
								final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
								if (inaccessiblePortSet.contains(charterOutEvent.getRelocateTo())) {
									final String msg = String.format("The port %s is not an accessible port for the assigned vessel", charterOutEvent.getRelocateTo().getName());
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
									dsd.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
									dsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getCharterOutEvent_RelocateTo());
									statues.add(dsd);
								}
							}
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}

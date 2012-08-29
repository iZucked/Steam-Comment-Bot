/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class AllowedVesselAssignmentConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (object instanceof ElementAssignment) {
			final ElementAssignment assignment = (ElementAssignment) object;

			final UUIDObject assignedObject = assignment.getAssignedObject();

			if (assignment.getAssignment() == null) {
				return ctx.createSuccessStatus();
			}

			final Set<AVessel> vessels = SetUtils.getVessels(assignment.getAssignment());

			Set<AVessel> allowedVessels = null;
			if (assignedObject instanceof Cargo) {
				final Cargo cargo = (Cargo) assignedObject;
				allowedVessels = SetUtils.getVessels(cargo.getAllowedVessels());
			} else if (assignedObject instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) assignedObject;
				allowedVessels = SetUtils.getVessels(vesselEvent.getAllowedVessels());
			}
			if (allowedVessels == null || allowedVessels.isEmpty()) {
				return ctx.createSuccessStatus();
			}

			allowedVessels.retainAll(vessels);

			if (allowedVessels.isEmpty()) {

				final String message;
				if (assignedObject instanceof Cargo) {
					message = String.format("Element Assignment for Cargo %s requires vessel(s) not in the allowed vessels list.", ((Cargo) assignedObject).getName());
				} else if (assignedObject instanceof VesselEvent) {
					message = String.format("Element Assignment for Vessel Event %s requires vessel(s) not in the allowed vessels list.", ((VesselEvent) assignedObject).getName());
				} else {
					throw new IllegalStateException("Unexpected code branch.");
				}
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				failure.addEObjectAndFeature(assignment, InputPackage.eINSTANCE.getElementAssignment_Assignment());
				if (assignedObject instanceof Cargo) {
					failure.addEObjectAndFeature(assignedObject, CargoPackage.eINSTANCE.getCargo_AllowedVessels());
				} else if (assignedObject instanceof VesselEvent) {
					failure.addEObjectAndFeature(assignedObject, FleetPackage.eINSTANCE.getVesselEvent_AllowedVessels());
				}

				failures.add(failure);
			}
		}
		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.validation.internal.Activator;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class AllowedVesselAssignmentConstraint extends AbstractModelConstraint {

	private static final Logger log = LoggerFactory.getLogger(AllowedVesselAssignmentConstraint.class);

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

			final AVesselSet vesselAssignment = assignment.getAssignment();
			if (vesselAssignment == null) {
				if (assignedObject instanceof VesselEvent) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Vessel events must have a vessel assigned to them."));
					status.addEObjectAndFeature(assignment, InputPackage.eINSTANCE.getElementAssignment_Assignment());
					return status;
				} else
					return ctx.createSuccessStatus();
			}

			// This will be a single vessel or a vessel class
			if (!(vesselAssignment instanceof Vessel || vesselAssignment instanceof VesselClass)) {
				// Unsupported case - bail out!
				log.error("ElementAssignment as non Vessel or VesselClass assignment - unable to validate");
				return ctx.createSuccessStatus();
			}

			EList<AVesselSet> allowedVessels = null;
			if (assignedObject instanceof Cargo) {
				final Cargo cargo = (Cargo) assignedObject;
				allowedVessels = cargo.getAllowedVessels();
			} else if (assignedObject instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) assignedObject;
				allowedVessels = vesselEvent.getAllowedVessels();
			}
			if (allowedVessels == null || allowedVessels.isEmpty()) {
				return ctx.createSuccessStatus();
			}

			// Expand out VesselGroups
			final Set<AVesselSet> expandedVessels = new HashSet<AVesselSet>();
			for (final AVesselSet s : allowedVessels) {
				if (s instanceof Vessel) {
					expandedVessels.add(s);
				} else if (s instanceof VesselClass) {
					expandedVessels.add(s);
				} else {
					expandedVessels.addAll(SetUtils.getVessels(s));
				}
			}

			boolean permitted = false;
			if (expandedVessels.contains(vesselAssignment)) {
				permitted = true;
			} else if (vesselAssignment instanceof Vessel) {
				final Vessel vessel = (Vessel) vesselAssignment;
				for (final AVesselSet vs : expandedVessels) {
					if (vs instanceof VesselClass) {
						if (vs == vessel.getVesselClass()) {
							permitted = true;
							break;
						}
					}
				}
			}

			if (!permitted) {

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

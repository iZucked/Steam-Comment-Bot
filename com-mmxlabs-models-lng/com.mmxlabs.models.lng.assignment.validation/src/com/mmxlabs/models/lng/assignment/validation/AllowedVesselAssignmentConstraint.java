/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
# * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Constraint to check the Assigned Vessel is in the allowed vessel list, if specified.
 * 
 */
public class AllowedVesselAssignmentConstraint extends AbstractModelMultiConstraint {

	private static final Logger log = LoggerFactory.getLogger(AllowedVesselAssignmentConstraint.class);

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;

			final AVesselSet<? extends Vessel> vesselAssignment = assignableElement.getAssignment();
			if (vesselAssignment == null) {
				if (assignableElement instanceof VesselEvent) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Vessel events must have a vessel assigned to them."));
					status.addEObjectAndFeature(assignableElement, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
					failures.add(status);
					return Activator.PLUGIN_ID;
				} else
					return Activator.PLUGIN_ID;
			}

			// This will be a single vessel or a vessel class
			if (!(vesselAssignment instanceof Vessel || vesselAssignment instanceof VesselClass)) {
				// Unsupported case - bail out!
				log.error("Assignment is not a Vessel or VesselClass - unable to validate");
				return Activator.PLUGIN_ID;
			}

			EList<AVesselSet<Vessel>> allowedVessels = null;
			if (assignableElement instanceof Cargo) {
				final Cargo cargo = (Cargo) assignableElement;
				allowedVessels = cargo.getAllowedVessels();
			} else if (assignableElement instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) assignableElement;
				allowedVessels = vesselEvent.getAllowedVessels();
			}
			if (allowedVessels == null || allowedVessels.isEmpty()) {
				return Activator.PLUGIN_ID;
			}

			// Expand out VesselGroups
			final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<AVesselSet<Vessel>>();
			for (final AVesselSet<Vessel> s : allowedVessels) {
				if (s instanceof Vessel) {
					expandedVessels.add(s);
				} else if (s instanceof VesselClass) {
					expandedVessels.add(s);
				} else {
					expandedVessels.addAll(SetUtils.getObjects(s));
				}
			}

			boolean permitted = false;
			if (expandedVessels.contains(vesselAssignment)) {
				permitted = true;
			} else if (vesselAssignment instanceof Vessel) {
				final Vessel vessel = (Vessel) vesselAssignment;
				for (final AVesselSet<Vessel> vs : expandedVessels) {
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
				if (assignableElement instanceof Cargo) {
					message = String.format("Cargo '%s': Assignment '%s' is not in the allowed vessels list.", ((Cargo) assignableElement).getName(), vesselAssignment.getName());
				} else if (assignableElement instanceof VesselEvent) {
					message = String.format("Vessel Event '%s': Assignment requires vessel(s) not in the allowed vessels list.", ((VesselEvent) assignableElement).getName());
				} else {
					throw new IllegalStateException("Unexpected code branch.");
				}
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				failure.addEObjectAndFeature(assignableElement, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
				if (assignableElement instanceof Cargo) {
					failure.addEObjectAndFeature(assignableElement, CargoPackage.eINSTANCE.getCargo_AllowedVessels());
				} else if (assignableElement instanceof VesselEvent) {
					failure.addEObjectAndFeature(assignableElement, CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels());
				}

				failures.add(failure);
			}
		}

		return Activator.PLUGIN_ID;
	}
}

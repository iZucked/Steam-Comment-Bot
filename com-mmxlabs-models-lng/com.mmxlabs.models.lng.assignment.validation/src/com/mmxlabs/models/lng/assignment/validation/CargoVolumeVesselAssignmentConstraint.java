/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectReferencerCondition;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoVolumeVesselAssignmentConstraint extends AbstractModelMultiConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof ElementAssignment) {
			final ElementAssignment assignment = (ElementAssignment) object;

			final UUIDObject assignedObject = assignment.getAssignedObject();

			if (assignment.getAssignment() == null) {
				return Activator.PLUGIN_ID;
			}

			int capacity = -1;
			if (assignment.getAssignment() instanceof Vessel) {
				final Vessel vessel = (Vessel) assignment.getAssignment();
				capacity = vessel.getVesselOrVesselClassCapacity();
			} else if (assignment.getAssignment() instanceof VesselClass) {
				final VesselClass vesselClass = (VesselClass) assignment.getAssignment();
				capacity = vesselClass.getCapacity();
			} else {
				// Can't do much here, no capacity...
				return Activator.PLUGIN_ID;
			}

			final UUIDObject obj = assignment.getAssignedObject();
			if (obj instanceof Cargo) {
				final Cargo cargo = (Cargo) obj;
				for (final Slot slot : cargo.getSlots()) {

					if (slot.getSlotOrContractMinQuantity() > capacity) {

						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Slot|" + slot.getName() +"] has a minimum volume greater than the capacity of current vessel assignment"));
						failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
						failures.add(failure);
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}

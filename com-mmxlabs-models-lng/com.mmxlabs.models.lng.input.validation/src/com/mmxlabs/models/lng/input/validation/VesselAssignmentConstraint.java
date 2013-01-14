/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.validation;

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

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.validation.internal.Activator;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class VesselAssignmentConstraint extends AbstractModelConstraint {
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

			final Set<APort> restrictedPorts = new HashSet<APort>();
			boolean firstPass = true;
			for (final AVessel vessel : vessels) {
				if (vessel instanceof Vessel) {
					final Vessel vessel2 = (Vessel) vessel;
					final EList<APortSet> inaccessiblePorts = vessel2.getInaccessiblePorts();
					if (inaccessiblePorts.isEmpty()) {
						// At least one vessel has no restrictions.
						return ctx.createSuccessStatus();
					} else {
						final Set<APort> ports = SetUtils.getPorts(inaccessiblePorts);
						if (firstPass) {
							restrictedPorts.addAll(ports);
							firstPass = false;
						} else {
							restrictedPorts.retainAll(inaccessiblePorts);
						}
					}
				}

			}

			// Multiple vessels have different restrictions, but between them they can visit any port.
			if (restrictedPorts.isEmpty()) {
				return ctx.createSuccessStatus();
			}

			// Next stage, check the cargoes, slots and events to see if they contain a restricted port.
			for (final APort port : restrictedPorts) {
				final SELECT query = new SELECT(new FROM(assignedObject), new WHERE(new EObjectReferencerCondition(port)));
				final IQueryResult result = query.execute();

				if (!result.isEmpty()) {

					final Iterator<EObject> iterator = result.iterator();
					while (iterator.hasNext()) {

						final EObject target = iterator.next();
						String name = target.toString();
						if (target instanceof NamedObject) {
							name = ((NamedObject) target).getName();
						}

						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(target.eClass().getName() + " " + name + " has reference to inaccessible port "
								+ port.getName()));

						if (target instanceof Slot) {
							failure.addEObjectAndFeature(target, CargoPackage.eINSTANCE.getSlot_Port());
						}

						failures.add(failure);
					}
				}
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

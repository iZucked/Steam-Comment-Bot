/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectReferencerCondition;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class VesselAssignmentConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof ElementAssignment) {
			final ElementAssignment assignment = (ElementAssignment) object;

			final UUIDObject assignedObject = assignment.getAssignedObject();

			if (assignment.getAssignment() == null) {
				return Activator.PLUGIN_ID;
			}

			final Set<Vessel> vessels = SetUtils.getObjects(assignment.getAssignment());

			final Set<Port> restrictedPorts = new HashSet<Port>();
			boolean firstPass = true;
			for (final Vessel vessel : vessels) {
				final EList<APortSet<Port>> inaccessiblePorts = vessel.getInaccessiblePorts();
				if (inaccessiblePorts.isEmpty()) {
					// At least one vessel has no restrictions.
					return Activator.PLUGIN_ID;
				} else {
					final Set<Port> ports = SetUtils.getObjects(inaccessiblePorts);
					if (firstPass) {
						restrictedPorts.addAll(ports);
						firstPass = false;
					} else {
						restrictedPorts.retainAll(inaccessiblePorts);
					}
				}
			}

			// Multiple vessels have different restrictions, but between them they can visit any port.
			if (restrictedPorts.isEmpty()) {
				return Activator.PLUGIN_ID;
			}

			// Next stage, check the cargoes, slots and events to see if they contain a restricted port.
			for (final Port port : restrictedPorts) {
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

						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(target.eClass().getName() + " " + name
								+ " has reference to inaccessible port " + port.getName()));

						if (target instanceof Slot) {
							failure.addEObjectAndFeature(target, CargoPackage.eINSTANCE.getSlot_Port());
						}

						failures.add(failure);
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}

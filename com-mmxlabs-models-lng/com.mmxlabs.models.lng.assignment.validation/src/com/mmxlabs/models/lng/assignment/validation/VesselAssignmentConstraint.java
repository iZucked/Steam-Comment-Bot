/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.Collections;
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

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VesselAssignmentConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignment = (AssignableElement) object;

			final VesselAssignmentType vesselAssignmentType = assignment.getVesselAssignmentType();
			if (vesselAssignmentType == null) {
				return Activator.PLUGIN_ID;
			}

			final Set<? extends Vessel> vessels;
			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				vessels = SetUtils.<AVesselSet<Vessel>, Vessel> getObjects(charterInMarket.getVesselClass());
			} else if (vesselAssignmentType instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
				vessels = Collections.singleton(vesselAvailability.getVessel());
			} else {
				return Activator.PLUGIN_ID;
			}

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
				final SELECT query = new SELECT(new FROM(assignment), new WHERE(new EObjectReferencerCondition(port)));
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

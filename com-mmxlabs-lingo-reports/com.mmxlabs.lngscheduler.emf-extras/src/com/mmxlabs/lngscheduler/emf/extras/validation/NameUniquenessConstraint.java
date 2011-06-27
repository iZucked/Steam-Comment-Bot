/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import scenario.NamedObject;
import scenario.ScenarioPackage;
import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.fleet.FleetPackage;
import scenario.fleet.VesselEvent;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

/**
 * Ensures that the name attribute of all {@link NamedObject}s in a collection
 * are unique.
 * 
 * TODO use ctx.addResults and ctx.skipCurrentConstraintFor...
 * 
 * @author Tom Hinton
 * 
 */
public class NameUniquenessConstraint extends AbstractModelConstraint {
	private IStatus validate(final IValidationContext ctx,
			final EAttribute nameAttribute) {
		final EObject target = ctx.getTarget();

		final EObject container = target.eContainer();
		if (container == null) return ctx.createSuccessStatus(); //TODO sort out this issue
		final EStructuralFeature feature = target.eContainingFeature();
		if (feature != null && feature.isMany()
				&& feature instanceof EReference
				&& ((EReference) feature).getEReferenceType()
						.getEAllAttributes().contains(nameAttribute)) {

			Map<EObject, Set<String>> badNames = (Map<EObject, Set<String>>) ctx
					.getCurrentConstraintData();
			if (badNames == null) {
				badNames = new HashMap<EObject, Set<String>>();
				ctx.putCurrentConstraintData(badNames);
			}

			Set<String> bad = badNames.get(container);
			if (bad == null) {
				bad = new HashSet<String>();
				badNames.put(container, bad);
				final EList<EObject> objects = (EList<EObject>) container
						.eGet(feature);
				final Set<String> temp = new HashSet<String>();
				for (final EObject no : objects) {
					final String n = (String) no.eGet(nameAttribute);
					if (temp.contains(n)) {
						bad.add(n);
					}
					temp.add(n);
				}
			}

			final String name = (String) target.eGet(nameAttribute);
			if (bad.contains(name)) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(target
								.eClass().getName(), name));
					dsd.addEObjectAndFeature(target, nameAttribute);
						return dsd;
			}
		}
		return ctx.createSuccessStatus();
	}

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof NamedObject) {
			return validate(ctx, ScenarioPackage.eINSTANCE.getNamedObject_Name());
		} else if (target instanceof Cargo) {
			return validate(ctx, CargoPackage.eINSTANCE.getCargo_Id());
		} else if (target instanceof VesselEvent) {
			return validate(ctx, FleetPackage.eINSTANCE.getVesselEvent_Id());
		}
		return ctx.createSuccessStatus();
	}

}

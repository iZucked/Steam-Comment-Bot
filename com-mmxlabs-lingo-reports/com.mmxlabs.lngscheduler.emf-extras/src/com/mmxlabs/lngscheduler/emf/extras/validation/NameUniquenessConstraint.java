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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

import scenario.NamedObject;
import scenario.ScenarioPackage;

/**
 * Ensures that the name attribute of all {@link NamedObject}s in a collection are unique.
 * 
 * @author Tom Hinton
 *
 */
public class NameUniquenessConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof NamedObject) {
			final NamedObject named = (NamedObject) target;
			final EObject container = named.eContainer();
			final EStructuralFeature feature = named.eContainingFeature();
			if (feature.isMany() && feature instanceof EReference && 
					ScenarioPackage.eINSTANCE.getNamedObject().isSuperTypeOf(
					((EReference) feature).getEReferenceType())) {
				
				Map<EObject, Set<String>> badNames = (Map<EObject, Set<String>>) ctx.getCurrentConstraintData();
				if (badNames == null) {
					badNames = new HashMap<EObject, Set<String>>();
					ctx.putCurrentConstraintData(badNames);
				}
				
				Set<String> bad = badNames.get(container);
				if (bad == null) {
					bad = new HashSet<String>();
					badNames.put(container, bad);
					final EList<NamedObject> objects = (EList<NamedObject>) container.eGet(feature);
					final Set<String> temp = new HashSet<String>();
					for (final NamedObject no : objects) {
						final String n = no.getName();
						if (temp.contains(n)) {
							bad.add(n);
						}
						temp.add(n);
					}
				}
				
				if (bad.contains(named.getName())) {
					return new DetailConstraintStatusDecorator(
							(IConstraintStatus)
							ctx.createFailureStatus(named.eClass().getName(), named.getName()),
							named, ScenarioPackage.eINSTANCE.getNamedObject_Name().getName());
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}

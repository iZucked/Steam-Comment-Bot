/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.validation.context.ValidationSupport;

/**
 * Ensures that the name attribute of all {@link NamedObject}s in a collection are unique.
 * 
 * TODO use ctx.addResults and ctx.skipCurrentConstraintFor...
 * 
 * @author Tom Hinton
 * 
 */
public class NameUniquenessConstraint extends AbstractModelConstraint {
	private IStatus validate(final IValidationContext ctx, final EAttribute nameAttribute) {
		final EObject target = ctx.getTarget();

		final Pair<EObject, EReference> containerAndFeature = ValidationSupport.getInstance().getContainer(target);

		final EObject container = containerAndFeature.getFirst(); // target.eContainer();
		if (container == null)
			return ctx.createSuccessStatus(); // TODO sort out this issue
		final EStructuralFeature feature = containerAndFeature.getSecond();
		if (feature != null && feature.isMany() && feature instanceof EReference && ((EReference) feature).getEReferenceType().getEAllAttributes().contains(nameAttribute)) {

			Map<EObject, Set<String>> badNames = (Map<EObject, Set<String>>) ctx.getCurrentConstraintData();
			if (badNames == null) {
				badNames = new HashMap<EObject, Set<String>>();
				ctx.putCurrentConstraintData(badNames);
			}

			Set<String> bad = badNames.get(container);
			if (bad == null) {
				bad = new HashSet<String>();
				badNames.put(container, bad);
				final List<EObject> objects = ValidationSupport.getInstance().getContents(container, (EReference) feature);
				// container
				// .eGet(feature);
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
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(target.eClass().getName(), name));
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
			return validate(ctx, MMXCorePackage.eINSTANCE.getNamedObject_Name());
		}
		return ctx.createSuccessStatus();
	}

}

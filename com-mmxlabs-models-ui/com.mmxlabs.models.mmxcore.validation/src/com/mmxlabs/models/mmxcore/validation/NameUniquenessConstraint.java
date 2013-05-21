/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.OtherNamesObject;
import com.mmxlabs.models.mmxcore.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Ensures that the name attribute of all {@link NamedObject}s in a collection are unique.
 * 
 * TODO use ctx.addResults and ctx.skipCurrentConstraintFor...
 * 
 * @author Tom Hinton
 * 
 */
public class NameUniquenessConstraint extends AbstractModelConstraint {
	private IStatus validate(final IValidationContext ctx, final EAttribute nameAttribute, @Nullable final EAttribute otherNamesAttribute) {
		final EObject target = ctx.getTarget();
		final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
		final Pair<EObject, EReference> containerAndFeature = new Pair<EObject, EReference>(extraValidationContext.getContainer(target), extraValidationContext.getContainment(target));

		final EObject container = containerAndFeature.getFirst(); // target.eContainer();
		if (container == null)
			return ctx.createSuccessStatus();
		final EStructuralFeature feature = containerAndFeature.getSecond();
		if (feature != null && feature.isMany() && feature instanceof EReference && ((EReference) feature).getEReferenceType().getEAllAttributes().contains(nameAttribute)) {

			final EAnnotation eAnnotation = target.eClass().getEAnnotation("http://www.mmxlabs.com/models/mmxcore/validation/NamedObject");
			if (eAnnotation != null) {
				if (Boolean.valueOf(eAnnotation.getDetails().get("nonUnique"))) {
					return ctx.createSuccessStatus();
				}
			}

			Map<Pair<EObject, EReference>, Set<String>> badNames = (Map<Pair<EObject, EReference>, Set<String>>) ctx.getCurrentConstraintData();
			if (badNames == null) {
				badNames = new HashMap<Pair<EObject, EReference>, Set<String>>();
				ctx.putCurrentConstraintData(badNames);
			}

			Set<String> bad = badNames.get(containerAndFeature);
			if (bad == null) {
				bad = new HashSet<String>();
				badNames.put(containerAndFeature, bad);
				final List<EObject> objects = extraValidationContext.getSiblings(target);

				final Set<String> temp = new HashSet<String>();
				for (final EObject no : objects) {
					final String n = (String) no.eGet(nameAttribute);
					if (temp.contains(n)) {
						bad.add(n);
					}
					temp.add(n);
					if (otherNamesAttribute != null) {
						final Collection<String> names = (Collection<String>) no.eGet(otherNamesAttribute);
						for (final String ns : names) {
							if (n.equals(ns)) {
								continue;
							}
							if (temp.contains(ns)) {
								bad.add(ns);
							}
							temp.add(ns);
						}
					}
				}
			}

			final String name = (String) target.eGet(nameAttribute);
			if (bad.contains(name)) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(target.eClass().getName() + " has non-unique name " + name));
				dsd.addEObjectAndFeature(target, nameAttribute);
				return dsd;
			}
			if (otherNamesAttribute != null) {
				final Collection<String> names = (Collection<String>) target.eGet(otherNamesAttribute);
				for (final String ns : names) {
					if (name.equals(ns)) {
						continue;
					}
					if (bad.contains(ns)) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(target.eClass().getName() + " " + name
								+ " has non-unique other name " + ns));
						dsd.addEObjectAndFeature(target, nameAttribute);
						return dsd;
					}
				}
			}
		}
		return ctx.createSuccessStatus();
	}

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof OtherNamesObject) {
			return validate(ctx, MMXCorePackage.eINSTANCE.getNamedObject_Name(), MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames());
		} else if (target instanceof NamedObject) {
			return validate(ctx, MMXCorePackage.eINSTANCE.getNamedObject_Name(), null);
		}
		return ctx.createSuccessStatus();
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint to check that id / name fields are all set (BugzID:288)
 * 
 * @author hinton
 * 
 */
public class NameValidityConstraint extends AbstractModelMultiConstraint {
	
	private static final EAttribute attribute = MMXCorePackage.eINSTANCE.getNamedObject_Name();
	
	@Override
	public void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses){
		final EObject target = ctx.getTarget();
		final Pair<EObject, EReference> containerAndFeature = Pair.of(extraContext.getContainer(target), extraContext.getContainment(target));

		final EObject container = containerAndFeature.getFirst();
		if (container == null) {
			return;
		}
		final EStructuralFeature feature = containerAndFeature.getSecond();
		if (feature != null && feature instanceof EReference && ((EReference) feature).getEReferenceType().getEAllAttributes().contains(attribute)) {
			final EAnnotation eAnnotation = feature.getEAnnotation("http://www.mmxlabs.com/models/mmxcore/validation/NamedObject");
			if (eAnnotation != null) {
				if (Boolean.valueOf(eAnnotation.getDetails().get("optionalName"))) {
					return;
				}
			}
		}
		statuses.add(validate(target, attribute, ctx));
	}

	/**
	 * Check whether the given attribute on the target object is null, empty, or whitespace. If it is any of these, error out
	 * 
	 * @param target
	 * @param attribute
	 * @param ctx
	 * @return validation status; success if attribute is a valid id/name
	 */
	private IStatus validate(final EObject target, final EAttribute attribute, final IValidationContext ctx) {
		final String name = (String) target.eGet(attribute);
		if (name == null || name.trim().isEmpty()) {
			final String msg = String.format("%s has an invalid %s - the %s must not be empty or composed of whitespace.", EditorUtils.unmangle(target), attribute.getName(), attribute.getName());
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			dsd.addEObjectAndFeature(target, attribute);
			return dsd;
		} else if (!name.equals(name.trim())) {
			final String msg = String.format("%s has an invalid %s -  remove leading and trailing whitespace.", EditorUtils.unmangle(target), attribute.getName());
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			dsd.addEObjectAndFeature(target, attribute);
			return dsd;
		}

		return ctx.createSuccessStatus();
	}
}

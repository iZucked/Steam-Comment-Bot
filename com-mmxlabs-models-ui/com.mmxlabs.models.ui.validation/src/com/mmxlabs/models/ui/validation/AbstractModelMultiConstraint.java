/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.internal.Activator;

/**
 * An abstract implementation of {@link AbstractModelConstraint} designed to allow multiple status messages to be reported.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractModelMultiConstraint extends AbstractModelConstraint {

	/**
	 * The real validate method call with the status list to populate.
	 * 
	 * @param ctx
	 * @param extraContext
	 * @param statuses
	 *            Output: Overriding methods should store any created validation status objects in this list.
	 * @return The calling Plugin/Bundle ID
	 */
	protected abstract String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses);

	@Override
	public IStatus validate(final IValidationContext ctx) {

		final List<IStatus> statuses = new LinkedList<IStatus>();

		final Activator activator = Activator.getDefault();
		final IExtraValidationContext extraValidationContext;
		if (activator == null) {
			// For unit tests outside of OSGi
			extraValidationContext = new DefaultExtraValidationContext((MMXRootObject) null, false);
		} else {
			extraValidationContext = activator.getExtraValidationContext();
		}
		final String pluginId = validate(ctx, extraValidationContext, statuses);

		if (statuses.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (statuses.size() == 1) {
			return statuses.get(0);
		} else {
			int code = IStatus.OK;
			for (final IStatus status : statuses) {
				if (status.getSeverity() > code) {
					code = status.getSeverity();
				}
			}

			final MultiStatus multi = new MultiStatus(pluginId, code, null, null);
			for (final IStatus s : statuses) {
				multi.add(s);
			}
			return multi;
		}
	}

	protected void createSimpleStatus(final IValidationContext ctx, @NonNull final List<IStatus> statuses, EObject target, EStructuralFeature feature, String message) {
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		dcsd.addEObjectAndFeature(target, feature);
		statuses.add(dcsd);
	}

	protected void createSimpleStatus(final IValidationContext ctx, @NonNull final List<IStatus> statuses, String message, EObject target, EStructuralFeature... features) {
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		for (EStructuralFeature feature : features) {
			dcsd.addEObjectAndFeature(target, feature);
		}
		statuses.add(dcsd);
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

/**
 * An abstract implementation of {@link AbstractModelConstraint} designed to allow multiple status messages to be reported.
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public abstract class AbstractModelMultiConstraint extends AbstractModelConstraint {

	/**
	 * The real validate method call with the status list to populate.
	 * 
	 * @param ctx
	 * @param failures
	 * @return The pluginID to pass into a {@link MultiStatus}.
	 */
	protected abstract String validate(final IValidationContext ctx, List<IStatus> statuses);

	@Override
	public IStatus validate(final IValidationContext ctx) {

		final List<IStatus> statuses = new LinkedList<IStatus>();

		final String pluginId = validate(ctx, statuses);

		if (statuses.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (statuses.size() == 1) {
			return statuses.get(0);
		} else {
			int code = Status.OK;
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

}

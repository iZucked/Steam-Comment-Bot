/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.impl;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.model.IModelConstraint;

/**
 * Extracted out of an API class
 */
public final class Multi extends MultiStatus implements IConstraintStatus {
	private final IModelConstraint constraint;
	private final EObject target;
	private final Set<EObject> resultLocus;

	public Multi(String pluginId, int code, IStatus[] children, String message) {
		super(pluginId, code, children, message, null);

		IConstraintStatus prototype = null;

		for (IStatus element : children) {
			if (element instanceof IConstraintStatus) {
				prototype = (IConstraintStatus) element;
				break;
			}
		}

		if (prototype == null) {
			constraint = null;
			target = null;
			resultLocus = Collections.emptySet();
		} else {
			constraint = prototype.getConstraint();
			target = prototype.getTarget();
			resultLocus = Collections.singleton(target);
		}
	}

	public IModelConstraint getConstraint() {
		return constraint;
	}

	public EObject getTarget() {
		return target;
	}

	public Set<EObject> getResultLocus() {
		return resultLocus;
	}
}
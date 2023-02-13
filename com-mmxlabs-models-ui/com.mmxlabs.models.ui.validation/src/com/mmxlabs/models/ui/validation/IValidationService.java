/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.jdt.annotation.Nullable;

/**
 */
public interface IValidationService {
	default IStatus runValidation(final IValidator<EObject> validator, final IExtraValidationContext extraContext, final EObject target, @Nullable EObject extraTarget) {
		return runValidation(validator, extraContext, Collections.singleton(target), extraTarget);
	}

	IStatus runValidation(IValidator<EObject> validator, IExtraValidationContext extraContext, Collection<? extends EObject> targets, @Nullable EObject extraTarget);

	/**
	 * Returns the registered {@link IExtraValidationContext} for the current thread
	 * 
	 * @return
	 */
	IExtraValidationContext getExtraContext();
}

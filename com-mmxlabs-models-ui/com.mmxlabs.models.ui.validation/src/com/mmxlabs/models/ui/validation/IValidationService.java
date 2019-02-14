/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.IValidator;

/**
 */
public interface IValidationService {
	default IStatus runValidation(final IValidator<EObject> validator, final IExtraValidationContext extraContext, final EObject target) {
		return runValidation(validator, extraContext, Collections.singleton(target));
	}

	IStatus runValidation(final IValidator<EObject> validator, final IExtraValidationContext extraContext, final Collection<? extends EObject> targets);

	/**
	 * Returns the registered {@link IExtraValidationContext} for the current thread
	 * 
	 * @return
	 */
	IExtraValidationContext getExtraContext();
}

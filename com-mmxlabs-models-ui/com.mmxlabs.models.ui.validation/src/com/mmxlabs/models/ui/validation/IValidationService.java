/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.jdt.annotation.Nullable;

/**
 */
public interface IValidationService {
	IStatus runValidation(IValidator<EObject> validator, IExtraValidationContext extraContext, IValidationRootObjectTransformerService transformer, EObject rootObject, @Nullable EObject extraTarget);

	/**
	 * Returns the registered {@link IExtraValidationContext} for the current thread
	 * 
	 * @return
	 */
	IExtraValidationContext getExtraContext();
}

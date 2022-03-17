/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

/**
 * A helper interface for validation to avoid cyclic dependency issues elsewhere. Intended to be linked to an {@link IStatus} implementation to see if the given object is a parent of any objects
 * linked to this IStatus instance.
 * 
 * @author Simon Goodall
 *
 */
public interface IValidationApplicableProvider {

	/**
	 * If any objects in this IStatus instance are a child of the given parent, return true. Applies to this IStatus instance and not any children.
	 * 
	 * @param parent
	 * @return
	 */
	boolean doesValidationApplyToChildOf(EObject parent);
}

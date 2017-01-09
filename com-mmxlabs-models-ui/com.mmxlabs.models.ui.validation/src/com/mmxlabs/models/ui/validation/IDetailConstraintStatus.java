/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.model.IConstraintStatus;

/**
 * Extended version of {@link IConstraintStatus} adding additional details about
 * which {@link EObject}s and {@link EStructuralFeature}s the status refers to.
 * 
 * @author Simon Goodall
 * 
 */
public interface IDetailConstraintStatus extends IConstraintStatus {

	/**
	 * Returns the {@link Collection} of EObjects this status refers to.
	 * 
	 * @return
	 */
	Collection<EObject> getObjects();

	/**
	 * Returns the {@link Collection} if {@link EStructuralFeature} related to
	 * the {@link EObject}. This should be an {@link EObject} instance returned
	 * from {@link #getObjects()}
	 * 
	 * @return
	 */
	Collection<EStructuralFeature> getFeaturesForEObject(EObject object);

}

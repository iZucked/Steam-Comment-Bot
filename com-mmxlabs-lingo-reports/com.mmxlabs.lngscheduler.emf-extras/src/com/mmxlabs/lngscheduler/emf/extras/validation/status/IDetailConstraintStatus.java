/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation.status;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.model.IConstraintStatus;

/**
 * Extended version of {@link IConstraintStatus} adding additional details about
 * which {@link EStructuralFeature} of an {@link EObject} the status refers to.
 *
 * TODO: Extend to allow multiple object/feature pairs
 * 
 * @author Simon Goodall
 * 
 */
public interface IDetailConstraintStatus extends IConstraintStatus {

	/**
	 * Returns the name of the feature as defined by
	 * {@link EStructuralFeature#getName()} this status applies to.
	 * 
	 * @return
	 */
	EStructuralFeature getFeature();

	/**
	 * The {@link EObject} instance {@link #getFeature()} relates to. This may
	 * be different to {@link IConstraintStatus}{@link #getTarget()} if the
	 * {@link AbstractModelConstraint} checks references.
	 * 
	 * @return
	 */
	EObject getObject();
}

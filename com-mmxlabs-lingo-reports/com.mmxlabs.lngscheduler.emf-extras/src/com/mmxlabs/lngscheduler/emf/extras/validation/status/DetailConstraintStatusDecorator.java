/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation.status;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.model.IModelConstraint;

/**
 * Implementation of {@link IDetailConstraintStatus} wrapping a
 * {@link IConstraintStatus} instance and adding in the extra methods for the
 * {@link IDetailConstraintStatus} interface
 * 
 * @author Simon Goodall
 * 
 */
public class DetailConstraintStatusDecorator implements IDetailConstraintStatus {

	private final IConstraintStatus status;
	private final EStructuralFeature feature;
	private final EObject object;

	public DetailConstraintStatusDecorator(final IConstraintStatus status,
			final EObject object, final EStructuralFeature feature) {
		this.status = status;
		this.feature = feature;
		this.object = object;
	}

	@Override
	public IModelConstraint getConstraint() {
		return status.getConstraint();
	}

	@Override
	public EObject getTarget() {
		return status.getTarget();
	}

	@Override
	public Set<EObject> getResultLocus() {
		return status.getResultLocus();
	}

	@Override
	public IStatus[] getChildren() {
		return status.getChildren();
	}

	@Override
	public int getCode() {
		return status.getCode();
	}

	@Override
	public Throwable getException() {
		return status.getException();
	}

	@Override
	public String getMessage() {
		return status.getMessage();
	}

	@Override
	public String getPlugin() {
		return status.getPlugin();
	}

	@Override
	public int getSeverity() {
		return status.getSeverity();
	}

	@Override
	public boolean isMultiStatus() {
		return status.isMultiStatus();
	}

	@Override
	public boolean isOK() {
		return status.isOK();
	}

	@Override
	public boolean matches(final int severityMask) {
		return status.matches(severityMask);
	}

	@Override
	public EStructuralFeature getFeature() {
		return feature;
	}

	@Override
	public final EObject getObject() {
		return object;
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.model.IModelConstraint;

/**
 * Implementation of {@link IDetailConstraintStatus} wrapping a {@link IConstraintStatus} instance and adding in the extra methods for the {@link IDetailConstraintStatus} interface
 * 
 * @author Simon Goodall
 * 
 */
public class DetailConstraintStatusDecorator implements IDetailConstraintStatus {

	private final IConstraintStatus status;
	private final int severity;
	private final Map<EObject, Collection<EStructuralFeature>> objectMap = new HashMap<EObject, Collection<EStructuralFeature>>();

	public DetailConstraintStatusDecorator(final IConstraintStatus status) {
		this(status, status.getSeverity());
	}

	public DetailConstraintStatusDecorator(final IConstraintStatus status, final int severity) {
		this.status = status;
		this.severity = severity;
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
		return severity;
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
		return (severity & severityMask) != 0;
	}

	@Override
	public Collection<EStructuralFeature> getFeaturesForEObject(final EObject obj) {
		if (objectMap.containsKey(obj)) {
			return objectMap.get(obj);
		}
		return Collections.emptyList();
	}

	@Override
	public final Collection<EObject> getObjects() {
		return objectMap.keySet();
	}

	public void addEObjectAndFeature(final EObject obj, final EStructuralFeature feature) {
		final Collection<EStructuralFeature> features;
		if (objectMap.containsKey(obj)) {
			features = objectMap.get(obj);
		} else {
			features = new HashSet<EStructuralFeature>();
			objectMap.put(obj, features);
		}
		features.add(feature);
	}
}

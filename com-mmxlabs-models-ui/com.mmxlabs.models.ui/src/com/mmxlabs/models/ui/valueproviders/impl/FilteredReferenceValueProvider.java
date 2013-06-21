/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

public abstract class FilteredReferenceValueProvider implements IReferenceValueProvider {
	private final IReferenceValueProvider delegate;

	public FilteredReferenceValueProvider(IReferenceValueProvider delegate) {
		super();
		this.delegate = delegate;
	}

	public List<Pair<String, EObject>> getAllowedValues(EObject target,EStructuralFeature field) {
		List<Pair<String, EObject>> allowedValues = delegate.getAllowedValues(target, field);
		final ArrayList<Pair<String, EObject>> filtered = new ArrayList<Pair<String, EObject>>(allowedValues.size());
		
		for (final Pair<String, EObject> p : allowedValues) {
			if (isAllowedValue(target, field, p)) filtered.add(p);
		}
		
		return filtered;
	}

	public String getName(EObject referer, EReference feature,
			EObject referenceValue) {
		return delegate.getName(referer, feature, referenceValue);
	}

	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer,
			EReference feature, EObject referenceValue) {
		return delegate.getNotifiers(referer, feature, referenceValue);
	}

	public boolean updateOnChangeToFeature(Object changedFeature) {
		return delegate.updateOnChangeToFeature(changedFeature);
	}

	public void dispose() {
		delegate.dispose();
	}
	
	protected abstract boolean isAllowedValue(final EObject target, final EStructuralFeature field, final Pair<String, EObject> value);
}

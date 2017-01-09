/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;

public abstract class FilteredReferenceValueProvider implements IReferenceValueProvider {
	private final IReferenceValueProvider delegate;

	public FilteredReferenceValueProvider(IReferenceValueProvider delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public List<Pair<String, EObject>> getAllowedValues(EObject target,EStructuralFeature field) {
		List<Pair<String, EObject>> allowedValues = delegate.getAllowedValues(target, field);
		final ArrayList<Pair<String, EObject>> filtered = new ArrayList<Pair<String, EObject>>(allowedValues.size());
		
		for (final Pair<String, EObject> p : allowedValues) {
			if (isAllowedValue(target, field, p)) filtered.add(p);
		}
		
		return filtered;
	}

	@Override
	public String getName(EObject referer, EReference feature,
			EObject referenceValue) {
		return delegate.getName(referer, feature, referenceValue);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer,
			EReference feature, EObject referenceValue) {
		return delegate.getNotifiers(referer, feature, referenceValue);
	}

	@Override
	public boolean updateOnChangeToFeature(Object changedFeature) {
		return delegate.updateOnChangeToFeature(changedFeature);
	}

	@Override
	public void dispose() {
		delegate.dispose();
	}
	
	protected abstract boolean isAllowedValue(final EObject target, final EStructuralFeature field, final Pair<String, EObject> value);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;

public abstract class AbstractIdentityReferenceValueProviderWrapper implements IReferenceValueProvider {

	@Override
	public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
		return getReferenceValueProvider().getAllowedValues(target, field);
	}

	@Override
	public String getName(EObject referer, EReference feature, EObject referenceValue) {
		return getReferenceValueProvider().getName(referer, feature, referenceValue);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer, EReference feature, EObject referenceValue) {
		return getReferenceValueProvider().getNotifiers(referer, feature, referenceValue);
	}

	@Override
	public boolean updateOnChangeToFeature(Object changedFeature) {
		return getReferenceValueProvider().updateOnChangeToFeature(changedFeature);
	}

	@Override
	public void dispose() {
		getReferenceValueProvider().dispose();
	}

	protected abstract IReferenceValueProvider getReferenceValueProvider();
}

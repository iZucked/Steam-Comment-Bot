/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class OptionalAvailabilityShippingOptionPortValueProvider extends SimpleReferenceValueProvider{

	public OptionalAvailabilityShippingOptionPortValueProvider(EObject container, EReference containingReference) {
		super(container, containingReference);
	}

	@Override
	protected Pair<String, EObject> getEmptyObject() {
		return new Pair<>("<None>", null);
	}
	
	@Override
	public List<Pair<String, EObject>> getAllowedValues(EObject target,
			EStructuralFeature field) {
		if (cachedValues == null) {
			cacheValues();
		}
		return cachedValues;
	}

	@Override
	protected boolean isRelevantTarget(Object target, Object feature) {
		return (super.isRelevantTarget(target, feature) && (containingReference
				.getEReferenceType().isSuperTypeOf(((EObject) target)
				.eClass())))
				|| feature == containingReference;
	}

	@Override
	protected void cacheValues() {
		cachedValues = getSortedNames(getObjects(), nameAttribute);
		final Pair<String, EObject> none = getEmptyObject();
		if (none != null)
			cachedValues.add(0, none);
	}

	protected EList<? extends EObject> getObjects() {
		return (EList<? extends EObject>) container.eGet(containingReference);
	}

	@Override
	public void dispose() {
		container.eAdapters().remove(this);
	}

}

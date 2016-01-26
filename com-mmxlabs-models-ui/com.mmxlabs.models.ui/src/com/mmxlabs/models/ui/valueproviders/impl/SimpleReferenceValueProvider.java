/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders.impl;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;

/**
 * A default reference value provider implementation, which tracks references contained in an EObject.
 * 
 * It is an adapter on the containing EObject.
 * 
 * @author hinton
 *
 */
public class SimpleReferenceValueProvider extends BaseReferenceValueProvider {
	protected List<Pair<String, EObject>> cachedValues = null;
	protected final EReference containingReference;
	protected final EObject container;

	public SimpleReferenceValueProvider(final EObject container, final EReference containingReference) {
		this.containingReference = containingReference;
		this.container = container;
		container.eAdapters().add(this);
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

	protected Pair<String, EObject> getEmptyObject() {
		return null;
	}

	protected EList<? extends EObject> getObjects() {
		return (EList<? extends EObject>) container.eGet(containingReference);
	}

	@Override
	public void dispose() {
		container.eAdapters().remove(this);
	}
}

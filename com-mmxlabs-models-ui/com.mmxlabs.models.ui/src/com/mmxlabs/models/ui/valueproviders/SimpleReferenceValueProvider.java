/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.InternalEList;

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
	public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
		if (cachedValues == null) {
			cacheValues();
		}
		return cachedValues;
	}

	@Override
	protected boolean isRelevantTarget(final Object target, final Object feature) {
		return (super.isRelevantTarget(target, feature) && (containingReference.getEReferenceType().isSuperTypeOf(((EObject) target).eClass()))) || feature == containingReference;
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

	/**
	 * Handles installation of the adapter on an EObject by adding the adapter to each of the directly contained objects.
	 */
	@Override
	protected void setTarget(EObject target) {
		basicSetTarget(target);
		if (target == container) {
			for (Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
				Notifier notifier = i.next();
				addAdapter(notifier);
			}
		}
	}

	/**
	 * Handles undoing the installation of the adapter from an EObject by removing the adapter from each of the directly contained objects.
	 */
	@Override
	protected void unsetTarget(EObject target) {
		basicUnsetTarget(target);
		if (target == container) {

			for (Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<EObject>) target.eContents()).basicIterator(); i.hasNext();) {
				Notifier notifier = i.next();
				removeAdapter(notifier, false, true);
			}
		}
	}
}

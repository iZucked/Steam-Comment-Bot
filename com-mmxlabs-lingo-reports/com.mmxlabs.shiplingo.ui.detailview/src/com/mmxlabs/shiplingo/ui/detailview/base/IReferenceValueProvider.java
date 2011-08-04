package com.mmxlabs.shiplingo.ui.detailview.base;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;

public interface IReferenceValueProvider {
	public List<Pair<String, EObject>> getAllowedValues(final EObject target,
			final EStructuralFeature field);

	public String getName(final EObject referer, final EReference feature,
			final EObject referenceValue);

	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
			final EObject referer, final EReference feature,
			final EObject referenceValue);

	/**
	 * Return true if labels/values for a feature on an object should be updated
	 * when the given separate feature is changed on that object.
	 * 
	 * @param changedFeature a feature which might be changed
	 * @return whether to update
	 */
	public boolean updateOnChangeToFeature(Object changedFeature);
}
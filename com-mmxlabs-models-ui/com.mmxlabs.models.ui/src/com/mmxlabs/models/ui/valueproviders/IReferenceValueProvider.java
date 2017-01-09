/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;

/**
 * Instances provide EObjects, usually of a given type, and display-friendly
 * names for those EObjects.
 * 
 * @author hinton
 * 
 */
public interface IReferenceValueProvider {
	/**
	 * Get the values which are allowed for the given field on the target
	 * EObject.
	 * 
	 * Returns a list of Pair<String, EObject>, whose string part is the display
	 * name for the value and EObject part the actual value itself.
	 * 
	 * @param target
	 * @param field
	 * @return
	 */
	public List<Pair<String, EObject>> getAllowedValues(final EObject target,
			final EStructuralFeature field);

	/**
	 * Get the display name for a value if it were set to the given feature of
	 * the given object
	 * 
	 * @param referer
	 *            the object containing the feature
	 * @param feature
	 *            the feature on referer
	 * @param referenceValue
	 *            the value for which the caller wants a name
	 * @return a name for referenceValue
	 */
	public String getName(final EObject referer, final EReference feature,
			final EObject referenceValue);

	/**
	 * Get the list of notifiers and associated features which should be watched
	 * to know when the given value's name might change on the given
	 * referer/feature.
	 * 
	 * @param referer
	 *            the object holding referenceValue in feature
	 * @param feature
	 *            the feature on referer
	 * @param referenceValue
	 *            the value
	 * @return an iterable of pairs, having first element a notifier and second
	 *         element a list of features on that notifier. When a notification
	 *         is raised by the notifier for one of the features in the list,
	 *         the result of {@link #getName(EObject, EReference, EObject)} may
	 *         have changed.
	 */
	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
			final EObject referer, final EReference feature,
			final EObject referenceValue);

	/**
	 * Return true if labels/values for a feature on an object should be updated
	 * when the given separate feature is changed on that object.
	 * 
	 * @param changedFeature
	 *            a feature which might be changed
	 * @return whether to update
	 */
	public boolean updateOnChangeToFeature(Object changedFeature);

	public void dispose();
}
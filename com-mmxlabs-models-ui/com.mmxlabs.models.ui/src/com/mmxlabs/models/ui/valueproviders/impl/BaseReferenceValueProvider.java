/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * A basic reference value provider implementation, which extends {@link EContentAdapter} in order to keep up-to-date
 * on changes to the reference values which it is displaying. The idea here is that typically reference values come from
 * a containment reference on an EObject, so this can be added as an adapter to the containing object (or objects). Subclasses
 * then need to override {@link #isRelevantTarget(Object, Object)} to figure out whether a notification should trigger a re-caching
 * of any cached values.
 * 
 * @author hinton
 *
 */
public abstract class BaseReferenceValueProvider extends MMXContentAdapter implements IReferenceValueProvider {
	/**
	 * An attribute used to get the name for objects being rendered. Almost always going to be namedObject name.
	 */
	protected final EAttribute nameAttribute;
	
	/**
	 * A comparator used to sort on the string in a pair of strings and ?s
	 */
	protected final Comparator<Pair<String, ?>> comparator = createComparator();

	protected Comparator<Pair<String, ?>> createComparator() {
		return new Comparator<Pair<String, ?>>() {
			@Override
			public int compare(Pair<String, ?> o1, Pair<String, ?> o2) {
				return o1.getFirst().compareTo(o2.getFirst());
			}
		};
	}
	
	/**
	 * Equivalent to {@link #BaseReferenceValueProvider(EAttribute)} with <code>MMXCorePackage.eINSTANCE.getNamedObject_Name()</code>
	 */
	protected BaseReferenceValueProvider() {
		this(MMXCorePackage.eINSTANCE.getNamedObject_Name());
	}
	
	/**
	 * Creates an instance which will use the given attribute to get the display name for referenced objects.
	 * @param nameAttribute
	 */
	protected BaseReferenceValueProvider(EAttribute nameAttribute) {
		this.nameAttribute = nameAttribute;
	}
	
	/**
	 * Subclasses should use this method to update any caches they may have.
	 */
	protected abstract void cacheValues();
	
	/**
	 * A utility method for subclasses; given a list of possible reference values and a name attribute, returns a of
	 * Pair<String, EObject> where the String is the name and the EObject the value, sorted by name.
	 * @param objects
	 * @param nameAttribute
	 * @return
	 */
	protected ArrayList<Pair<String, EObject>> getSortedNames(
			final EList<? extends EObject> objects,
			final EAttribute nameAttribute) {
		final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

		for (final EObject object : objects) {
			result.add(new Pair<String, EObject>(getName(null, null, object)
					 + "", object));
		}

		Collections.sort(result, comparator);

		return result;
	}

	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (!notification.isTouch()
				&& isRelevantTarget(notification.getNotifier(),
						notification.getFeature())) {
			cacheValues();
		}
	}
	
	@Override
	protected void missedNotifications(final List<Notification> missed) {
		for (final Notification notification : missed) {
			if (!notification.isTouch()
					&& isRelevantTarget(notification.getNotifier(),
							notification.getFeature())) {
				cacheValues();
				return;
			}
		}
	}

	/**
	 * A method which subclasses can override to decide whether the values need re-caching
	 * when a notification says this feature has changed.
	 * 
	 * @param target
	 * @param feature
	 * @return
	 */
	protected boolean isRelevantTarget(final Object target,
			final Object feature) {
		return feature.equals(nameAttribute);
	}

	/**
	 * Return a list of notifiers and features on those notifiers which should be watched
	 * for updating the given reference value's display name.
	 */
	@Override
	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
			EObject referer, EReference feature, EObject referenceValue) {
		if (referenceValue == null)
			return Collections.emptySet();
		return Collections.singleton(new Pair<Notifier, List<Object>>(
				referenceValue, Collections
						.singletonList((Object) nameAttribute)));
	}

	@Override
	public String getName(EObject referer, EReference feature,
			EObject referenceValue) {
		if (referenceValue != null) {
			return (String) referenceValue.eGet(nameAttribute);
		}
		return "";
	}

	/**
	 * If this reference value's name depends on another feature on the object, this method will be called with that feature
	 * at some point and upon returning true the displayed name will be recomputed.
	 */
	@Override
	public boolean updateOnChangeToFeature(Object changedFeature) {
		return false;
	}
}

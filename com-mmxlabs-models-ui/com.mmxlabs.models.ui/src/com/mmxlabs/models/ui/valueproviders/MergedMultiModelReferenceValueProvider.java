/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;

/**
 * A {@link IReferenceValueProvider} implementation that combines references from all {@link MMXSubModel}s in a {@link MMXRootObject} implementation. Given a target {@link EClass} super type, examine
 * all the top level containment multiple references in each {@link MMXSubModel} and combine into a single list.
 * 
 * 
 */
public class MergedMultiModelReferenceValueProvider extends BaseReferenceValueProvider {

	private final List<Pair<EObject, EStructuralFeature>> validReferences = new LinkedList<Pair<EObject, EStructuralFeature>>();

	private List<Pair<String, EObject>> cachedValues;

	/**
	 * List of references to EObjects in the value list which have a reference to the {@link #adapter} and need to be managed.
	 */
	private final Set<EObject> adapterReferences = new HashSet<EObject>();

	private final MMXAdapterImpl adapter = new MMXAdapterImpl() {
		@Override
		protected void missedNotifications(final List<Notification> missed) {
			// Clone list as the clearAdapterReferences can cause more notification to be added to the missed array. These will be missed as we take the original list, but we do not care about these
			// notifications as they are just adapter changed notifications.
			for (final Notification n : new ArrayList<Notification>(missed)) {
				reallyNotifyChanged(n);
			}
			super.missedNotifications(missed);
		}

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			final Object notifier = notification.getNotifier();

			for (final Pair<EObject, EStructuralFeature> p : validReferences) {
				if (p.getFirst() == notifier && p.getSecond() == notification.getFeature()) {
					cachedValues = null;
					clearAdapterReferences();

					return;
				}
			}
			// React to name changes
			if (notification.getFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
				cachedValues = null;
				clearAdapterReferences();
				return;
			}
		}

	};

	public MergedMultiModelReferenceValueProvider(final MMXRootObject rootObject, final EClass targetType) {
		super();

		for (final EReference ref : rootObject.eClass().getEAllContainments()) {
			if (ref.isMany() && targetType.isSuperTypeOf(ref.getEReferenceType())) {
				validReferences.add(new Pair<EObject, EStructuralFeature>(rootObject, ref));
				rootObject.eAdapters().add(adapter);
			}
		}
	}

	@Override
	protected boolean isRelevantTarget(final Object target, final Object feature) {
		for (final Pair<EObject, EStructuralFeature> p : validReferences) {
			if (p.getFirst() == target && p.getSecond() == feature) {
				return true;
			}
		}
		return feature == MMXCorePackage.eINSTANCE.getNamedObject_Name();
	}

	@Override
	public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
		if (cachedValues == null) {
			cacheValues();
		}
		return cachedValues;
	}

	@Override
	public void dispose() {

		for (final Pair<EObject, EStructuralFeature> p : validReferences) {
			p.getFirst().eAdapters().remove(adapter);
		}

		clearAdapterReferences();

		validReferences.clear();
		cachedValues = null;
	}

	@Override
	protected void cacheValues() {

		cachedValues = new ArrayList<Pair<String, EObject>>();

		for (final Pair<EObject, EStructuralFeature> p : validReferences) {
			final Object result = p.getFirst().eGet(p.getSecond());
			if (result instanceof Collection<?>) {
				final Collection<?> collection = (Collection<?>) result;
				for (final Object obj : collection) {
					if (obj instanceof NamedObject) {
						final NamedObject namedObject = (NamedObject) obj;
						cachedValues.add(new Pair<String, EObject>(namedObject.getName(), namedObject));
						// Add adapter to each contained value to cause list to be regenerated on name change
						namedObject.eAdapters().add(adapter);
						adapterReferences.add(namedObject);
					} else if (obj instanceof EObject) {
						cachedValues.add(new Pair<String, EObject>(obj.toString(), (EObject) obj));
					}
				}
			}
		}

		Collections.sort(cachedValues, new Comparator<Pair<String, EObject>>() {

			@Override
			public int compare(final Pair<String, EObject> o1, final Pair<String, EObject> o2) {
				final String first1 = o1.getFirst();
				final String first2 = o2.getFirst();

				if (first1 == null) {
					return 1;
				}
				if (first2 == null) {
					return -1;
				}
				return first1.compareTo(first2);
			}
		});
	}

	private void clearAdapterReferences() {
		for (final EObject eObj : adapterReferences) {
			eObj.eAdapters().remove(adapter);
		}
		adapterReferences.clear();
	}
}

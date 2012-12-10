/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;

/**
 * A {@link IReferenceValueProvider} implementation that combines references from all {@link MMXSubModel}s in a {@link MMXRootObject} implementation. Given a target {@link EClass} super type, examine
 * all the top level containment multiple references in each {@link MMXSubModel} and combine into a single list.
 * 
 * @since 2.0
 * 
 */
public class MergedMultiModelReferenceValueProvider extends BaseReferenceValueProvider {

	private final List<Pair<EObject, EReference>> validReferences = new LinkedList<Pair<EObject, EReference>>();

	private List<Pair<String, EObject>> cachedValues;

	private final MMXAdapterImpl adapter = new MMXAdapterImpl() {
		@Override
		protected void missedNotifications(final List<Notification> missed) {
			for (final Notification n : missed) {
				reallyNotifyChanged(n);
			}
			super.missedNotifications(missed);
		}

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			final Object notifier = notification.getNotifier();

			for (final Pair<EObject, EReference> p : validReferences) {
				if (p.getFirst() == notifier && p.getSecond() == notification.getFeature()) {
					cachedValues = null;
					return;
				}
			}
		}
	};

	public MergedMultiModelReferenceValueProvider(final MMXRootObject rootObject, final EClass targetType) {
		super();

		for (final MMXSubModel subModel : rootObject.getSubModels()) {
			final UUIDObject subModelInstance = subModel.getSubModelInstance();
			if (subModelInstance != null) {
				for (final EReference ref : subModelInstance.eClass().getEAllContainments()) {
					if (ref.isMany() && targetType.isSuperTypeOf(ref.getEReferenceType())) {
						validReferences.add(new Pair<EObject, EReference>(subModelInstance, ref));
						subModelInstance.eAdapters().add(adapter);
					}
				}
			}
		}
	}

	@Override
	protected boolean isRelevantTarget(final Object target, final Object feature) {
		for (final Pair<EObject, EReference> p : validReferences) {
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

		for (final Pair<EObject, EReference> p : validReferences) {
			p.getFirst().eAdapters().remove(adapter);
		}

		validReferences.clear();
		cachedValues = null;
	}

	@Override
	protected void cacheValues() {

		cachedValues = new ArrayList<Pair<String, EObject>>();

		for (final Pair<EObject, EReference> p : validReferences) {
			final Object result = p.getFirst().eGet(p.getSecond());
			if (result instanceof Collection<?>) {
				final Collection<?> collection = (Collection<?>) result;
				for (final Object obj : collection) {
					if (obj instanceof NamedObject) {
						final NamedObject namedObject = (NamedObject) obj;
						cachedValues.add(new Pair<String, EObject>(namedObject.getName(), namedObject));
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

}

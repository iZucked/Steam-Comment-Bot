/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;

/**
 * A reference value provider which is like simple reference value provider, but merges the values of several containment references.
 * 
 * @author hinton
 * 
 */
public class MergedReferenceValueProvider extends SimpleReferenceValueProvider {
	private final ArrayList<EReference> extraReferences;
	private final EObject container;

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
			if (notifier == container) {
				for (final EReference p : extraReferences) {
					if (p == notification.getFeature()) {
						cachedValues = null;
						return;
					}
				}
			}
		}
	};

	public MergedReferenceValueProvider(final EObject container, final EReference... containingReferences) {
		super(container, containingReferences[0]);
		this.container = container;
		this.container.eAdapters().add(adapter);
		extraReferences = new ArrayList<EReference>(containingReferences.length - 1);
		for (int i = 1; i < containingReferences.length; i++) {
			extraReferences.add(containingReferences[i]);
		}
	}

	@Override
	protected boolean isRelevantTarget(final Object target, final Object feature) {
		if (super.isRelevantTarget(target, feature))
			return true;
		if (target == container && extraReferences.contains(feature))
			return true;
		return feature == MMXCorePackage.eINSTANCE.getNamedObject_Name();
	}

	@Override
	protected EList<? extends EObject> getObjects() {
		final EList<EObject> values = new BasicEList<EObject>(super.getObjects());
		for (final EReference reference : extraReferences) {
			values.addAll((EList) container.eGet(reference));
		}
		return values;
	}

	@Override
	public void dispose() {
		container.eAdapters().remove(adapter);

		super.dispose();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class VesselCharterVesselMarkerValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory delegate;

	public VesselCharterVesselMarkerValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), FleetPackage.eINSTANCE.getVessel());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (delegate == null) {
			return null;
		}

		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == CargoPackage.eINSTANCE.getVesselCharter_Vessel()) {
			return new IReferenceValueProvider() {

				@Override
				public boolean updateOnChangeToFeature(Object changedFeature) {
					return delegateFactory.updateOnChangeToFeature(changedFeature);
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer, EReference feature, EObject referenceValue) {
					return delegateFactory.getNotifiers(referer, feature, referenceValue);
				}

				@Override
				public String getName(EObject referer, EReference feature, EObject referenceValue) {
					return delegateFactory.getName(referer, feature, referenceValue);
				}

				@Override
				public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);
					if (target instanceof VesselCharter) {
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<>();
						for (final Pair<String, EObject> value : delegateValue) {
							final EObject valueEObject = value.getSecond();
							if (valueEObject instanceof Vessel vessel) {
								if (!vessel.isMarker()) {
									filteredList.add(value);
								}
							}
						}
						
						return filteredList;
					}
					return delegateValue;
				}

				@Override
				public void dispose() {
					delegateFactory.dispose();
				}
			};
		}
		return delegateFactory;
	}
}

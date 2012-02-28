/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * Special case for restricting load slots to load contracts and vice versa for discharge.
 * 
 * TODO hook the filtering in in a more intelligent way; it would be good if you could get the delegate
 * to notify on update to cached values, so as to recache filtered values.
 * 
 * @author hinton
 *
 */
public class SlotContractValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public SlotContractValueProviderFactory() {
		this.delegate = Activator
				.getDefault()
				.getReferenceValueProviderFactoryRegistry()
				.getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(),
						TypesPackage.eINSTANCE.getAContract());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(
			final EClass owner, final EReference reference,
			final MMXRootObject rootObject) {
		if (delegate == null) return null;
		final IReferenceValueProvider delegateFactory = delegate
				.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == CargoPackage.eINSTANCE.getSlot_Contract()) {
			return new IReferenceValueProvider() {
				@Override
				public boolean updateOnChangeToFeature(Object changedFeature) {
					return delegateFactory
							.updateOnChangeToFeature(changedFeature);
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
						EObject referer, EReference feature,
						EObject referenceValue) {
					return delegateFactory.getNotifiers(referer, feature,
							referenceValue);
				}

				@Override
				public String getName(EObject referer, EReference feature,
						EObject referenceValue) {
					return delegateFactory.getName(referer, feature,
							referenceValue);
				}

				@Override
				public List<Pair<String, EObject>> getAllowedValues(
						final EObject target, final EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory
							.getAllowedValues(target, field);

					if (target instanceof LoadSlot) {
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> value : delegateValue) {
							if (value.getSecond() instanceof PurchaseContract) {
								filteredList.add(value);
							}
						}
						return filteredList;
					} else if (target instanceof DischargeSlot) {
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> value : delegateValue) {
							if (value.getSecond() instanceof SalesContract) {
								filteredList.add(value);
							}
						}
						return filteredList;
					}
					
					return delegateValue;
				}
			};
		} else {
			return delegateFactory;
		}
	}
}

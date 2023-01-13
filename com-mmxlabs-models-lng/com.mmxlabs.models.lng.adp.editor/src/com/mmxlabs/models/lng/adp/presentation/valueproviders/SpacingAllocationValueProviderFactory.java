/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SpacingProfile;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class SpacingAllocationValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory delegate;
	
	public SpacingAllocationValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), CommercialPackage.eINSTANCE.getSalesContract());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (delegate == null) {
			return null;
		}

		if (reference == ADPPackage.eINSTANCE.getSpacingAllocation_Contract()) {
			final Predicate<SalesContract> isAllowed;
			if (owner == ADPPackage.Literals.FOB_SPACING_ALLOCATION) {
				isAllowed = s -> s.getContractType() == ContractType.FOB;
			} else if (owner == ADPPackage.Literals.DES_SPACING_ALLOCATION) {
				isAllowed = s -> s.getContractType() == ContractType.DES;
			} else {
				throw new IllegalStateException("Unknown spacing type.");
			}
			final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
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
					if (target instanceof SpacingProfile) {
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<>();
						for (final Pair<String, EObject> p : delegateValue) {
							if (isAllowed.test((SalesContract) p.getSecond())) {
								filteredList.add(p);
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
		return null;
	}

}

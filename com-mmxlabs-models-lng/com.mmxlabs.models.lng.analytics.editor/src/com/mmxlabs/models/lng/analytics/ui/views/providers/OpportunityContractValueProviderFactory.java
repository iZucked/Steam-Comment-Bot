/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class OpportunityContractValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public OpportunityContractValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), CommercialPackage.eINSTANCE.getContract());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (delegate == null)
			return null;
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);

		// if (delegateFactory == null) return null;
		if (reference == AnalyticsPackage.eINSTANCE.getBuyOpportunity_Contract() || reference == AnalyticsPackage.eINSTANCE.getSellOpportunity_Contract()) {
			return new IReferenceValueProvider() {
				@Override
				public void dispose() {
					delegateFactory.dispose();
				}

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
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);

					if (target instanceof BuyOpportunity) {

						BuyOpportunity buy = (BuyOpportunity) target;
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> value : delegateValue) {
							EObject second = value.getSecond();
							if (((EReference) (second.eContainingFeature())).getEReferenceType().isSuperTypeOf(CommercialPackage.eINSTANCE.getPurchaseContract())) {
								ContractType contractType = ((Contract) second).getContractType();
								if (buy.isDesPurchase()) {
									if (contractType == ContractType.DES || contractType == ContractType.BOTH) {
										filteredList.add(value);
									}
								} else {
									if (contractType == ContractType.FOB || contractType == ContractType.BOTH) {
										filteredList.add(value);
									}
								}
							}
						}
						return addNullEntry(filteredList);
					} else if (target instanceof SellOpportunity) {
						SellOpportunity sell = (SellOpportunity) target;
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> value : delegateValue) {
							EObject second = value.getSecond();
							if (((EReference) (second.eContainingFeature())).getEReferenceType().isSuperTypeOf(CommercialPackage.eINSTANCE.getSalesContract())) {
								ContractType contractType = ((Contract) second).getContractType();
								if (sell.isFobSale()) {
									if (contractType == ContractType.FOB || contractType == ContractType.BOTH) {
										filteredList.add(value);
									}
								} else {
									if (contractType == ContractType.DES || contractType == ContractType.BOTH) {
										filteredList.add(value);
									}
								}
							}
						}
						return addNullEntry(filteredList);
					}

					return addNullEntry(delegateValue);
				}

				/**
				 * Adds the null entry to the list
				 * 
				 * @param input
				 * @return
				 */
				private List<Pair<String, EObject>> addNullEntry(List<Pair<String, EObject>> input) {
					final ArrayList<Pair<String, EObject>> output = new ArrayList<Pair<String, EObject>>();
					output.add(new Pair<String, EObject>("<None>", null));
					output.addAll(input);
					return output;
				}
			};
		} else {
			return delegateFactory;
		}
	}
}

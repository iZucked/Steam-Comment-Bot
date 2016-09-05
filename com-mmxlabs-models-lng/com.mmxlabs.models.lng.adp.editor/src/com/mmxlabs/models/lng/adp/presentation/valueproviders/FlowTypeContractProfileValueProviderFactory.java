/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class FlowTypeContractProfileValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel) {

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

			for (EObject ext : lngScenarioModel.getExtensions()) {
				if (ext instanceof ADPModel) {

					ADPModel adpModel = (ADPModel) ext;
					final EClass referenceClass = reference.getEReferenceType();

					if (referenceClass == ADPPackage.Literals.PURCHASE_CONTRACT_PROFILE) {
						return new SimpleReferenceValueProvider(adpModel, ADPPackage.Literals.ADP_MODEL__PURCHASE_CONTRACT_PROFILES) {
							@Override
							public String getName(EObject referer, EReference feature, EObject referenceValue) {
								if (referenceValue instanceof ContractProfile<?>) {
									ContractProfile<?> contractProfile = (ContractProfile<?>) referenceValue;
									return contractProfile.getContract().getName();
								}
								return "";
							}
						};
					} else if (referenceClass == ADPPackage.Literals.SALES_CONTRACT_PROFILE) {
						return new SimpleReferenceValueProvider(adpModel, ADPPackage.Literals.ADP_MODEL__SALES_CONTRACT_PROFILES) {
							@Override
							public String getName(EObject referer, EReference feature, EObject referenceValue) {
								if (referenceValue instanceof ContractProfile<?>) {
									ContractProfile<?> contractProfile = (ContractProfile<?>) referenceValue;
									return contractProfile.getContract().getName();
								}
								return "";
							}
						};
					} else if (referenceClass == ADPPackage.Literals.CONTRACT_PROFILE) {
						return new MergedReferenceValueProvider(adpModel, ADPPackage.Literals.ADP_MODEL__PURCHASE_CONTRACT_PROFILES, ADPPackage.Literals.ADP_MODEL__SALES_CONTRACT_PROFILES) {
							@Override
							public String getName(EObject referer, EReference feature, EObject referenceValue) {
								if (referenceValue instanceof ContractProfile<?>) {
									ContractProfile<?> contractProfile = (ContractProfile<?>) referenceValue;
									return contractProfile.getContract().getName();
								}
								return "";
							}
						};
					}
				}
			}
		}

		return null;
	}
}

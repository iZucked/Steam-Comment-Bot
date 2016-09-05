/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DeliverToProfileFlow;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SupplyFromProfileFlow;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class SubContractProfileValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		return new BaseReferenceValueProvider() {
			@Override
			public boolean updateOnChangeToFeature(Object changedFeature) {
				if (changedFeature == ADPPackage.Literals.BINDING_RULE__PROFILE) {
					return true;
				}
				if (changedFeature == ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__PROFILE) {
					return true;
				}
				if (changedFeature == ADPPackage.Literals.SUPPLY_FROM_PROFILE_FLOW__PROFILE) {
					return true;
				}
				// TODO Auto-generated method stub
				return super.updateOnChangeToFeature(changedFeature);
			}

			@Override
			public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {

				if (target instanceof BindingRule) {
					BindingRule bindingRule = (BindingRule) target;
					target = bindingRule.getProfile();
				} else if (target instanceof DeliverToProfileFlow) {
					DeliverToProfileFlow deliverToProfileFlow = (DeliverToProfileFlow) target;
					target = deliverToProfileFlow.getProfile();
				} else if (target instanceof SupplyFromProfileFlow) {
					SupplyFromProfileFlow supplyFromProfileFlow = (SupplyFromProfileFlow) target;
					target = supplyFromProfileFlow.getProfile();
				}

				if (target instanceof ContractProfile<?>) {
					ContractProfile<?> contractProfile = (ContractProfile<?>) target;

					List<Pair<String, EObject>> values = new LinkedList<>();
					for (SubContractProfile<?> subProfile : contractProfile.getSubProfiles()) {
						values.add(new Pair<>(subProfile.getName(), subProfile));
					}
					return values;
				}

				return new LinkedList<>();
			}

			@Override
			public void dispose() {

			}

			@Override
			protected void cacheValues() {

			}
		};
	}
}

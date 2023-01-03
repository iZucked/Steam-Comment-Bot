/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DeliverToFlow;
import com.mmxlabs.models.lng.adp.SupplyFromFlow;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class ContractProfileValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel) {

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

			{
				{

					// ADPModel adpModel = (ADPModel) ext;
					final EClass referenceClass = reference.getEReferenceType();

					if (referenceClass == ADPPackage.Literals.PURCHASE_CONTRACT_PROFILE) {
						return new BaseReferenceValueProvider() {

							@Override
							public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
								ADPModel adpModel = getADPModel(target);
								// List<Pair<String, EObject>> allowedValues = super.getAllowedValues(target, field);
								if (target instanceof DeliverToFlow) {

									return adpModel.getSalesContractProfiles().stream() //
											.map(p -> new Pair<>(((ContractProfile) p).getContract().getName(), (EObject) p)) //
											.collect(Collectors.toList());//
								} else if (target instanceof SupplyFromFlow) {

									return adpModel.getPurchaseContractProfiles().stream()//
											.map(p -> new Pair<>(((ContractProfile) p).getContract().getName(), (EObject) p)) //
											.collect(Collectors.toList());//
								}

								return Collections.emptyList();
							}

							@Override
							public String getName(EObject referer, EReference feature, EObject referenceValue) {
								if (referenceValue instanceof ContractProfile<?, ?>) {
									ContractProfile<?, ?> contractProfile = (ContractProfile<?, ?>) referenceValue;
									return contractProfile.getContract().getName();
								}
								return "";
							}

							@Override
							public void dispose() {
								// TODO Auto-generated method stub

							}

							@Override
							protected void cacheValues() {
								// TODO Auto-generated method stub

							}
						};
					} else if (referenceClass == ADPPackage.Literals.SALES_CONTRACT_PROFILE) {
						return new BaseReferenceValueProvider() {

							@Override
							public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
								ADPModel adpModel = getADPModel(target);
								// List<Pair<String, EObject>> allowedValues = super.getAllowedValues(target, field);
								if (target instanceof DeliverToFlow) {

									return adpModel.getSalesContractProfiles().stream() //
											.map(p -> new Pair<>(((ContractProfile) p).getContract().getName(), (EObject) p)) //
											.collect(Collectors.toList());//
								} else if (target instanceof SupplyFromFlow) {

									return adpModel.getPurchaseContractProfiles().stream()//
											.map(p -> new Pair<>(((ContractProfile) p).getContract().getName(), (EObject) p)) //
											.collect(Collectors.toList());//
								}

								return Collections.emptyList();
							}

							@Override
							public String getName(EObject referer, EReference feature, EObject referenceValue) {
								if (referenceValue instanceof ContractProfile<?, ?>) {
									ContractProfile<?, ?> contractProfile = (ContractProfile<?, ?>) referenceValue;
									return contractProfile.getContract().getName();
								}
								return "";
							}

							@Override
							public void dispose() {
								// TODO Auto-generated method stub

							}

							@Override
							protected void cacheValues() {
								// TODO Auto-generated method stub

							}
						};
					} else if (referenceClass == ADPPackage.Literals.CONTRACT_PROFILE) {
						return new BaseReferenceValueProvider() {

							@Override
							public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
								ADPModel adpModel = getADPModel(target);
								// List<Pair<String, EObject>> allowedValues = super.getAllowedValues(target, field);
								if (target instanceof DeliverToFlow) {

									return adpModel.getSalesContractProfiles().stream() //
											.map(p -> new Pair<>(((ContractProfile) p).getContract().getName(), (EObject) p)) //
											.collect(Collectors.toList());//
								} else if (target instanceof SupplyFromFlow) {

									return adpModel.getPurchaseContractProfiles().stream()//
											.map(p -> new Pair<>(((ContractProfile) p).getContract().getName(), (EObject) p)) //
											.collect(Collectors.toList());//
								}

								return Collections.emptyList();
							}

							@Override
							public String getName(EObject referer, EReference feature, EObject referenceValue) {
								if (referenceValue instanceof ContractProfile<?, ?>) {
									ContractProfile<?, ?> contractProfile = (ContractProfile<?, ?>) referenceValue;
									return contractProfile.getContract().getName();
								}
								return "";
							}

							@Override
							public void dispose() {
								// TODO Auto-generated method stub

							}

							@Override
							protected void cacheValues() {
								// TODO Auto-generated method stub

							}
						};
						// return new MergedReferenceValueProvider(adpModel, ADPPackage.Literals.ADP_MODEL__PURCHASE_CONTRACT_PROFILES, ADPPackage.Literals.ADP_MODEL__SALES_CONTRACT_PROFILES) {
						//
						// @Override
						// public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {
						//
						// List<Pair<String, EObject>> allowedValues = super.getAllowedValues(target, field);
						// if (target instanceof DeliverToFlow) {
						//
						// return allowedValues.stream() //
						// .filter(p -> p.getSecond() instanceof SalesContractProfile) //
						// .collect(Collectors.toList());
						// } else if (target instanceof SupplyFromFlow) {
						//
						// return allowedValues.stream() //
						// .filter(p -> p.getSecond() instanceof PurchaseContractProfile) //
						// .collect(Collectors.toList());
						// }
						//
						// return allowedValues;
						// }
						//
						// @Override
						// public String getName(EObject referer, EReference feature, EObject referenceValue) {
						// if (referenceValue instanceof ContractProfile<?>) {
						// ContractProfile<?> contractProfile = (ContractProfile<?>) referenceValue;
						// return contractProfile.getContract().getName();
						// }
						// return "";
						// }
						// };
					}
				}
			}
		}

		return null;
	}

	private ADPModel getADPModel(EObject object) {
		while (!(object instanceof ADPModel) && object != null) {
			object = object.eContainer();
		}
		return (ADPModel) object;
	}
}

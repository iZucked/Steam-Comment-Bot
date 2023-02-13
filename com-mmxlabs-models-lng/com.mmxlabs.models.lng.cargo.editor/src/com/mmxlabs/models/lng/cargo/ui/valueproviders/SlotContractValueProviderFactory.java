/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * Special case for restricting load slots to load contracts and vice versa for discharge.
 * 
 * TODO hook the filtering in in a more intelligent way; it would be good if you could get the delegate to notify on update to cached values, so as to recache filtered values.
 * 
 * @author hinton
 * 
 */
public class SlotContractValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public SlotContractValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), CommercialPackage.eINSTANCE.getContract());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (delegate == null)
			return null;
		

		// if (delegateFactory == null) return null;
		if (reference == CargoPackage.eINSTANCE.getSlot_Contract()) {
			final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
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
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final ETypedElement field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);

					if (target instanceof LoadSlot) {

						LoadSlot loadSlot = (LoadSlot) target;
						Contract c = loadSlot.getContract();
						boolean foundCurrent = false;
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> value : delegateValue) {
							EObject second = value.getSecond();

							if (((EReference) (second.eContainingFeature())).getEReferenceType().isSuperTypeOf(CommercialPackage.eINSTANCE.getPurchaseContract())) {
								ContractType contractType = ((Contract) second).getContractType();
								if (loadSlot.isDESPurchase()) {
									if (contractType == ContractType.DES || contractType == ContractType.BOTH) {
										filteredList.add(value);
										if (second == c) {
											foundCurrent = true;
										}
									}
								} else {
									if (contractType == ContractType.FOB || contractType == ContractType.BOTH) {
										filteredList.add(value);
										if (second == c) {
											foundCurrent = true;
										}
									}
								}
							}
						}
						if (c != null && !foundCurrent) {
							filteredList.add(0, new Pair<>(c.getName(), c));
						}
						return addNullEntry(filteredList);
					} else if (target instanceof DischargeSlot) {
						DischargeSlot dischargeSlot = (DischargeSlot) target;
						Contract c = dischargeSlot.getContract();
						boolean foundCurrent = false;
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> value : delegateValue) {
							EObject second = value.getSecond();
							if (((EReference) (second.eContainingFeature())).getEReferenceType().isSuperTypeOf(CommercialPackage.eINSTANCE.getSalesContract())) {
								ContractType contractType = ((Contract) second).getContractType();
								if (dischargeSlot.isFOBSale()) {
									if (contractType == ContractType.FOB || contractType == ContractType.BOTH) {
										filteredList.add(value);
										if (second == c) {
											foundCurrent = true;
										}
									}
								} else {
									if (contractType == ContractType.DES || contractType == ContractType.BOTH) {
										filteredList.add(value);
										if (second == c) {
											foundCurrent = true;
										}
									}
								}
							}
						}
						if (c != null && !foundCurrent) {
							filteredList.add(0, new Pair<>(c.getName(), c));
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
		} else if (reference == CargoPackage.eINSTANCE.getSlot_RestrictedContracts()){
			final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
			return new IReferenceValueProvider() {

				@Override
				public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);

					if (target instanceof Slot) {
						final Slot slot = (Slot) target;

						if (target instanceof LoadSlot) {
							return delegateValue.stream()
									.filter(l -> l.getSecond() instanceof SalesContract)
									.collect(Collectors.toList());
						} else if (target instanceof DischargeSlot) {
							return delegateValue.stream()
									.filter(l -> l.getSecond() instanceof PurchaseContract)
									.collect(Collectors.toList());
						}
					}
					return delegateValue;
				}

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
			};
		} else {
			final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
			return delegateFactory;
		}
	}
}

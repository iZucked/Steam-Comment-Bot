/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class SubContractProfilePortValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory delegate;
	
	public SubContractProfilePortValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), PortPackage.eINSTANCE.getPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (delegate == null) {
			return null;
		}
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == ADPPackage.eINSTANCE.getSubContractProfile_Port()) {
			return new IReferenceValueProvider() {
				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {
					return delegateFactory.updateOnChangeToFeature(changedFeature);
				}
				
				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
					return delegateFactory.getNotifiers(referer, feature, referenceValue);
				}

				@Override
				public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);
					
					if (target instanceof SubContractProfile) {
						final SubContractProfile subContractProfile = (SubContractProfile) target;
						final EObject targetContainer = target.eContainer();
						final PortCapability expectedCapability;
						if (targetContainer instanceof SalesContractProfile) {
							expectedCapability = PortCapability.DISCHARGE;
						} else if (targetContainer instanceof PurchaseContractProfile) {
							expectedCapability = PortCapability.LOAD;
						} else {
							throw new IllegalStateException();
						}
						final Contract contract = ((ContractProfile) targetContainer).getContract();
						final ArrayList<Pair<String, EObject>> filterPassOne = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> p : delegateValue) {
							if (((Port) p.getSecond()).getCapabilities().contains(expectedCapability)) {
								filterPassOne.add(p);
							}
						}
						if (contract == null) {
							return filterPassOne;
						}
						final ArrayList<Pair<String, EObject>> filterPassTwo = new ArrayList<Pair<String, EObject>>();
						Set<Port> contractAllowedPorts = SetUtils.getObjects(contract.getAllowedPorts());
						if (contractAllowedPorts != null && !contractAllowedPorts.isEmpty()) {
							for (final Pair<String, EObject> value : filterPassOne) {
								if (contractAllowedPorts.contains(value.getSecond())) {
									filterPassTwo.add(value);
								}
							}
						} else {
							return filterPassOne;
						}
						return filterPassTwo;
					}
					return delegateValue;
				}

				@Override
				public String getName(EObject referer, EReference feature, EObject referenceValue) {
					return delegateFactory.getName(referer, feature, referenceValue);
				}

				@Override
				public void dispose() {
					delegateFactory.dispose();
				}
			};
		} else {
			return delegateFactory;
		}
	}

}

package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class FobSpacingAllocationValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory delegate;
	
	public FobSpacingAllocationValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), CommercialPackage.eINSTANCE.getSalesContract());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (delegate == null) {
			return null;
		}
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == ADPPackage.eINSTANCE.getSpacingAllocation_Contract()) {
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
				public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);
					if (target instanceof FobSpacingAllocation) {
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<>();
						for (final Pair<String, EObject> p : delegateValue) {
							if (((SalesContract) p.getSecond()).getContractType() == ContractType.FOB) {
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
		
		
				if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			
		}
		return null;
	}

}

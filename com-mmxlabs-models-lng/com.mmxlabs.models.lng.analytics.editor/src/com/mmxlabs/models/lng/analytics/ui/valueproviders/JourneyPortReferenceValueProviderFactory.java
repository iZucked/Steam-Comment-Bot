/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.FilteredReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * This is a restricted case reference value provider which filters the port list on slots to those allowed by the slots' contract.
 * 
 * @author hinton
 * 
 */
public class JourneyPortReferenceValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public JourneyPortReferenceValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), PortPackage.eINSTANCE.getPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (delegate == null)
			return null;
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
		
		if (reference == AnalyticsPackage.eINSTANCE.getJourney_From()) {
			return new FilteredReferenceValueProvider(delegateFactory) {
				@Override
				protected boolean isAllowedValue(EObject target, EStructuralFeature field,
						Pair<String, EObject> value) {
					if (value.getSecond() instanceof Port) {
						return ((Port) value.getSecond()).getCapabilities().contains(PortCapability.LOAD);
					}
					return false;
				}
			};
		} else if (reference == AnalyticsPackage.eINSTANCE.getJourney_To()) {
			return new FilteredReferenceValueProvider(delegateFactory) {
				@Override
				protected boolean isAllowedValue(EObject target, EStructuralFeature field,
						Pair<String, EObject> value) {
					if (value.getSecond() instanceof Port) {
						return ((Port) value.getSecond()).getCapabilities().contains(PortCapability.DISCHARGE);
					}
					return false;
				}
			};
		} else {
			return delegateFactory;
		}
	}
}

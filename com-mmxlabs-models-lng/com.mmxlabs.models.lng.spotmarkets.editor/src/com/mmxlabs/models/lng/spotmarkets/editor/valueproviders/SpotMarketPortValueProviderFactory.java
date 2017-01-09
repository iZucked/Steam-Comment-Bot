/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.valueproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * This is a restricted case reference value provider which filters the port list depending on the SpotMarket type.
 * 
 * @author Simon Goodall
 * 
 */
public class SpotMarketPortValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public SpotMarketPortValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), PortPackage.eINSTANCE.getPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (delegate == null) {
			return null;
		}
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
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
			public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
				return delegateFactory.getName(referer, feature, referenceValue);
			}

			@Override
			public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
				final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);
				PortCapability capability = null;

				if (target instanceof DESPurchaseMarket) {
					capability = PortCapability.DISCHARGE;
				} else if (target instanceof DESSalesMarket) {
					capability = PortCapability.DISCHARGE;
				} else if (target instanceof FOBPurchasesMarket) {
					capability = PortCapability.LOAD;
				} else if (target instanceof FOBSalesMarket) {
					capability = PortCapability.LOAD;
				}

				final ArrayList<Pair<String, EObject>> filterOne = new ArrayList<Pair<String, EObject>>();
				for (final Pair<String, EObject> p : delegateValue) {
					if (((Port) p.getSecond()).getCapabilities().contains(capability)) {
						filterOne.add(p);
					}
				}

				return filterOne;
			}

			@Override
			public void dispose() {
				delegateFactory.dispose();
			}
		};
	}
}

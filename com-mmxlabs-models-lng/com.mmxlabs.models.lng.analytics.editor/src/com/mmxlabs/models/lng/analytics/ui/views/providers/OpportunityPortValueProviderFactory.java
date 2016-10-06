/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

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
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * This is a restricted case reference value provider which filters the port list on slots to those allowed by the slots' contract.
 * 
 * @author hinton
 * 
 */
public class OpportunityPortValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public OpportunityPortValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), PortPackage.eINSTANCE.getPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (delegate == null)
			return null;
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT || reference == AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT) {
			return new IReferenceValueProvider() {
				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {
					if (changedFeature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__CONTRACT) {
						return true;
					} else if (changedFeature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__CONTRACT) {
						return true;
					} else if (changedFeature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__DES_PURCHASE) {
						return true;
					} else if (changedFeature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__FOB_SALE) {
						return true;
					}

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

					Contract contract = null;
					Port port = null;
					PortCapability capability = null;
					if (target instanceof BuyOpportunity) {

						final BuyOpportunity buyOpportunity = (BuyOpportunity) target;
						contract = buyOpportunity.getContract();
						port = buyOpportunity.getPort();
						if (buyOpportunity.isDesPurchase()) {
							capability = PortCapability.DISCHARGE;
						} else {
							capability = PortCapability.LOAD;
						}
					} else if (target instanceof SellOpportunity) {

						final SellOpportunity sellOpportunity = (SellOpportunity) target;
						contract = sellOpportunity.getContract();
						port = sellOpportunity.getPort();
						if (sellOpportunity.isFobSale()) {
							capability = PortCapability.LOAD;
						} else {
							capability = PortCapability.DISCHARGE;
						}
					}

					final ArrayList<Pair<String, EObject>> filterOne = new ArrayList<Pair<String, EObject>>();
					for (final Pair<String, EObject> p : delegateValue) {
						if (capability == null || ((Port) p.getSecond()).getCapabilities().contains(capability)) {
							filterOne.add(p);
						}
					}

					{
						// Make sure current selection is in the list
						if (port != null) {
							final Pair<String, EObject> pair = new Pair<String, EObject>(port.getName(), port);
							if (!filterOne.contains(pair)) {
								filterOne.add(pair);
							}
						}
					}

					final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
					if (contract == null) {
						return filterOne;
					}

					final Set<Port> ports = SetUtils.getObjects(contract.getAllowedPorts());

					if (ports != null && !ports.isEmpty()) {
						for (final Pair<String, EObject> value : filterOne) {
							if (ports.contains(value.getSecond())) {
								filteredList.add(value);
							}
						}
					} else {
						return filterOne;
					}

					{
						// Make sure current selection is in the list (check again in case filtered by contract)
						if (port != null) {
							final Pair<String, EObject> pair = new Pair<String, EObject>(port.getName(), port);
							if (!filteredList.contains(pair)) {
								filteredList.add(pair);
							}
						}
					}

					return filteredList;

					// return delegateValue;
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

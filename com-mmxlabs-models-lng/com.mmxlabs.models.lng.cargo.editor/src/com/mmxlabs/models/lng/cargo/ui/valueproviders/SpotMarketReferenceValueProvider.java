/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;

/**
 * A default reference value provider implementation, which tracks references contained in an EObject.
 * 
 * It is an adapter on the containing EObject.
 * 
 * @author hinton
 * 
 */
public class SpotMarketReferenceValueProvider extends BaseReferenceValueProvider {
	protected final MMXRootObject rootObject;

	public SpotMarketReferenceValueProvider(final MMXRootObject rootObject) {
		this.rootObject = rootObject;
		// container.eAdapters().add(this);
	}

	@Override
	public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {

		final EStructuralFeature feature;
		if (target instanceof SpotLoadSlot) {

			final SpotLoadSlot slot = (SpotLoadSlot) target;

			if (slot.isDESPurchase()) {
				feature = PricingPackage.eINSTANCE.getPricingModel_DesPurchaseSpotMarket();
			} else {
				feature = PricingPackage.eINSTANCE.getPricingModel_FobPurchasesSpotMarket();
			}
		} else if (target instanceof SpotDischargeSlot) {

			final SpotDischargeSlot slot = (SpotDischargeSlot) target;

			if (slot.isFOBSale()) {
				feature = PricingPackage.eINSTANCE.getPricingModel_FobSalesSpotMarket();
			} else {
				feature = PricingPackage.eINSTANCE.getPricingModel_DesSalesSpotMarket();
			}
		} else {
			feature = null;
		}

		if (feature != null) {

			final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
			final SpotMarketGroup group = (SpotMarketGroup) pricingModel.eGet(feature);
			if (group != null) {
				final List<Pair<String, EObject>> values = Lists.transform(group.getMarkets(), new Function<SpotMarket, Pair<String, EObject>>() {

					@Override
					public Pair<String, EObject> apply(final SpotMarket market) {
						return new Pair<String, EObject>(market.getName(), market);
					}

				});
				// values.add(0, null);
				return values;
			}
		}
		return Collections.emptyList();
	}

	@Override
	protected boolean isRelevantTarget(Object target, Object feature) {
		return super.isRelevantTarget(target, feature);
	}

	@Override
	public void dispose() {
		// container.eAdapters().remove(this);
	}

	@Override
	protected void cacheValues() {

	}
}

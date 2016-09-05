/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.DeliverToSpotFlow;
import com.mmxlabs.models.lng.adp.SupplyFromSpotFlow;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;

public class SpotMarketReferenceValueProvider extends BaseReferenceValueProvider {
	protected final MMXRootObject rootObject;

	public SpotMarketReferenceValueProvider(final MMXRootObject rootObject) {
		this.rootObject = rootObject;
		// container.eAdapters().add(this);
	}

	@Override
	public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {

		if (rootObject instanceof LNGScenarioModel) {
			LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final List<EStructuralFeature> features = new LinkedList<>();
			;
			if (target instanceof SupplyFromSpotFlow) {

//				final SupplyFromSpotFlow slot = (SupplyFromSpotFlow) target;

				// if (slot.isDESPurchase()) {
				features.add(SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DesPurchaseSpotMarket());
				// } else {
				features.add(SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobPurchasesSpotMarket());
				// }
			} else if (target instanceof DeliverToSpotFlow) {

				final DeliverToSpotFlow slot = (DeliverToSpotFlow) target;

				// if (slot.isFOBSale()) {
				features.add(SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobSalesSpotMarket());
				// } else {
				features.add(SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DesSalesSpotMarket());
				// }
			} else {
				// feature = null;
			}

			if (!features.isEmpty()) {

				final SpotMarketsModel spotMarketsModel = lngScenarioModel.getReferenceModel().getSpotMarketsModel();
				final List<Pair<String, EObject>> values = new LinkedList<>();
				for (EStructuralFeature feature : features) {
					final SpotMarketGroup group = (SpotMarketGroup) spotMarketsModel.eGet(feature);
					if (group != null) {
						for (SpotMarket market : group.getMarkets()) {
							values.add(new Pair<String, EObject>(market.getName(), market));

						}
					}
				}
				values.add(0, new Pair<>("Not specified", null));
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

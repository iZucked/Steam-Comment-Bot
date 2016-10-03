/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.valueproviders;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;

/**
 * This is a restricted case reference value provider which filters the port list on slots to those allowed by the slots' contract.
 * 
 * @author hinton
 * 
 */
public class SellMarketReferenceValueProvider extends BaseReferenceValueProvider {
	protected final MMXRootObject rootObject;

	public SellMarketReferenceValueProvider(final MMXRootObject rootObject) {
		this.rootObject = rootObject;
	}

	@Override
	public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);

			final List<SpotMarket> options = new LinkedList<>();

			options.addAll(spotMarketsModel.getFobSalesSpotMarket().getMarkets());
			options.addAll(spotMarketsModel.getDesSalesSpotMarket().getMarkets());

			final List<Pair<String, EObject>> values = options.stream() //
					.map(m -> new Pair<String, EObject>(m.getName(), m)) //
					.collect(Collectors.toList());

			// values.add(0, null);
			return values;
		}
		return Collections.emptyList();
	}

	@Override
	protected boolean isRelevantTarget(final Object target, final Object feature) {
		return super.isRelevantTarget(target, feature);
	}

	@Override
	public void dispose() {

	}

	@Override
	protected void cacheValues() {

	}
}

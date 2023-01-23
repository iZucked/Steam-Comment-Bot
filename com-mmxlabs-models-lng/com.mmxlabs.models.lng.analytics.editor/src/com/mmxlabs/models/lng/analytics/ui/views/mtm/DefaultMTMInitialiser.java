/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

public class DefaultMTMInitialiser implements IMTMInitialiser {

	@Override
	public boolean initialiseModel(@NonNull LNGScenarioModel scenarioModel, final @NonNull MTMModel model) {
		final List<SpotMarket> markets = new LinkedList();
		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
		
		final SpotMarketGroup smgDS = spotModel.getDesSalesSpotMarket();
		if (smgDS != null) {
			for (final SpotMarket spotMarket : smgDS.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					markets.add(spotMarket);
				}
			}
		}
		final SpotMarketGroup smgFS = spotModel.getFobSalesSpotMarket();
		if (smgFS != null) {
			for (final SpotMarket spotMarket : smgFS.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					markets.add(spotMarket);
				}
			}
		}
		final SpotMarketGroup smgDP = spotModel.getDesPurchaseSpotMarket();
		if (smgDP != null) {
			for (final SpotMarket spotMarket : smgDP.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					markets.add(spotMarket);
				}
			}
		}
		final SpotMarketGroup smgFP = spotModel.getFobPurchasesSpotMarket();
		if (smgFP != null) {
			for (final SpotMarket spotMarket : smgFP.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					markets.add(spotMarket);
				}
			}
		}
		
		if (markets.isEmpty()) return false;
		model.getMarkets().addAll(markets);
		
		final List<CharterInMarket> charters = new LinkedList();
		
		for (final CharterInMarket cim : spotModel.getCharterInMarkets()) {
			if (cim != null && cim.isEnabled() && cim.isNominal() && cim.isMtm() && cim.getVessel() != null) {
				charters.add(cim);
			}
		}
		
		if (charters.isEmpty()) return false;
		model.getNominalMarkets().addAll(charters);
		
		return true;
	}

}

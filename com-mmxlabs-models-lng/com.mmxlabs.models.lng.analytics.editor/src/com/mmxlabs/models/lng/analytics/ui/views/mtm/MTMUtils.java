/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public final class MTMUtils {
	public static MTMModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name, 
			final boolean allowCargoes, final boolean allowSpotCargoes) {
		final MTMModel model = AnalyticsFactory.eINSTANCE.createMTMModel();
		model.setName(name);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(sm);

		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			if (allowSlot(slot, allowCargoes, allowSpotCargoes)) {
				final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
				buy.setSlot(slot);
				model.getBuys().add(buy);
			}
		}
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			if (allowSlot(slot, allowCargoes, allowSpotCargoes)) {
				final SellReference sale = AnalyticsFactory.eINSTANCE.createSellReference();
				sale.setSlot(slot);
				model.getSells().add(sale);
			}
		}
		for (final CharterInMarket cim : spotModel.getCharterInMarkets()) {
			if (cim != null && cim.isEnabled() && cim.isNominal() && cim.isMtm() && cim.getVessel() != null) {
				model.getNominalMarkets().add(cim);
			}
		}
		final SpotMarketGroup smgDS = spotModel.getDesSalesSpotMarket();
		if (smgDS != null) {
			for (final SpotMarket spotMarket : smgDS.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					model.getMarkets().add(spotMarket);
				}
			}
		}
		final SpotMarketGroup smgFS = spotModel.getFobSalesSpotMarket();
		if (smgFS != null) {
			for (final SpotMarket spotMarket : smgFS.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					model.getMarkets().add(spotMarket);
				}
			}
		}
		final SpotMarketGroup smgDP = spotModel.getDesPurchaseSpotMarket();
		if (smgDP != null) {
			for (final SpotMarket spotMarket : smgDP.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					model.getMarkets().add(spotMarket);
				}
			}
		}
		final SpotMarketGroup smgFP = spotModel.getFobPurchasesSpotMarket();
		if (smgFP != null) {
			for (final SpotMarket spotMarket : smgFP.getMarkets()) {
				if (spotMarket != null && spotMarket.isMtm()) {
					model.getMarkets().add(spotMarket);
				}
			}
		}

		populateModel(model);
		
		return model;
	}
	
	public static void populateModel(final @NonNull MTMModel model) {
		for (final BuyOption bo : model.getBuys()) {
			final MTMRow row = AnalyticsFactory.eINSTANCE.createMTMRow();
			row.setBuyOption(bo);
			model.getRows().add(row);
		}
		for (final SellOption so : model.getSells()) {
			final MTMRow row = AnalyticsFactory.eINSTANCE.createMTMRow();
			row.setSellOption(so);
			model.getRows().add(row);
		}
	}
	
	public static boolean allowSlot(final Slot slot, final boolean allowCargoes, final boolean allowSpotCargoes) {
		if (slot instanceof SpotSlot) {
			return false;
		}
		if (allowSpotCargoes) {
			final Cargo cargo = slot.getCargo();
			if (cargo != null) {
				for (final Slot s : cargo.getSlots()) {
					if (s instanceof SpotSlot) {
						return true;
					}
				}
				return false;
			}
		}
		if (allowCargoes) {
			return true;
		}
		return slot.getCargo() == null;
	}
	
	public static MTMModel evaluateMTMModel(final LNGScenarioModel scenarioModel, final ScenarioInstance scenarioInstance, 
			final IScenarioDataProvider sdp, final boolean allowCargoes, final String modelName, final boolean allowSpotCargoes) {
		final MTMModel model = MTMUtils.createModelFromScenario(scenarioModel, modelName, allowCargoes, allowSpotCargoes);
		MTMSandboxEvaluator.evaluate(sdp, scenarioInstance, model);
		return model;
	}
}

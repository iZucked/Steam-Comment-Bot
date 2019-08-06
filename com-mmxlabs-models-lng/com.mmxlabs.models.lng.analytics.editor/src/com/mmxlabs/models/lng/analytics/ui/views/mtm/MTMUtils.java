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
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public final class MTMUtils {
	
	public static MTMModel createModelFromScenario(final LNGScenarioModel sm, final @NonNull String name, 
			final boolean allowCargoes, final boolean allowSpotCargoes){
		if (sm == null) {
			return null;
		}
		final MTMModel[] model = new MTMModel[1];
		ServiceHelper.withOptionalServiceConsumer(IMTMInitialiser.class, customiser -> {
			if (customiser == null) {
				model[0] = createModelFromScenario(sm, name, allowCargoes, allowSpotCargoes, new DefaultMTMInitialiser());
			} else {
				model[0] = createModelFromScenario(sm, name, allowCargoes, allowSpotCargoes, customiser);
			}
		});
		
		return model[0];
	}
	
	protected static MTMModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name, 
			final boolean allowCargoes, final boolean allowSpotCargoes, final IMTMInitialiser customiser){
		final MTMModel model = AnalyticsFactory.eINSTANCE.createMTMModel();
		model.setName(name);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);

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
		
		if (customiser.initialiseModel(sm, model)) {
			populateModel(model);
		}
		return model;
	}
	
	protected static void populateModel(final @NonNull MTMModel model) {
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
	
	protected static boolean allowSlot(final Slot slot, final boolean allowCargoes, final boolean allowSpotCargoes) {
		if (slot instanceof SpotSlot || slot.isCancelled()) {
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
	
	public static void evaluateMTMModel(final MTMModel model, final ScenarioInstance scenarioInstance, final IScenarioDataProvider sdp) {
		MTMSandboxEvaluator.evaluate(sdp, scenarioInstance, model);
	}
	
}

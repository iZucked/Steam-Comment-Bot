/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
//package com.mmxlabs.models.lng.analytics.ui.views.evaluators;
//
//import java.util.function.BiConsumer;
//
//import org.eclipse.emf.ecore.util.EcoreUtil;
//import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
//
//import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
//import com.mmxlabs.models.lng.analytics.BaseCase;
//import com.mmxlabs.models.lng.analytics.BaseCaseRow;
//import com.mmxlabs.models.lng.analytics.BuyOption;
//import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
//import com.mmxlabs.models.lng.analytics.ResultSet;
//import com.mmxlabs.models.lng.analytics.SellOption;
//import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator.IMapperClass;
//import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator.Mapper;
//import com.mmxlabs.models.lng.cargo.Cargo;
//import com.mmxlabs.models.lng.cargo.CargoFactory;
//import com.mmxlabs.models.lng.cargo.DischargeSlot;
//import com.mmxlabs.models.lng.cargo.LoadSlot;
//import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
//import com.mmxlabs.models.mmxcore.MMXRootObject;
//import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
//
//public class ResultsSetEvaluator extends BaseCaseEvaluator {
//	protected static void buildScenario(final LNGScenarioModel clone, final OptionAnalysisModel clonedModel, final BaseCase clonedBaseCase, final IMapperClass mapper) {
//
//		createShipping(clone, clonedBaseCase);
//		ResultSet rs = new ResultSet();
//		for (final AnalysisResultRow row : rs.getRows()) {
//			final BuyOption buy = row.getBuyOption();
//			final LoadSlot loadSlot = AnalyticsBuilder.makeLoadSlot(buy, clone);
//
//			mapper.addMapping(buy, loadSlot);
//
//			final SellOption sell = row.getSellOption();
//			final DischargeSlot dischargeSlot = AnalyticsBuilder.makeDischargeSlot(sell, clone);
//			mapper.addMapping(sell, dischargeSlot);
//
//			Cargo cargo = null;
//			if (loadSlot != null && dischargeSlot != null) {
//				cargo = CargoFactory.eINSTANCE.createCargo();
//				cargo.getSlots().add(loadSlot);
//				cargo.getSlots().add(dischargeSlot);
//			}
//			setShipping(loadSlot, dischargeSlot, cargo, row.getShipping(), clone);
//
//			if (loadSlot != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot)) {
//				clone.getCargoModel().getLoadSlots().add(loadSlot);
//			}
//			if (dischargeSlot != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot)) {
//				clone.getCargoModel().getDischargeSlots().add(dischargeSlot);
//			}
//			if (cargo != null && !clone.getCargoModel().getCargoes().contains(cargo)) {
//				clone.getCargoModel().getCargoes().add(cargo);
//			}
//		}
//
//	}
//
//	public static LNGScenarioModel generateScenario(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model, final BaseCase baseCase,
//			final BiConsumer<LNGScenarioModel, IMapperClass> callback) {
//
//		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
//		if (rootObject instanceof LNGScenarioModel) {
//			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
//
//			final EcoreUtil.Copier copier = new Copier();
//			final LNGScenarioModel clone = (LNGScenarioModel) copier.copy(lngScenarioModel);
//			final OptionAnalysisModel clonedModel = (OptionAnalysisModel) copier.copy(model);
////			final BaseCase clonedBaseCase = model.getBaseCase() == baseCase ? clonedModel.getBaseCase() : (BaseCase) copier.copy(baseCase);
//			copier.copyReferences();
//
//			final IMapperClass mapper = new Mapper(copier);
//
//			clearData(clone, clonedModel, clonedBaseCase);
//
//			buildScenario(clone, clonedModel, clonedBaseCase, mapper);
//
//			callback.accept(clone, mapper);
//
//			return clone;
//
//		}
//		return null;
//	}
//
//	public static void evaluate(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model, final BaseCase baseCase) {
//
//		generateScenario(scenarioEditingLocation, model, baseCase, (clone, mapper) -> {
//			evaluateScenario(clone, scenarioEditingLocation.getScenarioInstance());
//
//			updateResults(scenarioEditingLocation, clone, baseCase);
//		});
//
//	}
//
//}

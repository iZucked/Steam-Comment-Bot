package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator.BreakEvenMode;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator.IMapperClass;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;

public class WhatIfEvaluator {

	public static void evaluate(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model) {

		final long targetPNL = model.getBaseCase().getProfitAndLoss();

		final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();

		final List<List<Runnable>> combinations = new LinkedList<>();

		for (final PartialCaseRow r : model.getPartialCase().getPartialCase()) {
			final BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			if (r.getBuyOptions().size() > 1) {
				final List<Runnable> options = new LinkedList<>();
				for (final BuyOption o : r.getBuyOptions()) {
					options.add(() -> bcr.setBuyOption(o));
				}
				combinations.add(options);
			} else if (r.getBuyOptions().size() == 1) {
				bcr.setBuyOption(r.getBuyOptions().get(0));
			}

			if (r.getSellOptions().size() > 1) {
				final List<Runnable> options = new LinkedList<>();
				for (final SellOption o : r.getSellOptions()) {
					options.add(() -> {
						bcr.setSellOption(o);
					});
				}
				combinations.add(options);
			} else if (r.getSellOptions().size() == 1) {
				bcr.setSellOption(r.getSellOptions().get(0));
			}
			bcr.setShipping(EcoreUtil.copy(r.getShipping()));
			baseCase.getBaseCase().add(bcr);
		}
		// TODO:Command
		model.getResultSets().clear();
		if (combinations.isEmpty()) {
			singleEval(scenarioEditingLocation, targetPNL, model, baseCase);
		} else {
			recursiveEval(0, combinations, scenarioEditingLocation, targetPNL, model, baseCase);
		}
	}

	private static void singleEval(final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL, final OptionAnalysisModel model, final BaseCase baseCase) {
		BaseCaseEvaluator.generateScenario(scenarioEditingLocation, model, baseCase, (lngScenarioModel, mapper) -> {

			evaluateScenario(lngScenarioModel, model.isUseTargetPNL(), targetPNL);

			if (lngScenarioModel.getScheduleModel().getSchedule() == null) {
				return;
			}

			final ResultSet resultSet = AnalyticsFactory.eINSTANCE.createResultSet();

			for (final BaseCaseRow row : baseCase.getBaseCase()) {
				final AnalysisResultRow res = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
				res.setBuyOption(row.getBuyOption());
				res.setSellOption(row.getSellOption());

				Triple<SlotAllocation, SlotAllocation, CargoAllocation> t = finder(lngScenarioModel, row, mapper);
				SlotAllocation loadAllocation = t.getFirst();
				SlotAllocation dischargeAllocation = t.getSecond();
				CargoAllocation cargoAllocation = t.getThird();

				if (isBreakEvenRow(loadAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(loadAllocation.getPrice());
					res.setResultDetail(r);
				} else if (isBreakEvenRow(dischargeAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(dischargeAllocation.getPrice());
					res.setResultDetail(r);
				} else {
					final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
					if (cargoAllocation != null) {
						r.setValue((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
					res.setResultDetail(r);
				}
				resultSet.getRows().add(res);
			}

			model.getResultSets().add(resultSet);
		});
	}

	private static Triple<SlotAllocation, SlotAllocation, CargoAllocation> finder(LNGScenarioModel lngScenarioModel, BaseCaseRow baseCaseRow, IMapperClass mapper) {

		CargoAllocation cargoAllocation = null;
		SlotAllocation loadAllocation = null;
		SlotAllocation dischargeAllocation = null;

		Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();
		EList<SlotAllocation> slotAllocations = schedule.getSlotAllocations();

		LoadSlot loadSlot = mapper.get(baseCaseRow.getBuyOption());
		DischargeSlot dischargeSlot = mapper.get(baseCaseRow.getSellOption());

		for (SlotAllocation a : slotAllocations) {
			if (a.getSlot() == loadSlot) {
				loadAllocation = a;
				if (cargoAllocation == null) {
					cargoAllocation = a.getCargoAllocation();
				}
			}
			if (a.getSlot() == dischargeSlot) {
				dischargeAllocation = a;
				if (cargoAllocation == null) {
					cargoAllocation = a.getCargoAllocation();
				}
			}
		}

		return new Triple<>(loadAllocation, dischargeAllocation, cargoAllocation);
	}

	private static boolean isBreakEvenRow(SlotAllocation slotAllocation) {

		if (slotAllocation != null) {
			Slot slot = slotAllocation.getSlot();
			if (slot != null) {
				String priceExpression = slot.getPriceExpression();
				if (priceExpression != null) {
					return priceExpression.equals("?");
				}
			}
		}

		return false;
	}

	private static void recursiveEval(int listIdx, List<List<Runnable>> combinations, final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL, final OptionAnalysisModel model,
			final BaseCase baseCase) {
		if (listIdx == combinations.size()) {
			singleEval(scenarioEditingLocation, targetPNL, model, baseCase);
			return;
		}
		List<Runnable> options = combinations.get(listIdx);
		for (Runnable r : options) {
			r.run();
			recursiveEval(listIdx + 1, combinations, scenarioEditingLocation, targetPNL, model, baseCase);
		}
	}

	private static void evaluateScenario(final LNGScenarioModel lngScenarioModel, boolean useTargetPNL, final long targetPNL) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.withService(IAnalyticsScenarioEvaluator.class, evaluator -> evaluator.breakEvenEvaluate(lngScenarioModel, userSettings, null, targetPNL, useTargetPNL ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT));
	}
}

package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
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
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
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
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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
			if (r.getShipping().size() > 1) {
				final List<Runnable> options = new LinkedList<>();
				for (final ShippingOption o : r.getShipping()) {
					options.add(() -> {
						bcr.setShipping(o);
					});
				}
				combinations.add(options);
			} else if (r.getShipping().size() == 1) {
				bcr.setShipping(r.getShipping().get(0));
			}
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

			// DEBUG: Pass in the scenario instance
			ScenarioInstance parentForFork = null;// scenarioEditingLocation.getScenarioInstance()
			evaluateScenario(lngScenarioModel, parentForFork, model.isUseTargetPNL(), targetPNL);

			if (lngScenarioModel.getScheduleModel().getSchedule() == null) {
				return;
			}

			final ResultSet resultSet = AnalyticsFactory.eINSTANCE.createResultSet();

			for (final BaseCaseRow row : baseCase.getBaseCase()) {
				final AnalysisResultRow res = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
				res.setBuyOption(row.getBuyOption());
				res.setSellOption(row.getSellOption());
				res.setShipping(row.getShipping());

				final Triple<SlotAllocation, SlotAllocation, CargoAllocation> t = finder(lngScenarioModel, row, mapper);
				final SlotAllocation loadAllocation = t.getFirst();
				final SlotAllocation dischargeAllocation = t.getSecond();
				final CargoAllocation cargoAllocation = t.getThird();

				ResultContainer container = AnalyticsFactory.eINSTANCE.createResultContainer();
				container.setCargoAllocation(cargoAllocation);
				if (loadAllocation != null) {
					container.getSlotAllocations().add(loadAllocation);
				}
				if (dischargeAllocation != null) {
					container.getSlotAllocations().add(dischargeAllocation);
				}
				if (cargoAllocation != null) {
					container.getSlotAllocations().add(dischargeAllocation);
				}

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
				if ((loadAllocation == null && row.getBuyOption() != null) || (dischargeAllocation == null && row.getSellOption() != null)) {
					final Pair<OpenSlotAllocation, OpenSlotAllocation> p = finder2(lngScenarioModel, row, mapper);
					if (p.getFirst() != null) {
						assert loadAllocation == null;
						container.getOpenSlotAllocations().add(p.getFirst());

						final double value = (double) p.getFirst().getGroupProfitAndLoss().getProfitAndLoss();
						if (res.getResultDetail() != null && res.getResultDetail() instanceof ProfitAndLossResult) {
							final ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) res.getResultDetail();
							profitAndLossResult.setValue(profitAndLossResult.getValue() + value);
						} else {
							final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
							r.setValue(value);
							res.setResultDetail(r);
						}
					}
					if (p.getSecond() != null) {
						assert dischargeAllocation == null;
						container.getOpenSlotAllocations().add(p.getSecond());

						final double value = (double) p.getSecond().getGroupProfitAndLoss().getProfitAndLoss();
						if (res.getResultDetail() != null && res.getResultDetail() instanceof ProfitAndLossResult) {
							final ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) res.getResultDetail();
							profitAndLossResult.setValue(profitAndLossResult.getValue() + value);
						} else {
							final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
							r.setValue(value);
							res.setResultDetail(r);
						}
					}
				}
				res.setResultDetails(container);
				// Clear old references
				if (container.getCargoAllocation() != null) {
					container.getCargoAllocation().unsetInputCargo();
				}
				for (SlotAllocation slotAllocation : container.getSlotAllocations()) {
					slotAllocation.unsetSlot();
					slotAllocation.unsetSpotMarket();
				}
				for (OpenSlotAllocation openSlotAllocation : container.getOpenSlotAllocations()) {
					openSlotAllocation.unsetSlot();
				}

				resultSet.getRows().add(res);
			}

			model.getResultSets().add(resultSet);
		});
	}

	private static Triple<SlotAllocation, SlotAllocation, CargoAllocation> finder(final LNGScenarioModel lngScenarioModel, final BaseCaseRow baseCaseRow, final IMapperClass mapper) {

		CargoAllocation cargoAllocation = null;
		SlotAllocation loadAllocation = null;
		SlotAllocation dischargeAllocation = null;

		final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();
		final EList<SlotAllocation> slotAllocations = schedule.getSlotAllocations();

		final LoadSlot loadSlot = mapper.get(baseCaseRow.getBuyOption());
		final DischargeSlot dischargeSlot = mapper.get(baseCaseRow.getSellOption());

		for (final SlotAllocation a : slotAllocations) {
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

	private static Pair<OpenSlotAllocation, OpenSlotAllocation> finder2(final LNGScenarioModel lngScenarioModel, final BaseCaseRow baseCaseRow, final IMapperClass mapper) {
		OpenSlotAllocation loadAllocation = null;
		OpenSlotAllocation dischargeAllocation = null;

		final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

		final LoadSlot loadSlot = mapper.get(baseCaseRow.getBuyOption());
		final DischargeSlot dischargeSlot = mapper.get(baseCaseRow.getSellOption());

		for (final OpenSlotAllocation a : schedule.getOpenSlotAllocations()) {
			if (a.getSlot() == loadSlot) {
				loadAllocation = a;
			}
			if (a.getSlot() == dischargeSlot) {
				dischargeAllocation = a;
			}
		}

		return new Pair<>(loadAllocation, dischargeAllocation);
	}

	private static boolean isBreakEvenRow(final SlotAllocation slotAllocation) {

		if (slotAllocation != null) {
			final Slot slot = slotAllocation.getSlot();
			if (slot != null) {
				final String priceExpression = slot.getPriceExpression();
				if (priceExpression != null) {
					return priceExpression.equals("?");
				}
			}
		}

		return false;
	}

	private static void recursiveEval(final int listIdx, final List<List<Runnable>> combinations, final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL,
			final OptionAnalysisModel model, final BaseCase baseCase) {
		if (listIdx == combinations.size()) {
			singleEval(scenarioEditingLocation, targetPNL, model, baseCase);
			return;
		}
		final List<Runnable> options = combinations.get(listIdx);
		for (final Runnable r : options) {
			r.run();
			recursiveEval(listIdx + 1, combinations, scenarioEditingLocation, targetPNL, model, baseCase);
		}
	}

	private static void evaluateScenario(final LNGScenarioModel lngScenarioModel, @Nullable ScenarioInstance parentForFork, final boolean useTargetPNL, final long targetPNL) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withService(IAnalyticsScenarioEvaluator.class,
				evaluator -> evaluator.breakEvenEvaluate(lngScenarioModel, userSettings, parentForFork, targetPNL, useTargetPNL ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT));
	}
}

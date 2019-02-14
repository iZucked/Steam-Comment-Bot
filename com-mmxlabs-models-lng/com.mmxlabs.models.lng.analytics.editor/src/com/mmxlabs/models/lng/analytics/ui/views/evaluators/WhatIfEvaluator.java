/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.YearMonth;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.Result;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parseutils.IndexConversion;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;

public class WhatIfEvaluator {

	public static void evaluateBaseCase(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model) {

		final BaseCase baseCase = model.getBaseCase();

		final CompoundCommand cmd = new CompoundCommand("Generate results");
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT, SetCommand.UNSET_VALUE));
		final long a = System.currentTimeMillis();
		final List<BaseCase> tasks = new LinkedList<>();
		recursiveTaskCreator(0, Collections.emptyList(), scenarioEditingLocation, 0, model, baseCase, tasks);

		filterTasks(tasks);
		final Result result = multiEval(scenarioEditingLocation, 0, model, tasks);
		long pnl = 0L;
		if (!result.getResultSets().isEmpty()) {
			final ResultSet rs1 = result.getResultSets().get(0);
			pnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(rs1.getScheduleModel().getSchedule());
		}
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), baseCase, AnalyticsPackage.Literals.BASE_CASE__PROFIT_AND_LOSS, pnl));
		final long b = System.currentTimeMillis();
		System.out.printf("Eval %d\n", b - a);

		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT, result));
		if (!cmd.isEmpty()) {
			scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, model, null);
		}
	}

	public static void evaluate(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model) {

		final long targetPNL = model.getBaseCase().getProfitAndLoss();

		final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
		baseCase.setKeepExistingScenario(model.getPartialCase().isKeepExistingScenario());
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
					options.add(() -> bcr.setSellOption(o));
				}
				combinations.add(options);
			} else if (r.getSellOptions().size() == 1) {
				bcr.setSellOption(r.getSellOptions().get(0));
			}
			if (r.getShipping().size() > 1) {
				final List<Runnable> options = new LinkedList<>();
				for (final ShippingOption o : r.getShipping()) {
					options.add(() -> bcr.setShipping(o));
				}
				combinations.add(options);
			} else if (r.getShipping().size() == 1) {
				bcr.setShipping(r.getShipping().get(0));
			}
			baseCase.getBaseCase().add(bcr);
		}

		final CompoundCommand cmd = new CompoundCommand("Generate results");
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS, SetCommand.UNSET_VALUE));

		final long a = System.currentTimeMillis();
		final List<BaseCase> tasks = new LinkedList<>();
		recursiveTaskCreator(0, combinations, scenarioEditingLocation, targetPNL, model, baseCase, tasks);

		filterTasks(tasks);
		Result result = multiEval(scenarioEditingLocation, targetPNL, model, tasks);

		final long b = System.currentTimeMillis();
		System.out.printf("Eval %d\n", b - a);

		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS, result));
		if (!cmd.isEmpty()) {
			scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, model, null);
		}
	}

	private static void filterTasks(final List<BaseCase> tasks) {
		final Set<BaseCase> duplicates = new HashSet<>();
		for (final BaseCase baseCase1 : tasks) {
			DUPLICATE_TEST: for (final BaseCase baseCase2 : tasks) {
				if (duplicates.contains(baseCase1) || duplicates.contains(baseCase2) || baseCase1 == baseCase2 || baseCase1.getBaseCase().size() != baseCase2.getBaseCase().size()) {
					continue;
				}
				for (int i = 0; i < baseCase1.getBaseCase().size(); i++) {
					final BaseCaseRow baseCase1Row = baseCase1.getBaseCase().get(i);
					final BaseCaseRow baseCase2Row = baseCase2.getBaseCase().get(i);
					if (baseCase1Row.getBuyOption() != baseCase2Row.getBuyOption() || baseCase1Row.getSellOption() != baseCase2Row.getSellOption()
							|| baseCase1Row.getShipping() != baseCase2Row.getShipping()) {
						continue DUPLICATE_TEST;
					}
				}
				duplicates.add(baseCase2);
			}
		}
		tasks.removeAll(duplicates);
	}

	private static Result multiEval(final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL, final OptionAnalysisModel model, final List<BaseCase> baseCases) {

		final ConcurrentLinkedQueue<ResultSet> ref = new ConcurrentLinkedQueue<>();

		final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
		final IMapperClass mapper = new Mapper(lngScenarioModel);

		final BaseCaseToScheduleSpecification builder = new BaseCaseToScheduleSpecification(lngScenarioModel, mapper);

		final List<Pair<BaseCase, ScheduleSpecification>> specifications = baseCases.stream() //
				.map(baseCase -> new Pair<>(baseCase, builder.generate(baseCase))) //
				.collect(Collectors.toList());

		final Result result = AnalyticsFactory.eINSTANCE.createResult();

		final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();
		result.getCharterInMarketOverrides().addAll(extraDataProvider.extraCharterInMarketOverrides);
		result.getExtraCharterInMarkets().addAll(extraDataProvider.extraCharterInMarkets);
		result.getExtraVesselAvailabilities().addAll(extraDataProvider.extraVesselAvailabilities);
		result.getExtraSlots().addAll(extraDataProvider.extraLoads);
		result.getExtraSlots().addAll(extraDataProvider.extraDischarges);

		final BiConsumer<BaseCase, Schedule> runner = (baseCase, schedule) -> {

			final ResultSet resultSet = AnalyticsFactory.eINSTANCE.createResultSet();

			for (final BaseCaseRow row : baseCase.getBaseCase()) {
				final AnalysisResultRow res = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
				res.setBuyOption(row.getBuyOption());
				res.setSellOption(row.getSellOption());
				if ((!AnalyticsBuilder.isShipped(row.getBuyOption()) || !AnalyticsBuilder.isShipped(row.getSellOption())) && AnalyticsBuilder.isShipped(row.getShipping())) {
					res.setShipping(null);
				} else {
					res.setShipping(row.getShipping());
				}

				final Triple<SlotAllocation, SlotAllocation, CargoAllocation> t = finder(schedule, row, mapper);
				final SlotAllocation loadAllocation = t.getFirst();
				final SlotAllocation dischargeAllocation = t.getSecond();
				final CargoAllocation cargoAllocation = t.getThird();

				// Add refs for ease of lookup later
				final ResultContainer container = AnalyticsFactory.eINSTANCE.createResultContainer();
				if (cargoAllocation != null) {
					container.setCargoAllocation(cargoAllocation);
				}
				if (loadAllocation != null) {
					container.getSlotAllocations().add(loadAllocation);
				}
				if (dischargeAllocation != null) {
					container.getSlotAllocations().add(dischargeAllocation);
				}

				if (isBreakEvenRow(loadAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(loadAllocation.getPrice());
					final String priceString = getPriceString(ScenarioModelUtil.getPricingModel(lngScenarioModel), ((BuyOpportunity) row.getBuyOption()).getPriceExpression(),
							loadAllocation.getPrice(), YearMonth.from(loadAllocation.getSlotVisit().getStart()));
					r.setPriceString(priceString);
					res.setResultDetail(r);
					if (cargoAllocation != null) {
						r.setCargoPNL((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
				} else if (isBreakEvenRow(dischargeAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(dischargeAllocation.getPrice());
					final String priceString = getPriceString(ScenarioModelUtil.getPricingModel(lngScenarioModel), ((SellOpportunity) row.getSellOption()).getPriceExpression(),
							dischargeAllocation.getPrice(), YearMonth.from(dischargeAllocation.getSlotVisit().getStart()));
					r.setPriceString(priceString);
					res.setResultDetail(r);
					if (cargoAllocation != null) {
						r.setCargoPNL((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
				} else {
					final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
					if (cargoAllocation != null) {
						r.setValue((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
					res.setResultDetail(r);
				}
				if ((loadAllocation == null && row.getBuyOption() != null) || (dischargeAllocation == null && row.getSellOption() != null)) {
					final Pair<OpenSlotAllocation, OpenSlotAllocation> p = finder2(schedule, row, mapper);
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

				resultSet.getRows().add(res);
			}
			final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
			scheduleModel.setSchedule(schedule);
			scheduleModel.setDirty(false);
			resultSet.setScheduleModel(scheduleModel);
			ref.add(resultSet);
		};

		// DEBUG: Pass in the scenario instance
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withServiceConsumer(IAnalyticsScenarioEvaluator.class,
				evaluator -> evaluator.multiEvaluate(scenarioEditingLocation.getScenarioInstance(), scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getScenarioDataProvider(), userSettings, targetPNL,
						model.isUseTargetPNL() ? IAnalyticsScenarioEvaluator.BreakEvenMode.PORTFOLIO : IAnalyticsScenarioEvaluator.BreakEvenMode.POINT_TO_POINT, specifications, mapper, runner));

		result.getResultSets().addAll(ref);
		return result;
	}

	private static String getPriceString(@NonNull final PricingModel pricingModel, @NonNull final String expression, final double breakevenPrice, @NonNull final YearMonth date) {
		if (expression.equals("?")) {
			return String.format("%,.3f", breakevenPrice);
		}
		final double rearrangedPrice = IndexConversion.getRearrangedPrice(pricingModel, expression, breakevenPrice, date);
		return expression.replaceFirst(Pattern.quote("?"), String.format("%,.2f", rearrangedPrice));
	}

	private static Triple<SlotAllocation, SlotAllocation, CargoAllocation> finder(final Schedule schedule, final BaseCaseRow baseCaseRow, final IMapperClass mapper) {

		CargoAllocation cargoAllocation = null;
		SlotAllocation loadAllocation = null;
		SlotAllocation dischargeAllocation = null;

		final EList<SlotAllocation> slotAllocations = schedule.getSlotAllocations();

		final LoadSlot loadSlot = mapper.getOriginal(baseCaseRow.getBuyOption());
		final DischargeSlot dischargeSlot = mapper.getOriginal(baseCaseRow.getSellOption());

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

	private static Pair<OpenSlotAllocation, OpenSlotAllocation> finder2(final Schedule schedule, final BaseCaseRow baseCaseRow, final IMapperClass mapper) {
		OpenSlotAllocation loadAllocation = null;
		OpenSlotAllocation dischargeAllocation = null;

		final LoadSlot loadSlot = mapper.getOriginal(baseCaseRow.getBuyOption());
		final DischargeSlot dischargeSlot = mapper.getOriginal(baseCaseRow.getSellOption());

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
					return priceExpression.contains("?");
				}
			}
		}

		return false;
	}

	private static void recursiveTaskCreator(final int listIdx, final List<List<Runnable>> combinations, final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL,
			final OptionAnalysisModel model, final BaseCase baseCase, final List<BaseCase> tasks) {
		if (listIdx == combinations.size()) {
			final BaseCase copy = EcoreUtil.copy(baseCase);
			filterShipping(copy);
			tasks.add(copy);
			return;
		}

		final List<Runnable> options = combinations.get(listIdx);
		for (final Runnable r : options) {
			r.run();
			recursiveTaskCreator(listIdx + 1, combinations, scenarioEditingLocation, targetPNL, model, baseCase, tasks);
		}
	}

	private static void filterShipping(final BaseCase copy) {
		for (final BaseCaseRow baseCaseRow : copy.getBaseCase()) {
			if (baseCaseRow.getBuyOption() != null && ((baseCaseRow.getBuyOption() instanceof BuyReference && ((BuyReference) baseCaseRow.getBuyOption()).getSlot().isDESPurchase())
					|| (baseCaseRow.getBuyOption() instanceof BuyOpportunity && ((BuyOpportunity) baseCaseRow.getBuyOption()).isDesPurchase()))) {
				baseCaseRow.setShipping(null);
			}
		}
	}

}

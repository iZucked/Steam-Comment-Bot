package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MultipleResultGrouper;
import com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
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
		int idx = 1;
		final List<List<Pair<EObject, Supplier<MultipleResultGrouperRow>>>> combinations = new LinkedList<>();
		final List<MultipleResultGrouper> groups = new LinkedList<>();
		for (final PartialCaseRow r : model.getPartialCase().getPartialCase()) {
			final BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			if (r.getBuyOptions().size() > 1) {
				final MultipleResultGrouper g = AnalyticsFactory.eINSTANCE.createMultipleResultGrouper();
				g.setReferenceRow(r);
				g.setFeatureName("buy");
				g.setName(String.format("{%d}", idx++));
				final List<Pair<EObject, Supplier<MultipleResultGrouperRow>>> options = new LinkedList<>();
				for (final BuyOption o : r.getBuyOptions()) {
					final MultipleResultGrouperRow row = AnalyticsFactory.eINSTANCE.createMultipleResultGrouperRow();
					row.setObject(o);
					g.getGroupResults().add(row);

					options.add(new Pair<EObject, Supplier<MultipleResultGrouperRow>>(o, () -> {
						bcr.setBuyOption(o);
						return row;
					}));
				}
				groups.add(g);
				combinations.add(options);
			} else if (r.getBuyOptions().size() == 1) {
				bcr.setBuyOption(r.getBuyOptions().get(0));
			}

			if (r.getSellOptions().size() > 1) {
				final List<Pair<EObject, Supplier<MultipleResultGrouperRow>>> options = new LinkedList<>();
				final MultipleResultGrouper g = AnalyticsFactory.eINSTANCE.createMultipleResultGrouper();
				g.setReferenceRow(r);
				g.setFeatureName("sell");
				g.setName(String.format("{%d}", idx++));
				for (final SellOption o : r.getSellOptions()) {
					final MultipleResultGrouperRow row = AnalyticsFactory.eINSTANCE.createMultipleResultGrouperRow();
					row.setObject(o);
					g.getGroupResults().add(row);
					options.add(new Pair<EObject, Supplier<MultipleResultGrouperRow>>(o, () -> {
						bcr.setSellOption(o);
						return row;
					}));
				}
				groups.add(g);
				combinations.add(options);
			} else if (r.getSellOptions().size() == 1) {
				bcr.setSellOption(r.getSellOptions().get(0));
			}
			if (r.getShipping().size() > 1) {
				final List<Pair<EObject, Supplier<MultipleResultGrouperRow>>> options = new LinkedList<>();
				final MultipleResultGrouper g = AnalyticsFactory.eINSTANCE.createMultipleResultGrouper();
				g.setReferenceRow(r);
				g.setFeatureName("shipping");
				g.setName(String.format("{%d}", idx++));
				for (final ShippingOption o : r.getShipping()) {
					final MultipleResultGrouperRow row = AnalyticsFactory.eINSTANCE.createMultipleResultGrouperRow();
					row.setObject(o);
					g.getGroupResults().add(row);
					options.add(new Pair<EObject, Supplier<MultipleResultGrouperRow>>(o, () -> {
						bcr.setShipping(o);
						return row;
					}));
				}
				groups.add(g);
				combinations.add(options);
			} else if (r.getShipping().size() == 1) {
				bcr.setShipping(r.getShipping().get(0));
			}
			baseCase.getBaseCase().add(bcr);
		}

		// TODO: Command
		model.getResultSets().clear();
		model.getResultGroups().clear();
		if (combinations.isEmpty()) {
			singleEval(scenarioEditingLocation, targetPNL, model, baseCase);
		} else {
			recursiveEval(0, combinations, scenarioEditingLocation, targetPNL, model, baseCase, new LinkedList<>());
			model.getResultGroups().addAll(groups);
		}
	}

	private static ResultSet singleEval(final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL, final OptionAnalysisModel model, final BaseCase baseCase) {

		final ResultSet[] ref = new ResultSet[1];
		BaseCaseEvaluator.generateScenario(scenarioEditingLocation, model, baseCase, (lngScenarioModel, mapper) -> {

			// DEBUG: Pass in the scenario instance
			final ScenarioInstance parentForFork = null;// scenarioEditingLocation.getScenarioInstance()
			evaluateScenario(lngScenarioModel, parentForFork, model.isUseTargetPNL(), targetPNL);

			if (lngScenarioModel.getScheduleModel().getSchedule() == null) {
				return;
			}

			final ResultSet resultSet = AnalyticsFactory.eINSTANCE.createResultSet();

			for (final BaseCaseRow row : baseCase.getBaseCase()) {
				final AnalysisResultRow res = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
				res.setBuyOption(row.getBuyOption());
				res.setSellOption(row.getSellOption());
				if ((!AnalyticsBuilder.isShipped(row.getBuyOption()) ||
						!AnalyticsBuilder.isShipped(row.getSellOption())) && AnalyticsBuilder.isShipped(row.getShipping())) {
					res.setShipping(null);
				} else {
					res.setShipping(row.getShipping());
				}

				final Triple<SlotAllocation, SlotAllocation, CargoAllocation> t = finder(lngScenarioModel, row, mapper);
				final SlotAllocation loadAllocation = t.getFirst();
				final SlotAllocation dischargeAllocation = t.getSecond();
				final CargoAllocation cargoAllocation = t.getThird();

				final ResultContainer container = AnalyticsFactory.eINSTANCE.createResultContainer();
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
				for (final SlotAllocation slotAllocation : container.getSlotAllocations()) {
					slotAllocation.unsetSlot();
					slotAllocation.unsetSpotMarket();
				}
				for (final OpenSlotAllocation openSlotAllocation : container.getOpenSlotAllocations()) {
					openSlotAllocation.unsetSlot();
				}

				resultSet.getRows().add(res);
			}

			model.getResultSets().add(resultSet);

			ref[0] = resultSet;
		});
		return ref[0];
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

	private static void recursiveEval(final int listIdx, final List<List<Pair<EObject, Supplier<MultipleResultGrouperRow>>>> combinations, final IScenarioEditingLocation scenarioEditingLocation,
			final long targetPNL, final OptionAnalysisModel model, final BaseCase baseCase, final List<Consumer<ResultSet>> mutliResultSetters) {
		if (listIdx == combinations.size()) {
			final ResultSet rs = singleEval(scenarioEditingLocation, targetPNL, model, baseCase);
			mutliResultSetters.forEach(s -> s.accept(rs));
			return;
		}
		final List<Pair<EObject, Supplier<MultipleResultGrouperRow>>> options = combinations.get(listIdx);
		for (final Pair<EObject, Supplier<MultipleResultGrouperRow>> p : options) {
			final Supplier<MultipleResultGrouperRow> r = p.getSecond();
			final MultipleResultGrouperRow row = r.get();
			final List<Consumer<ResultSet>> next = new LinkedList<>(mutliResultSetters);
			next.add(rs -> row.getGroupResults().add(rs));
			recursiveEval(listIdx + 1, combinations, scenarioEditingLocation, targetPNL, model, baseCase, next);
		}
	}

	private static void evaluateScenario(final LNGScenarioModel lngScenarioModel, @Nullable final ScenarioInstance parentForFork, final boolean useTargetPNL, final long targetPNL) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withService(IAnalyticsScenarioEvaluator.class,
				evaluator -> evaluator.breakEvenEvaluate(lngScenarioModel, userSettings, parentForFork, targetPNL, useTargetPNL ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT));
	}
	
	public static Predicate<BuyOption> isDESPurchase() {
		return b -> ((b instanceof BuyReference && ((BuyReference) b).getSlot() != null && ((BuyReference) b).getSlot().isDESPurchase() == true)
				|| (b instanceof BuyOpportunity && ((BuyOpportunity) b).isDesPurchase() == true));
	}
	
	public static Predicate<SellOption> isFOBSale() {
		return s -> ((s instanceof SellReference && ((SellReference) s).getSlot() != null && ((SellReference) s).getSlot().isFOBSale() == true)
				|| (s instanceof SellOpportunity && ((SellOpportunity) s).isFobSale() == true));
	}
	
	private static Predicate<ShippingOption> isNominated() {
		return s -> s instanceof NominatedShippingOption;
	}
}

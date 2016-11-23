package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
import com.mmxlabs.models.lng.commercial.parseutils.IndexConversion;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.pricing.PricingModel;
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
		final CompoundCommand cmd = new CompoundCommand("Generate results");
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS, SetCommand.UNSET_VALUE));
		// cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_GROUPS, SetCommand.UNSET_VALUE));
		if (combinations.isEmpty()) {
			final ResultSet resultSet = singleEval(scenarioEditingLocation, targetPNL, model, baseCase);
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS, Collections.singletonList(resultSet)));
		} else {
			final List<Callable<Supplier<ResultSet>>> tasks = new LinkedList<>();
			recursiveEval(0, combinations, scenarioEditingLocation, targetPNL, model, baseCase, new LinkedList<>(), tasks);
			final List<Future<Supplier<ResultSet>>> futures = new LinkedList<>();
			@NonNull
			final ExecutorService executor = Executors.newFixedThreadPool(4);
			tasks.forEach(task -> futures.add(executor.submit(task)));

			for (final Future<Supplier<ResultSet>> f : futures) {
				Supplier<ResultSet> resultSetSupplier;
				try {
					resultSetSupplier = f.get();
					// This has a side effect of updating the result set....
					// TODO: See if this could be done in the callable
					final ResultSet rs = resultSetSupplier.get();
					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS, rs));
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}

		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_GROUPS, groups));
		if (!cmd.isEmpty()) {
			scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, model, null);
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
				if ((!AnalyticsBuilder.isShipped(row.getBuyOption()) || !AnalyticsBuilder.isShipped(row.getSellOption())) && AnalyticsBuilder.isShipped(row.getShipping())) {
					res.setShipping(null);
				} else {
					res.setShipping(row.getShipping());
				}

				final Triple<SlotAllocation, SlotAllocation, CargoAllocation> t = finder(lngScenarioModel, row, mapper);
				final SlotAllocation loadAllocation = t.getFirst();
				final SlotAllocation dischargeAllocation = t.getSecond();
				final CargoAllocation cargoAllocation = t.getThird();

				// Move containership of schedule objects to the container.
				final ResultContainer container = AnalyticsFactory.eINSTANCE.createResultContainer();
				if (cargoAllocation != null) {
					container.setCargoAllocation(cargoAllocation);
					container.getEvents().addAll(cargoAllocation.getEvents());
				}
				if (loadAllocation != null) {
					container.getSlotAllocations().add(loadAllocation);
					if (!container.getEvents().contains(loadAllocation.getSlotVisit())) {
						container.getEvents().add(loadAllocation.getSlotVisit());
					}
				}
				if (dischargeAllocation != null) {
					container.getSlotAllocations().add(dischargeAllocation);
					if (!container.getEvents().contains(dischargeAllocation.getSlotVisit())) {
						container.getEvents().add(dischargeAllocation.getSlotVisit());
					}
				}

				if (isBreakEvenRow(loadAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(loadAllocation.getPrice());
					final String priceString = getPriceString(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getReferenceModel().getPricingModel(),
							((BuyOpportunity) row.getBuyOption()).getPriceExpression(), loadAllocation.getPrice(), YearMonth.from(loadAllocation.getSlotVisit().getStart()));
					r.setPriceString(priceString);
					res.setResultDetail(r);
					if (cargoAllocation != null) {
						r.setCargoPNL((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
				} else if (isBreakEvenRow(dischargeAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(dischargeAllocation.getPrice());
					final String priceString = getPriceString(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getReferenceModel().getPricingModel(),
							((SellOpportunity) row.getSellOption()).getPriceExpression(), dischargeAllocation.getPrice(), YearMonth.from(dischargeAllocation.getSlotVisit().getStart()));
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

			ref[0] = resultSet;
		});
		return ref[0];
	}

	private static String getPriceString(@NonNull final PricingModel pricingModel, @NonNull final String expression, final double breakevenPrice, @NonNull final YearMonth date) {
		if (expression.equals("?")) {
			return String.format("%,.3f", breakevenPrice);
		}
		final double rearrangedPrice = IndexConversion.getRearrangedPrice(pricingModel, expression, breakevenPrice, date);
		return expression.replaceFirst(Pattern.quote("?"), String.format("%,.3f", rearrangedPrice));
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
					return priceExpression.contains("?");
				}
			}
		}

		return false;
	}

	private static void recursiveEval(final int listIdx, final List<List<Pair<EObject, Supplier<MultipleResultGrouperRow>>>> combinations, final IScenarioEditingLocation scenarioEditingLocation,
			final long targetPNL, final OptionAnalysisModel model, final BaseCase baseCase, final List<Consumer<ResultSet>> mutliResultSetters, final List<Callable<Supplier<ResultSet>>> tasks) {
		if (listIdx == combinations.size()) {
			final BaseCase copy = EcoreUtil.copy(baseCase);
			tasks.add(() -> {
				final ResultSet rs = singleEval(scenarioEditingLocation, targetPNL, model, copy);
				return () -> {
					mutliResultSetters.forEach(s -> s.accept(rs));
					return rs;
				};
			});
			return;
		}
		final List<Pair<EObject, Supplier<MultipleResultGrouperRow>>> options = combinations.get(listIdx);
		for (final Pair<EObject, Supplier<MultipleResultGrouperRow>> p : options) {
			final Supplier<MultipleResultGrouperRow> r = p.getSecond();
			final MultipleResultGrouperRow row = r.get();
			final List<Consumer<ResultSet>> next = new LinkedList<>(mutliResultSetters);
			next.add(rs -> row.getGroupResults().add(rs));
			recursiveEval(listIdx + 1, combinations, scenarioEditingLocation, targetPNL, model, baseCase, next, tasks);
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

	// private static String getIndex(String expression, double bePrice) {
	// if (expression )
	// }
}

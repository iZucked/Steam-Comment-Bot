/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.ExistingBaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.exceptions.InfeasibleSolutionException;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;

public class SandboxDefineRunner {

	private final IScenarioDataProvider originalScenarioDataProvider;

	private final EditingDomain originalEditingDomain;
	private final OptionAnalysisModel model;

	private final UserSettings userSettings;

	private final ScheduleSpecificationHelper helper;

	private final @Nullable ScenarioInstance scenarioInstance;

	private LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	private List<Pair<BaseCase, ScheduleSpecification>> specifications;

	private boolean skipRun = false;

	public SandboxDefineRunner(@Nullable final ScenarioInstance scenarioInstance, final IScenarioDataProvider scenarioDataProvider, final UserSettings userSettings, final IMapperClass mapper,
			final OptionAnalysisModel model) {

		this.scenarioInstance = scenarioInstance;
		this.originalScenarioDataProvider = scenarioDataProvider;
		this.originalEditingDomain = scenarioDataProvider.getEditingDomain();
		this.userSettings = userSettings;
		this.model = model;

		{

			final BaseCase templateBaseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
			templateBaseCase.setKeepExistingScenario(model.getPartialCase().isKeepExistingScenario());

			final List<List<Runnable>> combinations = new LinkedList<>();
			for (final PartialCaseRow r : model.getPartialCase().getPartialCase()) {
				final BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
				bcr.setOptions(AnalyticsFactory.eINSTANCE.createBaseCaseRowOptions());

				// If we mix vessel events AND cargoes in the same row, we ensure we always have
				// a null vessel event combination. Later on this will be handled in the
				// recursive tasks step

				boolean hasVE = false;
				if (!r.getVesselEventOptions().isEmpty()) {
					hasVE = true;
				}

				if (r.getVesselEventOptions().size() > 1) {
					final List<Runnable> options = new LinkedList<>();
					for (final VesselEventOption o : r.getVesselEventOptions()) {
						options.add(() -> bcr.setVesselEventOption(o));
					}
					// Add Empty case if needed
					if (!r.getBuyOptions().isEmpty() || !r.getSellOptions().isEmpty()) {
						options.add(() -> bcr.setVesselEventOption(null));
					}
					combinations.add(options);
				} else if (r.getVesselEventOptions().size() == 1) {
					// Add Empty case if needed
					if (!r.getBuyOptions().isEmpty() || !r.getSellOptions().isEmpty()) {
						final List<Runnable> options = new LinkedList<>();
						options.add(() -> bcr.setVesselEventOption(r.getVesselEventOptions().get(0)));
						options.add(() -> bcr.setVesselEventOption(null));
						combinations.add(options);
					} else {
						bcr.setVesselEventOption(r.getVesselEventOptions().get(0));
					}
				}

				if (hasVE || r.getBuyOptions().size() > 1) {
					if (!r.getBuyOptions().isEmpty()) {
						final List<Runnable> options = new LinkedList<>();
						for (final BuyOption o : r.getBuyOptions()) {
							options.add(() -> bcr.setBuyOption(o));
						}
						combinations.add(options);
					}
				} else if (r.getBuyOptions().size() == 1) {
					bcr.setBuyOption(r.getBuyOptions().get(0));
				}

				if (hasVE || r.getSellOptions().size() > 1) {
					if (!r.getSellOptions().isEmpty()) {
						final List<Runnable> options = new LinkedList<>();
						for (final SellOption o : r.getSellOptions()) {
							options.add(() -> bcr.setSellOption(o));
						}
						combinations.add(options);
					}
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
				final PartialCaseRowOptions rowOptions = r.getOptions();
				if (rowOptions != null) {
					if (rowOptions.getLadenRoutes().size() > 1) {
						final List<Runnable> options = new LinkedList<>();
						for (final RouteOption ro : rowOptions.getLadenRoutes()) {
							options.add(() -> bcr.getOptions().setLadenRoute(ro));
						}
						combinations.add(options);
					} else if (rowOptions.getLadenRoutes().size() == 1) {
						bcr.getOptions().setLadenRoute(rowOptions.getLadenRoutes().get(0));
					}
					if (rowOptions.getBallastRoutes().size() > 1) {
						final List<Runnable> options = new LinkedList<>();
						for (final RouteOption ro : rowOptions.getBallastRoutes()) {
							options.add(() -> bcr.getOptions().setBallastRoute(ro));
						}
						combinations.add(options);
					} else if (rowOptions.getBallastRoutes().size() == 1) {
						bcr.getOptions().setBallastRoute(rowOptions.getBallastRoutes().get(0));
					}

					if (rowOptions.getLadenFuelChoices().size() > 1) {
						final List<Runnable> options = new LinkedList<>();
						for (final FuelChoice ro : rowOptions.getLadenFuelChoices()) {
							options.add(() -> bcr.getOptions().setLadenFuelChoice(ro));
						}
						combinations.add(options);
					} else if (rowOptions.getLadenFuelChoices().size() == 1) {
						bcr.getOptions().setLadenFuelChoice(rowOptions.getLadenFuelChoices().get(0));
					}

					if (rowOptions.getBallastFuelChoices().size() > 1) {
						final List<Runnable> options = new LinkedList<>();
						for (final FuelChoice ro : rowOptions.getBallastFuelChoices()) {
							options.add(() -> bcr.getOptions().setBallastFuelChoice(ro));
						}
						combinations.add(options);
					} else if (rowOptions.getBallastFuelChoices().size() == 1) {
						bcr.getOptions().setBallastFuelChoice(rowOptions.getBallastFuelChoices().get(0));
					}

					if (rowOptions.getLoadDates().size() > 1) {
						final List<Runnable> options = new LinkedList<>();
						for (final LocalDateTimeHolder ro : rowOptions.getLoadDates()) {
							options.add(() -> bcr.getOptions().setLoadDate(ro.getDateTime()));
						}
						combinations.add(options);
					} else if (rowOptions.getLoadDates().size() == 1) {
						bcr.getOptions().setLoadDate(rowOptions.getLoadDates().get(0).getDateTime());
					}
					if (rowOptions.getDischargeDates().size() > 1) {
						final List<Runnable> options = new LinkedList<>();
						for (final LocalDateTimeHolder ro : rowOptions.getDischargeDates()) {
							options.add(() -> bcr.getOptions().setDischargeDate(ro.getDateTime()));
						}
						combinations.add(options);
					} else if (rowOptions.getDischargeDates().size() == 1) {
						bcr.getOptions().setDischargeDate(rowOptions.getDischargeDates().get(0).getDateTime());
					}

				}

				templateBaseCase.getBaseCase().add(bcr);
			}

			final List<BaseCase> tasks = new LinkedList<>();
			recursiveTaskCreator(0, combinations, templateBaseCase, tasks);
			filterTasks(tasks);

			if (tasks.isEmpty()) {
				if (System.getProperty("lingo.suppress.dialogs") == null) {

					final Display display = PlatformUI.getWorkbench().getDisplay();
					if (display != null) {
						display.syncExec(() -> MessageDialog.openWarning(display.getActiveShell(), "No valid options found",
								String.format("Sandbox \"%s\": No valid combination of options found - most likely, specified options are incompatible.", model.getName())));
					}
				}
				skipRun = true;
			}
//tasks.add(0, model.getBaseCase());
			// ScheduleSpecification baseSpecification;
			if (model.getBaseCase().isKeepExistingScenario()) {
				final ExistingBaseCaseToScheduleSpecification builder = new ExistingBaseCaseToScheduleSpecification(originalScenarioDataProvider, mapper);
				// baseSpecification = builder.generate(model.getBaseCase());

				specifications = tasks.stream() //
						.map(baseCase -> {
							return new Pair<>(baseCase, builder.generate(baseCase, false));
						}) //
						.toList();

			} else {

				final BaseCaseToScheduleSpecification builder = new BaseCaseToScheduleSpecification(originalScenarioDataProvider.getTypedScenario(LNGScenarioModel.class), mapper);
				// baseSpecification = builder.generate(model.getBaseCase());

				specifications = tasks.stream() //
						.map(baseCase -> new Pair<>(baseCase, builder.generate(baseCase, false))) //
						.toList();
			}
		}
		helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());
	}

	private boolean checkSequenceSatifiesConstraints(final @NonNull LNGDataTransformer dataTransformer, final @NonNull ISequences rawSequences) {
		final ModifiableSequences emptySequences = new ModifiableSequences(new LinkedList<>());

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, emptySequences, emptySequences, dataTransformer.getHints());

		final Injector injector = evaluationTransformerUnit.getInjector();

		try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
			scope.enter();
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);

			final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

			return evaluationHelper.checkConstraintsForRelaxedConstraints(fullSequences, null);
		}
	}

	public IMultiStateResult runSandbox(final IProgressMonitor progressMonitor) {

		final List<String> hints = new LinkedList<>();
		if (model.isUseTargetPNL()) {
			hints.add(LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN);
		}

		progressMonitor.beginTask("Sandbox", specifications.size());
		if (skipRun) {
			progressMonitor.done();
			return null;
		}

		try {
			// Counter for solutions with constraint checker violations
			final AtomicInteger violationCount = new AtomicInteger();
			// Counter for solutions throwing infeasible exceptions.
			final AtomicInteger infeasibleCount = new AtomicInteger();

			// Evaluate all the partial cases / options. Note the base case is *NOT*
			// evaluated here.
			///////
			final List<ISequences> results = new LinkedList<>();
			helper.withRunner(scenarioInstance, userSettings, originalEditingDomain, hints, (bridge, injector, cores) -> {
				this.scenarioToOptimiserBridge = bridge;
				for (final Pair<BaseCase, ScheduleSpecification> p : specifications) {
					final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
					final ISequences base = transformer.createSequences(p.getSecond(), bridge.getDataTransformer(), false);

					// Check hard constraints are fine
					try {
						if (checkSequenceSatifiesConstraints(bridge.getDataTransformer(), base)) {
							results.add(base);
						} else {
							violationCount.incrementAndGet();
						}
					} catch (final InfeasibleSolutionException e) {
						// Ignore and continue.
						infeasibleCount.incrementAndGet();
					}

					progressMonitor.worked(1);
				}
			});
			if (results.isEmpty()) {
				if (System.getProperty("lingo.suppress.dialogs") == null) {

					final Display display = PlatformUI.getWorkbench().getDisplay();
					if (display != null) {
						String msg;
						if (infeasibleCount.get() > 0 && violationCount.get() == 0) {
							// Infeasible exceptions currently only for missing distance when a route is
							// specified.
							msg = String.format("Sandbox \"%s\": No solutions found - most likely due to missing distances for routes specified.", model.getName());
						} else {
							msg = String.format("Sandbox \"%s\": No solutions found - most likely due to constraint violations. Please check validation messages.", model.getName());
						}
						display.syncExec(() -> MessageDialog.openWarning(display.getActiveShell(), "No solutions found", msg));
					}
				}

			}
			if (results.isEmpty()) {
				return null;
			}

			// We need a non-null solution for the base, but it will be ignored and is not
			// really treated as the base. The base is handled separately during the export
			final ISequences base = results.get(0);
			final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.stream()//
					.map(s -> new NonNullPair<ISequences, Map<String, Object>>(s, new HashMap<>())) //
					.toList();
			return new MultiStateResult(new NonNullPair<>(base, new HashMap<>()), solutions);
		} finally {
			progressMonitor.done();
		}
	}

	private static void filterTasks(final List<BaseCase> tasks) {
		removeAllSpotBuySpotSellCargoes(tasks);
		final Set<BaseCase> duplicates = new HashSet<>();
		for (final BaseCase baseCase1 : tasks) {
			DUPLICATE_TEST: for (final BaseCase baseCase2 : tasks) {
				if (duplicates.contains(baseCase1) || duplicates.contains(baseCase2) || baseCase1 == baseCase2 || baseCase1.getBaseCase().size() != baseCase2.getBaseCase().size()) {
					continue;
				}
				for (int i = 0; i < baseCase1.getBaseCase().size(); i++) {
					final BaseCaseRow baseCase1Row = baseCase1.getBaseCase().get(i);
					final BaseCaseRow baseCase2Row = baseCase2.getBaseCase().get(i);

					// Check main attributes
					if (baseCase1Row.getBuyOption() != baseCase2Row.getBuyOption() //
							|| baseCase1Row.getSellOption() != baseCase2Row.getSellOption() //
							|| baseCase1Row.getVesselEventOption() != baseCase2Row.getVesselEventOption() //
							|| baseCase1Row.getShipping() != baseCase2Row.getShipping()) {
						continue DUPLICATE_TEST;
					}

					// Check optional extra options
					final BaseCaseRowOptions baseCase1RowOptions = baseCase1Row.getOptions();
					final BaseCaseRowOptions baseCase2RowOptions = baseCase2Row.getOptions();
					if (baseCase1RowOptions != null && baseCase2RowOptions != null) {

						if (baseCase1RowOptions.getLadenRoute() != baseCase2RowOptions.getLadenRoute() //
								|| baseCase1RowOptions.getBallastRoute() != baseCase2RowOptions.getBallastRoute() //
								|| baseCase1RowOptions.getLadenFuelChoice() != baseCase2RowOptions.getLadenFuelChoice() //
								|| baseCase1RowOptions.getBallastFuelChoice() != baseCase2RowOptions.getBallastFuelChoice() //
								|| !Objects.equals(baseCase1RowOptions.getLoadDate(), baseCase2RowOptions.getLoadDate())
								|| !Objects.equals(baseCase1RowOptions.getDischargeDate(), baseCase2RowOptions.getDischargeDate())

						) {
							continue DUPLICATE_TEST;
						}
					}

				}
				duplicates.add(baseCase2);
			}
		}
		tasks.removeAll(duplicates);

	}

	private static void removeAllSpotBuySpotSellCargoes(final List<BaseCase> tasks) {
		for (final BaseCase bc : tasks) {
			for (int i = 0; i < bc.getBaseCase().size(); i++) {
				final BaseCaseRow bcRow = bc.getBaseCase().get(i);
				if (bcRow.getBuyOption() instanceof BuyMarket && bcRow.getSellOption() instanceof SellMarket) {
					tasks.remove(bc);
					break;
				}

				if (bcRow.getBuyOption() != null && bcRow.getSellOption() == null) {
					if (AnalyticsBuilder.getDate(bcRow.getBuyOption()) == null || bcRow.getBuyOption() instanceof BuyMarket) {
						tasks.remove(bc);
						break;
					}
				}

				if (bcRow.getSellOption() != null && bcRow.getBuyOption() == null) {
					if (AnalyticsBuilder.getDate(bcRow.getSellOption()) == null || bcRow.getSellOption() instanceof SellMarket) {
						tasks.remove(bc);
						break;
					}
				}
			}
		}
	}

	private static void recursiveTaskCreator(final int listIdx, final List<List<Runnable>> combinations,
			final BaseCase templateBaseCase, final List<BaseCase> tasks) {
		if (listIdx == combinations.size()) {
			final BaseCase copy = EMFCopier.copy(templateBaseCase);

			final Set<Object> seenItems = new HashSet<>();
			final List<BaseCaseRow> data = new LinkedList<>(copy.getBaseCase());
			while (!data.isEmpty()) {
				final BaseCaseRow row = data.remove(0);
				if (row.getVesselEventOption() != null && !seenItems.add(row.getVesselEventOption())) {
					return;
				}
				if (row.getVesselEventOption() != null) {
					if (row.getBuyOption() != null && AnalyticsBuilder.getDate(row.getBuyOption()) != null) {
						final BaseCaseRow extra = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final BuyOption buyOption = row.getBuyOption();
						if (!(buyOption instanceof BuyMarket)) {
							extra.setBuyOption(buyOption);
							copy.getBaseCase().add(extra);
							data.add(extra);
						}
					}
					if (row.getSellOption() != null && AnalyticsBuilder.getDate(row.getSellOption()) != null) {
						final BaseCaseRow extra = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final SellOption sellOption = row.getSellOption();
						if (!(sellOption instanceof SellMarket)) {
							extra.setSellOption(sellOption);
							copy.getBaseCase().add(extra);
							data.add(extra);
						}
					}
					row.setBuyOption(null);
					row.setSellOption(null);
				}
				if (row.getBuyOption() != null && !seenItems.add(row.getBuyOption())) {
					return;
				}
				if (row.getSellOption() != null && !seenItems.add(row.getSellOption())) {
					return;
				}
				if ((row.getBuyOption() instanceof OpenBuy && row.getSellOption() instanceof OpenSell) || (row.getBuyOption() instanceof BuyMarket && row.getSellOption() instanceof SellMarket)) {
					return;
				}
				// Replace open options with null reference for eval code.
				if (row.getBuyOption() instanceof OpenBuy) {
					row.setBuyOption(null);
				}

				if (row.getSellOption() instanceof OpenSell) {
					row.setSellOption(null);
				}
			}
			filterShipping(copy);
			tasks.add(copy);
			return;
		}

		final List<Runnable> options = combinations.get(listIdx);
		for (final Runnable r : options) {
			r.run();
			recursiveTaskCreator(listIdx + 1, combinations, templateBaseCase, tasks);
		}
	}

	/**
	 * Set shipping to null if the row is for a non-shipped cargo
	 * 
	 * @param copy
	 */
	private static void filterShipping(final BaseCase copy) {
		for (final BaseCaseRow baseCaseRow : copy.getBaseCase()) {
			final BuyOption buyOption = baseCaseRow.getBuyOption();
			if (buyOption != null && AnalyticsBuilder.isDESPurchase().test(buyOption)) {
				baseCaseRow.setShipping(null);
			}
			final SellOption sellOption = baseCaseRow.getSellOption();
			if (sellOption != null && AnalyticsBuilder.isFOBSale().test(sellOption)) {
				baseCaseRow.setShipping(null);
			}
		}
	}

	public LNGScenarioToOptimiserBridge getBridge() {
		return scenarioToOptimiserBridge;
	}
}

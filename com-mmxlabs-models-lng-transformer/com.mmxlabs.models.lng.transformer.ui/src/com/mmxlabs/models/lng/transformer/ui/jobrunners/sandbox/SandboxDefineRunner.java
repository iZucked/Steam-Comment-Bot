/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Joiner;
import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.anon.AnonNameLookup;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowGroup;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowGroup;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.ScenarioMeta;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessSandboxJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessSandboxJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.common.ScenarioMetaUtils;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.errordialog.SandboxDefineErrorDialog;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.errordialog.SolutionErrorSet;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.exceptions.InfeasibleSolutionException;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.impl.SequencesAttributesProviderImpl;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.rcp.common.RunnerHelper;
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
			for (final PartialCaseRowGroup group : model.getPartialCase().getGroups()) {
				final BaseCaseRowGroup bcrg = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();
				for (final PartialCaseRow r : group.getRows()) {
					final BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
					bcr.setGroup(bcrg);
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
				templateBaseCase.getGroups().add(bcrg);
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

			final BaseCaseToScheduleSpecification builder = new BaseCaseToScheduleSpecification(originalScenarioDataProvider, mapper);
			specifications = tasks.stream() //
					.map(baseCase -> new Pair<>(baseCase, builder.generate(baseCase, model.getBaseCase().isKeepExistingScenario(), false))) //
					.toList();
		}
		helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());
	}

	private boolean checkSequenceSatifiesConstraints(final @NonNull LNGDataTransformer dataTransformer, final @NonNull ISequences rawSequences) {
		// Currently (at least) there is no need to use the rawSequence provider data
		final ModifiableSequences emptySequences = new ModifiableSequences(new LinkedList<>(), new SequencesAttributesProviderImpl());

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, emptySequences, emptySequences, dataTransformer.getHints());

		final Injector injector = evaluationTransformerUnit.getInjector();

		try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
			scope.enter();
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);

			final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

			return evaluationHelper.checkConstraintsForRelaxedConstraints(fullSequences, null, null);
		}
	}

	private List<@NonNull String> checkSequenceSatifiesConstraintsWithMessages(final @NonNull LNGDataTransformer dataTransformer, final @NonNull ISequences rawSequences) {
		// Currently (at least) there is no need to use the rawSequence provider data
		final ModifiableSequences emptySequences = new ModifiableSequences(new LinkedList<>(), new SequencesAttributesProviderImpl());

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, emptySequences, emptySequences, dataTransformer.getHints());

		final Injector injector = evaluationTransformerUnit.getInjector();

		try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
			scope.enter();
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);

			final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);
			final List<@NonNull String> messages = new LinkedList<>();
			evaluationHelper.checkConstraintsForRelaxedConstraints(fullSequences, null, messages);
			return messages;
		}
	}

	public IMultiStateResult runSandbox(final ScheduleSpecification startingPoint, final IProgressMonitor progressMonitor) {

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
			final List<SolutionErrorSet> errorInformation = new LinkedList<>();
			final Function<String, String> messageDecoder = input -> {
				try {
					final String jsonStr = AnonNameLookup.decodeStr(input);
					final JSONObject obj = (JSONObject) new JSONParser().parse(jsonStr);
					// TODO: Add API to enable de-anonymisation
					return obj.get("name").toString();
				} catch (final ParseException e) {
					// Give up and return the original string
					return input;
				}
			};
			final ISequences[] startingPointSequences = new ISequences[1];
			helper.withRunner(scenarioInstance, userSettings, originalEditingDomain, hints, (bridge, injector, cores) -> {
				this.scenarioToOptimiserBridge = bridge;
				{
					final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
					startingPointSequences[0] = transformer.createSequences(startingPoint, bridge.getDataTransformer(), false);

					// Check hard constraints are fine
					try {
						if (checkSequenceSatifiesConstraints(bridge.getDataTransformer(), startingPointSequences[0])) {
							// results.add(base);
						} else {
							// Solution failed, so collect error messages for display to the user
							final List<String> messages = checkSequenceSatifiesConstraintsWithMessages(bridge.getDataTransformer(), startingPointSequences[0]);
							if (!messages.isEmpty()) {
								errorInformation.add(new SolutionErrorSet("Fix scenario/starting point", messages.stream().map(s -> AnonNameLookup.decode(s, messageDecoder)).toList()));
							}
							// violationCount.incrementAndGet();
						}
					} catch (final InfeasibleSolutionException e) {
						// Ignore and continue.
						// infeasibleCount.incrementAndGet();
					}
				}
				for (final Pair<BaseCase, ScheduleSpecification> p : specifications) {
					final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
					final ISequences base = transformer.createSequences(p.getSecond(), bridge.getDataTransformer(), false);

					// Check hard constraints are fine
					try {
						if (checkSequenceSatifiesConstraints(bridge.getDataTransformer(), base)) {
							results.add(base);
						} else {
							// Solution failed, so collect error messages for display to the user
							final List<String> messages = checkSequenceSatifiesConstraintsWithMessages(bridge.getDataTransformer(), base);
							if (!messages.isEmpty()) {
								errorInformation.add(new SolutionErrorSet("Rejected solution", messages.stream().map(s -> AnonNameLookup.decode(s, messageDecoder)).toList()));
							}
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
							display.syncExec(() -> MessageDialog.openWarning(display.getActiveShell(), "No solutions found", msg));

						} else {

							// Combine the solution errors
							if (errorInformation.isEmpty()) {
								msg = String.format("Sandbox \"%s\": No solutions found - most likely due to constraint violations. Please check validation messages.", model.getName());
								display.syncExec(() -> MessageDialog.openWarning(display.getActiveShell(), "No solutions found", msg));
							} else {
								@SuppressWarnings("unused")
								final boolean ignoredResultNeededToCompile = RunnerHelper.syncExec(d -> {
									final SandboxDefineErrorDialog dialog = new SandboxDefineErrorDialog(d.getActiveShell(), errorInformation);
									dialog.open();
								});
							}
						}
					}
				}

			}
			if (results.isEmpty()) {
				return null;
			}

			// We need a non-null solution for the base, but it will be ignored and is not
			// really treated as the base. The base is handled separately during the export
			final ISequences base = startingPointSequences[0];
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

		{
			final Iterator<BaseCase> itr = tasks.iterator();
			TASK_LOOP: while (itr.hasNext()) {
				final BaseCase baseCase = itr.next();
				for (final var group : baseCase.getGroups()) {
					int numDischargesWithVolRange = 0;
					for (final BaseCaseRow r : group.getRows()) {
						if (r.getSellOption() != null) {
							// We don't care what the CV is here.
							final int[] sellVolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(1.0, r.getSellOption());
							if (sellVolumeInMMBTU == null || sellVolumeInMMBTU[0] != sellVolumeInMMBTU[1]) {
								numDischargesWithVolRange++;
							}
						}
					}
					if (numDischargesWithVolRange > 1) {
						itr.remove();
						continue TASK_LOOP;
					}
				}

			}
		}

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
		final List<BaseCase> tasksToRemove = new LinkedList<>();
		for (final BaseCase bc : tasks) {
			for (final BaseCaseRowGroup grp : bc.getGroups()) {
				boolean hasBuyMarket = false;

				for (final BaseCaseRow bcRow : grp.getRows()) {
					if (bcRow.getBuyOption() instanceof BuyMarket) {
						hasBuyMarket = true;
					}
					if (hasBuyMarket && bcRow.getSellOption() instanceof SellMarket) {
						tasksToRemove.add(bc);
						break;
					}
					// Filter open spot positions
					if (grp.getRows().size() == 1 && bcRow.getBuyOption() != null && bcRow.getSellOption() == null) {
						if (AnalyticsBuilder.getDate(bcRow.getBuyOption()) == null || bcRow.getBuyOption() instanceof BuyMarket) {
							tasksToRemove.add(bc);
							break;
						}
					}

					if (grp.getRows().size() == 1 && bcRow.getSellOption() != null && bcRow.getBuyOption() == null) {
						if (AnalyticsBuilder.getDate(bcRow.getSellOption()) == null || bcRow.getSellOption() instanceof SellMarket) {
							tasksToRemove.add(bc);
							break;
						}
					}
				}
			}
		}
		tasks.removeAll(tasksToRemove);
	}

	private static void recursiveTaskCreator(final int listIdx, final List<List<Runnable>> combinations, final BaseCase templateBaseCase, final List<BaseCase> tasks) {
		if (listIdx == combinations.size()) {
			final BaseCase copy = EMFCopier.copy(templateBaseCase);

			final Set<Object> seenItems = new HashSet<>();
			// May need to loop over groups?
			final List<BaseCaseRowGroup> data = new LinkedList<>(copy.getGroups());

			final List<BuyOption> openBuys = new LinkedList<>();
			final List<SellOption> openSells = new LinkedList<>();
			while (!data.isEmpty()) {
				final BaseCaseRowGroup grp = data.remove(0);
				boolean forceRowToOpen = false;
				// Copy of list as we can modify it
				for (final BaseCaseRow row : new LinkedList<>(grp.getRows())) {
					if (row.getVesselEventOption() != null && !seenItems.add(row.getVesselEventOption())) {
						return;
					}
					// LDD second row may not be covered here?
					if (forceRowToOpen || row.getVesselEventOption() != null) {
						forceRowToOpen = true;
						// Vessel with buy/sell options. This need to be treated as open, so split out some extra rows for the open options (unless they are spot market options).
						if (row.getBuyOption() != null && AnalyticsBuilder.getDate(row.getBuyOption()) != null) {
							final BuyOption buyOption = row.getBuyOption();
							if (!(buyOption instanceof BuyMarket)) {
								openBuys.add(buyOption);
							}
						}
						if (row.getSellOption() != null && AnalyticsBuilder.getDate(row.getSellOption()) != null) {
							final SellOption sellOption = row.getSellOption();
							if (!(sellOption instanceof SellMarket)) {
								openSells.add(sellOption);
							}
						}
						// Second LDD rows should be removed
						if (row.getVesselEventOption() == null) {
							row.setGroup(null);
							copy.getBaseCase().remove(row);
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
			}

			// Check options removed from event rows and see whether they have been used, or need to be marked as open
			for (final var buyOption : openBuys) {
				if (!seenItems.contains(buyOption)) {
					final BaseCaseRow extraOpenRow = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
					final BaseCaseRowGroup extraGroup = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();
					extraOpenRow.setBuyOption(buyOption);
					copy.getBaseCase().add(extraOpenRow);
					copy.getGroups().add(extraGroup);
					extraOpenRow.setGroup(extraGroup);
					data.add(extraGroup); // Add to list to avoid using the buy again
				}
			}
			for (final var sellOption : openSells) {
				if (!seenItems.contains(sellOption)) {
					final BaseCaseRow extraOpenRow = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
					final BaseCaseRowGroup extraGroup = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();
					extraOpenRow.setSellOption(sellOption);
					copy.getBaseCase().add(extraOpenRow);
					copy.getGroups().add(extraGroup);
					extraOpenRow.setGroup(extraGroup);
					data.add(extraGroup); // Add to list to avoid using the sell again
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

	public static Function<IProgressMonitor, AbstractSolutionSet> createSandboxJobFunction(final int threadsAvailable, final IScenarioDataProvider sdp,
			final @Nullable ScenarioInstance scenarioInstance, final SandboxSettings sandboxSettings, final OptionAnalysisModel model, @Nullable final Meta meta,
			@Nullable final Consumer<Object> registerLogging) {

		final HeadlessSandboxJSON json;
		if (registerLogging != null) {

			final HeadlessSandboxJSONTransformer transformer = new HeadlessSandboxJSONTransformer();
			json = transformer.createJSONResultObject(sandboxSettings);
			if (meta != null) {
				json.setMeta(meta);
			}
			json.getParams().setCores(threadsAvailable);
			// Should grab this from the task
			json.setType("sandbox:define");
			registerLogging.accept(json);
		} else {
			json = null;
		}

		final SandboxResult sandboxResult = AnalyticsFactory.eINSTANCE.createSandboxResult();
		sandboxResult.setUseScenarioBase(false);

		final boolean allowCaching = false;
		final UserSettings userSettings = sandboxSettings.getUserSettings();
		return SandboxRunnerUtil.createSandboxFunction(sdp, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final SandboxDefineRunner defineRunner = new SandboxDefineRunner(scenarioInstance, sdp, userSettings, mapper, model);

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return defineRunner.getBridge();
				}

				@Override
				public IMultiStateResult run(final ScheduleSpecification startingPoint, final IProgressMonitor monitor) {
					final long startTime = System.currentTimeMillis();
					try {
						return defineRunner.runSandbox(startingPoint, monitor);
					} finally {
						final long runTime = System.currentTimeMillis() - startTime;

						if (json != null) {
							json.getMetrics().setRuntime(runTime);

							final ScenarioMeta scenarioMeta = ScenarioMetaUtils.writeOptimisationMetrics( //
									defineRunner.getBridge().getOptimiserScenario(), //
									userSettings);

							json.setScenarioMeta(scenarioMeta);

						}
					}
				}
			};
		}, null, allowCaching);
	}
}

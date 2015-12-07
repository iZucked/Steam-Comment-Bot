/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.CopiedModelEntityMap;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.impl.MultiStateResult;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.period.CopiedScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.IScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker;
import com.mmxlabs.models.lng.transformer.period.PeriodExporter;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer;
import com.mmxlabs.models.lng.transformer.period.ScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IEvaluationContext;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.EvaluationContext;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.impl.ArbitraryStateLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;

public class LNGScenarioRunner {

	private static final int PROGRESS_OPTIMISATION = 100;
	private static final int PROGRESS_HILLCLIMBING_OPTIMISATION = 10;
	private static final int PROGRESS_ACTION_SET_OPTIMISATION = 20;
	private static final int PROGRESS_ACTION_SET_SAVE = 5;

	private static final Logger log = LoggerFactory.getLogger(LNGScenarioRunner.class);

	private final LNGScenarioModel originalScenario;
	private final EditingDomain originalEditingDomain;

	private LNGScenarioModel optimiserScenario;
	private EditingDomain optimiserEditingDomain;
	private final OptimiserSettings optimiserSettings;

	private IScenarioEntityMapping periodMapping;

	private ModelEntityMap modelEntityMap;
	private LocalSearchOptimiser optimiser;

	private IOptimisationContext context;
	private long startTimeMillis;

	private Injector injector = null;

	private LNGTransformer transformer;

	private Schedule initialSchedule;

	private Schedule finalSchedule;

	private final String[] hints;

	private boolean createOptimiser;

	private boolean doActionSetPostOptimisation;

	private Module extraModule;

	private EnumMap<ModuleType, List<Module>> localOverrides;

	private final ScenarioInstance scenarioInstance;

	private final boolean doHillClimb;

	private @Nullable IRunnerHook runnerHook;

	public LNGScenarioRunner(final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings, final String... hints) {
		this(scenario, null, optimiserSettings, createLocalEditingDomain(), hints);
	}

	public LNGScenarioRunner(final LNGScenarioModel scenario, @Nullable final ScenarioInstance scenarioInstance, final OptimiserSettings optimiserSettings, final EditingDomain editingDomain,
			final String... hints) {
		this.originalScenario = scenario;
		this.scenarioInstance = scenarioInstance;
		this.optimiserSettings = optimiserSettings;
		this.originalEditingDomain = editingDomain;
		this.hints = hints;
		if (hints != null) {
			for (final String hint : hints) {
				if (LNGTransformer.HINT_OPTIMISE_LSO.equals(hint)) {
					createOptimiser = true;
				}
			}
		}
		// Check for break down optimisation here.
		if (optimiserSettings.isBuildActionSets()) {
			if (LicenseFeatures.isPermitted("features:optimisation-actionset")) {
				doActionSetPostOptimisation = true;
			}
		}
		final boolean hillClimberPermitted = LicenseFeatures.isPermitted("features:optimisation-hillclimb");
		if (hillClimberPermitted) {
			doHillClimb = (this.optimiserSettings.getSolutionImprovementSettings() != null && this.optimiserSettings.getSolutionImprovementSettings().isImprovingSolutions());
		} else {
			doHillClimb = false;
		}

		optimiserScenario = originalScenario;
		optimiserEditingDomain = originalEditingDomain;
	}

	/**
	 * Convenience method to initialise the evaluation/optimiser system and evaluate the initial state. Calls {@link #init(IOptimiserProgressMonitor, Module, EnumMap)},and
	 * {@link #evaluateInitialState()};
	 */
	public void initAndEval() {
		init(null, null, null);
		evaluateInitialState();
	}

	/**
	 * Convenience method to initialise the evaluation/optimiser system and evaluate the initial state. Calls {@link #init(IOptimiserProgressMonitor,Module, EnumMap)}, and
	 * {@link #evaluateInitialState()} passing in the {@link Module} to {@link #init(IOptimiserProgressMonitor,Module, EnumMap)}.
	 */
	public void initAndEval(@Nullable final Module extraModule) {
		init(null, extraModule, null);

		evaluateInitialState();
	}

	/**
	 * Convenience method to initialise the evaluation/optimiser system and evaluate the initial state. Calls {@link #init(IOptimiserProgressMonitor,Module, EnumMap)}, and
	 * {@link #evaluateInitialState()} passing in the {@link Module} to {@link #init(IOptimiserProgressMonitor,Module, EnumMap)} and optionally overriding the number of iterations.
	 */
	public void initAndEval(@Nullable final Module extraModule, @Nullable final Integer iterations) {
		init(null, extraModule, null);

		// FIXME: Only for ITS!
		if (iterations != null && optimiser != null) {
			optimiser.setNumberOfIterations(iterations.intValue());
		}
		evaluateInitialState();
	}

	/**
	 * Initialise the evaluation/optimisation system. Prepare any period scenarios, creates the {@link LNGTransformer} and the optimiser (if an optimisation hint is specified) .
	 * 
	 */
	public void init(@Nullable final IOptimiserProgressMonitor monitor, @Nullable final Module extraModule, @Nullable final EnumMap<ModuleType, List<Module>> localOverrides) {

		this.extraModule = extraModule;
		this.localOverrides = localOverrides;
		if (createOptimiser) {
			initPeriodOptimisationData(optimiserSettings, extraModule);
		}
		transformer = new LNGTransformer(optimiserScenario, optimiserSettings, extraModule, localOverrides, hints);

		injector = transformer.getInjector();

		modelEntityMap = transformer.getModelEntityMap();
		if (createOptimiser) {
			context = transformer.getOptimisationContext();
			optimiser = transformer.getOptimiser();
		}

		if (optimiser != null) {
			if (monitor == null) {
				// because we are driving the optimiser ourself, so we can be paused, we
				// don't actually get progress callbacks.
				optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());
			} else {
				optimiser.setProgressMonitor(monitor);
			}

			optimiser.init();
		}
	}

	/**
	 * Evaluates the initial state
	 */
	@Nullable
	public Schedule evaluateInitialState() {
		startTimeMillis = System.currentTimeMillis();

		final IAnnotatedSolution startSolution;
		final IOptimisationContext pContext = this.context;
		if (createOptimiser) {
			// Pin variable for null analysis
			assert pContext != null;
			startSolution = optimiser.start(pContext, pContext.getInitialSequences());
		} else {
			startSolution = LNGSchedulerJobUtils.evaluateCurrentState(transformer);
		}

		final IRunnerHook pRunnerHook = runnerHook;
		if (pRunnerHook != null) {
			pRunnerHook.beginPhase(IRunnerHook.PHASE_INITIAL, transformer.getInjector());
			pRunnerHook.reportSequences(IRunnerHook.PHASE_INITIAL, transformer.getEvaluationContext().getInitialSequences());
		}

		initialSchedule = overwrite(0, transformer.getEvaluationContext().getInitialSequences(), LNGSchedulerJobUtils.extractOptimisationAnnotations(startSolution));
		final LocalSearchOptimiser pOptimiser = this.optimiser;
		if (pOptimiser != null) {
			try {
				pOptimiser.getProgressMonitor().begin(pOptimiser, pOptimiser.getFitnessEvaluator().getCurrentFitness(), startSolution);
			} catch (final Exception e) {

			} finally {
			}
		}

		if (pRunnerHook != null) {
			pRunnerHook.endPhase(IRunnerHook.PHASE_INITIAL);
		}
		return initialSchedule;
	}

	private void initPeriodOptimisationData(final OptimiserSettings optimiserSettings, @Nullable final Module extraModule) {
		{
			final OptimisationRange range = optimiserSettings.getRange();
			if (range != null) {
				if (range.getOptimiseAfter() != null || range.getOptimiseBefore() != null) {
					periodMapping = new ScenarioEntityMapping();
				}
			}
		}

		// Need period optimisation!
		if (periodMapping == null) {
			doActionSetPostOptimisation = false;
		}

		if (periodMapping != null) {

			final PeriodTransformer t = new PeriodTransformer(extraModule);
			t.setInclusionChecker(new InclusionChecker());

			optimiserScenario = t.transform(originalScenario, optimiserSettings, periodMapping);

			// DEBUGGING - store sub scenario as a "fork"
			if (false && scenarioInstance != null) {
				try {
					final IScenarioService scenarioService = scenarioInstance.getScenarioService();
					final ScenarioInstance dup = scenarioService.insert(scenarioInstance, EcoreUtil.copy(optimiserScenario));
					dup.setName("Period Scenario");

					// Copy across various bits of information
					dup.getMetadata().setContentType(scenarioInstance.getMetadata().getContentType());
					dup.getMetadata().setCreated(scenarioInstance.getMetadata().getCreated());
					dup.getMetadata().setLastModified(new Date());

					// Copy version context information
					dup.setVersionContext(scenarioInstance.getVersionContext());
					dup.setScenarioVersion(scenarioInstance.getScenarioVersion());

					dup.setClientVersionContext(scenarioInstance.getClientVersionContext());
					dup.setClientScenarioVersion(scenarioInstance.getClientScenarioVersion());
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			final BasicCommandStack commandStack = new BasicCommandStack();
			final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
			optimiserEditingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

			// Delete commands need a resource set on the editing domain
			final Resource r = new XMIResourceImpl();
			r.getContents().add(optimiserScenario);
			optimiserEditingDomain.getResourceSet().getResources().add(r);
		} else {
			optimiserScenario = originalScenario;
			optimiserEditingDomain = originalEditingDomain;
		}
	}

	/**
	 * Convenience method - calls #step(100). Only call if an optimisation hint has been specified
	 * 
	 * @return
	 */
	public IMultiStateResult run() {
		return runWithProgress(new NullProgressMonitor());
	}

	/**
	 * Export the current solution (and re-combine into original if period optimisation).
	 * 
	 * @param currentProgress
	 * @param solution
	 * @return
	 */
	private Schedule overwrite(final int currentProgress, @NonNull final ISequences rawSequences, @Nullable final Map<String, Object> extraAnnotations) {

		try {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(true);
			}

			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(currentProgress, rawSequences, optimiserScenario, optimiserEditingDomain, originalScenario, originalEditingDomain,
					transformer.getModelEntityMap(), /* originalDataTransformer.getModelEntityMap() , */ periodMapping, extraAnnotations);
			originalEditingDomain.getCommandStack().execute(commandPair.getFirst());
			return commandPair.getSecond();
		} finally {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(false);
			}
		}
	}

	/**
	 * Save the sequences in a complete copy of the scenario. Ensure the current scenario is in it's original state (excluding Schedule model and Parameters model changes) -- that is the data model
	 * still represents the initial solution..
	 * 
	 * @param rawSeqences
	 * @param extraAnnotations
	 * @return
	 */
	@NonNull
	private LNGScenarioModel exportAsCopy(@NonNull final ISequences rawSequences, @Nullable final Map<String, Object> extraAnnotations) {

		final EcoreUtil.Copier originalScenarioCopier = new EcoreUtil.Copier();
		final LNGScenarioModel targetOriginalScenario = (LNGScenarioModel) originalScenarioCopier.copy(originalScenario);
		originalScenarioCopier.copyReferences();

		assert targetOriginalScenario != null;
		final EditingDomain targetOriginalEditingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final IScenarioEntityMapping pPeriodMapping = periodMapping;
		if (pPeriodMapping != null) {
			final EcoreUtil.Copier optimiserScenarioCopier = new EcoreUtil.Copier();
			final LNGScenarioModel targetOptimiserScenario = (LNGScenarioModel) optimiserScenarioCopier.copy(optimiserScenario);
			optimiserScenarioCopier.copyReferences();

			assert targetOptimiserScenario != null;

			final CopiedModelEntityMap copiedOptimiserModelEntityMap = new CopiedModelEntityMap(transformer.getModelEntityMap(), optimiserScenarioCopier);
			// final CopiedModelEntityMap copiedOriginalModelEntityMap = new CopiedModelEntityMap(originalDataTransformer.getModelEntityMap(), originalScenarioCopier);

			final CopiedScenarioEntityMapping copiedPeriodMapping = new CopiedScenarioEntityMapping(pPeriodMapping, originalScenarioCopier, optimiserScenarioCopier);
			final EditingDomain targetOptimiserEditingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(100, rawSequences, targetOptimiserScenario, targetOptimiserEditingDomain, targetOriginalScenario,
					targetOriginalEditingDomain, copiedOptimiserModelEntityMap, copiedPeriodMapping, extraAnnotations);
			targetOriginalEditingDomain.getCommandStack().execute(commandPair.getFirst());

		} else {
			final CopiedModelEntityMap copiedModelEntityMap = new CopiedModelEntityMap(transformer.getModelEntityMap(), originalScenarioCopier);
			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(100, rawSequences, targetOriginalScenario, targetOriginalEditingDomain, targetOriginalScenario,
					targetOriginalEditingDomain, copiedModelEntityMap, null, extraAnnotations);
			targetOriginalEditingDomain.getCommandStack().execute(commandPair.getFirst());
		}

		return targetOriginalScenario;

	}

	/**
	 * Returns a Pair containing a command and a null schedule. Once the Command has been executed, the Schedule will be populated.
	 * 
	 * @param currentProgress
	 * @param solution
	 * @return
	 */
	// private Pair<Command, Schedule> creatExportScheduleCommand(final int currentProgress, @Nullable final IAnnotatedSolution solution) {

	private Pair<Command, Schedule> creatExportScheduleCommand(final int currentProgress, @NonNull final ISequences rawSeqeuences, @NonNull final LNGScenarioModel targetOptimiserScenario,
			@NonNull final EditingDomain targetOptimiserEditingDomain, @NonNull final LNGScenarioModel targetOriginalScenario, @NonNull final EditingDomain targetOriginalEditingDomain,
			@NonNull final ModelEntityMap optimiserModelEntityMap, /* @NonNull final ModelEntityMap originalModelEntityMap, */
			@Nullable final IScenarioEntityMapping periodMapping, @Nullable final Map<String, Object> extraAnnotations) {

		// Create a "wrapper" to set the user friendly command name for the undo menu
		final CompoundCommand wrapper = LNGSchedulerJobUtils.createBlankCommand(currentProgress);
		// Create a reference to be able to grab the Schedule object from the command state.
		final Pair<Command, Schedule> p = new Pair<>();
		// Create a custom compound command to create and execute changes as they are created during the command to allow us to have a single command in the history rather than several.
		final CompoundCommand c = new CompoundCommand() {
			@Override
			protected boolean prepare() {
				// Always prepared
				return true;
			}

			@Override
			public boolean canExecute() {
				// Always true
				return true;
			}

			@Override
			public void execute() {

				if (periodMapping == null) {

					// This either applies to the real scenario or the period copy if present.
					final Pair<Command, Schedule> updateCommand = LNGSchedulerJobUtils.exportSolution(injector, targetOptimiserScenario, optimiserSettings, targetOptimiserEditingDomain,
							optimiserModelEntityMap, rawSeqeuences, extraAnnotations);
					// Apply to real scenario
					appendAndExecute(updateCommand.getFirst());
					p.setSecond(updateCommand.getSecond());

				} else {
					// This either applies to the real scenario or the period copy if present.
					final Pair<Command, Schedule> updateCommand = LNGSchedulerJobUtils.exportSolution(injector, targetOptimiserScenario, optimiserSettings, targetOptimiserEditingDomain,
							optimiserModelEntityMap, rawSeqeuences, extraAnnotations);

					// Period optimisation code path, apply the dual commands to the real scenario.

					// Execute the update command on the period scenario
					targetOptimiserEditingDomain.getCommandStack().execute(updateCommand.getFirst());

					// Export the period scenario changes into the original scenario
					final PeriodExporter e = new PeriodExporter();
					final CompoundCommand part1 = LNGSchedulerJobUtils.createBlankCommand(currentProgress);
					part1.append(e.updateOriginal(targetOriginalEditingDomain, targetOriginalScenario, targetOptimiserScenario, periodMapping));
					if (part1.canExecute()) {
						appendAndExecute(part1);
					} else {
						throw new RuntimeException("Unable to execute period optimisation merge command");
					}

					// Re-evaluate command
					{
						final OptimiserSettings evalSettings = EcoreUtil.copy(transformer.getOptimiserSettings());
						// final OptimiserSettings evalSettings = EcoreUtil.copy(optimiserSettings);
						evalSettings.getRange().unsetOptimiseAfter();
						evalSettings.getRange().unsetOptimiseBefore();
						final LNGTransformer subTransformer = new LNGTransformer(targetOriginalScenario, evalSettings, extraModule, localOverrides);

						final ModelEntityMap subModelEntityMap = subTransformer.getModelEntityMap();
						final IAnnotatedSolution finalSolution = LNGSchedulerJobUtils.evaluateCurrentState(subTransformer);

						// Copy extra annotations - e.g. fitness information
						if (extraAnnotations != null) {
							extraAnnotations.entrySet().forEach(t -> finalSolution.setGeneralAnnotation(t.getKey(), t.getValue()));
						}

						final Pair<Command, Schedule> part2 = LNGSchedulerJobUtils.exportSolution(subTransformer.getInjector(), targetOriginalScenario, EcoreUtil.copy(optimiserSettings),
								targetOriginalEditingDomain, subModelEntityMap, finalSolution);
						if (part2.getFirst().canExecute()) {
							appendAndExecute(part2.getFirst());
						} else {
							throw new RuntimeException("Unable to execute period optimisation update command");
						}

						// Store reference to the final schedule model
						p.setSecond(part2.getSecond());
					}
				}
			}
		};

		wrapper.append(c);
		p.setFirst(wrapper);
		return p;

	}

	public void dispose() {

		if (this.modelEntityMap != null) {
			this.modelEntityMap = null;
		}

		this.optimiser = null;

		this.injector = null;
	}

	@Nullable
	public final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	@Nullable
	public final Schedule getIntialSchedule() {
		return initialSchedule;
	}

	public boolean isFinished() {
		return optimiser == null || optimiser.isFinished();
	}

	@Nullable
	private Injector getInjector() {
		return injector;
	}

	@NonNull
	public LNGScenarioModel getScenario() {
		return originalScenario;
	}

	@NonNull
	public static EditingDomain createLocalEditingDomain() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
		return ed;
	}

	@NonNull
	public static OptimiserSettings createDefaultSettings() {
		final OptimiserSettings optimiserSettings = ScenarioUtils.createDefaultSettings();
		assert optimiserSettings != null;

		return createExtendedSettings(optimiserSettings);
	}

	/**
	 * Use the {@link IParameterModesRegistry} to extend the existing settings object.
	 * 
	 * @param optimiserSettings
	 * @return
	 */
	@NonNull
	public static OptimiserSettings createExtendedSettings(@NonNull final OptimiserSettings optimiserSettings) {
		IParameterModesRegistry parameterModesRegistry = null;

		final Activator activator = Activator.getDefault();
		if (activator != null) {
			parameterModesRegistry = activator.getParameterModesRegistry();
		}

		if (parameterModesRegistry != null) {
			final Collection<IParameterModeExtender> extenders = parameterModesRegistry.getExtenders();
			if (extenders != null) {
				for (final IParameterModeExtender extender : extenders) {
					extender.extend(optimiserSettings, null);
				}
			}
		}
		return optimiserSettings;
	}

	@NonNull
	private IMultiStateResult storeBreakdownSolutionsAsForks(final List<NonNullPair<ISequences, Map<String, Object>>> breakdownSolution, final boolean keepFinalResult,
			@NonNull final IProgressMonitor progressMonitor) {

		// Assuming the scenario data is at the initial state.

		// Remove existing solutions
		if (scenarioInstance != null) {
			final List<Container> elementsToRemove = new LinkedList<>();
			for (final Container c : scenarioInstance.getElements()) {
				if (c.getName().startsWith("ActionSet-")) {
					elementsToRemove.add(c);
				}
			}
			for (final Container c : elementsToRemove) {
				scenarioInstance.getScenarioService().delete(c);
			}
		}
		final List<NonNullPair<ISequences, Map<String, Object>>> annotatedSolutions = new ArrayList<>(breakdownSolution.size());
		progressMonitor.beginTask("Saving action sets", breakdownSolution.size());
		try {
			int changeSetIdx = 0;
			for (final NonNullPair<ISequences, Map<String, Object>> changeSet : breakdownSolution) {

				/**
				 * Start the full evaluation process.
				 */

				// Get or derive the full sequences from the changeset
				final IModifiableSequences sequences = new ModifiableSequences(changeSet.getFirst());

				// Perform a full evaluation
				final EvaluationState state = new EvaluationState();
				final EvaluationProcessRegistry evaluationProcessRegistry = new EvaluationProcessRegistry();
				final IEvaluationContext evaluationContext = new EvaluationContext(this.context.getOptimisationData(), sequences, Collections.<String> emptyList(), evaluationProcessRegistry);

				final AnnotatedSolution solution = new AnnotatedSolution(sequences, evaluationContext, state);
				final IEvaluationProcess process = injector.getInstance(SchedulerEvaluationProcess.class);
				process.annotate(sequences, state, solution);

				// Export the solution onto the scenario
				final LNGScenarioModel copy = exportAsCopy(changeSet.getFirst(), changeSet.getSecond());

				annotatedSolutions.add(new NonNullPair<>(sequences, changeSet.getSecond()));

				// Save the scenario as a fork.
				if (scenarioInstance != null) {
					try {
						final IScenarioService scenarioService = scenarioInstance.getScenarioService();

						final ScenarioInstance dup = scenarioService.insert(scenarioInstance, copy);
						if (changeSetIdx == 0) {
							dup.setName("ActionSet-base");
							changeSetIdx++;
						} else {
							dup.setName(String.format("ActionSet-%s", (changeSetIdx++)));
						}

						// Copy across various bits of information
						dup.getMetadata().setContentType(scenarioInstance.getMetadata().getContentType());
						dup.getMetadata().setCreated(scenarioInstance.getMetadata().getCreated());
						dup.getMetadata().setLastModified(new Date());

						// Copy version context information
						dup.setVersionContext(scenarioInstance.getVersionContext());
						dup.setScenarioVersion(scenarioInstance.getScenarioVersion());

						dup.setClientVersionContext(scenarioInstance.getClientVersionContext());
						dup.setClientScenarioVersion(scenarioInstance.getClientScenarioVersion());

						// dup.setHidden(true);
					} catch (final Exception e) {
						log.error("Unable to store changeset scenario: " + e.getMessage(), e);
					}
				}
				//
				// // If we are keeping the best result, then update the field and do not execute the undo commands
				if (keepFinalResult && breakdownSolution.get(breakdownSolution.size() - 1) == changeSet) {
					this.finalSchedule = copy.getScheduleModel().getSchedule();
				}
				// // Reset state
				// if (periodMapping != null) {
				// LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, 100);
				// LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
				// } else {
				// LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
				// }
				// }

				progressMonitor.worked(1);
			}
			return new MultiStateResult(annotatedSolutions.get(annotatedSolutions.size() - 1), annotatedSolutions);
		} finally {
			progressMonitor.done();
		}
	}

	/**
	 * used by {@link AbstractEclipseJobControl} / {@link LNGSchedulerOptimiserJobControl}
	 * 
	 * @param progressMonitor
	 */
	public IMultiStateResult runWithProgress(final @NonNull IProgressMonitor progressMonitor) {
		assert createOptimiser;

		final int totalWork = (PROGRESS_OPTIMISATION) + (doHillClimb ? PROGRESS_HILLCLIMBING_OPTIMISATION : 0)
				+ (doActionSetPostOptimisation ? PROGRESS_ACTION_SET_OPTIMISATION + PROGRESS_ACTION_SET_SAVE : 0);
		progressMonitor.beginTask("", totalWork);

		IMultiStateResult bestResult = null;

		final IRunnerHook pRunnerHook = runnerHook;
		try {
			// Main Optimisation Loop
			{

				bestResult = performPhase(IRunnerHook.PHASE_LSO, progressMonitor, PROGRESS_OPTIMISATION, null);
				optimiser = null;

				assert bestResult != null;
				if (doHillClimb) {
					bestResult = performPhase(IRunnerHook.PHASE_HILL, progressMonitor, PROGRESS_HILLCLIMBING_OPTIMISATION, bestResult);

					assert bestResult != null;
					assert bestResult.getBestSolution() != null;
					assert bestResult.getBestSolution().getFirst() != null;
					assert bestResult.getBestSolution().getSecond() != null;
				}
			}

			// Clear any previous optimisation state prior to export
			if (periodMapping != null) {
				LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, 100);
			}
			LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);

			if (doActionSetPostOptimisation) {
				if (pRunnerHook != null) {
					pRunnerHook.beginPhase(IRunnerHook.PHASE_ACTION_SETS, getInjector());
				}
				if (bestResult == null) {
					log.error("No existing state - unable to find action sets");
				} else {

					boolean exportOptimiserSolution = true;
					// Generate the changesets decomposition.
					// Run optimisation

					final BagOptimiser instance = injector.getInstance(BagOptimiser.class);
					final boolean foundBetterResult = instance.optimise(bestResult.getBestSolution().getFirst(), new SubProgressMonitor(progressMonitor, PROGRESS_ACTION_SET_OPTIMISATION), 1000);

					// Store the results
					final List<NonNullPair<ISequences, Map<String, Object>>> breakdownSolution = instance.getBestSolution();
					if (breakdownSolution != null) {
						bestResult = storeBreakdownSolutionsAsForks(breakdownSolution, foundBetterResult, new SubProgressMonitor(progressMonitor, PROGRESS_ACTION_SET_SAVE));
						// exportOptimiserSolution = !foundBetterResult;
					}
					// The breakdown optimiser may find a better solution. This will be saved in storeBreakdownSolutionsAsForks
					// if (exportOptimiserSolution) {
					// export final state
					finalSchedule = overwrite(100, bestResult.getBestSolution().getFirst(), bestResult.getBestSolution().getSecond());
					// }

					if (pRunnerHook != null) {
						// List<ISequences> actionSetRawSequences = bestResult.getSolutions().stream().map(t -> t.getFirst()).collect(Collectors.toList());
						pRunnerHook.reportSequences(IRunnerHook.PHASE_ACTION_SETS, bestResult.getBestSolution().getFirst() /* , actionSetRawSequences */);
					}
				}
				if (pRunnerHook != null) {
					pRunnerHook.endPhase(IRunnerHook.PHASE_ACTION_SETS);
				}
			} else {
				assert bestResult != null;
				finalSchedule = overwrite(100, bestResult.getBestSolution().getFirst(), bestResult.getBestSolution().getSecond());
			}
		} finally {
			progressMonitor.done();
		}
		log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
		return bestResult;
	}

	private IMultiStateResult performSolutionImprovement(final IProgressMonitor progressMonitor, final ISequences bestRawSequences) throws OperationCanceledException {

		if (this.optimiserSettings.getSolutionImprovementSettings() != null && this.optimiserSettings.getSolutionImprovementSettings().isImprovingSolutions()) {
			final ArbitraryStateLocalSearchOptimiser hillClimber = injector.getInstance(ArbitraryStateLocalSearchOptimiser.class);
			// The optimiser may not have a best sequence set
			log.debug("Performing hill climbing...");
			final ISequences sequenceToImprove = bestRawSequences == null ? context.getInitialSequences() : bestRawSequences;
			hillClimber.start(context, sequenceToImprove);
			int hillClimberWork = 0;
			while (!hillClimber.isFinished()) {
				if (progressMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				hillClimber.step(1);
				if (++hillClimberWork % PROGRESS_HILLCLIMBING_OPTIMISATION == 0) {
					progressMonitor.worked(1);
				}
			}
			if (hillClimber.getBestRawSequences() != null) {
				return new MultiStateResult(hillClimber.getBestRawSequences(), LNGSchedulerJobUtils.extractOptimisationAnnotations(hillClimber.getBestSolution()));
			}
		}
		return null;
	}

	private IMultiStateResult performLSOOptimisation(final LocalSearchOptimiser lsoOptimiser, final IProgressMonitor progressMonitor/* , final ISequences bestRawSequences */)
			throws OperationCanceledException {

		while (!lsoOptimiser.isFinished()) {
			lsoOptimiser.step(1);
			if (progressMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			progressMonitor.worked(1);
		}
		assert lsoOptimiser.isFinished();

		if (lsoOptimiser.isFinished()) {

			if (lsoOptimiser.getBestRawSequences() != null) {
				return new MultiStateResult(lsoOptimiser.getBestRawSequences(), LNGSchedulerJobUtils.extractOptimisationAnnotations(lsoOptimiser.getBestSolution()));
			}
		}
		return null;
	}

	public IRunnerHook getRunnerHook() {
		return runnerHook;
	}

	public void setRunnerHook(final IRunnerHook runnerHook) {
		this.runnerHook = runnerHook;
	}

	private IMultiStateResult performPhase(final String phase, final IProgressMonitor progressMonitor, final int phaseTicks, IMultiStateResult bestResult) {
		final IRunnerHook pRunnerHook = runnerHook;

		ISequences preloadedResult = null;
		if (pRunnerHook != null) {
			pRunnerHook.beginPhase(phase, getInjector());
		}
		if (pRunnerHook != null) {
			preloadedResult = pRunnerHook.getPrestoredSequences(phase);
		}
		if (preloadedResult == null) {
			switch (phase) {
			case IRunnerHook.PHASE_LSO:
				bestResult = performLSOOptimisation(optimiser, progressMonitor /* ,bestResult.getBestSolution().getFirst() */);
				break;
			case IRunnerHook.PHASE_HILL:
				bestResult = performSolutionImprovement(progressMonitor, bestResult.getBestSolution().getFirst());
				break;
			default:
				assert false;
			}

			assert bestResult != null;
			assert bestResult.getBestSolution() != null;
			assert bestResult.getBestSolution().getFirst() != null;
			assert bestResult.getBestSolution().getSecond() != null;

			if (pRunnerHook != null) {
				pRunnerHook.reportSequences(phase, bestResult.getBestSolution().getFirst());
			}
		} else {
			bestResult = new MultiStateResult(preloadedResult, new HashMap<>());
			preloadedResult = null;
		}
		if (pRunnerHook != null) {
			pRunnerHook.endPhase(phase);
		}
		return bestResult;
	}
}

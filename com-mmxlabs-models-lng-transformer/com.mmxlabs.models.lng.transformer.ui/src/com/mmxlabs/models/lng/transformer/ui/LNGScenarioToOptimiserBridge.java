/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.period.IScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker;
import com.mmxlabs.models.lng.transformer.period.PeriodExporter;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer;
import com.mmxlabs.models.lng.transformer.period.ScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * The {@link LNGScenarioToOptimiserBridge} creates and maintains the mapping between the original {@link LNGScenarioModel} and the optimiser data structures to allow saving of the results. This class
 * also handles the Period Optimisation transformation. Call {@link #overwrite(int, ISequences, Map)} to save into the current {@link ScenarioInstance}. Call
 * {@link #storeAsCopy(ISequences, String, Container, Map)} to save the result in a copy of the {@link ScenarioInstance}
 * 
 * 
 * @author Simon Goodall
 *
 */
public class LNGScenarioToOptimiserBridge {

	private static final Logger log = LoggerFactory.getLogger(LNGScenarioToOptimiserBridge.class);

	@NonNull
	private final LNGScenarioModel originalScenario;

	@NonNull
	private final EditingDomain originalEditingDomain;

	@NonNull
	private LNGScenarioModel optimiserScenario;

	@NonNull
	private EditingDomain optimiserEditingDomain;

	@Nullable
	private final IScenarioEntityMapping periodMapping;

	@NonNull
	private final Collection<@NonNull String> hints;

	@Nullable
	private final ScenarioInstance scenarioInstance;

	@NonNull
	private LNGDataTransformer optimiserDataTransformer;

	@NonNull
	private final LNGDataTransformer originalDataTransformer;

	// Flag to check we can still export as copy. calls to #overwrite can invalidate the state
	private boolean canExportAsCopy = true;
	/**
	 * Integer to count "undo" state of the scenario. Each overwrite evaluation should increment this value. Calls to {@link LNGSchedulerJobUtils#undoPreviousOptimsationStep(EditingDomain, int)}
	 * should decrement it. Ideally we would track the current state of the command stack, but the API does not appear to permit this (maybe a command stack listener? - can we tell if a command is
	 * undo/redo?).
	 */
	private int overwriteCommandStackCounter = 0;

	private @NonNull final UserSettings userSettings;

	private @NonNull final SolutionBuilderSettings solutionBuilderSettings;

	private PeriodExporter periodExporter;

	public LNGScenarioToOptimiserBridge(@NonNull final LNGScenarioModel scenario, @Nullable final ScenarioInstance scenarioInstance, @NonNull final UserSettings userSettings,
			@NonNull final SolutionBuilderSettings solutionBuilderSettings, @NonNull final EditingDomain editingDomain, @Nullable final Module bootstrapModule,
			@Nullable final IOptimiserInjectorService localOverrides, final boolean evaluationOnly, final @NonNull String @Nullable... initialHints) {
		this.originalScenario = scenario;
		this.scenarioInstance = scenarioInstance;
		this.userSettings = userSettings;
		this.solutionBuilderSettings = solutionBuilderSettings;
		this.originalEditingDomain = editingDomain;
		this.hints = LNGTransformerHelper.getHints(userSettings, initialHints);

		final Collection<@NonNull IOptimiserInjectorService> services = LNGTransformerHelper.getOptimiserInjectorServices(bootstrapModule, localOverrides);

		originalDataTransformer = new LNGDataTransformer(this.originalScenario, userSettings, solutionBuilderSettings, hints, services);

		// TODO: These ideally should be final, but #overwrite currently needs these variables set.
		this.optimiserEditingDomain = originalEditingDomain;
		this.optimiserDataTransformer = originalDataTransformer;
		this.optimiserScenario = originalScenario;

		// Trigger initial evaluation - note no fitness state is saved
		overwrite(0, originalDataTransformer.getInitialSequences(), null);

		if (!evaluationOnly) {
			final Triple<@NonNull LNGScenarioModel, @NonNull EditingDomain, @Nullable IScenarioEntityMapping> t = initPeriodOptimisationData(scenarioInstance, originalScenario, originalEditingDomain,
					userSettings);

			// TODO: Replaces the above with that return in the triple (this could be original or optimiser)
			this.optimiserScenario = t.getFirst();
			this.optimiserEditingDomain = t.getSecond();
			this.periodMapping = t.getThird();

		} else {
			this.periodMapping = null;
		}
		// If we are in a period optimisation, then create a LNGDataTransformer for the period data
		if (this.periodMapping != null) {

			// Create a stub command here so match the initial evaluation command update for undo() calls later
			final CompoundCommand wrapper = LNGSchedulerJobUtils.createBlankCommand(0);
			wrapper.append(IdentityCommand.INSTANCE);
			optimiserEditingDomain.getCommandStack().execute(wrapper);

			optimiserDataTransformer = new LNGDataTransformer(this.optimiserScenario, userSettings, solutionBuilderSettings, hints, services);

			periodExporter = new PeriodExporter(originalDataTransformer, optimiserDataTransformer, periodMapping);
		} else {
			optimiserDataTransformer = originalDataTransformer;
		}

		// Reset flag as calls to #overwrite will have set this to false, but these should not have invalidated the internal state
		canExportAsCopy = true;
	}

	@NonNull
	private static Triple<@NonNull LNGScenarioModel, @NonNull EditingDomain, @Nullable IScenarioEntityMapping> initPeriodOptimisationData(@Nullable final ScenarioInstance scenarioInstance,
			@NonNull final LNGScenarioModel originalScenario, @NonNull final EditingDomain originalEditingDomain, @NonNull final UserSettings userSettings) {

		IScenarioEntityMapping periodMapping = null;
		{
			if (userSettings.getPeriodStart() != null || userSettings.getPeriodEnd() != null) {
				periodMapping = new ScenarioEntityMapping();
			}
		}

		if (periodMapping != null) {

			final PeriodTransformer t = new PeriodTransformer();
			t.setInclusionChecker(new InclusionChecker());

			final NonNullPair<LNGScenarioModel, EditingDomain> p = t.transform(originalScenario, userSettings, periodMapping);

			// DEBUGGING - store sub scenario as a "fork"
			if (scenarioInstance != null) {
				@NonNull
				final ScenarioInstance pScenarioInstance = scenarioInstance;
				if (false) {
					try {
						assert scenarioInstance != null;
						LNGScenarioRunnerUtils.saveScenarioAsChild(pScenarioInstance, pScenarioInstance, p.getFirst(), "Period Scenario");
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
			return new Triple<>(p.getFirst(), p.getSecond(), periodMapping);
		} else {
			return new Triple<>(originalScenario, originalEditingDomain, null);
		}

	}

	private Command previousOverwriteCommand = null;

	public Schedule overwrite(final int currentProgress, @NonNull final ISequences rawSeqences, @Nullable final Map<String, Object> extraAnnotations) {
		if (previousOverwriteCommand != null) {
			assert originalEditingDomain.getCommandStack().getUndoCommand() == previousOverwriteCommand;
			if (!LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, currentProgress, true)) {
				assert false;
			}
			previousOverwriteCommand = null;
			--overwriteCommandStackCounter;
		}

		assert overwriteCommandStackCounter == 0;

		final Schedule schedule = createSchedule(rawSeqences, extraAnnotations);
		assert schedule != null;

		try {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(true);
			}

			final Command cmd = LNGSchedulerJobUtils.exportSchedule(originalDataTransformer.getInjector(), originalScenario, originalEditingDomain, schedule);
			assert cmd != null;
			originalEditingDomain.getCommandStack().execute(cmd);
			previousOverwriteCommand = cmd;
			++overwriteCommandStackCounter;

			canExportAsCopy = currentProgress == 0;

			return schedule;
		} finally {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(false);
			}
		}
	}

	/**
	 * Save the sequences in a complete copy of the scenario. *Ensure* the current scenario is in it's original state (excluding Schedule model and Parameters model changes) -- that is the data model
	 * still represents the initial solution..
	 *
	 * @param rawSequences
	 * @param extraAnnotations
	 * @return
	 */
	@NonNull
	public LNGScenarioModel exportAsCopy(@NonNull final ISequences rawOptimiserSequences, @Nullable final Map<String, Object> extraAnnotations) {

		if (!canExportAsCopy) {
			throw new IllegalStateException("Unable to export copy - already overwritten data");
		}

			// Alt code path to avoid second transform.
			final Schedule child_schedule = createSchedule(rawOptimiserSequences, extraAnnotations);

			// Gather uncontained slots to manually copy
			final Set<Slot> extraSlots = new HashSet<>();

			// New spot slots etc will need to be contained here.
			for (final SlotAllocation a : child_schedule.getSlotAllocations()) {
				final Slot slot = a.getSlot();
				if (slot != null && slot.eContainer() == null) {
					extraSlots.add(slot);
				}
			}

			for (final CargoAllocation ca : child_schedule.getCargoAllocations()) {

				for (final SlotAllocation a : ca.getSlotAllocations()) {
					final Slot slot = a.getSlot();
					if (slot != null && slot.eContainer() == null) {
						extraSlots.add(slot);
					}
				}
			}
			for (final OpenSlotAllocation ca : child_schedule.getOpenSlotAllocations()) {
				final Slot slot = ca.getSlot();
				if (slot != null && slot.eContainer() == null) {
					assert false;
				}
			}

			final EcoreUtil.Copier copier = new EcoreUtil.Copier();
			// Copy base
			final LNGScenarioModel copiedOriginalScenario = (LNGScenarioModel) copier.copy(originalScenario);
			// Copy uncontained slots before the schedule
			extraSlots.forEach(s -> copier.copy(s));
			// Finally copy the schedule
			final Schedule copiedSchedule = (Schedule) copier.copy(child_schedule);

			copier.copyReferences();

			// Construct internal command stack to generate correct output schedule
			final EditingDomain ed = LNGSchedulerJobUtils.createLocalEditingDomain();

			// This injector is only needed for post-export handlers
			final Command cmd = LNGSchedulerJobUtils.exportSchedule(originalDataTransformer.getInjector(), copiedOriginalScenario, ed, copiedSchedule);
			assert cmd != null;
			ed.getCommandStack().execute(cmd);

			return copiedOriginalScenario;
	}
 

	private ISequences getTransformedOriginalRawSequences(final ISequences rawSequences) {
		// TODO: Wrap this in the period exporter
		if (periodExporter == null) {
			return rawSequences;
		}
		return periodExporter.transform(rawSequences);
	}

	@NonNull
	public Injector getInjector() {
		return optimiserDataTransformer.getInjector();
	}

	@NonNull
	public LNGScenarioModel getScenario() {
		return originalScenario;
	}

	public ScenarioInstance storeAsCopy(@NonNull final ISequences rawSequences, @NonNull final String newName, @NonNull final Container parent, @Nullable final Map<String, Object> extraAnnotations) {

		final ScenarioInstance pScenarioInstance = scenarioInstance;
		if (pScenarioInstance == null) {
			throw new NullPointerException();
		}

		// Export the solution onto the scenario
		final LNGScenarioModel copy = exportAsCopy(rawSequences, extraAnnotations);

		// Save the scenario as a fork.
		try {
			return LNGScenarioRunnerUtils.saveNewScenario(pScenarioInstance, parent, copy, newName);
		} catch (final Exception e) {
			throw new RuntimeException("Unable to store changeset scenario: " + e.getMessage(), e);
		} finally {

		}
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return optimiserDataTransformer;
	}

	/**
	 * Returns the scenario used by the optimisation. This method is intended for use in test cases rather than the main application.
	 * 
	 * @return
	 */
	public LNGScenarioModel getOptimiserScenario() {
		return optimiserScenario;
	}

	/**
	 * Returns a {@link Schedule} for the *optimiser* data
	 * 
	 * @param rawSequences
	 * @param extraAnnotations
	 */
	@NonNull
	public Schedule createSchedule(@NonNull final ISequences rawOptimiserSequences, @Nullable final Map<String, Object> extraAnnotations) {
		final ISequences rawSequences = getTransformedOriginalRawSequences(rawOptimiserSequences);

		final Injector injector;
		{
			final Collection<@NonNull IOptimiserInjectorService> services = originalDataTransformer.getModuleServices();
			final List<Module> modules = new LinkedList<>();
			modules.add(new InitialSequencesModule(originalDataTransformer.getInitialSequences()));
			modules.add(new InputSequencesModule(rawSequences));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(
					new LNGParameters_EvaluationSettingsModule(originalDataTransformer.getUserSettings(), originalDataTransformer.getSolutionBuilderSettings().getConstraintAndFitnessSettings()),
					services, IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
			injector = originalDataTransformer.getInjector().createChildInjector(modules);
		}

		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		{
			final Injector childInjector = injector.createChildInjector(new ExporterExtensionsModule());
			childInjector.injectMembers(exporter);
		}
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);
			final IAnnotatedSolution solution = LNGSchedulerJobUtils.evaluateCurrentState(injector, optimisationData, rawSequences).getFirst();

			// Copy extra annotations - e.g. fitness information
			if (extraAnnotations != null) {
				extraAnnotations.entrySet().forEach(e -> solution.setGeneralAnnotation(e.getKey(), e.getValue()));
			}

			return exporter.exportAnnotatedSolution(originalDataTransformer.getModelEntityMap(), solution);
		}
	}

	/**
	 * Method intended for use by unit tests.
	 * 
	 * @param rawSequences
	 * @param extraAnnotations
	 * @return
	 */
	public Schedule createOptimiserSchedule(@NonNull final ISequences rawSequences, @Nullable final Map<String, Object> extraAnnotations) {
		final Injector injector;
		{
			final Collection<@NonNull IOptimiserInjectorService> services = optimiserDataTransformer.getModuleServices();
			final List<Module> modules = new LinkedList<>();
			modules.add(new InitialSequencesModule(optimiserDataTransformer.getInitialSequences()));
			modules.add(new InputSequencesModule(rawSequences));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(
					new LNGParameters_EvaluationSettingsModule(optimiserDataTransformer.getUserSettings(), optimiserDataTransformer.getSolutionBuilderSettings().getConstraintAndFitnessSettings()),
					services, IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
			injector = optimiserDataTransformer.getInjector().createChildInjector(modules);
		}

		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		{
			final Injector childInjector = injector.createChildInjector(new ExporterExtensionsModule());
			childInjector.injectMembers(exporter);
		}
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);
			final IAnnotatedSolution solution = LNGSchedulerJobUtils.evaluateCurrentState(injector, optimisationData, rawSequences).getFirst();

			// Copy extra annotations - e.g. fitness information
			if (extraAnnotations != null) {
				extraAnnotations.entrySet().forEach(e -> solution.setGeneralAnnotation(e.getKey(), e.getValue()));
			}

			return exporter.exportAnnotatedSolution(optimiserDataTransformer.getModelEntityMap(), solution);
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Collection;
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
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.CopiedModelEntityMap;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.period.CopiedScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.IScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker;
import com.mmxlabs.models.lng.transformer.period.PeriodExporter;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer;
import com.mmxlabs.models.lng.transformer.period.ScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
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

	@NonNull
	private final OptimiserSettings optimiserSettings;

	@Nullable
	private final IScenarioEntityMapping periodMapping;

	@NonNull
	private final Collection<@NonNull String> hints;

	@Nullable
	private final ScenarioInstance scenarioInstance;

	@NonNull
	private LNGDataTransformer optimiserDataTransformer;

	// @NonNull
	// private final LNGDataTransformer originalDataTransformer;

	/**
	 * Integer to count "undo" state of the scenario. Each overwrite evaluation should increment this value. Calls to {@link LNGSchedulerJobUtils#undoPreviousOptimsationStep(EditingDomain, int)}
	 * should decrement it. Ideally we would track the current state of the command stack, but the API does not appear to permit this (maybe a command stack listener? - can we tell if a command is
	 * undo/redo?).
	 */
	private int overwriteCommandStackCounter = 0;

	private Collection<@NonNull IOptimiserInjectorService> optimiserInjectorServices;

	public LNGScenarioToOptimiserBridge(@NonNull final LNGScenarioModel scenario, @Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimiserSettings optimiserSettings,
			@NonNull final EditingDomain editingDomain, @Nullable final Module bootstrapModule, @Nullable final IOptimiserInjectorService localOverrides, boolean evaluationOnly,
			final @NonNull String @Nullable... initialHints) {
		this.originalScenario = scenario;
		this.scenarioInstance = scenarioInstance;
		this.optimiserSettings = optimiserSettings;
		this.originalEditingDomain = editingDomain;
		this.hints = LNGTransformerHelper.getHints(optimiserSettings, initialHints);

		optimiserInjectorServices = LNGTransformerHelper.getOptimiserInjectorServices(bootstrapModule, localOverrides);

		LNGDataTransformer originalDataTransformer = new LNGDataTransformer(this.originalScenario, optimiserSettings, hints, optimiserInjectorServices);

		// TODO: These ideally should be final, but #overwrite currently needs these variables set.
		this.optimiserEditingDomain = originalEditingDomain;
		this.optimiserDataTransformer = originalDataTransformer;
		this.optimiserScenario = originalScenario;

		// Trigger initial evaluation - note no fitness state is saved
		overwrite(0, originalDataTransformer.getInitialSequences(), null);

		if (!evaluationOnly) {
			final Triple<@NonNull LNGScenarioModel, @NonNull EditingDomain, @Nullable IScenarioEntityMapping> t = initPeriodOptimisationData(scenarioInstance, originalScenario, originalEditingDomain,
					optimiserSettings);

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

			optimiserDataTransformer = new LNGDataTransformer(this.optimiserScenario, optimiserSettings, hints, optimiserInjectorServices);
		} else {
			optimiserDataTransformer = originalDataTransformer;
		}
	}

	@NonNull
	private static Triple<@NonNull LNGScenarioModel, @NonNull EditingDomain, @Nullable IScenarioEntityMapping> initPeriodOptimisationData(@Nullable final ScenarioInstance scenarioInstance,
			@NonNull final LNGScenarioModel originalScenario, @NonNull final EditingDomain originalEditingDomain, @NonNull final OptimiserSettings optimiserSettings) {

		IScenarioEntityMapping periodMapping = null;
		{
			final OptimisationRange range = optimiserSettings.getRange();
			if (range != null) {
				if (range.getOptimiseAfter() != null || range.getOptimiseBefore() != null) {
					periodMapping = new ScenarioEntityMapping();
				}
			}
		}

		if (periodMapping != null) {

			final PeriodTransformer t = new PeriodTransformer();
			t.setInclusionChecker(new InclusionChecker());

			final NonNullPair<LNGScenarioModel, EditingDomain> p = t.transform(originalScenario, optimiserSettings, periodMapping);

			// DEBUGGING - store sub scenario as a "fork"
			if (scenarioInstance != null) {
				@NonNull
				ScenarioInstance pScenarioInstance = scenarioInstance;
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

	/**
	 * Export the current solution (and re-combine into original if period optimisation).
	 * 
	 * @param currentProgress
	 * @param solution
	 * @return
	 */
	public Schedule overwrite(final int currentProgress, @NonNull final ISequences rawSeqences, @Nullable final Map<String, Object> extraAnnotations) {

		// Clear any previous optimisation state.
		if (overwriteCommandStackCounter > 0) {
			if (periodMapping != null) {
				if (!LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, currentProgress, true)) {
					assert false;
				}
			}
			if (LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, currentProgress, true)) {
				--overwriteCommandStackCounter;
			} else {
				assert false;
			}
		}
		assert overwriteCommandStackCounter == 0;

		try {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(true);
			}

			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(currentProgress, rawSeqences, extraAnnotations, optimiserScenario, optimiserEditingDomain, originalScenario,
					originalEditingDomain, optimiserDataTransformer.getModelEntityMap(), optimiserSettings, periodMapping);
			originalEditingDomain.getCommandStack().execute(commandPair.getFirst());

			++overwriteCommandStackCounter;

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
	 * @param rawSequences
	 * @param extraAnnotations
	 * @return
	 */
	@NonNull
	public LNGScenarioModel exportAsCopy(@NonNull final ISequences rawSequences, @Nullable final Map<String, Object> extraAnnotations) {

		final EcoreUtil.Copier originalScenarioCopier = new EcoreUtil.Copier();
		final LNGScenarioModel targetOriginalScenario = (LNGScenarioModel) originalScenarioCopier.copy(originalScenario);
		// Do not do this here as CopiedModelEntityMap will do it. If we call it twice, we can end up with multiple copies of data
		// in reference lists
		// originalScenarioCopier.copyReferences();

		assert targetOriginalScenario != null;
		final EditingDomain targetOriginalEditingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final IScenarioEntityMapping pPeriodMapping = periodMapping;
		if (pPeriodMapping != null) {
			final EcoreUtil.Copier optimiserScenarioCopier = new EcoreUtil.Copier();
			final LNGScenarioModel targetOptimiserScenario = (LNGScenarioModel) optimiserScenarioCopier.copy(optimiserScenario);
			// As above
			// optimiserScenarioCopier.copyReferences();

			assert targetOptimiserScenario != null;

			final CopiedModelEntityMap copiedOptimiserModelEntityMap = new CopiedModelEntityMap(optimiserDataTransformer.getModelEntityMap(), optimiserScenarioCopier);

			final CopiedScenarioEntityMapping copiedPeriodMapping = new CopiedScenarioEntityMapping(pPeriodMapping, originalScenarioCopier, optimiserScenarioCopier);
			final EditingDomain targetOptimiserEditingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(100, rawSequences, extraAnnotations,

					targetOptimiserScenario, targetOptimiserEditingDomain, targetOriginalScenario, targetOriginalEditingDomain, copiedOptimiserModelEntityMap, optimiserSettings, copiedPeriodMapping);
			targetOriginalEditingDomain.getCommandStack().execute(commandPair.getFirst());

		} else {
			final CopiedModelEntityMap copiedModelEntityMap = new CopiedModelEntityMap(optimiserDataTransformer.getModelEntityMap(), originalScenarioCopier);
			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(100, rawSequences, extraAnnotations,

					targetOriginalScenario, targetOriginalEditingDomain, targetOriginalScenario, targetOriginalEditingDomain, copiedModelEntityMap, optimiserSettings, null);
			targetOriginalEditingDomain.getCommandStack().execute(commandPair.getFirst());
		}

		return targetOriginalScenario;

	}

	/**
	 * Returns a Pair containing a command and a null schedule. Once the Command has been executed, the Schedule will be populated.
	 * 
	 * @param currentProgress
	 * @param extraAnnotations
	 * @param solution
	 * @return
	 */
	private Pair<Command, Schedule> creatExportScheduleCommand(final int currentProgress, @NonNull final ISequences rawSequences, @Nullable final Map<String, Object> extraAnnotations,
			@NonNull final LNGScenarioModel targetOptimiserScenario, @NonNull final EditingDomain targetOptimiserEditingDomain, @NonNull final LNGScenarioModel targetOriginalScenario,
			@NonNull final EditingDomain targetOriginalEditingDomain, @NonNull final ModelEntityMap optimiserModelEntityMap, @NonNull OptimiserSettings originalOptimiserSettings,
			@Nullable final IScenarioEntityMapping periodMapping) {

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
					final Injector evaluationInjector;
					{
						final Collection<@NonNull IOptimiserInjectorService> services = optimiserDataTransformer.getModuleServices();
						final List<Module> modules = new LinkedList<>();
						modules.add(new InputSequencesModule(rawSequences));
						modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(optimiserDataTransformer.getOptimiserSettings()), services,
								IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
						modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
						evaluationInjector = optimiserDataTransformer.getInjector().createChildInjector(modules);
					}
					// This either applies to the real scenario or the period copy if present.
					final Pair<Command, Schedule> updateCommand = LNGSchedulerJobUtils.exportSolution(evaluationInjector, targetOptimiserScenario, optimiserSettings, targetOptimiserEditingDomain,
							optimiserModelEntityMap, rawSequences, extraAnnotations);

					// Apply to real scenario
					appendAndExecute(updateCommand.getFirst());
					p.setSecond(updateCommand.getSecond());
				} else {

					// TODO This should operate on a copy also

					// Stage 1, update the period scenario
					{
						final Injector evaluationInjector;
						{
							final Collection<@NonNull IOptimiserInjectorService> services = optimiserDataTransformer.getModuleServices();
							final List<@NonNull Module> modules = new LinkedList<>();
							modules.add(new InputSequencesModule(rawSequences));
							modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(optimiserDataTransformer.getOptimiserSettings()), services,
									IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
							modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
							evaluationInjector = optimiserDataTransformer.getInjector().createChildInjector(modules);
						}
						// This either applies to the real scenario or the period copy if present.
						final Pair<Command, Schedule> updateCommand = LNGSchedulerJobUtils.exportSolution(evaluationInjector, targetOptimiserScenario, optimiserSettings, targetOptimiserEditingDomain,
								optimiserModelEntityMap, rawSequences, extraAnnotations);

						// Period optimisation code path, apply the dual commands to the real scenario.

						// Execute the update command on the period scenario
						targetOptimiserEditingDomain.getCommandStack().execute(updateCommand.getFirst());
					}

					// Stage 2: Export the period scenario changes into the original scenario
					final PeriodExporter e = new PeriodExporter();
					final CompoundCommand part1 = LNGSchedulerJobUtils.createBlankCommand(currentProgress);

					part1.append(e.updateOriginal(targetOriginalEditingDomain, targetOriginalScenario, targetOptimiserScenario, periodMapping));
					// part1.append(e.updateOriginal(targetOptimiserEditingDomain, targetOriginalScenario, targetOptimiserScenario, periodMapping));
					if (part1.canExecute()) {
						appendAndExecute(part1);
					} else {
						throw new RuntimeException("Unable to execute period optimisation merge command");
					}

					// Stage 3: Re-evaluate original scenario to make sure it all ties in
					{
						final OptimiserSettings evalSettings = EcoreUtil.copy(originalOptimiserSettings);
						evalSettings.getRange().unsetOptimiseAfter();
						evalSettings.getRange().unsetOptimiseBefore();

						final Set<@NonNull String> hints = LNGTransformerHelper.getHints(evalSettings);

						// final LNGDataTransformer subTransformer = originalDataTransformer;

						// Always create a new transformer as we have problems with re-use and mapping newly created spot slots (and probably cargoes) between instances
						final LNGDataTransformer subTransformer = new LNGDataTransformer(targetOriginalScenario, evalSettings, hints, optimiserInjectorServices);
						ModelEntityMap originalModelEntityMap = subTransformer.getModelEntityMap();

						Injector evaluationInjector2;
						{
							final List<Module> modules2 = new LinkedList<>();
							modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(originalOptimiserSettings), optimiserInjectorServices,
									IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
							modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), optimiserInjectorServices,
									IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

							evaluationInjector2 = subTransformer.getInjector().createChildInjector(modules2);
						}

						// Take the new EMF state and generate an ISequences from it to re-evaluate

						final ISequences initialSequences;
						{
							final List<Module> modules2 = new LinkedList<>();
							modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGInitialSequencesModule(), optimiserInjectorServices,
									IOptimiserInjectorService.ModuleType.Module_InitialSolution, hints));

							final Injector initialSolutionInjector = evaluationInjector2.createChildInjector(modules2);
							final PerChainUnitScopeImpl scope = initialSolutionInjector.getInstance(PerChainUnitScopeImpl.class);
							try {
								scope.enter();
								initialSequences = initialSolutionInjector.getInstance(Key.get(ISequences.class, Names.named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)));
							} finally {
								scope.exit();
							}
						}

						final Pair<Command, Schedule> part2 = LNGSchedulerJobUtils.exportSolution(evaluationInjector2, targetOriginalScenario, EcoreUtil.copy(optimiserSettings),
								targetOriginalEditingDomain, originalModelEntityMap, initialSequences, extraAnnotations);
						if (part2.getFirst().canExecute()) {
							if (!appendAndExecute(part2.getFirst())) {
								throw new RuntimeException("Unable to execute period optimisation update command");
							}
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

	@Nullable
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
			// // Reset state
			// if (periodMapping != null) {
			// LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, 100);
			// LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
			// } else {
			// LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
			// }
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
}

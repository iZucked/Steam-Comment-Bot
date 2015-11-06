/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
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
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.period.CopiedScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.IScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker;
import com.mmxlabs.models.lng.transformer.period.PeriodExporter;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer;
import com.mmxlabs.models.lng.transformer.period.ScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioDataTransformer {

	private static final Logger log = LoggerFactory.getLogger(LNGScenarioDataTransformer2.class);

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
	private final Collection<String> hints;

	@Nullable
	private final ScenarioInstance scenarioInstance;

	@NonNull
	private LNGDataTransformer optimiserDataTransformer;

	@NonNull
	private final LNGDataTransformer originalDataTransformer;

	public LNGScenarioDataTransformer(@NonNull final LNGScenarioModel scenario, @Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimiserSettings optimiserSettings,
			@NonNull final EditingDomain editingDomain, @Nullable final Module bootstrapModule, @Nullable final IOptimiserInjectorService localOverrides, final String... initialHints) {
		this.originalScenario = scenario;
		this.scenarioInstance = scenarioInstance;
		this.optimiserSettings = optimiserSettings;
		this.originalEditingDomain = editingDomain;
		this.hints = LNGTransformerHelper.getHints(optimiserSettings, initialHints);

		final Collection<IOptimiserInjectorService> services = LNGTransformerHelper.getOptimiserInjectorServices(bootstrapModule, localOverrides);

		originalDataTransformer = new LNGDataTransformer(this.originalScenario, optimiserSettings, hints, services);
		// TODO: Evaluate initial state.

		this.optimiserEditingDomain = originalEditingDomain;
		this.optimiserDataTransformer = originalDataTransformer;
		this.optimiserScenario = originalScenario;

		System.out.println("1");
		System.out.println(originalEditingDomain.getCommandStack().getMostRecentCommand());
		System.out.println(optimiserEditingDomain.getCommandStack().getMostRecentCommand());
		overwrite(0, originalDataTransformer.getInitialSequences(), null);

		System.out.println("2");
		System.out.println(originalEditingDomain.getCommandStack().getMostRecentCommand());
		System.out.println(optimiserEditingDomain.getCommandStack().getMostRecentCommand());

		final Triple<LNGScenarioModel, EditingDomain, IScenarioEntityMapping> t = initPeriodOptimisationData(scenarioInstance, originalScenario, originalEditingDomain, optimiserSettings);
		this.optimiserScenario = t.getFirst();
		this.optimiserEditingDomain = t.getSecond();
		this.periodMapping = t.getThird();
		if (this.periodMapping != null) {
			optimiserDataTransformer = new LNGDataTransformer(this.optimiserScenario, optimiserSettings, hints, services);
		} else {
			optimiserDataTransformer = originalDataTransformer;
		}

		System.out.println("3");
		System.out.println(originalEditingDomain.getCommandStack().getMostRecentCommand());
		System.out.println(optimiserEditingDomain.getCommandStack().getMostRecentCommand());

		// TODO:
		// 1. Evaluate main scenario. Store data transformer.
		// 2. If period, pass copy into transformer, do not re-evaluate.
		// 2.1 -- store new "optimiser" datatransformer.

		// saveAsCopy
		// NEed to copy period scenario data model. Period mapping needs two copiers, original to copy1, period to copy2.

	}

	@NonNull
	private static Triple<LNGScenarioModel, EditingDomain, IScenarioEntityMapping> initPeriodOptimisationData(@Nullable final ScenarioInstance scenarioInstance,
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
			if (false && scenarioInstance != null) {
				try {
					LNGScenarioRunnerUtils.saveScenarioAsChild(scenarioInstance, scenarioInstance, p.getFirst(), "Period Scenario");
				} catch (final Exception e) {
					e.printStackTrace();
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
		System.out.println("Overwrite");
		System.out.println(originalEditingDomain.getCommandStack().getMostRecentCommand());
		System.out.println(optimiserEditingDomain.getCommandStack().getMostRecentCommand());
		// Clear any previous optimisation state.
		if (periodMapping != null) {
			LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, currentProgress);
		}
		LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, currentProgress);

		try {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(true);
			}

			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(currentProgress, rawSeqences, extraAnnotations, optimiserScenario, optimiserEditingDomain, originalScenario,
					originalEditingDomain, optimiserDataTransformer.getModelEntityMap(), periodMapping);
			originalEditingDomain.getCommandStack().execute(commandPair.getFirst());
			return commandPair.getSecond();
		} finally {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(false);
			}
		}
	}

	private LNGScenarioModel exportAsCopy(@NonNull final ISequences rawSeqences, @Nullable final Map<String, Object> extraAnnotations) {

		final EcoreUtil.Copier originalScenarioCopier = new EcoreUtil.Copier();
		final LNGScenarioModel targetOriginalScenario = (LNGScenarioModel) originalScenarioCopier.copy(originalScenario);
		originalScenarioCopier.copyReferences();

		assert targetOriginalScenario != null;
		final EditingDomain targetOriginalEditingDomain = LNGScenarioRunnerUtils.createLocalEditingDomain();

		final IScenarioEntityMapping pPeriodMapping = periodMapping;
		if (pPeriodMapping != null) {
			final EcoreUtil.Copier optimiserScenarioCopier = new EcoreUtil.Copier();
			final LNGScenarioModel targetOptimiserScenario = (LNGScenarioModel) optimiserScenarioCopier.copy(optimiserScenario);
			optimiserScenarioCopier.copyReferences();

			assert targetOptimiserScenario != null;

			final CopiedModelEntityMap copiedModelEntityMap = new CopiedModelEntityMap(optimiserDataTransformer.getModelEntityMap(), optimiserScenarioCopier);

			final CopiedScenarioEntityMapping copiedPeriodMapping = new CopiedScenarioEntityMapping(pPeriodMapping, originalScenarioCopier, optimiserScenarioCopier);
			final EditingDomain targetOptimiserEditingDomain = LNGScenarioRunnerUtils.createLocalEditingDomain();

			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(100, rawSeqences, extraAnnotations,

					targetOptimiserScenario, targetOptimiserEditingDomain, targetOriginalScenario, targetOriginalEditingDomain, copiedModelEntityMap, copiedPeriodMapping);
			targetOriginalEditingDomain.getCommandStack().execute(commandPair.getFirst());

		} else {
			final CopiedModelEntityMap copiedModelEntityMap = new CopiedModelEntityMap(originalDataTransformer.getModelEntityMap(), originalScenarioCopier);
			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(100, rawSeqences, extraAnnotations,

					targetOriginalScenario, targetOriginalEditingDomain, targetOriginalScenario, targetOriginalEditingDomain, copiedModelEntityMap, null);
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
			@NonNull final EditingDomain targetOriginalEditingDomain, @NonNull final ModelEntityMap modelEntityMap, @Nullable final IScenarioEntityMapping periodMapping) {

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
						final Collection<IOptimiserInjectorService> services = optimiserDataTransformer.getModuleServices();
						final List<Module> modules = new LinkedList<>();
						modules.add(new InputSequencesModule(rawSequences));
						modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(optimiserDataTransformer.getOptimiserSettings()), services,
								IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
						modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
						evaluationInjector = optimiserDataTransformer.getInjector().createChildInjector(modules);
					}
					// This either applies to the real scenario or the period copy if present.
					final Pair<Command, Schedule> updateCommand = LNGSchedulerJobUtils.exportSolution(evaluationInjector, targetOptimiserScenario, optimiserSettings, targetOptimiserEditingDomain,
							modelEntityMap, rawSequences, extraAnnotations);

					// Apply to real scenario
					appendAndExecute(updateCommand.getFirst());
					p.setSecond(updateCommand.getSecond());
				} else {

					// TODO This should operate on a copy also

					// Stage 1, update the period scenario
					{
						final Injector evaluationInjector;
						{
							final Collection<IOptimiserInjectorService> services = optimiserDataTransformer.getModuleServices();
							final List<Module> modules = new LinkedList<>();
							modules.add(new InputSequencesModule(rawSequences));
							modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(optimiserDataTransformer.getOptimiserSettings()), services,
									IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
							modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
							evaluationInjector = optimiserDataTransformer.getInjector().createChildInjector(modules);
						}
						// This either applies to the real scenario or the period copy if present.
						final Pair<Command, Schedule> updateCommand = LNGSchedulerJobUtils.exportSolution(evaluationInjector, targetOptimiserScenario, optimiserSettings, targetOptimiserEditingDomain,
								modelEntityMap, rawSequences, extraAnnotations);

						// Period optimisation code path, apply the dual commands to the real scenario.

						// Execute the update command on the period scenario
						targetOptimiserEditingDomain.getCommandStack().execute(updateCommand.getFirst());
					}

					// Stage 2: Export the period scenario changes into the original scenario
					final PeriodExporter e = new PeriodExporter();
					final CompoundCommand part1 = LNGSchedulerJobUtils.createBlankCommand(currentProgress);

					part1.append(e.updateOriginal(targetOptimiserEditingDomain, targetOriginalScenario, targetOptimiserScenario, periodMapping));
					if (part1.canExecute()) {
						appendAndExecute(part1);
					} else {
						throw new RuntimeException("Unable to execute period optimisation merge command");
					}

					// Stage 3: Re-evaluate original scenario to make sure it all ties in
					{
						final OptimiserSettings evalSettings = EcoreUtil.copy(originalDataTransformer.getOptimiserSettings());
						evalSettings.getRange().unsetOptimiseAfter();
						evalSettings.getRange().unsetOptimiseBefore();

						final Set<String> hints = LNGTransformerHelper.getHints(evalSettings);
						// Resuse from period transformer? (unless we are in a save as copy call)
						final LNGDataTransformer subTransformer = originalDataTransformer;

						Injector evaluationInjector2;
						{
							final Collection<IOptimiserInjectorService> services = subTransformer.getModuleServices();
							final List<Module> modules2 = new LinkedList<>();
							modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(originalDataTransformer.getOptimiserSettings()), services,
									IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
							modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

							evaluationInjector2 = subTransformer.getInjector().createChildInjector(modules2);
						}

						// Replace with copied version?
						final ModelEntityMap subModelEntityMap = subTransformer.getModelEntityMap();
						final Pair<Command, Schedule> part2 = LNGSchedulerJobUtils.exportSolution(evaluationInjector2, targetOriginalScenario, EcoreUtil.copy(optimiserSettings), originalEditingDomain,
								subModelEntityMap, subTransformer.getInitialSequences(), extraAnnotations);
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

	@Nullable
	public Injector getInjector() {
		return optimiserDataTransformer.getInjector();
	}

	@NonNull
	public LNGScenarioModel getScenario() {
		return originalScenario;
	}

	public ScenarioInstance storeAsCopy(@NonNull final ISequences rawSequences, @NonNull final String newName, @NonNull final Container parent, @Nullable final Map<String, Object> extraAnnotations) {

		System.out.println("storeAsCopy");
		System.out.println(originalEditingDomain.getCommandStack().getMostRecentCommand());
		System.out.println(optimiserEditingDomain.getCommandStack().getMostRecentCommand());

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

	/**
	 * TODO: Currently this is now the only reason to keep the {@link IAnnotatedSolution} in the API in addition to the raw {@link ISequences}
	 * 
	 * @param annotatedSolution
	 * @return
	 */
	@NonNull
	public static Map<String, Object> extractOptimisationAnnotations(@Nullable final IAnnotatedSolution annotatedSolution) {
		final Map<String, Object> extraAnnotations = new HashMap<>();
		if (annotatedSolution != null) {
			extraAnnotations.put(OptimiserConstants.G_AI_fitnessComponents, annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, Map.class));
			extraAnnotations.put(OptimiserConstants.G_AI_iterations, annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_iterations, Integer.class));
			extraAnnotations.put(OptimiserConstants.G_AI_runtime, annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_runtime, Long.class));
		}
		return extraAnnotations;
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return optimiserDataTransformer;
	}
}

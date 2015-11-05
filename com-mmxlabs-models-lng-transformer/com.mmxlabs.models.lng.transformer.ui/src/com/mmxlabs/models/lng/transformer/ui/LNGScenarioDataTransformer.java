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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
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
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioDataTransformer {

	private static final Logger log = LoggerFactory.getLogger(LNGScenarioDataTransformer.class);

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
	private final Module bootstrapModule;

	@Nullable
	private final IOptimiserInjectorService localOverrides;

	@Nullable
	private final ScenarioInstance scenarioInstance;

	@NonNull
	private final LNGDataTransformer dataTransformer;

	public LNGScenarioDataTransformer(@NonNull final LNGScenarioModel scenario, @Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimiserSettings optimiserSettings,
			@NonNull final EditingDomain editingDomain, @Nullable final Module bootstrapModule, @Nullable final IOptimiserInjectorService localOverrides, final String... initialHints) {
		this.originalScenario = scenario;
		this.scenarioInstance = scenarioInstance;
		this.optimiserSettings = optimiserSettings;
		this.originalEditingDomain = editingDomain;
		this.bootstrapModule = bootstrapModule;
		this.localOverrides = localOverrides;
		this.hints = LNGTransformerHelper.getHints(optimiserSettings, initialHints);

		optimiserScenario = originalScenario;
		optimiserEditingDomain = originalEditingDomain;

		periodMapping = initPeriodOptimisationData(optimiserSettings, bootstrapModule);

		dataTransformer = new LNGDataTransformer(this.optimiserScenario, optimiserSettings, hints, LNGTransformerHelper.getOptimiserInjectorServices(bootstrapModule, localOverrides));
	}

	@Nullable
	private IScenarioEntityMapping initPeriodOptimisationData(final OptimiserSettings optimiserSettings, @Nullable final Module extraModule) {

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

			final PeriodTransformer t = new PeriodTransformer(extraModule);
			t.setInclusionChecker(new InclusionChecker());

			// TODO: Store keep LNGDataTransformer
			optimiserScenario = t.transform(originalScenario, optimiserSettings, periodMapping, LNGTransformerHelper.getOptimiserInjectorServices(bootstrapModule, localOverrides));

			// DEBUGGING - store sub scenario as a "fork"
			if (false && scenarioInstance != null) {
				try {
					LNGScenarioRunnerUtils.saveScenarioAsChild(scenarioInstance, scenarioInstance, optimiserScenario, "Period Scenario");
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			optimiserEditingDomain = LNGScenarioRunnerUtils.createLocalEditingDomain();

			// Delete commands need a resource set on the editing domain
			final Resource r = new XMIResourceImpl();
			r.getContents().add(optimiserScenario);
			optimiserEditingDomain.getResourceSet().getResources().add(r);
		} else {
			optimiserScenario = originalScenario;
			optimiserEditingDomain = originalEditingDomain;
		}

		return periodMapping;
	}

	/**
	 * Export the current solution (and re-combine into original if period optimisation).
	 * 
	 * @param currentProgress
	 * @param solution
	 * @return
	 */
	public synchronized Schedule exportSchedule(final int currentProgress, @NonNull final ISequences rawSeqences, @Nullable final Map<String, Object> extraAnnotations) {
		try {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(true);
			}

			final Pair<Command, Schedule> commandPair = creatExportScheduleCommand(currentProgress, rawSeqences, extraAnnotations);
			originalEditingDomain.getCommandStack().execute(commandPair.getFirst());
			return commandPair.getSecond();
		} finally {
			if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(false);
			}
		}
	}

	/**
	 * Returns a Pair containing a command and a null schedule. Once the Command has been executed, the Schedule will be populated.
	 * 
	 * @param currentProgress
	 * @param extraAnnotations
	 * @param solution
	 * @return
	 */
	private Pair<Command, Schedule> creatExportScheduleCommand(final int currentProgress, @NonNull final ISequences rawSequences, @Nullable final Map<String, Object> extraAnnotations) {

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

				final Injector evaluationInjector;
				{
					final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();
					final List<Module> modules = new LinkedList<>();
					modules.add(new InputSequencesModule(rawSequences));
					modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(dataTransformer.getOptimiserSettings()), services,
							IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
					modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
					evaluationInjector = dataTransformer.getInjector().createChildInjector(modules);
				}
				// This either applies to the real scenario or the period copy if present.
				final Pair<Command, Schedule> updateCommand = LNGSchedulerJobUtils.exportSolution(evaluationInjector, optimiserScenario, optimiserSettings, optimiserEditingDomain,
						dataTransformer.getModelEntityMap(), rawSequences, extraAnnotations);
				if (periodMapping == null) {
					// Apply to real scenario
					appendAndExecute(updateCommand.getFirst());
					p.setSecond(updateCommand.getSecond());
				} else {
					// Period optimisation code path, apply the dual commands to the real scenario.

					// Execute the update command on the period scenario
					optimiserEditingDomain.getCommandStack().execute(updateCommand.getFirst());

					// Export the period scenario changes into the original scenario
					final PeriodExporter e = new PeriodExporter();
					final CompoundCommand part1 = LNGSchedulerJobUtils.createBlankCommand(currentProgress);
					part1.append(e.updateOriginal(originalEditingDomain, originalScenario, optimiserScenario, periodMapping));
					if (part1.canExecute()) {
						appendAndExecute(part1);
					} else {
						throw new RuntimeException("Unable to execute period optimisation merge command");
					}

					// Re-evaluate command
					{
						final OptimiserSettings evalSettings = EcoreUtil.copy(dataTransformer.getOptimiserSettings());
						evalSettings.getRange().unsetOptimiseAfter();
						evalSettings.getRange().unsetOptimiseBefore();

						Set<String> hints = LNGTransformerHelper.getHints(optimiserSettings);
						final LNGDataTransformer subTransformer = new LNGDataTransformer(originalScenario, evalSettings, hints,
								LNGTransformerHelper.getOptimiserInjectorServices(bootstrapModule, localOverrides));

						Injector evaluationInjector2;
						{
							final Collection<IOptimiserInjectorService> services = subTransformer.getModuleServices();
							final List<Module> modules2 = new LinkedList<>();
							modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(dataTransformer.getOptimiserSettings()), services,
									IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
							modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

							evaluationInjector2 = subTransformer.getInjector().createChildInjector(modules2);
						}

						// Replace with copied version?
						final ModelEntityMap subModelEntityMap = subTransformer.getModelEntityMap();
						final Pair<Command, Schedule> part2 = LNGSchedulerJobUtils.exportSolution(evaluationInjector2, originalScenario, EcoreUtil.copy(optimiserSettings), originalEditingDomain,
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
		return dataTransformer.getInjector();
	}

	@NonNull
	public LNGScenarioModel getScenario() {
		return originalScenario;
	}

	public synchronized ScenarioInstance storeSolutionAsChild(@NonNull final ISequences rawSequences, @NonNull final String newName, Container parent) {

		final ScenarioInstance pScenarioInstance = scenarioInstance;
		if (pScenarioInstance == null) {
			throw new NullPointerException();
		}

		// Export the solution onto the scenario
		exportSchedule(100, rawSequences, null);

		// Save the scenario as a fork.
		try {
			return LNGScenarioRunnerUtils.saveScenarioAsChild(pScenarioInstance, parent, originalScenario, newName);
		} catch (final Exception e) {
			throw new RuntimeException("Unable to store changeset scenario: " + e.getMessage(), e);
		} finally {
			// Reset state
			if (periodMapping != null) {
				LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, 100);
				LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
			} else {
				LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
			}
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
		// TODO Auto-generated method stub
		return dataTransformer;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.impl.MultiStateResult;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioRunner {

	@SuppressWarnings("null")
	@NonNull
	private static final Logger log = LoggerFactory.getLogger(LNGScenarioRunner.class);

	@NonNull
	private final IChainRunner chainRunner;

	private long startTimeMillis;

	@Nullable
	private Schedule schedule;

	@NonNull
	private LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	public LNGScenarioToOptimiserBridge getScenarioToOptimiserBridge() {
		return scenarioToOptimiserBridge;
	}

	@NonNull
	private LNGScenarioModel scenarioModel;

	@Nullable
	private ScenarioInstance scenarioInstance;

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings, final String... initialHints) {
		this(exectorService, scenario, null, optimiserSettings, LNGSchedulerJobUtils.createLocalEditingDomain(), initialHints);

	}

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenarioModel, @NonNull final OptimiserSettings optimiserSettings, @Nullable Module extraModule,
			final String... initialHints) {
		this(exectorService, scenarioModel, null, optimiserSettings, LNGSchedulerJobUtils.createLocalEditingDomain(), extraModule, null, initialHints);
	}

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance,
			@NonNull final OptimiserSettings optimiserSettings, @NonNull final EditingDomain editingDomain, final String... initialHints) {
		this(exectorService, scenarioModel, scenarioInstance, optimiserSettings, editingDomain, null, null, initialHints);
	}

	public LNGScenarioRunner(@NonNull ExecutorService executorService, @NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance,
			@NonNull final OptimiserSettings optimiserSettings, @NonNull final EditingDomain editingDomain, @Nullable final Module extraModule,
			@Nullable final IOptimiserInjectorService localOverrides, final String... initialHints) {

		this.scenarioModel = scenarioModel;
		this.scenarioInstance = scenarioInstance;

		// TODO: initial hints should specify LSO!

		// here we want to take user settings and generate initial state settings
		scenarioToOptimiserBridge = new LNGScenarioToOptimiserBridge(scenarioModel, scenarioInstance, optimiserSettings, editingDomain, extraModule, localOverrides,
				LNGTransformerHelper.HINT_OPTIMISE_LSO);

		// FB: 1712 Switch for enabling run-all similarity optimisation. Needs better UI hook ups.

		if (false) {
			chainRunner = LNGScenarioChainBuilder.createRunAllSimilarityOptimisationChain(scenarioToOptimiserBridge.getDataTransformer(), scenarioToOptimiserBridge, optimiserSettings, executorService,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
		} else {
			chainRunner = LNGScenarioChainBuilder.createStandardOptimisationChain(null, scenarioToOptimiserBridge.getDataTransformer(), scenarioToOptimiserBridge, optimiserSettings, executorService,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
		}

	}

	public void dispose() {

	}

	/**
	 * Evaluates the initial state
	 */
	@Nullable
	public Schedule evaluateInitialState() {
		startTimeMillis = System.currentTimeMillis();

		// TODO: The API would be *much* cleaner if we did not need to return the annotated solution to get the fitness trace. This would avoid the need here to re-calculate after doing an initial
		// export in the constructor
		// TODO: It is also pretty keyed to the first run LSO state and not any other stage in the process.
		// TODO: Fitness traces needed for ITS run. Additional data also needed for headless app runs - e.g. move analysis logger.
		final IMultiStateResult result = chainRunner.getInitialState();

		final ISequences startRawSequences = result.getBestSolution().getFirst();
		final Map<String, Object> extraAnnotations = result.getBestSolution().getSecond();
		schedule = scenarioToOptimiserBridge.overwrite(0, startRawSequences, extraAnnotations);

		// initialSchedule= ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule();

		// need to undo this.chainRunner..
		return schedule;
	}

	public IMultiStateResult run() {
		return runWithProgress(new NullProgressMonitor());
	}

	/**
	 * used by {@link AbstractEclipseJobControl} / {@link LNGSchedulerOptimiserJobControl}
	 * 
	 * @param progressMonitor
	 */

	@NonNull
	public IMultiStateResult runWithProgress(final @NonNull IProgressMonitor progressMonitor) {
		// assert createOptimiser;

		// TODO: Replace with originalScenario.getScheduleModel().getSchedule()
		if (schedule == null) {
			evaluateInitialState();
		}

		final IMultiStateResult result = chainRunner.run(progressMonitor);

		schedule = scenarioToOptimiserBridge.overwrite(100, result.getBestSolution().getFirst(), result.getBestSolution().getSecond());
		log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));

		return result;

<<<<<<< local
=======
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
						exportOptimiserSolution = !foundBetterResult;
					}
					// The breakdown optimiser may find a better solution. This will be saved in storeBreakdownSolutionsAsForks
					if (exportOptimiserSolution) {
						// export final state
						finalSchedule = overwrite(100, bestResult.getBestSolution().getFirst(), bestResult.getBestSolution().getSecond());
					}

					if (pRunnerHook != null) {
						// List<ISequences> actionSetRawSequences = bestResult.getSolutions().stream().map(t -> t.getFirst()).collect(Collectors.toList());
						pRunnerHook.reportSequences(IRunnerHook.PHASE_ACTION_SETS, bestResult.getBestSolution().getFirst() /* , actionSetRawSequences */);
					}
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
>>>>>>> other
	}

	@Nullable
	public final Schedule getSchedule() {
		return schedule;
	}

	@NonNull
	public LNGScenarioModel getScenario() {
		return scenarioModel;
	}

	@Nullable
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;

	}

	private IMultiStateResult performLSOOptimisation(LocalSearchOptimiser lsoOptimiser, final IProgressMonitor progressMonitor/* , final ISequences bestRawSequences */)
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
		return scenarioToOptimiserBridge.getDataTransformer().getRunnerHook();
	}

	public void setRunnerHook(IRunnerHook runnerHook) {
		scenarioToOptimiserBridge.getDataTransformer().setRunnerHook(runnerHook);
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import org.apache.shiro.authz.aop.UserAnnotationHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;
import org.omg.PortableInterceptor.SUCCESSFUL;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.BreakEvenTransformerUnit;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;

public class LightWeightSchedulerJobControl extends AbstractEclipseJobControl {

	// private static final int REPORT_PERCENTAGE = 1;
	// private int currentProgress = 0;

	private final LightWeightSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final com.mmxlabs.scenario.service.model.manager.ModelReference modelReference;

	private final LNGScenarioModel originalScenario;
	private final EditingDomain originalEditingDomain;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final LNGScenarioRunner scenarioRunner;

	private ExecutorService executorService;

	public LightWeightSchedulerJobControl(final LightWeightSchedulerJobDescriptor jobDescriptor) {
		super(jobDescriptor.getJobName());

		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

		this.modelReference = modelRecord.aquireReference("LNGSchedulerInsertSlotJobControl");
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();
		originalEditingDomain = modelReference.getEditingDomain();

		/*
		 * Error checks
		 */
		{
			// Disable optimisation in P&L testing phase
			if (LicenseFeatures.isPermitted("features:phase-pnl-testing")) {
				throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
			}
			// if (!OptimisationHelper.isAllowedGCO(this.originalScenario)) {
			// throw new RuntimeException("Optimisation is disabled when missing prices are used");
			// }
		}

		// TODO: This should be static / central service?
		executorService = LNGScenarioChainBuilder.createExecutorService();// Executors.newSingleThreadExecutor();

		UserSettings userSettings = jobDescriptor.getUserSettings();

		final OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();
		plan.setUserSettings(EcoreUtil.copy(userSettings));
		plan.setSolutionBuilderSettings(ScenarioUtils.createDefaultSolutionBuilderSettings());

		scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, scenarioInstance, plan, originalEditingDomain, null, false, //
				LNGTransformerHelper.HINT_OPTIMISE_LSO, //
				LNGTransformerHelper.HINT_DISABLE_CACHES, //
				LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN);

		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
	}

	@Override
	protected void reallyPrepare() {
		scenarioRunner.evaluateInitialState();
	}

	@Override
	protected void doRunJob(IProgressMonitor progressMonitor) {
		long start = System.currentTimeMillis();
		progressMonitor.beginTask("Optimise", 100);
		try {

			@NonNull
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
			// TODO: Filter
			final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
			while (iterator.hasNext()) {
				final Constraint constraint = iterator.next();
				if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
					iterator.remove();
				}
				if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
					iterator.remove();
				}

			}
			ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

			Schedule schedule = scenarioToOptimiserBridge.getScenario().getScheduleModel().getSchedule();

			final LightWeightSchedulerOptimiserUnit slotInserter = new LightWeightSchedulerOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), constraintAndFitnessSettings,
					scenarioRunner.getExecutorService(), dataTransformer.getInitialSequences(), scenarioToOptimiserBridge.getScenario(), dataTransformer.getInitialResult(), dataTransformer.getHints());

			Function<Integer, String> nameFactory = changeSetIdx -> {
				String newName;
				if (changeSetIdx == 0) {
					newName = "Scheduler-base";
					changeSetIdx++;
				} else {
					newName = String.format("Scheduler-%s", (changeSetIdx++));
				}
				return newName;
			};
			IMultiStateResult results = slotInserter.run(new SubProgressMonitor(progressMonitor, 90));
			{
				IProgressMonitor monitor = new SubProgressMonitor(progressMonitor, 10);
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
				monitor.beginTask("Export", solutions.size());
				System.out.printf("Found %d solutions\n", solutions.size() - 1);
				try {
					int changeSetIdx = 0;
					for (final NonNullPair<ISequences, Map<String, Object>> changeSet : solutions) {
						String newName = nameFactory.apply(changeSetIdx++);
						// if (changeSetIdx == 0) {
						// newName = "ActionSet-base";
						// changeSetIdx++;
						// } else {
						// newName = String.format("ActionSet-%s", (changeSetIdx++));
						// }

						@NonNull
						Collection<@NonNull String> hints = new LinkedList<>(dataTransformer.getHints());
						hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);
						try {
							scenarioToOptimiserBridge.storeAsCopy(changeSet.getFirst(), newName, scenarioInstance, null);
						} catch (final Exception e) {
							throw new RuntimeException("Unable to store scenario: " + e.getMessage(), e);
						}
						monitor.worked(1);
					}
				} finally {
					monitor.done();
				}
			}
		} finally {
			progressMonitor.done();
			if (true) {
				System.out.println("done in:" + (System.currentTimeMillis() - start));
			}
		}
		// if (scenarioRunner.isFinished()) {
		// return false;
		// } else {
		// return true;
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#kill()
	 */
	@Override
	protected void kill() {
	}

	@Override
	public void dispose() {
		executorService.shutdownNow();

		if (modelReference != null) {
			modelReference.close();
		}

		// if (scenarioRunner != null) {
		// scenarioRunner.dispose();
		// }
		super.dispose();
	}

	@Override
	public final MMXRootObject getJobOutput() {
		return originalScenario;
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return jobDescriptor;
	}

	@Override
	public boolean isPauseable() {
		return true;
	}

}

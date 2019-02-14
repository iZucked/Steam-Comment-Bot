/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;

import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.SequencesSerialiser;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerOptimiserJobControl extends AbstractEclipseJobControl {

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ScenarioModelRecord modelRecord;

	private final IScenarioDataProvider originalScenarioDataProvider;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final LNGScenarioRunner scenarioRunner;

	private CleanableExecutorService executorService;

	public LNGSchedulerOptimiserJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(),
				CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY, (jobDescriptor.isOptimising() ? imgOpti : imgEval)));

		// Disable optimisation in P&L testing phase
		if (LicenseFeatures.isPermitted("features:phase-pnl-testing")) {
			throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
		}

		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		this.originalScenarioDataProvider = modelRecord.aquireScenarioDataProvider("LNGSchedulerOptimiserJobControl");
		final EditingDomain originalEditingDomain = originalScenarioDataProvider.getEditingDomain();

		// Disable optimisation in P&L testing phase
		if (LicenseFeatures.isPermitted("features:phase-limited-testing")) {
			CargoModel cargoModel = ScenarioModelUtil.getCargoModel(originalScenarioDataProvider);

			long countA = cargoModel.getCargoes().stream().count();
			long countB = cargoModel.getLoadSlots().stream().count();
			long countC = cargoModel.getDischargeSlots().stream().count();
			long max = 15;
			if (countA > max || countB > max || countC > max) {
				throw new RuntimeException("Full optimisation is disabled during the P&L testing phase.");
			}
		}

		// TODO: This should be static / central service?
		executorService = LNGScenarioChainBuilder.createExecutorService();// Executors.newSingleThreadExecutor();

		@Nullable
		IRunnerHook runnerHook = null;
		if (false) {
			runnerHook = new AbstractRunnerHook() {

				@Override
				public void doReportSequences(final String phase, final ISequences rawSequences, final LNGDataTransformer dataTransformer) {
					switch (phase) {

					case IRunnerHook.STAGE_LSO:
					case IRunnerHook.STAGE_HILL:
					case IRunnerHook.STAGE_INITIAL:
						save(rawSequences, phase, dataTransformer.getInjector());
						break;
					case IRunnerHook.STAGE_ACTION_SETS:
						break;
					}
				}

				@Override
				public ISequences doGetPrestoredSequences(final String stage, final LNGDataTransformer dataTransformer) {
					switch (stage) {
					case IRunnerHook.STAGE_LSO:
					case IRunnerHook.STAGE_HILL:
						return load(stage, dataTransformer.getInjector());
					case IRunnerHook.STAGE_INITIAL:
					case IRunnerHook.STAGE_ACTION_SETS:
						break;

					}
					return null;
				}

				private void save(final ISequences rawSequences, final String type, final Injector injector) {
					// assert false;
					try {
						final String suffix = scenarioInstance.getName() + "." + type + ".sequences";
						// final File file2 = new File("/home/ubuntu/scenarios/"+suffix);
						final File file2 = new File("c:\\Temp\\" + suffix);
						try (FileOutputStream fos = new FileOutputStream(file2)) {
							assert (injector != null);
							SequencesSerialiser.save(injector.getInstance(IOptimisationData.class), rawSequences, fos);
						}
					} catch (final Exception e) {
						// Assert.fail(e.getMessage());
					}
				}

				private ISequences load(final String type, final Injector injector) {
					try {
						final String suffix = scenarioInstance.getName() + "." + type + ".sequences";
						// final File file2 = new File("/home/ubuntu/scenarios/"+suffix);
						final File file2 = new File("c:\\Temp\\" + suffix);
						try (FileInputStream fos = new FileInputStream(file2)) {
							assert (injector != null);
							return SequencesSerialiser.load(injector.getInstance(IOptimisationData.class), fos);
						}
					} catch (final Exception e) {
						// return
						// Assert.fail(e.getMessage());
					}
					return null;
				}
			};
		}

		scenarioRunner = LNGOptimisationBuilder.begin(originalScenarioDataProvider, scenarioInstance) //
				.withOptimisationPlan(jobDescriptor.getOptimisationPlan()) //
				.withRunnerHook(runnerHook) //
				.withOptimiseHint() //
				.buildDefaultRunner() //
				.getScenarioRunner();

		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
	}

	@Override
	protected void reallyPrepare() {
		if (!jobDescriptor.getOptimisationPlan().getUserSettings().isCleanStateOptimisation()) {
			originalScenarioDataProvider.setLastEvaluationFailed(true);
			scenarioRunner.evaluateInitialState();
		}
	}

	@Override
	protected void doRunJob(final IProgressMonitor progressMonitor) {
		final long start = System.currentTimeMillis();
		progressMonitor.beginTask("Optimise", 100);
		try {
			if (jobDescriptor.isOptimising() == false) {
				return; // if we are not optimising, finish.
			}
			scenarioRunner.runWithProgress(new SubProgressMonitor(progressMonitor, 100));
			super.setProgress(100);
			originalScenarioDataProvider.setLastEvaluationFailed(false);
		} finally {
			progressMonitor.done();
			if (true) {
				System.out.println("done in:" + (System.currentTimeMillis() - start));
			}
		}
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

		if (originalScenarioDataProvider != null) {
			originalScenarioDataProvider.close();
		}

		super.dispose();
	}

	@Override
	public final Object getJobOutput() {
		return null;
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

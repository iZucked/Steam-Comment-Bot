/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;

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
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.SequencesSerialiser;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerOptimiserJobControl extends AbstractEclipseJobControl {

	// private static final int REPORT_PERCENTAGE = 1;
	// private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final LNGScenarioRunner scenarioRunner;

	private ExecutorService executorService;

	public LNGSchedulerOptimiserJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(),
				CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY, (jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelReference = scenarioInstance.getReference();
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();
		final EditingDomain originalEditingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);

		// TODO: This should be static / central service?
		executorService = LNGScenarioChainBuilder.createExecutorService();// Executors.newSingleThreadExecutor();

		@Nullable
		IRunnerHook runnerHook = null;
		if (false) {
			runnerHook = new AbstractRunnerHook() {
				@Override
				public void beginPhase(String phase, Injector injector) {
					super.beginPhase(phase, injector);
					// for (IRunExporter exporter : exporters) {
					// exporter.setPhase(phase, injector);
					// }
				}

				@Override
				public void endPhase(String phase) {
					super.endPhase(phase);
				}

				@Override
				public void reportSequences(String phase, final ISequences rawSequences) {
					switch (phase) {

					case IRunnerHook.PHASE_LSO:
					case IRunnerHook.PHASE_HILL:
					case IRunnerHook.PHASE_INITIAL:
						save(rawSequences, phase);
						break;
					case IRunnerHook.PHASE_ACTION_SETS:
						break;
					}
				}

				@Override
				public ISequences getPrestoredSequences(String phase) {
					switch (phase) {
					case IRunnerHook.PHASE_LSO:
					case IRunnerHook.PHASE_HILL:
						return load(phase);
					case IRunnerHook.PHASE_INITIAL:
					case IRunnerHook.PHASE_ACTION_SETS:
						break;

					}
					return null;
				}

				private void save(final ISequences rawSequences, final String type) {
					// assert false;
					try {
						final String suffix = scenarioInstance.getName() + "." + type + ".sequences";
						// final File file2 = new File("/home/ubuntu/scenarios/"+suffix);
						final File file2 = new File("c:\\Temp\\" + suffix);
						try (FileOutputStream fos = new FileOutputStream(file2)) {
							final Injector injector = getInjector();
							assert (injector != null);
							SequencesSerialiser.save(injector.getInstance(IOptimisationData.class), rawSequences, fos);
						}
					} catch (final Exception e) {
						// Assert.fail(e.getMessage());
					}
				}

				private ISequences load(final String type) {
					try {
						final String suffix = scenarioInstance.getName() + "." + type + ".sequences";
						// final File file2 = new File("/home/ubuntu/scenarios/"+suffix);
						final File file2 = new File("c:\\Temp\\" + suffix);
						try (FileInputStream fos = new FileInputStream(file2)) {
							final Injector injector = getInjector();
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
		scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, scenarioInstance, jobDescriptor.getOptimisationPlan(), originalEditingDomain, runnerHook, false,
				LNGTransformerHelper.HINT_OPTIMISE_LSO);

		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));

		// Disable optimisation in P&L testing phase
		if (LicenseFeatures.isPermitted("features:phase-pnl-testing")) {
			throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
		}

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
			// TODO Auto-generated method stub
			if (jobDescriptor.isOptimising() == false) {
				return; // if we are not optimising, finish.
			}
			scenarioRunner.runWithProgress(new SubProgressMonitor(progressMonitor, 100));
			super.setProgress(100);
		} finally {
			progressMonitor.done();
			if (false) {
				System.out.println("done in:"+(System.currentTimeMillis() - start));
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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Date;

import javax.management.timer.Timer;

import org.apache.shiro.SecurityUtils;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.period.IScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker;
import com.mmxlabs.models.lng.transformer.period.PeriodExporter;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer;
import com.mmxlabs.models.lng.transformer.period.ScenarioEntityMapping;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerOptimiserJobControl extends AbstractEclipseJobControl {

	private static final Logger log = LoggerFactory.getLogger(LNGSchedulerOptimiserJobControl.class);

	private static final int REPORT_PERCENTAGE = 1;
	private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;
	private LNGScenarioModel optimiserScenario;

	private IScenarioEntityMapping periodMapping;

	private ModelEntityMap modelEntityMap;

	private LocalSearchOptimiser optimiser;

	private long startTimeMillis;

	private final EditingDomain originalEditingDomain;
	private EditingDomain optimiserEditingDomain;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private Injector injector = null;

	// private String lockKey;

	private LNGTransformer transformer;

	public LNGSchedulerOptimiserJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(), CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY,
				(jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelReference = scenarioInstance.getReference();
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();
		this.originalEditingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));

		// Disable optimisation in P&L testing phase
		if (SecurityUtils.getSubject().isPermitted("features:phase-pnl-testing")) {
			throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
		}

	}

	@Override
	protected void reallyPrepare() {
		startTimeMillis = System.currentTimeMillis();

		OptimiserSettings optimiserSettings = jobDescriptor.getOptimiserSettings();
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

			optimiserScenario = t.transform(originalScenario, optimiserSettings, periodMapping);

			// DEBUGGING - store sub scenario as a "fork"
			if (true) {
				try {
					IScenarioService scenarioService = scenarioInstance.getScenarioService();
					ScenarioInstance dup = scenarioService.insert(scenarioInstance, EcoreUtil.copy(optimiserScenario));
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
				} catch (Exception e) {
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

		transformer = new LNGTransformer(optimiserScenario, optimiserSettings, LNGTransformer.HINT_OPTIMISE_LSO);

		injector = transformer.getInjector();

		// final IOptimisationData data = transformer.getOptimisationData();
		modelEntityMap = transformer.getModelEntityMap();

		final IOptimisationContext context = transformer.getOptimisationContext();
		optimiser = transformer.getOptimiser();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		optimiser.init();
		final IAnnotatedSolution startSolution = optimiser.start(context);

		LNGSchedulerJobUtils.exportSolution(injector, optimiserScenario, transformer.getOptimiserSettings(), optimiserEditingDomain, modelEntityMap, startSolution, 0);
		if (periodMapping != null) {
			final PeriodExporter e = new PeriodExporter();
			final CompoundCommand cmd = LNGSchedulerJobUtils.createBlankCommand(0);
			cmd.append(e.updateOriginal(originalEditingDomain, originalScenario, optimiserScenario, periodMapping));
			if (cmd.canExecute()) {
				originalEditingDomain.getCommandStack().execute(cmd);
			} else {
				throw new RuntimeException("Unable to execute period optimisation merge command");
			}

			{
				final OptimiserSettings evalSettings = EcoreUtil.copy(optimiserSettings);
				evalSettings.getRange().unsetOptimiseAfter();
				evalSettings.getRange().unsetOptimiseBefore();
				final LNGTransformer transformer = new LNGTransformer(originalScenario, evalSettings);

				final ModelEntityMap modelEntityMap = transformer.getModelEntityMap();
				final IAnnotatedSolution finalSolution = LNGSchedulerJobUtils.evaluateCurrentState(transformer);
				LNGSchedulerJobUtils.exportSolution(transformer.getInjector(), originalScenario, transformer.getOptimiserSettings(), originalEditingDomain, modelEntityMap, finalSolution, 0);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#step()
	 */
	@Override
	protected boolean step() {
		// final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		if (jobDescriptor.isOptimising() == false) {
			// clear lock
			// scenarioInstance.getLock(lockKey).release();
			return false; // if we are not optimising, finish.
		}
		optimiser.step(REPORT_PERCENTAGE);
		currentProgress += REPORT_PERCENTAGE;

		super.setProgress(currentProgress);
		if (optimiser.isFinished()) {
			// export final state

			LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
			LNGSchedulerJobUtils.exportSolution(injector, optimiserScenario, transformer.getOptimiserSettings(), optimiserEditingDomain, modelEntityMap, optimiser.getBestSolution(), 100);
			if (periodMapping != null) {
				LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, 100);

				final PeriodExporter e = new PeriodExporter();
				final CompoundCommand cmd = LNGSchedulerJobUtils.createBlankCommand(100);
				cmd.append(e.updateOriginal(originalEditingDomain, originalScenario, optimiserScenario, periodMapping));
				if (cmd.canExecute()) {
					originalEditingDomain.getCommandStack().execute(cmd);

				} else {
					throw new RuntimeException("Unable to execute period optimisation merge command");
				}

				{
					OptimiserSettings evalSettings = EcoreUtil.copy(transformer.getOptimiserSettings());
					evalSettings.getRange().unsetOptimiseAfter();
					evalSettings.getRange().unsetOptimiseBefore();
					final LNGTransformer subTransformer = new LNGTransformer(originalScenario, evalSettings);

					final ModelEntityMap modelEntityMap = subTransformer.getModelEntityMap();
					final IAnnotatedSolution finalSolution = LNGSchedulerJobUtils.evaluateCurrentState(subTransformer);
					LNGSchedulerJobUtils.exportSolution(subTransformer.getInjector(), originalScenario, subTransformer.getOptimiserSettings(), originalEditingDomain, modelEntityMap, finalSolution, 0);
				}
			}

			optimiser = null;
			log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
			super.setProgress(100);
			// scenarioInstance.getLock(lockKey).release();
			return false;
		} else {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#kill()
	 */
	@Override
	protected void kill() {
		if (optimiser != null) {
			optimiser = null;
		}
	}

	@Override
	public void dispose() {

		kill();
		if (this.modelEntityMap != null) {
			this.modelEntityMap.dispose();
			this.modelEntityMap = null;
		}

		// TODO: this.scenario = null;
		this.optimiser = null;

		injector = null;
		if (this.modelReference != null) {
			this.modelReference.close();
		}

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

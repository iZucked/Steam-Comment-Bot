/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.emf.common.command.BasicCommandStack;
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
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
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
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;

public class LNGScenarioRunner {

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

	private Module extraModule;

	private EnumMap<ModuleType, List<Module>> localOverrides;

	private ScenarioInstance scenarioInstance;

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
		optimiserScenario = originalScenario;
		optimiserEditingDomain = originalEditingDomain;
	}

	interface RunnerHook {
		void periodScenarioGenerated();

		void startSolution();

		void endSolution();

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
	 * Intialise the evaluation/optimisation system. Prepare any period scenarios, creates the {@link LNGTransformer} and the optimiser (if an optimisation hint is specified) .
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
		if (createOptimiser) {
			// Pin variable for null analysis
			final IOptimisationContext pContext = this.context;
			assert pContext != null;

			startSolution = optimiser.start(pContext);
		} else {
			startSolution = LNGSchedulerJobUtils.evaluateCurrentState(transformer);
		}

		initialSchedule = LNGSchedulerJobUtils.exportSolution(injector, optimiserScenario, transformer.getOptimiserSettings(), optimiserEditingDomain, modelEntityMap, startSolution, 0);
		final Schedule periodSchedule = exportPeriodScenario(0);
		if (periodSchedule != null) {
			initialSchedule = periodSchedule;
		}
		final LocalSearchOptimiser pOptimiser = this.optimiser;
		if (pOptimiser != null) {
			try {
				pOptimiser.getProgressMonitor().begin(pOptimiser, pOptimiser.getFitnessEvaluator().getCurrentFitness(), startSolution);
			} catch (final Exception e) {

			} finally {
			}
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

		if (periodMapping != null) {

			final PeriodTransformer t = new PeriodTransformer(extraModule);
			t.setInclusionChecker(new InclusionChecker());

			optimiserScenario = t.transform(originalScenario, optimiserSettings, periodMapping);

			// // DEBUGGING - store sub scenario as a "fork"
			// if (true && scenarioInstance != null) {
			// try {
			// IScenarioService scenarioService = scenarioInstance.getScenarioService();
			// ScenarioInstance dup = scenarioService.insert(scenarioInstance, EcoreUtil.copy(optimiserScenario));
			// dup.setName("Period Scenario");
			//
			// // Copy across various bits of information
			// dup.getMetadata().setContentType(scenarioInstance.getMetadata().getContentType());
			// dup.getMetadata().setCreated(scenarioInstance.getMetadata().getCreated());
			// dup.getMetadata().setLastModified(new Date());
			//
			// // Copy version context information
			// dup.setVersionContext(scenarioInstance.getVersionContext());
			// dup.setScenarioVersion(scenarioInstance.getScenarioVersion());
			//
			// dup.setClientVersionContext(scenarioInstance.getClientVersionContext());
			// dup.setClientScenarioVersion(scenarioInstance.getClientScenarioVersion());
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }

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
	public boolean run() {
		return step(100);
	}

	/**
	 * Step the optimisation by x% of the total number of iterations. Only call if an optimisation hint has been specified.
	 * 
	 * @return Return true if the optimisation still has more work to do.
	 * 
	 */
	public boolean step(final int percent) {
		assert createOptimiser;
		final IOptimiserProgressMonitor monitor = optimiser.getProgressMonitor();
		optimiser.step(percent);
		if (monitor != null) {
			monitor.report(optimiser, optimiser.getNumberOfIterationsCompleted(), optimiser.getFitnessEvaluator().getCurrentFitness(), optimiser.getFitnessEvaluator().getBestFitness(),
					optimiser.getCurrentSolution(), optimiser.getBestSolution());
		}

		if (optimiser.isFinished()) {

			// Generate the changesets decomposition.
			if (false) {
				BreadthOptimiser instance = injector.getInstance(BreadthOptimiser.class);
				instance.optimise(optimiser.getBestRawSequencecs());
			}
			// export final state
			LNGSchedulerJobUtils.undoPreviousOptimsationStep(optimiserEditingDomain, 100);
			finalSchedule = LNGSchedulerJobUtils.exportSolution(injector, optimiserScenario, transformer.getOptimiserSettings(), optimiserEditingDomain, modelEntityMap, optimiser.getBestSolution(),
					100);

			final Schedule periodSchedule = exportPeriodScenario(100);
			if (periodSchedule != null) {
				finalSchedule = periodSchedule;
			}
			if (monitor != null) {
				monitor.done(optimiser, optimiser.getFitnessEvaluator().getBestFitness(), optimiser.getBestSolution());
			}
			optimiser = null;
			log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
			return false;
		} else {
			return true;
		}
	}

	@Nullable
	private Schedule exportPeriodScenario(final int currentProgress) {
		if (periodMapping != null) {
			LNGSchedulerJobUtils.undoPreviousOptimsationStep(originalEditingDomain, currentProgress);

			final PeriodExporter e = new PeriodExporter();
			final CompoundCommand cmd = LNGSchedulerJobUtils.createBlankCommand(currentProgress);
			cmd.append(e.updateOriginal(originalEditingDomain, originalScenario, optimiserScenario, periodMapping));
			if (cmd.canExecute()) {
				originalEditingDomain.getCommandStack().execute(cmd);
			} else {
				throw new RuntimeException("Unable to execute period optimisation merge command");
			}

			{
				final OptimiserSettings evalSettings = EcoreUtil.copy(transformer.getOptimiserSettings());
				// final OptimiserSettings evalSettings = EcoreUtil.copy(optimiserSettings);
				evalSettings.getRange().unsetOptimiseAfter();
				evalSettings.getRange().unsetOptimiseBefore();
				final LNGTransformer subTransformer = new LNGTransformer(originalScenario, evalSettings, extraModule, localOverrides);

				final ModelEntityMap subModelEntityMap = subTransformer.getModelEntityMap();
				final IAnnotatedSolution finalSolution = LNGSchedulerJobUtils.evaluateCurrentState(subTransformer);
				return LNGSchedulerJobUtils.exportSolution(subTransformer.getInjector(), originalScenario, EcoreUtil.copy(optimiserSettings), originalEditingDomain, subModelEntityMap, finalSolution,
						currentProgress);
			}
		}
		return null;
	}

	public void dispose() {

		if (this.modelEntityMap != null) {
			this.modelEntityMap.dispose();
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
	public Injector getInjector() {
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
}

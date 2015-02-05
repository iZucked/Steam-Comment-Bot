/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

import com.google.common.collect.Maps;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;

/**
 * Simple wrapper based on {@link LNGSchedulerJobDescriptor} to run an optimisation in the unit tests.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioRunner {

	private final LNGScenarioModel scenario;

	private IOptimisationContext context;
	private ModelEntityMap modelEntityMap;
	private LocalSearchOptimiser optimiser;

	// private Schedule intialSchedule;

	private Schedule finalSchedule;

	private Injector injector;

	private final SettingsOverride settings;

	public final Injector getInjector() {
		return injector;
	}

	private LNGTransformer transformer;

	public ScenarioRunner(final LNGScenarioModel scenario, final SettingsOverride settings) {
		this.scenario = scenario;
		this.settings = settings;
	}

	public final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	// public final Schedule getIntialSchedule() {
	// return intialSchedule;
	// }

	protected final LNGScenarioModel getScenario() {
		return scenario;
	}

	public final IOptimisationContext getContext() {
		return context;
	}

	public void initStage1() {
		OptimiserSettings optimiserSettings = scenario.getPortfolioModel().getParameters() == null ? ScenarioUtils.createDefaultSettings() : scenario.getPortfolioModel().getParameters();

		final EnumMap<ModuleType, List<Module>> localOverrides = Maps.newEnumMap(IOptimiserInjectorService.ModuleType.class);
		localOverrides.put(IOptimiserInjectorService.ModuleType.Module_ParametersModule, Collections.<Module> singletonList(new SettingsOverrideModule(settings)));

		transformer = new LNGTransformer(scenario, optimiserSettings, localOverrides, LNGTransformer.HINT_OPTIMISE_LSO);

		injector = transformer.getInjector();

		modelEntityMap = transformer.getModelEntityMap();

		context = transformer.getOptimisationContext();
		optimiser = transformer.getOptimiser();
	}

	public void initStage2(final IOptimiserProgressMonitor monitor) {
		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(monitor);

		optimiser.init();

		// intialSchedule = exportSchedule(optimiser.start(context));
	}

	public void run() {
		optimiser.optimise(context);
		finalSchedule = exportSchedule(optimiser.getBestSolution());
	}

	private Schedule exportSchedule(final IAnnotatedSolution solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		final Injector childInjector = injector.createChildInjector(new ExporterExtensionsModule());
		childInjector.injectMembers(exporter);

		final Schedule schedule = exporter.exportAnnotatedSolution(modelEntityMap, solution);

		return schedule;
	}

	/**
	 * Update the Scenario with the best solution. Note: This {@link ScenarioRunner} should not be used again.
	 */
	public Schedule updateScenario() {

		// Construct internal command stack to generate correct output schedule
		final BasicCommandStack commandStack = new BasicCommandStack();
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

		return LNGSchedulerJobUtils.exportSolution(injector, scenario, transformer.getOptimiserSettings(), ed, modelEntityMap, optimiser.getBestSolution(), 0);
	}

}

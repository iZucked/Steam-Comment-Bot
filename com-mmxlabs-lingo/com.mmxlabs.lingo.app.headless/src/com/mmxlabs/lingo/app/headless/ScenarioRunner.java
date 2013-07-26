/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import com.google.common.collect.Maps;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.util.ScenarioUtils;
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
	private ModelEntityMap entities;
	private LocalSearchOptimiser optimiser;

	// private Schedule intialSchedule;

	private Schedule finalSchedule;

	private Injector injector;

	private final SettingsOverride settings;

	public final Injector getInjector() {
		return injector;
	}

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

		final LNGTransformer transformer = new LNGTransformer(scenario, optimiserSettings, localOverrides);

		injector = transformer.getInjector();

		entities = transformer.getEntities();

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
		finalSchedule = exportSchedule(optimiser.getBestSolution(true));
	}

	private Schedule exportSchedule(final IAnnotatedSolution solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		final Injector childInjector = injector.createChildInjector(new ExporterExtensionsModule());
		childInjector.injectMembers(exporter);

		final Schedule schedule = exporter.exportAnnotatedSolution(entities, solution);

		return schedule;
	}
}

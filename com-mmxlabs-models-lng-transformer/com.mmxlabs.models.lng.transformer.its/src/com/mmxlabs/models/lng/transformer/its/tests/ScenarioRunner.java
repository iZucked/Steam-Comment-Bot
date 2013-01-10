/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import com.google.inject.Injector;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;

/**
 * Simple wrapper based on {@link LNGSchedulerJobDescriptor} to run an optimisation in the unit tests.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioRunner {

	private final MMXRootObject scenario;

	private IOptimisationContext context;
	private ResourcelessModelEntityMap entities;
	private LocalSearchOptimiser optimiser;

	private Schedule intialSchedule;

	private Schedule finalSchedule;

	private Injector injector;

	public ScenarioRunner(final MMXRootObject scenario) {
		this.scenario = scenario;
	}

	public final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	public final Schedule getIntialSchedule() {
		return intialSchedule;
	}

	protected final MMXRootObject getScenario() {
		return scenario;
	}

	public final IOptimisationContext getContext() {
		return context;
	}

	public void init() throws IncompleteScenarioException {

		final LNGTransformer transformer = new LNGTransformer(scenario, new ContractExtensionTestModule());

		injector = transformer.getInjector();

		entities = transformer.getEntities();

		context = transformer.getOptimisationContext();
		optimiser = transformer.getOptimiser();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		// Limit number of iterations to keep runtime down.
		optimiser.setNumberOfIterations(10000);

		optimiser.init();

		intialSchedule = exportSchedule(optimiser.start(context));
	}

	public void run() {
		run(100);
	}

	public void run(int percentage) {
		optimiser.step(percentage);
		finalSchedule = exportSchedule(optimiser.getBestSolution(true));
	}

	private Schedule exportSchedule(final IAnnotatedSolution solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		final Injector childInjector = injector.createChildInjector(new ExporterExtensionsModule());
		childInjector.injectMembers(exporter);

		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, solution);

		return schedule;
	}
}

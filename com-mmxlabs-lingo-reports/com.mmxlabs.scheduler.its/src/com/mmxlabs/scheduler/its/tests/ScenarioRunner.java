/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.OptimisationTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ResourcelessModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.export.AnnotatedSolutionExporter;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

/**
 * Simple wrapper based on {@link LNGSchedulerJobDescriptor} to run an optimisation in the
 * unit tests.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioRunner {

	private final Scenario scenario;

	private IOptimisationData<ISequenceElement> data;
	private IOptimisationContext<ISequenceElement> context;

	private final ModelEntityMap entities = new ResourcelessModelEntityMap();

	private LocalSearchOptimiser<ISequenceElement> optimiser;

	private Schedule intialSchedule;

	private Schedule finalSchedule;

	public ScenarioRunner(final Scenario scenario) {
		this.scenario = scenario;
	}

	protected final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	protected final Schedule getIntialSchedule() {
		return intialSchedule;
	}

	protected final Scenario getScenario() {
		return scenario;
	}

	public final IOptimisationContext<ISequenceElement> getContext() {
		return context;
	}

	public void init() throws IncompleteScenarioException {

		entities.setScenario(scenario);

		final LNGScenarioTransformer lst = new LNGScenarioTransformer(scenario);

		final OptimisationTransformer ot = new OptimisationTransformer(
				lst.getOptimisationSettings());

		data = lst.createOptimisationData(entities);

		final Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optAndContext = ot
				.createOptimiserAndContext(data, entities);

		context = optAndContext.getFirst();
		optimiser = optAndContext.getSecond();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser
				.setProgressMonitor(new NullOptimiserProgressMonitor<ISequenceElement>());

		// Limit number of iterations to keep runtime down.
		optimiser.setNumberOfIterations(10000);
		
		optimiser.init();

		optimiser.start(context);

		intialSchedule = exportSchedule(optimiser.getBestSolution(true));

	}

	public void run() {
		optimiser.step(100);
		finalSchedule = exportSchedule(optimiser.getBestSolution(true));
	}

	private Schedule exportSchedule(
			final IAnnotatedSolution<ISequenceElement> solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario,
				entities, solution);

		return schedule;
	}
}

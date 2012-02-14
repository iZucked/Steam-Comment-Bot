/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
import com.mmxlabs.lngscheduler.emf.extras.contracts.SimpleContractTransformer;
import com.mmxlabs.lngscheduler.emf.extras.export.AnnotatedSolutionExporter;
import com.mmxlabs.lngscheduler.emf.extras.inject.LNGTransformerModule;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;

/**
 * Simple wrapper based on {@link LNGSchedulerJobDescriptor} to run an optimisation in the unit tests.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioRunner {

	private final Scenario scenario;

	private IOptimisationData data;
	private IOptimisationContext context;

	private final ModelEntityMap entities = new ResourcelessModelEntityMap();

	private LocalSearchOptimiser optimiser;

	private Schedule intialSchedule;

	private Schedule finalSchedule;

	public ScenarioRunner(final Scenario scenario) {
		this.scenario = scenario;
	}

	public final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	public final Schedule getIntialSchedule() {
		return intialSchedule;
	}

	protected final Scenario getScenario() {
		return scenario;
	}

	public final IOptimisationContext getContext() {
		return context;
	}

	public void init() throws IncompleteScenarioException {

		entities.setScenario(scenario);

		final LNGScenarioTransformer lst = LNGTransformerModule.createLNGScenarioTransformer(scenario);

		if (!lst.addPlatformTransformerExtensions()) {
			// add extensions manually; TODO improve this later.
			final SimpleContractTransformer sct = new SimpleContractTransformer();
			lst.addTransformerExtension(sct);
			lst.addContractTransformer(sct, sct.getContractEClasses());
		}

		final OptimisationTransformer ot = new OptimisationTransformer(lst.getOptimisationSettings());

		data = lst.createOptimisationData(entities);

		final Pair<IOptimisationContext, LocalSearchOptimiser> optAndContext = ot.createOptimiserAndContext(data, entities);

		context = optAndContext.getFirst();
		optimiser = optAndContext.getSecond();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		// Limit number of iterations to keep runtime down.
		optimiser.setNumberOfIterations(10000);

		optimiser.init();

		intialSchedule = exportSchedule(optimiser.start(context));
	}

	public void run() {
		optimiser.step(100);
		finalSchedule = exportSchedule(optimiser.getBestSolution(true));
	}

	private Schedule exportSchedule(final IAnnotatedSolution solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		// TODO add trading exporter extension?
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, solution);

		return schedule;
	}
}

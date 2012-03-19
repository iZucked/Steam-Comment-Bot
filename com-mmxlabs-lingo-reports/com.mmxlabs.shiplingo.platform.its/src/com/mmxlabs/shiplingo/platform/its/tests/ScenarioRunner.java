/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.contracts.SimpleContractTransformer;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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

	private final MMXRootObject scenario;

	private IOptimisationData data;
	private IOptimisationContext context;
	private ModelEntityMap entities;
	private LocalSearchOptimiser optimiser;

	private Schedule intialSchedule;

	private Schedule finalSchedule;

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


		final LNGTransformer transformer = new LNGTransformer(scenario);

		entities = transformer.getEntities();
		LNGScenarioTransformer lst =  transformer.getLngScenarioTransformer();

		{
			// add extensions manually; TODO improve this later.
			final SimpleContractTransformer sct = new SimpleContractTransformer();
			lst.addTransformerExtension(sct);
			lst.addContractTransformer(sct, sct.getContractEClasses());
		}

		final OptimisationTransformer ot = transformer.getOptimisationTransformer();

		data = transformer.getOptimisationData();

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

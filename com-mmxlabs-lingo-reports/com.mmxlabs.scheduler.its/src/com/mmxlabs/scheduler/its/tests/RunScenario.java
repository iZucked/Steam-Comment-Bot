package com.mmxlabs.scheduler.its.tests;
import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.OptimisationTransformer;
import com.mmxlabs.lngscheduler.emf.extras.export.AnnotatedSolutionExporter;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

public class RunScenario {

	private Scenario scenario;

	private IOptimisationData<ISequenceElement> data;
	private IOptimisationContext<ISequenceElement> context;

	private final ModelEntityMap entities = new ModelEntityMap();

	private LocalSearchOptimiser<ISequenceElement> optimiser;

	private Schedule intialSchedule;

	private Schedule finalSchedule;

	protected final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	protected final Schedule getIntialSchedule() {
		return intialSchedule;
	}

	public RunScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	protected final Scenario getScenario() {
		return scenario;
	}

	public void init() {

		entities.setScenario(scenario);

		final LNGScenarioTransformer lst = new LNGScenarioTransformer(scenario);

		final OptimisationTransformer ot = new OptimisationTransformer(
				lst.getOptimisationSettings());

		try {
			data = lst.createOptimisationData(entities);
		} catch (IncompleteScenarioException e) {
			return;
		}

		final Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optAndContext = ot
				.createOptimiserAndContext(data, entities);

		final IOptimisationContext<ISequenceElement> context = optAndContext
				.getFirst();
		optimiser = optAndContext.getSecond();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser
				.setProgressMonitor(new NullOptimiserProgressMonitor<ISequenceElement>());

		optimiser.init();
		
		optimiser.start(context);

		intialSchedule = exportSchedule(optimiser.getBestSolution(true));

	}

	public void run() {
		int step = optimiser.step(100);
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

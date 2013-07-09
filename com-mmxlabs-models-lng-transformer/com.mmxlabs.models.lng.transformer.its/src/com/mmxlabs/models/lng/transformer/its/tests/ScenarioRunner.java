/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.transformer.util.ScenarioUtils;
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

	private final LNGScenarioModel scenario;

	private IOptimisationContext context;
	private ModelEntityMap entities;
	private LocalSearchOptimiser optimiser;

	private Schedule intialSchedule;

	private Schedule finalSchedule;

	private Injector injector;

	private LNGTransformer transformer;

	/**
	 * @since 3.0
	 */
	public ScenarioRunner(final LNGScenarioModel scenario) {
		this.scenario = scenario;
	}

	public final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	public final Schedule getIntialSchedule() {
		return intialSchedule;
	}

	/**
	 * @since 3.0
	 */
	public final LNGScenarioModel getScenario() {
		return scenario;
	}

	public final IOptimisationContext getContext() {
		return context;
	}

	public void init() throws IncompleteScenarioException {
		OptimiserSettings optimiserSettings = ScenarioUtils.createDefaultSettings();

		transformer = new LNGTransformer(scenario, optimiserSettings, new TransformerExtensionTestModule(), LNGTransformer.HINT_OPTIMISE_LSO);

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

		final Schedule schedule = exporter.exportAnnotatedSolution(entities, solution);

		return schedule;
	}

	/**
	 * Update the Scenario with the best solution. Note: This {@link ScenarioRunner} should not be used again.
	 */
	public void updateScenario() {

		// Construct internal command stack to generate correct output schedule
		final BasicCommandStack commandStack = new BasicCommandStack();
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

		LNGSchedulerJobUtils.exportSolution(injector, scenario, transformer.getOptimiserSettings(), ed, entities, optimiser.getBestSolution(true), 0);
	}

}

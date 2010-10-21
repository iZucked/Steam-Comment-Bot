/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.core.impl.AnnotatedSequence;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IScheduledEvent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SchedulerUtils;
import com.mmxlabs.scheduler.optimiser.fitness.impl.ga.GASequenceScheduler;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorUtil;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlanAnnotator;

/**
 * Temporary(?) utility class to use the {@link GASequenceScheduler} to schedule
 * a {@link ISequences} and generate the various {@link IScheduledEvent} objects
 * for an {@link IAnnotatedSolution}.
 * 
 * TODO: This is not an generic or very extensible approach - we may wish to dump this class going forward.
 * 
 * @author Simon Goodall
 * 
 */
public final class CargoSchedulerExporter {

	private CargoSchedulerExporter() {

	}

	public static <T> IAnnotatedSolution<T> exportState(
			IOptimisationContext<T> context, ISequences<T> state) {

		final IOptimisationData<T> data = context.getOptimisationData();

		// Create a fitness core to grab the components - need these for the GA
		// Scheduler
		
		// we need to get the scheduler factory out of the existing core
		ISchedulerFactory schedulerFactory = null;
		for (IFitnessCoreFactory factory : context.getFitnessFunctionRegistry().getFitnessCoreFactories()) {
			if (factory instanceof CargoSchedulerFitnessCoreFactory) {
				schedulerFactory = ((CargoSchedulerFitnessCoreFactory)factory).getSchedulerFactory();
			}
		}
		
		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();
		factory.setSchedulerFactory(schedulerFactory);
		final CargoSchedulerFitnessCore<T> core = factory.instantiate();

		core.init(data);

		final ISequenceScheduler<T> scheduler = schedulerFactory.createScheduler(data, 
				core.getCargoSchedulerFitnessComponent());

		@SuppressWarnings("unchecked")
		final IPortSlotProvider<T> portSlotProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);

		// TODO: Integrate this into the optimiser process rather than here
		final AnnotatedSolution<T> solution = new AnnotatedSolution<T>();
		solution.setSequences(state);
		solution.setContext(context);

		final VoyagePlanAnnotator<T> annotator = new VoyagePlanAnnotator<T>();
		annotator.setPortSlotProvider(portSlotProvider);

		// Run sequence manipulator to obtain a sequence to evaluate
		final ISequencesManipulator<T> manipulator = SequencesManipulatorUtil
				.createDefaultSequenceManipulators(data);
		final IModifiableSequences<T> modifiedSequences = new ModifiableSequences<T>(
				state);
		//Whoops: manipulator gets applied twice if we're not careful.
//		manipulator.manipulate(modifiedSequences); 

		// Schedule sequences and generate the output data
		for (final Map.Entry<IResource, ISequence<T>> entry : modifiedSequences
				.getSequences().entrySet()) {

			final IResource resource = entry.getKey();
			final ISequence<T> sequence = entry.getValue();

			final AnnotatedSequence<T> annotatedSequence = new AnnotatedSequence<T>();

			if (sequence.size() > 0) {

				// Schedule sequence
				final Pair<Integer, List<VoyagePlan>> plans = scheduler.schedule(resource,
						sequence);

				final ArrayList<T> elements = new ArrayList<T>(sequence.size());

				for (final T e : sequence) {
					elements.add(e);
				}

				annotator.annotateFromVoyagePlan(resource, plans.getSecond(), plans.getFirst(),
						annotatedSequence);
			}
			solution.setAnnotatedSequence(resource, annotatedSequence);
		}
		return solution;
	}
}

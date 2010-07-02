package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

/**
 * This class contains the logic required to schedule a {@link ISequence}. This
 * will determine arrival times and additional information.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ISequenceScheduler<T> {

	/**
	 * Attempt to schedule the given {@link ISequence}. Returns a {@link List}
	 * of {@link IVoyagePlan}s or null if there was a problem.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */
	List<IVoyagePlan> schedule(IResource resource, ISequence<T> sequence);

	/**
	 * Release resources.
	 */
	void dispose();

}

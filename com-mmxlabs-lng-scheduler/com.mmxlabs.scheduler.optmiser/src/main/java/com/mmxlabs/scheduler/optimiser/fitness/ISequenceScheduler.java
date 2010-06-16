package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;

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
	 * Attempt to schedule the given {@link ISequence}. Returns false if for
	 * some reason a valid schedule is unable to be found.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */
	boolean schedule(IResource resource, ISequence<T> sequence, IAnnotatedSequence<T> annotatedSequence);

	/**
	 * Release resources.
	 */
	void dispose();

}

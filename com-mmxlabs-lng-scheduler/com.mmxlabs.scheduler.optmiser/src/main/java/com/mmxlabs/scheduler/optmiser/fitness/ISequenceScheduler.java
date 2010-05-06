package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;

/**
 * This class contains the logic required to schedule a {@link ISequence}. This
 * will determine arrival times and additional information.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequenceScheduler {

	/**
	 * Attempt to schedule the given {@link ISequence}. Returns false if for
	 * some reason a valid schedule is unable to be found.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */
	boolean schedule(IResource resource, ISequence<ISequenceElement> sequence);

	/**
	 * Return an additional data object tied to the given sequence element.
	 * 
	 * @param <U>
	 * @param key
	 * @param clz
	 * @return
	 */
	<U> U getAdditionalInformation(ISequenceElement element, String key,
			Class<U> clz);

}

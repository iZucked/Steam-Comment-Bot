package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.optimiser.ISequence;

/**
 * This class contains the logic required to schedule a {@link ISequence}. This
 * will determine arrival times and additional information.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequenceScheduler<T> {

	/**
	 * Attempt to schedule the given {@link ISequence}. Returns false if for
	 * some reason a valid schedule is unable to be found.
	 * 
	 * @param sequence
	 * @return
	 */
	boolean schedule(ISequence<T> sequence);

	/**
	 * Returns the scheduled start time for the given sequence element.
	 * 
	 * @param element
	 * @return
	 */
	int getStartTime(T element);

	/**
	 * Returns the scheduled end time for the given sequence element.
	 * 
	 * @param element
	 * @return
	 */
	int getEndTime(T element);

	/**
	 * Return an additional data object tied to the given sequence element.
	 * 
	 * @param <U>
	 * @param key
	 * @param clz
	 * @return
	 */
	<U> U getAdditionalInformation(T element, String key, Class<U> clz);
}

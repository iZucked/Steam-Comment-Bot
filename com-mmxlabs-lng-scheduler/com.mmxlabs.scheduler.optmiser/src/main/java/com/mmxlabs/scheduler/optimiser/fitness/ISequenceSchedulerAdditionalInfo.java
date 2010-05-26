package com.mmxlabs.scheduler.optimiser.fitness;

/**
 * Interface to store "additional info" objects based upon keys.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequenceSchedulerAdditionalInfo {

	<U> U get(String key, Class<U> clz);

	void put(String key, Object value);

}
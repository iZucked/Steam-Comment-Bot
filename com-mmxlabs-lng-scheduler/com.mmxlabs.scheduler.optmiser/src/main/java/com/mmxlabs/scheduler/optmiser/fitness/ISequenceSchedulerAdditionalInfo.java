package com.mmxlabs.scheduler.optmiser.fitness;

public interface ISequenceSchedulerAdditionalInfo {

	public abstract <U> U get(final String key, final Class<U> clz);

	public abstract void put(final String key, final Object value);

}
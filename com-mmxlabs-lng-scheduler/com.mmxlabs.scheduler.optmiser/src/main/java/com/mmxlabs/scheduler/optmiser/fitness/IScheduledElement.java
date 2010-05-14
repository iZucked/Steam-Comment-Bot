package com.mmxlabs.scheduler.optmiser.fitness;


public interface IScheduledElement<T> {

	T getSequenceElement();
	
	int getStartTime();
	
	int getEndTime();
	
	int getDuration();
	
	String getName();
}

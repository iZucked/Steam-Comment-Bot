package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;

public interface IScheduledElement {

	ISequenceElement getSequenceElement();
	
	int getStartTime();
	
	int getEndTime();
	
	int getDuration();
	
	String getName();
}

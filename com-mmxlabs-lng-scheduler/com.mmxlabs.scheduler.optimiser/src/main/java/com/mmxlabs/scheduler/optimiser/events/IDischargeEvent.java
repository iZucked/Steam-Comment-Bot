package com.mmxlabs.scheduler.optimiser.events;

public interface IDischargeEvent<T> extends IPortVisitEvent<T> {

	long getDischargeVolume();

	long getSalesPrice();

}

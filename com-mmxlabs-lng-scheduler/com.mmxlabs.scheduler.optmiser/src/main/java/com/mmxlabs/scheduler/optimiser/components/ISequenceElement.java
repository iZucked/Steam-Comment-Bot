package com.mmxlabs.scheduler.optimiser.components;

public interface ISequenceElement {

	String getName();
	
	IPort getPort();

	ICargo getCargo();
}

package com.mmxlabs.scheduler.optmiser.components;

public interface ISequenceElement {

	String getName();
	
	IPort getPort();

	ICargo getCargo();
}

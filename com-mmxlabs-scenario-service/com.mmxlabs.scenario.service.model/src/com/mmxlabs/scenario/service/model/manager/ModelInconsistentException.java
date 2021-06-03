package com.mmxlabs.scenario.service.model.manager;

public class ModelInconsistentException extends RuntimeException {

	public ModelInconsistentException(final String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}

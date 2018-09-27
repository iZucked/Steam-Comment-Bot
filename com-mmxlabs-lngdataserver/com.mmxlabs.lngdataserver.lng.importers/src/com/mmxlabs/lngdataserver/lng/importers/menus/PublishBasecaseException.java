package com.mmxlabs.lngdataserver.lng.importers.menus;

public class PublishBasecaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Type {
		FAILED_TO_EVALUATE,

		OTHER
	};

	private final Type type;

	public Type getType() {
		return type;
	}

	public PublishBasecaseException(final String message, final Type type) {
		super(message);
		this.type = type;
	}

	public PublishBasecaseException(final String message, final Type type, final Exception e) {
		super(message, e);
		this.type = type;
	}
}

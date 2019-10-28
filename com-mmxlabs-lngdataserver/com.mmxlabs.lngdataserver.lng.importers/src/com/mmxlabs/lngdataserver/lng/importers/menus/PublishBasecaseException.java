/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

public class PublishBasecaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Type {
		FAILED_UNKNOWN_ERROR, //
		FAILED_SERVICE_LOCKED, //
		FAILED_TO_MIGRATE, //
		FAILED_TO_EVALUATE, //
		FAILED_TO_SAVE, //
		FAILED_TO_UPLOAD_BASECASE, //
		FAILED_TO_UPLOAD_BASECASE_CSV, //
		FAILED_TO_UPLOAD_BACKING_DATA, //
		FAILED_TO_UPLOAD_REPORT, //
		FAILED_TO_GENERATE_REPORT, //
		FAILED_TO_MAKE_CURRENT,//
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

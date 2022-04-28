/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

public class CloudOptimisationPushException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Type {
		FAILED_UNKNOWN_ERROR, //
		FAILED_NOT_PERMITTED, // User is not permitted to publish base case
		FAILED_SERVICE_LOCKED, // Service is locked by a different user
		FAILED_TO_MIGRATE, //
		FAILED_TO_EVALUATE, //
		EVALUATION_FAILED, //
		FAILED_TO_SAVE, //
		FAILED_TO_UPLOAD, //
		FAILED_UNSUPPORTED_VERSION, //
		FAILED_WRONG_CLIENT_CODE, //
		FAILED_TO_SAVE_ENCRYPTION_KEY, //
		FAILED_MISSING_KEY_UUID, //
		FAILED_GETTING_PUB_KEY, //
		FAILED_STATUS_CHECK, //
	};

	private final Type type;

	public Type getType() {
		return type;
	}

	public CloudOptimisationPushException(final String message, final Type type) {
		super(message);
		this.type = type;
	}

	public CloudOptimisationPushException(final String message, final Type type, final Exception e) {
		super(message, e);
		this.type = type;
	}
}
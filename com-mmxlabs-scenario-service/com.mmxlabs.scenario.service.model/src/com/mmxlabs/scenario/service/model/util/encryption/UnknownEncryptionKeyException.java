/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption;

public class UnknownEncryptionKeyException extends RuntimeException {

	private final byte[] key;

	public UnknownEncryptionKeyException(final Exception e, byte[] key) {
		super(e);
		this.key = key;
	}

	public UnknownEncryptionKeyException(String message, byte[] key) {
		super(message);
		this.key = key;
	}

	public byte[] getKey() {
		return key;
	}

}
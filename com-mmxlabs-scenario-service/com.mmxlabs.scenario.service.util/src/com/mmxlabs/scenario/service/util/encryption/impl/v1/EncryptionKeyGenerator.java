package com.mmxlabs.scenario.service.util.encryption.impl.v1;

import java.security.Key;

import javax.crypto.KeyGenerator;

final class EncryptionKeyGenerator {

	private static final String ENCRYPTION_KEY_ALGORITHM = "AES";

	public static Key generateKey(final int keysize) {
		try {
			final KeyGenerator keygen = KeyGenerator.getInstance(ENCRYPTION_KEY_ALGORITHM);
			keygen.init(keysize);
			return keygen.generateKey();
		} catch (final Exception ex) {
			// all implementations of Java 1.5 should support AES
			throw new RuntimeException(ex);
		}
	}
}

package com.mmxlabs.scenario.service.model.util.encryption.impl;

import java.io.InputStream;
import java.security.SecureRandom;

public class KeyFileUtil {

	public static final int UUID_LENGTH = 16;

	private KeyFileUtil() {

	}

	public static byte[] randomBytes(final int length) {
		final SecureRandom random = new SecureRandom();

		final byte[] bytes = new byte[length];
		random.nextBytes(bytes);

		return bytes;
	}

	public static byte[] readBytes(final int length, final InputStream in) throws Exception {
		final byte[] bytes = new byte[length];
		final int read = in.read(bytes);

		if (read != length) {
			throw new Exception("expected length != actual length");
		}

		return bytes;
	}
}

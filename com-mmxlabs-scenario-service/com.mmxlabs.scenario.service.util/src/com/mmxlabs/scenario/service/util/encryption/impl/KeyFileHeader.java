package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Small header data structure stored at the top of each key file containing the format version and a password type to indicate how to decrypt the contents.
 * 
 * @author Simon Goodall
 * 
 */
public final class KeyFileHeader {

	public static final byte PASSWORD_TYPE__PROMPT = (byte) 0;

	final byte version;
	final byte passwordType;

	public KeyFileHeader(final byte version, final byte passwordType) {
		this.version = version;
		this.passwordType = passwordType;
	}

	public static KeyFileHeader read(final InputStream is) throws IOException {
		final byte[] header = new byte[2];
		is.read(header);
		return new KeyFileHeader(header[0], header[1]);
	}

	public int write(final OutputStream os) throws IOException {
		byte[] header = new byte[] { version, passwordType };
		os.write(header);
		return header.length;
	}
}

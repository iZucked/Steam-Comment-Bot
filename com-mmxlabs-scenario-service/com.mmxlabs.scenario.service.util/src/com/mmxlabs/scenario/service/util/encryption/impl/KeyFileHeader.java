/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	/**
	 * Use default password built in to application
	 */
	public static final byte PASSWORD_TYPE__DEFAULT = (byte) 0;
	/**
	 * Prompt user for password
	 */
	public static final byte PASSWORD_TYPE__PROMPT = (byte) 1;

	public static final byte VERSION__0 = (byte) 0;

	final byte version;
	final byte passwordType;
	final String name;

	public KeyFileHeader(final byte version, final byte passwordType, final String name) {
		this.version = version;
		this.passwordType = passwordType;
		this.name = name;
	}

	public static KeyFileHeader read(final InputStream is) throws IOException {
		final byte[] header = new byte[2];
		final DataInputStream dis = new DataInputStream(is);
		dis.read(header);
		final String name = dis.readUTF();
		return new KeyFileHeader(header[0], header[1], name);
	}

	public int write(final OutputStream os) throws IOException {
		final byte[] header = new byte[] { version, passwordType };

		final DataOutputStream dos = new DataOutputStream(os);
		dos.write(header);
		dos.writeUTF(name);
		dos.flush();
		return header.length;
	}
}

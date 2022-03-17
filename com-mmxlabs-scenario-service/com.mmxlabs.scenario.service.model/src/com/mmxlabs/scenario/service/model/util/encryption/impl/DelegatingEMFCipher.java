/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;

import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;

/**
 * Based on {@link AESCipherImpl}
 * 
 * @author Simon Goodall
 * 
 */
public class DelegatingEMFCipher implements URIConverter.Cipher {

	static class ByteArrayKey {
		private final byte[] bytes;

		public ByteArrayKey(final byte[] bytes) {
			this.bytes = bytes;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;

			}
			if (obj instanceof final ByteArrayKey other) {
				return Arrays.equals(bytes, other.bytes);
			}

			return false;
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(bytes);
		}
	}

	private final Map<ByteArrayKey, IKeyFile> keystore = new HashMap<>();
	private IKeyFile defaultCipher;

	public List<byte[]> listKeys() {
		final List<byte[]> l = new LinkedList<>();
		for (final IKeyFile k : keystore.values()) {
			l.add(Arrays.copyOf(k.getKeyUUID(), k.getKeyUUID().length));
		}
		return l;
	}

	public IKeyFile getKeyFile(final byte[] key) {
		return keystore.get(new ByteArrayKey(key));
	}
	
	public byte[] getDefaultKey() {
		return Arrays.copyOf(defaultCipher.getKeyUUID(), defaultCipher.getKeyUUID().length);
	}

	public void addKeyFile(final IKeyFile keyFile, final boolean makeDefault) {
		if (makeDefault || defaultCipher == null) {
			defaultCipher = keyFile;
		}

		keystore.put(new ByteArrayKey(keyFile.getKeyUUID()), keyFile);
	}

	@Override
	public OutputStream encrypt(OutputStream outputStream) throws Exception {
		// Use a buffered outputstream to speed up write operations significantly
		outputStream = new BufferedOutputStream(outputStream);

		outputStream.write(defaultCipher.getKeyUUID());

		return new WrappedOutputStream(defaultCipher, defaultCipher.encrypt(outputStream));
	}

	@Override
	public void finish(final OutputStream out) throws Exception {
		if (out instanceof final WrappedOutputStream wos) {
			wos.finish();
		}
	}

	@Override
	public InputStream decrypt(final InputStream in) throws Exception {
		final byte[] keyFileUUID = KeyFileUtil.readBytes(KeyFileUtil.UUID_LENGTH, in);

		final IKeyFile keyfile = keystore.get(new ByteArrayKey(keyFileUUID));

		if (keyfile == null) {
			throw new ScenarioEncryptionException("Data was not encrypted with known decryption key");
		}
		return new WrappedInputStream(keyfile, keyfile.decrypt(in));
	}

	@Override
	public void finish(final InputStream in) throws Exception {
		if (in instanceof final WrappedInputStream wis) {
			wis.finish();
		}
	}

	private static class WrappedInputStream extends BufferedInputStream {

		private final IKeyFile keyfile;

		public WrappedInputStream(final IKeyFile keyfile, final InputStream in) {
			super(in);
			this.keyfile = keyfile;
		}

		public void finish() throws Exception {
			keyfile.finish(this);
		}
	}

	private static class WrappedOutputStream extends BufferedOutputStream {

		private final IKeyFile keyfile;

		public WrappedOutputStream(final IKeyFile keyfile, final OutputStream os) {
			super(os);
			this.keyfile = keyfile;
		}

		public void finish() throws Exception {
			keyfile.finish(this);
		}
	}
}

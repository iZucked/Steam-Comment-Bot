/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;

import com.mmxlabs.scenario.service.model.util.encryption.impl.IKeyFile;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;

/**
 * Based on {@link AESCipherImpl}
 * 
 * See https://docs.google.com/document/d/1EQzpr94x9sxxnZsW_1DclcvMhpHi061X-uET7S5AI4M/edit#heading=h.adz1phvucrbl
 * 
 * @author Simon Goodall
 * 
 */
public final class KeyFileV2 implements IKeyFile {

	public static final String KEYFILE_TYPE = "v2";

	public static final String ENCRYPTION_KEY_ALGORITHM = "AES";

	private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";

	private static final byte FLAG_ZIPPED = 0x1;

	// Save data compressed by default.
	final boolean useZip = true;

	private final Key key;

	/**
	 * UUID of the shared key itself.
	 */
	private final byte[] keyUUID;

	public KeyFileV2(byte[] keyUUID, final SecretKey key) {
		this.keyUUID = keyUUID;
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public byte[] getKeyUUID() {
		return keyUUID;
	}

	@Override
	public OutputStream encrypt(OutputStream outputStream) throws Exception {

		// Flags - currently unused
		byte flags = (byte) 0;
		if (useZip) {
			flags |= FLAG_ZIPPED;
		}
		outputStream.write(flags);
		// generate the IV for encryption
		final byte[] encryptionIV = KeyFileUtil.randomBytes(16);
		outputStream.write(encryptionIV);

		// create the encryption cipher
		GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
		cipher.init(true, new AEADParameters(new KeyParameter(key.getEncoded()), 128, encryptionIV, null));

		// The CipherOutputStream shouldn't close the underlying stream
		//
		outputStream = new FilterOutputStream(outputStream) {
			@Override
			public void close() throws IOException {
				// Do nothing
			}
		};
		final CipherOutputStream cos = new CipherOutputStream(outputStream, cipher);

		if (useZip) {
			final ZipOutputStream zipOutputStream = new ZipOutputStream(cos) {
				@Override
				public void finish() throws IOException {
					super.finish();
					def.end();
				}

				@Override
				public void flush() {
					// Do nothing.
				}

				@Override
				public void close() throws IOException {
					try {
						super.flush();
					} catch (final IOException exception) {
						// Continue and try to close.
					}
					super.close();
				}
			};
			zipOutputStream.putNextEntry(new ZipEntry("ResourceContents"));
			return zipOutputStream;
		}
		return cos;
	}

	@Override
	public void finish(final OutputStream out) throws Exception {
		out.close();
	}

	@Override
	public InputStream decrypt(InputStream in) throws Exception {

		// Flags - currently unused
		final byte[] flags = KeyFileUtil.readBytes(1, in);
		final byte[] encryptionIV = KeyFileUtil.readBytes(16, in);

		// create the decryption cipher
		GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
		cipher.init(false, new AEADParameters(new KeyParameter(key.getEncoded()), 128, encryptionIV, null));

		in = new CipherInputStream(in, cipher) {

			@Override
			public void close() throws IOException {
				try {
					// http://stackoverflow.com/questions/27124931/java-7-java-8-aes-causes-exception-badpaddingexception-given-final-block
					// https://bugs.openjdk.java.net/browse/JDK-8061619
					super.close();
				} catch (final Exception e) {
					// ignore IllegalBlockSizeExceptions
				}
			}
		};

		if ((flags[0] & FLAG_ZIPPED) == FLAG_ZIPPED) {
			final ZipInputStream zipInputStream = new ZipInputStream(in);
			while (zipInputStream.available() != 0) {
				@SuppressWarnings("unused")
				final ZipEntry zipEntry = zipInputStream.getNextEntry();
				in = zipInputStream;
				break;
			}
		}

		return in;
	}

	@Override
	public void finish(final InputStream in) throws Exception {
		// Do nothing
	}

	public static SecretKey generateKey(final int keysize) {
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

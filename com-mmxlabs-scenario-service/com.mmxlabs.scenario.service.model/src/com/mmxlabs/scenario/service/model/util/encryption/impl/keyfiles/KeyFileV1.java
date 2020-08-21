/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

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
public final class KeyFileV1 implements IKeyFile {

	public static final String KEYFILE_TYPE = "v1";
	
	// Key params
	private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final byte FLAG_ZIPPED = 0x1;

	// Save data compressed by default.
	final boolean useZip = true;

	private final Key key;
	/**
	 * UUID of the shared key itself.
	 */
	private final byte[] keyUUID;

	public KeyFileV1(final byte[] keyUUID, final Key key) {
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

		// now create the encryption cipher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getKey(), new IvParameterSpec(encryptionIV));

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

		// now create the decrypt cipher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getKey(), new IvParameterSpec(encryptionIV));
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
}
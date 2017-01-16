/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl.v1;

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;

import com.mmxlabs.scenario.service.util.encryption.ScenarioEncryptionException;

/**
 * Based on {@link AESCipherImpl}
 * 
 * @author Simon Goodall
 * 
 */
class EMFCipher implements URIConverter.Cipher {
	private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";

	private static final byte FLAG_ZIPPED = 0x1;
	
	private final KeyFile keyFile;

	// Save data compressed by default.
	final boolean useZip = true;

	public EMFCipher(final KeyFile keyFile) {
		this.keyFile = keyFile;
	}

	private static byte[] randomBytes(final int length) {
		final SecureRandom random = new SecureRandom();

		final byte[] bytes = new byte[length];
		random.nextBytes(bytes);

		return bytes;
	}

	private static byte[] readBytes(final int length, final InputStream in) throws Exception {
		final byte[] bytes = new byte[length];
		final int read = in.read(bytes);

		if (read != length) {
			throw new Exception("expected length != actual length");
		}

		return bytes;
	}

	@Override
	public OutputStream encrypt(OutputStream outputStream) throws Exception {
		// Use a buffered outputstream to speed up write operations significantly
		outputStream = new BufferedOutputStream(outputStream);
		outputStream.write(keyFile.getKeyUUID());
		// Flags - currently unused
		byte flags = (byte)0;
		if (useZip) {
			flags |= FLAG_ZIPPED;
		}
		outputStream.write(flags);
		// generate the IV for encryption
		final byte[] encryptionIV = randomBytes(16);
		outputStream.write(encryptionIV);

		// now create the encryption cipher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, keyFile.getKey(), new IvParameterSpec(encryptionIV));

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
		final byte[] uuid = keyFile.getKeyUUID();
		final byte[] fileUUID = readBytes(uuid.length, in);
		if (!Arrays.equals(uuid, fileUUID)) {
			throw new ScenarioEncryptionException("Data was not encrypted with decryption key file");
		}
		// Flags - currently unused
		final byte[] flags = readBytes(1, in);
		final byte[] encryptionIV = readBytes(16, in);

		// now create the decrypt cipher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keyFile.getKey(), new IvParameterSpec(encryptionIV));
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

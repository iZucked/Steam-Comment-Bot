/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

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
public final class KeyFileLegacy implements IKeyFile {
	// Encrypted key params
	private static final String ENCRYPTION_KEY_ALGORITHM = "AES";
	private static final String PBE_ALGORITHM = "PBEWithMD5AndDES";
	private static final int PBE_IV_LENGTH = 8;
	private static final int PBE_ITERATIONS = 1000;

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

	private final int keySize;

	private KeyFileLegacy(final byte[] keyUUID, final Key key, final int keySize) {
		this.keyUUID = keyUUID;
		this.key = key;
		this.keySize = keySize;
	}

	private static byte[] transformWithPassword(final byte[] bytes, final byte[] iv, final char[] password, final byte[] salt, int keysize, final int mode) throws Exception {
		// generate the key
		final PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, PBE_ITERATIONS, keysize);
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
		final SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
		final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(iv, PBE_ITERATIONS);

		// encrypt the input
		final Cipher keyCipher = Cipher.getInstance(PBE_ALGORITHM);
		keyCipher.init(mode, pbeKey, pbeParamSpec);
		return keyCipher.doFinal(bytes);
	}

	public void save(final OutputStream os, final char[] password) throws Exception {
		final DataOutputStream outputStream = new DataOutputStream(os);
		// Write the UUID of this key
		outputStream.write(keyUUID);

		// create the IV for the password generation algorithm
		final byte[] pbeIV = KeyFileUtil.randomBytes(PBE_IV_LENGTH);
		final byte[] salt = KeyFileUtil.randomBytes(8);

		// turn the password into an AES key
		final byte[] encryptedKeyBytes = transformWithPassword(key.getEncoded(), pbeIV, password, salt, keySize, Cipher.ENCRYPT_MODE);

		// Write data to the keyfile
		outputStream.write(pbeIV);
		outputStream.write(encryptedKeyBytes.length);
		outputStream.write(encryptedKeyBytes);
		outputStream.write(salt);
		outputStream.writeInt(keySize);
	}

	public static KeyFileLegacy load(final InputStream in, final char[] password) throws Exception {

		final DataInputStream dis = new DataInputStream(in);

		final byte[] fileUUID = KeyFileUtil.readBytes(KeyFileUtil.UUID_LENGTH, dis);

		// Read the header of the encrypted file.
		final byte[] pbeIV = KeyFileUtil.readBytes(PBE_IV_LENGTH, dis);
		final int keyLength = dis.read();
		final byte[] encryptedKeyBytes = KeyFileUtil.readBytes(keyLength, dis);
		final byte[] salt = KeyFileUtil.readBytes(8, dis);
		final int keysize = dis.readInt();

		// Decrypt the key bytes
		final byte[] decryptedKeyBytes = transformWithPassword(encryptedKeyBytes, pbeIV, password, salt, keysize, Cipher.DECRYPT_MODE);

		// Create the key from the key bytes
		final Key key = new SecretKeySpec(decryptedKeyBytes, ENCRYPTION_KEY_ALGORITHM);

		return new KeyFileLegacy(fileUUID, key, keysize);
	}

	public static KeyFileLegacy generateKey(final int keysize) {
		try {
			final KeyGenerator keygen = KeyGenerator.getInstance(ENCRYPTION_KEY_ALGORITHM);
			keygen.init(keysize);
			final byte[] keyUUID = new byte[KeyFileUtil.UUID_LENGTH];
			EcoreUtil.generateUUID(keyUUID);
			final SecretKey generatedKey = keygen.generateKey();
			return new KeyFileLegacy(keyUUID, generatedKey, keysize);
		} catch (final Exception ex) {
			// all implementations of Java 1.5 should support AES
			throw new RuntimeException(ex);
		}
	}

	public Key getKey() {
		return key;
	}

	public byte[] getKeyUUID() {
		return keyUUID;
	}

	public int getKeySize() {
		return keySize;
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

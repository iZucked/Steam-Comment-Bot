/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl.v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.scenario.service.util.encryption.impl.IKeyFile;

/**
 * Based on {@link AESCipherImpl}
 * 
 * @author Simon Goodall
 * 
 */
public final class KeyFile implements IKeyFile {
	private static final int UUID_LENGTH = 16;
	private static final String ENCRYPTION_KEY_ALGORITHM = "AES";
	private static final String PBE_ALGORITHM = "PBEWithMD5AndDES";
	private static final int PBE_IV_LENGTH = 8;
	private static final int PBE_ITERATIONS = 1000;

	private final Key key;
	/**
	 * UUID of the shared key itself.
	 */
	private final byte[] keyUUID;

	private final int keySize;

	private KeyFile(final byte[] keyUUID, final Key key, final int keySize) {
		this.keyUUID = keyUUID;
		this.key = key;
		this.keySize = keySize;
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

	@Override
	public URIConverter.Cipher createCipher() {
		return new EMFCipher(this);
	}

	public void save(final OutputStream os, final char[] password) throws Exception {
		final DataOutputStream outputStream = new DataOutputStream(os);
		// Write the UUID of this key
		outputStream.write(keyUUID);

		// create the IV for the password generation algorithm
		final byte[] pbeIV = randomBytes(PBE_IV_LENGTH);
		final byte[] salt = randomBytes(8);

		// turn the password into an AES key
		final byte[] encryptedKeyBytes = transformWithPassword(key.getEncoded(), pbeIV, password, salt, keySize, Cipher.ENCRYPT_MODE);

		// Write data to the keyfile
		outputStream.write(pbeIV);
		outputStream.write(encryptedKeyBytes.length);
		outputStream.write(encryptedKeyBytes);
		outputStream.write(salt);
		outputStream.writeInt(keySize);
	}

	public static KeyFile load(final InputStream in, final char[] password) throws Exception {

		final DataInputStream dis = new DataInputStream(in);

		final byte[] fileUUID = readBytes(UUID_LENGTH, dis);

		// Read the header of the encrypted file.
		final byte[] pbeIV = readBytes(PBE_IV_LENGTH, dis);
		final int keyLength = dis.read();
		final byte[] encryptedKeyBytes = readBytes(keyLength, dis);
		final byte[] salt = readBytes(8, dis);
		final int keysize = dis.readInt();

		// Decrypt the key bytes
		final byte[] decryptedKeyBytes = transformWithPassword(encryptedKeyBytes, pbeIV, password, salt, keysize, Cipher.DECRYPT_MODE);

		// Create the key from the key bytes
		final Key key = new SecretKeySpec(decryptedKeyBytes, ENCRYPTION_KEY_ALGORITHM);

		final KeyFile keyfile = new KeyFile(fileUUID, key, keysize);

		return keyfile;
	}

	public static KeyFile generateKey(final int keysize) {
		try {
			final KeyGenerator keygen = KeyGenerator.getInstance(ENCRYPTION_KEY_ALGORITHM);
			keygen.init(keysize);
			final byte[] keyUUID = new byte[UUID_LENGTH];
			EcoreUtil.generateUUID(keyUUID);
			final SecretKey generatedKey = keygen.generateKey();
			return new KeyFile(keyUUID, generatedKey, keysize);
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
}

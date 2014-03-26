package com.mmxlabs.scenario.service.util.encryption.impl.v1;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.emf.ecore.resource.URIConverter;

import com.mmxlabs.scenario.service.util.encryption.impl.IKeyFile;

public final class KeyFile implements IKeyFile {
	private static final String ENCRYPTION_KEY_ALGORITHM = "AES";
	private static final String PBE_ALGORITHM = "PBEWithMD5AndDES";
	private static final int PBE_IV_LENGTH = 8;
	private static final int PBE_ITERATIONS = 1000;

	private final Key key;
	/**
	 * UUID of the shared key itself.
	 */
	private final String keyUUID;
	/**
	 * UUID if this generated instance of a key file
	 */
	private final String keyFileUUID;

	private KeyFile(String keyUUID, String keyFileUUID, Key key) {
		this.keyUUID = keyUUID;
		this.keyFileUUID = keyFileUUID;
		this.key = key;
	}

	private static byte[] randomBytes(final int length) {
		SecureRandom random = new SecureRandom();

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

	private static byte[] transformWithPassword(final byte[] bytes, final byte[] iv, final char[] password, final int mode) throws Exception {
		// generate the key
		final PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
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
		return new EMFCipher(key);
	}

	public void save(final OutputStream outputStream, final char[] password) throws Exception {

		// create the IV for the password generation algorithm
		byte[] pbeIV = randomBytes(PBE_IV_LENGTH);

		// turn the password into an AES key
		byte[] encryptedKeyBytes = transformWithPassword(key.getEncoded(), pbeIV, password, Cipher.ENCRYPT_MODE);

		// write the header to the output stream. this has the format
		// (delimeters are not written):
		// PBE IV|ENCRYPTION IV|ENCRYPTED KEY LENGTH|ENCRYPTED KEY
		outputStream.write(pbeIV);
		outputStream.write(encryptedKeyBytes.length);
		outputStream.write(encryptedKeyBytes);
	}

	public static KeyFile load(final InputStream in, final char[] password) throws Exception {
		// Read the header of the encrypted file.
		final byte[] pbeIV = readBytes(PBE_IV_LENGTH, in);
		final int keyLength = in.read();
		final byte[] encryptedKeyBytes = readBytes(keyLength, in);

		// Decrypt the key bytes
		final byte[] decryptedKeyBytes = transformWithPassword(encryptedKeyBytes, pbeIV, password, Cipher.DECRYPT_MODE);

		// Create the key from the key bytes
		final Key key = new SecretKeySpec(decryptedKeyBytes, ENCRYPTION_KEY_ALGORITHM);

		// If we haven't yet generated a key, just use this one
		final KeyFile keyfile = new KeyFile(null, null, key);

		return keyfile;
	}
}

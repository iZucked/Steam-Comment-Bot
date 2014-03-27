package com.mmxlabs.scenario.service.util.encryption.impl.v1;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;

/**
 * Based on {@link AESCipherImpl}
 * 
 * @author Simon Goodall
 * 
 */
class EMFCipher implements URIConverter.Cipher {
	private static final String ENCRYPTION_ALGORITHM = "AES/CFB8/PKCS5Padding";
	private static final int ENCRYPTION_IV_LENGTH = 16;

	private final Key key;
	private byte[] uuid;

	public EMFCipher(final Key key, byte[] uuid) {
		this.key = key;
		this.uuid = uuid;
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
		outputStream.write(uuid);
		// generate the IV for encryption
		final byte[] encryptionIV = randomBytes(ENCRYPTION_IV_LENGTH);
		outputStream.write(encryptionIV);

		// now create the encryption cipher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(encryptionIV));

		// The CipherOutputStream shouldn't close the underlying stream
		//
		outputStream = new FilterOutputStream(outputStream) {
			@Override
			public void close() throws IOException {
				// Do nothing
			}
		};
		return new CipherOutputStream(outputStream, cipher);
	}

	@Override
	public void finish(final OutputStream out) throws Exception {
		out.close();
	}

	@Override
	public InputStream decrypt(final InputStream in) throws Exception {
		final byte[] fileUUID = readBytes(uuid.length, in);
		if (!uuid.equals(fileUUID)) {
			throw new RuntimeException("Data was not encrypted with decryption key file");
		}

		final byte[] encryptionIV = readBytes(ENCRYPTION_IV_LENGTH, in);

		// now create the decrypt cipher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(encryptionIV));
		return new CipherInputStream(in, cipher);
	}

	@Override
	public void finish(final InputStream in) throws Exception {
		// Do nothing
	}
}

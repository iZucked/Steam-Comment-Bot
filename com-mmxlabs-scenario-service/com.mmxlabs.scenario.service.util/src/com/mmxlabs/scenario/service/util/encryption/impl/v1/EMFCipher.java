package com.mmxlabs.scenario.service.util.encryption.impl.v1;

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Arrays;

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
	private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";

	private final KeyFile keyFile;

	boolean useZip = false;

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
		outputStream.write((byte) 0);
		// generate the IV for encryption
		final byte[] encryptionIV = randomBytes(keyFile.getKeySize() / 8);
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
		return new CipherOutputStream(outputStream, cipher);

		// if (useZip) {
		// ZipOutputStream zipOutputStream = new ZipOutputStream(cos) {
		// @Override
		// public void finish() throws IOException {
		// super.finish();
		// def.end();
		// }
		//
		// @Override
		// public void flush() {
		// // Do nothing.
		// }
		//
		// @Override
		// public void close() throws IOException {
		// try {
		// super.flush();
		// } catch (IOException exception) {
		// // Continue and try to close.
		// }
		// super.close();
		// }
		// };
		// zipOutputStream.putNextEntry(new ZipEntry("ResourceContents"));
		// return zipOutputStream;
		// }
	}

	@Override
	public void finish(final OutputStream out) throws Exception {
		out.close();
	}

	@Override
	public InputStream decrypt(final InputStream in) throws Exception {
		byte[] uuid = keyFile.getKeyUUID();
		final byte[] fileUUID = readBytes(uuid.length, in);
		if (!Arrays.equals(uuid, fileUUID)) {
			throw new RuntimeException("Data was not encrypted with decryption key file");
		}
		// Flags - currently unused
		byte[] flags = readBytes(1, in);
		final byte[] encryptionIV = readBytes(keyFile.getKeySize() / 8, in);

		// if (useZip) {
		// ZipInputStream zipInputStream = new ZipInputStream(in);
		// while (zipInputStream.available() != 0) {
		// ZipEntry zipEntry = zipInputStream.getNextEntry();
		// // if (isContentZipEntry(zipEntry)) {
		// in = zipInputStream;
		// break;
		// // }
		// }
		// }
		// now create the decrypt cipher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keyFile.getKey(), new IvParameterSpec(encryptionIV));
		return new CipherInputStream(in, cipher);
	}

	@Override
	public void finish(final InputStream in) throws Exception {
		// Do nothing
	}
}

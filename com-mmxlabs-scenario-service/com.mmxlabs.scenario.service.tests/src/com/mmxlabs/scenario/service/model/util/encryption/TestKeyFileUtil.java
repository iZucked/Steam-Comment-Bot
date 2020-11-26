package com.mmxlabs.scenario.service.model.util.encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileLoader;

public class TestKeyFileUtil {

	private final File oldKeyStore;
	private final File newKeyStore;

	public TestKeyFileUtil() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		oldKeyStore = File.createTempFile("keystore", ".pks");
		newKeyStore = File.createTempFile("keystore", ".pks");

		generateTestKeyStore(oldKeyStore, newKeyStore);
	}

	public DelegatingEMFCipher loadOldKeyStore() throws Exception {
		return KeyFileLoader.loadCipher(oldKeyStore);
	}

	public DelegatingEMFCipher loadNewKeyStore() throws Exception {
		return KeyFileLoader.loadCipher(newKeyStore);
	}

	void generateTestKeyStore(final File keystoreOld, final File keystoreNew) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {

		KeyFileLoader.initalise(keystoreOld);

		KeyFileLoader.generate(keystoreOld.getAbsolutePath());
		try {
			Thread.sleep(1_000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Files.copy(keystoreOld, keystoreNew);
try {
	Thread.sleep(1_000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		KeyFileLoader.generate(keystoreNew.getAbsolutePath());
	}

	public static byte[] encrypt(final Cipher cipher, final byte[] source) throws Exception {

		try (ByteArrayInputStream bis = new ByteArrayInputStream(source)) {
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				try (OutputStream eos = cipher.encrypt(outputStream)) {
					ByteStreams.copy(bis, eos);
				}
				return outputStream.toByteArray();
			}
		}
	}

	public static byte[] decrypt(final Cipher cipher, final byte[] source) throws Exception {

		try (ByteArrayInputStream bis = new ByteArrayInputStream(source)) {
			try (InputStream eos = cipher.decrypt(bis)) {
				try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
					ByteStreams.copy(eos, outputStream);

					outputStream.flush();
					return outputStream.toByteArray();
				}
			}
		}
	}
}

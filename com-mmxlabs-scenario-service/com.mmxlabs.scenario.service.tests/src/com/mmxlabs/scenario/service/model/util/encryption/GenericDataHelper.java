/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Assertions;

import com.google.common.io.Files;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;

public class GenericDataHelper {

	private File path;

	public GenericDataHelper(File path) {
		this.path = path;
	}

	public static class GDEntry {
		public byte[] rawData;
		public byte[] encData;
		public String type;
		public String filename;
		public File file;

	}

	private byte[] createRandomData(final int size) {

		final byte[] data = new byte[size];
		new Random().nextBytes(data);

		return data;
	}

	public GDEntry createRecord(String type, String filename, int size, DelegatingEMFCipher cipher) throws Exception {

		File typePath = new File(path + File.separator + type);
		typePath.mkdirs();

		final GDEntry record = new GDEntry();
		record.rawData = createRandomData(size);
		record.type = type;
		record.filename = filename;
		record.file = new File(typePath.getAbsolutePath() + File.separator + filename);

		record.encData = TestKeyFileUtil.encrypt(cipher, record.rawData);

		Files.write(record.encData, record.file);

		return record;
	}

	public static void check(GDEntry record, DelegatingEMFCipher cipher) throws Exception {

		byte[] newContent = Files.asByteSource(record.file).read();
		byte[] decryptedData = TestKeyFileUtil.decrypt(cipher, newContent);

		// Ensure decrypted data is identical
		Assertions.assertTrue(Arrays.equals(record.rawData, decryptedData));
		// Ensure encrypted data is different
		Assertions.assertFalse(Arrays.equals(record.encData, newContent));
	}
}

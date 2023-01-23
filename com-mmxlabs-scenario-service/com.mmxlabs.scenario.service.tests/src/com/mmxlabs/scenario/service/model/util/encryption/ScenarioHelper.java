/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Assertions;

import com.google.common.io.ByteStreams;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;

public class ScenarioHelper {

	private final File path;

	public ScenarioHelper(final File path) {
		this.path = path;
	}

	public static class SSEntry {
		public byte[] rawRootObjectData;
		public byte[] rawManifestData;

		public byte[] encRootObjectData;
		public byte[] encManifestData;
		public String filename;
		public File file;
		public File file2;
	}

	private byte[] createRandomData(final int size) {

		final byte[] data = new byte[size];
		new Random().nextBytes(data);

		return data;
	}

	public SSEntry createRecord(final String filename, final int size, final DelegatingEMFCipher cipher) throws Exception {

		path.mkdirs();

		final SSEntry record = new SSEntry();
		record.rawManifestData = createRandomData(2048);
		record.rawRootObjectData = createRandomData(size);
		record.filename = filename;
		record.file = new File(path.getAbsolutePath() + File.separator + filename);

		record.encManifestData = TestKeyFileUtil.encrypt(cipher, record.rawManifestData);
		record.encRootObjectData = TestKeyFileUtil.encrypt(cipher, record.rawRootObjectData);

		try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(record.file))) {
			{
				final ZipEntry e = new ZipEntry("MANIFEST.xmi");
				out.putNextEntry(e);
				out.write(record.encManifestData);
				out.closeEntry();
			}
			{
				final ZipEntry e = new ZipEntry("rootObject.xmi");
				out.putNextEntry(e);
				out.write(record.encRootObjectData);
				out.closeEntry();
			}
		}

		return record;
	}

	public static class LoadedData {
		public byte[] encRootObjectData;
		public byte[] encManifestData;
	}

	public static LoadedData getData(final SSEntry record) throws Exception {

		final LoadedData d = new LoadedData();

		try (ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(record.file)))) {

			ZipEntry zipInEntry = zipIn.getNextEntry();

			while (zipInEntry != null) {
				if ("MANIFEST.xmi".contentEquals(zipInEntry.getName())) {
					d.encManifestData = ByteStreams.toByteArray(zipIn);
				} else if ("rootObject.xmi".contentEquals(zipInEntry.getName())) {
					d.encRootObjectData = ByteStreams.toByteArray(zipIn);
				}
				zipIn.closeEntry();
				zipInEntry = zipIn.getNextEntry();
			}
		} catch (final Exception e1) {
			e1.printStackTrace();
		}

		return d;

	}

	public static byte[] getKeyID(InputStream in) throws Exception {
		return KeyFileUtil.readBytes(KeyFileUtil.UUID_LENGTH, in);
	}

	public static void check(SSEntry record, DelegatingEMFCipher cipher) throws Exception {

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(record.file))) {
			ZipEntry nextEntry = zis.getNextEntry();
			while (nextEntry != null) {
				// System.out.println(nextEntry.getName());
				byte[] id = getKeyID(zis);
				StringBuilder sb = new StringBuilder();
				for (byte b : id) {
					sb.append(String.format("%02X ", b));
				}
				String key = sb.toString();
				// System.out.println("Check  " + key);
				nextEntry = zis.getNextEntry();

			}
		}

		LoadedData data = getData(record);

		byte[] decryptedManifest = TestKeyFileUtil.decrypt(cipher, data.encManifestData);
		byte[] decryptedRootObject = TestKeyFileUtil.decrypt(cipher, data.encRootObjectData);

		String ma = new String(record.rawManifestData);
		String ra = new String(record.rawRootObjectData);

		String m = new String(decryptedManifest);
		String r = new String(decryptedRootObject);

		// Ensure decrypted data is identical
		Assertions.assertTrue(Arrays.equals(record.rawManifestData, decryptedManifest));
		Assertions.assertTrue(Arrays.equals(record.rawRootObjectData, decryptedRootObject));
		// Ensure encrypted data is different
		Assertions.assertFalse(Arrays.equals(record.encRootObjectData, data.encRootObjectData));
		Assertions.assertFalse(Arrays.equals(record.encManifestData, data.encManifestData));
	}

}

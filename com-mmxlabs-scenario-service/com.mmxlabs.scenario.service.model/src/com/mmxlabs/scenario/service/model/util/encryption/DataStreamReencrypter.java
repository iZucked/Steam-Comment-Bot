/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;

import com.google.common.io.ByteStreams;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;

public class DataStreamReencrypter {

	// Temporary flag to enable or disable this code before making it live
	public static boolean ENABLED = false;

	public static final String GENERIC_DATA_KEY = "content";

	/**
	 * "Magic" bytes at the start of a zip file.
	 */
	private static final byte[] MAGIC_ZIP_HEADER = { 0x50, 0x4B, 0x03, 0x04 };

	private static class NonClosingInputStream extends FilterInputStream {
		protected NonClosingInputStream(final InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
			// Do not close the stream

		}
	}

	private static class NonClosingOutputStream extends FilterOutputStream {
		protected NonClosingOutputStream(final OutputStream out) {
			super(out);
		}

		@Override
		public void close() throws IOException {
			// Do not close the stream.
			flush();
		}
	}

	/**
	 * Copies an {@link InputStream} of scenario data to the output stream through a re-encryption step. Returns a map of the decrypted digests for each entry in the zip file.
	 * 
	 * @param cipher
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> reencryptScenario(final DelegatingEMFCipher cipher, final InputStream in, final OutputStream out) throws Exception {

		final Map<String, String> digests = new HashMap<>();

		// LiNGO scenario is a zip file, so we need to unzip and decrypt.
		try (ZipOutputStream zipOut = new ZipOutputStream(out)) {
			try (ZipInputStream zipIn = new ZipInputStream(in)) {

				ZipEntry zipInEntry = zipIn.getNextEntry();
				final NonClosingInputStream filterIn = new NonClosingInputStream(zipIn);

				while (zipInEntry != null) {
					final ZipEntry zipOutEntry = new ZipEntry(zipInEntry);

					zipOut.putNextEntry(zipOutEntry);

					final NonClosingOutputStream filterOut = new NonClosingOutputStream(zipOut);
					final MessageDigest digest = MessageDigest.getInstance("SHA-256");

					try (PushbackInputStream pin = new PushbackInputStream(filterIn, KeyFileUtil.UUID_LENGTH)) {
						// Does this stream need to be re-ecrypted or should we just pass through?
						if (!needToReencryptStream(cipher, pin)) {
							try (DigestInputStream dis = new DigestInputStream(pin, digest)) {
								ByteStreams.copy(dis, filterOut);
							}
						} else {
							try (InputStream is = cipher.decrypt(pin)) {
								try (DigestInputStream dis = new DigestInputStream(is, digest)) {
									try (OutputStream os = cipher.encrypt(filterOut)) {
										ByteStreams.copy(dis, os);
									}
								}
							}
						}
					}
					zipOut.closeEntry();

					zipIn.closeEntry();

					// Record digest based on entry name
					digests.put(zipInEntry.getName(), digestToString(digest));

					zipInEntry = zipIn.getNextEntry();
				}
			}
		}
		return digests;
	}

	/**
	 * Alternaive to {@link #reencryptScenario(DelegatingEMFCipher, InputStream, OutputStream)} storing entire contents in a in-memory byte array. This avoidings overriding the *Stream close methods
	 * and PushbackInputStream use.
	 * 
	 * @param cipher
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> reencryptScenarioAlternative(final DelegatingEMFCipher cipher, final InputStream in, final OutputStream out) throws Exception {

		final Map<String, String> digests = new HashMap<>();

		// LiNGO scenario is a zip file, so we need to unzip and decrypt.
		try (ZipOutputStream zipOut = new ZipOutputStream(out)) {
			try (ZipInputStream zipIn = new ZipInputStream(in)) {

				ZipEntry zipInEntry = zipIn.getNextEntry();

				while (zipInEntry != null) {
					final ZipEntry zipOutEntry = new ZipEntry(zipInEntry);

					zipOut.putNextEntry(zipOutEntry);

					byte[] data;

					try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
						ByteStreams.copy(zipIn, bytesOut);
						data = bytesOut.toByteArray();
					}

					final byte[] header = Arrays.copyOf(data, KeyFileUtil.UUID_LENGTH);

					if (!needToReencryptStream(cipher, header)) {
						try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(data)) {
							ByteStreams.copy(bytesIn, zipOut);
						}
					} else {
						final MessageDigest digest = MessageDigest.getInstance("SHA-256");
						byte[] newData;
						try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
							try (InputStream is = cipher.decrypt(bis)) {
								try (DigestInputStream dis = new DigestInputStream(is, digest)) {

									try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
										try (OutputStream os = cipher.encrypt(bytesOut)) {
											ByteStreams.copy(dis, os);

											cipher.finish(is);
											cipher.finish(os);

											newData = bytesOut.toByteArray();
										}
									}
								}
							}
						}
						try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(newData)) {
							ByteStreams.copy(bytesIn, zipOut);
						}
						digests.put(zipInEntry.getName(), digestToString(digest));
					}

					zipOut.closeEntry();

					zipIn.closeEntry();

					zipInEntry = zipIn.getNextEntry();
				}
			}

		}
		return digests;
	}

	/**
	 * Returns true if the header bytes match a known key and are not for the current key.
	 * 
	 * @param cipher
	 * @param pin
	 * @return
	 * @throws IOException
	 */
	private boolean needToReencryptStream(final DelegatingEMFCipher cipher, final PushbackInputStream pin) throws IOException {
		// Read header bytes representing a key id
		final byte[] bytes = new byte[KeyFileUtil.UUID_LENGTH];
		final int read = pin.read(bytes);

		// Check for a valid read value
		if (read <= 0) {
			return false;
		}

		// Push bytes back into stream
		pin.unread(bytes, 0, read);

		if (read != KeyFileUtil.UUID_LENGTH) {
			// Input stream is not big enough, not encrypted at all
			return false;
		}

		if (Arrays.equals(cipher.getDefaultKey(), bytes)) {
			// Key ID matches, no need to re-encrypt
			return false;
		}
		// Is it a valid key?
		for (final byte[] key : cipher.listKeys()) {
			if (Arrays.equals(key, bytes)) {
				// Known Key ID match, need to re-encrypt
				return true;
			}
		}

		// Not encrypted, or encrypted with unknown key
		return false;
	}

	private boolean needToReencryptStream(final DelegatingEMFCipher cipher, final byte[] bytes) throws IOException {

		if (bytes.length != KeyFileUtil.UUID_LENGTH) {
			// Input stream is not big enough, not encrypted at all
			return false;
		}

		if (Arrays.equals(cipher.getDefaultKey(), bytes)) {
			// Key ID matches, no need to re-encrypt
			return false;
		}

		// Is it a valid key?
		for (final byte[] key : cipher.listKeys()) {
			if (Arrays.equals(key, bytes)) {
				// Known Key ID match, need to re-encrypt
				return true;
			}
		}

		// Not encrypted, or encrypted with unknown key
		return false;
	}

	/**
	 * Copies an {@link InputStream} of scenario data to the output stream through a re-encryption step. Returns a map of the decrypted digests for each entry in the zip file.
	 * 
	 * @param cipher
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	private String reencryptData(final DelegatingEMFCipher cipher, final InputStream in, final OutputStream out) throws Exception {

		final MessageDigest digest = MessageDigest.getInstance("SHA-256");

		try (PushbackInputStream pin = new PushbackInputStream(in, KeyFileUtil.UUID_LENGTH)) {
			if (!needToReencryptStream(cipher, pin)) {
				// Data can just be copied across
				try (DigestInputStream dis = new DigestInputStream(pin, digest)) {
					ByteStreams.copy(dis, out);
				}
				return digestToString(digest);
			} else {
				// Data needs to be decrypted then encrypted
				try (InputStream is = cipher.decrypt(pin)) {
					try (DigestInputStream dis = new DigestInputStream(is, digest)) {
						try (OutputStream os = cipher.encrypt(out)) {
							ByteStreams.copy(dis, os);
						}
					}
				}
				return digestToString(digest);
			}
		}
	}

	/**
	 * Convert a byte array to a human readable string
	 * 
	 * @param digest
	 * @return
	 */
	private static String digestToString(final MessageDigest digest) {
		final StringBuilder sb = new StringBuilder();
		for (final byte b : digest.digest()) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	/**
	 * Try to detect whether the stream is a scenario or generic data stream. A generic data stream will return the digest for an entry named "content".
	 * 
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> reencryptStream(final InputStream in, final OutputStream out) throws Exception {

		if (!ENABLED) {
			ByteStreams.copy(in, out);
			return new HashMap<>();
		}

		final PushbackInputStream pin = new PushbackInputStream(in, MAGIC_ZIP_HEADER.length);
		// Read the header bytes...
		final byte[] header = new byte[MAGIC_ZIP_HEADER.length];
		final int read = pin.read(header);
		// .. then put them back in the stream
		pin.unread(header, 0, read);

		// If they match the zip header bytes, then assume a scenario
		if (Arrays.equals(MAGIC_ZIP_HEADER, header)) {
			return reencryptScenario(pin, out);
		} else {
			final Map<String, String> m = new HashMap<>();
			m.put(GENERIC_DATA_KEY, reencryptData(pin, out));
			return m;
		}
	}

	public static Map<String, String> reencryptScenario(final InputStream in, final OutputStream out) throws Exception {
		if (!ENABLED) {
			ByteStreams.copy(in, out);
			return new HashMap<>();
		}

		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			if (scenarioCipherProvider != null) {
				final Cipher sharedCipher = scenarioCipherProvider.getSharedCipher();
				if (sharedCipher instanceof DelegatingEMFCipher) {
					final DelegatingEMFCipher delegatingEMFCipher = (DelegatingEMFCipher) sharedCipher;
					return new DataStreamReencrypter().reencryptScenario(delegatingEMFCipher, in, out);
				}
			}
			ByteStreams.copy(in, out);
			return new HashMap<>();
		});
	}

	public static Map<String, String> reencryptScenario(final InputStream in, final OutputStream out, final Cipher cipher) throws Exception {
		if (!ENABLED) {
			ByteStreams.copy(in, out);
			return new HashMap<>();
		}

		if (cipher instanceof DelegatingEMFCipher) {
			final DelegatingEMFCipher delegatingEMFCipher = (DelegatingEMFCipher) cipher;
			return new DataStreamReencrypter().reencryptScenario(delegatingEMFCipher, in, out);
		}
		ByteStreams.copy(in, out);
		return new HashMap<>();

	}

	public static String reencryptData(final InputStream in, final OutputStream out) throws Exception {
		if (!ENABLED) {
			ByteStreams.copy(in, out);
			return "";
		}

		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			if (scenarioCipherProvider != null) {
				final Cipher sharedCipher = scenarioCipherProvider.getSharedCipher();
				if (sharedCipher instanceof DelegatingEMFCipher) {
					final DelegatingEMFCipher delegatingEMFCipher = (DelegatingEMFCipher) sharedCipher;
					return new DataStreamReencrypter().reencryptData(delegatingEMFCipher, in, out);
				}
			}
			ByteStreams.copy(in, out);
			return "";
		});
	}

	public static String reencryptData(final InputStream in, final OutputStream out, final Cipher cipher) throws Exception {
		if (!ENABLED) {
			ByteStreams.copy(in, out);
			return "";
		}

		if (cipher instanceof DelegatingEMFCipher) {
			final DelegatingEMFCipher delegatingEMFCipher = (DelegatingEMFCipher) cipher;
			return new DataStreamReencrypter().reencryptData(delegatingEMFCipher, in, out);
		}
		ByteStreams.copy(in, out);
		return "";
	}

	public Map<String, String> getScenarioDigests(final DelegatingEMFCipher cipher, final InputStream in) throws Exception {

		final Map<String, String> digests = new HashMap<>();

		// LiNGO scenario is a zip file, so we need to unzip and decrypt.
		try (ZipInputStream zipIn = new ZipInputStream(in)) {

			ZipEntry zipInEntry = zipIn.getNextEntry();
			final NonClosingInputStream filterIn = new NonClosingInputStream(zipIn);

			while (zipInEntry != null) {
				final MessageDigest digest = MessageDigest.getInstance("SHA-256");
				try {
					try (PushbackInputStream pin = new PushbackInputStream(filterIn, KeyFileUtil.UUID_LENGTH)) {
						try (InputStream is = cipher.decrypt(pin)) {
							try (DigestInputStream dis = new DigestInputStream(is, digest)) {
								ByteStreams.copy(dis, ByteStreams.nullOutputStream());
							}
						}
					}

					// Record digest based on entry name
					digests.put(zipInEntry.getName(), digestToString(digest));
				} catch (ScenarioEncryptionException e) {
					// Ignore as e.g. file stream could be smaller than KeyFileUtil.UUID_LENGTH
				}
				zipIn.closeEntry();

				zipInEntry = zipIn.getNextEntry();
			}
		}
		return digests;
	}

	public String getDataDigests(final DelegatingEMFCipher cipher, final InputStream in) throws Exception {

		final MessageDigest digest = MessageDigest.getInstance("SHA-256");

		try (PushbackInputStream pin = new PushbackInputStream(in, KeyFileUtil.UUID_LENGTH)) {

			// Data needs to be decrypted then encrypted
			try (InputStream is = cipher.decrypt(pin)) {
				try (DigestInputStream dis = new DigestInputStream(is, digest)) {
					ByteStreams.copy(dis, ByteStreams.nullOutputStream());
				}
			}
			return digestToString(digest);
		}
	}
}

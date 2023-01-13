/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.Entry.Attribute;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PKCS12Attribute;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileLegacy;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV1;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;
import com.mmxlabs.scenario.service.model.util.encryption.ui.ExistingPasswordPromptDialog;

/**
 * 
 * @author Simon Goodall
 */
public final class KeyFileLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(KeyFileLoader.class);

	private static final String KEYFILE_LEGACY = "lingo.data";

	private static final String KEYFILE_CURRENT = "lingo.pks";

	private static final String KEYSTORE_TYPE = "PKCS12";

	private static final String ENC_KEY_FILE_PROPERTY = "lingo.enc.keyfile";
	private static final String CLOUD_OPTI_ENC_KEY_FILE_PROPERTY = "lingo.cloud.enc.keyfile";
	private static final String CLOUD_OPTI_PRIV_KEY_FILE_PROPERTY = "lingo.cloud.priv.keyfile";
	private static final String CLOUD_OPTI_KEY_UUID = "lingo.cloud.key.uuid";

	// Arbitrary numeric strings to pass PKCS12 parser.

	/**
	 * The revision of the keyfile. These should be unique and the highest number is the one to use for writing.
	 */
	private static final String OID_MMXLABS_KEYFILE_REVISION = "2.0.1.0.1";
	/**
	 * Attribute indicating which KeyFileVN to use with the key
	 */
	private static final String OID_MMXLABS_KEYFILE_TYPE = "2.0.1.0.2";

	private static final char[] ppLegacyValue = new char[] { 'U', 'n', 'i', 'c', 'o', 'r', 'n', '1', '4' };

	private static final char[] ppCurrentValue = new char[] { 'q', 'u', 'o', 'x', '1', 'E', 'e', 'w', 'u', 'V' };

	public static URIConverter.Cipher loadCipher() throws Exception {

		// Try to find the key file

		// Look for file specified in cloud opti system property
		File keyFileFile = getCloudOptimisationKeyFromProperty();

		if (keyFileFile == null) {
			// Look for file specified in system property
			keyFileFile = getEncFilePropertyDataKeyFile();
		}
		if (keyFileFile == null) {
			// Look in instance area - e.g. workspace folder / runtime folder
			keyFileFile = getInstanceDataKeyFile();
		}
		if (keyFileFile == null) {
			// Look in eclipse top of installation directory
			keyFileFile = getEclipseDataKeyFile();
		}
		if (keyFileFile == null) {
			// Look in <user home>/mmxlabs
			keyFileFile = getUserDataAKeyFile();
		}
		if (keyFileFile == null) {
			// Look in <user home>/LiNGO
			keyFileFile = getUserDataBKeyFile();
		}

		if (keyFileFile == null) {
			throw new FileNotFoundException("Unable to locate key file");
		}

		return loadCipher(keyFileFile);
	}

	public static DelegatingEMFCipher loadCipher(@NonNull File keyFileFile) throws Exception {

		final DelegatingEMFCipher cipher = new DelegatingEMFCipher();
		try {

			final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
			try (final InputStream astream = new FileInputStream(keyFileFile)) {
				keyStore.load(astream, ppCurrentValue);
			}

			final ProtectionParameter pp = new PasswordProtection(ppCurrentValue);

			final List<Pair<Integer, IKeyFile>> keyFiles = new LinkedList<>();
			final Iterator<String> itr = keyStore.aliases().asIterator();
			while (itr.hasNext()) {
				final String alias = itr.next();
				final KeyStore.Entry entry = keyStore.getEntry(alias, pp);
				if (entry instanceof SecretKeyEntry) {
					final SecretKeyEntry secretKeyEntry = (SecretKeyEntry) entry;
					final SecretKey secretKey = secretKeyEntry.getSecretKey();

					int revision = -1;
					IKeyFile keyFile = null;

					final byte[] uuid = getBytesFromUUID(alias);
					if (uuid.length != 16) {
						// Check for valid uuid length
						continue;
					}
					try {
						for (final Attribute a : entry.getAttributes()) {
							if (OID_MMXLABS_KEYFILE_REVISION.equals(a.getName())) {
								revision = Integer.parseInt(a.getValue());
							} else if (OID_MMXLABS_KEYFILE_TYPE.equals(a.getName())) {
								switch (a.getValue()) {
								case "v1":
									keyFile = new KeyFileV1(uuid, secretKey);
									break;
								case "v2":
									keyFile = new KeyFileV2(uuid, secretKey);
									break;
								}
							}
						}
						if (revision > -1 && keyFile != null) {
							keyFiles.add(Pair.of(revision, keyFile));
						}
					} catch (final Exception e) {
						LOGGER.error("Error loading keyfile " + alias, e);
					}
				}
			}

			if (!keyFiles.isEmpty()) { // Sort in revision order
				keyFiles.sort((a, b) -> Integer.compare(a.getFirst(), b.getFirst()));
				// Last element should be more recent revision and one to use by default
				final IKeyFile latest = keyFiles.get(keyFiles.size() - 1).getSecond();
				for (final var p : keyFiles) {
					cipher.addKeyFile(p.getSecond(), p.getSecond() == latest);
				}
			}
		} catch (final Exception e) {
			// Fallback!
			// e.printStackTrace();

			try (FileInputStream fis = new FileInputStream(keyFileFile)) {
				readLegacyKeyFile(cipher, fis, true);
			}
		}
		return cipher;
	}

	public static KeyFileV2 loadKeyFile(@NonNull File keyFileFile) throws Exception {
		KeyFileV2 keyFile = null;
		final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		try (final InputStream astream = new FileInputStream(keyFileFile)) {
			keyStore.load(astream, ppCurrentValue);
		}

		final Iterator<String> itr = keyStore.aliases().asIterator();
		while (itr.hasNext()) {
			final String alias = itr.next();
			final ProtectionParameter pp = new PasswordProtection(ppCurrentValue);
			final KeyStore.Entry entry = keyStore.getEntry(alias, pp);
			if (entry instanceof SecretKeyEntry secretKeyEntry) {
				final SecretKey secretKey = secretKeyEntry.getSecretKey();

				final byte[] uuid = getBytesFromUUID(alias);
				if (uuid.length != 16) {
					continue;
				}
				for (final Attribute a : entry.getAttributes()) {
					if (OID_MMXLABS_KEYFILE_TYPE.equals(a.getName()) && "v2".equals(a.getValue())) {
						keyFile = new KeyFileV2(uuid, secretKey);
					}
				}
			}
		}
		return keyFile;
	}

	private static void readLegacyKeyFile(final DelegatingEMFCipher cipher, final InputStream is, final boolean makeDefaultKey) throws Exception {
		char[] password = null;
		try {
			// Read the header data
			final KeyFileHeader header = KeyFileHeader.read(is);

			// Determine the password protecting the keyfile
			switch (header.passwordType) {
			case KeyFileHeader.PASSWORD_TYPE__DEFAULT: {
				password = ppLegacyValue;
				break;
			}
			case KeyFileHeader.PASSWORD_TYPE__PROMPT: {
				password = promptForPassword();
				break;
			}
			default:
				throw new IllegalArgumentException("Unknown password type: " + header.passwordType);
			}

			// Next check the version of the key file and pass off to the relevant decoder
			switch (header.version) {
			case 0: {
				// Load the key file
				final KeyFileLegacy keyFile = KeyFileLegacy.load(is, password);
				// Return the cipher for encrypting/decrypting content
				cipher.addKeyFile(keyFile, makeDefaultKey);
				break;
			}
			default:
				throw new IllegalArgumentException("Unknown keyfile version: " + header.version);
			}
		} finally {
			// Blank array to avoid password hanging around in memory too long
			if (password != null) {
				Arrays.fill(password, (char) 0);
			}
		}
	}

	private static char[] promptForPassword() {

		final char[][] password = new char[1][];
		final Display display = PlatformUI.getWorkbench().getDisplay();
		display.syncExec(() -> {
			final ExistingPasswordPromptDialog dialog = new ExistingPasswordPromptDialog(display.getActiveShell());
			dialog.setBlockOnOpen(true);
			if (dialog.open() == Window.OK) {
				password[0] = dialog.getPassword();
			}
		});

		return password[0];
	}

	private static File getUserDataAKeyFile() {

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/" + KEYFILE_CURRENT);
			if (f.exists()) {
				return f;
			}
		}
		// Fallback to legacy
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/" + KEYFILE_LEGACY);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

	private static File getUserDataBKeyFile() {

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/LiNGO/" + KEYFILE_CURRENT);
			if (f.exists()) {
				return f;
			}
		}
		// Fallback to legacy
		if (userHome != null) {
			final File f = new File(userHome + "/LiNGO/" + KEYFILE_LEGACY);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

	private static File getInstanceDataKeyFile() {

		final String userHome = System.getProperty("osgi.instance.area");
		if (userHome != null) {
			try {
				final URL u = new URL(userHome + "/" + KEYFILE_CURRENT);
				final File f = new File(u.getFile());
				if (f.exists()) {
					return f;
				}
			} catch (final MalformedURLException e) {
				// Ignore
			}
			// Fallback to legacy filename
			try {
				final URL u = new URL(userHome + "/" + KEYFILE_LEGACY);
				final File f = new File(u.getFile());
				if (f.exists()) {
					return f;
				}
			} catch (final MalformedURLException e) {
				// Ignore
			}
		}
		return null;
	}

	private static File getEclipseDataKeyFile() {

		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			try {
				final URL u = new URL(userHome + "/" + KEYFILE_CURRENT);
				final File f = new File(u.getFile());
				if (f.exists()) {
					return f;
				}
			} catch (final MalformedURLException e) {
				// Ignore
			}
			// Fallback to legacy filename

			try {
				final URL u = new URL(userHome + "/" + KEYFILE_LEGACY);
				final File f = new File(u.getFile());
				if (f.exists()) {
					return f;
				}
			} catch (final MalformedURLException e) {
				// Ignore
			}
		}
		return null;
	}

	private static byte[] getKeyUUIDFromProperty() {
		final String keyUUID = System.getProperty(CLOUD_OPTI_KEY_UUID);
		if (keyUUID == null) {
			throw new RuntimeException("KeyUUID is null");
		}
		return Base64.getDecoder().decode(keyUUID);
	}

	public static KeyFileV2 getCloudOptimisationKeyFileV2() throws FileNotFoundException, IOException, GeneralSecurityException {
		final byte[] keyUUID = getKeyUUIDFromProperty();
		SecretKey secretKey = getCloudOptimisationSecretKeyFromProperty();
		KeyFileV2 keyfile = new KeyFileV2(keyUUID, secretKey);
		return keyfile;
	}


	public static SecretKey getCloudOptimisationSecretKeyFromProperty() throws FileNotFoundException, IOException, GeneralSecurityException {
		final String secretKeyFilePath = System.getProperty(CLOUD_OPTI_ENC_KEY_FILE_PROPERTY);
		if (secretKeyFilePath != null) {
			final File secretKeyFile = new File(secretKeyFilePath);
			if (secretKeyFile.exists()) {
				// decrypt secret key
				RSAPrivateKey privateKey = getCloudOptimisationPrivateKeyFromProperty();
				SecretKey secretKey = decryptSymmetricKey(privateKey, secretKeyFile);
				return secretKey;
			}
		}
		return null;
	}

	public static RSAPrivateKey getCloudOptimisationPrivateKeyFromProperty() throws FileNotFoundException, IOException {
		final String privateKeyPath = System.getProperty(CLOUD_OPTI_PRIV_KEY_FILE_PROPERTY);
		if (privateKeyPath != null) {
			final File privateKeyFile = new File(privateKeyPath);
			if (privateKeyFile.exists()) {
				try (FileReader keyReader = new FileReader(privateKeyFile)) {
					PEMParser pemParser = new PEMParser(keyReader);
					JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
					PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
					return (RSAPrivateKey) converter.getPrivateKey(privateKeyInfo);
				}
			}
		}
		return null;
	}

	private static File getCloudOptimisationKeyFromProperty() throws IOException, GeneralSecurityException {
		final String secretKeyFilePath = System.getProperty(CLOUD_OPTI_ENC_KEY_FILE_PROPERTY);
		RSAPrivateKey privateKey = getCloudOptimisationPrivateKeyFromProperty();
		if (secretKeyFilePath != null) {
			final File secretKeyFile = new File(secretKeyFilePath);
			if (secretKeyFile.exists()) {
				SecretKey secretKey = decryptSymmetricKey(privateKey, secretKeyFile);
				final File keyFile = new File(secretKeyFilePath + ".p12");
				final byte[] keyUUID = getKeyUUIDFromProperty();
				KeyFileLoader.initalise(keyFile);
				KeyFileLoader.addToStore(keyFile, keyUUID, secretKey, KeyFileV2.KEYFILE_TYPE);
				return keyFile;
			}
		}
		return null;
	}

	public static SecretKey decryptSymmetricKey(RSAPrivateKey privkey, File encryptedKeyFile) throws IOException, GeneralSecurityException {
		var cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privkey);
		byte[] secretKeyBytes;
		try (FileInputStream fis = new FileInputStream(encryptedKeyFile)) {
			secretKeyBytes = cipher.doFinal(fis.readAllBytes());
		}
		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, KeyFileV2.ENCRYPTION_KEY_ALGORITHM);
		return secretKey;
	}

	private static File getEncFilePropertyDataKeyFile() {

		final String encKeyFile = System.getProperty(ENC_KEY_FILE_PROPERTY);
		if (encKeyFile != null) {
			if (encKeyFile.endsWith(".data")) {
				final File f = new File(encKeyFile.replaceAll("\\.data", ".pks"));
				if (f.exists()) {
					return f;
				}
			}
			{
				final File f = new File(encKeyFile);
				if (f.exists()) {
					return f;
				}
			}
		}
		return null;
	}

	public static void listWSKeys() throws Exception {
		String home = "c:\\users\\sg";
		listKeys(home + "\\mmxlabs\\keyfiles\\k\\lingo.pks");
		listKeys(home + "\\mmxlabs\\keyfiles\\p\\lingo.pks");
		listKeys(home + "\\mmxlabs\\keyfiles\\r\\lingo.pks");
		listKeys(home + "\\mmxlabs\\keyfiles\\s\\lingo.pks");
		listKeys(home + "\\mmxlabs\\keyfiles\\t\\lingo.pks");
	}

	public static void main(final String[] args) throws Exception {

		// Sample code to convert legacy file and generate a new v2 key

		// Local users keystore and client code.
		final String pathRoot = "C:/users/sg/mmxlabs/keyfiles/v/";

		// Old keystore
		final String source = pathRoot + "lingo.data";
		// New keystore
		final String dest = pathRoot + "lingo.pks";

		// Convert Copy legacy key into new style keystore (Only need to do this once per client)
		convert(source, dest, ppLegacyValue);

		// Generate a new key into the new store. Can repeat this to generate a new key for a client.
		generate(dest);

		// List all key files in the user workspace
		// listlistWSKeys();
	}

	public static void generate(String out) {
		final SecretKey newKey = KeyFileV2.generateKey(256);
		final File f = new File(out);
		try {
			if (!f.exists()) {
				KeyFileLoader.initalise(f);
			}
			{
				final byte[] keyUUID = new byte[KeyFileUtil.UUID_LENGTH];
				EcoreUtil.generateUUID(keyUUID);
				KeyFileLoader.addToStore(f, keyUUID, newKey, KeyFileV2.KEYFILE_TYPE);
			}
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void listKeys(String keyfile) throws Exception {
		System.out.println("\nKey ID's in " + keyfile);

		DelegatingEMFCipher keystore = KeyFileLoader.loadCipher(new File(keyfile));
		keystore.listKeys().forEach(k -> System.out.println(KeyFileUtil.byteToString(k, ":")));
	}

	public static void convert(final String in, final String out, final char[] pw) throws Exception {

		initalise(new File(out));

		KeyFileLegacy v1 = null;
		try (FileInputStream is = new FileInputStream(in)) {
			// Read header (then ignore!)
			final KeyFileHeader header = KeyFileHeader.read(is);
			v1 = KeyFileLegacy.load(is, pw);
		}

		addToStore(new File(out), v1.getKeyUUID(), (SecretKey) v1.getKey(), KeyFileV1.KEYFILE_TYPE);
	}

	// Based on https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e

	public static byte[] getBytesFromUUID(final String uuidStr) {
		final UUID uuid = UUID.fromString(uuidStr);
		final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());

		return bb.array();
	}

	public static String getUUIDFromBytes(final byte[] bytes) {
		final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		final Long high = byteBuffer.getLong();
		final Long low = byteBuffer.getLong();

		return new UUID(high, low).toString();
	}

	private static final Function<char[], PasswordProtection> ppGen = password -> {
		final byte[] salt = new byte[20];
		new SecureRandom().nextBytes(salt);
		return new PasswordProtection(password, "PBEWithHmacSHA512AndAES_256", new PBEParameterSpec(salt, 100_000));
	};

	public static void initalise(File f) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		// Initialise key store
		keyStore.load(null, null);

		// Save
		try (FileOutputStream os = new FileOutputStream(f)) {
			keyStore.store(os, ppCurrentValue);
		}
	}

	public static void addToStore(File f, byte[] keyUUID, SecretKey key, String type) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		try (FileInputStream is = new FileInputStream(f)) {
			keyStore.load(is, ppCurrentValue);
		}

		int nextRevision = 1 + keyStore.size();
		{
			final PasswordProtection pp = ppGen.apply(ppCurrentValue);

			final Set<Attribute> attribs = new HashSet<>();
			attribs.add(new PKCS12Attribute(OID_MMXLABS_KEYFILE_REVISION, Integer.toString(nextRevision)));
			attribs.add(new PKCS12Attribute(OID_MMXLABS_KEYFILE_TYPE, type));
			final SecretKeyEntry entry = new SecretKeyEntry(key, attribs);

			keyStore.setEntry(getUUIDFromBytes(keyUUID), entry, pp);
		}

		// Save
		try (FileOutputStream os = new FileOutputStream(f)) {
			keyStore.store(os, ppCurrentValue);
		}
	}
}

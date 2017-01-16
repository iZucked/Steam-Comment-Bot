/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.options.InvalidArgumentException;
import com.mmxlabs.scenario.service.util.encryption.impl.v1.KeyFile;
import com.mmxlabs.scenario.service.util.encryption.ui.ExistingPasswordPromptDialog;

/**
 * 
 * @author Simon Goodall
 */
public final class KeyFileLoader {

	private static final String ENC_KEY_FILE_PROPERTY = "lingo.enc.keyfile";

	public static URIConverter.Cipher loadCipher(final String keyFileName) throws Exception {

		// Try to find the key file

		// Look for file specified in system property
		File keyFileFile = getEncFilePropertyDataKeyFile(keyFileName);
		if (keyFileFile == null) {
			// Look in instance area - e.g. workspace folder / runtime folder
			keyFileFile = getInstanceDataKeyFile(keyFileName);
		}
		if (keyFileFile == null) {
			// Look in eclipse top of installation directory
			keyFileFile = getEclipseDataKeyFile(keyFileName);
		}
		if (keyFileFile == null) {
			// Look in <user home>/mmxlabs
			keyFileFile = getUserDataAKeyFile(keyFileName);
		}
		if (keyFileFile == null) {
			// Look in <user home>/LiNGO
			keyFileFile = getUserDataBKeyFile(keyFileName);
		}

		if (keyFileFile == null) {
			throw new FileNotFoundException("Unable to locate key file");
		}

		char[] password = null;
		try (FileInputStream fis = new FileInputStream(keyFileFile)) {
			// Read the header data
			final KeyFileHeader header = KeyFileHeader.read(fis);

			// Determine the password protecting the keyfile
			switch (header.passwordType) {
			case KeyFileHeader.PASSWORD_TYPE__DEFAULT: {
				password = new char[] { 'U', 'n', 'i', 'c', 'o', 'r', 'n', '1', '4' };
				break;
			}
			case KeyFileHeader.PASSWORD_TYPE__PROMPT: {
				password = promptForPassword();
				break;
			}
			default:
				throw new InvalidArgumentException("Unknown password type: " + header.passwordType);
			}

			// Next check the version of the key file and pass off to the relevant decoder
			switch (header.version) {
			case 0: {
				// Load the key file
				final KeyFile keyFile = KeyFile.load(fis, password);
				// Return the cipher for encrypting/decrypting content
				return keyFile.createCipher();
			}
			default:
				throw new InvalidArgumentException("Unknown keyfile version: " + header.version);
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
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				final ExistingPasswordPromptDialog dialog = new ExistingPasswordPromptDialog(display.getActiveShell());
				dialog.setBlockOnOpen(true);
				if (dialog.open() == Window.OK) {
					password[0] = dialog.getPassword();
				}
			}
		});

		return password[0];
	}

	private static File getUserDataAKeyFile(final String keyFileName) {

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/" + keyFileName);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

	private static File getUserDataBKeyFile(final String keyFileName) {

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/LiNGO/" + keyFileName);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

	private static File getInstanceDataKeyFile(final String keyFileName) {

		final String userHome = System.getProperty("osgi.instance.area");
		if (userHome != null) {
			try {
				final URL u = new URL(userHome + "/" + keyFileName);
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

	private static File getEclipseDataKeyFile(final String keyFileName) {

		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			try {
				final URL u = new URL(userHome + "/" + keyFileName);
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

	private static File getEncFilePropertyDataKeyFile(final String keyFileName) {

		final String encKeyFile = System.getProperty(ENC_KEY_FILE_PROPERTY);
		if (encKeyFile != null) {
			final File f = new File(encKeyFile);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

}

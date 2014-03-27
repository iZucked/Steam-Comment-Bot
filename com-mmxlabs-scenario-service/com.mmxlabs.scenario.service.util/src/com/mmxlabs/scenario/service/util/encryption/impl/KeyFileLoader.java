/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.options.InvalidArgumentException;
import com.mmxlabs.scenario.service.util.encryption.impl.v1.KeyFile;
import com.mmxlabs.scenario.service.util.encryption.ui.PasswordPromptDialog;

/**
 * 
 * @author Simon Goodall
 */
final class KeyFileLoader {

	private static final String KEYFILE = "lingodata.key";

	public static URIConverter.Cipher loadCipher() throws Exception {

		// Try to find the key file
		File keyFileFile = getUserDataAKeyFile();
		if (keyFileFile == null) {
			keyFileFile = getUserDataBKeyFile();
		}
		// TODO keyFileFile == check other search locations

		if (keyFileFile == null) {
			throw new FileNotFoundException("Unable to locate key file");
		}

		char[] password = null;
		try (FileInputStream fis = new FileInputStream(keyFileFile)) {
			// Read the header data
			final KeyFileHeader header = KeyFileHeader.read(fis);

			// Determine the password protecting the keyfile
			switch (header.passwordType) {
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
				// TODO Auto-generated method stub
				final PasswordPromptDialog dialog = new PasswordPromptDialog(display.getActiveShell());
				dialog.setBlockOnOpen(true);
				if (dialog.open() == Window.OK) {
					password[0] = dialog.getPassword();
				}

			}
		});

		return password[0];
	}

	private static File getUserDataAKeyFile() {

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/" + KEYFILE);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

	private static File getUserDataBKeyFile() {

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/LiNGO/" + KEYFILE);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

}

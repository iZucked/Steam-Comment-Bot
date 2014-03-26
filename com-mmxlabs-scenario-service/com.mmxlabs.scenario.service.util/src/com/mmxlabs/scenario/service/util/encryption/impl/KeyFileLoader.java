/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jface.window.Window;
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
		final File keyFileFile = getUserDataKeyFile();
		if (keyFileFile == null) {
			// keyFileFile == check other search locations
		}

		if (keyFileFile == null) {
			throw new FileNotFoundException("Unable to locate key file");
		}

		try (FileInputStream fis = new FileInputStream(keyFileFile)) {
			// Read the header data
			final KeyFileHeader header = KeyFileHeader.read(fis);

			// Determine the password protecting the keyfile
			final char[] password;
			switch (header.passwordType) {
			case KeyFileHeader.PASSWORD_TYPE__PROMPT: {
				password = promptForPassword();// ;new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };
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
		}
	}

	private static char[] promptForPassword() {

		final PasswordPromptDialog dialog = new PasswordPromptDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		dialog.setBlockOnOpen(true);
		if (dialog.open() == Window.OK) {
			return dialog.getPassword();
		}

		return null;
	}

	private static File getUserDataKeyFile() {

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/" + KEYFILE);
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

}

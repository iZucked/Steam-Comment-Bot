package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

/**
 * Implementation of {@link IScenarioCipherProvider} which prompts the user for a password the first time a cipher is requested and caches the result for subsequent invocations. This will return a
 * {@link AESCipherImpl}, or null if there is a problem.
 * 
 * @author Simon Goodall
 * 
 */
public class PromptingSharedCipherProvider implements IScenarioCipherProvider {

	private static final Logger log = LoggerFactory.getLogger(PromptingSharedCipherProvider.class);

	private AESCipherImpl cipher = null;

	@Override
	public synchronized Cipher getSharedCipher() {

		if (cipher == null) {
			String password = getPassword();
			if (password == null || password.isEmpty()) {
				return null;
			}
			try {
				cipher = new AESCipherImpl(password) {
					@Override
					public OutputStream encrypt(OutputStream outputStream) throws Exception {
						// Insert a buffered output stream here to avoid crazily slow save performance (from seconds to minutes!).
						return super.encrypt(new BufferedOutputStream(outputStream));
					}
				};
			} catch (Exception e) {
				log.error("Unable to create cipher: " + e.getMessage(), e);
			}
			// Portable means missing JCE!
			cipher.setKeysize(128);
		}

		return cipher;
	}

	private String getPassword() {
		return "password";
	}

}

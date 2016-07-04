/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.FileNotFoundException;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

/**
 * Implementation of {@link IScenarioCipherProvider} which loads the {@link Cipher} and caches it for further use
 * 
 * @author Simon Goodall
 * 
 */
public class CachingSharedCipherProvider implements IScenarioCipherProvider {

	private static final Logger log = LoggerFactory.getLogger(CachingSharedCipherProvider.class);

	private Cipher cipher = null;

	@Override
	public synchronized Cipher getSharedCipher() {

		if (cipher == null) {
			try {
				cipher = KeyFileLoader.loadCipher("lingo.data");
			} catch (final FileNotFoundException e) {
				cipher = new PassthoughCipher();
				// Ignore
			} catch (final Exception e) {
				log.error("Unable to load cipher: " + e.getMessage(), e);
				// Used to disable dialog during test runs, otherwise test blocks until the "OK" button is pressed.
				if (System.getProperty("lingo.suppress.dialogs") == null) {

					final Display display = PlatformUI.getWorkbench().getDisplay();
					if (display != null) {
						display.asyncExec(new Runnable() {

							@Override
							public void run() {
								MessageDialog.openError(display.getActiveShell(), "Unable to load cipher", "Unable to load cipher: " + e.getMessage());
							}
						});
					}
				}
			}
		}

		return cipher;
	}
}

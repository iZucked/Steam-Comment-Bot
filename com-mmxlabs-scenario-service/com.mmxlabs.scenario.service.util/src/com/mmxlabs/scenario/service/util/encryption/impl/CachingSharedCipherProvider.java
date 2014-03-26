package com.mmxlabs.scenario.service.util.encryption.impl;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.eclipse.jface.dialogs.MessageDialog;
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
				cipher = KeyFileLoader.loadCipher();
			} catch (final Exception e) {
				log.error("Unable to load cipher: " + e.getMessage(), e);
				MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Unable to load cipher", "Unable to load cipher: " + e.getMessage());
			}
		}

		return cipher;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;

public class CloudOptimisationSharedCipherProvider implements IScenarioCipherProvider {
	private static final Logger LOG = LoggerFactory.getLogger(CloudOptimisationSharedCipherProvider.class);

	private KeyFileV2 keyfile;

	public CloudOptimisationSharedCipherProvider(KeyFileV2 keyfile) {
		this.keyfile = keyfile;
	}

	@Override
	public synchronized DelegatingEMFCipher getSharedCipher() {
		final DelegatingEMFCipher cipher = new DelegatingEMFCipher();
		// add keyfile to cipher
		if (this.keyfile != null) {
			cipher.addKeyFile(this.keyfile, true);
		} else {
			throw new RuntimeException("KeyFile hasn't been set");
		}

		return cipher;
	}
}

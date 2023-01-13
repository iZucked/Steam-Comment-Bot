/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;

import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

public class PassthroughCipherProvider implements IScenarioCipherProvider {

	@Override
	public Cipher getSharedCipher() {
		return new PassthoughCipher();
	}

}

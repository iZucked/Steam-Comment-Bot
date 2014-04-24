package com.mmxlabs.scenario.service.util.encryption.impl;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;

import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

public class PassthroughCipherProvider implements IScenarioCipherProvider {

	@Override
	public Cipher getSharedCipher() {
		return new PassthoughCipher();
	}

}

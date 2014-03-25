package com.mmxlabs.scenario.service.util.encryption;

import org.eclipse.emf.ecore.resource.URIConverter;

public interface IScenarioCipherProvider {

	URIConverter.Cipher getSharedCipher();
}

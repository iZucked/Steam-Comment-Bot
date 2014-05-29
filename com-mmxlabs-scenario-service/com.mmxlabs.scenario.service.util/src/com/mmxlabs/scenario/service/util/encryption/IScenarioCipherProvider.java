/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption;

import org.eclipse.emf.ecore.resource.URIConverter;

public interface IScenarioCipherProvider {

	URIConverter.Cipher getSharedCipher();
}

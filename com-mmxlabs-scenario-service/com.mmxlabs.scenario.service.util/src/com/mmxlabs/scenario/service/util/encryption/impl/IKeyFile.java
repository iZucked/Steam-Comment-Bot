/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl;

import org.eclipse.emf.ecore.resource.URIConverter;

public interface IKeyFile {
	URIConverter.Cipher createCipher();
}

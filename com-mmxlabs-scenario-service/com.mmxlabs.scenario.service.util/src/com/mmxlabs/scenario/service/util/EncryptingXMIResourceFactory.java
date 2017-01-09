/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public final class EncryptingXMIResourceFactory extends ResourceFactoryImpl {

	private final URIConverter.Cipher cipher;

	public EncryptingXMIResourceFactory(final URIConverter.Cipher cipher) {
		this.cipher = cipher;
	}

	@Override
	public final Resource createResource(final URI uri) {

		final XMIResourceImpl resource = new XMIResourceImpl(uri);
		resource.getDefaultLoadOptions().put(Resource.OPTION_CIPHER, cipher);
		resource.getDefaultSaveOptions().put(Resource.OPTION_CIPHER, cipher);

		return resource;
	}
}

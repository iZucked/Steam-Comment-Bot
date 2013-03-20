/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.util;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * An MMX resource factory using {@link BinaryResourceImpl}s;
 * 
 * @author Simon Goodall
 * @since 2.3
 */
public class MMXCoreBinaryResourceFactoryImpl extends XMIResourceFactoryImpl implements Factory {

	public MMXCoreBinaryResourceFactoryImpl() {
		super();
	}

	/**
	 * Create a resource for the given URI and associate an {@link MMXCoreResourceHandler} with its load options.
	 */
	@Override
	public Resource createResource(URI uri) {
		final BinaryResourceImpl res = new BinaryResourceImpl(uri) {
			@Override
			public void load(Map<?, ?> options) throws IOException {
				MMXCoreHandlerUtil.preLoad(this);
				super.load(options);
				MMXCoreHandlerUtil.postLoad(this);
			}

			@Override
			public void save(Map<?, ?> options) throws IOException {
				MMXCoreHandlerUtil.preSave(this);
				super.save(options);
				MMXCoreHandlerUtil.postSave(this);
			}
		};
		return res;
	}
}

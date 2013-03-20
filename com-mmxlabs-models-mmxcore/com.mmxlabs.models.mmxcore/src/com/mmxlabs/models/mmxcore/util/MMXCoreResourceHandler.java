/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.ResourceHandler;

/**
 * Resource handler which automatically creates / destroys proxies when a resource is saved.
 * 
 * To correct proxies when a resource is loaded use the static method {@link #postLoad(Collection)}
 * 
 * @author hinton
 * 
 */
public class MMXCoreResourceHandler implements ResourceHandler {
	/**
	 * Restore proxies across a collection of resources which have been loaded together
	 * 
	 * @param resources
	 * @deprecated Use {@link MMXCoreHandlerUtil#postLoad(Collection)} instead
	 */
	public static void postLoad(final Collection<Resource> resources) {
		MMXCoreHandlerUtil.restoreProxiesForResources(resources);
	}

	@Override
	public void preLoad(final XMLResource resource, final InputStream inputStream, final Map<?, ?> options) {
		MMXCoreHandlerUtil.preLoad(resource);
	}

	@Override
	public void postLoad(final XMLResource resource, final InputStream inputStream, final Map<?, ?> options) {
		MMXCoreHandlerUtil.postLoad(resource);
	}

	@Override
	public void preSave(final XMLResource resource, final OutputStream outputStream, final Map<?, ?> options) {
		MMXCoreHandlerUtil.preSave(resource);
	}

	@Override
	public void postSave(final XMLResource resource, final OutputStream outputStream, final Map<?, ?> options) {
		MMXCoreHandlerUtil.postSave(resource);
	}
}

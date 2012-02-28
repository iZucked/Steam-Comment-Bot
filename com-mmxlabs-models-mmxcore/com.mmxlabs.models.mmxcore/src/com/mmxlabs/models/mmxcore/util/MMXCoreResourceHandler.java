/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.ResourceHandler;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

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
	 * @param resources
	 */
	public static void postLoad(Collection<Resource> resources) {
		final Map<String, UUIDObject> keyedObjects = new HashMap<String, UUIDObject>();
		for (final Resource resource : resources) {
			final TreeIterator<EObject> iterator = resource.getAllContents();
			while (iterator.hasNext()) {
				final EObject o = iterator.next();
				if (o instanceof MMXObject){
					((MMXObject) o).collectUUIDObjects(keyedObjects);
					iterator.prune();
				}
			}
		}
		for (final Resource resource : resources) {
			final TreeIterator<EObject> iterator = resource.getAllContents();
			while (iterator.hasNext()) {
				final EObject o = iterator.next();
				if (o instanceof MMXObject){
					((MMXObject) o).resolveProxies(keyedObjects);
					((MMXObject) o).restoreProxies();
				}
			}
		}
	}
	
	@Override
	public void preLoad(XMLResource resource, InputStream inputStream,
			Map<?, ?> options) {

	}

	@Override
	public void postLoad(XMLResource resource, InputStream inputStream,
			Map<?, ?> options) {
		
	}

	@Override
	public void preSave(XMLResource resource, OutputStream outputStream,
			Map<?, ?> options) {
		final TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof MMXObject){
				((MMXObject) o).makeProxies();
				iterator.prune();
			}
		}
	}

	@Override
	public void postSave(XMLResource resource, OutputStream outputStream,
			Map<?, ?> options) {
		final TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof MMXObject){
				((MMXObject) o).restoreProxies();
				iterator.prune();
			}
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * An MMX resource factory which sets some default load options on generated resources;
 * 
 * @author hinton
 */
public class MMXCoreResourceFactoryImpl extends XMIResourceFactoryImpl
		implements Factory {

	private MMXCoreResourceHandler handler;

	public MMXCoreResourceFactoryImpl() {
		super();
		this.handler = new MMXCoreResourceHandler();
	}

	/**
	 * Create a resource for the given URI and associate an {@link MMXCoreResourceHandler} with its load options.
	 */
	@Override
	public Resource createResource(URI uri) {
		final XMIResource res = (XMIResource) super.createResource(uri);
		res.getDefaultLoadOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, handler);
		res.getDefaultSaveOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, handler);
		return res;
	}
	
	/**
	 * Load several related resources which are using MMXProxy objects, and then link up the proxies within the set.
	 * @param uris
	 * @return
	 * @deprecated See {@link MMXCoreHandlerUtil#loadAndLink(List, org.eclipse.emf.ecore.resource.Resource.Factory.Registry)}
	 */
	public List<Resource> loadAndLink(final List<URI> uris) {
		final ArrayList<Resource> result = new ArrayList<Resource>(uris.size());
		for (final URI uri : uris) {
			result.add(createResource(uri));
		}
		MMXCoreHandlerUtil.restoreProxiesForResources(result);
		return result;
	}
	
	/**
	 * Take a list of resources, each of which should contain a single {@link UUIDObject} in their root,
	 * and one of which should contain an {@link MMXRootObject} as its root, and put the sub models into the root object.
	 * 
	 * @param resources
	 * @return
	 * @deprecated see {@link MMXCoreHandlerUtil#composeRootObject(List)}
	 */
	public MMXRootObject composeRootObject(final List<Resource> resources) {
		MMXRootObject rootObject = null;
		final List<UUIDObject> subModels = new LinkedList<UUIDObject>();
		for (final Resource resource : resources) {
			assert(resource.getContents().size() == 1);
			final EObject rootObjectForResource = resource.getContents().get(0);
			if (rootObjectForResource instanceof MMXRootObject) {
				assert rootObject == null;
				rootObject = (MMXRootObject) rootObjectForResource;
			} else {
				assert (rootObjectForResource instanceof UUIDObject);
				subModels.add((UUIDObject) rootObjectForResource);
			}
		}
		if (rootObject != null) {
			for (final UUIDObject subModel : subModels) {
				rootObject.addSubModel(subModel);
			}
		}
		return rootObject;
	}
	
	/**
	 * Equivalent to {@link #composeRootObject(List)} applied to {@link #loadAndLink(List)}
	 * 
	 * @param uris
	 * @return
	 * @deprecated ser {@link MMXCoreHandlerUtil#loadLinkAndCompose(List, org.eclipse.emf.ecore.resource.Resource.Factory.Registry)}
	 */
	public MMXRootObject loadLinkAndCompose(final List<URI> uris) {
		return composeRootObject(loadAndLink(uris));
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * A set of util methods to create and destroy proxies when a resource is saved.
 * 
 * To correct proxies when a resource is loaded use the static method {@link #postLoad(Collection)}
 * 
 * @author hinton
 * @since 2.3
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class MMXCoreHandlerUtil {

	private MMXCoreHandlerUtil() {

	}

	/**
	 * Restore proxies across a collection of resources which have been loaded together
	 * 
	 * @param resources
	 */
	public static void restoreProxiesForResources(final Collection<Resource> resources) {
		final Map<String, UUIDObject> keyedObjects = new HashMap<String, UUIDObject>();
		for (final Resource resource : resources) {
			final TreeIterator<EObject> iterator = resource.getAllContents();
			while (iterator.hasNext()) {
				final EObject o = iterator.next();
				if (o instanceof MMXObject) {
					((MMXObject) o).collectUUIDObjects(keyedObjects);
					iterator.prune();
				}
			}
		}
		for (final Resource resource : resources) {
			final TreeIterator<EObject> iterator = resource.getAllContents();
			while (iterator.hasNext()) {
				final EObject o = iterator.next();
				if (o instanceof MMXObject) {
					((MMXObject) o).resolveProxies(keyedObjects);
					((MMXObject) o).restoreProxies();
				}
			}
		}
	}

	/**
	 * Restore proxies across a collection of EObjects which have been loaded together
	 * 
	 * @param resources
	 */
	public static void restoreProxiesForEObjects(final Collection<EObject> parts) {
		final HashMap<String, UUIDObject> table = new HashMap<String, UUIDObject>();
		for (final EObject part : parts) {
			collect(part, table);
		}
		// now restore proxies
		for (final EObject part : parts) {
			resolve(part, table);
		}
	}

	public static void preLoad(final Resource resource) {
		if (resource instanceof ResourceImpl) {
			((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
		}
	}

	public static void postLoad(final Resource resource) {
		if (resource instanceof ResourceImpl) {
			((ResourceImpl) resource).setIntrinsicIDToEObjectMap(null);
		}
	}

	public static void preSave(final Resource resource) {
		final TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof MMXObject) {
				((MMXObject) o).makeProxies();
				iterator.prune();
			}
		}
	}

	public static void postSave(final Resource resource) {
		final TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof MMXObject) {
				((MMXObject) o).restoreProxies();
				iterator.prune();
			}
		}
	}

	/**
	 * Load several related resources which are using MMXProxy objects, and then link up the proxies within the set.
	 * 
	 * @param uris
	 * @return
	 */
	public static List<Resource> loadAndLink(final List<URI> uris, final Resource.Factory.Registry registry) {
		final ArrayList<Resource> result = new ArrayList<Resource>(uris.size());
		for (final URI uri : uris) {
			result.add(createResource(registry, uri, null));
		}
		MMXCoreHandlerUtil.restoreProxiesForResources(result);
		return result;
	}

	/**
	 * Take a list of resources, each of which should contain a single {@link UUIDObject} in their root, and one of which should contain an {@link MMXRootObject} as its root, and put the sub models
	 * into the root object.
	 * 
	 * @param resources
	 * @return
	 */
	public static MMXRootObject composeRootObject(final List<Resource> resources) {
		MMXRootObject rootObject = null;
		final List<UUIDObject> subModels = new LinkedList<UUIDObject>();
		for (final Resource resource : resources) {
			assert (resource.getContents().size() == 1);
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
	 */
	public static MMXRootObject loadLinkAndCompose(final List<URI> uris, final Resource.Factory.Registry registry) {
		return composeRootObject(loadAndLink(uris, registry));
	}

	/*
	 * Javadoc copied from interface.
	 */
	public static Resource createResource(final Resource.Factory.Registry registry, final URI uri, final String contentType) {
		final Resource.Factory resourceFactory = registry.getFactory(uri, contentType);
		if (resourceFactory != null) {
			final Resource result = resourceFactory.createResource(uri);
			return result;
		} else {
			return null;
		}
	}

	private static void collect(final EObject object, final HashMap<String, UUIDObject> table) {
		if (object == null) {
			return;
		}
		if (object instanceof MMXObject)
			((MMXObject) object).collectUUIDObjects(table);
		else {
			for (final EObject o : object.eContents())
				collect(o, table);
		}
	}

	private static void resolve(final EObject part, final HashMap<String, UUIDObject> table) {
		if (part == null) {
			return;
		}
		if (part instanceof MMXObject) {
			((MMXObject) part).resolveProxies(table);
			((MMXObject) part).restoreProxies();
		} else {
			for (final EObject child : part.eContents())
				resolve(child, table);
		}
	}
}

package com.mmxlabs.model.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class MMXCoreResourceHandler  {
	/**
	 * Restore proxies across a collection of resources which have been loaded together
	 * 
	 * @param resources
	 */
	public static void postLoad(Collection<Resource> resources) {
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

	public void preLoad(Resource resource) {
		if (resource instanceof ResourceImpl) {
			((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
		}
	}

	public void postLoad(Resource resource) {
		if (resource instanceof ResourceImpl) {
			((ResourceImpl) resource).setIntrinsicIDToEObjectMap(null);
		}
	}

	public void preSave(Resource resource) {
		final TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof MMXObject) {
				((MMXObject) o).makeProxies();
				iterator.prune();
			}
		}
	}

	public void postSave(Resource resource) {
		final TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof MMXObject) {
				((MMXObject) o).restoreProxies();
				iterator.prune();
			}
		}
	}
}
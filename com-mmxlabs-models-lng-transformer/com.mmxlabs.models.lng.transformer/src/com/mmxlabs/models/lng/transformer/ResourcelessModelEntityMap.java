/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * A model entity map which doesn't use the resourceset.
 * 
 * @author Tom Hinton
 * 
 */
public class ResourcelessModelEntityMap extends ModelEntityMap {
	private final Map<Object, Object> modelToOptimiser = new HashMap<Object, Object>();
	private final Map<Object, Object> optimiserToModel = new HashMap<Object, Object>();

	@Override
	public <U> U getModelObject(final Object internalObject, final Class<? extends U> clz) {
		return clz.cast(optimiserToModel.get(internalObject));
	}

	@Override
	public void addModelObject(final EObject modelObject, final Object internalObject) {
		modelToOptimiser.put(modelObject, internalObject);
		optimiserToModel.put(internalObject, modelObject);
	}

	@Override
	public <T> T getOptimiserObject(final EObject modelObject, final Class<? extends T> clz) {
		return clz.cast(modelToOptimiser.get(modelObject));
	}

	@Override
	public void setScenario(final MMXRootObject rootObject) {
	}

	@Override
	public void dispose() {
		super.dispose();
		modelToOptimiser.clear();
		optimiserToModel.clear();
	}

	public <T extends EObject> Collection<T> getAllModelObjects(final Class<? extends T> clz) {

		final List<T> objects = new LinkedList<T>();
		for (final Object obj : modelToOptimiser.keySet()) {
			if (clz.isInstance(obj)) {
				objects.add(clz.cast(obj));
			}
		}
		return objects;
	}
}

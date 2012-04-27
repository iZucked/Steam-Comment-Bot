/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.emfpath.EMFUtils;

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
	public void dispose() {
		super.dispose();
		modelToOptimiser.clear();
		optimiserToModel.clear();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.extensions.IModelFactoryExtension;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;
import com.mmxlabs.models.util.importer.registry.impl.AbstractRegistry;

public class ModelFactoryRegistry extends
		AbstractRegistry<EClass, IModelFactory> implements IModelFactoryRegistry{

	@Inject Iterable<IModelFactoryExtension> extensions;
	
	@Override
	protected IModelFactory match(final EClass key) {
		IModelFactoryExtension bestMatch = null;
		int bestCloseness = Integer.MAX_VALUE;
		for (final IModelFactoryExtension extension : extensions) {
			int matchCloseness = getMinimumGenerations(key, extension.getTargetEClass());
			if (matchCloseness == 0) {
				bestMatch = extension;
				break;
			} else if (matchCloseness < bestCloseness && Boolean.valueOf(extension.isInheritable())) {
				bestMatch = extension;
				bestCloseness = matchCloseness;
			}
		}
		if (bestMatch == null) return null;
		if (factoryExistsForID(bestMatch.getID())) {
			return getFactoryForID(bestMatch.getID());
		} else {
			return cacheFactoryForID(bestMatch.getID(), bestMatch.createFactory());
		}
	}

	@Override
	public synchronized IModelFactory getModelFactory(EClass targetEClass) {
		return get(targetEClass);
	}
}

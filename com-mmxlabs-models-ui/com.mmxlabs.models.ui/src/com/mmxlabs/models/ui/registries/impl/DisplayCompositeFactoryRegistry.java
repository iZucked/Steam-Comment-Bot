/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.extensions.IDisplayCompositeFactoryExtension;
import com.mmxlabs.models.ui.registries.IDisplayCompositeFactoryRegistry;
import com.mmxlabs.models.util.importer.registry.impl.AbstractRegistry;

public class DisplayCompositeFactoryRegistry extends AbstractRegistry<EClass, IDisplayCompositeFactory> implements IDisplayCompositeFactoryRegistry {
	@Inject Iterable<IDisplayCompositeFactoryExtension> extensions;
	public DisplayCompositeFactoryRegistry() {

	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.impl.compositefactoryregistry.IDisplayCompositeFactoryRegistry#getDisplayCompositeFactory(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public synchronized IDisplayCompositeFactory getDisplayCompositeFactory(final EClass displayedClass) {
		return get(displayedClass);
	}

	@Override
	protected IDisplayCompositeFactory match(final EClass key) {
		IDisplayCompositeFactoryExtension bestExtension = null;
		int bestExtensionMatch = Integer.MAX_VALUE;
		for (final IDisplayCompositeFactoryExtension extension : extensions) {
			final int closeness = getMinimumGenerations(key, extension.getEClassName());
			if (Boolean.valueOf(extension.isInheritable()) || closeness == 0) {
				if (closeness < bestExtensionMatch) {
					bestExtensionMatch = closeness;
					bestExtension = extension;
				}
			}
		}
		
		if (bestExtension == null) return null;
		
		if (factoryExistsForID(bestExtension.getID())) {
			return getFactoryForID(bestExtension.getID());
		} else {
			return cacheFactoryForID(bestExtension.getID(), bestExtension.instantiate());
		}
	}
}

package com.mmxlabs.models.ui.registries.impl;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.extensions.IComponentHelperExtension;
import com.mmxlabs.models.ui.extensions.IDisplayCompositeFactoryExtension;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * Handles the extension point com.mmxlabs.models.ui.componenthelpers. When
 * constructing a UI element for an EClass, the default editor will ask this
 * registry to get an {@link IComponentHelper} matching the EClass. Extensions
 * are matched according to the following rules:
 * 
 * <ol>
 * <li>An extension matches an EClass if (a) the modelClass attribute matches
 * the EClass' interface canonical name exactly, or (b) matches a supertype's
 * interface canonical name exactly and inheritable=true</li>
 * <li>If more than one extension matches, the one with the most specific
 * modelClass wins</li>
 * </ol>
 * 
 * @author hinton
 * 
 */
public class ComponentHelperRegistry extends AbstractRegistry<EClass, IComponentHelper> implements IComponentHelperRegistry {
	@Inject Iterable<IComponentHelperExtension> extensions;
	
	public ComponentHelperRegistry() {

	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.registries.impl.IComponentHelperRegistry#getComponentHelper(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public synchronized IComponentHelper getComponentHelper(final EClass modelClass) {
		return get(modelClass);
	}

	@Override
	protected IComponentHelper match(final EClass key) {
		IComponentHelperExtension bestExtension = null;
		int bestExtensionMatch = Integer.MAX_VALUE;
		for (final IComponentHelperExtension extension : extensions) {
			final int closeness = getMinimumGenerations(key, extension.getEClassName());
			if (extension.isInheritable() || closeness == 0) {
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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.extensions.IComponentHelperExtension;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.util.importer.registry.impl.AbstractRegistry;

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
	
	/**
	 * If an EClass has no component helper registered that will match it, but its supertypes do have registered component helpers,
	 * we will still want to invoke those on the supertypes. This method returns a component helper to do that job.
	 * 
	 * @param eClass
	 * @return
	 */
	private IComponentHelper reflector(final EClass eClass) {
		final ArrayList<IComponentHelper> superHelpers = new ArrayList<IComponentHelper>();
		
		for (final EClass eSuper : eClass.getESuperTypes()) {
			final IComponentHelper h = getComponentHelper(eSuper);
			if (h != null) superHelpers.add(h);
		}
		
		// This check should ensure that pointless reflectors are not created.
		if (superHelpers.isEmpty()) return null;
		
		return new IComponentHelper() {
			@Override
			public void addEditorsToComposite(IInlineEditorContainer detailComposite,
					EClass displayedClass) {
				for (final IComponentHelper h : superHelpers) h.addEditorsToComposite(detailComposite, displayedClass);
			}
			
			@Override
			public void addEditorsToComposite(IInlineEditorContainer detailComposite) {
				addEditorsToComposite(detailComposite, eClass);
			}
		};
	}
	
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
			if ( Boolean.valueOf(extension.isInheritable()) || closeness == 0) {
				if (closeness < bestExtensionMatch) {
					bestExtensionMatch = closeness;
					bestExtension = extension;
				}
			}
		}
		
		if (bestExtension == null) {
			if (key.getESuperTypes().isEmpty()) return null;
			else return reflector(key);
		}
		
		if (factoryExistsForID(bestExtension.getID())) {
			return getFactoryForID(bestExtension.getID());
		} else {
			return cacheFactoryForID(bestExtension.getID(), bestExtension.instantiate());
		}
	}
}

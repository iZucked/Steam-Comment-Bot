/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.extensions.IComponentHelperExtension;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
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
public class ComponentHelperRegistry extends AbstractRegistry<EClass, List<IComponentHelper>> implements IComponentHelperRegistry {

	@Inject
	private Iterable<IComponentHelperExtension> extensions;

	private Comparator<IComponentHelper> comparator = (arg0, arg1) -> {

		if (arg0 == null) {
			return -1;
		}
		if (arg1 == null) {
			return 1;
		}

		return ((Integer) arg0.getDisplayPriority()).compareTo(arg1.getDisplayPriority());
	};

	@Override
	public synchronized List<IComponentHelper> getComponentHelpers(final EClass modelClass) {
		return get(modelClass);
	}

	@Override
	protected List<IComponentHelper> match(final EClass key) {

		final List<IComponentHelper> helpers = new ArrayList<>();
		IComponentHelperExtension bestExtension = null;
		int bestExtensionMatch = Integer.MAX_VALUE;
		for (final IComponentHelperExtension extension : extensions) {
			final int closeness = getMinimumGenerations(key, extension.getEClassName());
			if (closeness == 0) {
				if (closeness < bestExtensionMatch) {
					bestExtensionMatch = closeness;
					bestExtension = extension;
				} else if (Boolean.valueOf(extension.isInheritable()) == false) {
					if (factoryExistsForID(extension.getID())) {
						helpers.addAll(getFactoryForID(extension.getID()));
					} else {
						if (extension.getHelperClass().equals(DefaultComponentHelper.class.getCanonicalName())) {
							// Note: key may need to be replaced with the extension eclass?
							helpers.addAll(cacheFactoryForID(extension.getID(), Collections.singletonList(new DefaultComponentHelper(key))));
						} else {
							helpers.addAll(cacheFactoryForID(extension.getID(), Collections.singletonList(extension.instantiate())));
						}
					}
				}
			}

		}

		if (bestExtension == null) {
			helpers.add(new DefaultComponentHelper(key));
			return helpers;
		}

		if (factoryExistsForID(bestExtension.getID())) {
			helpers.addAll(getFactoryForID(bestExtension.getID()));
		} else {
			if (bestExtension.getHelperClass().equals(DefaultComponentHelper.class.getCanonicalName())) {
				// Note: key may need to be replaced with the extension eclass?
				helpers.addAll(cacheFactoryForID(bestExtension.getID(), Collections.singletonList(new DefaultComponentHelper(key))));
			} else {
				helpers.addAll(cacheFactoryForID(bestExtension.getID(), Collections.singletonList(bestExtension.instantiate())));
			}
		}

		if (helpers.size() > 1) {
			Collections.sort(helpers, comparator);
		}

		while (helpers.remove(null))
			;

		return helpers;
	}
}

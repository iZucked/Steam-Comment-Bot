/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.extensions.IComponentHelperExtension;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.util.importer.registry.impl.AbstractRegistry;

/**
 * Handles the extension point com.mmxlabs.models.ui.componenthelpers. When constructing a UI element for an EClass, the default editor will ask this registry to get an {@link IComponentHelper}
 * matching the EClass. Extensions are matched according to the following rules:
 * 
 * <ol>
 * <li>An extension matches an EClass if (a) the modelClass attribute matches the EClass' interface canonical name exactly, or (b) matches a supertype's interface canonical name exactly and
 * inheritable=true</li>
 * <li>If more than one extension matches, the one with the most specific modelClass wins</li>
 * </ol>
 * 
 * @author hinton
 * 
 */
public class ComponentHelperRegistry extends AbstractRegistry<EClass, List<IComponentHelper>> implements IComponentHelperRegistry {
	@Inject
	Iterable<IComponentHelperExtension> extensions;
	private Comparator<IComponentHelper> comparator = new Comparator<IComponentHelper>() {
		@Override
		public int compare(IComponentHelper arg0, IComponentHelper arg1) {

			if (arg0 == null) {
				return -1;
			}
			if (arg1 == null) {
				return 1;
			}

			return ((Integer) arg0.getDisplayPriority()).compareTo(arg1.getDisplayPriority());
		}
	};

	/**
	 * If an EClass has no component helper registered that will match it, but its supertypes do have registered component helpers, we will still want to invoke those on the supertypes. This method
	 * returns a component helper to do that job.
	 * 
	 * @param eClass
	 * @return
	 */
	private IComponentHelper reflector(final EClass eClass) {
		final ArrayList<IComponentHelper> superHelpers = new ArrayList<IComponentHelper>();

		for (final EClass eSuper : eClass.getESuperTypes()) {
			final List<IComponentHelper> h = getComponentHelpers(eSuper);
			superHelpers.addAll(h);
		}

		// This check should ensure that pointless reflectors are not created.
		if (superHelpers.isEmpty())
			return null;

		return new BaseComponentHelper() {
			@Override
			public void addEditorsToComposite(IInlineEditorContainer detailComposite, EClass displayedClass) {
				for (final IComponentHelper h : superHelpers)
					h.addEditorsToComposite(detailComposite, displayedClass);
			}

			@Override
			public void addEditorsToComposite(IInlineEditorContainer detailComposite) {
				addEditorsToComposite(detailComposite, eClass);
			}
		};
	}

	public ComponentHelperRegistry() {

	}

	@Override
	public synchronized List<IComponentHelper> getComponentHelpers(final EClass modelClass) {
		return get(modelClass);
	}

	@Override
	protected List<IComponentHelper> match(final EClass key) {
		final List<IComponentHelper> helpers = new ArrayList<IComponentHelper>();
		IComponentHelperExtension bestExtension = null;
		int bestExtensionMatch = Integer.MAX_VALUE;
		for (final IComponentHelperExtension extension : extensions) {
			final int closeness = getMinimumGenerations(key, extension.getEClassName());
			if (Boolean.valueOf(extension.isInheritable()) || closeness == 0) {
				if (closeness < bestExtensionMatch) {
					bestExtensionMatch = closeness;
					bestExtension = extension;
				} else if (Boolean.valueOf(extension.isInheritable()) == false) {
					if (factoryExistsForID(extension.getID())) {
						helpers.addAll(getFactoryForID(extension.getID()));
					} else {
						helpers.addAll(cacheFactoryForID(extension.getID(), Collections.singletonList(extension.instantiate())));
					}
				}
			}

		}

		if (bestExtension == null) {
			if (key.getESuperTypes().isEmpty())
				return helpers;
			final IComponentHelper reflector = reflector(key);
			if (reflector != null)
				helpers.add(reflector);
			return helpers;
		}

		if (factoryExistsForID(bestExtension.getID())) {
			helpers.addAll(getFactoryForID(bestExtension.getID()));
		} else {
			helpers.addAll(cacheFactoryForID(bestExtension.getID(), Collections.singletonList(bestExtension.instantiate())));
		}

		if (helpers.size() > 1) {
			Collections.sort(helpers, comparator);
		}

		while (helpers.remove(null))
			;

		return helpers;
	}
}

package com.mmxlabs.models.ui;

import java.util.HashMap;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

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
public class ComponentHelperRegistry {
	private static final String COMPONENT_HELPER_EXTENSION_ID = "com.mmxlabs.models.ui.componenthelpers";
	
	private WeakHashMap<EClass, IComponentHelper> cache = new WeakHashMap<EClass, IComponentHelper>();
	// TODO add weak references to this
	private HashMap<String, IComponentHelper> cacheById = new HashMap<String, IComponentHelper>();

	public ComponentHelperRegistry() {

	}

	private int getMinimumGenerations(final EClass eClass,
			final String canonicalNameOfSuper) {
		if (eClass.getInstanceClass().getCanonicalName()
				.equals(canonicalNameOfSuper)) {
			return 0;
		}
		int result = Integer.MAX_VALUE;
		for (final EClass superClass : eClass.getESuperTypes()) {
			final int d = getMinimumGenerations(superClass,
					canonicalNameOfSuper);
			if (d - 1 < result - 1) {
				result = d + 1;
			}
		}
		return result;
	}

	public synchronized IComponentHelper getComponentHelper(
			final EClass modelClass) {
		IComponentHelper result = null;
		if (cache.containsKey(modelClass) == false) {
			IConfigurationElement bestElement = null;
			int distance = Integer.MAX_VALUE;
			top: for (final IConfigurationElement element : Platform
					.getExtensionRegistry().getConfigurationElementsFor(
							COMPONENT_HELPER_EXTENSION_ID)) {
				for (final IConfigurationElement helperElement : element
						.getChildren("componentHelper")) {
					final String modelClassName = helperElement
							.getAttribute("modelClass");
					final String inheritable = helperElement
							.getAttribute("inheritable");
					if (modelClass.getInstanceClass().getCanonicalName()
							.equals(modelClassName)) {
						// we are done
						bestElement = helperElement;
						break top;
					} else if (inheritable.equalsIgnoreCase("true")) {
						int thisDistance = getMinimumGenerations(modelClass,
								modelClassName);
						if (thisDistance < distance) {
							distance = thisDistance;
							bestElement = helperElement;
						}
					}
				}
			}

			if (bestElement != null) {
				final String id = bestElement.getAttribute("id");
				if (cacheById.containsKey(id)) {
					result = cacheById.get(id);
				} else {
					try {
						result = (IComponentHelper) bestElement
								.createExecutableExtension("helperClass");
						cacheById.put(id, result);
					} catch (CoreException e) {
						cacheById.put(id, null);
					}

				}
			}

			cache.put(modelClass, result);
		} else {
			result = cache.get(modelClass);
		}
		return result;
	}
}

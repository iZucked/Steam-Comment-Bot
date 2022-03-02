/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * Handy methods for editor parts
 * 
 * @author Tom Hinton
 * 
 */
public class EditorUtils {
	/**
	 * Adapter factory instance. This contains all factories registered in the global registry.
	 */
	private static final ComposedAdapterFactory FACTORY = createAdapterFactory();

	/**
	 * Utility method to create a {@link ComposedAdapterFactory}. Taken from org.eclipse.emf.compare.util.AdapterUtils.
	 * 
	 * @return
	 */
	public static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	public static String unmangleFeature(final EStructuralFeature feature, EObject input) {

		// Note: Make sure the property type for the feature is not NONE in the gen model.

		// This will fetch the property source of the input object
		final IItemPropertySource inputPropertySource = (IItemPropertySource) FACTORY.adapt(input, IItemPropertySource.class);

		// Iterate through the property descriptors to find a matching
		// descriptor for the feature
		for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(input)) {

			if (feature.equals(descriptor.getFeature(input))) {
				// Found match
				return descriptor.getDisplayName(input);
			}
		}

		return unmangle(feature.getName());
	}

	public static String unmangle(final EObject object) {
		Object adapter = FACTORY.getRootAdapterFactory().adapt(object, IItemLabelProvider.class);
		if (adapter instanceof ResourceLocator) {
			return ((ResourceLocator) adapter).getString(String.format("_UI_%s_type", object.eClass().getName()));
		}
		// if (str != null) {
		// return str;
		// }
		return unmangle(object.eClass().getName());
	}

	public static String unmangle(final String name) {
		final StringBuilder sb = new StringBuilder();
		boolean lastWasLower = false;
		boolean lastLastWasLower = false;
		boolean firstChar = true;
		for (final char c : name.toCharArray()) {
			if (firstChar) {
				sb.append(Character.toUpperCase(c));
				lastWasLower = Character.isLowerCase(c);
				firstChar = false;
			} else {
				if (lastWasLower && Character.isUpperCase(c)) {
					sb.append(" ");
				} else if (!lastLastWasLower && !lastWasLower && !Character.isUpperCase(c)) {
					sb.insert(sb.length() - 1, " ");
				}
				lastLastWasLower = lastWasLower;
				lastWasLower = Character.isLowerCase(c);
				sb.append(c);
			}
		}
		return sb.toString().trim();
	}
}

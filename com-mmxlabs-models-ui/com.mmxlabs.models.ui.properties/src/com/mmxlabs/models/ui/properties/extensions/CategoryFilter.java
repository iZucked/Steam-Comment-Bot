/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.extensions;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.AttributeFilter;

import com.google.inject.Module;

/**
 * An {@link AttributeFilter} for the category attribute of the DetailPropertyFactory extension point. This should used in a Guice {@link Module} definition with something like;
 * 
 * <pre>
 * 		bind(iterable(DetailPropertyFactoryExtensionPoint.class)).toProvider(service(DetailPropertyFactoryExtensionPoint.class).filter(new CategoryFilter("<category>").multiple());
 * </pre>
 * 
 * @author Simon Goodall
 * 
 */
public class CategoryFilter implements AttributeFilter {

	private static final String KEY_CATEGORY = "category";
	private final String category;

	public CategoryFilter(@NonNull final String category) {
		this.category = category;
	}

	@Override
	public boolean matches(final Map<String, ?> attributes) {

		if (attributes.containsKey(KEY_CATEGORY)) {
			final Object value = attributes.get(KEY_CATEGORY);
			return category.equals(value);
		}

		return false;
	}

}

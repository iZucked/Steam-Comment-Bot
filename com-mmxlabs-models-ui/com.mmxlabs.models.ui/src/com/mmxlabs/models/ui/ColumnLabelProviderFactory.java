/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;

public class ColumnLabelProviderFactory {
	private ColumnLabelProviderFactory() {

	}

	public static @NonNull ColumnLabelProvider make(Function<Object, String> provider) {
		return new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {
				return provider.apply(element);
			}
		};
	}

	public static <T> @NonNull ColumnLabelProvider make(Class<T> cls, @Nullable String defaultValue, Function<T, String> provider) {
		return new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {
				if (cls.isInstance(element)) {
					return provider.apply(cls.cast(element));
				}
				return defaultValue;
			}
		};
	}

	public static @NonNull ColumnLabelProvider make(Function<Object, String> provider, Function<Object, Color> backgroundProvider) {
		return new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {
				return provider.apply(element);
			}

			@Override
			public Color getBackground(Object element) {
				return backgroundProvider.apply(element);
			}

		};
	}

	public static <T> @NonNull ColumnLabelProvider make(Class<T> cls, @Nullable String defaultValue, Function<T, String> provider, Function<Object, Color> backgroundProvider) {
		return new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {
				if (cls.isInstance(element)) {
					return provider.apply(cls.cast(element));
				}
				return defaultValue;
			}

			@Override
			public Color getBackground(Object element) {
				return backgroundProvider.apply(element);
			}
		};
	}
}

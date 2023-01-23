/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editors;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.EnumCheckboxEditor;

public final class RouteChoiceInlineEditorHelper {

	private RouteChoiceInlineEditorHelper() {

	}

	public static List<IInlineEditor> createRouteChoiceEditors(final EStructuralFeature feature) {

		final EnumMap<RouteOption, String> m = new EnumMap<>(RouteOption.class);
		m.put(RouteOption.DIRECT, "Direct");
		m.put(RouteOption.SUEZ, "Suez canal");
		m.put(RouteOption.PANAMA, "Panama canal");
		final Function<Enumerator, String> f = m::get;

		final List<IInlineEditor> editors = new LinkedList<>();
		for (final RouteOption ro : RouteOption.VALUES) {
			editors.add(new EnumCheckboxEditor(feature, ro, "", f));
		}
		return editors;
	}
}

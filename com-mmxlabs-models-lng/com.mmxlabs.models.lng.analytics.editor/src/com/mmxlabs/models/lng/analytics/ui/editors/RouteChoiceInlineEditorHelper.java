/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editors;

import java.util.EnumMap;
import java.util.function.Function;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.EnumCheckboxEditor;

public class RouteChoiceInlineEditorHelper {

	public static void createRouteChoiceEditors(final EStructuralFeature feature, final IInlineEditorContainer detailComposite) {

		final EnumMap<RouteOption, String> m = new EnumMap<>(RouteOption.class);
		m.put(RouteOption.DIRECT, "Direct");
		m.put(RouteOption.SUEZ, "Suez canal");
		m.put(RouteOption.PANAMA, "Panama canal");
		final Function<Enumerator, String> f = m::get;
		for (final RouteOption ro : RouteOption.VALUES) {

			detailComposite.addInlineEditor(new EnumCheckboxEditor(feature, ro, "", f));
		}
	}
}

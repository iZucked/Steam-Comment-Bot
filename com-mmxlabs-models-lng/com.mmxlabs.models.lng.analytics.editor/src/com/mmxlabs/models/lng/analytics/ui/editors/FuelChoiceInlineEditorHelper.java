/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editors;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.EnumCheckboxEditor;

public final class FuelChoiceInlineEditorHelper {

	private FuelChoiceInlineEditorHelper() {
	}

	public static List<IInlineEditor> createFuelChoiceEditors(final EStructuralFeature feature) {

		final EnumMap<FuelChoice, String> m = new EnumMap<>(FuelChoice.class);
		m.put(FuelChoice.NBO_BUNKERS, "NBO + Bunkers");
		m.put(FuelChoice.NBO_FBO, "NBO + FBO");
		final Function<Enumerator, String> f = m::get;

		final List<IInlineEditor> editors = new LinkedList<>();
		for (final FuelChoice ro : FuelChoice.VALUES) {

			editors.add(new EnumCheckboxEditor(feature, ro, "Fuel: ", f));
		}

		return editors;
	}
}

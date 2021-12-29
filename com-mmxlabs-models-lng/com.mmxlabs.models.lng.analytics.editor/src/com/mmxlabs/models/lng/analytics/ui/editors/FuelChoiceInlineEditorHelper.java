/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editors;

import java.util.EnumMap;
import java.util.function.Function;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.EnumCheckboxEditor;

public class FuelChoiceInlineEditorHelper {

	public static void createFuelChoiceEditors(final EStructuralFeature feature, final IInlineEditorContainer detailComposite) {

		final EnumMap<FuelChoice, String> m = new EnumMap<>(FuelChoice.class);
		m.put(FuelChoice.NBO_BUNKERS, "NBO + Bunkers");
		m.put(FuelChoice.NBO_FBO, "NBO + FBO");
		final Function<Enumerator, String> f = m::get;
		for (final FuelChoice ro : FuelChoice.VALUES) {

			detailComposite.addInlineEditor(new EnumCheckboxEditor(feature, ro, "Fuel: ", f));
		}
	}
}

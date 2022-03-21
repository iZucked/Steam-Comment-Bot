/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.common.Pair;

public class YesNoInlineEditor extends ValueListInlineEditor<Object> {

	private static final List<Pair<String, Object>> DEFAULT_VALUES = getDefaultValues();

	public YesNoInlineEditor(final EAttribute feature) {
		super(feature, DEFAULT_VALUES);
	}

	private static List<Pair<String, Object>> getDefaultValues() {
		final ArrayList<Pair<String, Object>> result = new ArrayList<>();
		result.add(Pair.of("No", Boolean.FALSE));
		result.add(Pair.of("Yes", Boolean.TRUE));
		return result;
	}
}

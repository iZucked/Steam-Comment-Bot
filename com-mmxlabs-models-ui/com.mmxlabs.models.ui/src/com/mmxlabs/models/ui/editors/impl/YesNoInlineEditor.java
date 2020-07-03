/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.common.Pair;

/**
 */
public class YesNoInlineEditor extends ValueListInlineEditor {
	private static List<Pair<String, Object>> values = getDefaultValues();

	public YesNoInlineEditor(EAttribute feature) {
		super(feature, values);
	}

	private static List<Pair<String, Object>> getDefaultValues() {
		ArrayList<Pair<String, Object>> result = new ArrayList<>();
		result.add(new Pair<String, Object>("No", Boolean.FALSE));
		result.add(new Pair<String, Object>("Yes", Boolean.TRUE));
		return result;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.impl.ValueListInlineEditor;

/**
 * @author hinton
 *
 */
public class TimeOfDayInlineEditor extends ValueListInlineEditor<Object> {
	private static final List<Pair<String, Object>> DEFAULT_VALUES;
	static {
		DEFAULT_VALUES = new ArrayList<>(24);
		for (int i = 0; i < 24; i++) {
			DEFAULT_VALUES.add(Pair.of(String.format("%02d:00", i), i));
		}
	}

	/**
	 * @param feature
	 * @param DEFAULT_VALUES
	 */
	public TimeOfDayInlineEditor(EStructuralFeature feature) {
		super(feature, DEFAULT_VALUES);
	}

}

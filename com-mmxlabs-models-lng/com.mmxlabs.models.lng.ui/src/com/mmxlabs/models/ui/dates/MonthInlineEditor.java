/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.impl.ValueListInlineEditor;

public class MonthInlineEditor extends ValueListInlineEditor<Integer> {

	private static final List<Pair<String, Integer>> VALUE;
	static {
		VALUE = new ArrayList<>(12);
		for (int i = 0; i < 12; i++) {
			VALUE.add(new Pair<String, Integer>(Month.of(1 + i).getDisplayName(TextStyle.SHORT, Locale.getDefault()), i));
		}
	}

	/**
	 * @param feature
	 * @param VALUE
	 */
	public MonthInlineEditor(ETypedElement feature) {
		super(feature, VALUE);
	}

}

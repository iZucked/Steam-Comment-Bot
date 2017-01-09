/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
public class TimeOfDayInlineEditor extends ValueListInlineEditor {
	final static List<Pair<String, Object>> values;
	static {
		values = new ArrayList<Pair<String, Object>>(24);
		for (int i = 0; i<24; i++) {
			values.add(new Pair<String, Object>(
					String.format("%02d:00", i), i
					));
		}
	}
	/**
	 * @param feature
	 * @param values
	 */
	public TimeOfDayInlineEditor(EStructuralFeature feature) {
		super(feature, values);
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;

public class TimezoneInlineEditor extends ValueListInlineEditor {
	public TimezoneInlineEditor(final EStructuralFeature feature) {
		super(feature, getTimezones());
	}

	public static List<Pair<String, Object>> getTimezones() {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();

		for (final String s : TimeZone.getAvailableIDs()) {
			if (s.indexOf("/") != -1)
				values.add(new Pair<String, Object>(s, s));
		}

		Collections.sort(values, new Comparator<Pair<String, Object>>() {
			@Override
			public int compare(Pair<String, Object> o1, Pair<String, Object> o2) {
				return o1.getFirst().compareTo(o2.getFirst());
			}
		});

		return values;
	}
}

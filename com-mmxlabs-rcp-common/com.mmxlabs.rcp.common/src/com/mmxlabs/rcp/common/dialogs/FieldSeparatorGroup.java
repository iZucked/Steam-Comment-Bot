/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.rcp.common.controls.RadioSelectionGroup;

public class FieldSeparatorGroup extends RadioSelectionGroup {

	public FieldSeparatorGroup(final Composite parent, final String title, final int style) {
		super(parent, title, style, createLabels(), createValues());
	}

	public static String[] createLabels() {
		final List<String> labels = new ArrayList<>(FieldSeparatorChoice.values().length);
		for (final FieldSeparatorChoice choice : FieldSeparatorChoice.values()) {
			labels.add(String.format("%s (\"%s\")", choice.getDisplayName(), choice.getChar()));
		}
		return labels.toArray(new String[labels.size()]);
	}

	public static int[] createValues() {
		final int[] values = new int[FieldSeparatorChoice.values().length];
		for (int i = 0; i < FieldSeparatorChoice.values().length; ++i) {
			values[i] = i;
		}
		return values;
	}
}

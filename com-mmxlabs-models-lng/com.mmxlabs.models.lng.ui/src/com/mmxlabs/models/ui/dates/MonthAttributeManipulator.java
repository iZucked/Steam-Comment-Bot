/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

public class MonthAttributeManipulator extends ValueListAttributeManipulator<Integer> {

	private static final List<Pair<String, Integer>> VALUE;
	static {
		VALUE = new ArrayList<>(13);
		VALUE.add(new Pair<String, Integer>("ANY",0));
		for (int i = 1; i <= 12; i++) {
			VALUE.add(new Pair<String, Integer>(Month.of(i).getDisplayName(TextStyle.SHORT, Locale.getDefault()), i));
		}
	}
	
	public MonthAttributeManipulator(final EAttribute field, final ICommandHandler commandHandler) {
		super(field, commandHandler, VALUE);
	}
}

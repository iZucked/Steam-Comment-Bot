/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.utils;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.base.Joiner;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.NamedObject;

@NonNullByDefault
public class AnalyticsSolutionHelper {

	public static String generateName(final SlotInsertionOptions plan) {

		final List<String> names = new LinkedList<>();
		for (final Slot<?> s : plan.getSlotsInserted()) {
			names.add(s.getName());
		}

		return "Optionise: " + Joiner.on(", ").join(names);
	}

	public static String generateInsertionName(boolean cloud, final List<? extends NamedObject> objects) {

		final List<String> names = new LinkedList<>();
		for (final NamedObject s : objects) {
			names.add(s.getName());
		}
		while (names.remove(null))
			;
		String optionise = "Optionise";
		if (cloud) {
			optionise += " on a cloud";
		}
		return optionise + ": \"" + Joiner.on(", ").join(names) + "\"";
	}

	public static String generateName(final ActionableSetPlan plan) {

		return "Action Plan:";
	}
}

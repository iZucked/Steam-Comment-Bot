package com.mmxlabs.models.lng.analytics.ui.utils;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.base.Joiner;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.Slot;

@NonNullByDefault
public class AnalyticsSolutionHelper {

	public static String generateName(final SlotInsertionOptions plan) {

		final List<String> names = new LinkedList<>();
		for (final Slot s : plan.getSlotsInserted()) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	public static String generateName(final ActionableSetPlan plan) {

		return "Action Plan:";
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class TimezoneInlineEditor extends ValueListInlineEditor {
	public TimezoneInlineEditor(final EMFPath path,
			final EStructuralFeature feature,
			final EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor, getTimezones());
	}

	public static List<Pair<String, Object>> getTimezones() {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();

		for (final String s : TimeZone.getAvailableIDs()) {
			values.add(new Pair<String, Object>(s, s));
		}

		return values;
	}
}

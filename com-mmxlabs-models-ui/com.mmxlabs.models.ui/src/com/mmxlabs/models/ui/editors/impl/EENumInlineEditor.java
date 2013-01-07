/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;

import com.mmxlabs.common.Pair;

public class EENumInlineEditor extends ValueListInlineEditor {
	public EENumInlineEditor(EAttribute feature) {
		super(feature, getValues((EEnum) feature.getEAttributeType()));
	}

	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		for (final EEnumLiteral literal : eenum.getELiterals()) {
			values.add(new Pair<String, Object>(literal.getName(), literal
					.getInstance()));
		}
		return values;
	}
}

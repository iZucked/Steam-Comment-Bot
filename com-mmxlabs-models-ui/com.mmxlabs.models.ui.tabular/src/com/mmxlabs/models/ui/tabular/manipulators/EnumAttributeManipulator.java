/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;

/**
 * Editor for enums
 * 
 * @author hinton
 */
public class EnumAttributeManipulator extends ValueListAttributeManipulator {
	public EnumAttributeManipulator(final EAttribute field, final EditingDomain editingDomain) {
		super(field, editingDomain, getValues((EEnum) field.getEAttributeType()));
	}

	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		for (final EEnumLiteral literal : eenum.getELiterals()) {
			values.add(new Pair<String, Object>(literal.getName(), literal.getInstance()));
		}
		return values;
	}
}

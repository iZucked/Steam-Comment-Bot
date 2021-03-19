/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.ICommandHandler;

/**
 * Editor for enums
 * 
 * @author hinton
 */
public class EnumAttributeManipulator extends ValueListAttributeManipulator<Object> {
	
	public EnumAttributeManipulator(final EAttribute field, final ICommandHandler commandHandler) {
		super(field, commandHandler, getValues((EEnum) field.getEAttributeType()));
	}

	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<>();
		for (final EEnumLiteral literal : eenum.getELiterals()) {
			values.add(Pair.of(literal.getName(), literal.getInstance()));
		}
		return values;
	}
}

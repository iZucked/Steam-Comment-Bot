/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;

import com.mmxlabs.models.ui.editors.ICommandHandler;

/**
 * Sub class of {@link BasicAttributeManipulator} for editing overridable strings.
 * 
 * @author Simon Goodall
 *
 */
public class StringAttributeManipulator extends BasicAttributeManipulator {
	public StringAttributeManipulator(final EStructuralFeature field, final ICommandHandler commandHandler) {
		super(field, commandHandler);
	}

	@Override
	public void doSetValue(final Object object, Object value) {
		if ("".equals(value)) {
			value = SetCommand.UNSET_VALUE;
		}
		super.doSetValue(object, value);
	}

	@Override
	public Object getValue(final Object object) {
		final Object v = super.getValue(object);
		if (v == null || v == SetCommand.UNSET_VALUE) {
			return "";
		}
		return v;
	}

}

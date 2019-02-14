/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Sub class of {@link BasicAttributeManipulator} for editing overridable strings.
 * 
 * @author Simon Goodall
 *
 */
public class StringAttributeManipulator extends BasicAttributeManipulator {
	public StringAttributeManipulator(EStructuralFeature field, EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public void doSetValue(Object object, Object value) {
		if ("".equals(value)) {
			value = SetCommand.UNSET_VALUE;
		}
		super.doSetValue(object, value);
	}

	@Override
	public Object getValue(Object object) {
		Object v = super.getValue(object);
		if (v == null || v == SetCommand.UNSET_VALUE) {
			return "";
		}
		return v;
	}

}

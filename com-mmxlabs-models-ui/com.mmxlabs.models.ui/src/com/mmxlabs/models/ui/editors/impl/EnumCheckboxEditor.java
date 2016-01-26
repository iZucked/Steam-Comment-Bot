/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

public class EnumCheckboxEditor extends BooleanInlineEditor {
	private Enumerator enumValue;
	private String labelPrefix;
	public EnumCheckboxEditor(EStructuralFeature feature, Enumerator enumValue, String labelPrefix) {
		super(feature);
		this.enumValue = enumValue;
		this.labelPrefix = labelPrefix;
	}

	@Override
	protected Object getValue() {
		final Object superValue = super.getValue();
		if (superValue instanceof Collection)
		return ((Collection<?>)superValue).contains(enumValue);
		else return Boolean.FALSE;
	}

	@Override
	protected Command createSetCommand(Object value) {
		if ((Boolean) value) {
			return AddCommand.create(commandHandler.getEditingDomain(), input, feature, enumValue);
		} else {
			return RemoveCommand.create(commandHandler.getEditingDomain(), input, feature, enumValue);
		}
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, MMXRootObject context, EObject input, final Collection<EObject> range) {
		super.display(dialogContext, context, input, range);
		if (label != null) label.setText(labelPrefix + enumValue.getName());
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author hinton
 * 
 */
public abstract class DialogFeatureManipulator extends
		BasicAttributeManipulator {

	/**
	 * @param field
	 * @param editingDomain
	 */
	public DialogFeatureManipulator(EStructuralFeature field,
			EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public CellEditor getCellEditor(Composite c, final Object object) {
		// TODO Auto-generated method stub
		return new DialogCellEditor(c) {
			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				return DialogFeatureManipulator.this.openDialogBox(
						cellEditorWindow, object);
			}

			@Override
			protected void updateContents(Object value) {
				getDefaultLabel().setText(DialogFeatureManipulator.this.renderValue(value));
			}

		};
	}

	@Override
	public String render(final Object object) {
		return renderValue(getValue(object));
	}

	protected abstract Object openDialogBox(Control cellEditorWindow,
			Object object);
	
	protected abstract String renderValue(Object value);
}

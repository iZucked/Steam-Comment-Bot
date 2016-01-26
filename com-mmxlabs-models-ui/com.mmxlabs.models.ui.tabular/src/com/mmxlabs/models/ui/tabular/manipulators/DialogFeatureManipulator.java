/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author hinton
 */
public abstract class DialogFeatureManipulator extends BasicAttributeManipulator {

	/**
	 * @param field
	 * @param editingDomain
	 */
	public DialogFeatureManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	protected CellEditor createCellEditor(final Composite c, final Object object) {
		return new CellEditor(c) {
			private Object value;
			private Composite editor;

			@Override
			protected void doSetValue(final Object value) {
				this.value = value;
				if (label != null) {
					label.setText(renderValue(value));
				}
			}

			private boolean locked = false;
			private Label label;

			@Override
			protected void doSetFocus() {
				if (!locked) {
					locked = true;
					c.getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							final Object o = openDialogBox(editor, object);
							if (o != null) {
								value = o;
								valueChanged(true, true);
							}
							focusLost();
							locked = false;
						}

					});
				}
				if (label != null) {
					label.setText(renderValue(value));
				}
			}

			@Override
			protected boolean dependsOnExternalFocusListener() {
				return false;
			}

			@Override
			protected Object doGetValue() {
				return value;
			}

			@Override
			protected Control createControl(final Composite parent) {
				editor = new Composite(parent, getStyle());

				editor.setBackground(parent.getBackground());

				label = new Label(editor, getStyle());

				return editor;
			}
		};
		// return new DialogCellEditor(c) {
		// @Override
		// protected Object openDialogBox(Control cellEditorWindow) {
		// return DialogFeatureManipulator.this.openDialogBox(
		// cellEditorWindow, object);
		// }
		//
		// @Override
		// protected void updateContents(Object value) {
		// getDefaultLabel().setText(DialogFeatureManipulator.this.renderValue(value));
		// }
		//
		// };
	}

	
	
	@Override
	protected String renderSetValue(Object o, Object setValue) {
		return renderValue(setValue);
	}

	protected abstract Object openDialogBox(Control cellEditorWindow, Object object);

	protected abstract String renderValue(Object value);
}

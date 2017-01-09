/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;
import com.mmxlabs.rcp.common.ViewerHelper;

/**
 * Abstract class for table based inline editors
 * 
 * @author hinton, Simon Goodall
 * 
 */
public abstract class AbstractTableInlineEditor extends BasicAttributeInlineEditor implements ILabelLayoutDataProvidingEditor {
	protected TableViewer tableViewer;
	private Control control;
	protected final Repacker repacker = new Repacker();

	public AbstractTableInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		final Composite composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(1, false));

		@Nullable
		String labelText = getTableLabelText();
		if (labelText != null) {
			toolkit.createLabel(composite, labelText);
		}
		tableViewer = createTable(composite);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		tableViewer.getTable().addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				repacker.repack();
			}
		});

		createButtons(composite);

		control = composite;
		return super.wrapControl(control);
	}

	protected abstract @Nullable String getTableLabelText();

	protected void createButtons(Composite composite) {

	}

	protected abstract TableViewer createTable(final Composite parent);

	@Override
	public Object createLabelLayoutData(final MMXRootObject root, final EObject value, final Control control, final Label label) {
		return new GridData(SWT.RIGHT, SWT.TOP, false, false);
	}

	@Override
	public boolean hasLabel() {
		return false;
	}

	/**
	 * Overrides the default layout data for the editing control, forcing it to span two columns instead of one.
	 */
	@Override
	public Object createLayoutData(final MMXRootObject root, final EObject value, final Control control) {
		final GridData result = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		result.heightHint = 200;
		return result;
	}

	protected class Repacker {
		boolean resizing = false;

		public void repack() {
			if (resizing) {
				return;
			}
			resizing = true;
			for (int i = 0; i < tableViewer.getTable().getColumnCount(); i++) {
				tableViewer.getTable().getColumn(i).pack();
			}
			resizing = false;
		}
	}

	@Override
	protected void updateDisplay(final Object value) {
		tableViewer.setInput(value);
		repacker.repack();
		tableViewer.refresh();
	}

	@Override
	public void notifyChanged(Notification notification) {

		super.notifyChanged(notification);

		ViewerHelper.refresh(tableViewer, true);
	}

	@Override
	public void reallyNotifyChanged(final Notification msg) {
		super.reallyNotifyChanged(msg);

		ViewerHelper.refresh(tableViewer, true);
	}

	/**
	 * Hook a listener into the cell editor to correctly clean up and refresh view state
	 * 
	 * @param editor
	 */
	protected void addCellEditorListener(CellEditor editor) {
		editor.getControl().addListener(SWT.Deactivate, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (event.type == SWT.Deactivate) {
					editor.deactivate();
					editor.dispose();
					tableViewer.refresh();
				}
			}
		});

	}
}

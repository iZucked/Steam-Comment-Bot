/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors.ldd;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;

import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class CellManipulatorEditingSupport extends EditingSupport {
	private final GridTableViewer viewer;
	private final ICellManipulator manipulator;
	private final EMFPath path;
	private boolean lockedForEditing;

	public CellManipulatorEditingSupport(final ColumnViewer columnViewer, final GridTableViewer viewer, final ICellManipulator manipulator, final EMFPath path) {
		super(columnViewer);
		this.viewer = viewer;
		this.manipulator = manipulator;
		this.path = path;
	}

	@Override
	protected boolean canEdit(final Object element) {
		return (lockedForEditing == false) && (manipulator != null) && manipulator.canEdit(path.get((EObject) element));
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		return manipulator.getCellEditor(viewer.getGrid(), path.get((EObject) element));
	}

	@Override
	protected Object getValue(final Object element) {
		return manipulator.getValue(path.get((EObject) element));
	}

	@Override
	protected void setValue(final Object element, final Object value) {
		// a value has come out of the celleditor and is being set on
		// the element.
		if (lockedForEditing) {
			return;
		}
		manipulator.setValue(path.get((EObject) element), value);
		viewer.refresh();
	}
}

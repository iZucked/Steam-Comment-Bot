/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.function.Supplier;

import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.nebula.widgets.grid.Grid;

public class GridViewerEditingSupport extends EditingSupport {
	private final Grid grid;
	private ICellManipulator internalManipulator;
	private final Supplier<ICellManipulator> manipulatorFactory;

	public GridViewerEditingSupport(final ColumnViewer viewer, final Grid grid, final Supplier<ICellManipulator> manipulatorFactory) {
		super(viewer);
		this.grid = grid;
		this.manipulatorFactory = manipulatorFactory;
	}

	private ICellManipulator getManipulator() {
		if (internalManipulator == null) {
			internalManipulator = manipulatorFactory.get();
		}
		return internalManipulator;
	}

	@Override
	protected boolean canEdit(final Object element) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		final ICellManipulator manipulator = getManipulator();
		return manipulator.getCellEditor(grid, element);
	}

	@Override
	protected Object getValue(final Object element) {
		final ICellManipulator manipulator = getManipulator();

		final Object value = manipulator.getValue(element);
		if (value == SetCommand.UNSET_VALUE) {
			return null;
		}
		return value;
	}

	@Override
	protected void setValue(final Object element, final Object value) {
		final ICellManipulator manipulator = getManipulator();

		// a value has come out of the celleditor and is being set on
		// the element.
		// if (lockedForEditing) {
		// return;
		// }
		final Object obj = element;
		manipulator.setParent(element, obj);
		manipulator.setValue(obj, value);
		getViewer().refresh();
	}
}
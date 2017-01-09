/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.tabular.ICellManipulator;

public final class NoEditingCellManipulator implements ICellManipulator {

	public static NoEditingCellManipulator INSTANCE = new NoEditingCellManipulator();

	private NoEditingCellManipulator() {

	}

	@Override
	public void setValue(final Object object, final Object value) {

	}

	@Override
	public Object getValue(final Object object) {
		return null;
	}

	@Override
	public CellEditor getCellEditor(final Composite parent, final Object object) {
		return null;
	}

	@Override
	public boolean canEdit(final Object object) {
		return false;
	}
}

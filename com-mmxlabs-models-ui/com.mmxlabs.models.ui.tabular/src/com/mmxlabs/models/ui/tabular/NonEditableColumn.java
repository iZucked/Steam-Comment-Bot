/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;

public abstract class NonEditableColumn implements ICellManipulator, ICellRenderer {

	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
	}

	@Override
	public void setValue(final Object object, final Object value) {

	}

	@Override
	public CellEditor getCellEditor(final Composite parent, final Object object) {
		return null;
	}

	@Override
	public Object getValue(final Object object) {
		return null;
	}

	@Override
	public boolean canEdit(final Object object) {
		return false;
	}

	@Override
	public boolean isValueUnset(Object object) {
		return false;
	}
	
	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.shiplingo.ui.tableview.ICellRenderer#getFilterValue(java.lang.Object)
	 */
	@Override
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

}

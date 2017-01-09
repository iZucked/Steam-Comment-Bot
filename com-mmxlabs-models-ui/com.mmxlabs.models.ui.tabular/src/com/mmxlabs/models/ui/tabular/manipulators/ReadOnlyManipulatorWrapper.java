/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * An {@link ICellManipulator} and {@link ICellRenderer} wrapper class to take an existing implemention of these interfaces and disable the editing capabilities.
 * 
 * @author Simon Goodall
 * 
 */
public class ReadOnlyManipulatorWrapper<T extends ICellManipulator & ICellRenderer> implements ICellManipulator, ICellRenderer {

	private final T wrapped;

	public ReadOnlyManipulatorWrapper(final T wrapped) {

		this.wrapped = wrapped;
	}

	@Override
	public String render(final Object object) {
		return wrapped.render(object);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		return wrapped.getComparable(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return wrapped.getFilterValue(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return wrapped.getExternalNotifiers(object);
	}

	@Override
	public boolean isValueUnset(final Object object) {
		return wrapped.isValueUnset(object);
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
		return wrapped.getValue(object);
	}

	@Override
	public boolean canEdit(final Object object) {
		return false;
	}
}

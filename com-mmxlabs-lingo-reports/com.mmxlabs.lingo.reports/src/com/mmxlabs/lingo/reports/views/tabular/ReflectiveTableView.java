/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.tabular;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.common.Pair;

/**
 * A {@link ViewPart} which displays stuff from an {@link IReflectiveContentProvider}.
 * 
 * @author hinton
 * 
 */
public abstract class ReflectiveTableView extends ViewPart {
	protected TableViewer viewer;
	private IReflectiveContentProvider contentProvider;

	public void setContentProvider(final IReflectiveContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(contentProvider);

		final Class<?> rowClass = contentProvider.getRowClass();
		final List<Pair<TableField, Method>> accessors = new ArrayList<Pair<TableField, Method>>();
		for (final Method m : rowClass.getMethods()) {
			if (m.isAnnotationPresent(TableField.class)) {
				final TableField tf = m.getAnnotation(TableField.class);
				accessors.add(new Pair<TableField, Method>(tf, m));
			}
		}

		Collections.sort(accessors, new Comparator<Pair<TableField, Method>>() {

			@Override
			public int compare(final Pair<TableField, Method> arg0, final Pair<TableField, Method> arg1) {
				return ((Integer) (arg0.getFirst().order())).compareTo((arg1.getFirst().order()));
			}
		});

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		for (final Pair<TableField, Method> accessor : accessors) {
			final TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(accessor.getFirst().columnText());
			column.setLabelProvider(new ReflectiveLabelProvider(accessor.getSecond()));
			column.getColumn().pack();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		viewer.getTable().setFocus();
	}

}

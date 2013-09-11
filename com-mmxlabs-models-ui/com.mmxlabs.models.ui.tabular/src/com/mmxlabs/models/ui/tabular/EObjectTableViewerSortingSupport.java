/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * Sorting related code for the {@link EObjectTableViewer}
 * 
 * @author Simon Goodall
 * @since 4.0
 * 
 */
public class EObjectTableViewerSortingSupport {

	/**
	 * Overridding sort order of objects. Any change in column sort order will set this back to null.
	 */
	private List<Object> fixedSortOrder = null;
	private final ArrayList<GridColumn> columnSortOrder = new ArrayList<GridColumn>();

	private boolean sortDescending = false;

	public void removeSortableColumn(final GridColumn tColumn) {
		columnSortOrder.remove(tColumn);
	}

	/**
	 * Register a listener on the column header to change sorting on click
	 * 
	 * @param viewer
	 * @param column
	 * @param tColumn
	 * @since 7.0
	 */
	public void addSortableColumn(final ColumnViewer viewer, final GridViewerColumn column, final GridColumn tColumn) {
		columnSortOrder.add(tColumn);

		column.getColumn().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {

				// Sort order changed - clear fixed ordering
				fixedSortOrder = null;

				if (columnSortOrder.get(0) == tColumn) {
					sortDescending = !sortDescending;
				} else {
					sortDescending = false;
					columnSortOrder.get(0).setSort(SWT.NONE);
					columnSortOrder.remove(tColumn);
					columnSortOrder.add(0, tColumn);
				}
				tColumn.setSort(sortDescending ? SWT.UP : SWT.DOWN);
				viewer.refresh(false);
			}
		});
	}

	/**
	 * Set a predefined sort order to override current column sort order. This will be overridden if the column sort order changes.
	 * 
	 * @since 2.0
	 */
	public void setFixedSortOrder(final List<Object> fixedSortOrder) {
		this.fixedSortOrder = fixedSortOrder;
	}

	/**
	 * @since 2.0
	 */
	public List<Object> getFixedSortOrder() {
		return fixedSortOrder;
	}

	public void clearColumnSortOrder() {
		columnSortOrder.clear();
	}

	/**
	 * Create a {@link ViewerComparator} to install in the {@link EObjectTableViewer} to define the sort order
	 * 
	 * @return
	 */
	public ViewerComparator createViewerComparer() {
		return new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object leftObject, final Object rightObject) {

				// If there is a fixed sort order use that.
				if (fixedSortOrder != null) {
					final int idx1 = fixedSortOrder.indexOf(leftObject);
					final int idx2 = fixedSortOrder.indexOf(rightObject);
					return idx1 - idx2;
				}

				final Iterator<GridColumn> iterator = columnSortOrder.iterator();
				int comparison = 0;
				while (iterator.hasNext() && (comparison == 0)) {
					final GridColumn column = iterator.next();
					// additional text rendering information and comparison information
					final ICellRenderer renderer = (ICellRenderer) column.getData(EObjectTableViewer.COLUMN_RENDERER);
					
					final EMFPath sortPath = (EMFPath) column.getData(EObjectTableViewer.COLUMN_SORT_PATH);
					// ???
					final EMFPath path = sortPath != null ? sortPath : (EMFPath) column.getData(EObjectTableViewer.COLUMN_PATH);
					
					final Object leftOwner;
					final Object rightOwner;
					
					if (path == null) {
						leftOwner = leftObject;
						rightOwner = rightObject;
					}
					else {
						leftOwner = path.get((EObject) leftObject);
						rightOwner = path.get((EObject) rightObject);
					}
					
					final Comparable left = renderer.getComparable(leftOwner);
					final Comparable right = renderer.getComparable(rightOwner);
					if (left == null) {
						return -1;
					} else if (right == null) {
						return 1;
					} else {
						comparison = left.compareTo(right);
					}
				}
				return sortDescending ? -comparison : comparison;
			}
		};
	}

	public boolean isSortDescending() {
		return sortDescending;
	}
}

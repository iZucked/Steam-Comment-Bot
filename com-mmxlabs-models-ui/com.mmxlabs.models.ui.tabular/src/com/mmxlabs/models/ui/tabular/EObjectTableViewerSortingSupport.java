/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.models.util.emfpath.IEMFPath;

/**
 * Sorting related code for the {@link EObjectTableViewer}
 * 
 * @author Simon Goodall
 * 
 */
public class EObjectTableViewerSortingSupport {

	/**
	 * Overridding sort order of objects. Any change in column sort order will set this back to null.
	 */
	private List<Object> fixedSortOrder = null;
	private final List<GridColumn> columnSortOrder = new ArrayList<>();

	private boolean sortDescending = false;

	public void removeSortableColumn(final GridColumn tColumn) {
		columnSortOrder.remove(tColumn);
	}

	private boolean sortOnlyOnSelect = false;

	public boolean isSortOnlyOnSelect() {
		return sortOnlyOnSelect;
	}

	public void setSortOnlyOnSelect(boolean sortOnlyOnSelect) {
		this.sortOnlyOnSelect = sortOnlyOnSelect;
	}

	private boolean doSort = false;

	/**
	 * Register a listener on the column header to change sorting on click
	 * 
	 * @param viewer
	 * @param column
	 * @param tColumn
	 */
	public void addSortableColumn(final ColumnViewer viewer, final GridViewerColumn column, final GridColumn tColumn) {
		if (getColumnSortOrder().contains(tColumn)) {
			return;
		}

		getColumnSortOrder().add(tColumn);

		column.getColumn().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				sortColumnsBy(tColumn);
				doSort = true;
				viewer.refresh(false);
				doSort = false;
			}

		});
	}

	public void sortColumnsBy(final GridColumn tColumn) {
		// Sort order changed - clear fixed ordering
		fixedSortOrder = null;
		if (getColumnSortOrder().isEmpty()) {
			return;
		}
		if (getColumnSortOrder().get(0) == tColumn) {
			setSortDescending(!isSortDescending());
		} else {
			setSortDescending(false);
			getColumnSortOrder().get(0).setSort(SWT.NONE);
			getColumnSortOrder().remove(tColumn);
			getColumnSortOrder().add(0, tColumn);
		}
		tColumn.setSort(isSortDescending() ? SWT.UP : SWT.DOWN);
	}
	
	public void sortColumnsBy(final GridColumn tColumn, boolean descending) {
		// Sort order changed - clear fixed ordering
		fixedSortOrder = null;
		if (getColumnSortOrder().isEmpty()) {
			return;
		}
		if (getColumnSortOrder().get(0) == tColumn) {
			setSortDescending(descending);
		} else {
			setSortDescending(descending);
			getColumnSortOrder().get(0).setSort(SWT.NONE);
			getColumnSortOrder().remove(tColumn);
			getColumnSortOrder().add(0, tColumn);
		}
		tColumn.setSort(isSortDescending() ? SWT.UP : SWT.DOWN);
	}

	/**
	 * Set a predefined sort order to override current column sort order. This will be overridden if the column sort order changes.
	 * 
	 */
	public void setFixedSortOrder(final List<Object> fixedSortOrder) {
		this.fixedSortOrder = fixedSortOrder;
	}

	/**
	 */
	public List<Object> getFixedSortOrder() {
		return fixedSortOrder;
	}

	public void clearColumnSortOrder() {
		getColumnSortOrder().clear();
	}

	// BE category (ie bin) support for sorting
	private Function<Object, Integer> categoryFunction = null;

	public void setCategoryFunction(Function<Object, Integer> categoryFunction) {
		this.categoryFunction = categoryFunction;
	}

	public Function<Object, Integer> getCategoryFunction() {
		return categoryFunction;
	}

	private int getCategory(Object object) {
		return categoryFunction.apply(object);
	}

	// BE

	/**
	 * Create a {@link ViewerComparator} to install in the {@link EObjectTableViewer} to define the sort order
	 * 
	 * @return
	 */
	public ViewerComparator createViewerComparer() {
		return new ViewerComparator() {

			@Override
			public void sort(Viewer viewer, Object[] elements) {
				if (!sortOnlyOnSelect || doSort) {
					super.sort(viewer, elements);
				}
			}

			@Override
			// BE for completeness -- this one's not actually used, see compare() below
			public int category(Object object) {
				return getCategory(object);
			}

			@Override
			public int compare(final Viewer viewer, final Object leftObject, final Object rightObject) {
				// BE take the categories (ie bins) into account while
				if (categoryFunction != null) {
					int cat1 = getCategory(leftObject);
					int cat2 = getCategory(rightObject);

					if (cat1 != cat2) {
						return cat1 - cat2;
					}
				}
				// BE

				// If there is a fixed sort order use that.
				if (fixedSortOrder != null) {
					final int idx1 = fixedSortOrder.indexOf(leftObject);
					final int idx2 = fixedSortOrder.indexOf(rightObject);
					return idx1 - idx2;
				}

				final Iterator<GridColumn> iterator = getColumnSortOrder().iterator();
				int comparison = 0;
				while (iterator.hasNext() && (comparison == 0)) {
					final GridColumn column = iterator.next();

					// additional text rendering information and comparison information
					final IComparableProvider renderer = (IComparableProvider) column.getData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER);

					final IEMFPath sortPath = (IEMFPath) column.getData(EObjectTableViewer.COLUMN_SORT_PATH);
					Object leftOwner = null;
					Object rightOwner = null;

					if (sortPath != null) {
						leftOwner = sortPath.get((EObject) leftObject);
						rightOwner = sortPath.get((EObject) rightObject);
					} else {
						Object data = column.getData(EObjectTableViewer.COLUMN_PATH);
						if (data instanceof EMFPath) {
							final EMFPath path = (EMFPath) data;
							leftOwner = path.get((EObject) leftObject);
							rightOwner = path.get((EObject) rightObject);
						} else if (data instanceof EMFPath[]) {

							leftOwner = null;
							rightOwner = null;

							EMFPath[] paths = (EMFPath[]) data;
							for (final EMFPath p : paths) {
								final Object x = p.get((EObject) leftObject);
								if (x != null) {
									leftOwner = x;
									break;
								}
							}

							for (final EMFPath p : paths) {
								final Object x = p.get((EObject) rightObject);
								if (x != null) {
									rightOwner = x;
									break;
								}
							}
						} else {
							leftOwner = leftObject;
							rightOwner = rightObject;

						}

					}
					final Comparable<Object> left = renderer.getComparable(leftOwner);
					final Comparable<Object> right = renderer.getComparable(rightOwner);

					if (left == right) {
						continue;
					}

					if (left == null) {
						return -1;
					} else if (right == null) {
						return 1;
					} else {
						comparison = left.compareTo(right);
					}
				}
				return isSortDescending() ? -comparison : comparison;
			}
		};
	}

	public boolean isSortDescending() {
		return sortDescending;
	}

	public List<GridColumn> getColumnSortOrder() {
		return columnSortOrder;
	}

	public void setSortDescending(boolean sortDescending) {
		this.sortDescending = sortDescending;
	}
}

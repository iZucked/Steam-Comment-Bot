/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * Sorting related code for the {@link EObjectTableViewer}
 * 
 * @author Simon Goodall
 * 
 */
public class EObjectTableViewerSortingSupport {
	private static final Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	/**
	 * Overridding sort order of objects. Any change in column sort order will set this back to null.
	 */
	private List<Object> fixedSortOrder = null;
	private final ArrayList<GridColumn> columnSortOrder = new ArrayList<GridColumn>();

	private boolean sortDescending = false;

	public void removeSortableColumn(final GridColumn tColumn) {
		getColumnSortOrder().remove(tColumn);
	}

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

		column.getColumn().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {

				sortColumnsBy(tColumn);
				viewer.refresh(false);
			}

		});
	}

	public void sortColumnsBy(final GridColumn tColumn) {
		// Sort order changed - clear fixed ordering
		fixedSortOrder = null;
		
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

					final EMFPath sortPath = (EMFPath) column.getData(EObjectTableViewer.COLUMN_SORT_PATH);
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

							leftOwner = null;//leftObject;
							rightOwner =null;// rightObject;

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
					final Comparable left = renderer.getComparable(leftOwner);
					final Comparable right = renderer.getComparable(rightOwner);

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

	public ArrayList<GridColumn> getColumnSortOrder() {
		return columnSortOrder;
	}

	public void setSortDescending(boolean sortDescending) {
		this.sortDescending = sortDescending;
	}
}

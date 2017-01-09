/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IImageProvider;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class GridTableViewerColumnFactory implements IColumnFactory {

	private final GridTableViewer viewer;
	private final EObjectTableViewerSortingSupport sortingSupport;
	private final EObjectTableViewerFilterSupport filterSupport;

	public GridTableViewerColumnFactory(final GridTableViewer viewer, final EObjectTableViewerSortingSupport sortingSupport, final EObjectTableViewerFilterSupport filterSupport) {
		this.viewer = viewer;
		this.sortingSupport = sortingSupport;
		this.filterSupport = filterSupport;
	}

	@Override
	public GridViewerColumn createColumn(final ColumnHandler handler) {
		GridColumnGroup group = handler.block.getOrCreateColumnGroup(viewer.getGrid());

		final String title = handler.title;
		final ICellRenderer formatter = handler.getFormatter();
		final EMFPath[] path = handler.getPaths();
		final ETypedElement[][] features = handler.getFeatures();
		final String tooltip = handler.getTooltip();

		final GridColumn col;
		if (group != null) {
			col = new GridColumn(group, SWT.NONE);
		} else {
			col = new GridColumn(viewer.getGrid(), SWT.NONE);
		}
		final GridViewerColumn column = new GridViewerColumn(viewer, col);
		column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		column.getColumn().setText(title);
		column.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, formatter);

		// Set a default label provider
		column.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {

				Object element = cell.getElement();
				if (element != null) {
					boolean found = false;
					for (final EMFPath p : path) {
						final Object x = p.get((EObject) element);
						if (x != null) {
							element = x;
							found = true;
							break;
						}
					}
					if (!found) {
						element = null;
					}
				}

				if (element instanceof Row) {
					for (final EMFPath p : path) {
						final Object x = p.get((EObject) element);
						if (x != null) {
							element = x;
							break;
						}
					}

				}
				if (col.isVisible()) {
					setRowSpan(formatter, cell, element);
				}
				cell.setText(formatter.render(element));
				if (formatter instanceof IImageProvider) {
					cell.setImage(((IImageProvider) formatter).getImage(cell.getElement()));
				}
			}
		});

		// But try and create an observable based label provider for better data linkage.
		try {
			if (viewer.getContentProvider() instanceof ObservableListContentProvider) {

				final ObservableListContentProvider contentProvider = (ObservableListContentProvider) viewer.getContentProvider();
				// We can pass EOperations into this, if so, throw a class cast exception to break out.

				final IObservableMap[] attributeMap = new IObservableMap[features.length];

				boolean foundFeaturePath = features.length > 0;
				for (int i = 0; i < features.length; ++i) {
					final EStructuralFeature[] f = new EStructuralFeature[features[i].length];
					int idx = 0;
					for (final ETypedElement element : features[i]) {
						if (!(element instanceof EStructuralFeature)) {
							// Fall back to non-listening approach
							throw new ClassCastException();
						}
						f[idx++] = (EStructuralFeature) element;
					}
					// If idx is zero, then we have not found any features, so ignore
					if (idx == 0) {
						foundFeaturePath = false;
					} else {
						attributeMap[i] = EMFProperties.value(FeaturePath.fromList(f)).observeDetail(contentProvider.getKnownElements());
					}
				}

				if (foundFeaturePath) {

					column.setLabelProvider(new ObservableMapCellLabelProvider(attributeMap) {

						@Override
						public void update(final ViewerCell cell) {

							Object element = cell.getElement();
							if (element != null) {
								boolean found = false;
								for (final IObservableMap attributeMap : attributeMaps) {
									if (attributeMap != null) {
										final Object x = attributeMap.get(element);
										if (x != null) {
											element = x;
											found = true;
											break;
										}
									}
								}
								if (!found) {
									element = null;
								}
							}

							if (col.isVisible()) {
								setRowSpan(formatter, cell, element);
							}
							cell.setText(formatter.render(element));

							if (formatter instanceof IImageProvider) {
								cell.setImage(((IImageProvider) formatter).getImage(cell.getElement()));
							}

						}

					}

					);
				}
			}
		} catch (final Exception cce) {
			// Ignore
		}
		final GridColumn tc = column.getColumn();

		tc.setData(ColumnHandler.COLUMN_HANDLER, this);
		tc.setData(EObjectTableViewer.COLUMN_PATH, path);
		tc.setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, formatter);

		if (tooltip != null) {
			column.getColumn().setHeaderTooltip(tooltip);
		}

		if (filterSupport != null) {
			filterSupport.createColumnMnemonics(tc, title);
		}
		if (sortingSupport != null) {
			sortingSupport.addSortableColumn(viewer, column, tc);
		}

		column.getColumn().setVisible(false);

		return column;
	}

	@Override
	public void destroy(final GridViewerColumn gvc) {
		// filterSupport.removeColum(column);
		if (sortingSupport != null) {
			sortingSupport.removeSortableColumn(gvc.getColumn());
		}
		gvc.getColumn().dispose();
	}

	private void setRowSpan(final ICellRenderer formatter, final ViewerCell cell, Object element) {
		if (formatter instanceof IRowSpanProvider) {
			final IRowSpanProvider rowSpanProvider = (IRowSpanProvider) formatter;
			final DataVisualizer dv = viewer.getGrid().getDataVisualizer();
			// final GridItem item = (GridItem) cell.getItem();
			int rowSpan = rowSpanProvider.getRowSpan(cell, element);
			{
				ViewerCell c = cell;
				for (int i = 0; i < rowSpan; i++) {
					dv.setRowSpan((GridItem) c.getItem(), cell.getColumnIndex(), i);
					c = c.getNeighbor(ViewerCell.ABOVE, false);
					// Set row span each time in case the first cell in the range is not visible
				}
				dv.setRowSpan((GridItem) c.getItem(), cell.getColumnIndex(), rowSpan);
			}
		}
	}

}

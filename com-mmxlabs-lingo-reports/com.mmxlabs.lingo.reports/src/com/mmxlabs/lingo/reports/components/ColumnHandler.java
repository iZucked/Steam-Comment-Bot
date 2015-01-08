/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.emf.common.notify.Notifier;
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
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.formatters.IFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.CompiledEMFPath;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class ColumnHandler {
	/**
	 * 
	 */
	private static final String COLUMN_HANDLER = "COLUMN_HANDLER";
	private final IFormatter formatter;
	private final EMFPath[] path;
	public final String title;
	private String tooltip;
	public GridViewerColumn column;
	public int viewIndex;
	public final ColumnBlock block;
	private final ETypedElement[][] features;

	public ColumnHandler(final ColumnBlock block, final IFormatter formatter, final ETypedElement[] features, final String title) {
		super();
		this.formatter = formatter;
		this.features = new ETypedElement[][] { features };
		this.path = new EMFPath[] { new CompiledEMFPath(getClass().getClassLoader(), true, features) };

		this.title = title;
		this.block = block;
	}

	public ColumnHandler(final ColumnBlock block, final IFormatter formatter, final ETypedElement[][] features, final String title) {
		super();
		this.formatter = formatter;
		this.features = features;
		this.path = new EMFPath[features.length];
		for (int i = 0; i < features.length; ++i) {
			path[i] = new CompiledEMFPath(getClass().getClassLoader(), true, features[i]);
		}

		this.title = title;
		this.block = block;
	}

	public GridViewerColumn createColumn(final EObjectTableViewer viewer) {
		final GridViewerColumn column = viewer.addColumn(title, new ICellRenderer() {
			@Override
			public String render(final Object object) {
				return formatter.format(object);
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
				// TODO fix this
				return Collections.emptySet();
			}

			@Override
			public Comparable getComparable(final Object object) {
				return formatter.getComparable(object);
			}

			@Override
			public Object getFilterValue(final Object object) {
				return formatter.getFilterable(object);
			}
		}, NoEditingCellManipulator.INSTANCE, path[0]);

		final GridColumn tc = column.getColumn();
		tc.setData(COLUMN_HANDLER, this);
		this.column = column;

		if (tooltip != null) {
			column.getColumn().setHeaderTooltip(tooltip);
		}

		column.getColumn().setVisible(false);

		return column;
	}

	public GridViewerColumn createColumn(final GridTableViewer viewer) {
		final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText(title);

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

				cell.setText(formatter.format(element));
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

							cell.setText(formatter.format(element));
						}
					}

					);
				}
			}
		} catch (final Exception cce) {
			// Ignore
		}
		final GridColumn tc = column.getColumn();

		tc.setData(COLUMN_HANDLER, this);
		tc.setData(EObjectTableViewer.COLUMN_PATH, path);
		tc.setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, formatter);
		this.column = column;

		if (tooltip != null) {
			column.getColumn().setHeaderTooltip(tooltip);
		}

		column.getColumn().setVisible(false);

		return column;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}
}
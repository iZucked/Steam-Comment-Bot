/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.fleet.formatters.CharterLengthDaysFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.GeneratedCharterDaysFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.GeneratedCharterRevenueFormatter;
import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IImageProvider;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.IColumnFactory;
import com.mmxlabs.models.ui.tabular.columngeneration.IRowSpanProvider;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class DiffingGridTableViewerColumnFactory implements IColumnFactory {

	private final GridTableViewer viewer;
	private final EObjectTableViewerSortingSupport sortingSupport;
	private final EObjectTableViewerFilterSupport filterSupport;

	private static final ImageDescriptor imageDescriptorGreenArrowDown = Activator.getPlugin().getImageDescriptor("icons/green_arrow_down.png");
	private static final ImageDescriptor imageDescriptorGreenArrowUp = Activator.getPlugin().getImageDescriptor("icons/green_arrow_up.png");
	private static final ImageDescriptor imageDescriptorRedArrowDown = Activator.getPlugin().getImageDescriptor("icons/red_arrow_down.png");
	private static final ImageDescriptor imageDescriptorRedArrowUp = Activator.getPlugin().getImageDescriptor("icons/red_arrow_up.png");

	private static final Image cellImageGreenArrowDown = imageDescriptorGreenArrowDown.createImage();
	private static final Image cellImageGreenArrowUp = imageDescriptorGreenArrowUp.createImage();
	private static final Image cellImageRedArrowDown = imageDescriptorRedArrowDown.createImage();
	private static final Image cellImageRedArrowUp = imageDescriptorRedArrowUp.createImage();
	private BooleanSupplier copyPasteMode;
	
	private final TriConsumer<ViewerCell, ColumnHandler, Object> colourProvider;

	public DiffingGridTableViewerColumnFactory(final GridTableViewer viewer, final EObjectTableViewerSortingSupport sortingSupport, final EObjectTableViewerFilterSupport filterSupport,
			BooleanSupplier copyPasteMode, TriConsumer<ViewerCell, ColumnHandler, Object> colourProvider) {
		this.viewer = viewer;
		this.sortingSupport = sortingSupport;
		this.filterSupport = filterSupport;
		this.copyPasteMode = copyPasteMode;
		this.colourProvider = colourProvider;
	}

	private Object computeCompositeRow(final CompositeRow element, final EMFPath[] path, final GridColumn col, final ICellRenderer formatter) {

		Object pinnedElement = null;
		Object previousElement = null;

		if (element instanceof CompositeRow) {
			final Row pinnedRow = ((CompositeRow) element).getPinnedRow();
			final Row previousRow = ((CompositeRow) element).getPreviousRow();

			for (final EMFPath p : path) {
				pinnedElement = p.get((EObject) pinnedRow);
				if (pinnedElement != null) {
					break;
				}
			}

			for (final EMFPath p : path) {
				previousElement = p.get((EObject) previousRow);
				if (previousElement != null) {
					break;
				}
			}

			Object valuePinned = null;
			Object valuePrevious = null;

			if (pinnedElement != null) {
				valuePinned = formatter.getComparable(pinnedElement);
			}

			if (previousElement != null) {
				valuePrevious = formatter.getComparable(previousElement);
			}
			// Those formatters will also return -MAX_VALUE for the reference row
			// Bug ?
			if (formatter instanceof GeneratedCharterDaysFormatter) {
				valuePinned = Double.valueOf(0.0);
			}

			if (formatter instanceof GeneratedCharterRevenueFormatter) {
				valuePinned = Integer.valueOf(0);
			}
			
			if (formatter instanceof CharterLengthDaysFormatter) {
				valuePinned = Double.valueOf(0.0);
			}

			if (valuePrevious instanceof Integer || valuePinned instanceof Integer) {
				int delta = 0;

				if (valuePrevious != null && valuePinned != null) {
					delta = ((int) valuePrevious) - ((int) valuePinned);
				} else if (valuePrevious != null) {
					delta = (int) valuePrevious;
				} else if (valuePinned != null) {
					delta = -((int) valuePinned);
				}
				return delta;
			} else if (valuePrevious instanceof Long || valuePinned instanceof Long) {
				long delta = 0L;
				if (valuePrevious != null && valuePinned != null) {
					delta = ((long) valuePrevious) - ((long) valuePinned);
				} else if (valuePrevious != null) {
					delta = (long) valuePrevious;
				} else if (valuePinned != null) {
					delta = -((long) valuePinned);
				}
				return delta;
			} else if (valuePrevious instanceof Double || valuePinned instanceof Double) {
				final double epsilon = 0.0001f;
				double delta = 0.0f;

				if (valuePrevious != null && valuePinned != null) {
					delta = ((double) valuePrevious) - ((double) valuePinned);
				} else if (valuePrevious != null) {
					delta = (double) valuePrevious;
				} else if (valuePinned != null) {
					delta = -((double) valuePinned);
				}
				return delta;
			} else if (valuePrevious instanceof String || valuePinned instanceof String) {
				String deltaValue = null;
				if (col.getText().compareTo("Scenario") == 0) {
					deltaValue = " ";
				}

				if (col.getText().compareTo("Vessel") == 0) {
					if (valuePinned != null) {
						deltaValue = (String) valuePinned;
					} else {
						deltaValue = (String) valuePrevious;
					}
				}

				if (col.getText().compareTo("L-ID") == 0) {
					if (valuePinned != null) {
						deltaValue = (String) valuePinned;
					} else {
						deltaValue = (String) valuePrevious;
					}
				}

				if (col.getText().compareTo("D-ID") == 0) {
					if (valuePinned != null) {
						deltaValue = (String) valuePinned;
					} else {
						deltaValue = (String) valuePrevious;
					}
				}
				return deltaValue;
			}
		}
		return null;
	}

	@Override
	public GridViewerColumn createColumn(final ColumnHandler handler) {
		final GridColumnGroup group = handler.block.getOrCreateColumnGroup(viewer.getGrid());

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

			public void setIndicationArrow(final ViewerCell cell, final ICellRenderer formatter, final boolean isNegative) {
				if (formatter instanceof CostFormatter) {
					final CostFormatter costFormatter = (CostFormatter) formatter;
					final CostFormatter.Type typeFormatter = costFormatter.getType();
					final String formattedValue = cell.getText();

					if (formattedValue != null) {
						final List<String> nullValues = new ArrayList<>();
						nullValues.add("0");
						nullValues.add("$0");
						nullValues.add("$0mmbtu");
						nullValues.add("0mmbtu");

						if (!nullValues.contains(formattedValue.toLowerCase())) {
							if (typeFormatter == CostFormatter.Type.COST) {
								if (isNegative) {
									cell.setImage(cellImageGreenArrowDown);
								} else if (!formattedValue.contains("-")) {
									cell.setImage(cellImageRedArrowUp);
								}
							} else if (typeFormatter == CostFormatter.Type.REVENUE) {
								if (isNegative) {
									cell.setImage(cellImageRedArrowDown);
								} else if (!formattedValue.contains("-")) {
									cell.setImage(cellImageGreenArrowUp);
								}
							}
						}
					}
				}
			}

			@Override
			public void update(final ViewerCell cell) {

				Object element = cell.getElement();

				if (element instanceof List) {

					if (((List<CompositeRow>) element).size() > 0) {
						// Fetch the first element of the list and pass it through the column formatter
						// to get its type
						final CompositeRow firstCompositeRow = ((List<CompositeRow>) element).get(0);

						Object pinnedElement = null;
						for (final EMFPath p : path) {
							pinnedElement = p.get((EObject) firstCompositeRow.getPinnedRow());
							if (pinnedElement != null) {
								break;
							}
						}

						String deltaValue = "";
						boolean isNegative = false;
						final Object valuePinned = formatter.getComparable(pinnedElement);

						// Sum the value depending on the column type
						if (valuePinned != null) {
							final List<CompositeRow> compositeRows = (List<CompositeRow>) element;
							if (valuePinned instanceof Integer) {
								int accInt = 0;
								for (final CompositeRow compositeRow : compositeRows) {
									boolean included = true;
									for (final ViewerFilter viewerFilter : viewer.getFilters()) {
										included &= viewerFilter.select(viewer, null, compositeRow.getPinnedRow());
										included &= viewerFilter.select(viewer, null, compositeRow.getPreviousRow());
									}

									if (included) {
										final Object res = computeCompositeRow(compositeRow, path, col, formatter);
										if (res instanceof Number) {
											final Number number = (Number) res;
											if (number.intValue() != 0) {
												accInt += number.intValue();
											}
										}
									}
								}
								isNegative = accInt < 0.0;
								if (copyPasteMode.getAsBoolean()) {
									deltaValue = NumberFormat.getInstance().format(accInt);
								} else {
									deltaValue = NumberFormat.getInstance().format(Math.abs(accInt));
								}
							} else if (valuePinned instanceof Long) {
								long accLong = 0L;

								for (final CompositeRow compositeRow : compositeRows) {

									boolean included = true;
									for (final ViewerFilter viewerFilter : viewer.getFilters()) {
										included &= viewerFilter.select(viewer, null, compositeRow.getPinnedRow());
										included &= viewerFilter.select(viewer, null, compositeRow.getPreviousRow());
									}

									if (included) {

										final Object res = computeCompositeRow(compositeRow, path, col, formatter);
										if (res instanceof Number) {
											final Number number = (Number) res;
											if (number.longValue() != 0L) {
												accLong += number.longValue();
											}
										}
									}
								}
								isNegative = accLong < 0.0;
								if (copyPasteMode.getAsBoolean()) {
									deltaValue = NumberFormat.getInstance().format(accLong);
								} else {
									deltaValue = NumberFormat.getInstance().format(Math.abs(accLong));
								}
							} else if (valuePinned instanceof Double) {
								double accDouble = 0.0f;
								for (final CompositeRow compositeRow : compositeRows) {

									boolean included = true;
									for (final ViewerFilter viewerFilter : viewer.getFilters()) {
										included &= viewerFilter.select(viewer, null, compositeRow.getPinnedRow());
										included &= viewerFilter.select(viewer, null, compositeRow.getPreviousRow());
									}

									if (included) {
										final Object res = computeCompositeRow(compositeRow, path, col, formatter);
										if (res instanceof Number) {
											final Number number = (Number) res;
											if (number.doubleValue() != 0.0) {
												accDouble += number.doubleValue();
											}
										}
									}
								}
								isNegative = accDouble < 0.0;
								if (copyPasteMode.getAsBoolean()) {
									deltaValue = NumberFormat.getInstance().format(accDouble);
								} else {
									deltaValue = NumberFormat.getInstance().format(Math.abs(accDouble));
								}
							}
						}

						setRowSpan(formatter, cell, pinnedElement);
						cell.setText(deltaValue);
						if (col.getText().equals("Scenario")) {
							cell.setText("Total Δ");// FM Changed to "Total Δ"
						}
						setIndicationArrow(cell, formatter, isNegative);
					}
				} else if (element instanceof CompositeRow) {
					final CompositeRow compositeRow = (CompositeRow) element;
					final Object deltaValue = computeCompositeRow(compositeRow, path, col, formatter);

					// Get the underlying pinned element object to set the row span in the grid
					Object pinnedElement = null;
					for (final EMFPath p : path) {
						pinnedElement = p.get((EObject) compositeRow.getPinnedRow());
						if (pinnedElement != null) {
							break;
						}
					}
					setRowSpan(formatter, cell, pinnedElement);

					if (col.getText().equals("Scenario")) {
						cell.setText("Δ");
					} else {
						String txt = deltaValue == null ? "" : deltaValue.toString();
						boolean isNegative = false;
						if (deltaValue instanceof Number) {
							final Number num = (Number) deltaValue;
							isNegative = num.doubleValue() < 0.0;
							if (copyPasteMode.getAsBoolean()) {
								if (num instanceof Double) {
									txt = NumberFormat.getInstance().format(num.doubleValue());
								} else {
									txt = NumberFormat.getInstance().format(num.longValue());
								}
							} else {
								if (num instanceof Double) {
									txt = NumberFormat.getInstance().format(Math.abs(num.doubleValue()));
								} else {
									txt = NumberFormat.getInstance().format(Math.abs(num.longValue()));
								}
							}
						}
						cell.setText(txt);
						setIndicationArrow(cell, formatter, isNegative);
					}
				} else {
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
					cell.setImage(null);
					if (formatter instanceof IImageProvider) {
						final Image image = ((IImageProvider) formatter).getImage(cell.getElement());
						if (image != null && !image.isDisposed()) {
							cell.setImage(image);
						}
					}
				}
				
				if (colourProvider != null) {
					colourProvider.accept(cell, handler, element);
				}
			}
		});

		// But try and create an observable based label provider for better data
		// linkage.
		try

		{
			if (viewer.getContentProvider() instanceof ObservableListContentProvider) {

				final ObservableListContentProvider contentProvider = (ObservableListContentProvider) viewer.getContentProvider();
				// We can pass EOperations into this, if so, throw a class cast exception to
				// break out.

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

		if (gvc != null) {
			if (sortingSupport != null) {
				sortingSupport.removeSortableColumn(gvc.getColumn());
			}
			gvc.getColumn().dispose();
		}
	}

	private void setRowSpan(final ICellRenderer formatter, final ViewerCell cell, final Object element) {
		if (formatter instanceof IRowSpanProvider) {
			final IRowSpanProvider rowSpanProvider = (IRowSpanProvider) formatter;
			final DataVisualizer dv = viewer.getGrid().getDataVisualizer();
			// final GridItem item = (GridItem) cell.getItem();
			final int rowSpan = rowSpanProvider.getRowSpan(cell, element);
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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

import com.mmxlabs.lingo.reports.internal.Activator;

public class GridTableViewerColumnFactory implements IColumnFactory {

	private final GridTableViewer viewer;
	private final EObjectTableViewerSortingSupport sortingSupport;
	private final EObjectTableViewerFilterSupport filterSupport;

	private static final ImageDescriptor imageDescriptorSteadyArrow = Activator.getPlugin().getImageDescriptor("icons/steady_arrow.png");
	private static final ImageDescriptor imageDescriptorGreenArrowDown = Activator.getPlugin().getImageDescriptor("icons/green_arrow_down.png");
	private static final ImageDescriptor imageDescriptorGreenArrowUp = Activator.getPlugin().getImageDescriptor("icons/green_arrow_up.png");
	private static final ImageDescriptor imageDescriptorRedArrowDown = Activator.getPlugin().getImageDescriptor("icons/red_arrow_down.png");
	private static final ImageDescriptor imageDescriptorRedArrowUp = Activator.getPlugin().getImageDescriptor("icons/red_arrow_up.png");

	private static final Image cellImageSteadyArrow = imageDescriptorSteadyArrow.createImage();
	private static final Image cellImageGreenArrowDown = imageDescriptorGreenArrowDown.createImage();
	private static final Image cellImageGreenArrowUp = imageDescriptorGreenArrowUp.createImage();
	private static final Image cellImageRedArrowDown = imageDescriptorRedArrowDown.createImage();
	private static final Image cellImageRedArrowUp = imageDescriptorRedArrowUp.createImage();

	public GridTableViewerColumnFactory(final GridTableViewer viewer, final EObjectTableViewerSortingSupport sortingSupport, final EObjectTableViewerFilterSupport filterSupport) {
		this.viewer = viewer;
		this.sortingSupport = sortingSupport;
		this.filterSupport = filterSupport;
	}

	private String computeCompositeRow(CompositeRow element, EMFPath[] path, GridColumn col, ICellRenderer formatter, boolean withFormatting) {

		Object pinnedElement = null;
		Object previousElement = null;

		String deltaValue = "";
		if (element instanceof CompositeRow) {
			Row pinnedRow = ((CompositeRow) element).getPinnedRow();
			Row previousRow = ((CompositeRow) element).getPreviousRow();

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

			if (pinnedElement != null && previousElement != null) {
				Object valuePinned = formatter.getComparable(pinnedElement);
				Object valuePrevious = formatter.getComparable(previousElement);

				// Those formatters will also return -MAX_VALUE for the reference row
				// Bug ?
				if (formatter instanceof GeneratedCharterDaysFormatter) {
					valuePinned = new Double(0);
				}

				if (formatter instanceof GeneratedCharterRevenueFormatter) {
					valuePinned = new Integer(0);
				}

				deltaValue = "";
				if (valuePrevious != null && valuePinned != null) {
					if (valuePrevious instanceof Integer) {
						int delta = ((int) valuePrevious) - ((int) valuePinned);

						if (withFormatting) {
							deltaValue = NumberFormat.getInstance().format(delta);
						} else {
							deltaValue = String.valueOf(delta);
						}
					} else if (valuePrevious instanceof Long) {
						long delta = ((long) valuePrevious) - ((long) valuePinned);

						if (withFormatting) {
							deltaValue = NumberFormat.getInstance().format(delta);
						} else {
							deltaValue = String.valueOf(delta);
						}
					} else if (valuePrevious instanceof Double) {
						double delta = ((double) valuePrevious) - ((double) valuePinned);
						double epsilon = 0.0001f;

						if (withFormatting) {
							deltaValue = NumberFormat.getInstance().format(delta);
						} else {
							deltaValue = String.valueOf(delta);
						}
					} else if (valuePrevious instanceof String) {
						if (col.getText().compareTo("Scenario") == 0) {
							deltaValue = " ";
						}

						if (col.getText().compareTo("Vessel") == 0) {
							deltaValue = (String) valuePinned;
						}

						if (col.getText().compareTo("L-ID") == 0) {
							deltaValue = (String) valuePinned;
						}

						if (col.getText().compareTo("D-ID") == 0) {
							deltaValue = (String) valuePinned;
						}
					}
					return deltaValue;
				}
			}
		}
		return "";
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

			public void setIndicationArrow(ViewerCell cell, ICellRenderer formatter) {
				if (formatter instanceof CostFormatter) {
					CostFormatter costFormatter = (CostFormatter) formatter;
					CostFormatter.Type typeFormatter = costFormatter.getType();
					if (typeFormatter == CostFormatter.Type.COST) {
						// pin pin !
						String formattedValue = cell.getText();
						if (formattedValue != null) {
							List<String> nullValues = new ArrayList<>();
							nullValues.add("0");
							nullValues.add("$0");
							nullValues.add("$0/mmBtu");
							nullValues.add("0mmBtu");

							if (nullValues.contains(formattedValue)) {
								cell.setImage(cellImageSteadyArrow);
							} else {
								if (formattedValue.contains("-")) {
									cell.setImage(cellImageGreenArrowDown);
								} else if (!formattedValue.contains("-")) {
									cell.setImage(cellImageRedArrowUp);
								}
							}
						}

					} else if (typeFormatter == CostFormatter.Type.REVENUE) {
						// pon pon !
						String formattedValue = cell.getText();
						if (formattedValue != null) {
							List<String> nullValues = new ArrayList<>();
							nullValues.add("0");
							nullValues.add("$0");
							nullValues.add("$0/mmBtu");
							nullValues.add("0mmBtu");

							if (nullValues.contains(formattedValue)) {
								cell.setImage(cellImageSteadyArrow);
							} else {
								if (formattedValue.contains("-")) {

									cell.setImage(cellImageGreenArrowUp);
								} else if (!formattedValue.contains("-")) {
									cell.setImage(cellImageRedArrowDown);
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
					int accInt = 0;
					long accLong = 0L;
					double accDouble = 0.0f;

					if (((List<CompositeRow>) element).size() > 0) {
						// Fetch the first element of the list and pass it through the column formatter
						// to get its type
						CompositeRow firstCompositeRow = ((List<CompositeRow>) element).get(0);

						Object pinnedElement = null;
						for (final EMFPath p : path) {
							pinnedElement = p.get((EObject) firstCompositeRow.getPinnedRow());
							if (pinnedElement != null) {
								break;
							}
						}

						String deltaValue = "";
						Object valuePinned = formatter.getComparable(pinnedElement);

						// Sum the value depending on the column type
						if (valuePinned != null) {
							List<CompositeRow> compositeRows = (List<CompositeRow>) element;
							if (valuePinned instanceof Integer) {
								for (CompositeRow compositeRow : compositeRows) {
									boolean included = true;
									for(ViewerFilter viewerFilter: viewer.getFilters()) {
										included &= viewerFilter.select(viewer, null, compositeRow.getPinnedRow());
										included &= viewerFilter.select(viewer, null, compositeRow.getPreviousRow());
									}
									
									if (included) {
										String res = computeCompositeRow(compositeRow, path, col, formatter, false);

										if (res.compareTo("") != 0) {
											accInt += Integer.parseInt(computeCompositeRow(compositeRow, path, col, formatter, false));
										}
									}
								}
								deltaValue = NumberFormat.getInstance().format(accInt);
							} else if (valuePinned instanceof Long) {
								for (CompositeRow compositeRow : compositeRows) {

									boolean included = true;
									for (ViewerFilter viewerFilter : viewer.getFilters()) {
										included &= viewerFilter.select(viewer, null, compositeRow.getPinnedRow());
										included &= viewerFilter.select(viewer, null, compositeRow.getPreviousRow());
									}

									if (included) {

										String res = computeCompositeRow(compositeRow, path, col, formatter, false);
										if (res.compareTo("") != 0) {
											accLong += Long.parseLong(res);
										}
									}
								}
								deltaValue = NumberFormat.getInstance().format(accLong);
							} else if (valuePinned instanceof Double) {
								for (CompositeRow compositeRow : compositeRows) {

									boolean included = true;
									for (ViewerFilter viewerFilter : viewer.getFilters()) {
										included &= viewerFilter.select(viewer, null, compositeRow.getPinnedRow());
										included &= viewerFilter.select(viewer, null, compositeRow.getPreviousRow());
									}

									if (included) {
										String res = computeCompositeRow(compositeRow, path, col, formatter, false);

										if (res.compareTo("") != 0) {
											accDouble += Double.parseDouble(res);
										}
									}
								}
								deltaValue = NumberFormat.getInstance().format(accDouble);
							}
						}

						setRowSpan(formatter, cell, pinnedElement);
						cell.setText(deltaValue);
						if (col.getText().equals("Scenario")) {
							cell.setText("Δ Total");
						}
						setIndicationArrow(cell, formatter);
					}
				} else if (element instanceof CompositeRow) {
					CompositeRow compositeRow = (CompositeRow) element;
					String deltaValue = computeCompositeRow(compositeRow, path, col, formatter, true);

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
						cell.setText(deltaValue);
						setIndicationArrow(cell, formatter);
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
					if (formatter instanceof IImageProvider) {
						cell.setImage(((IImageProvider) formatter).getImage(cell.getElement()));
					}
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

		if (gvc != null) {
			if (sortingSupport != null) {
				sortingSupport.removeSortableColumn(gvc.getColumn());
			}
			gvc.getColumn().dispose();
		}
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

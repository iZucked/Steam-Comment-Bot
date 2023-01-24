/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.MTMResult;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.AsLocalDateFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IComparableProvider;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;

public class MainTableComponent {

	private final @NonNull List<Consumer<MTMModel>> inputWants = new LinkedList<>();
	private Color myColor;
	
	private final List<GridColumnGroup> dynamicColumnGroups = new LinkedList<>();
	private final List<GridColumn> dynamicColumns = new LinkedList<>();
	private GridTreeViewer tableViewer;
	private Map<String, GridColumnGroup> marketCGByName = new HashMap<>();
	private EObjectTableViewerSortingSupport sortingSupport;
	
	public GridTreeViewer getViewer() {
		return this.tableViewer;
	}

	public @NonNull List<Consumer<MTMModel>> getInputWants() {
		return inputWants;
	}

	public void createControls(final Composite main_parent, final MTMView breakEvenModellerView) {
		myColor = new Color(Display.getDefault(), 0, 168, 107);
		Control control = createViewer(main_parent);
		control.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 300).grab(true, true).create());
	}
	
	public void dispose() {
		myColor.dispose();
	}

	private Control createViewer(final Composite parent) {
		tableViewer = new GridTreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(tableViewer);

		GridViewerHelper.configureLookAndFeel(tableViewer);
		tableViewer.getGrid().setHeaderVisible(true);

		tableViewer.getGrid().setAutoHeight(true);
		tableViewer.getGrid().setRowHeaderVisible(true);
		
		createColumn(tableViewer, "Buy", new BuyOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);
		GridViewerColumn gvcd = createColumn(tableViewer, "Date", new AsLocalDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy")), false);
		
		sortingSupport = new EObjectTableViewerSortingSupport();
		tableViewer.setComparator(sortingSupport.createViewerComparer());
		
		final IComparableProvider provider = (m) -> {
			if (m instanceof MTMRow) {
				final BuyOption bo = ((MTMRow)m).getBuyOption();
				if (bo instanceof BuyReference) {
					final LoadSlot ls = ((BuyReference)bo).getSlot();
					if (ls != null) {
						return ls.getWindowStart();
					}
				}
			}
			return null;
		};

		gvcd.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, provider);
		
		sortingSupport.addSortableColumn(tableViewer, gvcd, gvcd.getColumn());
		sortingSupport.setSortDescending(true);
		sortingSupport.sortColumnsBy(gvcd.getColumn());		

		Consumer<MTMModel> refreshDynamicColumnGroups = (mt) -> {
			dynamicColumnGroups.forEach(GridColumnGroup::dispose);
			dynamicColumnGroups.clear();
			marketCGByName.clear();
			
			final List<SpotMarket> markets = mt == null ? Collections.emptyList()
					: mt.getMarkets().stream() //
							.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())) //
							.collect(Collectors.toList());
			
			for (final SpotMarket sm : markets) {
				final GridColumnGroup marketGroup = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER);
				GridViewerHelper.configureLookAndFeel(marketGroup);
				marketGroup.setText(sm.getName());
				dynamicColumnGroups.add(marketGroup);
				marketCGByName.putIfAbsent(sm.getName(), marketGroup);
			}
			
		};
		inputWants.add(refreshDynamicColumnGroups);

		Consumer<MTMModel> refreshDynamicColumns = (m) -> {
			
			dynamicColumns.clear();

			final List<SpotMarket> markets = m == null ? Collections.emptyList()
					: m.getMarkets().stream() //
							.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())) //
							.collect(Collectors.toList());

			for (final SpotMarket sm : markets) {
				if (!sm.isMtm()) {
					continue;
				}
				GridColumnGroup group = marketCGByName.get(sm.getName());

				//Earliest date
				{
					final GridColumn gc = new GridColumn(group, SWT.CENTER | SWT.WRAP);
					final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
					gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
					gvc.getColumn().setText("Date");
					gvc.getColumn().setWordWrap(true);
					gvc.getColumn().setData(sm);
	
					gvc.getColumn().setWidth(120);
					gvc.setLabelProvider(new ColumnLabelProvider() {
						@Override
						public void update(final ViewerCell cell) {
							double price = Double.MIN_VALUE;
							cell.setText("");
	
							final Object element = cell.getElement();
							if (element instanceof MTMRow) {
								final MTMRow row = (MTMRow) element;
								double rowPrice = row.getPrice();
								if (row.getBuyOption() != null) {
									for (final MTMResult result : row.getRhsResults()) {
										if (result.getEarliestETA() == null) continue;
										if (price < result.getEarliestPrice()){
											price = result.getEarliestPrice();
										}
										if (result.getTarget() != sm) continue;
										final ShippingOption so = result.getShipping();
										if (so == null) {
											continue;
										}
										final ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) so;
										final CharterInMarket cim = ecmo.getCharterInMarket();
										if (cim == null) {
											continue;
										}
										cell.setText(formatDate(result.getEarliestETA()));
									}
									highlightCellForeground(sm, cell, price, row);
								}
							}
						}


					});
					dynamicColumns.add(gvc.getColumn());
				}
				//Earliest NetBack price
				{
					final GridColumn gc = new GridColumn(group, SWT.CENTER | SWT.WRAP);
					final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
					gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
					gvc.getColumn().setText("Price");
					gvc.getColumn().setWordWrap(true);
					gvc.getColumn().setData(sm);
	
					gvc.getColumn().setWidth(120);
					gvc.setLabelProvider(new ColumnLabelProvider() {
						@Override
						public void update(final ViewerCell cell) {
							double price = Double.MIN_VALUE;
							cell.setText("");
	
							final Object element = cell.getElement();
							if (element instanceof MTMRow) {
								final MTMRow row = (MTMRow) element;
								if (row.getBuyOption() != null) {
									for (final MTMResult result : row.getRhsResults()) {
										if (result.getEarliestETA() == null) continue;
										if (price < result.getEarliestPrice()){
											price = result.getEarliestPrice();
										}
										if (result.getTarget() != sm) continue;
										ShippingOption so = result.getShipping();
										if (so == null) {
											continue;
										}
										ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) so;
										CharterInMarket cim = ecmo.getCharterInMarket();
										if (cim == null) {
											continue;
										}
										if (result.getTarget() == sm) {
											cell.setText(formatPrice(result.getEarliestPrice()));
										}
									}
									
								}
							}
							if (cell.getText().contains(String.format("$%.2f", price))) {
								cell.setForeground(myColor);
							}
						}
					});
					dynamicColumns.add(gvc.getColumn());
				}
				//Vessel
				{
					final GridColumn gc = new GridColumn(group, SWT.CENTER | SWT.WRAP);
					final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
					gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
					gvc.getColumn().setText("Vessel");
					gvc.getColumn().setWordWrap(true);
					gvc.getColumn().setData(sm);
	
					gvc.getColumn().setWidth(120);
					gvc.setLabelProvider(new ColumnLabelProvider() {
						@Override
						public void update(final ViewerCell cell) {
							double price = Double.MIN_VALUE;
							cell.setText("");
	
							final Object element = cell.getElement();
							if (element instanceof MTMRow) {
								final MTMRow row = (MTMRow) element;
								if (row.getBuyOption() != null) {
									for (final MTMResult result : row.getRhsResults()) {
										if (result.getEarliestETA() == null) continue;
										if (price < result.getEarliestPrice()){
											price = result.getEarliestPrice();
										}
										if (result.getTarget() != sm) continue;
										ShippingOption so = result.getShipping();
										if (so == null) {
											continue;
										}
										ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) so;
										CharterInMarket cim = ecmo.getCharterInMarket();
										if (cim == null) {
											continue;
										}
										if (result.getShippingCost() > 0.0) {
											cell.setText(cim.getName());
										} else {
											cell.setText("Non-shipped");
										}
									}
									highlightCellForeground(sm, cell, price, row);
								}
							}
						}
					});
					dynamicColumns.add(gvc.getColumn());
				}
				//Shipping cost
				{
					final GridColumn gc = new GridColumn(group, SWT.CENTER | SWT.WRAP);
					final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
					gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
					gvc.getColumn().setText("Shipping");
					gvc.getColumn().setWordWrap(true);
					gvc.getColumn().setData(sm);
	
					gvc.getColumn().setWidth(120);
					gvc.setLabelProvider(new ColumnLabelProvider() {
						@Override
						public void update(final ViewerCell cell) {
							double price = Double.MIN_VALUE;
							cell.setText("");
	
							final Object element = cell.getElement();
							if (element instanceof MTMRow) {
								final MTMRow row = (MTMRow) element;
								if (row.getBuyOption() != null) {
									for (final MTMResult result : row.getRhsResults()) {
										if (result.getEarliestETA() == null) continue;
										if (price < result.getEarliestPrice()){
											price = result.getEarliestPrice();
										}
										if (result.getTarget() != sm) continue;
										ShippingOption so = result.getShipping();
										if (so == null) {
											continue;
										}
										ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) so;
										CharterInMarket cim = ecmo.getCharterInMarket();
										if (cim == null) {
											continue;
										}
										if (result.getShippingCost() > 0.0) {
											cell.setText(formatPrice(result.getShippingCost()));
										} else {
											cell.setText("Non-shipped");
										}
									}
									highlightCellForeground(sm, cell, price, row);
								}
							}
						}
					});
					dynamicColumns.add(gvc.getColumn());
				}
			}
		};
		inputWants.add(refreshDynamicColumns);
		tableViewer.setContentProvider(new MTMModelContentProvider());
		inputWants.add(model -> tableViewer.setInput(model));

		return tableViewer.getGrid();
	}

	public void refresh() {
		tableViewer.refresh();
		GridViewerHelper.recalculateRowHeights(tableViewer.getGrid());
	}

	private String formatDate(final LocalDate date) {
		if (date == null) {
			return "";
		}
		return String.format("%s %d", date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),date.getYear()%100);
	}
	
	private String formatPrice(final double price) {
		if (price == 0.0) {
			return "";
		}
		return String.format("$%.2f", price);
	}
	
	@SuppressWarnings("unused")
	private String formatVolume(final int volume) {
		if (volume == 0) {
			return "";
		}
		return String.format("%.2f", ((double)volume)/1000_000L);
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {
		return createColumn(viewer, createLabelProvider(name, renderer, pathObjects), name, renderer, isTree, pathObjects);
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, CellFormatterLabelProvider labelProvider, final String name, final ICellRenderer renderer, final boolean isTree,
			final ETypedElement... pathObjects) {

		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		gvc.getColumn().setTree(isTree);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(250);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(labelProvider);
		return gvc;
	}

	private CellFormatterLabelProvider createLabelProvider(final String name, final ICellRenderer renderer, final ETypedElement... pathObjects) {
		return new CellFormatterLabelProvider(renderer, pathObjects);
	}
	
	public void setFocus() {
		ViewerHelper.setFocus(getViewer());
	}
	
	private void highlightCellForeground(final SpotMarket sm, final ViewerCell cell, double price, final MTMRow row) {
		cell.setForeground(null);
		for (final MTMResult result : row.getRhsResults()) {
			if (result.getEarliestETA() == null) continue;
			if (result.getTarget() != sm) continue;
			final ShippingOption so = result.getShipping();
			if (so == null) {
				continue;
			}
			final ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) so;
			final CharterInMarket cim = ecmo.getCharterInMarket();
			if (cim == null) {
				continue;
			}
			if (result.getEarliestPrice() == price)
				cell.setForeground(myColor);
		}
	}
}

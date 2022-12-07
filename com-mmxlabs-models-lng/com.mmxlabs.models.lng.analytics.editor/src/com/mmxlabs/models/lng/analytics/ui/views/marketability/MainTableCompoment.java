/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityResult;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.properties.ui.DateTimeFormatLabelProvider;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;

public class MainTableCompoment {

	private final @NonNull List<Consumer<MarketabilityModel>> inputWants = new LinkedList<>();

	private final List<GridColumn> dynamicColumns = new LinkedList<>();
	private GridTableViewer tableViewer;
	
	public GridTableViewer getViewer() {
		return this.tableViewer;
	}

	public @NonNull List<Consumer<MarketabilityModel>> getInputWants() {
		return inputWants;
	}

	public void createControls(final Composite mainParent, final MarketabilityView breakEvenModellerView) {

		Control control = createViewer(mainParent);
		control.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 300).grab(true, true).create());
	}

	private Control createViewer(final Composite parent) {
		tableViewer = new GridTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(tableViewer);
		
		assert tableViewer != null;
		GridViewerHelper.configureLookAndFeel(tableViewer);
		tableViewer.getGrid().setHeaderVisible(true);
		tableViewer.getGrid().setAutoHeight(true);
		//tableViewer.getGrid().setRowHeaderVisible(true);
		createColumn(tableViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false).getColumn();
		createPortSlotColumn(tableViewer.getGrid(), "Load", true);
		//createPortSlotColumn(tableViewer.getGrid(), "Discharge", false);
		//createNextEventColumn(tableViewer.getGrid(), "Next Event");

		final GridColumnGroup rhsMarkets = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(rhsMarkets);
		rhsMarkets.setText("Sales markets");

		Consumer<MarketabilityModel> refreshDynamicColumns = (m) -> {
			dynamicColumns.forEach(GridColumn::dispose);
			dynamicColumns.clear();

			final List<SpotMarket> markets = m == null ? Collections.emptyList()
					: m.getMarkets().stream() //
							.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())) //
							.toList();

			for (final SpotMarket sm : markets) {
				GridColumnGroup group = rhsMarkets;

				final GridColumn gc = new GridColumn(group, SWT.CENTER | SWT.WRAP);
				final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
				gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				gvc.getColumn().setText(sm.getName());
				gvc.getColumn().setData(sm);

				gvc.getColumn().setWidth(120);
				gvc.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public void update(final ViewerCell cell) {

						cell.setText("");

						final Object element = cell.getElement();
						if (element instanceof MarketabilityRow row && row.getBuyOption() != null) {
								for (final MarketabilityResult result : row.getRhsResults()) {
									if (result.getTarget() == sm) {
										if (result.getEarliestETA() == null) continue;
										cell.setText(generateString(result));
									}
								}
						}
					}
				});
				dynamicColumns.add(gvc.getColumn());
			}
		};
		inputWants.add(refreshDynamicColumns);
		tableViewer.setContentProvider(new MarketabilityModelContentProvider());
		inputWants.add(model -> tableViewer.setInput(model));

		return tableViewer.getGrid();
	}

	public void refresh() {
		tableViewer.refresh();
		GridViewerHelper.recalculateRowHeights(tableViewer.getGrid());
		tableViewer.getGrid().pack();
	}
	private void createNextEventColumn(Grid parent, String title) {
		GridColumnGroup nextEventColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(nextEventColumn);
		nextEventColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		nextEventColumn.setText(title);
		
		createChildColumn(tableViewer, new ColumnLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
			}
		}, "ID", null, false, nextEventColumn);
		
		createChildColumn(tableViewer, new ColumnLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
			}
		}, "Port", null, false, nextEventColumn);

		createChildColumn(tableViewer, new ColumnLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
			}
		}, "Nom Date", null, false, nextEventColumn);

		createChildColumn(tableViewer, new ColumnLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
			}
		}, "Window", null, false, nextEventColumn);

	}
	
	private GridColumnGroup createPortSlotColumn(Grid parent, String title, boolean isLoadSlot) {
		GridColumnGroup portColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(portColumn);
		portColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		portColumn.setText(title);
		createChildColumn(tableViewer, new ColumnLabelProvider() {
			
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				if(cell.getElement() instanceof MarketabilityRow row) {
					
					if(isLoadSlot && row.getBuyOption() instanceof BuyReference bo ) {
						cell.setText(bo.getSlot().getName());
					} else {
						if(row.getSellOption() instanceof SellReference sr) {
							cell.setText(sr.getSlot().getName());
						}
					}
				}
			}
		}, "ID", null, false, portColumn);
		
		createChildColumn(tableViewer, new ColumnLabelProvider() {
					
					@Override
					public void update(final ViewerCell cell) {
						cell.setText("");
						if(cell.getElement() instanceof MarketabilityRow row) {
							
							if(isLoadSlot && row.getBuyOption() instanceof BuyReference bo ) {
								cell.setText(bo.getSlot().getPort().getName());
							} else {
								if(row.getSellOption() instanceof SellReference sr) {
									cell.setText(sr.getSlot().getPort().getName());
								}
							}
						}
					}
				}, "Port", null, false, portColumn);

		createChildColumn(tableViewer, new ColumnLabelProvider() {
			
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				if(cell.getElement() instanceof MarketabilityRow row) {
					//TODO: TO BE IMPLEMENTED
				}
			}
		}, "Nom Date", null, false, portColumn);
		createChildColumn(tableViewer, new ColumnLabelProvider() {
			
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				if(cell.getElement() instanceof MarketabilityRow row) {
					Slot<?> slot = null;
					if(isLoadSlot && row.getBuyOption() instanceof BuyReference bo ) {
						slot = bo.getSlot();
					} else {
						if(row.getSellOption() instanceof SellReference sr) {
							slot = sr.getSlot();
						}
					}
					if(slot != null) {
						cell.setText(formatWindow(slot.getWindowStart(), slot.getWindowSize(), slot.getWindowSizeUnits()));
					}
				}
			}
		}, "Window", null, false, portColumn);
		
		createChildColumn(tableViewer, new ColumnLabelProvider() {
			
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				if(cell.getElement() instanceof MarketabilityRow row) {
					int duration = 0;
					if(isLoadSlot && row.getBuyOption() instanceof BuyReference bo ) {
						duration = bo.getSlot().getDuration();
						
					} else {
						if(row.getSellOption() instanceof SellReference sr) {
							duration = sr.getSlot().getDuration();
						}
					}
					cell.setText(formatDuration(duration));
				}
			}
		}, "Duration", null, false, portColumn);
		createChildColumn(tableViewer, new ColumnLabelProvider() {
			
			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				if(cell.getElement() instanceof MarketabilityRow row) {
					// TODO: Panama
				}
			}
		}, "Panama", null, false, portColumn );
	
		
		return portColumn;
	}
	
	private String generateString(final MarketabilityResult result) {
		String r="";
		if (result.getEarliestETA() != null) {
			if (result.getEarliestETA().getDayOfYear() == result.getLatestETA().getDayOfYear() && 
					result.getEarliestETA().getYear() == result.getLatestETA().getYear()) {
				r = String.format("%s– %s @%s", //
						formatDate(result.getEarliestETA()), //
						formatVolume(result.getEarliestVolume()),
						formatPrice(result.getEarliestPrice()));
			} else {
				r = String.format("%s– %s @%s%n%s– %s @%s", //
					formatDate(result.getEarliestETA()), //
					formatVolume(result.getEarliestVolume()),//
					formatPrice(result.getEarliestPrice()), //
					formatDate(result.getLatestETA()), //
					formatVolume(result.getLatestVolume()),//
					formatPrice(result.getLatestPrice()) //
					);
			}
		}
		return r;
	}

	@NonNullByDefault
	private String formatWindow(final LocalDate start, int length, final TimePeriod units ) {
		String date = formatDate(start);
		String unitsString = units.getLiteral().toLowerCase().substring(0, 1);
		return String.format("%s +%s%s", date, String.valueOf(length), unitsString);
	}
	
	private String formatDuration(final int duration) {
		return String.format("%dh", duration);
	}
	
	private String formatDate(final LocalDate date) {
		if (date == null) {
			return "";
		}
		return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
	}
	
	private String formatPrice(final double price) {
		if (price == 0.0) {
			return "";
		}
		return String.format("$%.2f", price);
	}
	
	private String formatVolume(final int volume) {
		if (volume == 0) {
			return "";
		}
		return String.format("%.2f", ((double)volume)/1000_000L);
	}

	protected GridViewerColumn createColumn(final GridTableViewer viewer, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {
		return createColumn(viewer, createLabelProvider(name, renderer, pathObjects), name, renderer, isTree, pathObjects);
	}

	protected GridViewerColumn createColumn(final GridTableViewer viewer, CellLabelProvider labelProvider, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {
		
		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		gvc.getColumn().setTree(isTree);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(250);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(labelProvider);
		
		gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		gvc.getColumn().setWidth(120);
		return gvc;
	}
	
	protected GridViewerColumn createChildColumn(final GridTableViewer viewer, CellLabelProvider labelProvider, final String name, final ICellRenderer renderer, final boolean isTree, final GridColumnGroup parent) {
		
		final GridColumn gc = new GridColumn(parent, SWT.CENTER | SWT.WRAP);
		final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
		gvc.getColumn().setTree(isTree);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(250);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(labelProvider);
		
		gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		gvc.getColumn().setWidth(120);
		return gvc;
	}

	private CellFormatterLabelProvider createLabelProvider(final String name, final ICellRenderer renderer, final ETypedElement... pathObjects) {
		return new CellFormatterLabelProvider(renderer, pathObjects) {

			Image imgShippingRoundTrip = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/roundtrip.png").createImage();
			Image imgShippingFleet = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/fleet.png").createImage();

			@Override
			protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

				if (element instanceof RoundTripShippingOption) {
					return imgShippingRoundTrip;
				} else if (element instanceof SimpleVesselCharterOption) {
					return imgShippingFleet;
				}
				return null;
			}

			@Override
			public void dispose() {

				imgShippingRoundTrip.dispose();
				imgShippingFleet.dispose();

				super.dispose();
			}
		};
	}
	
	public void setFocus() {
		ViewerHelper.setFocus(getViewer());
	}
}

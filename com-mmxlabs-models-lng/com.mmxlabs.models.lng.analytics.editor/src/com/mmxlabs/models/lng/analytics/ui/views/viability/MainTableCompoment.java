/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.viability;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ViabilityResult;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;

public class MainTableCompoment {

	private final @NonNull List<Consumer<ViabilityModel>> inputWants = new LinkedList<>();
	
	private LoadSlot selectedLong;

	private final List<GridColumn> dynamicColumns = new LinkedList<>();
	private GridTreeViewer tableViewer;
	
	public GridTreeViewer getViewer() {
		return this.tableViewer;
	}

	public @NonNull List<Consumer<ViabilityModel>> getInputWants() {
		return inputWants;
	}

	public void createControls(final Composite main_parent, final ViabilityView breakEvenModellerView) {

		Control control = createViewer(main_parent);
		control.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 300).grab(true, true).create());
	}

	private Control createViewer(final Composite parent) {
		tableViewer = new GridTreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(tableViewer);

		GridViewerHelper.configureLookAndFeel(tableViewer);
		tableViewer.getGrid().setHeaderVisible(true);

		tableViewer.getGrid().setAutoHeight(true);
		tableViewer.getGrid().setRowHeaderVisible(true);

		// Left hand side buy markets
		// TODO : uncomment if you want to see BUY markets
//		final GridColumnGroup lhsMarkets = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER);
//		GridViewerHelper.configureLookAndFeel(lhsMarkets);
//		lhsMarkets.setText("Purchase markets");
//		{
//			// Dummy column to pin lhs markets to lhs
//			final GridColumn gc = new GridColumn(lhsMarkets, SWT.CENTER);
//			final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
//			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
//			gvc.getColumn().setText("");
//			gvc.getColumn().setResizeable(false);
//			gvc.getColumn().setVisible(true);
//			gvc.getColumn().setWidth(1);
//			gvc.setLabelProvider(new ColumnLabelProvider());
//		}

		//TODO : uncomment if you want to see longs (LoadSlot)
//		createColumn(tableViewer, "Buy", new BuyOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);

//		{
//			final GridViewerColumn gvc = new GridViewerColumn(tableViewer, SWT.NONE);
//			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
//			gvc.getColumn().setText("Price");
//			gvc.getColumn().setWordWrap(true);
//
//			gvc.getColumn().setWidth(60);
//			gvc.setLabelProvider(new ColumnLabelProvider() {
//				@Override
//				public void update(final ViewerCell cell) {
//					cell.setText("");
//
//					final Object element = cell.getElement();
//					if (element instanceof ViabilityRow) {
//						final ViabilityRow row = (ViabilityRow) element;
//						for (final ViabilityResult rs : row.getRhsResults()) {
//							cell.setText(String.format("%.2f", rs.getPrice()));
//						}
//					}
//				}
//			});
//		}

		//TODO : uncomment if you want to see shorts (DischargeSlot)
		// createColumn(tableViewer, "Sell", new SellOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);
		// {
		// final GridViewerColumn gvc = new GridViewerColumn(tableViewer, SWT.NONE);
		// gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		// gvc.getColumn().setText("Price");
		// gvc.getColumn().setWordWrap(true);
		//
		// gvc.getColumn().setWidth(60);
		// gvc.setLabelProvider(new ColumnLabelProvider() {
		// @Override
		// public void update(final ViewerCell cell) {
		//
		// cell.setText("");
		//
		// final Object element = cell.getElement();
		// if (element instanceof ViabilityRow) {
		// final ViabilityRow row = (ViabilityRow) element;
		// for (final ViabilityResult rs : row.getLhsResults()) {
		// cell.setText(String.format("%.2f", rs.getPrice()));
		// }
		// }
		// }
		// });
		// }

		createColumn(tableViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);

		final GridColumnGroup rhsMarkets = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(rhsMarkets);
		rhsMarkets.setText("Sales markets");

		Consumer<ViabilityModel> refreshDynamicColumns = (m) -> {
			dynamicColumns.forEach(GridColumn::dispose);
			dynamicColumns.clear();

			final List<SpotMarket> markets = m == null ? Collections.emptyList()
					: m.getMarkets().stream() //
							.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())) //
							.collect(Collectors.toList());

			for (final SpotMarket sm : markets) {
				GridColumnGroup group = rhsMarkets;
				//TODO : uncomment if you want to see spot market options on both sides
				
//				if (sm instanceof FOBPurchasesMarket || sm instanceof DESPurchaseMarket) {
//					group = lhsMarkets;
//				} else {
//					group = rhsMarkets;
//				}

				final GridColumn gc = new GridColumn(group, SWT.CENTER | SWT.WRAP);
				final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
				gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				gvc.getColumn().setText(sm.getName());
				gvc.getColumn().setWordWrap(true);
				gvc.getColumn().setData(sm);

				gvc.getColumn().setWidth(120);
				gvc.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public void update(final ViewerCell cell) {

						cell.setText("");

						final Object element = cell.getElement();
						if (element instanceof ViabilityRow) {
							final ViabilityRow row = (ViabilityRow) element;
							if (row.getBuyOption() != null) {
								for (final ViabilityResult result : row.getRhsResults()) {
									if (result.getTarget() == sm) {
										if (result.getEarliestETA() == null) continue;
										cell.setText(generateString(result));
//										final double delta = result.getPrice() - result.getReferencePrice();
//										cell.setText(String.format("%s\n%.2f\n%s%.2f", format(result.getEta()), result.getPrice(), delta < 0 ? "↓" : "↑", Math.abs(delta)));
									}
								}
							}
							//TODO : uncomment if you want to see buy options
//							if (row.getSellOption() != null) {
//								for (final ViabilityResult result : row.getLhsResults()) {
//									if (result.getTarget() == sm) {
//										cell.setText(String.format("%s %s", format(result.getEarliestETA()), format(result.getLatestETA())));
//
////										final double delta = result.getPrice() - result.getReferencePrice();
////										cell.setText(String.format("%s\n%.2f\n%s%.2f", format(result.getEta()), result.getPrice(), delta < 0 ? "↓" : "↑", Math.abs(delta)));
//									}
//								}
//							}
						}
					}
				});
				dynamicColumns.add(gvc.getColumn());
			}
		};
		inputWants.add(refreshDynamicColumns);
		tableViewer.setContentProvider(new ViabilityModelContentProvider());
		ViewerFilter filter = new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof ViabilityRow) {
					ViabilityRow vr = (ViabilityRow) element;
					BuyOption bo = vr.getBuyOption();
					if (bo instanceof BuyReference) {
						BuyReference br = (BuyReference) bo;
						if (br.getSlot().equals(selectedLong)){
							return true;
						}
					}
				}
				return false;
			}
		};
		
		tableViewer.setFilters(filter);
		inputWants.add(model -> tableViewer.setInput(model));

		return tableViewer.getGrid();
	}

	public void refresh() {
		tableViewer.refresh();
		GridViewerHelper.recalculateRowHeights(tableViewer.getGrid());
	}
	
	private String generateString(final ViabilityResult result) {
		String r="";
		if (result.getEarliestETA() != null) {
			if (result.getEarliestETA().getDayOfYear() == result.getLatestETA().getDayOfYear() && 
					result.getEarliestETA().getYear() == result.getLatestETA().getYear()) {
				r = String.format("%s– %s @%s", //
						formatDate(result.getEarliestETA()), //
						formatVolume(result.getEarliestVolume()),
						formatPrice(result.getEarliestPrice()));
			} else {
				r = String.format("%s– %s @%s\n%s– %s @%s", //
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
	
	/*
	 * Set the selected LoadSlot
	 */
	public void setSelectedLong(final LoadSlot load) {
		// sets the long from viewer
		selectedLong = load;
		// triggers refresh
		this.refresh();
	}
}

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
import java.util.function.Function;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
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

import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;

public class MainTableCompoment {

	private final @NonNull List<Consumer<MarketabilityModel>> inputWants = new LinkedList<>();

	private final List<GridColumn> dynamicColumns = new LinkedList<>();
	private GridTreeViewer tableViewer;

	public GridTreeViewer getViewer() {
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
		tableViewer = new GridTreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		// ColumnViewerToolTipSupport.enableFor(tableViewer);

		assert tableViewer != null;
		GridViewerHelper.configureLookAndFeel(tableViewer);
		tableViewer.getGrid().setHeaderVisible(true);
		tableViewer.getGrid().setAutoHeight(true);
		// tableViewer.getGrid().setRowHeaderVisible(true);
		createColumn(tableViewer, "Vessel", new ShippingOptionDescriptionFormatter(), false).getColumn();
		createLoadSlotColumn(tableViewer.getGrid());
		createDischargeSlotColumn(tableViewer.getGrid());
		createNextEventColumn(tableViewer.getGrid());

		Consumer<MarketabilityModel> refreshDynamicColumns = (m) -> {
			dynamicColumns.forEach(GridColumn::dispose);
			dynamicColumns.clear();

			final List<SpotMarket> markets = m == null ? Collections.emptyList()
					: m.getMarkets().stream() //
							.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())) //
							.toList();

			for (final SpotMarket sm : markets) {

				final GridColumnGroup datesGroup = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER | SWT.WRAP);
				GridViewerHelper.configureLookAndFeel(datesGroup);
				datesGroup.setText(sm.getName());
				datesGroup.setData(sm);
				var earliestColumn = createChildColumn(tableViewer, new CellLabelProvider() {

					@Override
					public void update(ViewerCell cell) {
						cell.setText("");
						if (cell.getElement() instanceof MarketabilityRow row) {
							row.getRhsResults().stream().filter(x -> x.getTarget() == sm).forEach(res -> cell.setText(formatDate(res.getEarliestETA())));
						}
					}
				}, "Earliest", datesGroup);

				var latestColumn = createChildColumn(tableViewer, new CellLabelProvider() {

					@Override
					public void update(ViewerCell cell) {
						cell.setText("");
						if (cell.getElement() instanceof MarketabilityRow row) {
							row.getRhsResults().stream().filter(x -> x.getTarget() == sm).forEach(res -> cell.setText(formatDate(res.getLatestETA())));
						}

					}
				}, "Latest", datesGroup);
				dynamicColumns.add(earliestColumn.getColumn());
				dynamicColumns.add(latestColumn.getColumn());
				// dynamicColumns.add(gvc.getColumn());
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
	}

	private void createNextEventColumn(Grid parent) {
		GridColumnGroup nextEventColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(nextEventColumn);
		nextEventColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		nextEventColumn.setText("Next Event");

		createChildColumn(tableViewer, row -> {
			if (row.getSellOption() != null && row.getNextSlotVisit() != null) {
				return (row.getNextSlotVisit().getSlotAllocation().getName());
			}
			return "";
		}, "ID", nextEventColumn);

		createChildColumn(tableViewer, row -> {

			if (row.getSellOption() != null && row.getNextSlotVisit() != null) {
				return (row.getNextSlotVisit().getPort().getName());
			}
			return "";
		}, "Port", nextEventColumn);

		createChildColumn(tableViewer, row -> {

			if (row.getSellOption() != null && row.getNextSlotVisit() != null) {
				return (formatDate(row.getNextSlotVisit().getStart().toLocalDate()));
			}
			return "";
		}, "Nom Date", nextEventColumn);

	}

	private GridColumnGroup createLoadSlotColumn(Grid parent) {
		GridColumnGroup portColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(portColumn);
		portColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		portColumn.setText("Load");
		createChildColumn(tableViewer, row -> {

			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getName());
			}
			return "";
		}, "ID", portColumn);

		createChildColumn(tableViewer, row -> {
			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getPort().getName());
			}
			return "";
		}, "Port", portColumn);

		createChildColumn(tableViewer, row -> {
			if (row.getBuyOption() instanceof BuyReference) {
				return (DateTimeFormatter.ofPattern("dd/MM/yyyy").format(row.getBuySlotAllocation().getSlotVisit().getStart()));
			}
			return "";
		}, "Nom Date", portColumn);

		createChildColumn(tableViewer, row -> {
			if (row.getBuyOption() instanceof BuyReference bo) {
				int duration = bo.getSlot().getDuration();
				return (formatDuration(duration));
			}
			return "";
		}, "Duration", portColumn);
		createChildColumn(tableViewer, row -> {
			// TODO: PLACEHOLDER
			return (DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()));
		}, "Panama", portColumn);

		return portColumn;
	}

	private GridColumnGroup createDischargeSlotColumn(Grid parent) {
		GridColumnGroup portColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(portColumn);
		portColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		portColumn.setText("Discharge");
		createChildColumn(tableViewer, row -> {

			if (row.getSellOption() instanceof SellReference sr) {
				return (sr.getSlot().getCounterparty());
			}
			return "";
		}, "C/P", portColumn);

		createChildColumn(tableViewer, row -> {
			if (row.getBuyOption() instanceof BuyReference) {
				return (formatDate(row.getSellSlotAllocation().getSlotVisit().getStart().toLocalDate()));
			}
			return "";
		}, "Nom Date", portColumn);
		createChildColumn(tableViewer, row -> {
			// TODO: PLACEHOLDER
			return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now());
		}, "Panama", portColumn);

		return portColumn;
	}

	private String formatDuration(final int duration) {
		return String.format("%dh", duration);
	}

	@SuppressWarnings("null")
	@NonNull
	private String formatDate(final LocalDate date) {
		if (date == null) {
			return "";
		}
		return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {
		return createColumn(viewer, createLabelProvider(name, renderer, pathObjects), name, renderer, isTree, pathObjects);
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, CellLabelProvider labelProvider, final String name, final ICellRenderer renderer, final boolean isTree,
			final ETypedElement... pathObjects) {

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

	protected GridViewerColumn createChildColumn(final GridTreeViewer viewer, CellLabelProvider provider, final String name, final GridColumnGroup parent) {

		final GridColumn gc = new GridColumn(parent, SWT.CENTER | SWT.WRAP);
		final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
		gvc.getColumn().setTree(false);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		// FIXME: DOESN'T SCROLL WHEN USING A LARGER COLUMN DEFAULT WIDTH
		// gvc.getColumn().setWidth(90);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(provider);
		gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		return gvc;
	}

	protected GridViewerColumn createChildColumn(final GridTreeViewer viewer, Function<MarketabilityRow, String> labelFunction, final String name, final GridColumnGroup parent) {

		return createChildColumn(viewer, new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() instanceof MarketabilityRow row) {
					cell.setText(labelFunction.apply(row));
				}

			}
		}, name, parent);
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

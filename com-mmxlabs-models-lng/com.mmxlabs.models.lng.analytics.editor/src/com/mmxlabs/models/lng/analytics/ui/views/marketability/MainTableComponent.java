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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;

public class MainTableComponent {

	private final @NonNull List<Consumer<MarketabilityModel>> inputWants = new LinkedList<>();

	private final List<Item> dynamicColumns = new LinkedList<>();
	private GridTreeViewer tableViewer;

	public GridTreeViewer getViewer() {
		return this.tableViewer;
	}

	public @NonNull List<Consumer<MarketabilityModel>> getInputWants() {
		return inputWants;
	}

	public void createControls(final Composite mainParent, final MarketabilityView breakEvenModellerView) {
		final Composite vesselSpeedComposite = new Composite(mainParent, SWT.NONE);
		vesselSpeedComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		vesselSpeedComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());
		final Label lbl = new Label(vesselSpeedComposite, SWT.NONE);
		lbl.setText("Vessel speed:");
		lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());
		final Text vesselSpeedText = new Text(vesselSpeedComposite, SWT.SINGLE | SWT.BORDER);
		vesselSpeedText.setEditable(true);
		vesselSpeedText.setTextLimit(2);
		vesselSpeedText.addVerifyListener(x -> x.doit = x.text.matches("\\d*"));
		
		Control control = createViewer(mainParent);
		control.setLayoutData(GridDataFactory.fillDefaults().minSize(300, 300).grab(true, true).create());
	}

	private Control createViewer(final Composite parent) {
		tableViewer = new GridTreeViewer(parent, SWT.BORDER|SWT.H_SCROLL | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(tableViewer);

		assert tableViewer != null;
		GridViewerHelper.configureLookAndFeel(tableViewer);
		tableViewer.getGrid().setHeaderVisible(true);
		tableViewer.getGrid().setAutoHeight(true);

		createColumn(tableViewer, "Vessel", new ShippingOptionDescriptionFormatter(), false).getColumn();
		createLoadSlotColumn(tableViewer.getGrid());
		createDischargeSlotColumn(tableViewer.getGrid());
		createNextEventColumn(tableViewer.getGrid());

		Consumer<MarketabilityModel> refreshDynamicColumns = (m) -> {
			dynamicColumns.forEach(Item::dispose);
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
							row.getResult().getRhsResults().stream().filter(x -> x.getTarget() == sm).forEach(res -> cell.setText(formatDate(res.getEarliestETA())));
						}
					}
				}, "Earliest", datesGroup);

				var latestColumn = createChildColumn(tableViewer, new CellLabelProvider() {

					@Override
					public void update(ViewerCell cell) {
						cell.setText("");
						if (cell.getElement() instanceof MarketabilityRow row) {
							row.getResult().getRhsResults().stream().filter(x -> x.getTarget() == sm).forEach(res -> cell.setText(formatDate(res.getLatestETA())));
						}

					}
				}, "Latest", datesGroup);
				dynamicColumns.add(earliestColumn.getColumn());
				dynamicColumns.add(latestColumn.getColumn());
				dynamicColumns.add(datesGroup);
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

		createChildColumn(tableViewer, "ID", nextEventColumn, row -> {
			if (row.getSellOption() != null) {
				if(row.getResult().getNextEvent() instanceof MarketabilityAssignableElement assignable ) {
					if (assignable.getElement() instanceof VesselEvent event) {
						return (event.getName());
					} else if(assignable.getElement() instanceof Cargo cargo) {
						return (cargo.getLoadName());
					}
				}
			}
			return "";
		});

		createChildColumn(tableViewer, "Port", nextEventColumn, row -> {

			if (row.getSellOption() != null) {
				if(row.getResult().getNextEvent() instanceof MarketabilityAssignableElement assignable ) {
					Port p = null;
					if (assignable.getElement() instanceof VesselEvent event) {
						p = event.getPort();
					} else if(assignable.getElement() instanceof Cargo cargo) {
						p = cargo.getSortedSlots().get(0).getPort();
					}
					return (p != null) ? p.getName() : "";
				}
			}
			return "";
		});

		createChildColumn(tableViewer, "Date", nextEventColumn, row -> {

			if (row.getSellOption() != null && row.getResult() != null) {
				if(row.getResult().getNextEvent() != null) {
					return formatDate(row.getResult().getNextEvent().getStart().toLocalDate());
				}			}
			return "";
		});

	}

	private GridColumnGroup createLoadSlotColumn(Grid parent) {
		GridColumnGroup portColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(portColumn);
		portColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		portColumn.setText("Load");
		createChildColumn(tableViewer, "ID", portColumn, row -> {

			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getName());
			}
			return "";
		});

		createChildColumn(tableViewer, "Port", portColumn, row -> {
			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getPort().getName());
			}
			return "";
		});

		createChildColumn(tableViewer, "Date", portColumn, row -> {
			if (row.getResult().getBuyDate() != null) {
				return (DateTimeFormatter.ofPattern("dd/MM/yyyy").format(row.getResult().getBuyDate()));
			}
			return "";
		});
		createChildColumn(tableViewer, "Panama", portColumn, row -> {
			if (row.getResult().getLadenPanama() != null) {
				return formatDate(row.getResult().getLadenPanama());
			}
			return "";
		});

		return portColumn;
	}

	private GridColumnGroup createDischargeSlotColumn(Grid parent) {
		GridColumnGroup portColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(portColumn);
		portColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		portColumn.setText("Discharge");
		createChildColumn(tableViewer, "C/P", portColumn, row -> {

			if (row.getSellOption() instanceof SellReference sr) {
				return (sr.getSlot().getCounterparty());
			}
			return "";
		});

		createChildColumn(tableViewer, "Date", portColumn, row -> {
			if (row.getResult().getSellDate() != null) {
				return (formatDate(row.getResult().getSellDate()));
			}
			return "";
		});
		createChildColumn(tableViewer, "Panama", portColumn, row -> {
			if (row.getResult().getBallastPanama() != null) {
				return formatDate(row.getResult().getBallastPanama());
			}
			return "";
		});

		return portColumn;
	}

	@SuppressWarnings("null")
	private @NonNull String formatDate(final LocalDate date) {
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
		gvc.getColumn().setWidth(90);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(provider);
		gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		return gvc;
	}

	protected GridViewerColumn createChildColumn(final GridTreeViewer viewer, final String name, final GridColumnGroup parent, Function<MarketabilityRow, String> labelFunction) {

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

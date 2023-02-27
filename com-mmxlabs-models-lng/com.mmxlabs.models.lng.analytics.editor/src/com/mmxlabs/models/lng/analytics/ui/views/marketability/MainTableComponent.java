/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DefaultCellRenderer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;

public class MainTableComponent {

	private final @NonNull List<Consumer<MarketabilityModel>> inputWants = new LinkedList<>();

	private final List<Item> dynamicColumns = new LinkedList<>();
	private EObjectTableViewer tableViewer;
	private Text vesselSpeedText;
	private LocalResourceManager localResourceManager;
	private ISelectionChangedListener tableSelectionChangedListener;

	public GridTreeViewer getViewer() {
		return this.tableViewer;
	}

	public @NonNull List<Consumer<MarketabilityModel>> getInputWants() {
		return inputWants;
	}

	public @NonNull Optional<Integer> getVesselSpeed() {
		String speed = vesselSpeedText.getText();
		try {
			return Optional.of(Integer.parseInt(speed));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	public void createControls(final Composite mainParent, final MarketabilityView marketabilityModellerView) {
		final Composite vesselSpeedComposite = new Composite(mainParent, SWT.NONE);
		vesselSpeedComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		vesselSpeedComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.END).minSize(1000, -1).create());
		final Label lbl = new Label(vesselSpeedComposite, SWT.NONE);
		lbl.setText("Vessel speed:");
		lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());
		vesselSpeedText = new Text(vesselSpeedComposite, SWT.SINGLE | SWT.BORDER);
		vesselSpeedText.setEditable(true);
		vesselSpeedText.setTextLimit(2);
		vesselSpeedText.setMessage("max");
		vesselSpeedText.setData(vesselSpeedText.getText());

		vesselSpeedText.addVerifyListener(x -> x.doit = x.text.matches("\\d*"));
		final Label knotsLabel = new Label(vesselSpeedComposite, SWT.NONE);
		knotsLabel.setText("kts");
		Control control = createViewer(mainParent);

		control.setLayoutData(GridDataFactory.fillDefaults().minSize(0, 0).grab(true, true).create());

		final ESelectionService service = marketabilityModellerView.getSite().getService(ESelectionService.class);
		tableSelectionChangedListener = x -> {
			Object obj = x.getStructuredSelection().getFirstElement();
			if (obj instanceof MarketabilityRow row && row.getBuyOption() instanceof BuyReference br) {
				service.setPostSelection(br.getSlot());
			}
		};
		tableViewer.addSelectionChangedListener(tableSelectionChangedListener);
	}

	private Control createViewer(final Composite parent) {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		tableViewer = new EObjectTableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.init(new MarketabilityModelContentProvider(), null);
		ColumnViewerToolTipSupport.enableFor(tableViewer);

		assert tableViewer != null;
		GridViewerHelper.configureLookAndFeel(tableViewer);
		tableViewer.getGrid().setHeaderVisible(true);
		tableViewer.getGrid().setAutoHeight(true);

		createVesselColumn();
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

			int i = 0;
			for (final SpotMarket sm : markets) {

				final GridColumnGroup datesGroup = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER | SWT.WRAP);
				GridViewerHelper.configureLookAndFeel(datesGroup);
				datesGroup.setText(sm.getName());
				datesGroup.setData(sm);
				final Color backgroundColour = localResourceManager.createColor(new RGB(245, 245, 245));
				final boolean hasColouredBackground = i % 2 == 0;
				@SuppressWarnings("null")
				var earliestColumn = createSortableChildColumn(tableViewer, new CellLabelProvider() {

					@Override
					public void update(ViewerCell cell) {
						cell.setText("");
						if (hasColouredBackground) {
							cell.setBackground(backgroundColour);
						}
						if (cell.getElement() instanceof MarketabilityRow row) {
							for (var result : row.getResult().getRhsResults()) {
								if (result.getTarget() == sm && result.getEarliestETA() != null) {
									String text = formatDate(result.getEarliestETA().toLocalDate());
									text += " " + formatTime(result.getEarliestETA().toLocalTime());
									cell.setText(text);
								}
							}
						}
					}
				}, row -> row.getResult().getRhsResults().stream()//
						.filter(res -> res.getTarget() == sm && res.getEarliestETA() != null)//
						.map(x -> x.getEarliestETA().toLocalDateTime())//
						.findAny()//
						.orElse(LocalDateTime.MIN), "Earliest", datesGroup);

				@SuppressWarnings("null")
				var latestColumn = createSortableChildColumn(tableViewer, new CellLabelProvider() {

					@Override
					public void update(ViewerCell cell) {
						cell.setText("");
						if (hasColouredBackground) {
							cell.setBackground(backgroundColour);
						}
						if (cell.getElement() instanceof MarketabilityRow row) {
							for (var result : row.getResult().getRhsResults()) {
								if (result.getTarget() == sm && result.getLatestETA() != null) {
									String text = formatDate(result.getLatestETA().toLocalDate());
									text += " " + formatTime(result.getLatestETA().toLocalTime());
									cell.setText(text);
								}
							}
						}

					}
				}, row -> row.getResult().getRhsResults().stream()//
						.filter(res -> res.getTarget() == sm && res.getLatestETA() != null)//
						.map(x -> x.getLatestETA().toLocalDateTime())//
						.findAny()//
						.orElse(LocalDateTime.MIN), "Latest", datesGroup);

				dynamicColumns.add(earliestColumn.getColumn());
				dynamicColumns.add(latestColumn.getColumn());
				dynamicColumns.add(datesGroup);

				i++;
			}
		};
		inputWants.add(refreshDynamicColumns);
		tableViewer.setContentProvider(new MarketabilityModelContentProvider());
		inputWants.add(model -> {
			tableViewer.setInput(model);
			tableViewer.refresh();
		});
		return tableViewer.getGrid();
	}

	public void refresh() {
		tableViewer.refresh();
		GridViewerHelper.recalculateRowHeights(tableViewer.getGrid());

	}

	private void createVesselColumn() {
		createSortableColumn(tableViewer, "Vessel", new BaseFormatter() {
			@Override
			public @Nullable String render(Object object) {
				if (object instanceof MarketabilityRow row) {
					final ShippingOption shipping = row.getShipping();
					if (shipping instanceof ExistingVesselCharterOption option) {
						final VesselCharter availability = option.getVesselCharter();
						String vesselName = "<No vessel>";
						if (availability != null) {
							Vessel vessel = availability.getVessel();
							if (vessel != null) {
								final String s = vessel.getName();
								if (s != null && !s.trim().isEmpty()) {
									vesselName = s;
								}
							}
						}
						return vesselName;
					}
				}
				return "";
			}

			@Override
			public Comparable<?> getComparable(Object object) {
				return render(object);
			}
		}, false);

	}

	private void createNextEventColumn(Grid parent) {
		GridColumnGroup nextEventColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(nextEventColumn);
		nextEventColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		nextEventColumn.setText("Next Event");

		createSortableChildColumn(tableViewer, "ID", nextEventColumn, row -> {
			if (row.getSellOption() != null) {
				if (row.getResult().getNextEvent() instanceof MarketabilityAssignableElement assignable) {
					if (assignable.getElement() instanceof VesselEvent event) {
						return (event.getName());
					} else if (assignable.getElement() instanceof Cargo cargo) {
						return (cargo.getLoadName());
					}
				}
			}
			return "";
		}, row -> {
			if (row.getSellOption() != null && row.getResult().getNextEvent() instanceof MarketabilityAssignableElement assignable) {
				if (assignable.getElement() instanceof VesselEvent event) {
					return (event.getName());
				} else if (assignable.getElement() instanceof Cargo cargo) {
					return (cargo.getLoadName());
				}
			}
			return "";
		});

		createSortableChildColumn(tableViewer, "Port", nextEventColumn, row -> {

			if (row.getSellOption() != null && row.getResult().getNextEvent() instanceof MarketabilityAssignableElement assignable) {
				Port p = null;
				if (assignable.getElement() instanceof VesselEvent event) {
					p = event.getPort();
				} else if (assignable.getElement() instanceof Cargo cargo) {
					p = cargo.getSortedSlots().get(0).getPort();
				}
				return (p != null) ? p.getName() : "";
			}
			return "";
		}, row -> {
			if (row.getSellOption() != null && row.getResult().getNextEvent() instanceof MarketabilityAssignableElement assignable) {
				Port p = null;
				if (assignable.getElement() instanceof VesselEvent event) {
					p = event.getPort();
				} else if (assignable.getElement() instanceof Cargo cargo) {
					p = cargo.getSortedSlots().get(0).getPort();
				}
				return (p != null) ? p.getName() : "";
			}

			return "";
		});

		createSortableChildColumn(tableViewer, "Date", nextEventColumn, row -> {

			if (row.getSellOption() != null && row.getResult() != null && row.getResult().getNextEvent() != null) {
				return formatDate(row.getResult().getNextEvent().getStart().toLocalDate());
			}
			return "";
		}, row -> {
			if (row.getSellOption() != null && row.getResult() != null && row.getResult().getNextEvent() != null) {
				return row.getResult().getNextEvent().getStart();
			}
			return LocalDateTime.MIN;
		});

	}

	private GridColumnGroup createLoadSlotColumn(Grid parent) {
		GridColumnGroup portColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(portColumn);
		portColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		portColumn.setText("Load");
		createSortableChildColumn(tableViewer, "ID", portColumn, row -> {

			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getName());
			}
			return "";
		}, row -> {
			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getName());
			}
			return "";
		});

		createSortableChildColumn(tableViewer, "Port", portColumn, row -> {
			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getPort().getName());
			}
			return "";
		}, row -> {
			if (row.getBuyOption() instanceof BuyReference bo) {
				return (bo.getSlot().getPort().getName());
			}
			return "";
		});

		createSortableChildColumn(tableViewer, "Date", portColumn, row -> {
			if (row.getResult().getBuyDate() != null) {
				return (formatDate(row.getResult().getBuyDate().toLocalDate()));
			}
			return "";
		}, row -> {
			if (row.getResult().getBuyDate() != null) {
				return row.getResult().getBuyDate();
			}
			return LocalDateTime.MIN;
		});
		createSortableChildColumn(tableViewer, "Panama", portColumn, row -> {
			if (row.getResult().getLadenPanama() != null) {
				return formatDate(row.getResult().getLadenPanama().toLocalDate());
			}
			return "";
		}, row -> {
			if (row.getResult().getLadenPanama() != null) {
				return row.getResult().getLadenPanama();
			}
			return LocalDateTime.MIN;
		});

		return portColumn;
	}

	private GridColumnGroup createDischargeSlotColumn(Grid parent) {
		GridColumnGroup portColumn = new GridColumnGroup(parent, SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(portColumn);
		portColumn.getHeaderRenderer().setHorizontalAlignment(SWT.CENTER);
		portColumn.setText("Discharge");
		createSortableChildColumn(tableViewer, "C/P", portColumn, row -> {

			if (row.getSellOption() instanceof SellReference sr) {
				return sr.getSlot().getSlotOrDelegateCounterparty();
			}
			return "";
		}, row -> {
			if (row.getSellOption() instanceof SellReference sr) {
				return Objects.requireNonNullElse(sr.getSlot().getSlotOrDelegateCounterparty(), "");
			}
			return "";
		});

		createSortableChildColumn(tableViewer, "Date", portColumn, row -> {
			if (row.getResult().getSellDate() != null) {
				return (formatDate(row.getResult().getSellDate().toLocalDate()));
			}
			return "";
		}, row -> {
			if (row.getResult().getSellDate() != null) {
				return row.getResult().getSellDate();
			}
			return LocalDateTime.MIN;
		});
		createSortableChildColumn(tableViewer, "Panama", portColumn, row -> {
			if (row.getResult().getBallastPanama() != null) {
				return formatDate(row.getResult().getBallastPanama().toLocalDate());
			}
			return "";
		}, row -> {
			if (row.getResult().getBallastPanama() != null) {
				return row.getResult().getBallastPanama();
			}
			return LocalDateTime.MIN;
		});

		return portColumn;
	}

	
	public void selectRowWithLoad(LoadSlot slot) {
		Object input = tableViewer.getInput();
		if(input instanceof MarketabilityModel model) {
			Optional<@NonNull MarketabilityRow> loadRow = model.getRows().stream().filter(x -> 
				x.getBuyOption() instanceof BuyReference br && br.getSlot() == slot
			).findAny();
			if(loadRow.isPresent()) {
				tableViewer.setSelection(new StructuredSelection(loadRow.get()), true);
			}
		}
			//tableViewer.setSelection(null, false);
	}
	@SuppressWarnings("null")
	private @NonNull String formatDate(final LocalDate date) {
		if (date == null) {
			return "";
		}
		return DateTimeFormatter.ofPattern("dd/MM/yy").format(date);
	}

	@SuppressWarnings("null")
	private @NonNull String formatTime(final LocalTime time) {
		if (time == null) {
			return "";
		}
		return DateTimeFormatter.ofPattern("HH:mm").format(time);
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

	protected GridViewerColumn createSortableColumn(final EObjectTableViewer viewer, CellLabelProvider labelProvider, final String name, final ICellRenderer renderer, final boolean isTree,
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

		gvc.getColumn().setCellRenderer(new DefaultCellRenderer());
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, renderer);
		viewer.getSortingSupport().addSortableColumn(viewer, gvc, gvc.getColumn());
		return gvc;
	}

	protected GridViewerColumn createSortableColumn(final EObjectTableViewer viewer, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {

		return createSortableColumn(viewer, createLabelProvider(name, renderer, pathObjects), name, renderer, isTree, pathObjects);
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

	protected GridViewerColumn createSortableChildColumn(final EObjectTableViewer viewer, final String name, final GridColumnGroup parent, Function<MarketabilityRow, String> labelFunction,
			Function<MarketabilityRow, Comparable<?>> comparer) {
		return createSortableChildColumn(viewer, name, parent, new ICellRenderer() {

			@Override
			public @Nullable Object getFilterValue(Object object) {
				return null;
			}

			@Override
			public Comparable<?> getComparable(Object object) {
				if (object instanceof MarketabilityRow row) {
					return comparer.apply(row);
				}
				return null;
			}

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof MarketabilityRow row) {
					return labelFunction.apply(row);
				}
				return null;
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				return null;
			}
		});
	}

	protected GridViewerColumn createSortableChildColumn(final EObjectTableViewer viewer, final String name, final GridColumnGroup parent, ICellRenderer renderer) {
		GridViewerColumn childColumn = createChildColumn(viewer, new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				cell.setText(renderer.render(cell.getElement()));
			}
		}, name, parent);

		childColumn.getColumn().setCellRenderer(new DefaultCellRenderer());
		childColumn.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
		childColumn.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, renderer);

		viewer.getSortingSupport().addSortableColumn(viewer, childColumn, childColumn.getColumn());
		return childColumn;
	}

	protected GridViewerColumn createSortableChildColumn(final EObjectTableViewer viewer, CellLabelProvider labelProvider, Function<MarketabilityRow, Comparable<?>> comparer, final String name,
			final GridColumnGroup parent, final ETypedElement... pathObjects) {

		final GridColumn gc = new GridColumn(parent, SWT.CENTER | SWT.WRAP);

		final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
		gvc.getColumn().setTree(false);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(250);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(labelProvider);

		gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		gvc.getColumn().setWidth(120);
		final ICellRenderer renderer = new ICellRenderer() {

			@Override
			public @Nullable Object getFilterValue(Object object) {
				return null;
			}

			@Override
			public Comparable<?> getComparable(Object object) {
				if (object instanceof MarketabilityRow row) {
					return comparer.apply(row);
				}
				return null;
			}

			@Override
			public @Nullable String render(Object object) {
				return null;
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				return null;
			}
		};

		gvc.getColumn().setCellRenderer(new DefaultCellRenderer());
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, renderer);
		viewer.getSortingSupport().addSortableColumn(viewer, gvc, gvc.getColumn());
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

	public void dispose() {
		localResourceManager.dispose();
		tableViewer.removeSelectionChangedListener(tableSelectionChangedListener);
		tableViewer.dispose();
	}

	public void setFocus() {
		ViewerHelper.setFocus(getViewer());
	}

}

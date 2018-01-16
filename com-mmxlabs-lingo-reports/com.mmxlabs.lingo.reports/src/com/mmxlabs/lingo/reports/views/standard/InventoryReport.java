/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.LineStyle;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class InventoryReport extends ViewPart {
	public static final String ID = "com.mmxlabs.lingo.reports.views.InventoryReport";

	private Chart viewer;

	private Inventory selectedInventory;
	private ScenarioResult currentResult;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = new Runnable() {

				@Override
				public void run() {

					selectedInventory = null;

					currentResult = pinned;
					if (currentResult == null) {
						for (final ScenarioResult other : others) {
							currentResult = other;
							break;
						}
					}

					if (currentResult != null) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) currentResult.getRootObject();
						final CargoModel cargoModel = scenarioModel.getCargoModel();
						// Inventory inventory = null;

						List<Inventory> models = cargoModel.getInventoryModels().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
						comboViewer.setInput(models);
						if (!models.isEmpty()) {
							selectedInventory = cargoModel.getInventoryModels().get(0);
							comboViewer.setSelection(new StructuredSelection(selectedInventory));
							updatePlots(Collections.singleton(selectedInventory), currentResult);
						} else {
							updatePlots(Collections.emptyList(), currentResult);
						}
					}
				}
			};
			RunnerHelper.exec(r, block);
		}
	};

	private SelectedScenariosService selectedScenariosService;

	private ComboViewer comboViewer;

	/**
	 * The constructor.
	 */
	public InventoryReport() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);
		parent.setLayout(new GridLayout(1, true));
		{

			Composite selector = new Composite(parent, SWT.NONE);
			selector.setLayout(new GridLayout(2, false));
			final Label label = new Label(selector, SWT.NONE);
			label.setText("Facility: ");
			comboViewer = new ComboViewer(selector);
			comboViewer.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(70, SWT.DEFAULT).create());

			comboViewer.setContentProvider(new ArrayContentProvider());
			comboViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(final Object element) {
					return ((Inventory) element).getName();
				}
			});
			comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(final SelectionChangedEvent event) {
					selectedInventory = (Inventory) ((IStructuredSelection) comboViewer.getSelection()).getFirstElement();
					updatePlots(Collections.singleton(selectedInventory), currentResult);
				}
			});
		}
		viewer = new Chart(parent, SWT.NONE);
		viewer.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		
		viewer.getTitle().setVisible(false);
		
		// Create the help context id for the viewer's control
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_Fitness");
		// makeActions();
		// hookContextMenu();
		// contributeToActionBars();

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);

	}

	// private void hookContextMenu() {
	// final MenuManager menuMgr = new MenuManager("#PopupMenu");
	// menuMgr.setRemoveAllWhenShown(true);
	// menuMgr.addMenuListener(new IMenuListener() {
	// @Override
	// public void menuAboutToShow(final IMenuManager manager) {
	// InventoryReport.this.fillContextMenu(manager);
	// }
	// });
	// final Menu menu = menuMgr.createContextMenu(viewer.getControl());
	// viewer.getControl().setMenu(menu);
	// getSite().registerContextMenu(menuMgr, viewer);
	// }
	//
	// private void contributeToActionBars() {
	// final IActionBars bars = getViewSite().getActionBars();
	// fillLocalPullDown(bars.getMenuManager());
	// fillLocalToolBar(bars.getToolBarManager());
	// }
	//
	// private void fillLocalPullDown(final IMenuManager manager) {
	// manager.add(new Separator());
	// }
	//
	// private void fillContextMenu(final IMenuManager manager) {
	//
	// manager.add(new GroupMarker("pack"));
	// // Other plug-ins can contribute there actions here
	// manager.add(new GroupMarker("additions"));
	// manager.add(new GroupMarker("edit"));
	// manager.add(new GroupMarker("copy"));
	// manager.add(new GroupMarker("importers"));
	// manager.add(new GroupMarker("exporters"));
	// }
	//
	// private void fillLocalToolBar(final IToolBarManager manager) {
	// manager.add(new GroupMarker("pack"));
	// // Other plug-ins can contribute there actions here
	// manager.add(new GroupMarker("additions"));
	// manager.add(new GroupMarker("edit"));
	// manager.add(new GroupMarker("copy"));
	// manager.add(new GroupMarker("importers"));
	// manager.add(new GroupMarker("exporters"));
	//
	// manager.appendToGroup("pack", packColumnsAction);
	// manager.appendToGroup("copy", copyTableAction);
	// }
	//
	// private void makeActions() {
	// packColumnsAction = new PackGridTableColumnsAction(viewer);
	// copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
	// getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
	// }

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	@Override
	public void dispose() {

		selectedScenariosService.removeListener(selectedScenariosServiceListener);

		super.dispose();
	}

	private void updatePlots(Collection<Inventory> inventoryModels, ScenarioResult toDisplay) {

		final ISeriesSet seriesSet = viewer.getSeriesSet();
		// Delete existing data
		{
			Set<String> names = new HashSet<>();
			for (ISeries s : seriesSet.getSeries()) {
				names.add(s.getId());
			}
			names.forEach(s -> seriesSet.deleteSeries(s));
		}
		int colour = 2;
		for (final Inventory inventory : inventoryModels) {
			// Used for max level
			++colour;
			if (colour == SWT.COLOR_RED) {
				++colour;
			}
			if (inventory.getName() == null) {
				continue;
			}
			{
				final List<Pair<LocalDate, Integer>> changes = new LinkedList<>();
				final List<Pair<LocalDate, Integer>> cargo_changes = new LinkedList<>();
				for (final InventoryEventRow r : inventory.getFeeds()) {
					if (r.getPeriod() == InventoryFrequency.CARGO || r.getPeriod() == InventoryFrequency.LEVEL) {
						changes.add(new Pair<>(r.getStartDate(), r.getVolume()));
						if (r.getPeriod() == InventoryFrequency.CARGO) {
							cargo_changes.add(new Pair<>(r.getStartDate(), r.getVolume()));
						}
					} else {
						LocalDateTime start = r.getStartDate().atStartOfDay();
						if (r.getStartDate() == r.getEndDate()) {
							changes.add(new Pair<>(LocalDate.from(start), r.getVolume()));
						} else {
							while (start.isBefore(r.getEndDate().plusDays(1).atStartOfDay())) {
								changes.add(new Pair<>(LocalDate.from(start), r.getVolume()));

								if (r.getPeriod() == InventoryFrequency.HOURLY) {
									start = start.plusHours(1);
								} else if (r.getPeriod() == InventoryFrequency.DAILY) {
									start = start.plusDays(1);
								} else if (r.getPeriod() == InventoryFrequency.MONTHLY) {
									start = start.plusMonths(1);
								} else {
									assert false;
								}
							}
						}
					}
				}
				for (final InventoryEventRow r : inventory.getOfftakes()) {
					if (r.getPeriod() == InventoryFrequency.CARGO || r.getPeriod() == InventoryFrequency.LEVEL) {
						changes.add(new Pair<>(r.getStartDate(), -r.getVolume()));
						if (r.getPeriod() == InventoryFrequency.CARGO) {
							cargo_changes.add(new Pair<>(r.getStartDate(), -r.getVolume()));
						}
					} else {
						LocalDateTime start = r.getStartDate().atStartOfDay();
						while (start.isBefore(r.getEndDate().plusDays(1).atStartOfDay())) {
							changes.add(new Pair<>(LocalDate.from(start), -r.getVolume()));

							if (r.getPeriod() == InventoryFrequency.HOURLY) {
								start = start.plusHours(1);
							} else if (r.getPeriod() == InventoryFrequency.DAILY) {
								start = start.plusDays(1);
							} else if (r.getPeriod() == InventoryFrequency.MONTHLY) {
								start = start.plusMonths(1);
							} else {
								assert false;
							}
						}
					}
				}

				final ScheduleModel scheduleModel = toDisplay.getTypedResult(ScheduleModel.class);
				if (scheduleModel != null) {
					final Schedule schedule = scheduleModel.getSchedule();
					if (schedule != null) {
						for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
							if (slotAllocation.getPort() == inventory.getPort()) {
								int change = (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) ? -slotAllocation.getPhysicalVolumeTransferred()
										: slotAllocation.getPhysicalVolumeTransferred();
								changes.add(new Pair<>(LocalDate.from(slotAllocation.getSlotVisit().getStart()), change));
								cargo_changes.add(new Pair<>(LocalDate.from(slotAllocation.getSlotVisit().getStart()), change));
							}
						}
					}
				}

				//
				final Map<LocalDate, List<Integer>> m = changes.stream() //
						.filter(p -> p.getFirst() != null && p.getSecond() != null) //
						.collect(Collectors.groupingBy(e -> e.getFirst(), Collectors.mapping(e -> e.getSecond(), Collectors.toList())));
				final Map<LocalDate, List<Integer>> m2 = cargo_changes.stream() //
						.filter(p -> p.getFirst() != null && p.getSecond() != null) //
						.collect(Collectors.groupingBy(e -> e.getFirst(), Collectors.mapping(e -> e.getSecond(), Collectors.toList())));

				if (m.size() > 0) {
					final TreeMap<LocalDate, List<Integer>> sorted = new TreeMap<>(m);
					final Date[] dates = new Date[sorted.size()];
					final double[] values = new double[sorted.size()];
					int idx = 0;

					int total = 0;
					for (final Map.Entry<LocalDate, List<Integer>> e : sorted.entrySet()) {
						final int sum = e.getValue().stream().mapToInt(Integer::intValue).sum();
						dates[idx] = Date.from(e.getKey().atStartOfDay().atZone(ZoneId.of("UTC")).toInstant());
						total += sum;
						values[idx] = (double) total;
						idx++;
					}

					{
						final ILineSeries createSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, inventory.getName());
						createSeries.setXDateSeries(dates);
						createSeries.setYSeries(values);
						createSeries.setSymbolSize(2);
						//
						createSeries.setLineColor(Display.getDefault().getSystemColor(colour));

					}
				}
				{
					final TreeMap<LocalDate, List<Integer>> sorted = new TreeMap<>(m2);
					final Date[] dates = new Date[sorted.size() * 2];
					final double[] values = new double[sorted.size() * 2];
					int idx = 0;

					int total = 0;
					for (final Map.Entry<LocalDate, List<Integer>> e : sorted.entrySet()) {
						final int sum = e.getValue().stream().mapToInt(Integer::intValue).sum();
						dates[idx] = Date.from(e.getKey().atStartOfDay().atZone(ZoneId.of("UTC")).toInstant());
						total += sum;
						values[idx] = (double) sum;
						++idx;
						dates[idx] = Date.from(e.getKey().plusDays(1).atStartOfDay().atZone(ZoneId.of("UTC")).toInstant());
						values[idx] = 0;
						++idx;
					}

					{
						final IBarSeries createSeries = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, inventory.getName() + " Cargoes");
						createSeries.setXDateSeries(dates);
						createSeries.setYSeries(values);
						createSeries.setBarWidth(2);
						// createSeries.setSymbolSize(2);

						createSeries.setBarColor(Display.getDefault().getSystemColor(colour));

					}
				}
			}
			{
				final List<Pair<LocalDate, Integer>> min_level = new LinkedList<>();
				final List<Pair<LocalDate, Integer>> max_level = new LinkedList<>();
				for (final InventoryCapacityRow r : inventory.getCapacities()) {
					min_level.add(new Pair<>(r.getDate(), r.getMinVolume()));
					max_level.add(new Pair<>(r.getDate(), r.getMaxVolume()));
				}
				{
					final Map<LocalDate, List<Integer>> m = max_level.stream() //
							.filter(p -> p.getFirst() != null && p.getSecond() != null) //
							.collect(Collectors.groupingBy(e -> e.getFirst(), Collectors.mapping(e -> e.getSecond(), Collectors.toList())));

					if (m.size() > 0) {

						final TreeMap<LocalDate, List<Integer>> sorted = new TreeMap<>(m);

						final Date[] dates = new Date[2 * sorted.size() - 1];
						final double[] values = new double[2 * sorted.size() - 1];
						int idx = 0;
						for (final Map.Entry<LocalDate, List<Integer>> e : sorted.entrySet()) {

							if (idx != 0) {
								dates[idx] = Date.from(e.getKey().atStartOfDay().atZone(ZoneId.of("UTC")).toInstant());
								values[idx] = values[idx - 1];
								idx++;
							}

							final int sum = e.getValue().stream().mapToInt(Integer::intValue).sum();

							dates[idx] = Date.from(e.getKey().atStartOfDay().atZone(ZoneId.of("UTC")).toInstant());
							values[idx] = (double) sum;
							idx++;
						}
						final ILineSeries createSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, inventory.getName() + "-Max");
						createSeries.setXDateSeries(dates);
						createSeries.setYSeries(values);
						createSeries.setSymbolSize(2);
						createSeries.setLineStyle(LineStyle.DASH);
						createSeries.setLineColor(Display.getDefault().getSystemColor(colour));
					}
				}
			}
		}
		final IAxisSet axisSet = viewer.getAxisSet();

		// viewer.getTitle().setText("Inventory");
		// viewer.getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		viewer.getAxisSet().getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		viewer.getAxisSet().getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		viewer.getAxisSet().getXAxis(0).getTitle().setText("Date");
		viewer.getAxisSet().getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		viewer.getAxisSet().getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		viewer.getAxisSet().getYAxis(0).getTitle().setText("Volume");
		// 5. adjust the range for all axes.

		axisSet.adjustRange();
		viewer.redraw();
	}
}
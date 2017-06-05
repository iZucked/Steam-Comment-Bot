/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;

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
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class InventoryReport extends ViewPart {
	public static final String ID = "com.mmxlabs.lingo.reports.views.InventoryReport";

	private Chart viewer;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = new Runnable() {
				@Override
				public void run() {

					ScenarioResult toDisplay = pinned;
					if (toDisplay == null) {
						for (final ScenarioResult other : others) {
							toDisplay = other;
							break;
						}
					}

					if (toDisplay != null) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) toDisplay.getRootObject();
						final CargoModel cargoModel = scenarioModel.getCargoModel();
						Inventory inventory = null;
						for (final Inventory invent : cargoModel.getInventoryModels()) {
							if (invent.getName().equals("Bintulu")) {
								inventory = invent;
								break;
							}
						}
						if (inventory != null) {
							final ISeriesSet seriesSet = viewer.getSeriesSet();
							{
								final List<Pair<LocalDate, Integer>> changes = new LinkedList<>();
								for (final InventoryEventRow r : inventory.getFeeds()) {
									if (r.getPeriod() == InventoryFrequency.CARGO || r.getPeriod() == InventoryFrequency.LEVEL) {
										changes.add(new Pair<>(r.getStartDate(), r.getVolume()));
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
											if (slotAllocation.getPort().getName().equals("Bintulu")) {
												changes.add(new Pair<>(LocalDate.from(slotAllocation.getSlotVisit().getStart()), -slotAllocation.getPhysicalVolumeTransferred()));
											}
										}
									}
								}

								//
								final Map<LocalDate, List<Integer>> m = changes.stream() //
										.collect(Collectors.groupingBy(e -> e.getFirst(), Collectors.mapping(e -> e.getSecond(), Collectors.toList())));

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
									final ILineSeries createSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "Bintulu");
									createSeries.setXDateSeries(dates);
									createSeries.setYSeries(values);
									createSeries.setSymbolSize(2);

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
											.collect(Collectors.groupingBy(e -> e.getFirst(), Collectors.mapping(e -> e.getSecond(), Collectors.toList())));

									final TreeMap<LocalDate, List<Integer>> sorted = new TreeMap<>(m);

									final Date[] dates = new Date[2 * sorted.size() - 1];
									final double[] values = new double[2* sorted.size() - 1];
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

									final ILineSeries createSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "Bintulu-Max");
									createSeries.setXDateSeries(dates);
									createSeries.setYSeries(values);
									createSeries.setSymbolSize(2);
									createSeries.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
								}
							}

							final IAxisSet axisSet = viewer.getAxisSet();

							viewer.getTitle().setText("Bintulu Inventory");
							viewer.getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
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
				}
			};
			RunnerHelper.exec(r, block);
		}
	};

	private SelectedScenariosService selectedScenariosService;

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

		viewer = new Chart(parent, SWT.NONE);

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

}
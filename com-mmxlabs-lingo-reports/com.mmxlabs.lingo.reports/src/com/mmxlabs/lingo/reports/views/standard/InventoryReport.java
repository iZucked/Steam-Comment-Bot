/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.LineStyle;
import org.swtchart.Range;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class InventoryReport extends ViewPart {
	public static final String ID = "com.mmxlabs.lingo.reports.views.InventoryReport";

	private Chart chartViewer;
	private GridTableViewer tableViewer;

	private Inventory selectedInventory;
	private ScenarioResult currentResult;
	private Color color_Orange;
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

						final List<Inventory> models = cargoModel.getInventoryModels().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
						comboViewer.setInput(models);
						if (!models.isEmpty()) {
							if (lastFacility != null) {
								for (final Inventory inventory : cargoModel.getInventoryModels()) {
									if (lastFacility.equals(inventory.getName())) {
										selectedInventory = inventory;
										break;
									}
								}
							}
							if (selectedInventory == null) {
								selectedInventory = cargoModel.getInventoryModels().get(0);
							}
							lastFacility = selectedInventory.getName();
							comboViewer.setSelection(new StructuredSelection(selectedInventory));
							updatePlots(Collections.singleton(selectedInventory), currentResult);
						} else {
							updatePlots(Collections.emptyList(), currentResult);
						}
					} else {
						comboViewer.setInput(Collections.emptyList());
						updatePlots(Collections.emptyList(), null);
					}
				}
			};
			RunnerHelper.exec(r, block);
		}
	};

	private SelectedScenariosService selectedScenariosService;

	private ComboViewer comboViewer;

	private String lastFacility = null;

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

		color_Orange = new Color(PlatformUI.getWorkbench().getDisplay(), new RGB(255, 200, 15));

		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);
		parent.setLayout(new GridLayout(1, true));
		{

			final Composite selector = new Composite(parent, SWT.NONE);
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
					if (selectedInventory != null) {
						lastFacility = selectedInventory.getName();
					} else {
						lastFacility = null;
					}
					updatePlots(Collections.singleton(selectedInventory), currentResult);
				}
			});
		}

		final CTabFolder folder = new CTabFolder(parent, SWT.BOTTOM);
		folder.setLayout(new GridLayout(1, true));
		folder.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		final CTabItem chartItem = new CTabItem(folder, SWT.NONE);
		chartItem.setText("Chart");

		final CTabItem tableItem = new CTabItem(folder, SWT.NONE);
		tableItem.setText("Table");
		{
			chartViewer = new Chart(folder, SWT.NONE);
			chartViewer.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			chartViewer.getTitle().setVisible(false);
			chartItem.setControl(chartViewer);
		}
		{
			tableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			GridViewerHelper.configureLookAndFeel(tableViewer);
			tableViewer.getGrid().setTreeLinesVisible(true);
			tableViewer.getGrid().setHeaderVisible(true);
			final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
			{
				createColumn("Date", 150, o -> "" + o.date.format(formatter));
				createColumn("Type", 150, o -> o.type);
				createColumn("Change", 150, o -> String.format("%,d", o.changeInM3));
				createColumn("Level", 150, o -> String.format("%,d", o.runningTotal));
				// createColumn("Date", o -> o.toString());
				tableItem.setControl(tableViewer.getControl());

			}

		}

		folder.setSelection(0);

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
		ViewerHelper.setFocus(chartViewer);
	}

	@Override
	public void dispose() {
		if (color_Orange != null) {
			color_Orange.dispose();
		}
		selectedScenariosService.removeListener(selectedScenariosServiceListener);

		super.dispose();
	}

	private void updatePlots(final Collection<Inventory> inventoryModels, final ScenarioResult toDisplay) {
		final DateFormat dateFormat = new SimpleDateFormat("d MMM");
		final ISeriesSet seriesSet = chartViewer.getSeriesSet();
		// Delete existing data
		{
			final Set<String> names = new HashSet<>();
			for (final ISeries s : seriesSet.getSeries()) {
				names.add(s.getId());
			}
			names.forEach(s -> seriesSet.deleteSeries(s));
		}
		LocalDate minDate = null;
		LocalDate maxDate = null;
		final List<InventoryLevel> tableLevels = new LinkedList<>();

		if (toDisplay != null) {
			final ScheduleModel scheduleModel = toDisplay.getTypedResult(ScheduleModel.class);
			if (scheduleModel != null) {
				final Schedule schedule = scheduleModel.getSchedule();
				if (schedule != null) {
					for (final InventoryEvents inventoryEvents : schedule.getInventoryLevels()) {
						final Inventory inventory = inventoryEvents.getFacility();
						if (!inventoryModels.contains(inventory)) {
							continue;
						}

						if (inventory.getName() == null) {
							continue;
						}
						{

							final List<Pair<LocalDateTime, Integer>> inventoryLevels = inventoryEvents.getEvents().stream() //
									.collect(Collectors.toMap( //
											InventoryChangeEvent::getDate, //
											InventoryChangeEvent::getCurrentLevel, //
											(a, b) -> (b), // Take latest value
											LinkedHashMap::new))
									.entrySet().stream() //
									.map((e) -> new Pair<>(e.getKey(), e.getValue())) //
									.collect(Collectors.toList());
							if (!inventoryLevels.isEmpty()) {
								final ILineSeries series = createSmoothLineSeries(seriesSet, "Inventory", inventoryLevels);
								series.setSymbolSize(1);
								series.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

								minDate = inventoryLevels.get(0).getFirst().toLocalDate();
								maxDate = inventoryLevels.get(inventoryLevels.size() - 1).getFirst().toLocalDate();

								inventoryEvents.getEvents().forEach(e -> {
									String type = "Unknown";
									if (e.getSlotAllocation() != null) {
										type = "SCHEDULE";
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity());
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										tableLevels.add(lvl);

									} else if (e.getOpenSlotAllocation() != null) {
										type = "OPEN";
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity());
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										tableLevels.add(lvl);
									} else if (e.getEvent() != null) {
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), e.getEvent().getPeriod(), e.getChangeQuantity());
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										tableLevels.add(lvl);
									}
								});
							}
						}
						{

							final List<Pair<LocalDateTime, Integer>> inventoryLevels = inventoryEvents.getEvents().stream() //
									.collect(Collectors.toMap( //
											InventoryChangeEvent::getDate, //
											InventoryChangeEvent::getCurrentMin, //
											(a, b) -> (b), // Take latest value
											LinkedHashMap::new))
									.entrySet().stream() //
									.map((e) -> new Pair<>(e.getKey(), e.getValue())) //
									.collect(Collectors.toList());
							if (!inventoryLevels.isEmpty()) {
								final ILineSeries series = createStepLineSeries(seriesSet, "Tank min", inventoryLevels);
								series.setSymbolSize(1);
								series.setSymbolType(PlotSymbolType.NONE);
								series.setLineStyle(LineStyle.DASH);
								series.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW));
							}
						}
						{

							final List<Pair<LocalDateTime, Integer>> inventoryLevels = inventoryEvents.getEvents().stream() //
									.collect(Collectors.toMap( //
											InventoryChangeEvent::getDate, //
											InventoryChangeEvent::getCurrentMax, //
											(a, b) -> (b), // Take latest value
											LinkedHashMap::new))
									.entrySet().stream() //
									.map((e) -> new Pair<>(e.getKey(), e.getValue())) //
									.collect(Collectors.toList());
							if (!inventoryLevels.isEmpty()) {

								final ILineSeries series = createStepLineSeries(seriesSet, "Tank max", inventoryLevels);
								series.setSymbolSize(1);
								series.setSymbolType(PlotSymbolType.NONE);
								series.setLineStyle(LineStyle.DASH);
								series.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
							}
						}
						{

							final List<Pair<LocalDateTime, Integer>> values = inventoryEvents.getEvents().stream() //
									.filter(evt -> evt.getSlotAllocation() != null) //
									.map((e) -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
									.collect(Collectors.toList());
							if (!values.isEmpty()) {

								final IBarSeries series = createDaySizedBarSeries(seriesSet, "Cargoes", values);
								series.setBarWidth(1);
								series.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
							}
						}
						{
							final List<Pair<LocalDateTime, Integer>> values = inventoryEvents.getEvents().stream() //
									.filter(evt -> evt.getOpenSlotAllocation() != null) //
									.map((e) -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
									.collect(Collectors.toList());
							if (!values.isEmpty()) {

								final IBarSeries series = createDaySizedBarSeries(seriesSet, "Open", values);
								series.setBarWidth(1);
								series.setBarColor(color_Orange);
							}
						}
						{
							{
								final List<Pair<LocalDateTime, Integer>> values = inventoryEvents.getEvents().stream() //
										.filter(evt -> evt.getEvent() != null) //
										.filter(evt -> evt.getEvent().getPeriod() == InventoryFrequency.CARGO) //
										.map((e) -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
										.collect(Collectors.toList());
								if (!values.isEmpty()) {

									final IBarSeries series = createDaySizedBarSeries(seriesSet, "3rd-party", values);
									series.setBarWidth(2);
									series.setBarColor(color_Orange);
								}
							}
						}
					}
				}
			}
		}
		final IAxisSet axisSet = chartViewer.getAxisSet();

		// viewer.getTitle().setText("Inventory");
		// viewer.getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartViewer.getAxisSet().getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartViewer.getAxisSet().getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartViewer.getAxisSet().getXAxis(0).getTitle().setText("Date");
		chartViewer.getAxisSet().getXAxis(0).getTick().setFormat(dateFormat);

		chartViewer.getAxisSet().getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartViewer.getAxisSet().getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartViewer.getAxisSet().getYAxis(0).getTitle().setText("Volume");
		// 5. adjust the range for all axes.

		// Auto adjust everything
		axisSet.adjustRange();
		// Month align the date range
		if (minDate != null && maxDate != null) {
			chartViewer.getAxisSet().getXAxis(0).setRange(new Range( //
					1000L * minDate.withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000L * maxDate.withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		} else {
			chartViewer.getAxisSet().getXAxis(0).setRange(new Range( //
					1000L * LocalDate.now().withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000L * LocalDate.now().withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		}
		// Try to force month labels
		chartViewer.getAxisSet().getXAxis(0).getTick().setTickMarkStepHint((int) (15L));
		chartViewer.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);

		chartViewer.updateLayout();

		chartViewer.redraw();

		Collections.sort(tableLevels, (a, b) -> a.date.compareTo(b.date));

		// inventoryLevels.sort((a,b) -> a.getFirst().compareTo(b.getFirst()));

		int total = 0;
		for (final InventoryLevel lvl : tableLevels) {
			lvl.runningTotal = total + lvl.changeInM3;
			total = lvl.runningTotal;
		}

		tableViewer.setInput(tableLevels);
	}

	private ILineSeries createSmoothLineSeries(final ISeriesSet seriesSet, final String name, final List<Pair<LocalDateTime, Integer>> data) {

		final Date[] dates = new Date[data.size()];
		final double[] values = new double[data.size()];
		int idx = 0;
		for (final Pair<LocalDateTime, Integer> e : data) {
			final int sum = e.getSecond();

			dates[idx] = Date.from(e.getFirst().atZone(ZoneId.of("UTC")).toInstant());
			values[idx] = (double) sum;
			idx++;
		}

		final ILineSeries series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, name);
		series.setXDateSeries(dates);
		series.setYSeries(values);
		return series;
	}

	private ILineSeries createStepLineSeries(final ISeriesSet seriesSet, final String name, final List<Pair<LocalDateTime, Integer>> data) {

		final Date[] dates = new Date[2 * data.size() - 1];
		final double[] values = new double[2 * data.size() - 1];
		int idx = 0;
		for (final Pair<LocalDateTime, Integer> e : data) {
			if (idx != 0) {
				dates[idx] = Date.from(e.getFirst().atZone(ZoneId.of("UTC")).toInstant());
				values[idx] = values[idx - 1];
				idx++;
			}

			final int sum = e.getSecond();

			dates[idx] = Date.from(e.getFirst().atZone(ZoneId.of("UTC")).toInstant());
			values[idx] = (double) sum;
			idx++;
		}

		final ILineSeries series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, name);
		series.setXDateSeries(dates);
		series.setYSeries(values);
		return series;
	}

	private IBarSeries createDaySizedBarSeries(final ISeriesSet seriesSet, final String name, final List<Pair<LocalDateTime, Integer>> data) {

		final Date[] dates = new Date[2 * data.size()];
		final double[] values = new double[2 * data.size()];
		int idx = 0;
		for (final Pair<LocalDateTime, Integer> e : data) {

			final int sum = e.getSecond();

			dates[idx] = Date.from(e.getFirst().atZone(ZoneId.of("UTC")).toInstant());
			values[idx] = (double) sum;
			idx++;

			dates[idx] = Date.from(e.getFirst().atZone(ZoneId.of("UTC")).plusDays(1).toInstant());
			values[idx] = 0;
			idx++;
		}

		final IBarSeries series = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, name);
		series.setXDateSeries(dates);
		series.setYSeries(values);
		return series;
	}

	private GridViewerColumn createColumn(final String title, final int width, final Function<InventoryLevel, String> labelProvider) {
		final GridViewerColumn column = new GridViewerColumn(tableViewer, SWT.NONE);
		GridViewerHelper.configureLookAndFeel(column);

		column.getColumn().setText(title);
		column.getColumn().setWidth(width);
		column.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {

				final Object element = cell.getElement();

				final InventoryLevel lvl = (InventoryLevel) element;
				cell.setText(labelProvider.apply(lvl));
				cell.setBackground(null);
				if (lvl.breach) {
					cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
				}
			}
		});

		return column;
	}

	private static class InventoryLevel {

		public final LocalDate date;
		public final int changeInM3;
		public final String type;
		public int runningTotal = 0;
		public boolean breach = false;

		public InventoryLevel(final LocalDate date, final InventoryFrequency type, final int changeInM3) {
			this.date = date;
			this.type = type.toString();
			this.changeInM3 = changeInM3;

		}

		public InventoryLevel(final LocalDate date, final String type, final int changeInM3) {
			this.date = date;
			this.type = type;
			this.changeInM3 = changeInM3;
		}

	}

}
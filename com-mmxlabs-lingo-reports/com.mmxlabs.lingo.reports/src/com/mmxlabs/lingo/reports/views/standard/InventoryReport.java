/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.event.EventHandler;
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
import com.mmxlabs.lingo.reports.views.standard.inventory.InventoryLevel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class InventoryReport extends ViewPart {
	public static final String ID = "com.mmxlabs.lingo.reports.views.standard.InventoryReport";

	private Chart chartViewer;
	private GridTableViewer tableViewer;

	private Inventory selectedInventory;
	private ScenarioResult currentResult;
	private Color color_Orange;
	
	private EventHandler todayHandler;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = () -> {

				selectedInventory = null;

				currentResult = pinned;
				if (currentResult == null && !others.isEmpty()) {
					currentResult = others.iterator().next();
				}

				if (currentResult != null) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) currentResult.getRootObject();
					assert scenarioModel != null;
					final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

					final List<Inventory> models = cargoModel.getInventoryModels().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
					comboViewer.setInput(models);
					if (!models.isEmpty()) {
						if (lastFacility != null) {
							final Optional<Inventory> match = cargoModel.getInventoryModels().stream() //
									.filter(inventory -> lastFacility.equals(inventory.getName())) //
									.findFirst();

							if (match.isPresent()) {
								selectedInventory = match.get();
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
			};
			RunnerHelper.exec(r, block);
		}
	};

	private SelectedScenariosService selectedScenariosService;

	private ComboViewer comboViewer;

	private String lastFacility = null;

	private PackGridTableColumnsAction packAction;

	private CopyGridToClipboardAction copyAction;

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		color_Orange = new Color(PlatformUI.getWorkbench().getDisplay(), new RGB(255, 200, 15));

		selectedScenariosService = getSite().getService(SelectedScenariosService.class);
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
			comboViewer.addSelectionChangedListener(event -> {
				selectedInventory = (Inventory) ((IStructuredSelection) comboViewer.getSelection()).getFirstElement();
				if (selectedInventory != null) {
					lastFacility = selectedInventory.getName();
				} else {
					lastFacility = null;
				}
				updatePlots(Collections.singleton(selectedInventory), currentResult);
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
				//createColumn("Type", 150, o -> o.type);
				createColumn("Total Feed In", 150, o -> String.format("%,d", o.feedIn));
				createColumn("Forecast low", 150, o -> String.format("%,d", o.volumeLow));
				createColumn("Forecast high", 150, o -> String.format("%,d", o.volumeHigh));
				createColumn("Total Feed Out", 150, o -> String.format("%,d", o.feedOut));
				createColumn("Total Cargo Out", 150, o -> String.format("%,d", o.cargoOut));
				createColumn("Change", 150, o -> String.format("%,d", o.changeInM3));
				createColumn("Level", 150, o -> String.format("%,d", o.runningTotal));
				createColumn("Level low", 150, o -> String.format("%,d", o.ttlLow));
				createColumn("Level high", 150, o -> String.format("%,d", o.ttlHigh));
				createColumn("Vessel", 150, o -> o.vessel);
				createColumn("D-ID", 150, o -> o.dischargeId);
				createColumn("Buyer", 150, o -> o.salesContract);
				createColumn("Delivery date", 150, o -> o.salesDate != null ? o.salesDate.format(formatter) : null);
				createColumn("Delivery port", 150, o -> o.dischargePort);
				tableItem.setControl(tableViewer.getControl());
			}

		}

		folder.setSelection(0);

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);

		packAction = PackActionFactory.createPackColumnsAction(tableViewer);
		getViewSite().getActionBars().getToolBarManager().add(packAction);
		packAction.setEnabled(folder.getSelectionIndex() == 1);
		
		copyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(tableViewer);
		getViewSite().getActionBars().getToolBarManager().add(copyAction);
		copyAction.setEnabled(folder.getSelectionIndex() == 1);
		
		//Adding an event broker for the snap-to-date event todayHandler
		IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		this.todayHandler = event -> snapTo((LocalDate)  event.getProperty(IEventBroker.DATA));
	    eventBroker.subscribe(TodayHandler.EVENT_SNAP_TO_DATE, this.todayHandler);
		
		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copyAction.setEnabled(folder.getSelectionIndex() == 1);
				packAction.setEnabled(folder.getSelectionIndex() == 1);
			}
		});
		getViewSite().getActionBars().getToolBarManager().update(true);
	}

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
		if (this.todayHandler != null) {
			IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
			eventBroker.unsubscribe(this.todayHandler);
		}

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
			names.forEach(seriesSet::deleteSeries);
		}

		LocalDate minDate = null;
		LocalDate maxDate = null;

		final List<InventoryLevel> tableLevels = new LinkedList<>();
		
		Optional<InventoryChangeEvent> firstInventoryData = Optional.empty();

		if (toDisplay != null) {
			final ScheduleModel scheduleModel = toDisplay.getTypedResult(ScheduleModel.class);
			if (scheduleModel != null) {
				final Schedule schedule = scheduleModel.getSchedule();
				if (schedule != null) {
					for (final InventoryEvents inventoryEvents : schedule.getInventoryLevels()) {
						final Inventory inventory = inventoryEvents.getFacility();

						// Find the date of the latest position/cargo
						final Optional<LocalDateTime> latestLoad = inventoryEvents.getEvents().stream() //
								.filter(evt -> evt.getSlotAllocation() != null || evt.getOpenSlotAllocation() != null) //
								.map(InventoryChangeEvent::getDate) //
								.reduce((a, b) -> a.isAfter(b) ? a : b);

						// Find the first inventory feed event
						firstInventoryData = inventoryEvents.getEvents().stream() //
								.filter(evt -> evt.getSlotAllocation() == null && evt.getOpenSlotAllocation() == null)
								// .map(x -> x.getDate())
								.reduce((a, b) -> a.getDate().isAfter(b.getDate()) ? b : a);

						if (!inventoryModels.contains(inventory)) {
							continue;
						}

						if (inventory.getName() == null) {
							continue;
						}
						
						final List<InventoryEventRow> invs = inventory.getFeeds();
						if (invs == null){
							continue;
						}

						{
							final Optional<InventoryChangeEvent> firstInventoryDataFinal = firstInventoryData;
							final List<Pair<LocalDateTime, Integer>> inventoryLevels = inventoryEvents.getEvents().stream() //
									.collect(Collectors.toMap( //
											InventoryChangeEvent::getDate, //
											InventoryChangeEvent::getCurrentLevel, //
											(a, b) -> (b), // Take latest value
											LinkedHashMap::new))
									.entrySet().stream() //
									.filter(x -> {
										if (latestLoad.isPresent() && firstInventoryDataFinal.isPresent()) {
											return x.getKey().isBefore(latestLoad.get()) && x.getKey().isAfter(firstInventoryDataFinal.get().getDate());
										}
										return false;
									}).map(e -> new Pair<>(e.getKey(), e.getValue())) //
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
										type = "Cargo";
										final String vessel = e.getSlotAllocation().getCargoAllocation().getEvents().get(0).getSequence().getName();
										SlotAllocation allocation = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof DischargeSlot).findFirst().get();
										final String dischargeId = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof DischargeSlot).findFirst().get().getName();
										final String dischargePort = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof DischargeSlot).findFirst().get().getPort().getName();
										Contract contract = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof DischargeSlot).findFirst().get().getContract();
										String salesContract = "";
										if (contract != null) {
											salesContract = contract.getName();
										}
										ZonedDateTime time = allocation.getSlotVisit().getStart();
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity(), vessel, dischargeId,
												dischargePort, salesContract, time == null ? null : time.toLocalDate());
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										if (e.getEvent() != null) {
											lvl.volumeLow = e.getEvent().getVolumeLow();
											lvl.volumeHigh = e.getEvent().getVolumeHigh();
										}
										// FM cargo out happens only when there's a vessel
										lvl.cargoOut = lvl.changeInM3;
										addToInventoryLevelList(tableLevels, lvl);
									} else if (e.getOpenSlotAllocation() != null) {
										type = "Open";
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity(), null, null, null, null, null);
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										if (e.getEvent() != null) {
											lvl.volumeLow = e.getEvent().getVolumeLow();
											lvl.volumeHigh = e.getEvent().getVolumeHigh();
										}
										// FM
										setInventoryLevelFeed(lvl);
										addToInventoryLevelList(tableLevels, lvl);
									} else if (e.getEvent() != null) {
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), e.getEvent().getPeriod(), e.getChangeQuantity(), null, null, null, null, null);
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										if (e.getEvent() != null) {
											lvl.volumeLow = e.getEvent().getVolumeLow();
											lvl.volumeHigh = e.getEvent().getVolumeHigh();
										}
										InventoryChangeEvent first = firstInventoryDataFinal.get();
										
										// FM
										setInventoryLevelFeed(lvl);
										addToInventoryLevelList(tableLevels, lvl);
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
									.map(e -> new Pair<>(e.getKey(), e.getValue())) //
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
									.map(e -> new Pair<>(e.getKey(), e.getValue())) //
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
									.map(e -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
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
									.map(e -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
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
										.map(e -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
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

		int total = 0;
		if (firstInventoryData.isPresent()) {
			// Set the current level to the first data in the list. Remove the change quantity so the first iteration of the loop tallies.
			final InventoryChangeEvent evt = firstInventoryData.get();
			total = evt.getCurrentLevel() - evt.getChangeQuantity();
		}
		int totalLow = total;
		int totalHigh = total;
		for (final InventoryLevel lvl : tableLevels) {
			total += lvl.changeInM3;
			lvl.runningTotal = total;
			/*
			 * In the case, when the low/high forecast value is zero , we assume that's a wrong data!
			 * Hence we use the feedIn (actual volume) if it's also not zero.
			 * Maybe we need to fix that!
			 */
			final int vl = lvl.volumeLow == 0 ? lvl.feedIn == 0 ? 0 : lvl.feedIn : lvl.volumeLow;
			totalLow += vl - Math.abs(lvl.feedOut) - Math.abs(lvl.cargoOut);
			lvl.ttlLow = totalLow;
			final int vh = lvl.volumeHigh == 0 ? lvl.feedIn == 0 ? 0 : lvl.feedIn : lvl.volumeHigh;
			totalHigh += vh - Math.abs(lvl.feedOut) - Math.abs(lvl.cargoOut);
			lvl.ttlHigh = totalHigh;
			
		}
		
		tableViewer.setInput(tableLevels);
	}

	private void addToInventoryLevelList(final List<InventoryLevel> tableLevels, final InventoryLevel lvl) {
		//can swap with proper search through the list
		final int i = tableLevels.size();
		if (i > 0) {
			final InventoryLevel tlvl = tableLevels.get(i-1);
			if (tlvl.date.equals(lvl.date)) {
				tlvl.merge(lvl);
			} else {
				tableLevels.add(lvl);
			}
		} else {
			tableLevels.add(lvl);
		}
	}

	private void setInventoryLevelFeed(final InventoryLevel lvl) {
		if (lvl.changeInM3 > 0) {
			lvl.feedIn = lvl.changeInM3;
		}else {
			lvl.feedOut = lvl.changeInM3;
		}
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

	private void snapTo(LocalDate date) {
		if (tableViewer == null) {
			return;
		}
		final Grid grid = tableViewer.getGrid();
		if (grid == null) {
			return;
		}
		final int count = grid.getItemCount();
		if (count <= 0) {
			return;
		}
		
		final GridItem[] items = grid.getItems();
		int pos = -1;
		for (GridItem item : items) {
			Object oData = item.getData();
			if (oData instanceof InventoryLevel) {
				InventoryLevel il = (InventoryLevel) oData;
				if (il.date.isAfter(date)) {
					break;
				}
				pos++;
			}
		}
		if (pos != -1) {
			grid.deselectAll();
			grid.select(pos);
			grid.showSelection();
		}
	}

}
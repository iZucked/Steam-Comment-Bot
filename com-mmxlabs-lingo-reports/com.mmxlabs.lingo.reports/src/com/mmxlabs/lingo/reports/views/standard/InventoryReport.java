/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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
import com.mmxlabs.lingo.reports.views.standard.inventory.MullInformation;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.InventorySubprofile;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.properties.ui.YearMonthFormatLabelProvider;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.IComparableProvider;
import com.mmxlabs.models.ui.tabular.IFilterProvider;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
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
	
	private GridTableViewer mullTableViewer;
	
	private FilterField filterField;
	private EObjectTableViewerFilterSupport filterSupport;
	
	private EObjectTableViewerSortingSupport sortingSupport;

	private Inventory selectedInventory;
	private ScenarioResult currentResult;
	
	private ScenarioResult pinnedResult;
	private ScenarioResult otherResult;
	
	private Color color_Orange;
	private Color color_light_grey;
	private final Color colourRed = new Color(Display.getDefault(), 255, 36, 0);

	private EventHandler todayHandler;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = () -> {

				selectedInventory = null;

				currentResult = pinned;
				
				pinnedResult = pinned;
				otherResult = others.isEmpty() ? null : others.iterator().next();

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
	
	private PackGridTableColumnsAction packAction2;
	private CopyGridToClipboardAction copyAction2;

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		color_Orange = new Color(PlatformUI.getWorkbench().getDisplay(), new RGB(255, 200, 15));
		color_light_grey = new Color(PlatformUI.getWorkbench().getDisplay(), new RGB(230, 230, 230));
		filterField = new FilterField(parent);

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
			final DateTimeFormatter formatter = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
			{
				createColumn("Date", 150, o -> "" + o.date.format(formatter));
				// createColumn("Type", 150, o -> o.type);
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

		
		final CTabItem mullItem = new CTabItem(folder, SWT.NONE);
		
		
		
		mullItem.setText("MULL Monthly");
		{
			mullTableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);
			
			//final Composite container = new Composite(folder, SWT.NONE);
			
			this.filterSupport = new EObjectTableViewerFilterSupport(mullTableViewer, mullTableViewer.getGrid());
			this.sortingSupport = new EObjectTableViewerSortingSupport();
			mullTableViewer.setComparator(sortingSupport.createViewerComparer());
			
			mullTableViewer.addFilter(filterSupport.createViewerFilter());
			filterField.setFilterSupport(filterSupport);
			
			getViewSite().getActionBars().getToolBarManager().add(filterField.getContribution());
			
			
			mullTableViewer.setContentProvider(new ArrayContentProvider());
			mullTableViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			
			GridViewerHelper.configureLookAndFeel(mullTableViewer);
			mullTableViewer.getGrid().setTreeLinesVisible(true);
			mullTableViewer.getGrid().setHeaderVisible(true);
			
			final DateTimeFormatter formatter = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
			final YearMonthFormatLabelProvider formatter2;
			
			
			
			{
				createMullColumn("Month", 150, o -> String.format("%d-%d", o.getFirst().ym.getMonthValue(), o.getFirst().ym.getYear()), o -> o.getFirst().getYM());
				createMullColumn("Entity", 150, o -> o.getFirst().entity.getName(), o -> o.getFirst().entity.getName());
				// createMullColumn("Monthly Overlift", 150, o -> generateDiffString(o, MullInformation::getOverlift), o -> generateSortValue(o, MullInformation::getOverlift));
				createMullColumn("Overlift", 150, o-> generateDiffString(o, MullInformation::getOverliftCF), o-> generateSortValue(o, MullInformation::getOverliftCF));
				createMullColumn("Total lifted", 150, o -> generateDiffString(o, MullInformation::getLifted), o-> generateSortValue(o, MullInformation::getLifted));
				createMullColumn("# Cargo", 150, o -> generateDiffString(o, MullInformation::getCargoCount), o -> generateSortValue(o, MullInformation::getCargoCount));
				createMullColumn("Monthly entitlement", 150, o -> generateDiffString(o, MullInformation::getMonthlyRE), o -> generateSortValue(o, MullInformation::getMonthlyRE));
				createMullColumn("Carried entitlement", 150, o -> generateDiffString(o, MullInformation::getCarriedEntitlement), o -> generateSortValue(o, MullInformation::getCarriedEntitlement));
				
				mullItem.setControl(mullTableViewer.getControl());
			}
		}
		
		packAction2 = PackActionFactory.createPackColumnsAction(mullTableViewer);
		getViewSite().getActionBars().getToolBarManager().add(packAction2);
		packAction2.setEnabled(folder.getSelectionIndex() == 2);
		
		copyAction2 = CopyToClipboardActionFactory.createCopyToClipboardAction(mullTableViewer);
		getViewSite().getActionBars().getToolBarManager().add(copyAction2);
		copyAction2.setEnabled(folder.getSelectionIndex() == 2);
		
		
		// Adding an event broker for the snap-to-date event todayHandler
		IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		this.todayHandler = event -> snapTo((LocalDate) event.getProperty(IEventBroker.DATA));
		eventBroker.subscribe(TodayHandler.EVENT_SNAP_TO_DATE, this.todayHandler);

		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copyAction.setEnabled(folder.getSelectionIndex() == 1);
				packAction.setEnabled(folder.getSelectionIndex() == 1);
				copyAction2.setEnabled(folder.getSelectionIndex() == 2);
				packAction2.setEnabled(folder.getSelectionIndex() == 2);
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
		final List<MullInformation> mullList = new LinkedList<>();
		
		final List<Pair<MullInformation,MullInformation>> pairedMullList = new LinkedList<>();

		Optional<InventoryChangeEvent> firstInventoryData = Optional.empty();

		if (toDisplay != null) {
			final ScheduleModel scheduleModel = toDisplay.getTypedResult(ScheduleModel.class);
			if (scheduleModel != null) {
				final Schedule schedule = scheduleModel.getSchedule();
				if (schedule != null) {
					// List<>
					List<CargoAllocation> l = schedule.getCargoAllocations();
					
					assert inventoryModels.size() == 1;
					final Inventory expectedInventory = inventoryModels.iterator().next();
					final Port expectedLoadPort = expectedInventory.getPort();

					final ADPModel adpModel = ((LNGScenarioModel)(scheduleModel.eContainer())).getAdpModel();
					final YearMonth adpStart = adpModel.getYearStart();
					final YearMonth adpEnd = adpModel.getYearEnd();

					final Map<YearMonth, Integer> monthlyProduction = calculateMonthlyProduction(expectedInventory, adpStart);
					
					final InventorySubprofile sProfile = adpModel.getMultipleInventoriesProfile().getInventories().stream().filter(s -> s.getInventory().equals(expectedInventory)).findAny().get();
					final List<BaseLegalEntity> entitiesOrdered = calculateEntityOrder(sProfile);
					
					if (pinnedResult != null) {
						final ScheduleModel pinnedScheduleModel = pinnedResult.getTypedResult(ScheduleModel.class);
						final Schedule pinnedSchedule = pinnedScheduleModel.getSchedule();
						final List<MullInformation> pinnedMullList = new LinkedList<>();
						if (pinnedSchedule != null) {
							final List<CargoAllocation> pinnedCargoAllocations = pinnedSchedule.getCargoAllocations();
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> pinnedMonthlyRE = calculateMonthlyRE(adpStart, adpEnd, sProfile, monthlyProduction);
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> pinnedActualLift = calculateActualLift(adpStart, adpEnd, sProfile, pinnedCargoAllocations, expectedLoadPort);
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> pinnedCargoCount = calculateCargoCount(adpStart, adpEnd, sProfile, pinnedCargoAllocations, expectedLoadPort);
							final Map<BaseLegalEntity, Integer> pinnedInitialAllocation = calculateInitialAllocation(sProfile);
							
							for (YearMonth ym = adpStart; ym.isBefore(adpEnd); ym = ym.plusMonths(1)) {
								for (BaseLegalEntity entity : entitiesOrdered) {
									final MullInformation currRow = new MullInformation();
									currRow.entity = entity;
									currRow.ym = ym;
									currRow.lifted = pinnedActualLift.get(ym).get(entity);
									currRow.overlift = pinnedActualLift.get(ym).get(entity) - pinnedMonthlyRE.get(ym).get(entity);
									currRow.monthlyRE = pinnedMonthlyRE.get(ym).get(entity);
									currRow.cargoCount = pinnedCargoCount.get(ym).get(entity);
									pinnedMullList.add(currRow);
								}
							}
							calculateMonthEndEntitlement(pinnedMullList, adpStart);
							calculateCarriedEntitlement(pinnedMullList, adpStart, pinnedInitialAllocation);
						}
						final ScheduleModel otherScheduleModel = otherResult.getTypedResult(ScheduleModel.class);
						final Schedule otherSchedule = otherScheduleModel.getSchedule();
						final List<MullInformation> otherMullList = new LinkedList<>();
						if (otherSchedule != null) {
							final LNGScenarioModel otherLngScenarioModel = (LNGScenarioModel) otherResult.getRootObject();
							final ADPModel otherADPModel = otherLngScenarioModel.getAdpModel();
							
							final Inventory otherInventory = otherLngScenarioModel.getCargoModel().getInventoryModels().stream().filter(i -> i.getName().equals(expectedInventory.getName())).findAny().get();
							
							
							final InventorySubprofile otherSProfile = otherADPModel.getMultipleInventoriesProfile().getInventories().stream().filter(s -> s.getInventory().equals(otherInventory)).findAny().get();
							final Port otherInventoryLoadPort = otherInventory.getPort();
							
							final List<CargoAllocation> otherCargoAllocations = otherSchedule.getCargoAllocations();
							final Map<BaseLegalEntity, Integer> otherInitialAllocation = calculateInitialAllocation(otherSProfile);
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> otherMonthlyRE = calculateMonthlyRE(adpStart, adpEnd, otherSProfile, monthlyProduction);
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> otherActualLift = calculateActualLift(adpStart, adpEnd, otherSProfile, otherCargoAllocations, otherInventoryLoadPort);
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> otherCargoCount = calculateCargoCount(adpStart, adpEnd, otherSProfile, otherCargoAllocations, otherInventoryLoadPort);
							
							final List<BaseLegalEntity> otherEntities = otherLngScenarioModel.getReferenceModel().getCommercialModel().getEntities();
							final Map<BaseLegalEntity,BaseLegalEntity> entityEntityMap = new HashMap<>();
							for (BaseLegalEntity entityToKeep : entitiesOrdered) {
								otherEntities.stream().filter(e -> entityToKeep.getName().equals(e.getName())).findAny().ifPresent(e -> entityEntityMap.put(entityToKeep, e));
							}
							
							for (YearMonth ym = adpStart; ym.isBefore(adpEnd); ym = ym.plusMonths(1)) {
								for (BaseLegalEntity entity : entitiesOrdered) {
									final BaseLegalEntity otherEntityMirrorObject = entityEntityMap.get(entity);
									final MullInformation currRow = new MullInformation();
									currRow.entity = entity;
									currRow.ym = ym;
									currRow.lifted = otherActualLift.get(ym).get(otherEntityMirrorObject);
									currRow.overlift = otherActualLift.get(ym).get(otherEntityMirrorObject) - otherMonthlyRE.get(ym).get(otherEntityMirrorObject);
									currRow.monthlyRE = otherMonthlyRE.get(ym).get(otherEntityMirrorObject);
									currRow.cargoCount = otherCargoCount.get(ym).get(otherEntityMirrorObject);
									// currRow.monthlyRECF = 
									otherMullList.add(currRow);
								}
							}
							calculateMonthEndEntitlement(otherMullList, adpStart);
							//calculateMonthlyRECF(otherMullList, adpStart, otherInitialAllocation);
							calculateOverliftCF(otherMullList, adpStart);
							Map<BaseLegalEntity, Integer> mappedOtherInitialAllocation = new HashMap<>();
							entitiesOrdered.stream().forEach(e -> mappedOtherInitialAllocation.put(e, otherInitialAllocation.get(entityEntityMap.get(e))));
							//otherInitialAllocation.entrySet().stream()
							calculateCarriedEntitlement(otherMullList, adpStart, mappedOtherInitialAllocation);
						}
						
						if (pinnedMullList.isEmpty() && !otherMullList.isEmpty()) {
							final Iterator<MullInformation> otherIter = otherMullList.iterator();
							while (otherIter.hasNext()) {
								pairedMullList.add(new Pair<>(otherIter.next(), null));
							}
						} else if (!pinnedMullList.isEmpty() && otherMullList.isEmpty()) {
							final Iterator<MullInformation> pinnedIter = pinnedMullList.iterator();
							while (pinnedIter.hasNext()) {
								pairedMullList.add(new Pair<>(pinnedIter.next(), null));
							}
						} else if (!pinnedMullList.isEmpty() && !otherMullList.isEmpty()) {
							final Iterator<MullInformation> pinnedIter = pinnedMullList.iterator();
							final Iterator<MullInformation> otherIter = otherMullList.iterator();
							while (pinnedIter.hasNext() && otherIter.hasNext()) {
								final MullInformation currPin = pinnedIter.next();
								final MullInformation currOth = otherIter.next();
								pairedMullList.add(new Pair<>(currPin, currOth));
							}
							assert !pinnedIter.hasNext() && !otherIter.hasNext();
						}
					} else if (otherResult != null) {
						final ScheduleModel otherScheduleModel = otherResult.getTypedResult(ScheduleModel.class);
						final Schedule otherSchedule = otherScheduleModel.getSchedule();
						if (otherSchedule != null) {
							final List<CargoAllocation> otherCargoAllocations = otherSchedule.getCargoAllocations();
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> otherMonthlyRE = calculateMonthlyRE(adpStart, adpEnd, sProfile, monthlyProduction);
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> otherActualLift = calculateActualLift(adpStart, adpEnd, sProfile, otherCargoAllocations, expectedLoadPort);
							final Map<YearMonth, Map<BaseLegalEntity,Integer>> otherCargoCount = calculateCargoCount(adpStart, adpEnd, sProfile, otherCargoAllocations, expectedLoadPort);
							final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(sProfile);
							final List<MullInformation> otherMullList = new LinkedList<>();
							for (YearMonth ym = adpStart; ym.isBefore(adpEnd); ym = ym.plusMonths(1)) {
								for (BaseLegalEntity entity : entitiesOrdered) {
									final MullInformation currRow = new MullInformation();
									currRow.entity = entity;
									currRow.ym = ym;
									currRow.lifted = otherActualLift.get(ym).get(entity);
									currRow.overlift = otherActualLift.get(ym).get(entity) - otherMonthlyRE.get(ym).get(entity);
									currRow.monthlyRE = otherMonthlyRE.get(ym).get(entity);
									currRow.cargoCount = otherCargoCount.get(ym).get(entity);
									otherMullList.add(currRow);
								}
							}
							calculateMonthEndEntitlement(otherMullList, adpStart);
							calculateOverliftCF(otherMullList, adpStart);
							calculateCarriedEntitlement(otherMullList, adpStart, initialAllocation);
							final Iterator<MullInformation> otherIter = otherMullList.iterator();
							while (otherIter.hasNext()) {
								pairedMullList.add(new Pair<>(otherIter.next(), null));
							}
						}
					}
					
					final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(sProfile);
					final Map<YearMonth, Map<BaseLegalEntity,Integer>> monthlyRE = calculateMonthlyRE(adpStart, adpEnd, sProfile, monthlyProduction);
					final Map<YearMonth, Map<BaseLegalEntity,Integer>> actualLift = calculateActualLift(adpStart, adpEnd, sProfile, l, expectedLoadPort);
					final Map<YearMonth, Map<BaseLegalEntity,Integer>> cargoCount = calculateCargoCount(adpStart, adpEnd, sProfile, l, expectedLoadPort);
					
					
					for (YearMonth ym = adpStart; ym.isBefore(adpEnd); ym = ym.plusMonths(1)) {
						for (BaseLegalEntity entity : entitiesOrdered) {
							final MullInformation currRow = new MullInformation();
							currRow.entity = entity;
							currRow.ym = ym;
							currRow.lifted = actualLift.get(ym).get(entity);
							currRow.overlift = actualLift.get(ym).get(entity) - monthlyRE.get(ym).get(entity);
							currRow.monthlyRE = monthlyRE.get(ym).get(entity);
							currRow.cargoCount = cargoCount.get(ym).get(entity);
							mullList.add(currRow);
						}
					}
					
					calculateMonthEndEntitlement(mullList, adpStart);
					calculateCarriedEntitlement(mullList, adpStart, initialAllocation);
					calculateOverliftCF(mullList, adpStart);
					
					int i = 0;
					
					for (final InventoryEvents inventoryEvents : schedule.getInventoryLevels()) {
						final Inventory inventory = inventoryEvents.getFacility();
						boolean cargoIn = inventory.getFacilityType() != InventoryFacilityType.UPSTREAM;

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
						if (invs == null) {
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
											boolean res = !x.getKey().isAfter(latestLoad.get()) && x.getKey().isAfter(firstInventoryDataFinal.get().getDate());
											return res;
										}
										return false;
									})//
									.map(e -> new Pair<>(e.getKey(), e.getValue())) //
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
										final SlotAllocation dischargeAllocation = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof DischargeSlot)
												.findFirst().get();
										final SlotAllocation loadAllocation = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof LoadSlot)
												.findFirst().get();
										final String dischargeId = dischargeAllocation.getName();
										final String dischargePort = dischargeAllocation.getPort().getName();
										final String loadId = loadAllocation.getName();
										final String loadPort = loadAllocation.getPort().getName();
										Contract contract = dischargeAllocation.getContract();
										String salesContract = "";
										if (contract != null) {
											salesContract = contract.getName();
										}
										contract = loadAllocation.getContract();
										String purchaseContract = "";
										if (contract != null) {
											purchaseContract = contract.getName();
										}
										
										ZonedDateTime dischargeTime = dischargeAllocation.getSlotVisit().getStart();
										ZonedDateTime loadTime = loadAllocation.getSlotVisit().getStart();
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity(), vessel, //
												dischargeId, dischargePort, salesContract, loadId, loadPort, purchaseContract, //
												dischargeTime == null ? null : dischargeTime.toLocalDate(), //
												loadTime == null ? null : loadTime.toLocalDate());
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										if (e.getEvent() != null) {
											lvl.volumeLow = e.getEvent().getVolumeLow();
											lvl.volumeHigh = e.getEvent().getVolumeHigh();
										}
										// FM cargo out and in happens only when there's a vessel
										if (inventory.getFacilityType() == InventoryFacilityType.DOWNSTREAM 
												|| inventory.getFacilityType() == InventoryFacilityType.HUB) {
											lvl.cargoIn = lvl.changeInM3;
										}
										if (inventory.getFacilityType() == InventoryFacilityType.UPSTREAM 
												|| inventory.getFacilityType() == InventoryFacilityType.HUB){
											lvl.cargoOut = lvl.changeInM3;
										}
										addToInventoryLevelList(tableLevels, lvl);
									} else if (e.getOpenSlotAllocation() != null) {
										type = "Open";
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity(), null, null, null, null, null, null, null, null, null);
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										if (e.getEvent() != null) {
											lvl.volumeLow = e.getEvent().getVolumeLow();
											lvl.volumeHigh = e.getEvent().getVolumeHigh();
										}
										// FM
										setInventoryLevelFeed(lvl);
										addToInventoryLevelList(tableLevels, lvl);
									} else if (e.getEvent() != null) {
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), e.getEvent().getPeriod(), e.getChangeQuantity(), null, null, null, null, null, null, null, null, null);
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
							
//							final List<Pair<LocalDateTime, Integer>> valls = inventoryEvents.getEvents().stream() //
//									.filter(evt -> evt.getSlotAllocation() != null) //
//									.filter(evt -> {
//										LocalDateTime evt.getDate()
//										evt.getDate().isAfter(evt.getSlotAllocation().getCargoAllocation().getSlotAllocations().get(0).getSlot().getWindowStart().plusDays(arg0))
//									}) //
//									.map(e -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
//									.collect(Collectors.toList());
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
			 * In the case, when the low/high forecast value is zero , we assume that's a wrong data! Hence we use the feedIn (actual volume) if it's also not zero. Maybe we need to fix that!
			 */
			final int vl = lvl.volumeLow == 0 ? lvl.feedIn == 0 ? 0 : lvl.feedIn : lvl.volumeLow;
			
			totalLow += vl - Math.abs(lvl.feedOut) - Math.abs(lvl.cargoOut) + Math.abs(lvl.cargoIn);
			lvl.ttlLow = totalLow;
			
			final int vh = lvl.volumeHigh == 0 ? lvl.feedIn == 0 ? 0 : lvl.feedIn : lvl.volumeHigh;
			totalHigh += vh - Math.abs(lvl.feedOut) - Math.abs(lvl.cargoOut) + Math.abs(lvl.cargoIn);
			lvl.ttlHigh = totalHigh;

		}

		tableViewer.setInput(tableLevels);
		// mullTableViewer.setInput(mullList);
		mullTableViewer.setInput(pairedMullList);
	}



	private void addToInventoryLevelList(final List<InventoryLevel> tableLevels, final InventoryLevel lvl) {
		// can swap with proper search through the list
		final int i = tableLevels.size();
		if (i > 0) {
			final InventoryLevel tlvl = tableLevels.get(i - 1);
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
		} else {
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
	
	private GridViewerColumn createMullColumn(final String title, final int width, Function<Pair<MullInformation,MullInformation>, String> labelProvider, Function<Pair<MullInformation, MullInformation>, Comparable<?>> sortingFetcher) {
		final GridViewerColumn column = new GridViewerColumn(mullTableViewer, SWT.NONE);
		GridViewerHelper.configureLookAndFeel(column);
		column.getColumn().setText(title);
		column.getColumn().setWidth(width);
		column.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final Pair<MullInformation, MullInformation> element = (Pair<MullInformation, MullInformation>) cell.getElement();
				final String cellText = labelProvider.apply(element);
				cell.setText(cellText);
				if (element.getFirst().ym.getMonthValue() % 2 == 1) {
					cell.setBackground(color_light_grey);
				}
				if (!title.equals("Month") && !title.equals("Entity") && element.getSecond() != null) {
					if (cellText.length() > 0) {
						if (cellText.equals("0")) {
							// cell.setForeground(colourRed);
						} else {
							cell.setForeground(colourRed);
//							if (cellText.substring(1).contains("-")) {
//								cell.setForeground(colourRed);
//							}
						}
					}
				}
			}
		});

		this.filterSupport.createColumnMnemonics(column.getColumn(), column.getColumn().getText());
		final IFilterProvider filterProvider = new IFilterProvider() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof Pair<?, ?>) {
					return labelProvider.apply((Pair<MullInformation, MullInformation>) object);
				}
				return null;
			}

			@Override
			public Object getFilterValue(final Object object) {
				return labelProvider.apply((Pair<MullInformation,MullInformation>) object);
			}
		};

		column.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);
		
		this.sortingSupport.addSortableColumn(mullTableViewer, column, column.getColumn());
		final IComparableProvider sortingProvider = (m) -> {
			return sortingFetcher.apply((Pair<MullInformation, MullInformation>) m);
		};
		
		column.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
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
	
	private Map<YearMonth, Integer> calculateMonthlyProduction(final Inventory inventory, final YearMonth adpStart) {
		final Map<YearMonth, Integer> monthlyProduction = new HashMap<>();
		inventory.getFeeds().stream().forEach(row -> {
			final YearMonth currYM = YearMonth.from(row.getStartDate());
			final Integer currVal = monthlyProduction.get(currYM);
			if (currVal == null) {
				monthlyProduction.put(currYM, row.getVolume());
			} else {
				monthlyProduction.put(currYM, currVal+row.getVolume());
			}
		});
		inventory.getOfftakes().stream().forEach(row -> {
			final YearMonth currYM = YearMonth.from(row.getStartDate());
			final Integer currVal = monthlyProduction.get(currYM);
			if (currVal == null) {
				monthlyProduction.put(currYM, -row.getVolume());
			} else {
				monthlyProduction.put(currYM, currVal-row.getVolume());
			}
		});
		final Set<YearMonth> ymToAccumulate = monthlyProduction.keySet().stream().filter(adpStart::isAfter).collect(Collectors.toSet());
		Integer accTemp = 0;
		for (final YearMonth ym : ymToAccumulate) {
			accTemp += monthlyProduction.remove(ym);
		}
		final Integer acc = accTemp;
		monthlyProduction.compute(adpStart, (k, v) -> v + acc);
		
		return monthlyProduction;
	}
	
	private Map<YearMonth, Map<BaseLegalEntity, Integer>> calculateMonthlyRE(final YearMonth adpStart, final YearMonth adpEnd, final InventorySubprofile sProfile, final Map<YearMonth, Integer> monthlyProduction) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> monthlyRE = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd); ym = ym.plusMonths(1)) {
			final Integer thisTotalProd = monthlyProduction.get(ym);
			final Map<BaseLegalEntity, Integer> currMap = new HashMap<>();
			monthlyRE.put(ym, currMap);
			final YearMonth ymm = ym;
			sProfile.getEntityTable().stream().forEach(row -> {
				if (ymm.equals(adpStart)) {
					
					int calc1 = ((Double) (row.getRelativeEntitlement()*thisTotalProd)).intValue();
					int initVal = (Integer.parseInt(row.getInitialAllocation()));
					int t = calc1 + initVal;
					int v = (Integer.parseInt(row.getInitialAllocation()) + ((int) (row.getRelativeEntitlement()*thisTotalProd)));
					currMap.put(row.getEntity(), (int) (Integer.parseInt(row.getInitialAllocation()) + ((int) (row.getRelativeEntitlement()*thisTotalProd))));
					int j = 0;
				} else {
					currMap.put(row.getEntity(), (int) (row.getRelativeEntitlement()*thisTotalProd));
				}
			});
		}
		return monthlyRE;
	}
	
	private Map<YearMonth, Map<BaseLegalEntity,Integer>> calculateActualLift(final YearMonth adpStart, final YearMonth adpEnd, final InventorySubprofile sProfile, final List<CargoAllocation> cargoAllocations, final Port inventoryLoadPort) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> actualLift = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd.plusMonths(1)); ym = ym.plusMonths(1)) {
			final Map<BaseLegalEntity, Integer> currMap = new HashMap<>();
			sProfile.getEntityTable().stream().map(InventoryADPEntityRow::getEntity).forEach(entity -> currMap.put(entity, 0));
			actualLift.put(ym, currMap);
		}
		cargoAllocations.stream() //
				.map(cargoAlloc -> cargoAlloc.getSlotAllocations().get(0))
				.filter(loadSlotAlloc -> loadSlotAlloc.getPort().equals(inventoryLoadPort))
				.forEach(loadSlotAlloc -> {
					final YearMonth ym = YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate());
					final BaseLegalEntity thisEntity = loadSlotAlloc.getSlot().getEntity();
					actualLift.get(ym).computeIfPresent(thisEntity, (k, v) -> v + loadSlotAlloc.getPhysicalVolumeTransferred());
				});
		return actualLift;
	}
	
	private Map<YearMonth, Map<BaseLegalEntity,Integer>> calculateCargoCount(final YearMonth adpStart, final YearMonth adpEnd, final InventorySubprofile sProfile, final List<CargoAllocation> cargoAllocations, final Port inventoryLoadPort) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> cargoCount = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd.plusMonths(1)); ym = ym.plusMonths(1)) {
			final Map<BaseLegalEntity, Integer> cargoCountMap = new HashMap<>();
			sProfile.getEntityTable().stream().map(InventoryADPEntityRow::getEntity).forEach(entity -> cargoCountMap.put(entity, 0));
			cargoCount.put(ym, cargoCountMap);
		}
		cargoAllocations.stream() //
		.map(cargoAlloc -> cargoAlloc.getSlotAllocations().get(0))
		.filter(loadSlotAlloc -> loadSlotAlloc.getPort().equals(inventoryLoadPort))
		.forEach(loadSlotAlloc -> {
			final YearMonth ym = YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate());
			final BaseLegalEntity thisEntity = loadSlotAlloc.getSlot().getEntity();
			cargoCount.get(ym).computeIfPresent(thisEntity, (k, v) -> v + 1);
		});
		return cargoCount;
	}
	
	private void calculateMonthEndEntitlement(final List<MullInformation> mullList, final YearMonth adpStart) {
		final Map<BaseLegalEntity, MullInformation> prev = new HashMap<>();
		for (MullInformation curr : mullList) {
			if (curr.ym.equals(adpStart)) {
				curr.monthEndEntitlement = curr.monthlyRE - curr.lifted;
			} else {
				curr.monthEndEntitlement = prev.get(curr.entity).monthEndEntitlement +curr.monthlyRE - curr.lifted;
			}
			prev.put(curr.entity, curr);
		}
	}
	
	private void calculateMonthlyRECF(final List<MullInformation> mullList, final YearMonth adpStart, final Map<BaseLegalEntity, Integer> initialAllocations) {
		for (MullInformation curr : mullList) {
			if (curr.ym.equals(adpStart)) {
				
			}
		}
	}
	
	private List<BaseLegalEntity> calculateEntityOrder(final InventorySubprofile sProfile) {
		final Map<BaseLegalEntity,Double> orderVal = new HashMap<>();
		sProfile.getEntityTable().stream().forEach(row -> orderVal.put(row.getEntity(), row.getRelativeEntitlement()));
		return sProfile.getEntityTable().stream() //
				.map(InventoryADPEntityRow::getEntity) //
				.sorted((a, b) -> orderVal.get(b).compareTo(orderVal.get(a))) //
				.collect(Collectors.toList());
	}
	
	private String generateDiffString(final Pair<MullInformation, MullInformation> element, final Function<MullInformation, Integer> valueExtractor) {
		final MullInformation pinned = element.getFirst();
		final MullInformation other = element.getSecond();
		final int pinnedVal = valueExtractor.apply(pinned);
		if (other != null) {
			final int otherVal = valueExtractor.apply(other);
			if (pinnedVal == otherVal) {
				return "0";
				//return String.format("%,d", pinnedVal);
			} else {
				int difference = otherVal - pinnedVal;
				return String.format("%,d", difference);
//				if (difference > 0) {
//					return String.format("%,d + %,d", pinnedVal, difference);
//				} else {
//					return String.format("%,d - %,d", pinnedVal, -1*difference);
//				}
			}
		} else {
			return String.format("%,d", pinnedVal);
		}
	}
	
	private Map<BaseLegalEntity, Integer> calculateInitialAllocation(InventorySubprofile sProfile) {
		Map<BaseLegalEntity, Integer> initialAllocations = new HashMap<>();
		sProfile.getEntityTable().stream().forEach(row -> initialAllocations.put(row.getEntity(), Integer.parseInt(row.getInitialAllocation())));
		return initialAllocations;
	}
	
	private Comparable generateSortValue(final Pair<MullInformation, MullInformation> element, final Function<MullInformation, Integer> valueExtractor) {
		final MullInformation otherVal = element.getSecond();
		if (element.getSecond() == null) {
			return valueExtractor.apply(element.getFirst());
		} else {
			return valueExtractor.apply(otherVal) - valueExtractor.apply(element.getFirst());
		}
	}
	
	private void calculateOverliftCF(final List<MullInformation> mullList, final YearMonth adpStart) {
//		final Map<BaseLegalEntity, MullInformation> prev = new HashMap<>();
//		for (MullInformation curr : mullList) {
//			if (curr.ym.equals(adpStart)) {
//				curr.monthEndEntitlement = curr.monthlyRE - curr.lifted;
//			} else {
//				curr.monthEndEntitlement = prev.get(curr.entity).monthEndEntitlement +curr.monthlyRE - curr.lifted;
//			}
//			prev.put(curr.entity, curr);
//		}
		final Map<BaseLegalEntity, MullInformation> prev = new HashMap<>();
		for (MullInformation curr : mullList) {
			if (curr.ym.equals(adpStart)) {
				curr.overliftCF = curr.overlift;
			} else {
				curr.overliftCF = curr.overlift - prev.get(curr.entity).monthEndEntitlement;
			}
			prev.put(curr.entity, curr);
		}
	}
	
	private void calculateCarriedEntitlement(List<MullInformation> mullList, YearMonth adpStart, Map<BaseLegalEntity, Integer> initialAllocation) {
		final Map<BaseLegalEntity, MullInformation> prev = new HashMap<>();
		for (MullInformation curr : mullList) {
			if (curr.ym.equals(adpStart)) {
				curr.carriedEntitlement = initialAllocation.get(curr.getEntity());
			} else {
				curr.carriedEntitlement = -prev.get(curr.entity).getOverliftCF();
			}
			prev.put(curr.entity, curr);
		}
	}
}
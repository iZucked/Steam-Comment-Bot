/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.ActionContributionItem;
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
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.views.standard.inventory.InventoryLevel;
import com.mmxlabs.lingo.reports.views.standard.inventory.MullDailyInformation;
import com.mmxlabs.lingo.reports.views.standard.inventory.MullInformation;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
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
import com.mmxlabs.scenario.service.ScenarioResult;

public class InventoryReport extends ViewPart {
	public static final String ID = "com.mmxlabs.lingo.reports.views.standard.InventoryReport";

	private Chart chartViewer;
	private Chart inventoryDailyChartViewer;
	private Chart chartViewer2;
	private Chart chartViewer3;
	
	private GridTableViewer inventoryTableViewer;
	
	private GridTableViewer mullMonthlyTableViewer;
	private GridTableViewer mullDailyTableViewer;
	
	private FilterField mullMonthlyTableFilterField;
	private EObjectTableViewerFilterSupport mullMonthlyTableFilterSupport;
//	private ActionContributionItem mullTableActionContributionItem;
	
	private FilterField mullDailyTableFilterField;
	private EObjectTableViewerFilterSupport mullDailyTableFilterSupport;
	
	private EObjectTableViewerSortingSupport mullMonthlyTableSortingSupport;
	private EObjectTableViewerSortingSupport mullDailySortingSupport;
	
	private int previousTabSelection;
	
	private ActionContributionItem inventoryTableCopyActionContributionItem;
	private ActionContributionItem inventoryTablePackActionContributionItem;
	private ActionContributionItem mullMonthlyTableCopyActionContributionItem;
	private ActionContributionItem mullMonthlyTablePackActionContributionItem;
	private ActionContributionItem mullDailyTableCopyActionContributionItem;
	private ActionContributionItem mullDailyTablePackActionContributionItem;

	private Inventory selectedInventory;
	private ScenarioResult currentResult;
	
	private ScenarioResult pinnedResult;
	private ScenarioResult otherResult;
	
	private Color colorOrange;
	private Color colorLightGrey;
	private final Color colourRed = new Color(Display.getDefault(), 255, 36, 0);

	private EventHandler todayHandler;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		public void selectedDataProviderChanged(ISelectedDataProvider selectedDataProvider, boolean block) {
			final Runnable r = () -> {

				selectedInventory = null;

				currentResult = selectedDataProvider.getPinnedScenarioResult();
				if (currentResult == null) {

					if (!selectedDataProvider.getAllScenarioResults().isEmpty()) {
						currentResult = selectedDataProvider.getAllScenarioResults().iterator().next();
					}
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

	private ScenarioComparisonService selectedScenariosService;

	private ComboViewer comboViewer;

	private String lastFacility = null;

	private PackGridTableColumnsAction inventoryTablePackAction;
	private CopyGridToClipboardAction inventoryTableCopyAction;
	private PackGridTableColumnsAction mullMonthlyTablePackAction;
	private CopyGridToClipboardAction mullMonthlyTableCopyAction;
	private PackGridTableColumnsAction mullDailyTablePackAction;
	private CopyGridToClipboardAction mullDailyTableCopyAction;

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		colorOrange = new Color(PlatformUI.getWorkbench().getDisplay(), new RGB(255, 200, 15));
		colorLightGrey = new Color(PlatformUI.getWorkbench().getDisplay(), new RGB(230, 230, 230));
		mullMonthlyTableFilterField = new FilterField(parent);
		
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			mullDailyTableFilterField = new FilterField(parent);
		}

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);
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
		
		final CTabItem inventoryDailyChartItem = new CTabItem(folder, SWT.NONE);
		inventoryDailyChartItem.setText("Inventory Daily");
		
		final CTabItem tableItem = new CTabItem(folder, SWT.NONE);
		tableItem.setText("Table");
		{
			chartViewer = new Chart(folder, SWT.NONE);
			chartViewer.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			chartViewer.getTitle().setVisible(false);
			chartItem.setControl(chartViewer);
		}
		{
			inventoryDailyChartViewer = new Chart(folder,  SWT.None);
			inventoryDailyChartViewer.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			inventoryDailyChartViewer.getTitle().setVisible(false);
			inventoryDailyChartItem.setControl(inventoryDailyChartViewer);
		}
		
		{
			inventoryTableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);
			inventoryTableViewer.setContentProvider(new ArrayContentProvider());
			inventoryTableViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			GridViewerHelper.configureLookAndFeel(inventoryTableViewer);
			inventoryTableViewer.getGrid().setTreeLinesVisible(true);
			inventoryTableViewer.getGrid().setHeaderVisible(true);
			final DateTimeFormatter formatter = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
			{
				createColumn("Date", 150, o -> "" + o.date.format(formatter));
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
				tableItem.setControl(inventoryTableViewer.getControl());
			}

		}

		final int folderSelection = 0;
		folder.setSelection(folderSelection);

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);

		inventoryTablePackAction = PackActionFactory.createPackColumnsAction(inventoryTableViewer);
		inventoryTablePackActionContributionItem = new ActionContributionItem(inventoryTablePackAction);
		getViewSite().getActionBars().getToolBarManager().add(inventoryTablePackActionContributionItem);
		inventoryTablePackActionContributionItem.setVisible(folderSelection == 1);

		inventoryTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(inventoryTableViewer);
		inventoryTableCopyActionContributionItem = new ActionContributionItem(inventoryTableCopyAction);
		getViewSite().getActionBars().getToolBarManager().add(inventoryTableCopyActionContributionItem);
		inventoryTableCopyActionContributionItem.setVisible(folderSelection == 1);

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			final CTabItem mullItem = new CTabItem(folder, SWT.NONE);
			mullItem.setText("MULL Monthly");
			{
				mullMonthlyTableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);
				
				this.mullMonthlyTableFilterSupport = new EObjectTableViewerFilterSupport(mullMonthlyTableViewer, mullMonthlyTableViewer.getGrid());
				this.mullMonthlyTableSortingSupport = new EObjectTableViewerSortingSupport();
				mullMonthlyTableViewer.setComparator(mullMonthlyTableSortingSupport.createViewerComparer());
				
				mullMonthlyTableViewer.addFilter(mullMonthlyTableFilterSupport.createViewerFilter());
				mullMonthlyTableFilterField.setFilterSupport(mullMonthlyTableFilterSupport);
				
				getViewSite().getActionBars().getToolBarManager().add(mullMonthlyTableFilterField.getContribution());
				mullMonthlyTableFilterField.getContribution().setVisible(folderSelection == 2);
				
				mullMonthlyTableViewer.setContentProvider(new ArrayContentProvider());
				mullMonthlyTableViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
				
				GridViewerHelper.configureLookAndFeel(mullMonthlyTableViewer);
				mullMonthlyTableViewer.getGrid().setTreeLinesVisible(true);
				mullMonthlyTableViewer.getGrid().setHeaderVisible(true);
				
				{
					createMullColumn("Month", 150, o -> String.format("%d-%d", o.getFirst().ym.getMonthValue(), o.getFirst().ym.getYear()), o -> o.getFirst().getYM());
					createMullColumn("Entity", 150, o -> o.getFirst().entity.getName(), o -> o.getFirst().entity.getName());
					createMullColumn("Overlift", 150, o-> generateDiffString(o, MullInformation::getOverliftCF), o-> generateSortValue(o, MullInformation::getOverliftCF));
					createMullColumn("Total lifted", 150, o -> generateDiffString(o, MullInformation::getLifted), o-> generateSortValue(o, MullInformation::getLifted));
					createMullColumn("# Cargo", 150, o -> generateDiffString(o, MullInformation::getCargoCount), o -> generateSortValue(o, MullInformation::getCargoCount));
					createMullColumn("Monthly entitlement", 150, o -> generateDiffString(o, MullInformation::getMonthlyRE), o -> generateSortValue(o, MullInformation::getMonthlyRE));
					createMullColumn("Carried entitlement", 150, o -> generateDiffString(o, MullInformation::getCarriedEntitlement), o -> generateSortValue(o, MullInformation::getCarriedEntitlement));
					
					mullItem.setControl(mullMonthlyTableViewer.getControl());
				}
			}
			
			mullMonthlyTablePackAction = PackActionFactory.createPackColumnsAction(mullMonthlyTableViewer);
			mullMonthlyTablePackActionContributionItem = new ActionContributionItem(mullMonthlyTablePackAction);
			getViewSite().getActionBars().getToolBarManager().add(mullMonthlyTablePackActionContributionItem);
			mullMonthlyTablePackActionContributionItem.setVisible(folder.getSelectionIndex() == 2);
			
			mullMonthlyTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(mullMonthlyTableViewer);
			mullMonthlyTableCopyActionContributionItem = new ActionContributionItem(mullMonthlyTableCopyAction);
			getViewSite().getActionBars().getToolBarManager().add(mullMonthlyTableCopyActionContributionItem);
			mullMonthlyTableCopyActionContributionItem.setVisible(folder.getSelectionIndex() == 2);
			
			final CTabItem mullDailyItem = new CTabItem(folder, SWT.NONE);

			mullDailyItem.setText("MULL Daily");
			{
				mullDailyTableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);

				this.mullDailyTableFilterSupport = new EObjectTableViewerFilterSupport(mullDailyTableViewer, mullDailyTableViewer.getGrid());
				this.mullDailySortingSupport = new EObjectTableViewerSortingSupport();
				mullDailyTableViewer.setComparator(mullDailySortingSupport.createViewerComparer());
				
				mullDailyTableViewer.addFilter(mullDailyTableFilterSupport.createViewerFilter());
				mullDailyTableFilterField.setFilterSupport(mullDailyTableFilterSupport);
				
				getViewSite().getActionBars().getToolBarManager().add(mullDailyTableFilterField.getContribution());
				mullDailyTableFilterField.getContribution().setVisible(folderSelection == 3);
				
				mullDailyTableViewer.setContentProvider(new ArrayContentProvider());
				mullDailyTableViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

				GridViewerHelper.configureLookAndFeel(mullDailyTableViewer);
				mullDailyTableViewer.getGrid().setTreeLinesVisible(true);
				mullDailyTableViewer.getGrid().setHeaderVisible(true);
				{
					createMullDailyColumn("Date", 150, o -> {
						LocalDate date = o.getFirst().date;
						return String.format("%01d-%01d-%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
					}, o -> o.getFirst().date);
					createMullDailyColumn("Entity", 150, o -> o.getFirst().entity.getName(), o -> o.getFirst().entity.getName());
					createMullDailyColumn("Production allocation", 150, o -> generateDailyDiffString(o, MullDailyInformation::getAllocatedEntitlement), o -> generateDailySortValue(o, MullDailyInformation::getAllocatedEntitlement));
					createMullDailyColumn("Running entitlement", 150, o -> generateDailyDiffString(o, MullDailyInformation::getActualEntitlement), o -> generateDailySortValue(o, MullDailyInformation::getActualEntitlement));
					mullDailyItem.setControl(mullDailyTableViewer.getControl());
				}
				
			}
			
			mullDailyTablePackAction = PackActionFactory.createPackColumnsAction(mullDailyTableViewer);
			mullDailyTablePackActionContributionItem = new ActionContributionItem(mullDailyTablePackAction);
			getViewSite().getActionBars().getToolBarManager().add(mullDailyTablePackActionContributionItem);
			mullDailyTablePackActionContributionItem.setVisible(folder.getSelectionIndex() == 3);
			
			mullDailyTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(mullDailyTableViewer);
			mullDailyTableCopyActionContributionItem = new ActionContributionItem(mullDailyTableCopyAction);
			getViewSite().getActionBars().getToolBarManager().add(mullDailyTableCopyActionContributionItem);
			mullDailyTableCopyActionContributionItem.setVisible(folder.getSelectionIndex() == 3);
			
			final CTabItem chartItem2 = new CTabItem(folder, SWT.NONE);
			chartItem2.setText("MULL Chart");
			{
				chartViewer2 = new Chart(folder, SWT.NONE);
				chartViewer2.setLayoutData(GridDataFactory.fillDefaults().grab(true, true));
				chartViewer2.getTitle().setVisible(false);
				chartItem2.setControl(chartViewer2);
			}
			
			final CTabItem chartItem3 = new CTabItem(folder, SWT.NONE);
			chartItem3.setText("# Cargo Chart");
			{
				chartViewer3 = new Chart(folder, SWT.NONE);
				chartViewer3.setLayoutData(GridDataFactory.fillDefaults().grab(true, true));
				chartViewer3.getTitle().setVisible(false);
				chartItem3.setControl(chartViewer3);
			}
		}
		// Adding an event broker for the snap-to-date event todayHandler
		IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		this.todayHandler = event -> snapTo((LocalDate) event.getProperty(IEventBroker.DATA));
		eventBroker.subscribe(TodayHandler.EVENT_SNAP_TO_DATE, this.todayHandler);

		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final int currentTabSelection = folder.getSelectionIndex();
				inventoryTableCopyActionContributionItem.setVisible(currentTabSelection == 1);
				inventoryTablePackActionContributionItem.setVisible(currentTabSelection == 1);
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
					mullMonthlyTableCopyActionContributionItem.setVisible(currentTabSelection == 2);
					mullMonthlyTablePackActionContributionItem.setVisible(currentTabSelection == 2);
					if (mullMonthlyTableFilterField.getContribution().getAction().isChecked()) {
						mullMonthlyTableFilterField.toggleVisibility();
					}
					mullMonthlyTableFilterField.getContribution().setVisible(currentTabSelection == 2);
					
					mullDailyTableCopyActionContributionItem.setVisible(currentTabSelection == 3);
					mullDailyTablePackActionContributionItem.setVisible(currentTabSelection == 3);
					if(mullDailyTableFilterField.getContribution().getAction().isChecked()) {
						mullDailyTableFilterField.toggleVisibility();
					}
					mullDailyTableFilterField.getContribution().setVisible(currentTabSelection == 3);
				}
				getViewSite().getActionBars().getToolBarManager().update(true);
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
		if (colorOrange != null) {
			colorOrange.dispose();
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
		final ISeriesSet inventoryDailyChartSeriesSet = inventoryDailyChartViewer.getSeriesSet();
		
		
		// Delete existing data
		{
			final Set<String> names = new HashSet<>();
			for (final ISeries s : seriesSet.getSeries()) {
				names.add(s.getId());
			}
			names.forEach(seriesSet::deleteSeries);
		}
		
		final ISeriesSet seriesSet2 = chartViewer2.getSeriesSet();
		{
			final Set<String> names = new HashSet<>();
			for (final ISeries s : seriesSet2.getSeries()) {
				names.add(s.getId());
			}
			names.forEach(seriesSet2::deleteSeries);
		}
		
		final ISeriesSet seriesSet3 = chartViewer3.getSeriesSet();
		{
			final Set<String> names = new HashSet<>();
			for (final ISeries s : seriesSet3.getSeries()) {
				names.add(s.getId());
			}
			names.forEach(seriesSet3::deleteSeries);
		}
		LocalDate minDate = null;
		LocalDate maxDate = null;

		final List<InventoryLevel> tableLevels = new LinkedList<>();
		
		final List<Pair<MullInformation,MullInformation>> pairedMullList = new LinkedList<>();
		final List<Pair<MullDailyInformation, MullDailyInformation>> pairedDailyMullList = new LinkedList<>();

		Optional<InventoryChangeEvent> firstInventoryData = Optional.empty();

		if (toDisplay != null) {
			
			final ScheduleModel scheduleModel = toDisplay.getTypedResult(ScheduleModel.class);
			if (scheduleModel != null) {
				final Schedule schedule = scheduleModel.getSchedule();
				if (schedule != null) {
					for (final InventoryEvents inventoryEvents : schedule.getInventoryLevels()) {
						final Inventory inventory = inventoryEvents.getFacility();
						if (!inventoryModels.contains(inventory) || inventory.getName() == null) {
							continue;
						}
						final List<InventoryEventRow> invs = inventory.getFeeds();
						if (invs == null) {
							continue;
						}
						if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
							final Port expectedLoadPort = inventory.getPort();
							final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) toDisplay.getRootObject();
							final ADPModel adpModel = lngScenarioModel.getAdpModel();
							if (adpModel != null) {
								final MullProfile mullProfile = adpModel.getMullProfile();
								if (mullProfile != null) {
									final YearMonth adpStart = adpModel.getYearStart();
									final YearMonth adpEnd = adpModel.getYearEnd();
									final LocalDate startDate = adpStart.atDay(1);
									TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = getInventoryInsAndOuts(inventory, lngScenarioModel);
									int totalInventoryVolume = 0;
									while (!insAndOuts.isEmpty() && insAndOuts.firstKey().isBefore(startDate)) {
										final InventoryDailyEvent event = insAndOuts.remove(insAndOuts.firstKey());
										totalInventoryVolume += event.netVolumeIn;
									}
									if (!insAndOuts.isEmpty()) {
										insAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
										final Map<YearMonth, Integer> monthlyProduction = calculateMonthlyProduction(inventory, adpStart);
										final Optional<MullSubprofile> sProfileOpt = mullProfile.getInventories().stream().filter(s -> s.getInventory() != null && s.getInventory().equals(inventory)).findAny();
										if (sProfileOpt.isPresent()) {
											final MullSubprofile sProfile = sProfileOpt.get();
											if (!sProfile.getEntityTable().isEmpty() && isValidMullSubProfile(sProfile)) {
												final List<BaseLegalEntity> entitiesOrdered = calculateEntityOrder(sProfile);
												final List<Pair<BaseLegalEntity, Double>> relativeEntitlements = sProfile.getEntityTable().stream() //
														.map(row -> new Pair<BaseLegalEntity, Double>(row.getEntity(), row.getRelativeEntitlement())) //
														.collect(Collectors.toList());
												if (pinnedResult != null) {
													final ScheduleModel pinnedScheduleModel = pinnedResult.getTypedResult(ScheduleModel.class);
													final Schedule pinnedSchedule = pinnedScheduleModel.getSchedule();
													final LNGScenarioModel pinnedLNGScenarioModel = (LNGScenarioModel) pinnedResult.getRootObject();
													final List<MullInformation> pinnedMullList = new LinkedList<>();
													final List<MullDailyInformation> pinnedMullDailyList = new LinkedList<>();
													if (pinnedSchedule != null) {
														final List<CargoAllocation> pinnedCargoAllocations = pinnedSchedule.getCargoAllocations();
														
														fillMullInformationList(adpStart, adpEnd, entitiesOrdered, sProfile, monthlyProduction, expectedLoadPort, pinnedCargoAllocations, pinnedMullList, pinnedLNGScenarioModel);
														
														final List<CargoAllocation> sortedPinnedCargoAllocations = pinnedCargoAllocations.stream() //
																.filter(c->c.getSlotAllocations().get(0).getPort().equals(expectedLoadPort)) //
																.sorted((c1, c2) -> c1.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate().compareTo(c2.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate())) //
																.collect(Collectors.toList());
														final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(sProfile);
														
														final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = buildEntityEntityMap(pinnedLNGScenarioModel, entitiesOrdered);
														final Map<BaseLegalEntity, BaseLegalEntity> reverseEntityEntityMap = new HashMap<>();
														for (final Entry<BaseLegalEntity, BaseLegalEntity> entry : entityEntityMap.entrySet()) {
															reverseEntityEntityMap.put(entry.getValue(), entry.getKey());
														}
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> pinnedAllocatedEntitlement = calculateDailyAllocatedEntitlement(insAndOuts, relativeEntitlements, entityEntityMap, reverseEntityEntityMap);
														final List<BaseLegalEntity> otherEntities = sProfile.getEntityTable().stream().map(MullEntityRow::getEntity).collect(Collectors.toList());
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> pinnedActualEntitlement = calculateDailyActualEntitlement(otherEntities, sortedPinnedCargoAllocations, insAndOuts, pinnedAllocatedEntitlement, initialAllocation);
														insAndOuts.entrySet().stream() //
																.forEach(e -> {
																	for (BaseLegalEntity entity : entitiesOrdered) {
																		final MullDailyInformation currRow = new MullDailyInformation();
																		currRow.date = e.getKey();
																		currRow.entity = entity;
																		currRow.allocatedEntitlement = pinnedAllocatedEntitlement.get(currRow.getDate()).get(entity);
																		currRow.actualEntitlement = pinnedActualEntitlement.get(currRow.getDate()).get(entity);
																		pinnedMullDailyList.add(currRow);
																	}
																});
													}
													final ScheduleModel otherScheduleModel = otherResult.getTypedResult(ScheduleModel.class);
													final Schedule otherSchedule = otherScheduleModel.getSchedule();
													final List<MullInformation> otherMullList = new LinkedList<>();
													final List<MullDailyInformation> otherMullDailyList = new LinkedList<>();
													if (otherSchedule != null) {
														final LNGScenarioModel otherLngScenarioModel = (LNGScenarioModel) otherResult.getRootObject();
														final ADPModel otherADPModel = otherLngScenarioModel.getAdpModel();
														final Inventory otherInventory = otherLngScenarioModel.getCargoModel().getInventoryModels().stream().filter(i -> i.getName().equals(inventory.getName())).findAny().get();
														
														final MullSubprofile otherSProfile = otherADPModel.getMullProfile().getInventories().stream().filter(s -> s.getInventory().equals(otherInventory)).findAny().get();
														final Port otherInventoryLoadPort = otherInventory.getPort();
														
														final List<CargoAllocation> otherCargoAllocations = otherSchedule.getCargoAllocations();
														
														fillMullInformationList(adpStart, adpEnd, entitiesOrdered, otherSProfile, monthlyProduction, otherInventoryLoadPort, otherCargoAllocations, otherMullList, otherLngScenarioModel);
														
														final List<CargoAllocation> sortedOtherCargoAllocations = otherCargoAllocations.stream() //
																.filter(c->c.getSlotAllocations().get(0).getPort().equals(otherInventoryLoadPort)) //
																.sorted((c1, c2) -> c1.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate().compareTo(c2.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate())) //
																.collect(Collectors.toList());
														
														final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(otherSProfile);
														
														final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = buildEntityEntityMap(otherLngScenarioModel, entitiesOrdered);
														final Map<BaseLegalEntity, BaseLegalEntity> reverseEntityEntityMap = new HashMap<>();
														for (final Entry<BaseLegalEntity, BaseLegalEntity> entry : entityEntityMap.entrySet()) {
															reverseEntityEntityMap.put(entry.getValue(), entry.getKey());
														}
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherAllocatedEntitlement = calculateDailyAllocatedEntitlement(insAndOuts, relativeEntitlements, entityEntityMap, reverseEntityEntityMap);
														final List<BaseLegalEntity> otherEntities = otherSProfile.getEntityTable().stream().map(MullEntityRow::getEntity).collect(Collectors.toList());
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherActualEntitlement = calculateDailyActualEntitlement(otherEntities, sortedOtherCargoAllocations, insAndOuts, otherAllocatedEntitlement, initialAllocation);
														boolean grey = false;
														for (final Entry<LocalDate, InventoryDailyEvent> e : insAndOuts.entrySet()) {
															for (BaseLegalEntity entity : entitiesOrdered) {
																final MullDailyInformation currRow = new MullDailyInformation();
																currRow.date = e.getKey();
																currRow.entity = entity;
																currRow.allocatedEntitlement = otherAllocatedEntitlement.get(currRow.getDate()).get(entityEntityMap.get(entity));
																currRow.actualEntitlement = otherActualEntitlement.get(currRow.getDate()).get(entityEntityMap.get(entity));
																currRow.grey = grey;
																otherMullDailyList.add(currRow);
															}
															grey = !grey;
														}
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
													
													if (pinnedMullDailyList.isEmpty() && !otherMullDailyList.isEmpty()) {
														final Iterator<MullDailyInformation> otherIter = otherMullDailyList.iterator();
														while (otherIter.hasNext()) {
															pairedDailyMullList.add(new Pair<>(otherIter.next(), null));
														}
													} else if (!pinnedMullDailyList.isEmpty() && otherMullDailyList.isEmpty()) {
														final Iterator<MullDailyInformation> pinnedIter = pinnedMullDailyList.iterator();
														while (pinnedIter.hasNext()) {
															pairedDailyMullList.add(new Pair<>(pinnedIter.next(), null));
														}
													} else if (!pinnedMullDailyList.isEmpty() && !otherMullDailyList.isEmpty()) {
														final Iterator<MullDailyInformation> pinnedIter = pinnedMullDailyList.iterator();
														final Iterator<MullDailyInformation> otherIter = otherMullDailyList.iterator();
														while (pinnedIter.hasNext() && otherIter.hasNext()) {
															final MullDailyInformation currPin = pinnedIter.next();
															final MullDailyInformation currOth = otherIter.next();
															pairedDailyMullList.add(new Pair<>(currPin, currOth));
														}
														assert !pinnedIter.hasNext() && !otherIter.hasNext();
													}
													
												} else if (otherResult != null) {
													final ScheduleModel otherScheduleModel = otherResult.getTypedResult(ScheduleModel.class);
													final Schedule otherSchedule = otherScheduleModel.getSchedule();
													if (otherSchedule != null) {
														final List<CargoAllocation> otherCargoAllocations = otherSchedule.getCargoAllocations();
														final List<MullInformation> otherMullList = new LinkedList<>();
														final List<MullDailyInformation> otherMullDailyList = new LinkedList<>();
														
														fillMullInformationList(adpStart, adpEnd, entitiesOrdered, sProfile, monthlyProduction, expectedLoadPort, otherCargoAllocations, otherMullList, lngScenarioModel);
														final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(sProfile);
														final List<CargoAllocation> sortedOtherCargoAllocations = otherCargoAllocations.stream() //
																.filter(c->c.getSlotAllocations().get(0).getPort().equals(expectedLoadPort)) //
																.sorted((c1, c2) -> c1.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate().compareTo(c2.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate())) //
																.collect(Collectors.toList());
														
														final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = buildEntityEntityMap(lngScenarioModel, entitiesOrdered);
														final Map<BaseLegalEntity, BaseLegalEntity> reverseEntityEntityMap = new HashMap<>();
														for (final Entry<BaseLegalEntity, BaseLegalEntity> entry : entityEntityMap.entrySet()) {
															reverseEntityEntityMap.put(entry.getValue(), entry.getKey());
														}
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherAllocatedEntitlement = calculateDailyAllocatedEntitlement(insAndOuts, relativeEntitlements, entityEntityMap, reverseEntityEntityMap);
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherActualEntitlement = calculateDailyActualEntitlement(entitiesOrdered, sortedOtherCargoAllocations, insAndOuts, otherAllocatedEntitlement, initialAllocation);
														boolean grey = false;
														for (final Entry<LocalDate, InventoryDailyEvent> e : insAndOuts.entrySet()) {
															for (BaseLegalEntity entity : entitiesOrdered) {
																final MullDailyInformation currRow = new MullDailyInformation();
																currRow.date = e.getKey();
																currRow.entity = entity;
																currRow.allocatedEntitlement = otherAllocatedEntitlement.get(currRow.getDate()).get(entity);
																currRow.actualEntitlement = otherActualEntitlement.get(currRow.getDate()).get(entity);
																currRow.grey = grey;
																otherMullDailyList.add(currRow);
															}
															grey = !grey;
														}
														final Iterator<MullInformation> otherIter = otherMullList.iterator();
														while (otherIter.hasNext()) {
															pairedMullList.add(new Pair<>(otherIter.next(), null));
														}
														final Iterator<MullDailyInformation> otherDailyIter = otherMullDailyList.iterator();
														while (otherDailyIter.hasNext()) {
															pairedDailyMullList.add(new Pair<>(otherDailyIter.next(), null));
														}
													}
												}
												
												Map<BaseLegalEntity, List<Pair<Integer, Integer>>> barSeries = new HashMap<>();
												for (BaseLegalEntity entity : entitiesOrdered) {
													barSeries.put(entity, new LinkedList<>());
												}
												Map<BaseLegalEntity, List<Pair<Integer, Integer>>> cargoCountBarSeries = new HashMap<>();
												for (BaseLegalEntity entity : entitiesOrdered) {
													cargoCountBarSeries.put(entity, new LinkedList<>());
												}
												int i = 0;
												int spacing = 15;
												Iterator<Pair<MullInformation, MullInformation>> iterMullList = pairedMullList.iterator();
												while (iterMullList.hasNext()) {
													for (final BaseLegalEntity entity : entitiesOrdered) {
														final MullInformation currMull = iterMullList.next().getFirst();
														barSeries.get(entity).add(new Pair<>(i, currMull.overliftCF));
														i += 2;
													}
													i += spacing;
												}
												
												i = 0;
												spacing = 15;
												iterMullList = pairedMullList.iterator();
												while (iterMullList.hasNext()) {
													for (final BaseLegalEntity entity : entitiesOrdered) {
														final MullInformation currMull = iterMullList.next().getFirst();
														cargoCountBarSeries.get(entity).add(new Pair<>(i, currMull.cargoCount));
														i += 2;
													}
													i += spacing;
												}
												
												List<Integer> colours = new LinkedList<>();
												colours.add(SWT.COLOR_BLUE);
												colours.add(SWT.COLOR_GREEN);
												colours.add(SWT.COLOR_RED);
												colours.add(SWT.COLOR_CYAN);
												colours.add(SWT.COLOR_GRAY);
												colours.add(SWT.COLOR_MAGENTA);
												
												Iterator<Integer> colIter = colours.iterator();
												{
													for (BaseLegalEntity entity : entitiesOrdered) {
														final Integer currCol = colIter.next();
														final IBarSeries series = createMULLBarSeries(seriesSet2, entity.getName(), barSeries.get(entity));
														series.setBarWidth(20);
														series.setBarColor(Display.getDefault().getSystemColor(currCol));
														
													}
												}
												Iterator<Integer> colIter2 = colours.iterator();
												{
													for (BaseLegalEntity entity : entitiesOrdered) {
														final Integer currCol = colIter2.next();
														final IBarSeries series = createCargoCountBarSeries(seriesSet3, entity.getName(), cargoCountBarSeries.get(entity));
														series.setBarWidth(20);
														series.setBarColor(Display.getDefault().getSystemColor(currCol));
													}
												}
											}
										}
									}
								}
							}
						}

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
								final TreeMap<LocalDateTime, Integer> hourlyLevels = new TreeMap<>();
								for (final InventoryChangeEvent event : inventoryEvents.getEvents()) {
									final InventoryEventRow eventRow = event.getEvent();
									if (eventRow != null) {
										final int delta = eventRow.getVolume() / 24;
										final int firstAmount = delta + (eventRow.getVolume() % 24);
										LocalDateTime currentDateTime = LocalDateTime.of(eventRow.getStartDate(), LocalTime.of(0, 0));
										hourlyLevels.compute(currentDateTime, (k,v) -> v == null ? firstAmount: v + firstAmount);
										for (int hour = 1; hour < 24; hour++) {
											currentDateTime = currentDateTime.plusHours(1);
											hourlyLevels.compute(currentDateTime, (k,v) -> v == null ? delta: v + delta);
										}
										
									} else if (event.getSlotAllocation() != null) {
										final SlotAllocation loadAllocation = event.getSlotAllocation().getCargoAllocation().getSlotAllocations().get(0);
										final int volumeLoaded = loadAllocation.getVolumeTransferred();
										final LocalDateTime loadStart = loadAllocation.getSlotVisit().getStart().toLocalDateTime();
										if (((DischargeSlot) event.getSlotAllocation().getCargoAllocation().getSlotAllocations().get(1).getSlot()).isFOBSale()) {
											final int loadDuration = loadAllocation.getPort().getLoadDuration();
											final int delta = volumeLoaded/loadDuration;
											final int firstAmount = delta + (volumeLoaded % loadDuration);
											LocalDateTime currentDateTime = loadStart;
											hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? -firstAmount : v - firstAmount);
											for (int hour = 1; hour < loadDuration; ++hour) {
												currentDateTime = currentDateTime.plusHours(1);
												hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? -delta : v - delta);
											}
										} else {
											final int duration = loadAllocation.getSlotVisit().getDuration();
											final int delta = volumeLoaded/duration;
											final int firstAmount = delta + (volumeLoaded % duration);
											LocalDateTime currentDateTime = loadStart;
											hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? -firstAmount : v - firstAmount);
											for (int hour = 1; hour < duration; ++hour) {
												currentDateTime = currentDateTime.plusHours(1);
												hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? -delta : v - delta);
											}
										}
									}
								}
								final LongAccumulator acc = new LongAccumulator(Long::sum, 0L);
								final List<Pair<LocalDateTime, Integer>> hourlyInventoryLevels = hourlyLevels.entrySet().stream() //
										.map(entry -> {
											acc.accumulate(entry.getValue().longValue());
											return Pair.of(entry.getKey(), acc.intValue());
										}) //
										.collect(Collectors.toList());
								
								final TreeMap<LocalDateTime, Integer> dailyLevelsMap = new TreeMap<>();
								for (final InventoryChangeEvent event : inventoryEvents.getEvents()) {
									final InventoryEventRow eventRow = event.getEvent();
									if (eventRow != null) {
										final int change = eventRow.getVolume();
										dailyLevelsMap.compute(event.getDate(), (k, v) -> v == null ? change : v+change);
									}
								}
								final List<Pair<LocalDateTime, Integer>> dailyInventoryLevels = dailyLevelsMap.entrySet().stream() //
										.map(entry -> Pair.of(entry.getKey(), entry.getValue())) //
										.collect(Collectors.toList());
								
								final ILineSeries seriesDaily = createSmoothLineSeries(inventoryDailyChartSeriesSet, "Production", dailyInventoryLevels);
								seriesDaily.setSymbolType(PlotSymbolType.NONE);
								seriesDaily.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
								
								
								final ILineSeries seriesHourly = createSmoothLineSeries(seriesSet, "Inventory", hourlyInventoryLevels);
								//seriesHourly.setSymbolSize(1);
								seriesHourly.setSymbolType(PlotSymbolType.NONE);
								seriesHourly.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

								minDate = inventoryLevels.get(0).getFirst().toLocalDate();
								maxDate = inventoryLevels.get(inventoryLevels.size() - 1).getFirst().toLocalDate();

								inventoryEvents.getEvents().forEach(e -> {
									String type = "Unknown";
									if (e.getSlotAllocation() != null) {
										type = "Cargo";
										final String vessel = e.getSlotAllocation().getCargoAllocation().getEvents().get(0).getSequence().getName();
										final SlotAllocation dischargeAllocation = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream()
												.filter(x -> x.getSlot() instanceof DischargeSlot).findFirst().get();
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
										if (inventory.getFacilityType() == InventoryFacilityType.DOWNSTREAM || inventory.getFacilityType() == InventoryFacilityType.HUB) {
											lvl.cargoIn = lvl.changeInM3;
										}
										if (inventory.getFacilityType() == InventoryFacilityType.UPSTREAM || inventory.getFacilityType() == InventoryFacilityType.HUB) {
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
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), e.getEvent().getPeriod(), e.getChangeQuantity(), null, null, null, null, null, null,
												null, null, null);
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										if (e.getEvent() != null) {
											lvl.volumeLow = e.getEvent().getVolumeLow();
											lvl.volumeHigh = e.getEvent().getVolumeHigh();
										}

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
								series.setBarColor(colorOrange);
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
									series.setBarColor(colorOrange);
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
					1000.0 * minDate.withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000.0 * maxDate.withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		} else {
			chartViewer.getAxisSet().getXAxis(0).setRange(new Range( //
					1000.0 * LocalDate.now().withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000.0 * LocalDate.now().withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		}
		// Try to force month labels
		chartViewer.getAxisSet().getXAxis(0).getTick().setTickMarkStepHint((int) (15L));
		chartViewer.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);

		chartViewer.updateLayout();

		chartViewer.redraw();
		
		final IAxisSet inventoryDailyChartAxisSet = inventoryDailyChartViewer.getAxisSet();

		inventoryDailyChartViewer.getAxisSet().getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryDailyChartViewer.getAxisSet().getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryDailyChartViewer.getAxisSet().getXAxis(0).getTitle().setText("Date");
		inventoryDailyChartViewer.getAxisSet().getXAxis(0).getTick().setFormat(dateFormat);

		inventoryDailyChartViewer.getAxisSet().getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryDailyChartViewer.getAxisSet().getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryDailyChartViewer.getAxisSet().getYAxis(0).getTitle().setText("Volume");
		// 5. adjust the range for all axes.

		// Auto adjust everything
		inventoryDailyChartAxisSet.adjustRange();
		// Month align the date range
		if (minDate != null && maxDate != null) {
			inventoryDailyChartViewer.getAxisSet().getXAxis(0).setRange(new Range( //
					1000.0 * minDate.withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000.0 * maxDate.withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		} else {
			inventoryDailyChartViewer.getAxisSet().getXAxis(0).setRange(new Range( //
					1000.0 * LocalDate.now().withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000.0 * LocalDate.now().withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		}
		// Try to force month labels
		inventoryDailyChartViewer.getAxisSet().getXAxis(0).getTick().setTickMarkStepHint((int) (15L));
		inventoryDailyChartViewer.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);

		inventoryDailyChartViewer.updateLayout();

		inventoryDailyChartViewer.redraw();
		
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			final IAxisSet axisSet2 = chartViewer2.getAxisSet();
			
			axisSet2.getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet2.getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet2.getXAxis(0).getTitle().setText("Lifters");
			// axisSet2.getXAxis(0).getTick().setTickMarkStepHint(6);
			// axisSet2.getXAxis(0).
			
			axisSet2.getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet2.getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet2.getYAxis(0).getTitle().setText("Overlift");
			
			axisSet2.getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			
			axisSet2.adjustRange();
			
			chartViewer2.updateLayout();

			chartViewer2.redraw();
			
			final IAxisSet axisSet3 = chartViewer3.getAxisSet();
			
			axisSet3.getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet3.getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet3.getXAxis(0).getTitle().setText("Month");
			
			axisSet3.getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet3.getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			axisSet3.getYAxis(0).getTitle().setText("# Cargoes Lifted");
			
			axisSet3.getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			
			axisSet3.adjustRange();
			
			chartViewer3.updateLayout();

			chartViewer3.redraw();
		}

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

		inventoryTableViewer.setInput(tableLevels);
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			mullMonthlyTableViewer.setInput(pairedMullList);
			mullDailyTableViewer.setInput(pairedDailyMullList);
		}
	}

	private Map<LocalDate, Map<BaseLegalEntity, Integer>> calculateDailyAllocatedEntitlement(TreeMap<LocalDate, InventoryDailyEvent> insAndOuts,
			List<Pair<BaseLegalEntity, Double>> relativeEntitlements, Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap, Map<BaseLegalEntity, BaseLegalEntity> reverseEntityEntityMap) {
		Map<LocalDate, Map<BaseLegalEntity, Integer>> ret = new HashMap<>();
		for (Entry<LocalDate, InventoryDailyEvent> e : insAndOuts.entrySet()) {
			int vol = e.getValue().netVolumeIn;
			Map<BaseLegalEntity, Integer> currentAllocation = new HashMap<>();
			for (Pair<BaseLegalEntity, Double> entitlement : relativeEntitlements) {
				currentAllocation.put(entityEntityMap.get(entitlement.getFirst()), ((Double) (vol*entitlement.getSecond())).intValue());
			}
			ret.put(e.getKey(), currentAllocation);
		}
		return ret;
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
	
	private IBarSeries createCargoCountBarSeries(final ISeriesSet seriesSet, final String name, final List<Pair<Integer, Integer>> mullPairs) {
		final double[] xVals = new double[2*mullPairs.size()];
		final double[] values = new double[2*mullPairs.size()];
		int idx = 0;
		
		for (final Pair<Integer, Integer> p : mullPairs) {
			int xVal = p.getFirst();
			xVals[idx] = (double) xVal;
			values[idx] = (double) p.getSecond();
			idx++;
			
			xVals[idx] = (double) (xVal+1);
			values[idx] = 0;
			idx++;
		}
		final IBarSeries series = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, name);
		series.setXSeries(xVals);
		series.setYSeries(values);
		return series;
	}
	
	private IBarSeries createMULLBarSeries(final ISeriesSet seriesSet, final String name, final List<Pair<Integer, Integer>> mullPairs) {
		
		final double[] xVals = new double[2*mullPairs.size()];
		final double[] values = new double[2*mullPairs.size()];
		int idx = 0;
		
		for (final Pair<Integer, Integer> p : mullPairs) {
			int xVal = p.getFirst();
			xVals[idx] = (double) xVal;
			values[idx] = (double) p.getSecond();
			idx++;
			
			xVals[idx] = (double) (xVal+1);
			values[idx] = 0;
			idx++;
		}
		final IBarSeries series = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, name);
		series.setXSeries(xVals);
		series.setYSeries(values);
		return series;
	}

	private GridViewerColumn createColumn(final String title, final int width, final Function<InventoryLevel, String> labelProvider) {
		final GridViewerColumn column = new GridViewerColumn(inventoryTableViewer, SWT.NONE);
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
	
	private GridViewerColumn createMullDailyColumn(final String title, final int width, Function<Pair<MullDailyInformation, MullDailyInformation>, String> labelProvider, Function<Pair<MullDailyInformation, MullDailyInformation>, Comparable<?>> sortingFetcher) {
		final GridViewerColumn column = new GridViewerColumn(mullDailyTableViewer, SWT.NONE);
		GridViewerHelper.configureLookAndFeel(column);
		column.getColumn().setText(title);
		column.getColumn().setWidth(width);
		column.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final Pair<MullDailyInformation, MullDailyInformation> element = (Pair<MullDailyInformation, MullDailyInformation>) cell.getElement();
				final String cellText = labelProvider.apply(element);
				cell.setText(cellText);
				if (element.getFirst().grey) {
					cell.setBackground(colorLightGrey);
				}
			}
		});
		
		this.mullDailyTableFilterSupport.createColumnMnemonics(column.getColumn(), column.getColumn().getText());
		final IFilterProvider filterProvider = new IFilterProvider() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof Pair<?, ?>) {
					return labelProvider.apply((Pair<MullDailyInformation, MullDailyInformation>) object);
				}
				return null;
			}

			@Override
			public Object getFilterValue(final Object object) {
				return labelProvider.apply((Pair<MullDailyInformation,MullDailyInformation>) object);
			}
		};

		column.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);
		
		this.mullDailySortingSupport.addSortableColumn(mullDailyTableViewer, column, column.getColumn());
		final IComparableProvider sortingProvider = m -> sortingFetcher.apply((Pair<MullDailyInformation, MullDailyInformation>) m);
		
		column.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
		return column;
	}
	
	private GridViewerColumn createMullColumn(final String title, final int width, Function<Pair<MullInformation,MullInformation>, String> labelProvider, Function<Pair<MullInformation, MullInformation>, Comparable<?>> sortingFetcher) {
		final GridViewerColumn column = new GridViewerColumn(mullMonthlyTableViewer, SWT.NONE);
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
					cell.setBackground(colorLightGrey);
				}
				if (!title.equals("Month") && !title.equals("Entity") && element.getSecond() != null && cellText.length() > 0 && !cellText.equals("0")) {
					cell.setForeground(colourRed);
				}
			}
		});

		this.mullMonthlyTableFilterSupport.createColumnMnemonics(column.getColumn(), column.getColumn().getText());
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
		
		this.mullMonthlyTableSortingSupport.addSortableColumn(mullMonthlyTableViewer, column, column.getColumn());
		final IComparableProvider sortingProvider = m -> sortingFetcher.apply((Pair<MullInformation, MullInformation>) m);
		
		column.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
		return column;
	}

	private void snapTo(LocalDate date) {
		if (inventoryTableViewer == null) {
			return;
		}
		final Grid grid = inventoryTableViewer.getGrid();
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
	
	private boolean isValidMullSubProfile(final MullSubprofile subProfile) {
		return subProfile.getEntityTable().stream().allMatch(
				row -> {
					if (row.getEntity() == null)
						return false;
					if (row.getInitialAllocation() == null || !row.getInitialAllocation().matches("-?\\d+"))
						return false;
					if (row.getRelativeEntitlement() <= 0.0)
						return false;
					return true;
				}
		);
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
	
	private Map<YearMonth, Map<BaseLegalEntity, Integer>> calculateMonthlyRE(final YearMonth adpStart, final YearMonth adpEnd, final MullSubprofile sProfile, final Map<YearMonth, Integer> monthlyProduction) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> monthlyRE = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd); ym = ym.plusMonths(1)) {
			Integer totalProdTemp = monthlyProduction.get(ym);
			if (totalProdTemp == null) {
				totalProdTemp = 0;
			}
			final int thisTotalProd = totalProdTemp;
			final Map<BaseLegalEntity, Integer> currMap = new HashMap<>();
			monthlyRE.put(ym, currMap);
			final YearMonth ymm = ym;
			sProfile.getEntityTable().stream().forEach(row -> {
				if (ymm.equals(adpStart)) {
					currMap.put(row.getEntity(),  Integer.parseInt(row.getInitialAllocation()) + (int) (row.getRelativeEntitlement()*thisTotalProd));
				} else {
					currMap.put(row.getEntity(), (int) (row.getRelativeEntitlement()*thisTotalProd));
				}
			});
		}
		return monthlyRE;
	}
	
	private Map<YearMonth, Map<BaseLegalEntity,Integer>> calculateActualLift(final YearMonth adpStart, final YearMonth adpEnd, final MullSubprofile sProfile, final List<CargoAllocation> cargoAllocations, final Port inventoryLoadPort) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> actualLift = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd.plusMonths(1)); ym = ym.plusMonths(1)) {
			final Map<BaseLegalEntity, Integer> currMap = new HashMap<>();
			sProfile.getEntityTable().stream().map(MullEntityRow::getEntity).forEach(entity -> currMap.put(entity, 0));
			actualLift.put(ym, currMap);
		}
		cargoAllocations.stream() //
				.map(cargoAlloc -> cargoAlloc.getSlotAllocations().get(0))
				.filter(loadSlotAlloc -> loadSlotAlloc.getPort().equals(inventoryLoadPort))
				.filter(loadSlotAlloc -> YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate()).isBefore(adpEnd.plusMonths(1)))
				.forEach(loadSlotAlloc -> {
					final YearMonth ym = YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate());
					final BaseLegalEntity thisEntity = loadSlotAlloc.getSlot().getEntity();
					final Map<BaseLegalEntity, Integer> ymLifts = actualLift.get(ym);
					if (ymLifts != null) {
						ymLifts.computeIfPresent(thisEntity, (k, v) -> v + loadSlotAlloc.getPhysicalVolumeTransferred());
					}
				});
		return actualLift;
	}
	
	private Map<YearMonth, Map<BaseLegalEntity,Integer>> calculateCargoCount(final YearMonth adpStart, final YearMonth adpEnd, final MullSubprofile sProfile, final List<CargoAllocation> cargoAllocations, final Port inventoryLoadPort) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> cargoCount = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd.plusMonths(1)); ym = ym.plusMonths(1)) {
			final Map<BaseLegalEntity, Integer> cargoCountMap = new HashMap<>();
			sProfile.getEntityTable().stream().map(MullEntityRow::getEntity).forEach(entity -> cargoCountMap.put(entity, 0));
			cargoCount.put(ym, cargoCountMap);
		}
		cargoAllocations.stream() //
		.map(cargoAlloc -> cargoAlloc.getSlotAllocations().get(0))
		.filter(loadSlotAlloc -> loadSlotAlloc.getPort().equals(inventoryLoadPort))
		.filter(loadSlotAlloc -> YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate()).isBefore(adpEnd.plusMonths(1)))
		.forEach(loadSlotAlloc -> {
			final YearMonth ym = YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate());
			final BaseLegalEntity thisEntity = loadSlotAlloc.getSlot().getEntity();
			final Map<BaseLegalEntity, Integer> ymLifts = cargoCount.get(ym);
			if (ymLifts != null) {
				ymLifts.computeIfPresent(thisEntity, (k, v) -> v + 1);
			}
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
	
	private List<BaseLegalEntity> calculateEntityOrder(final MullSubprofile sProfile) {
		final Map<BaseLegalEntity,Double> orderVal = new HashMap<>();
		sProfile.getEntityTable().stream().forEach(row -> orderVal.put(row.getEntity(), row.getRelativeEntitlement()));
		return sProfile.getEntityTable().stream() //
				.map(MullEntityRow::getEntity) //
				.sorted((a, b) -> orderVal.get(b).compareTo(orderVal.get(a))) //
				.collect(Collectors.toList());
	}
	
	private String generateDailyDiffString(final Pair<MullDailyInformation, MullDailyInformation> element, final ToIntFunction<MullDailyInformation> valueExtractor) {
		final MullDailyInformation pinned = element.getFirst();
		final MullDailyInformation other = element.getSecond();
		final int pinnedVal = valueExtractor.applyAsInt(pinned);
		if (other != null) {
			final int otherVal = valueExtractor.applyAsInt(other);
			if (pinnedVal == otherVal) {
				return "0";
			} else {
				int difference = otherVal - pinnedVal;
				return String.format("%,d", difference);
			}
		} else {
			return String.format("%,d", pinnedVal);
		}
	}
	
	private String generateDiffString(final Pair<MullInformation, MullInformation> element, final ToIntFunction<MullInformation> valueExtractor) {
		final MullInformation pinned = element.getFirst();
		final MullInformation other = element.getSecond();
		final int pinnedVal = valueExtractor.applyAsInt(pinned);
		if (other != null) {
			final int otherVal = valueExtractor.applyAsInt(other);
			if (pinnedVal == otherVal) {
				return "0";
			} else {
				int difference = otherVal - pinnedVal;
				return String.format("%,d", difference);
			}
		} else {
			return String.format("%,d", pinnedVal);
		}
	}
	
	private Map<BaseLegalEntity, Integer> calculateInitialAllocation(MullSubprofile sProfile) {
		Map<BaseLegalEntity, Integer> initialAllocations = new HashMap<>();
		sProfile.getEntityTable().stream().forEach(row -> initialAllocations.put(row.getEntity(), Integer.parseInt(row.getInitialAllocation())));
		return initialAllocations;
	}
	
	private Comparable generateSortValue(final Pair<MullInformation, MullInformation> element, final ToIntFunction<MullInformation> valueExtractor) {
		final MullInformation otherVal = element.getSecond();
		if (element.getSecond() == null) {
			return valueExtractor.applyAsInt(element.getFirst());
		} else {
			return valueExtractor.applyAsInt(otherVal) - valueExtractor.applyAsInt(element.getFirst());
		}
	}
	
	private Comparable generateDailySortValue(final Pair<MullDailyInformation, MullDailyInformation> element, final ToIntFunction<MullDailyInformation> valueExtractor) {
		final MullDailyInformation otherVal = element.getSecond();
		if (otherVal == null) {
			return valueExtractor.applyAsInt(element.getFirst());
		} else {
			return valueExtractor.applyAsInt(otherVal) - valueExtractor.applyAsInt(element.getFirst());
		}
	}
	
	private void calculateOverliftCF(final List<MullInformation> mullList, final YearMonth adpStart) {
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
	
	private TreeMap<LocalDate, InventoryDailyEvent> getInventoryInsAndOuts(Inventory inventory, LNGScenarioModel scenarioModel) {
		List<InventoryCapacityRow> capacities = inventory.getCapacities();
		
		TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap = 
				capacities.stream()
				.collect(Collectors.toMap(InventoryCapacityRow::getDate,
							c -> c,
							(oldValue, newValue) -> newValue,
							TreeMap::new));
		
		TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = new TreeMap<>();
		
		// add all feeds to map
		addNetVolumes(inventory.getFeeds(), capcityTreeMap, insAndOuts, IntUnaryOperator.identity());
		addNetVolumes(inventory.getOfftakes(), capcityTreeMap, insAndOuts, a -> -a);
		return insAndOuts;
	}
	
	private void addNetVolumes(List<InventoryEventRow> events, TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, IntUnaryOperator volumeFunction) {
		for (InventoryEventRow inventoryEventRow : events) {
			if (inventoryEventRow.getStartDate() != null) {
				InventoryDailyEvent inventoryDailyEvent = insAndOuts.get(inventoryEventRow.getStartDate());
				if (inventoryDailyEvent == null) {
					inventoryDailyEvent = new InventoryDailyEvent();
					inventoryDailyEvent.date = inventoryEventRow.getStartDate();
					
//					InventoryCapacityRow capacityRow = capcityTreeMap.get(inventoryDailyEvent.date) == null //
//							? capcityTreeMap.lowerEntry(inventoryDailyEvent.date).getValue() //
//							: capcityTreeMap.get(inventoryDailyEvent.date);
//					inventoryDailyEvent.minVolume = capacityRow.getMinVolume();
//					inventoryDailyEvent.maxVolume = capacityRow.getMaxVolume();
					insAndOuts.put(inventoryEventRow.getStartDate(), inventoryDailyEvent);
				}
				inventoryDailyEvent.addVolume(volumeFunction.applyAsInt(inventoryEventRow.getReliableVolume()));
			}
		}
	}
	
	private class InventoryDailyEvent {
		LocalDate date;
		int netVolumeIn = 0;
		int minVolume = 0;
		int maxVolume = 0;
		
		public InventoryDailyEvent() {
		}
		
		public InventoryDailyEvent(LocalDate date, int netVolumeIn, int minVolume, int maxVolume) {
			this.date = date;
			this.netVolumeIn = netVolumeIn;
			this.minVolume = minVolume;
			this.maxVolume = maxVolume;
		}
		
		public void addVolume(int volume) {
			netVolumeIn += volume;
		}
	}
	
	private void fillMullInformationList(final YearMonth adpStart, final YearMonth adpEnd, final List<BaseLegalEntity> entitiesOrdered, final MullSubprofile sProfile, final Map<YearMonth, Integer> monthlyProduction, final Port inventoryLoadPort, final List<CargoAllocation> cargoAllocations, final List<MullInformation> list, final LNGScenarioModel lngScenarioModel) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> monthlyRE = calculateMonthlyRE(adpStart, adpEnd, sProfile, monthlyProduction);
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> actualLift = calculateActualLift(adpStart, adpEnd, sProfile, cargoAllocations, inventoryLoadPort);
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> cargoCount = calculateCargoCount(adpStart, adpEnd, sProfile, cargoAllocations, inventoryLoadPort);
		final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(sProfile);
		
		final List<BaseLegalEntity> localEntities = lngScenarioModel.getReferenceModel().getCommercialModel().getEntities();
		final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = new HashMap<>();
		for (final BaseLegalEntity entityToKeep : entitiesOrdered) {
			localEntities.stream().filter(e -> entityToKeep.getName().equals(e.getName())).findAny().ifPresent(e -> entityEntityMap.put(entityToKeep, e));
		}
		
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd); ym = ym.plusMonths(1)) {
			for (final BaseLegalEntity entity : entitiesOrdered) {
				final BaseLegalEntity entityMirrorObject = entityEntityMap.get(entity);
				final MullInformation currRow = new MullInformation();
				currRow.entity = entity;
				currRow.ym = ym;
				currRow.lifted = actualLift.get(ym).get(entityMirrorObject);
				currRow.overlift = actualLift.get(ym).get(entityMirrorObject) - monthlyRE.get(ym).get(entityMirrorObject);
				currRow.monthlyRE = monthlyRE.get(ym).get(entityMirrorObject);
				currRow.cargoCount = cargoCount.get(ym).get(entityMirrorObject);
				list.add(currRow);
			}
		}
		calculateMonthEndEntitlement(list, adpStart);
		calculateOverliftCF(list, adpStart);
		final Map<BaseLegalEntity, Integer> mappedInitialAllocation = new HashMap<>();
		entitiesOrdered.stream().forEach(e -> mappedInitialAllocation.put(e, initialAllocation.get(entityEntityMap.get(e))));
		calculateCarriedEntitlement(list, adpStart, mappedInitialAllocation);
	}
	
	private Map<LocalDate, Map<BaseLegalEntity, Integer>> calculateDailyActualEntitlement(List<BaseLegalEntity> entities, List<CargoAllocation> sortedCargoAllocations, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, Map<LocalDate, Map<BaseLegalEntity, Integer>> allocatedEntitlement, final Map<BaseLegalEntity, Integer> initialAllocations) {
		Map<LocalDate, Map<BaseLegalEntity, Integer>> ret = new HashMap<>();
		Iterator<CargoAllocation> iterCargo = sortedCargoAllocations.iterator();
		CargoAllocation currentCargoAlloc = null;
		if (iterCargo.hasNext()) {
			currentCargoAlloc = iterCargo.next();
		}
		boolean first = true;
		Map<BaseLegalEntity, Integer> prevMap = null;
		for (Entry<LocalDate, InventoryDailyEvent> e : insAndOuts.entrySet()) {
			Map<BaseLegalEntity, Integer> currMap = new HashMap<>();
			entities.stream().forEach(ee -> currMap.put(ee, 0));
			ret.put(e.getKey(), currMap);
			if (first) {
				Map<BaseLegalEntity, Integer> currAlloc = allocatedEntitlement.get(e.getKey());
				for (final BaseLegalEntity ee : entities) {
					currMap.put(ee, currAlloc.get(ee) + initialAllocations.get(ee));
				}
				while (currentCargoAlloc != null && currentCargoAlloc.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate().equals(e.getKey())) {
					BaseLegalEntity currEnt = currentCargoAlloc.getSlotAllocations().get(0).getSlot().getEntity();
					int transferred = currentCargoAlloc.getSlotAllocations().get(0).getPhysicalVolumeTransferred();
					currMap.computeIfPresent(currEnt, (k,v) -> v - transferred);
					if (iterCargo.hasNext()) {
						currentCargoAlloc = iterCargo.next();
					} else {
						currentCargoAlloc = null;
					}
				}
				first = false;
				prevMap = currMap;
				continue;
			}
			Map<BaseLegalEntity, Integer> currAlloc = allocatedEntitlement.get(e.getKey());
			for (BaseLegalEntity ee : entities) {
				currMap.put(ee, currAlloc.get(ee) + prevMap.get(ee));
			}
			while (currentCargoAlloc != null && currentCargoAlloc.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate().equals(e.getKey())) {
				BaseLegalEntity currEnt = currentCargoAlloc.getSlotAllocations().get(0).getSlot().getEntity();
				int transferred = currentCargoAlloc.getSlotAllocations().get(0).getPhysicalVolumeTransferred();
				currMap.computeIfPresent(currEnt, (k,v) -> v - transferred);
				if (iterCargo.hasNext()) {
					currentCargoAlloc = iterCargo.next();
				} else {
					currentCargoAlloc = null;
				}
			}
			prevMap = currMap;
		}
		return ret;
	}
	
	private Map<BaseLegalEntity, BaseLegalEntity> buildEntityEntityMap(final LNGScenarioModel lngScenarioModel, final List<BaseLegalEntity> entitiesOrdered) {
		final List<BaseLegalEntity> localEntities = lngScenarioModel.getReferenceModel().getCommercialModel().getEntities();
		final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = new HashMap<>();
		for (final BaseLegalEntity entityToKeep : entitiesOrdered) {
			localEntities.stream().filter(e -> entityToKeep.getName().equals(e.getName())).findAny().ifPresent(e -> entityEntityMap.put(entityToKeep, e));
		}
		return entityEntityMap;
	}
	
}
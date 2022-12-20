/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
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
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.model.DateArraySeriesModel;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.event.EventHandler;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.views.standard.inventory.ChartColourSchemeAction;
import com.mmxlabs.lingo.reports.views.standard.inventory.ColourSequence;
import com.mmxlabs.lingo.reports.views.standard.inventory.InventoryLevel;
import com.mmxlabs.lingo.reports.views.standard.inventory.MullDailyInformation;
import com.mmxlabs.lingo.reports.views.standard.inventory.MullInformation;
import com.mmxlabs.lingo.reports.views.standard.inventory.OverliftChartModeAction;
import com.mmxlabs.lingo.reports.views.standard.inventory.OverliftChartState;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.CargoSlotSorter;
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
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.scenario.service.ScenarioResult;

public class InventoryReport extends ViewPart {

	public enum OverliftChartMode {
		CUMULATIVE, NON_CUMULATIVE
	}

	public static final String ID = "com.mmxlabs.lingo.reports.views.standard.InventoryReport";

	private static final String MULL_YEAR_MONTH_FORMAT = "MMM yy";

	private Chart inventoryInsAndOutChart;
	private Chart inventoryDailyChartViewer;
	private Chart mullMonthlyOverliftChart;
	private Chart mullMonthlyCargoCountChart;

	private GridTableViewer inventoryTableViewer;

	private GridTableViewer mullMonthlyTableViewer;
	private GridTableViewer mullMonthlyCumulativeTableViewer;
	private GridTableViewer mullDailyTableViewer;

	private FilterField mullMonthlyTableFilterField;
	private EObjectTableViewerFilterSupport mullMonthlyTableFilterSupport;

	private FilterField mullMonthlyCumulativeFilterField;
	private EObjectTableViewerFilterSupport mullMonthlyCumulativeTableFilterSupport;

	private FilterField mullDailyTableFilterField;
	private EObjectTableViewerFilterSupport mullDailyTableFilterSupport;

	private EObjectTableViewerSortingSupport mullMonthlyTableSortingSupport;
	private EObjectTableViewerSortingSupport mullMonthlyCumulativeTableSortingSupport;
	private EObjectTableViewerSortingSupport mullDailySortingSupport;

	private int previousTabSelection;

	private ActionContributionItem inventoryTableCopyActionContributionItem;
	private ActionContributionItem inventoryTablePackActionContributionItem;
	private ActionContributionItem mullMonthlyTableCopyActionContributionItem;
	private ActionContributionItem mullMonthlyTablePackActionContributionItem;
	private ActionContributionItem mullMonthlyCumulativeTableCopyActionContributionItem;
	private ActionContributionItem mullMonthlyCumulativeTablePackActionContributionItem;
	private ActionContributionItem mullDailyTableCopyActionContributionItem;
	private ActionContributionItem mullDailyTablePackActionContributionItem;

	private ChartColourSchemeAction colourSchemeAction;
	private ActionContributionItem colourSchemeActionContributionItem;

	private OverliftChartModeAction overliftChartModeAction;
	private ActionContributionItem overliftChartModeActionContributionItem;

	private Inventory selectedInventory;
	private ScenarioResult currentResult;

	private ScenarioResult pinnedResult;
	private ScenarioResult otherResult;

	private LocalResourceManager localResourceManager;

	private Color[] mullChartColourSequence;
	private Color colourOrange;
	private Color colourLightGrey;
	private Color colourRed;

	private EventHandler todayHandler;

	private IBarSeries cargoSeries;
	private IBarSeries openSeries;
	private IBarSeries thirdPartyCargoSeries;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		public void selectedDataProviderChanged(ISelectedDataProvider selectedDataProvider, boolean block) {
			final Runnable r = () -> {

				selectedInventory = null;

				currentResult = selectedDataProvider.getPinnedScenarioResult();
				pinnedResult = currentResult;
				otherResult = selectedDataProvider.getOtherScenarioResults().isEmpty() ? null : selectedDataProvider.getOtherScenarioResults().iterator().next();
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
			ViewerHelper.runIfViewerValid(comboViewer, block, r);
		}
	};

	private ScenarioComparisonService selectedScenariosService;

	private ComboViewer comboViewer;

	private String lastFacility = null;

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		this.localResourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		this.colourOrange = this.localResourceManager.createColor(new RGB(255, 200, 15));
		this.colourLightGrey = this.localResourceManager.createColor(new RGB(230, 230, 230));
		this.colourRed = this.localResourceManager.createColor(new RGB(255, 36, 0));

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			mullMonthlyTableFilterField = new FilterField(parent);
			mullMonthlyCumulativeFilterField = new FilterField(parent);
			mullDailyTableFilterField = new FilterField(parent);

			this.mullChartColourSequence = ColourSequence.createColourSequence(localResourceManager);
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
		inventoryDailyChartItem.setText("Daily Production");

		final CTabItem tableItem = new CTabItem(folder, SWT.NONE);
		tableItem.setText("Table");
		{
			inventoryInsAndOutChart = new Chart(folder, SWT.NONE);
			inventoryInsAndOutChart.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			inventoryInsAndOutChart.getTitle().setVisible(false);
			chartItem.setControl(inventoryInsAndOutChart);
		}
		{
			inventoryDailyChartViewer = new Chart(folder, SWT.None);
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
				createColumn("Total Feed Out", 150, o -> String.format("%,d", o.feedOut));
				createColumn("Total Cargo Out", 150, o -> String.format("%,d", o.cargoOut));
				createColumn("Change", 150, o -> String.format("%,d", o.changeInM3));
				createColumn("Level", 150, o -> String.format("%,d", o.runningTotal));
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

		colourSchemeAction = new ChartColourSchemeAction(this);
		colourSchemeActionContributionItem = new ActionContributionItem(colourSchemeAction);
		getViewSite().getActionBars().getToolBarManager().add(colourSchemeActionContributionItem);
		colourSchemeActionContributionItem.setVisible(folderSelection == 0);

		final PackGridTableColumnsAction inventoryTablePackAction = PackActionFactory.createPackColumnsAction(inventoryTableViewer);
		inventoryTablePackActionContributionItem = new ActionContributionItem(inventoryTablePackAction);
		getViewSite().getActionBars().getToolBarManager().add(inventoryTablePackActionContributionItem);
		inventoryTablePackActionContributionItem.setVisible(folderSelection == 2);

		final CopyGridToClipboardAction inventoryTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(inventoryTableViewer);
		inventoryTableCopyActionContributionItem = new ActionContributionItem(inventoryTableCopyAction);
		getViewSite().getActionBars().getToolBarManager().add(inventoryTableCopyActionContributionItem);
		inventoryTableCopyActionContributionItem.setVisible(folderSelection == 2);

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			final CTabItem mullItem = new CTabItem(folder, SWT.NONE);
			ServiceHelper.withOptionalServiceConsumer(IOverliftReportCustomiser.class, customiser -> {
				final String labelText = customiser != null ? customiser.getMonthlyOverliftLabel() : "Overlift Monthly";
				mullItem.setText(labelText);
			});
			{
				mullMonthlyTableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);

				this.mullMonthlyTableFilterSupport = new EObjectTableViewerFilterSupport(mullMonthlyTableViewer, mullMonthlyTableViewer.getGrid());
				this.mullMonthlyTableSortingSupport = new EObjectTableViewerSortingSupport();
				mullMonthlyTableViewer.setComparator(mullMonthlyTableSortingSupport.createViewerComparer());

				mullMonthlyTableViewer.addFilter(mullMonthlyTableFilterSupport.createViewerFilter());
				mullMonthlyTableFilterField.setFilterSupport(mullMonthlyTableFilterSupport);

				getViewSite().getActionBars().getToolBarManager().add(mullMonthlyTableFilterField.getContribution());
				mullMonthlyTableFilterField.getContribution().setVisible(folderSelection == 3);

				mullMonthlyTableViewer.setContentProvider(new ArrayContentProvider());
				mullMonthlyTableViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

				GridViewerHelper.configureLookAndFeel(mullMonthlyTableViewer);
				mullMonthlyTableViewer.getGrid().setTreeLinesVisible(true);
				mullMonthlyTableViewer.getGrid().setHeaderVisible(true);

				{
					createMullColumn("Month", 150, o -> o.getFirst().ym.format(DateTimeFormatter.ofPattern(MULL_YEAR_MONTH_FORMAT)), o -> o.getFirst().getYM());
					createMullColumn("Entity", 150, o -> o.getFirst().entity.getName(), o -> o.getFirst().entity.getName());
					createMullColumn("Monthly Entitlement", 150, o -> generateDiffString(o, MullInformation::getMonthlyRE), o -> generateSortValue(o, MullInformation::getMonthlyRE));
					createMullColumn("Monthly lifted", 150, o -> generateDiffString(o, MullInformation::getLifted), o -> generateSortValue(o, MullInformation::getLifted));
					createMullColumn("Delta", 150, o -> generateDiffString(o, m -> -1 * m.getDelta()), o -> generateSortValue(o, m -> -1 * m.getDelta()));
					createMullColumn("Cumulative Delta", 150, o -> generateDiffString(o, m -> -1 * m.getCumulativeDelta()), o -> generateSortValue(o, m -> -1 * m.getCumulativeDelta()));
					createMullColumn("# Cargoes", 150, o -> generateDiffString(o, MullInformation::getCargoCount), o -> generateSortValue(o, MullInformation::getCargoCount));

					mullItem.setControl(mullMonthlyTableViewer.getControl());
				}
			}

			final PackGridTableColumnsAction mullMonthlyTablePackAction = PackActionFactory.createPackColumnsAction(mullMonthlyTableViewer);
			mullMonthlyTablePackActionContributionItem = new ActionContributionItem(mullMonthlyTablePackAction);
			getViewSite().getActionBars().getToolBarManager().add(mullMonthlyTablePackActionContributionItem);
			mullMonthlyTablePackActionContributionItem.setVisible(folder.getSelectionIndex() == 3);

			final CopyGridToClipboardAction mullMonthlyTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(mullMonthlyTableViewer);
			mullMonthlyTableCopyActionContributionItem = new ActionContributionItem(mullMonthlyTableCopyAction);
			getViewSite().getActionBars().getToolBarManager().add(mullMonthlyTableCopyActionContributionItem);
			mullMonthlyTableCopyActionContributionItem.setVisible(folder.getSelectionIndex() == 3);

			final CTabItem mullMonthlyCumulativeItem = new CTabItem(folder, SWT.NONE);
			ServiceHelper.withOptionalServiceConsumer(IOverliftReportCustomiser.class, customiser -> {
				final String labelText = customiser != null ? customiser.getMonthlyCumulativeOverliftLabel() : "Overlift Monthly Cumulative";
				mullMonthlyCumulativeItem.setText(labelText);
			});
			{
				mullMonthlyCumulativeTableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);

				this.mullMonthlyCumulativeTableFilterSupport = new EObjectTableViewerFilterSupport(mullMonthlyCumulativeTableViewer, mullMonthlyCumulativeTableViewer.getGrid());
				this.mullMonthlyCumulativeTableSortingSupport = new EObjectTableViewerSortingSupport();
				mullMonthlyCumulativeTableViewer.setComparator(mullMonthlyCumulativeTableSortingSupport.createViewerComparer());

				mullMonthlyCumulativeTableViewer.addFilter(mullMonthlyCumulativeTableFilterSupport.createViewerFilter());
				mullMonthlyCumulativeFilterField.setFilterSupport(mullMonthlyCumulativeTableFilterSupport);

				getViewSite().getActionBars().getToolBarManager().add(mullMonthlyCumulativeFilterField.getContribution());
				mullMonthlyCumulativeFilterField.getContribution().setVisible(folderSelection == 4);

				mullMonthlyCumulativeTableViewer.setContentProvider(new ArrayContentProvider());
				mullMonthlyCumulativeTableViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

				GridViewerHelper.configureLookAndFeel(mullMonthlyCumulativeTableViewer);
				mullMonthlyCumulativeTableViewer.getGrid().setTreeLinesVisible(true);
				mullMonthlyCumulativeTableViewer.getGrid().setHeaderVisible(true);

				{
					createMullCumulativeColumn("Period", 150, o -> {
						final YearMonth adpStart = o.getFirst().adpStart;
						final YearMonth ym = o.getFirst().ym;
						if (ym.equals(adpStart)) {
							return ym.format(DateTimeFormatter.ofPattern(MULL_YEAR_MONTH_FORMAT));
						} else {
							return String.format("%s - %s", adpStart.format(DateTimeFormatter.ofPattern(MULL_YEAR_MONTH_FORMAT)), ym.format(DateTimeFormatter.ofPattern(MULL_YEAR_MONTH_FORMAT)));
						}
					}, o -> o.getFirst().getYM());
					createMullCumulativeColumn("Entity", 150, o -> o.getFirst().entity.getName(), o -> o.getFirst().entity.getName());
					createMullCumulativeColumn("Reference Entitlement", 150, o -> generateDiffString(o, MullInformation::getMonthlyRE), o -> generateSortValue(o, MullInformation::getMonthlyRE));
					createMullCumulativeColumn("Lifted", 150, o -> generateDiffString(o, MullInformation::getLifted), o -> generateSortValue(o, MullInformation::getLifted));
					createMullCumulativeColumn("Delta", 150, o -> generateDiffString(o, m -> -1 * m.getCumulativeDelta()), o -> generateSortValue(o, m -> -1 * m.getCumulativeDelta()));
					createMullCumulativeColumn("# Cargoes", 150, o -> generateDiffString(o, MullInformation::getCargoCount), o -> generateSortValue(o, MullInformation::getCargoCount));

					mullMonthlyCumulativeItem.setControl(mullMonthlyCumulativeTableViewer.getControl());
				}
			}

			final PackGridTableColumnsAction mullMonthlyCumulativeTablePackAction = PackActionFactory.createPackColumnsAction(mullMonthlyCumulativeTableViewer);
			mullMonthlyCumulativeTablePackActionContributionItem = new ActionContributionItem(mullMonthlyCumulativeTablePackAction);
			getViewSite().getActionBars().getToolBarManager().add(mullMonthlyCumulativeTablePackActionContributionItem);
			mullMonthlyCumulativeTablePackActionContributionItem.setVisible(folder.getSelectionIndex() == 4);

			final CopyGridToClipboardAction mullMonthlyCumulativeTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(mullMonthlyCumulativeTableViewer);
			mullMonthlyCumulativeTableCopyActionContributionItem = new ActionContributionItem(mullMonthlyCumulativeTableCopyAction);
			getViewSite().getActionBars().getToolBarManager().add(mullMonthlyCumulativeTableCopyActionContributionItem);
			mullMonthlyCumulativeTableCopyActionContributionItem.setVisible(folder.getSelectionIndex() == 4);

			final CTabItem mullDailyItem = new CTabItem(folder, SWT.NONE);

			ServiceHelper.withCheckedOptionalServiceConsumer(IOverliftReportCustomiser.class, customiser -> {
				final String labelText = customiser != null ? customiser.getDailyOverliftTableLabel() : "Overlift Daily";
				mullDailyItem.setText(labelText);
			});
			{
				mullDailyTableViewer = new GridTableViewer(folder, SWT.V_SCROLL | SWT.H_SCROLL);

				this.mullDailyTableFilterSupport = new EObjectTableViewerFilterSupport(mullDailyTableViewer, mullDailyTableViewer.getGrid());
				this.mullDailySortingSupport = new EObjectTableViewerSortingSupport();
				mullDailyTableViewer.setComparator(mullDailySortingSupport.createViewerComparer());

				mullDailyTableViewer.addFilter(mullDailyTableFilterSupport.createViewerFilter());
				mullDailyTableFilterField.setFilterSupport(mullDailyTableFilterSupport);

				getViewSite().getActionBars().getToolBarManager().add(mullDailyTableFilterField.getContribution());
				mullDailyTableFilterField.getContribution().setVisible(folderSelection == 5);

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
					createMullDailyColumn("Production Allocation", 150, o -> generateDailyDiffString(o, MullDailyInformation::getAllocatedEntitlement),
							o -> generateDailySortValue(o, MullDailyInformation::getAllocatedEntitlement));
					createMullDailyColumn("Running Overlift", 150, o -> generateDailyDiffString(o, m -> -1 * m.getActualEntitlement()),
							o -> generateDailySortValue(o, MullDailyInformation::getActualEntitlement));
					createMullDailyColumn("Lifting", 150, o -> o.getFirst().lifting ? "Y" : "", o -> o.getFirst().lifting);
					mullDailyItem.setControl(mullDailyTableViewer.getControl());
				}
			}

			final PackGridTableColumnsAction mullDailyTablePackAction = PackActionFactory.createPackColumnsAction(mullDailyTableViewer);
			mullDailyTablePackActionContributionItem = new ActionContributionItem(mullDailyTablePackAction);
			getViewSite().getActionBars().getToolBarManager().add(mullDailyTablePackActionContributionItem);
			mullDailyTablePackActionContributionItem.setVisible(folder.getSelectionIndex() == 5);

			final CopyGridToClipboardAction mullDailyTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(mullDailyTableViewer);
			mullDailyTableCopyActionContributionItem = new ActionContributionItem(mullDailyTableCopyAction);
			getViewSite().getActionBars().getToolBarManager().add(mullDailyTableCopyActionContributionItem);
			mullDailyTableCopyActionContributionItem.setVisible(folder.getSelectionIndex() == 5);

			final CTabItem chartItem2 = new CTabItem(folder, SWT.NONE);
			ServiceHelper.withOptionalServiceConsumer(IOverliftReportCustomiser.class, customiser -> {
				final String labelText = customiser != null ? customiser.getOverliftChartLabel() : "Overlift Chart";
				chartItem2.setText(labelText);
			});
			{
				mullMonthlyOverliftChart = new Chart(folder, SWT.NONE);
				mullMonthlyOverliftChart.setLayoutData(GridDataFactory.fillDefaults().grab(true, true));
				mullMonthlyOverliftChart.getTitle().setVisible(false);
				chartItem2.setControl(mullMonthlyOverliftChart);
			}
			overliftChartModeAction = new OverliftChartModeAction(mullMonthlyOverliftChart);
			overliftChartModeActionContributionItem = new ActionContributionItem(overliftChartModeAction);
			getViewSite().getActionBars().getToolBarManager().add(overliftChartModeActionContributionItem);
			overliftChartModeActionContributionItem.setVisible(folder.getSelectionIndex() == 6);

			final CTabItem chartItem3 = new CTabItem(folder, SWT.NONE);
			chartItem3.setText("# Cargo Chart");
			{
				mullMonthlyCargoCountChart = new Chart(folder, SWT.NONE);
				mullMonthlyCargoCountChart.setLayoutData(GridDataFactory.fillDefaults().grab(true, true));
				mullMonthlyCargoCountChart.getTitle().setVisible(false);
				chartItem3.setControl(mullMonthlyCargoCountChart);
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
				colourSchemeActionContributionItem.setVisible(currentTabSelection == 0);
				inventoryTableCopyActionContributionItem.setVisible(currentTabSelection == 2);
				inventoryTablePackActionContributionItem.setVisible(currentTabSelection == 2);
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
					mullMonthlyTableCopyActionContributionItem.setVisible(currentTabSelection == 3);
					mullMonthlyTablePackActionContributionItem.setVisible(currentTabSelection == 3);
					if (mullMonthlyTableFilterField.getContribution().getAction().isChecked()) {
						mullMonthlyTableFilterField.toggleVisibility();
					}
					mullMonthlyTableFilterField.getContribution().setVisible(currentTabSelection == 3);

					mullMonthlyCumulativeTableCopyActionContributionItem.setVisible(currentTabSelection == 4);
					mullMonthlyCumulativeTablePackActionContributionItem.setVisible(currentTabSelection == 4);
					if (mullMonthlyCumulativeFilterField.getContribution().getAction().isChecked()) {
						mullMonthlyCumulativeFilterField.toggleVisibility();
					}
					mullMonthlyCumulativeFilterField.getContribution().setVisible(currentTabSelection == 4);

					mullDailyTableCopyActionContributionItem.setVisible(currentTabSelection == 5);
					mullDailyTablePackActionContributionItem.setVisible(currentTabSelection == 5);
					if (mullDailyTableFilterField.getContribution().getAction().isChecked()) {
						mullDailyTableFilterField.toggleVisibility();
					}
					mullDailyTableFilterField.getContribution().setVisible(currentTabSelection == 5);

					overliftChartModeActionContributionItem.setVisible(currentTabSelection == 6);
				}
				getViewSite().getActionBars().getToolBarManager().update(true);
			}

		});
		getViewSite().getActionBars().getToolBarManager().update(true);

		folder.addListener(SWT.Dispose, e -> {
			inventoryInsAndOutChart.dispose();
			inventoryDailyChartViewer.dispose();
			mullMonthlyOverliftChart.dispose();
			mullMonthlyCargoCountChart.dispose();
		});
	}

	public void setCargoVisibilityInInventoryChart(final boolean isVisible) {
		if (cargoSeries != null) {
			cargoSeries.setVisible(isVisible);
			cargoSeries.setVisibleInLegend(isVisible);
			thirdPartyCargoSeries.setVisible(isVisible);
			thirdPartyCargoSeries.setVisibleInLegend(isVisible);
			inventoryInsAndOutChart.updateLayout();
			inventoryInsAndOutChart.redraw();
		}
	}

	public void setOpenSlotVisibilityInInventoryChart(final boolean isVisible) {
		if (openSeries != null) {
			openSeries.setVisible(isVisible);
			openSeries.setVisibleInLegend(isVisible);
			inventoryInsAndOutChart.updateLayout();
			inventoryInsAndOutChart.redraw();
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(inventoryInsAndOutChart);
	}

	@Override
	public void dispose() {
		localResourceManager.dispose();
		colourSchemeAction.dispose();
		// Colours are added to localResourceManager so they don't need an explicit
		// dispose
		selectedScenariosService.removeListener(selectedScenariosServiceListener);
		if (this.todayHandler != null) {
			IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
			eventBroker.unsubscribe(this.todayHandler);
		}
		super.dispose();
	}

	private void updatePlots(final Collection<Inventory> inventoryModels, final ScenarioResult toDisplay) {

		final DateFormat dateFormat = new SimpleDateFormat("d MMM");
		final ISeriesSet seriesSet = inventoryInsAndOutChart.getSeriesSet();
		final ISeriesSet inventoryDailyChartSeriesSet = inventoryDailyChartViewer.getSeriesSet();

		// Delete existing data
		clearChartData(seriesSet);
		cargoSeries = null;
		openSeries = null;
		thirdPartyCargoSeries = null;
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			clearChartData(mullMonthlyOverliftChart.getSeriesSet());
			clearChartData(mullMonthlyCargoCountChart.getSeriesSet());
			overliftChartModeAction.clear();
		}

		LocalDate minDate = null;
		LocalDate maxDate = null;

		final List<InventoryLevel> tableLevels = new LinkedList<>();

		final List<Pair<MullInformation, MullInformation>> pairedMullList = new LinkedList<>();
		final List<Pair<MullInformation, MullInformation>> pairedCumulativeMullList = new LinkedList<>();
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
									final Optional<MullSubprofile> sProfileOpt = mullProfile.getInventories().stream().filter(s -> s.getInventory() != null && s.getInventory().equals(inventory))
											.findAny();
									if (sProfileOpt.isPresent()) {
										final MullSubprofile sProfile = sProfileOpt.get();
										if (!sProfile.getEntityTable().isEmpty() && isValidADPDateRange(adpModel) && isValidMullSubProfile(sProfile)) {

											final YearMonth adpStart = adpModel.getYearStart();
											final YearMonth adpEnd = adpModel.getYearEnd();
											final LocalDate startDate = adpStart.atDay(1);
											TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = getInventoryInsAndOuts(inventory);
											int totalInventoryVolume = 0;
											while (!insAndOuts.isEmpty() && insAndOuts.firstKey().isBefore(startDate)) {
												final InventoryDailyEvent event = insAndOuts.remove(insAndOuts.firstKey());
												totalInventoryVolume += event.netVolumeIn;
											}
											if (!insAndOuts.isEmpty()) {
												// Do not need to add inventory volume pre ADP year since they should be
												// associated with pre ADP cargoes and initial tank volume (which should be
												// covered by initial allocation)
												// insAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
												final Map<YearMonth, Integer> monthlyProduction = calculateMonthlyProduction(inventory, adpStart);
												final List<BaseLegalEntity> entitiesOrdered = calculateEntityOrder(sProfile);
												final List<Pair<BaseLegalEntity, Double>> relativeEntitlements = sProfile.getEntityTable().stream() //
														.map(row -> new Pair<BaseLegalEntity, Double>(row.getEntity(), row.getRelativeEntitlement())) //
														.collect(Collectors.toList());
												if (pinnedResult != null) {
													final ScheduleModel pinnedScheduleModel = pinnedResult.getTypedResult(ScheduleModel.class);
													final Schedule pinnedSchedule = pinnedScheduleModel.getSchedule();
													final LNGScenarioModel pinnedLNGScenarioModel = (LNGScenarioModel) pinnedResult.getRootObject();
													final List<MullInformation> pinnedMullList = new LinkedList<>();
													final List<MullInformation> pinnedMullCumulativeList = new LinkedList<>();
													final List<MullDailyInformation> pinnedMullDailyList = new LinkedList<>();
													if (pinnedSchedule != null) {
														final List<CargoAllocation> pinnedCargoAllocations = pinnedSchedule.getCargoAllocations();

														fillMullInformationList(adpStart, adpEnd, entitiesOrdered, sProfile, monthlyProduction, expectedLoadPort, pinnedCargoAllocations,
																pinnedMullList, pinnedLNGScenarioModel, mullProfile.getFullCargoLotValue());
														final Map<BaseLegalEntity, MullInformation> previousCumulativeInformationMap = new HashMap<>();
														for (final MullInformation currentMullInfo : pinnedMullList) {
															final MullInformation prevMullInfo = previousCumulativeInformationMap.get(currentMullInfo.entity);
															final MullInformation nextCumulativeMullInformation = new MullInformation();
															nextCumulativeMullInformation.ym = currentMullInfo.getYM();
															nextCumulativeMullInformation.entity = currentMullInfo.getEntity();
															nextCumulativeMullInformation.adpStart = adpStart;
															nextCumulativeMullInformation.cumulativeDelta = currentMullInfo.cumulativeDelta;
															nextCumulativeMullInformation.cumulativeDeltaViolatesFCL = currentMullInfo.cumulativeDeltaViolatesFCL;
															if (prevMullInfo == null) {
																nextCumulativeMullInformation.cargoCount = currentMullInfo.getCargoCount();
																nextCumulativeMullInformation.monthlyRE = currentMullInfo.getMonthlyRE();
																nextCumulativeMullInformation.lifted = currentMullInfo.getLifted();
															} else {
																nextCumulativeMullInformation.cargoCount = currentMullInfo.getCargoCount() + prevMullInfo.getCargoCount();
																nextCumulativeMullInformation.monthlyRE = currentMullInfo.getMonthlyRE() + prevMullInfo.getMonthlyRE();
																nextCumulativeMullInformation.lifted = currentMullInfo.getLifted() + prevMullInfo.getLifted();
															}
															previousCumulativeInformationMap.put(nextCumulativeMullInformation.getEntity(), nextCumulativeMullInformation);
															pinnedMullCumulativeList.add(nextCumulativeMullInformation);
														}

														final List<CargoAllocation> sortedPinnedCargoAllocations = pinnedCargoAllocations.stream() //
																.filter(c -> c.getSlotAllocations().get(0).getPort().equals(expectedLoadPort)) //
																.filter(c -> !c.getSlotAllocations().get(0).getSlot().getWindowStart().isBefore(startDate)) //
																.sorted((c1, c2) -> c1.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate()
																		.compareTo(c2.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate())) //
																.collect(Collectors.toList());
														final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(sProfile);

														final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = buildEntityEntityMap(pinnedLNGScenarioModel, entitiesOrdered);
														final Map<BaseLegalEntity, BaseLegalEntity> reverseEntityEntityMap = new HashMap<>();
														for (final Entry<BaseLegalEntity, BaseLegalEntity> entry : entityEntityMap.entrySet()) {
															reverseEntityEntityMap.put(entry.getValue(), entry.getKey());
														}
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> pinnedAllocatedEntitlement = calculateDailyAllocatedEntitlement(insAndOuts,
																relativeEntitlements, entityEntityMap);
														final List<BaseLegalEntity> otherEntities = sProfile.getEntityTable().stream().map(MullEntityRow::getEntity).collect(Collectors.toList());
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> pinnedActualEntitlement = calculateDailyActualEntitlement(otherEntities,
																sortedPinnedCargoAllocations, insAndOuts, pinnedAllocatedEntitlement, initialAllocation);
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

														final Map<BaseLegalEntity, List<LocalDate>> pinnedLiftingDates = new HashMap<>();
														for (final BaseLegalEntity entity : entitiesOrdered) {
															pinnedLiftingDates.put(entity, new LinkedList<>());
														}
														for (final CargoAllocation alloc : sortedPinnedCargoAllocations) {
															final SlotAllocation slotAlloc = alloc.getSlotAllocations().get(0);
															final BaseLegalEntity slotEntity = slotAlloc.getSlot().getEntity();
															if (slotEntity != null) {
																pinnedLiftingDates.get(slotEntity).add(slotAlloc.getSlotVisit().getStart().toLocalDate());
															}
														}
														final Map<BaseLegalEntity, Iterator<LocalDate>> iterPinnedLiftingDates = new HashMap<>();
														final Map<BaseLegalEntity, LocalDate> pinnedNextLiftingDates = new HashMap<>();
														pinnedLiftingDates.forEach((e, l) -> iterPinnedLiftingDates.put(e, l.iterator()));
														iterPinnedLiftingDates.entrySet().stream().filter(e -> e.getValue().hasNext())
																.forEach(e -> pinnedNextLiftingDates.put(e.getKey(), e.getValue().next()));

														pinnedMullDailyList.forEach(m -> {
															final LocalDate nextLiftingDate = pinnedNextLiftingDates.get(m.entity);
															if (nextLiftingDate != null) {
																if (m.date.equals(nextLiftingDate)) {
																	m.lifting = true;
																	final Iterator<LocalDate> iter = iterPinnedLiftingDates.get(m.entity);
																	if (iter.hasNext()) {
																		pinnedNextLiftingDates.put(m.entity, iter.next());
																	} else {
																		pinnedNextLiftingDates.remove(m.entity);
																	}
																}
															}
														});

													}
													final List<MullInformation> otherMullList = new LinkedList<>();
													final List<MullInformation> otherMullCumulativeList = new LinkedList<>();
													final List<MullDailyInformation> otherMullDailyList = new LinkedList<>();
													if (otherResult != null) {
														final ScheduleModel otherScheduleModel = otherResult.getTypedResult(ScheduleModel.class);
														final Schedule otherSchedule = otherScheduleModel.getSchedule();
														if (otherSchedule != null) {
															final LNGScenarioModel otherLngScenarioModel = (LNGScenarioModel) otherResult.getRootObject();
															final ADPModel otherADPModel = otherLngScenarioModel.getAdpModel();
															final Optional<Inventory> optOtherInventory = otherLngScenarioModel.getCargoModel().getInventoryModels().stream()
																	.filter(i -> i.getName().equals(inventory.getName())).findAny();
															if (optOtherInventory.isPresent()) {
																final Inventory otherInventory = optOtherInventory.get();
																final MullSubprofile otherSProfile = otherADPModel.getMullProfile().getInventories().stream()
																		.filter(s -> s.getInventory().equals(otherInventory)).findAny().get();
																final Port otherInventoryLoadPort = otherInventory.getPort();

																final List<CargoAllocation> otherCargoAllocations = otherSchedule.getCargoAllocations();

																fillMullInformationList(adpStart, adpEnd, entitiesOrdered, otherSProfile, monthlyProduction, otherInventoryLoadPort,
																		otherCargoAllocations, otherMullList, otherLngScenarioModel, mullProfile.getFullCargoLotValue());

																final Map<BaseLegalEntity, MullInformation> previousCumulativeInformationMap = new HashMap<>();
																for (final MullInformation currentMullInfo : otherMullList) {
																	final MullInformation prevMullInfo = previousCumulativeInformationMap.get(currentMullInfo.entity);
																	final MullInformation nextCumulativeMullInformation = new MullInformation();
																	nextCumulativeMullInformation.ym = currentMullInfo.getYM();
																	nextCumulativeMullInformation.entity = currentMullInfo.getEntity();
																	nextCumulativeMullInformation.adpStart = adpStart;
																	nextCumulativeMullInformation.cumulativeDelta = currentMullInfo.cumulativeDelta;
																	nextCumulativeMullInformation.cumulativeDeltaViolatesFCL = currentMullInfo.cumulativeDeltaViolatesFCL;
																	if (prevMullInfo == null) {
																		nextCumulativeMullInformation.cargoCount = currentMullInfo.getCargoCount();
																		nextCumulativeMullInformation.monthlyRE = currentMullInfo.getMonthlyRE();
																		nextCumulativeMullInformation.lifted = currentMullInfo.getLifted();
																	} else {
																		nextCumulativeMullInformation.cargoCount = currentMullInfo.getCargoCount() + prevMullInfo.getCargoCount();
																		nextCumulativeMullInformation.monthlyRE = currentMullInfo.getMonthlyRE() + prevMullInfo.getMonthlyRE();
																		nextCumulativeMullInformation.lifted = currentMullInfo.getLifted() + prevMullInfo.getLifted();
																	}
																	previousCumulativeInformationMap.put(nextCumulativeMullInformation.getEntity(), nextCumulativeMullInformation);
																	otherMullCumulativeList.add(nextCumulativeMullInformation);
																}

																final List<CargoAllocation> sortedOtherCargoAllocations = otherCargoAllocations.stream() //
																		.filter(c -> c.getSlotAllocations().get(0).getPort().equals(otherInventoryLoadPort)) //
																		.filter(c -> !c.getSlotAllocations().get(0).getSlot().getWindowStart().isBefore(startDate)) //
																		.sorted((c1, c2) -> c1.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate()
																				.compareTo(c2.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate())) //
																		.collect(Collectors.toList());

																final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(otherSProfile);

																final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = buildEntityEntityMap(otherLngScenarioModel, entitiesOrdered);
																final Map<BaseLegalEntity, BaseLegalEntity> reverseEntityEntityMap = new HashMap<>();
																for (final Entry<BaseLegalEntity, BaseLegalEntity> entry : entityEntityMap.entrySet()) {
																	reverseEntityEntityMap.put(entry.getValue(), entry.getKey());
																}

																final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherAllocatedEntitlement = calculateDailyAllocatedEntitlement(insAndOuts,
																		relativeEntitlements, entityEntityMap);
																final List<BaseLegalEntity> otherEntities = otherSProfile.getEntityTable().stream().map(MullEntityRow::getEntity)
																		.collect(Collectors.toList());
																final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherActualEntitlement = calculateDailyActualEntitlement(otherEntities,
																		sortedOtherCargoAllocations, insAndOuts, otherAllocatedEntitlement, initialAllocation);
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

																final Map<BaseLegalEntity, List<LocalDate>> otherLiftingDates = new HashMap<>();
																for (final BaseLegalEntity entity : entitiesOrdered) {
																	otherLiftingDates.put(entity, new LinkedList<>());
																}
																for (final CargoAllocation alloc : sortedOtherCargoAllocations) {
																	final SlotAllocation slotAlloc = alloc.getSlotAllocations().get(0);
																	final BaseLegalEntity slotEntity = slotAlloc.getSlot().getEntity();
																	if (slotEntity != null) {
																		otherLiftingDates.get(reverseEntityEntityMap.get(slotEntity)).add(slotAlloc.getSlotVisit().getStart().toLocalDate());
																	}
																}
																final Map<BaseLegalEntity, Iterator<LocalDate>> iterOtherLiftingDates = new HashMap<>();
																final Map<BaseLegalEntity, LocalDate> otherNextLiftingDates = new HashMap<>();
																otherLiftingDates.forEach((e, l) -> iterOtherLiftingDates.put(e, l.iterator()));
																iterOtherLiftingDates.entrySet().stream().filter(e -> e.getValue().hasNext())
																		.forEach(e -> otherNextLiftingDates.put(e.getKey(), e.getValue().next()));

																otherMullDailyList.forEach(m -> {
																	final LocalDate nextLiftingDate = otherNextLiftingDates.get(m.entity);
																	if (nextLiftingDate != null) {
																		if (m.date.equals(nextLiftingDate)) {
																			m.lifting = true;
																			final Iterator<LocalDate> iter = iterOtherLiftingDates.get(m.entity);
																			if (iter.hasNext()) {
																				otherNextLiftingDates.put(m.entity, iter.next());
																			} else {
																				otherNextLiftingDates.remove(m.entity);
																			}
																		}
																	}
																});
															}
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

													if (pinnedMullCumulativeList.isEmpty() && !otherMullCumulativeList.isEmpty()) {
														final Iterator<MullInformation> otherIter = otherMullCumulativeList.iterator();
														while (otherIter.hasNext()) {
															pairedCumulativeMullList.add(new Pair<>(otherIter.next(), null));
														}
													} else if (!pinnedMullCumulativeList.isEmpty() && otherMullCumulativeList.isEmpty()) {
														final Iterator<MullInformation> pinnedIter = pinnedMullCumulativeList.iterator();
														while (pinnedIter.hasNext()) {
															pairedCumulativeMullList.add(new Pair<>(pinnedIter.next(), null));
														}
													} else if (!pinnedMullCumulativeList.isEmpty() && !otherMullCumulativeList.isEmpty()) {
														final Iterator<MullInformation> pinnedIter = pinnedMullCumulativeList.iterator();
														final Iterator<MullInformation> otherIter = otherMullCumulativeList.iterator();
														while (pinnedIter.hasNext() && otherIter.hasNext()) {
															final MullInformation currPin = pinnedIter.next();
															final MullInformation currOth = otherIter.next();
															pairedCumulativeMullList.add(new Pair<>(currPin, currOth));
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
														final List<MullInformation> otherMullCumulativeList = new LinkedList<>();
														final List<MullDailyInformation> otherMullDailyList = new LinkedList<>();

														fillMullInformationList(adpStart, adpEnd, entitiesOrdered, sProfile, monthlyProduction, expectedLoadPort, otherCargoAllocations, otherMullList,
																lngScenarioModel, mullProfile.getFullCargoLotValue());

														final Map<BaseLegalEntity, Integer> initialAllocation = calculateInitialAllocation(sProfile);
														final Map<BaseLegalEntity, MullInformation> previousCumulativeInformationMap = new HashMap<>();
														for (final MullInformation currentMullInfo : otherMullList) {
															final MullInformation prevMullInfo = previousCumulativeInformationMap.get(currentMullInfo.entity);
															final MullInformation nextCumulativeMullInformation = new MullInformation();
															nextCumulativeMullInformation.ym = currentMullInfo.getYM();
															nextCumulativeMullInformation.entity = currentMullInfo.getEntity();
															nextCumulativeMullInformation.adpStart = adpStart;
															nextCumulativeMullInformation.cumulativeDelta = currentMullInfo.cumulativeDelta;
															nextCumulativeMullInformation.cumulativeDeltaViolatesFCL = currentMullInfo.cumulativeDeltaViolatesFCL;
															if (prevMullInfo == null) {
																nextCumulativeMullInformation.cargoCount = currentMullInfo.getCargoCount();
																nextCumulativeMullInformation.monthlyRE = currentMullInfo.getMonthlyRE();
																nextCumulativeMullInformation.lifted = currentMullInfo.getLifted();
															} else {
																nextCumulativeMullInformation.cargoCount = currentMullInfo.getCargoCount() + prevMullInfo.getCargoCount();
																nextCumulativeMullInformation.monthlyRE = currentMullInfo.getMonthlyRE() + prevMullInfo.getMonthlyRE();
																nextCumulativeMullInformation.lifted = currentMullInfo.getLifted() + prevMullInfo.getLifted();
															}
															previousCumulativeInformationMap.put(nextCumulativeMullInformation.getEntity(), nextCumulativeMullInformation);
															otherMullCumulativeList.add(nextCumulativeMullInformation);
														}
														final List<CargoAllocation> sortedOtherCargoAllocations = otherCargoAllocations.stream() //
																.filter(c -> c.getSlotAllocations().get(0).getPort().equals(expectedLoadPort)) //
																.filter(c -> !c.getSlotAllocations().get(0).getSlot().getWindowStart().isBefore(startDate)) //
																.sorted((c1, c2) -> c1.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate()
																		.compareTo(c2.getSlotAllocations().get(0).getSlotVisit().getStart().toLocalDate())) //
																.collect(Collectors.toList());

														final Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap = buildEntityEntityMap(lngScenarioModel, entitiesOrdered);
														final Map<BaseLegalEntity, BaseLegalEntity> reverseEntityEntityMap = new HashMap<>();
														for (final Entry<BaseLegalEntity, BaseLegalEntity> entry : entityEntityMap.entrySet()) {
															reverseEntityEntityMap.put(entry.getValue(), entry.getKey());
														}
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherAllocatedEntitlement = calculateDailyAllocatedEntitlement(insAndOuts,
																relativeEntitlements, entityEntityMap);
														final Map<LocalDate, Map<BaseLegalEntity, Integer>> otherActualEntitlement = calculateDailyActualEntitlement(entitiesOrdered,
																sortedOtherCargoAllocations, insAndOuts, otherAllocatedEntitlement, initialAllocation);
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
														final Map<BaseLegalEntity, List<LocalDate>> otherLiftingDates = new HashMap<>();
														for (final BaseLegalEntity entity : entitiesOrdered) {
															otherLiftingDates.put(entity, new LinkedList<>());
														}
														for (final CargoAllocation alloc : sortedOtherCargoAllocations) {
															final SlotAllocation slotAlloc = alloc.getSlotAllocations().get(0);
															final BaseLegalEntity slotEntity = slotAlloc.getSlot().getEntity();
															if (slotEntity != null) {
																final List<LocalDate> entityLiftingDates = otherLiftingDates.get(slotEntity);
																if (entityLiftingDates != null) {
																	entityLiftingDates.add(slotAlloc.getSlotVisit().getStart().toLocalDate());
																}
															}
														}
														final Map<BaseLegalEntity, Iterator<LocalDate>> iterOtherLiftingDates = new HashMap<>();
														final Map<BaseLegalEntity, LocalDate> otherNextLiftingDates = new HashMap<>();
														otherLiftingDates.forEach((e, l) -> iterOtherLiftingDates.put(e, l.iterator()));
														iterOtherLiftingDates.entrySet().stream().filter(e -> e.getValue().hasNext())
																.forEach(e -> otherNextLiftingDates.put(e.getKey(), e.getValue().next()));

														otherMullDailyList.forEach(m -> {
															final LocalDate nextLiftingDate = otherNextLiftingDates.get(m.entity);
															if (nextLiftingDate != null) {
																if (m.date.equals(nextLiftingDate)) {
																	m.lifting = true;
																	final Iterator<LocalDate> iter = iterOtherLiftingDates.get(m.entity);
																	if (iter.hasNext()) {
																		otherNextLiftingDates.put(m.entity, iter.next());
																	} else {
																		otherNextLiftingDates.remove(m.entity);
																	}
																}
															}
														});
														otherMullList.forEach(m -> pairedMullList.add(Pair.of(m, null)));
														otherMullCumulativeList.forEach(m -> pairedCumulativeMullList.add(Pair.of(m, null)));
														otherMullDailyList.forEach(m -> pairedDailyMullList.add(Pair.of(m, null)));
													}
												}

												final List<YearMonth> monthsToDisplay = new LinkedList<>();
												YearMonth currentSeen = null;
												for (Pair<MullInformation, MullInformation> pair : pairedMullList) {
													final YearMonth nextYearMonth = pair.getFirst().getYM();
													if (!nextYearMonth.equals(currentSeen)) {
														monthsToDisplay.add(nextYearMonth);
														currentSeen = nextYearMonth;
													}
												}
												final DateTimeFormatter categoryFormatter = DateTimeFormatter.ofPattern("MMMyy");
												final List<String> monthsList = monthsToDisplay.stream().map(ym -> ym.format(categoryFormatter)).collect(Collectors.toList());
												final String[] temp = new String[0];
												final String[] formattedMonthLabels = monthsList.toArray(temp);

												final List<MullInformation> mullInformationList = pairedMullList.stream().map(Pair::getFirst).toList();
												final OverliftChartState overliftChartState = new OverliftChartState(mullInformationList, entitiesOrdered, formattedMonthLabels,
														mullChartColourSequence);
												overliftChartModeAction.setState(overliftChartState);
												overliftChartModeAction.updateChartData();
//												setMULLChartData(mullMonthlyOverliftChart, formattedMonthLabels, entitiesOrdered, pairedMullList, m -> m.getOverliftCF());
												// int axisId = mullMonthlyOverliftChart.getAxisSet().createXAxis();
												// final IAxis xAxis2 = mullMonthlyOverliftChart.getAxisSet().getXAxis(axisId);
												//// xAxis2.setPosition(Position.Primary);
												//// xAxis2.
												//// mullProfile.getFullCargoLotValue();
												// final double[] dates = new double[2];
												// dates[0] = -0.5;
												// dates[1] = 1000.5;
												// final double[] values = new double[2];
												// values[0] = mullProfile.getFullCargoLotValue();
												// values[1] = mullProfile.getFullCargoLotValue();
												// final ILineSeries series = (ILineSeries)
												// mullMonthlyOverliftChart.getSeriesSet().createSeries(SeriesType.LINE, "FCL
												// Max");
												// final DoubleArraySeriesModel seriesModel = new DoubleArraySeriesModel(dates,
												// values);
												// series.setDataModel(seriesModel);
												// series.setSymbolType(PlotSymbolType.NONE);
												// series.setXAxisId(axisId);

												// mullMonthlyOverliftChart.

												// final ILineSeries series = (ILineSeries)
												// seriesSet.createSeries(SeriesType.LINE, "FCL Min");

												setMULLChartData(mullMonthlyCargoCountChart, formattedMonthLabels, entitiesOrdered, pairedMullList, MullInformation::getCargoCount);
											}
										}
									}
								}
							}
						}

						boolean cargoIn = inventory.getFacilityType() != InventoryFacilityType.UPSTREAM;

						// Find the date of the latest position/cargo
						final Optional<LocalDateTime> latestSlotDate = inventoryEvents.getEvents().stream() //
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
							final Predicate<Entry<LocalDateTime, Integer>> filterFunction;
							if (latestSlotDate.isPresent() && firstInventoryDataFinal.isPresent()) {
								final LocalDateTime dateTime = latestSlotDate.get();
								final LocalDateTime firstInventoryDate = firstInventoryDataFinal.get().getDate();
								filterFunction = entry -> {
									final LocalDateTime entryDate = entry.getKey();
									return !entryDate.isAfter(dateTime) && entryDate.isAfter(firstInventoryDate);
								};
							} else {
								filterFunction = entry -> false;
							}
							final List<Pair<LocalDateTime, Integer>> inventoryLevels = inventoryEvents.getEvents().stream() //
									.collect(Collectors.toMap( //
											InventoryChangeEvent::getDate, //
											InventoryChangeEvent::getCurrentLevel, //
											(a, b) -> (b), // Take latest value
											LinkedHashMap::new))
									.entrySet().stream() //
									.filter(filterFunction) //
									.map(e -> new Pair<>(e.getKey(), e.getValue())) //
									.collect(Collectors.toList());
							if (!inventoryLevels.isEmpty()) {
								final TreeMap<LocalDateTime, Integer> hourlyLevels = new TreeMap<>();
								for (final InventoryChangeEvent event : inventoryEvents.getEvents()) {
									final InventoryEventRow eventRow = event.getEvent();
									if (eventRow != null) {
										if (eventRow.getPeriod() == InventoryFrequency.LEVEL) {
											hourlyLevels.compute(event.getDate(), (k, v) -> v == null ? event.getChangeQuantity() : v + event.getChangeQuantity());
										} else if (eventRow.getPeriod() == InventoryFrequency.CARGO) {
											hourlyLevels.compute(event.getDate(), (k, v) -> v == null ? event.getChangeQuantity() : v + event.getChangeQuantity());
										} else if (eventRow.getPeriod() == InventoryFrequency.HOURLY) {
											hourlyLevels.compute(event.getDate(), (k, v) -> v == null ? event.getChangeQuantity() : v + event.getChangeQuantity());
										} else if (eventRow.getPeriod() == InventoryFrequency.DAILY) {
											final int delta = event.getChangeQuantity() / 24;
											final int firstAmount = delta + (event.getChangeQuantity() % 24);
											LocalDateTime currentDateTime = event.getDate();
											hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? firstAmount : v + firstAmount);
											for (int hour = 1; hour < 24; hour++) {
												currentDateTime = currentDateTime.plusHours(1);
												hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? delta : v + delta);
											}
										} else if (eventRow.getPeriod() == InventoryFrequency.MONTHLY) {
											final Duration dur = Duration.between(event.getDate(), event.getDate().plusMonths(1));
											final int numHours = (int) dur.toHours();
											final int delta = event.getChangeQuantity() / numHours;
											final int firstAmount = delta + (event.getChangeQuantity() % numHours);
											LocalDateTime currentDateTime = event.getDate();
											hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? firstAmount : v + firstAmount);
											for (int hour = 1; hour < numHours; ++hour) {
												currentDateTime = currentDateTime.plusHours(1);
												hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? delta : v + delta);
											}
										}
									} else if (event.getSlotAllocation() != null) {
										final SlotAllocation slotAllocation = event.getSlotAllocation();
										final int slotDuration = getSlotDuration(slotAllocation);
										final int volumeTransferred = slotAllocation.getVolumeTransferred();
										final int delta = volumeTransferred / slotDuration;
										final int firstAmount = delta + (volumeTransferred % slotDuration);
										LocalDateTime currentDateTime = slotAllocation.getSlotVisit().getStart().toLocalDateTime();
										if (cargoIn) {
											hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? firstAmount : v + firstAmount);
											for (int hour = 1; hour < slotDuration; ++hour) {
												currentDateTime = currentDateTime.plusHours(1);
												hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? delta : v + delta);
											}
										} else {
											hourlyLevels.compute(currentDateTime, (k, v) -> v == null ? -firstAmount : v - firstAmount);
											for (int hour = 1; hour < slotDuration; ++hour) {
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
										dailyLevelsMap.compute(event.getDate(), (k, v) -> v == null ? change : v + change);
									}
								}
								final List<Pair<LocalDateTime, Integer>> dailyInventoryLevels = dailyLevelsMap.entrySet().stream() //
										.map(entry -> Pair.of(entry.getKey(), entry.getValue())) //
										.collect(Collectors.toList());

								final ILineSeries seriesDaily = createSmoothLineSeries(inventoryDailyChartSeriesSet, "Production", dailyInventoryLevels);
								seriesDaily.setSymbolType(PlotSymbolType.NONE);
								seriesDaily.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

								final ILineSeries seriesHourly = createSmoothLineSeries(seriesSet, "Inventory", hourlyInventoryLevels);
								// seriesHourly.setSymbolSize(1);
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
									.toList();
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
									.toList();
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
									.filter(evt -> !evt.getSlotAllocation().getSlot().getSlotOrDelegateEntity().isThirdParty()) //
									.map(e -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
									.toList();
							if (!values.isEmpty()) {

								final IBarSeries series = createDaySizedBarSeries(seriesSet, "Cargoes", values);
								series.setBarWidth(1);
								series.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
								series.setVisible(colourSchemeAction.isShowingCargoes());
								series.setVisibleInLegend(colourSchemeAction.isShowingCargoes());
								cargoSeries = series;
							}
						}
						{
							final List<Pair<LocalDateTime, Integer>> values = inventoryEvents.getEvents().stream() //
									.filter(evt -> evt.getOpenSlotAllocation() != null) //
									.map(e -> new Pair<>(e.getDate(), Math.abs(e.getChangeQuantity()))) //
									.toList();
							if (!values.isEmpty()) {

								final IBarSeries series = createDaySizedBarSeries(seriesSet, "Open", values);
								series.setBarWidth(1);
								series.setBarColor(colourOrange);
								series.setVisible(colourSchemeAction.isShowingOpenSlots());
								series.setVisibleInLegend(colourSchemeAction.isShowingOpenSlots());
								openSeries = series;
							}
						}
						{
							{

								final List<Pair<LocalDateTime, Integer>> values = new ArrayList<>();
								for (final InventoryChangeEvent changeEvent : inventoryEvents.getEvents()) {
									if (changeEvent.getSlotAllocation() != null) {
										if (changeEvent.getSlotAllocation().getSlot().getSlotOrDelegateEntity().isThirdParty()) {
											values.add(Pair.of(changeEvent.getDate(), Math.abs(changeEvent.getChangeQuantity())));
										}
									} else if (changeEvent.getEvent() != null && changeEvent.getEvent().getPeriod() == InventoryFrequency.CARGO) {
										values.add(Pair.of(changeEvent.getDate(), Math.abs(changeEvent.getChangeQuantity())));
									}
								}
								if (!values.isEmpty()) {
									final IBarSeries series = createDaySizedBarSeries(seriesSet, "3rd-party", values);
									series.setBarWidth(2);
									series.setBarColor(colourOrange);
									series.setVisible(colourSchemeAction.isShowingCargoes());
									series.setVisibleInLegend(colourSchemeAction.isShowingCargoes());
									thirdPartyCargoSeries = series;
								}
							}
						}

					}
				}
			}
		}
		final IAxisSet axisSet = inventoryInsAndOutChart.getAxisSet();

		inventoryInsAndOutChart.getAxisSet().getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryInsAndOutChart.getAxisSet().getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryInsAndOutChart.getAxisSet().getXAxis(0).getTitle().setText("Date");
		inventoryInsAndOutChart.getAxisSet().getXAxis(0).getTick().setFormat(dateFormat);

		inventoryInsAndOutChart.getAxisSet().getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryInsAndOutChart.getAxisSet().getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		inventoryInsAndOutChart.getAxisSet().getYAxis(0).getTitle().setText("Volume");
		// 5. adjust the range for all axes.

		// Auto adjust everything
		axisSet.adjustRange();
		// Month align the date range
		if (minDate != null && maxDate != null) {
			inventoryInsAndOutChart.getAxisSet().getXAxis(0).setRange(new Range( //
					1000.0 * minDate.withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000.0 * maxDate.withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		} else {
			inventoryInsAndOutChart.getAxisSet().getXAxis(0).setRange(new Range( //
					1000.0 * LocalDate.now().withDayOfMonth(1).atStartOfDay().toEpochSecond(ZoneOffset.of("Z")),
					1000.0 * LocalDate.now().withDayOfMonth(1).atStartOfDay().plusMonths(1).toEpochSecond(ZoneOffset.of("Z"))));
		}
		// Try to force month labels
		inventoryInsAndOutChart.getAxisSet().getXAxis(0).getTick().setTickMarkStepHint((int) (15L));
		inventoryInsAndOutChart.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);

		inventoryInsAndOutChart.updateLayout();

		inventoryInsAndOutChart.redraw();

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
			finaliseMULLChart(mullMonthlyOverliftChart, "Month", "Underlift/Overlift");
			finaliseMULLChart(mullMonthlyCargoCountChart, "Month", "# Cargoes Lifted");
		}

		int total = 0;
		if (firstInventoryData.isPresent()) {
			// Set the current level to the first data in the list. Remove the change
			// quantity so the first iteration of the loop tallies.
			final InventoryChangeEvent evt = firstInventoryData.get();
			total = evt.getCurrentLevel() - evt.getChangeQuantity();
		}
		int totalLow = total;
		int totalHigh = total;
		for (final InventoryLevel lvl : tableLevels) {
			total += lvl.changeInM3;
			lvl.runningTotal = total;
			/*
			 * In the case, when the low/high forecast value is zero , we assume that's a
			 * wrong data! Hence we use the feedIn (actual volume) if it's also not zero.
			 * Maybe we need to fix that!
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
			mullMonthlyCumulativeTableViewer.setInput(pairedCumulativeMullList);
			mullDailyTableViewer.setInput(pairedDailyMullList);
		}
	}

	private int getSlotDuration(final SlotAllocation slotAllocation) {
		final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
		if (cargoAllocation != null) {
			final List<SlotAllocation> cargoSlotAllocations = cargoAllocation.getSlotAllocations();
			if (cargoSlotAllocations.size() == 2) {
				final List<Slot<?>> slots = cargoSlotAllocations.stream().<Slot<?>>map(SlotAllocation::getSlot).toList();
				final List<Slot<?>> sortedSlots = CargoSlotSorter.sortedSlots(slots);
				final LoadSlot loadSlot = (LoadSlot) sortedSlots.get(0);
				final DischargeSlot dischargeSlot = (DischargeSlot) sortedSlots.get(1);
				if (loadSlot.isDESPurchase()) {
					return dischargeSlot.getSchedulingTimeWindow().getDuration();
				} else if (dischargeSlot.isFOBSale()) {
					return loadSlot.getSchedulingTimeWindow().getDuration();
				}
			}
		}
		// LDDs assumed to be shipped
		return slotAllocation.getSlotVisit().getDuration();
	}

	private Map<LocalDate, Map<BaseLegalEntity, Integer>> calculateDailyAllocatedEntitlement(TreeMap<LocalDate, InventoryDailyEvent> insAndOuts,
			List<Pair<BaseLegalEntity, Double>> relativeEntitlements, Map<BaseLegalEntity, BaseLegalEntity> entityEntityMap) {
		Map<LocalDate, Map<BaseLegalEntity, Integer>> ret = new HashMap<>();
		for (Entry<LocalDate, InventoryDailyEvent> e : insAndOuts.entrySet()) {
			int vol = e.getValue().netVolumeIn;
			Map<BaseLegalEntity, Integer> currentAllocation = new HashMap<>();
			final double totalRE = relativeEntitlements.stream().mapToDouble(Pair::getSecond).sum();
			for (Pair<BaseLegalEntity, Double> entitlement : relativeEntitlements) {
				currentAllocation.put(entityEntityMap.get(entitlement.getFirst()), ((Double) (vol * entitlement.getSecond() / totalRE)).intValue());
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
		final DateArraySeriesModel seriesModel = new DateArraySeriesModel(dates, values);
		series.setDataModel(seriesModel);
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
		final DateArraySeriesModel seriesModel = new DateArraySeriesModel(dates, values);
		series.setDataModel(seriesModel);
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

		final DateArraySeriesModel seriesModel = new DateArraySeriesModel(dates, values);
		series.setDataModel(seriesModel);
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

	private GridViewerColumn createMullDailyColumn(final String title, final int width, Function<Pair<MullDailyInformation, MullDailyInformation>, String> labelProvider,
			Function<Pair<MullDailyInformation, MullDailyInformation>, Comparable<?>> sortingFetcher) {
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
					cell.setBackground(colourLightGrey);
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
				return labelProvider.apply((Pair<MullDailyInformation, MullDailyInformation>) object);
			}
		};

		column.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);

		this.mullDailySortingSupport.addSortableColumn(mullDailyTableViewer, column, column.getColumn());
		final IComparableProvider sortingProvider = m -> sortingFetcher.apply((Pair<MullDailyInformation, MullDailyInformation>) m);

		column.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
		return column;
	}

	private GridViewerColumn createMullColumn(final String title, final int width, Function<Pair<MullInformation, MullInformation>, String> labelProvider,
			Function<Pair<MullInformation, MullInformation>, Comparable<?>> sortingFetcher) {
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
					cell.setBackground(colourLightGrey);
				}
				ServiceHelper.withOptionalServiceConsumer(IOverliftReportCustomiser.class, customiser -> {
					if (customiser != null && customiser.hightlightFclViolations() && !title.equals("Month") && !title.equals("Entity") && !title.equals("Lifting")) {
						if (element.getSecond() == null) {
							if ((element.getFirst().deltaViolatesFCL && title.equals("Delta")) || (element.getFirst().cumulativeDeltaViolatesFCL && title.equals("Cumulative Delta"))) {
								cell.setForeground(colourRed);
							}
						} else {
							if (cellText.length() > 0 && !cellText.equals("0")) {
								cell.setForeground(colourRed);
							}
						}
					}
				});
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
				return labelProvider.apply((Pair<MullInformation, MullInformation>) object);
			}
		};

		column.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);

		this.mullMonthlyTableSortingSupport.addSortableColumn(mullMonthlyTableViewer, column, column.getColumn());
		final IComparableProvider sortingProvider = m -> sortingFetcher.apply((Pair<MullInformation, MullInformation>) m);

		column.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
		return column;
	}

	private GridViewerColumn createMullCumulativeColumn(final String title, final int width, Function<Pair<MullInformation, MullInformation>, String> labelProvider,
			Function<Pair<MullInformation, MullInformation>, Comparable<?>> sortingFetcher) {
		final GridViewerColumn column = new GridViewerColumn(mullMonthlyCumulativeTableViewer, SWT.NONE);
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
					cell.setBackground(colourLightGrey);
				}
				ServiceHelper.withOptionalServiceConsumer(IOverliftReportCustomiser.class, customiser -> {
					if (customiser != null && customiser.hightlightFclViolations() && !title.equals("Period") && !title.equals("Entity")) {
						if (element.getSecond() == null) {
							if (element.getFirst().cumulativeDeltaViolatesFCL && title.equals("Delta")) {
								cell.setForeground(colourRed);
							}
						} else {
							if (cellText.length() > 0 && !cellText.equals("0")) {
								cell.setForeground(colourRed);
							}
						}
					}
				});
			}
		});

		this.mullMonthlyCumulativeTableFilterSupport.createColumnMnemonics(column.getColumn(), column.getColumn().getText());
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
				return labelProvider.apply((Pair<MullInformation, MullInformation>) object);
			}
		};

		column.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);

		this.mullMonthlyCumulativeTableSortingSupport.addSortableColumn(mullMonthlyCumulativeTableViewer, column, column.getColumn());
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

	private boolean isValidADPDateRange(@NonNull final ADPModel adpModel) {
		final YearMonth start = adpModel.getYearStart();
		final YearMonth end = adpModel.getYearEnd();
		return start != null && end != null && start.isBefore(end) && Months.between(start, end) == 12;
	}

	private boolean isValidMullSubProfile(final MullSubprofile subProfile) {
		return subProfile.getEntityTable().stream().allMatch(row -> {
			if (row.getEntity() == null)
				return false;
			if (row.getInitialAllocation() == null || !row.getInitialAllocation().matches("-?\\d+"))
				return false;
			return true;
		});
	}

	private Map<YearMonth, Integer> calculateMonthlyProduction(final Inventory inventory, final YearMonth adpStart) {
		final Map<YearMonth, Integer> monthlyProduction = new HashMap<>();
		inventory.getFeeds().stream().filter(event -> event.getStartDate() != null && event.getEndDate() != null && !YearMonth.from(event.getStartDate()).isBefore(adpStart)).forEach(row -> {

			if (row.getPeriod() == InventoryFrequency.DAILY) {
				if (row.getStartDate().equals(row.getEndDate())) {
					final YearMonth currYM = YearMonth.from(row.getStartDate());
					monthlyProduction.compute(currYM, (ym, v) -> v == null ? row.getVolume() : v + row.getVolume());
				} else {
					final LocalDate inclusiveEnd = row.getEndDate().minusDays(1L);
					final YearMonth endYm = YearMonth.from(inclusiveEnd);
					LocalDate currentStartDate = row.getStartDate();
					YearMonth currentYm = YearMonth.from(currentStartDate);
					if (!currentYm.isBefore(endYm)) {
						final int productionDays = inclusiveEnd.getDayOfMonth() - currentStartDate.getDayOfMonth() + 1;
						final int productionAmount = productionDays * row.getVolume();
						monthlyProduction.compute(currentYm, (ym, v) -> v == null ? productionAmount : v + productionAmount);
					} else {
						if (currentStartDate.getDayOfMonth() != 1) {
							final int productionDays = currentYm.lengthOfMonth() - currentStartDate.getDayOfMonth() + 1;
							final int productionAmount = productionDays * row.getVolume();
							monthlyProduction.compute(currentYm, (ym, v) -> v == null ? productionAmount : v + productionAmount);
							currentYm = currentYm.plusMonths(1L);
							currentStartDate = currentYm.atDay(1);
						}
						while (currentYm.isBefore(endYm)) {
							final int productionAmount = currentYm.lengthOfMonth() * row.getVolume();
							monthlyProduction.compute(currentYm, (ym, v) -> v == null ? productionAmount : v + productionAmount);
							currentYm = currentYm.plusMonths(1L);
							currentStartDate = currentYm.atDay(1);
						}
						final int productionDays = inclusiveEnd.getDayOfMonth() - currentStartDate.getDayOfMonth() + 1;
						final int productionAmount = productionDays * row.getVolume();
						monthlyProduction.compute(currentYm, (ym, v) -> v == null ? productionAmount : v + productionAmount);
					}
				}
			} else {
				final YearMonth currYM = YearMonth.from(row.getStartDate());
				final Integer currVal = monthlyProduction.get(currYM);
				if (currVal == null) {
					monthlyProduction.put(currYM, row.getVolume());
				} else {
					monthlyProduction.put(currYM, currVal + row.getVolume());
				}
			}
		});
		inventory.getOfftakes().stream().filter(event -> event.getStartDate() != null && event.getEndDate() != null).forEach(row -> {
			final YearMonth currYM = YearMonth.from(row.getStartDate());
			final Integer currVal = monthlyProduction.get(currYM);
			if (currVal == null) {
				monthlyProduction.put(currYM, -row.getVolume());
			} else {
				monthlyProduction.put(currYM, currVal - row.getVolume());
			}
		});
		final Set<YearMonth> ymToAccumulate = monthlyProduction.keySet().stream().filter(adpStart::isAfter).collect(Collectors.toSet());
		Integer accTemp = 0;
		for (final YearMonth ym : ymToAccumulate) {
			accTemp += monthlyProduction.remove(ym);
		}
		final Integer acc = accTemp;
		monthlyProduction.compute(adpStart, (k, v) -> v == null ? acc : v + acc);

		return monthlyProduction;
	}

	private Map<YearMonth, Map<BaseLegalEntity, Integer>> calculateMonthlyRE(final YearMonth adpStart, final YearMonth adpEnd, final MullSubprofile sProfile,
			final Map<YearMonth, Integer> monthlyProduction) {
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
			final double totalRE = sProfile.getEntityTable().stream().mapToDouble(MullEntityRow::getRelativeEntitlement).sum();
			sProfile.getEntityTable().stream().forEach(row -> {
//				if (ymm.equals(adpStart)) {
//					currMap.put(row.getEntity(), Integer.parseInt(row.getInitialAllocation()) + (int) (row.getRelativeEntitlement() / totalRE * thisTotalProd));
//				} else {
				currMap.put(row.getEntity(), (int) (row.getRelativeEntitlement() / totalRE * thisTotalProd));
//				}
			});
		}
		return monthlyRE;
	}

	private Map<YearMonth, Map<BaseLegalEntity, Integer>> calculateActualLift(final YearMonth adpStart, final YearMonth adpEnd, final MullSubprofile sProfile,
			final List<CargoAllocation> cargoAllocations, final Port inventoryLoadPort) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> actualLift = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd.plusMonths(1)); ym = ym.plusMonths(1)) {
			final Map<BaseLegalEntity, Integer> currMap = new HashMap<>();
			sProfile.getEntityTable().stream().map(MullEntityRow::getEntity).forEach(entity -> currMap.put(entity, 0));
			actualLift.put(ym, currMap);
		}
		cargoAllocations.stream() //
				.map(cargoAlloc -> cargoAlloc.getSlotAllocations().get(0)).filter(loadSlotAlloc -> loadSlotAlloc.getPort().equals(inventoryLoadPort))
				.filter(loadSlotAlloc -> YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate()).isBefore(adpEnd.plusMonths(1))).forEach(loadSlotAlloc -> {
					final YearMonth ym = YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate());
					final BaseLegalEntity thisEntity = loadSlotAlloc.getSlot().getEntity();
					final Map<BaseLegalEntity, Integer> ymLifts = actualLift.get(ym);
					if (ymLifts != null) {
						ymLifts.computeIfPresent(thisEntity, (k, v) -> v + loadSlotAlloc.getPhysicalVolumeTransferred());
					}
				});
		return actualLift;
	}

	private Map<YearMonth, Map<BaseLegalEntity, Integer>> calculateCargoCount(final YearMonth adpStart, final YearMonth adpEnd, final MullSubprofile sProfile,
			final List<CargoAllocation> cargoAllocations, final Port inventoryLoadPort) {
		final Map<YearMonth, Map<BaseLegalEntity, Integer>> cargoCount = new HashMap<>();
		for (YearMonth ym = adpStart; ym.isBefore(adpEnd.plusMonths(1)); ym = ym.plusMonths(1)) {
			final Map<BaseLegalEntity, Integer> cargoCountMap = new HashMap<>();
			sProfile.getEntityTable().stream().map(MullEntityRow::getEntity).forEach(entity -> cargoCountMap.put(entity, 0));
			cargoCount.put(ym, cargoCountMap);
		}
		cargoAllocations.stream() //
				.map(cargoAlloc -> cargoAlloc.getSlotAllocations().get(0)).filter(loadSlotAlloc -> loadSlotAlloc.getPort().equals(inventoryLoadPort))
				.filter(loadSlotAlloc -> YearMonth.from(loadSlotAlloc.getSlotVisit().getStart().toLocalDate()).isBefore(adpEnd.plusMonths(1))).forEach(loadSlotAlloc -> {
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
				curr.monthEndEntitlement = prev.get(curr.entity).monthEndEntitlement + curr.monthlyRE - curr.lifted;
			}
			prev.put(curr.entity, curr);
		}
	}

	private List<BaseLegalEntity> calculateEntityOrder(final MullSubprofile sProfile) {
		final Map<BaseLegalEntity, Double> orderVal = new HashMap<>();
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
			return pinnedVal == 0 ? "-" : String.format("%,d", pinnedVal);
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

	private void calculateOverliftCF(final List<MullInformation> mullList, final YearMonth adpStart, final Map<BaseLegalEntity, Integer> initialAllocation) {
		final Map<BaseLegalEntity, MullInformation> prev = new HashMap<>();
		for (MullInformation curr : mullList) {
			if (curr.ym.equals(adpStart)) {
				curr.overliftCF = curr.overlift - initialAllocation.get(curr.entity);
			} else {
				curr.overliftCF = curr.overlift - prev.get(curr.entity).monthEndEntitlement;
			}
			prev.put(curr.entity, curr);
		}
	}

	private void calculateCarriedEntitlement(List<MullInformation> mullList, YearMonth adpStart, final Map<BaseLegalEntity, Integer> initialAllocation) {
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

	private void calculateCumulativeDelta(List<MullInformation> mullList, YearMonth adpStart, final Map<BaseLegalEntity, Integer> initialAllocation, int fclValue) {
		final Map<BaseLegalEntity, MullInformation> prev = new HashMap<>();
		for (MullInformation curr : mullList) {
			if (curr.ym.equals(adpStart)) {
				curr.cumulativeDelta = curr.getDelta() + initialAllocation.get(curr.getEntity());
			} else {
				curr.cumulativeDelta = prev.get(curr.entity).getCumulativeDelta() + curr.getDelta();
			}
			curr.cumulativeDeltaViolatesFCL = curr.cumulativeDelta < -fclValue || curr.cumulativeDelta > fclValue;
			prev.put(curr.entity, curr);
		}
	}

	private TreeMap<LocalDate, InventoryDailyEvent> getInventoryInsAndOuts(Inventory inventory) {
		TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = new TreeMap<>();

		// add all feeds to map
		addNetVolumes(inventory.getFeeds(), insAndOuts, IntUnaryOperator.identity());
		addNetVolumes(inventory.getOfftakes(), insAndOuts, a -> -a);
		return insAndOuts;
	}

	private void addNetVolumes(List<InventoryEventRow> events, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, IntUnaryOperator volumeFunction) {
		for (InventoryEventRow inventoryEventRow : events) {
			if (inventoryEventRow.getStartDate() != null && inventoryEventRow.getEndDate() != null) {
				final LocalDate exclusiveEnd = inventoryEventRow.getStartDate().equals(inventoryEventRow.getEndDate()) ? inventoryEventRow.getStartDate().plusDays(1L) : inventoryEventRow.getEndDate();
				for (LocalDate currentDate = inventoryEventRow.getStartDate(); currentDate.isBefore(exclusiveEnd); currentDate = currentDate.plusDays(1L)) {
					final InventoryDailyEvent inventoryDailyEvent = insAndOuts.computeIfAbsent(currentDate, d -> {
						final InventoryDailyEvent newEvent = new InventoryDailyEvent();
						newEvent.date = d;
						return newEvent;
					});
					inventoryDailyEvent.addVolume(volumeFunction.applyAsInt(inventoryEventRow.getReliableVolume()));
				}
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

	private void fillMullInformationList(final YearMonth adpStart, final YearMonth adpEnd, final List<BaseLegalEntity> entitiesOrdered, final MullSubprofile sProfile,
			final Map<YearMonth, Integer> monthlyProduction, final Port inventoryLoadPort, final List<CargoAllocation> cargoAllocations, final List<MullInformation> list,
			final LNGScenarioModel lngScenarioModel, final int fclValue) {
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
				currRow.monthlyRE = monthlyRE.get(ym).get(entityMirrorObject);
				currRow.overlift = currRow.lifted - currRow.monthlyRE;
				currRow.cargoCount = cargoCount.get(ym).get(entityMirrorObject);
				currRow.delta = currRow.monthlyRE - currRow.lifted;
				currRow.deltaViolatesFCL = currRow.delta > fclValue || currRow.delta < -fclValue;
				list.add(currRow);
			}
		}
		calculateMonthEndEntitlement(list, adpStart);
		calculateOverliftCF(list, adpStart, initialAllocation);
		calculateCumulativeDelta(list, adpStart, initialAllocation, fclValue);
		final Map<BaseLegalEntity, Integer> mappedInitialAllocation = new HashMap<>();
		entitiesOrdered.stream().forEach(e -> mappedInitialAllocation.put(e, initialAllocation.get(entityEntityMap.get(e))));
		calculateCarriedEntitlement(list, adpStart, mappedInitialAllocation);
	}

	private Map<LocalDate, Map<BaseLegalEntity, Integer>> calculateDailyActualEntitlement(List<BaseLegalEntity> entities, List<CargoAllocation> sortedCargoAllocations,
			TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, Map<LocalDate, Map<BaseLegalEntity, Integer>> allocatedEntitlement, final Map<BaseLegalEntity, Integer> initialAllocations) {
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
					currMap.computeIfPresent(currEnt, (k, v) -> v - transferred);
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
				currMap.computeIfPresent(currEnt, (k, v) -> v - transferred);
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

	private void clearChartData(final ISeriesSet seriesSet) {
		final Set<String> names = new HashSet<>();
		for (final ISeries s : seriesSet.getSeries()) {
			names.add(s.getId());
		}
		names.forEach(seriesSet::deleteSeries);
	}

	private void finaliseMULLChart(final Chart chart, final String xLabel, final String yLabel) {
		final IAxisSet axisSet = chart.getAxisSet();
		axisSet.getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getXAxis(0).getTitle().setText(xLabel);

		axisSet.getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getYAxis(0).getTitle().setText(yLabel);

		axisSet.adjustRange();
		chart.updateLayout();
		chart.redraw();
	}

	private void setMULLChartData(final Chart chart, final String[] xCategoryLabels, final List<BaseLegalEntity> entitiesOrdered, final List<Pair<MullInformation, MullInformation>> pairedMullList,
			final ToIntFunction<MullInformation> valueExtractor) {
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(xCategoryLabels);

		final Map<BaseLegalEntity, List<Integer>> barSeriesData = new HashMap<>();
		for (final BaseLegalEntity entity : entitiesOrdered) {
			barSeriesData.put(entity, new LinkedList<>());
		}

		final Iterator<Pair<MullInformation, MullInformation>> iterMullList2 = pairedMullList.iterator();
		while (iterMullList2.hasNext()) {
			for (final BaseLegalEntity entity : entitiesOrdered) {
				final MullInformation currMull = iterMullList2.next().getFirst();
				barSeriesData.get(entity).add(valueExtractor.applyAsInt(currMull));
			}
		}

		int colourIndex = 0;
		for (final BaseLegalEntity entity : entitiesOrdered) {
			final List<Integer> intSeries = barSeriesData.get(entity);
			final double[] doubleSeries = new double[intSeries.size()];
			int seriesIndex = 0;
			final Iterator<Integer> iterIntSeries = intSeries.iterator();
			while (iterIntSeries.hasNext()) {
				doubleSeries[seriesIndex] = iterIntSeries.next().doubleValue();
				++seriesIndex;
			}
			IBarSeries currentEntitySeries = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, entity.getName());
			currentEntitySeries.setYSeries(doubleSeries);
			currentEntitySeries.setBarColor(this.mullChartColourSequence[colourIndex]);
			++colourIndex;
			colourIndex %= this.mullChartColourSequence.length;
		}
	}
}
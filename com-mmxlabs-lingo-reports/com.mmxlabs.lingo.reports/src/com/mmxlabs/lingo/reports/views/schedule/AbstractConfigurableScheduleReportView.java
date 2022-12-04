/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.EventHandler;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContents;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableGridReportView;
import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnOverrideExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialDiffOption;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialRowType;
import com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.CopyGridToJSONUtil;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 * A customisable report for schedule based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available
 * for the user to change the default settings.
 */
public abstract class AbstractConfigurableScheduleReportView extends AbstractConfigurableGridReportView {

	protected final ScheduleBasedReportBuilder builder;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnOverrideExtension> columnOverrideExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedReportInitialStateExtension> initialStates;

	@Inject
	private ScenarioComparisonService scenarioComparisonService;

	private EventHandler todayHandler;

	protected ReentrantSelectionManager selectionManager;

	/*
	 * Field to allow subclasses of specific reports to only include visible columns rather than everything
	 */
	protected boolean includeAllColumnsForITS = true;

	protected AbstractConfigurableScheduleReportView(final String id, final ScheduleBasedReportBuilder builder) {
		super(id);

		// Setup the builder hooks.
		this.builder = builder;
		builder.setBlockManager(getBlockManager());
		builder.setAdaptableReport(this);
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (GridTableViewer.class.isAssignableFrom(adapter)) {
			return adapter.cast(viewer);
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			return adapter.cast(viewer.getGrid());
		}

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			// Set a more repeatable sort order
			final ColumnBlock[] initialReverseSortOrder = getInitialReverseSortOrderForITS();

			if (includeAllColumnsForITS) {
				// Sort columns by ID
				final List<String> blockIDOrder = new ArrayList<>(getBlockManager().getBlockIDOrder());
				Collections.sort(blockIDOrder);
				getBlockManager().setBlockIDOrder(blockIDOrder);
			}

			// go through in reverse order as latest is set to primary sort
			for (final ColumnBlock block : initialReverseSortOrder) {
				if (block != null) {
					final List<ColumnHandler> handlers = block.getColumnHandlers();
					for (final ColumnHandler handler : handlers) {
						if (handler.column != null) {
							sortingSupport.sortColumnsBy(handler.column.getColumn());
						}
					}
				}
			}

			return adapter.cast(new IReportContentsGenerator() {
				public IReportContents getReportContents(final ScenarioResult pin, final ScenarioResult other, final @Nullable List<Object> selectedObjects) {
					final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
					if (pin != null) {
						provider.addScenario(pin);
						provider.setPinnedScenarioInstance(pin);
					}
					if (other != null) {
						provider.addScenario(other);
					}
					// Request a blocking update ...
					selectedScenariosServiceListener.selectedDataProviderChanged(provider, true);

					// ... so the data is ready to be read here.
					final CopyGridToHtmlStringUtil htmlUtil = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, includeAllColumnsForITS);
					htmlUtil.setShowBackgroundColours(true);
					htmlUtil.setShowForegroundColours(true);
					final String htmlContents = htmlUtil.convert();

					final CopyGridToJSONUtil jsonUtil = new CopyGridToJSONUtil(viewer.getGrid(), includeAllColumnsForITS);
					final String jsonContents = jsonUtil.convert();

					return ReportContents.make(htmlContents, jsonContents);
				}
			});
		}
		return super.getAdapter(adapter);

	}

	protected ColumnBlock[] getInitialReverseSortOrderForITS() {
		final ColumnBlock[] initialReverseSortOrder = { //
				getBlockManager().getBlockByID("com.mmxlabs.lingo.reports.components.columns.schedule.id") //
		};
		return initialReverseSortOrder;
	}

	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {

			clearInputEquivalents();

			final TransformedSelectedDataProvider newSelectedDataProvider = new TransformedSelectedDataProvider(selectedDataProvider);
			List<Row> rows = ScheduleReportTransformer.transform(selectedDataProvider.getPinnedScenarioResult(), selectedDataProvider.getOtherScenarioResults(), newSelectedDataProvider);
			processInputs(rows, newSelectedDataProvider);

			List<LNGScenarioModel> rootObjects = new LinkedList<>();
			for (ScenarioResult sr : selectedDataProvider.getAllScenarioResults()) {
				rootObjects.add(sr.getTypedRoot(LNGScenarioModel.class));
			}

			builder.refreshPNLColumns(rootObjects);

			for (final ColumnBlock handler : builder.getBlockManager().getBlocksInVisibleOrder()) {
				if (handler != null) {
					handler.setViewState(selectedDataProvider.getAllScenarioResults().size() > 1, selectedDataProvider.inPinDiffMode());
				}
			}
			setCurrentSelectedDataProvider(newSelectedDataProvider);

			ViewerHelper.setInput(viewer, block, rows);
			final ISelection selection = SelectionHelper.adaptSelection(newSelectedDataProvider.getSelectedObjects());

			viewer.setSelection(selection, true);
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			ViewerHelper.refresh(viewer, true);
		}

		@Override
		public void selectedObjectChanged(@Nullable MPart source, @NonNull ISelection selection) {

			if (scenarioComparisonService.getDiffOptions().isFilterSelectedElements()) {
				ViewerHelper.refresh(viewer, false);
			} else {
				selectionManager.setSelection(selection, false, true);
			}

		}
	};

	@Override
	public void initPartControl(final Composite parent) {
		super.initPartControl(parent);

		viewer.setContentProvider(new ArrayContentProvider());

		// Add a filter to only show certain rows.
		viewer.setFilters(super.filterSupport.createViewerFilter(), new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				Collection<Object> selectedElements = currentSelectedDataProvider.getChangeSetSelection();

				if (scenarioComparisonService.getDiffOptions().isFilterSelectedElements() && selectedElements != null && !selectedElements.isEmpty()) {
					if (!selectedElements.contains(element)) {
						if (element instanceof Row) {
							final Row row = (Row) element;
							final Set<EObject> elements = new HashSet<>();
							elements.add(row.getTarget());
							elements.add(row.getCargoAllocation());
							elements.add(row.getLoadAllocation());
							elements.add(row.getDischargeAllocation());
							elements.add(row.getOpenLoadSlotAllocation());
							elements.add(row.getOpenDischargeSlotAllocation());
							elements.addAll(row.getInputEquivalents());
							elements.retainAll(selectedElements);
							if (elements.isEmpty()) {
								return false;
							}
						}
					}
				}

				if (element instanceof Row) {
					final Row row = (Row) element;
					// Filter out reference scenario if required
					if (!builder.getDiffFilterInfo().contains(AbstractReportBuilder.DIFF_FILTER_PINNDED_SCENARIO.id)) {
						if (row.isReference()) {
							return false;
						}
					}

					// Element type filters
					if (!builder.showRow(row)) {
						return false;
					}

					// Only show visible rows
					return row.isVisible();
				} else if (element instanceof CompositeRow) {
					return true;
				}
				return true;
			}
		});
		selectionManager = new ReentrantSelectionManager(viewer, selectedScenariosServiceListener, scenarioComparisonService);
		try {
			scenarioComparisonService.triggerListener(selectedScenariosServiceListener, false);
		} catch (Exception e) {
			// Ignore any initial issues.
		}
		selectionManager.addListener(this);

		// Adding an event broker for the snap-to-date event todayHandler
		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		this.todayHandler = event -> snapTo((LocalDate) event.getProperty(IEventBroker.DATA));
		eventBroker.subscribe(TodayHandler.EVENT_SNAP_TO_DATE, this.todayHandler);
	}

	private void snapTo(final LocalDate date) {
		if (viewer == null) {
			return;
		}
		final Grid grid = viewer.getGrid();
		if (grid == null) {
			return;
		}
		final int count = grid.getItemCount();
		if (count <= 0) {
			return;
		}

		final GridItem[] items = grid.getItems();
		int pos = -1;
		for (final GridItem item : items) {
			final Object oData = item.getData();
			if (oData instanceof Row) {
				final Row r = (Row) oData;
				final SlotAllocation sa = r.getLoadAllocation();
				if (sa == null) {
					break;
				}
				final SlotVisit sv = sa.getSlotVisit();
				if (sv == null) {
					break;
				}
				if (sv.getStart().toLocalDate().isAfter(date)) {
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

	@Override
	public void dispose() {
		if (this.todayHandler != null) {
			final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
			eventBroker.unsubscribe(this.todayHandler);
		}
		super.dispose();
	}

	@Override
	public void saveConfigState(final IMemento configMemento) {
		if (configMemento != null) {
			builder.saveToMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
		}
	}

	@Override
	protected void initConfigMemento(final IMemento configMemento) {
		if (configMemento != null) {
			builder.initFromMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
		}
	}

	/**
	 * Check the view extension point to see if we can enable the customise dialog
	 * 
	 * @return
	 */
	@Override
	protected boolean checkCustomisable() {

		if (initialStates != null) {

			for (final IScheduleBasedReportInitialStateExtension ext : initialStates) {

				final String viewId = ext.getViewID();

				if (viewId != null && viewId.equals(getViewSite().getId())) {
					final String customisableString = ext.getCustomisable();
					if (customisableString != null) {
						return customisableString.equals("true");
					}
				}
			}
		}
		return true;
	}

	/**
	 * Examine the view extension point to determine the default set of columns, order,row types and diff options.
	 */
	@Override
	protected void setInitialState() {

		if (initialStates != null) {

			for (final IScheduleBasedReportInitialStateExtension ext : initialStates) {

				final String viewId = ext.getViewID();

				// Is this a matching view definition?
				if (viewId != null && viewId.equals(getViewSite().getId())) {
					// Get visible columns and order
					{
						final InitialColumn[] initialColumns = ext.getInitialColumns();
						if (initialColumns != null) {
							final List<String> defaultOrder = new LinkedList<>();
							for (final InitialColumn col : initialColumns) {
								final String blockID = col.getID();
								ColumnBlock block = getBlockManager().getBlockByID(blockID);
								if (block == null) {
									block = getBlockManager().createBlock(blockID, "", ColumnType.NORMAL);
								}
								block.setUserVisible(true);
								defaultOrder.add(blockID);

							}
							getBlockManager().setBlockIDOrder(defaultOrder);
						}
					}
					// Get row types
					{
						final List<String> rowFilter = new ArrayList<>(builder.ROW_FILTER_ALL.length);
						final InitialRowType[] initialRows = ext.getInitialRows();
						if (initialRows != null) {
							for (final InitialRowType row : initialRows) {

								switch (row.getRowType()) {
								case "cargo":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_CARGO_ROW.id);
									break;
								case "long":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_LONG_CARGOES.id);
									break;
								case "short":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_SHORT_CARGOES.id);
									break;
								case "virtualcharters":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_CHARTER_OUT_ROW.id);
									break;
								case "event":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_EVENT_ROW.id);
									break;
								case "orphanlegs":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_START_ROW.id);
									break;
								case "endevents":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_END_ROW.id);
									break;
								case "charterlength":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_CHARTER_LENGTH.id);
									break;
								}
							}
						}
						builder.setRowFilter(rowFilter.toArray(new String[0]));
					}
					// Get diff options
					{
						final List<String> diffOptions = new ArrayList<>(builder.DIFF_FILTER_ALL.length);
						final InitialDiffOption[] initialDiffOptions = ext.getInitialDiffOptions();
						if (initialDiffOptions != null) {
							for (final InitialDiffOption diffOption : initialDiffOptions) {

								switch (diffOption.getOption()) {

								case "vessel":
									diffOptions.add(ScheduleBasedReportBuilder.DIFF_FILTER_VESSEL_CHANGES.id);
									break;
								case "scenario":
									diffOptions.add(AbstractReportBuilder.DIFF_FILTER_PINNDED_SCENARIO.id);
									break;
								}
							}
						}
						builder.setDiffFilter(diffOptions.toArray(new String[0]));
					}
					break;
				}
			}
		}
	}

	public void processInputs(final List<Row> result, final TransformedSelectedDataProvider currentSelectedDataProvider) {
		clearInputEquivalents();
		for (final Object obj : result) {
			if (obj instanceof Row row) {
				setInputEquivalents(row, row.getInputEquivalents());
				final ScenarioResult scenarioResult = currentSelectedDataProvider.getScenarioResult(row.getSchedule());
				if (scenarioResult != null) {
					currentSelectedDataProvider.addExtraData(row, scenarioResult, row.getSchedule());
				}
			}
		}
	}

	@Override
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		return builder.adaptSelectionFromWidget(selection);
	}

	/**
	 */
	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected void addDialogCheckBoxes(final ColumnConfigurationDialog dialog) {
		dialog.addCheckBoxInfo("Show rows for", builder.ROW_FILTER_ALL, () -> builder.getRowFilterInfo());
		dialog.addCheckBoxInfo("In diff mode", builder.DIFF_FILTER_ALL, () -> builder.getDiffFilterInfo());
	}

	@Override
	protected void postDialogOpen(final ColumnConfigurationDialog dialog) {
		builder.refreshDiffOptions();
		// Update options state
		// table.getOptions().setShowPinnedScenario(!builder.getDiffFilterInfo().contains(AbstractReportBuilder.DIFF_FILTER_PINNDED_SCENARIO));

	}

	/**
	 * Examine the eclipse registry for defined columns for this report and hook them in.
	 */
	@Override
	protected void registerReportColumns() {

		final EMFReportColumnManager manager = new EMFReportColumnManager();

		// Find any shared column factories and install.
		final Map<String, IScheduleColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final IScheduleBasedColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		final Map<String, String> nameMap = new HashMap<>();
		if (columnOverrideExtensions != null) {
			for (final IScheduleBasedColumnOverrideExtension ext : columnOverrideExtensions) {
				nameMap.put(ext.getColumnID(), ext.getNameOverride());
			}
		}

		// Now find the column definitions themselves.
		if (columnExtensions != null) {

			for (final IScheduleBasedColumnExtension ext : columnExtensions) {
				IScheduleColumnFactory factory;
				if (ext.getHandlerID() != null) {
					factory = handlerMap.get(ext.getHandlerID());
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					factory.registerColumn(ext.getColumnID(), manager, builder);
				}
			}
		}
		manager.overrideColumnNames(nameMap, ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID);
		// Create the actual columns instances.
		manager.addColumns(ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID, getBlockManager());
	}

	@Override
	protected boolean refreshOnSelectionChange() {
		return false;// (scenarioComparisonService.getDiffOptions().isFilterSelectedElements() && !scenarioComparisonService.getSelectedElements().isEmpty());
	}
}
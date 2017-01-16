/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableGridReportView;
import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialDiffOption;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialRowType;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * A customisable report for schedule based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available
 * for the user to change the default settings.
 */
public abstract class AbstractConfigurableScheduleReportView extends AbstractConfigurableGridReportView {

	private final ScheduleBasedReportBuilder builder;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedReportInitialStateExtension> initialStates;

	@Inject
	private ScenarioComparisonService scenarioComparisonService;

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
			return (T) viewer;
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			return (T) viewer.getGrid();
		}

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getStringContents() {
					return contents;
				}
			};

		}
		return super.getAdapter(adapter);
	}

	private final IScenarioComparisonServiceListener scenarioComparisonServiceListener = new IScenarioComparisonServiceListener() {

		private final Map<Object, ScenarioResult> _elementToInstanceMap = new HashMap<>();
		private final Map<Object, LNGScenarioModel> _elementToModelMap = new HashMap<>();

		@Override
		public void compareDataUpdate(@NonNull final ISelectedDataProvider selectedDataProvider, @NonNull final ScenarioResult pin, @NonNull final ScenarioResult other, @NonNull final Table table,
				@NonNull final List<LNGScenarioModel> rootObjects, @NonNull final Map<EObject, Set<EObject>> equivalancesMap) {
			clearInputEquivalents();
			builder.refreshPNLColumns(rootObjects);
			processInputs(table.getRows());

			for (final ColumnBlock handler : builder.getBlockManager().getBlocksInVisibleOrder()) {
				if (handler != null) {
					handler.setViewState(true, true);
				}
			}

			updateElementMaps(selectedDataProvider, table);

			ViewerHelper.setInput(viewer, true, new ArrayList<>(table.getRows()));
		}

		@Override
		public void multiDataUpdate(@NonNull final ISelectedDataProvider selectedDataProvider, @NonNull final Collection<ScenarioResult> others, @NonNull final Table table,
				@NonNull final List<LNGScenarioModel> rootObjects) {
			clearInputEquivalents();

			builder.refreshPNLColumns(rootObjects);
			processInputs(table.getRows());

			final int numberOfSchedules = others.size();
			for (final ColumnBlock handler : builder.getBlockManager().getBlocksInVisibleOrder()) {
				if (handler != null) {
					handler.setViewState(numberOfSchedules > 1, false);
				}
			}
			updateElementMaps(selectedDataProvider, table);

			ViewerHelper.setInput(viewer, true, new ArrayList<>(table.getRows()));
		}

		private void updateElementMaps(final ISelectedDataProvider selectedDataProvider, final Table table) {
			_elementToInstanceMap.clear();
			_elementToModelMap.clear();
			for (final Row row : table.getRows()) {
				@Nullable
				final ScenarioResult scenarioResult = selectedDataProvider.getScenarioResult(row.getSchedule());
				final LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
				_elementToModelMap.put(row.getSchedule(), scenarioModel);
				_elementToInstanceMap.put(row.getSchedule(), scenarioResult);
				row.getInputEquivalents().forEach(e -> {
					_elementToInstanceMap.put(e, scenarioResult);
					_elementToModelMap.put(e, scenarioModel);
				});
			}

			mapInputs(_elementToInstanceMap, _elementToModelMap);
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			ViewerHelper.refresh(viewer, true);
		}

	};

	@Override
	public void initPartControl(final Composite parent) {
		super.initPartControl(parent);

		viewer.setContentProvider(new ArrayContentProvider());
		scenarioComparisonService.addListener(scenarioComparisonServiceListener);

		// Add a filter to only show certain rows.
		viewer.setFilters(new ViewerFilter[] { super.filterSupport.createViewerFilter(), new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				if (scenarioComparisonService.getDiffOptions().isFilterSelectedElements() && !scenarioComparisonService.getSelectedElements().isEmpty()) {
					if (!scenarioComparisonService.getSelectedElements().contains(element)) {
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
							elements.retainAll(scenarioComparisonService.getSelectedElements());
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
				}
				return true;
			}
		} });

		scenarioComparisonService.triggerListener(scenarioComparisonServiceListener);
	}

	@Override
	public void dispose() {
		scenarioComparisonService.removeListener(scenarioComparisonServiceListener);
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

	public void processInputs(final List<Row> result) {
		clearInputEquivalents();
		for (final Row row : result) {
			setInputEquivalents(row, row.getInputEquivalents());
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

		// Create the actual columns instances.
		manager.addColumns(ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID, getBlockManager());
	}

}

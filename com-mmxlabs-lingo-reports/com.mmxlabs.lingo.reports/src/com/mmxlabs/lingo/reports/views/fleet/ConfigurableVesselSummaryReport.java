/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.widgets.grid.DefaultCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.components.GroupAlternatingRowCellRenderer;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableGridReportView;
import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension.InitialDiffOption;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension.InitialRowType;
import com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;

/**
 * A customisable report for fleet based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available for
 * the user to change the default settings.
 */
public class ConfigurableVesselSummaryReport extends AbstractConfigurableGridReportView {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurableVesselSummaryReport.class);

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView";

	private final VesselSummaryReportBuilder builder;

	private boolean diffMode = false;

	private ViewerComparator defaultViewerComparator = null;

	@Inject(optional = true)
	private Iterable<IFleetBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IFleetBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IFleetBasedReportInitialStateExtension> initialStates;

	@Inject
	private ScenarioComparisonService scenarioComparisonService;

	protected ReentrantSelectionManager selectionManager;

	/*
	 * Field to allow subclasses of specific reports to only include visible columns rather than everything
	 */
	protected boolean includeAllColumnsForITS = true;

	@Inject
	public ConfigurableVesselSummaryReport(final VesselSummaryReportBuilder builder) {
		super("com.mmxlabs.lingo.doc.Reports_VesselSummary");

		// Setup the builder hooks.
		this.builder = builder;
		builder.setBlockManager(getBlockManager());
	}

	public boolean toggleDiffMode() {
		diffMode = !diffMode;
		return diffMode;
	}
	
	public boolean isDiffMode() {
		return diffMode;
	}

	public ViewerComparator getGroupDeltaComparator() {
		{

			if (defaultViewerComparator == null) {
				defaultViewerComparator = viewer.getComparator();
			}

			final ViewerComparator vc = defaultViewerComparator;

			// Wrap around with group sorter
			return new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, Object e1, Object e2) {
					RowGroup g1 = null;
					RowGroup g2 = null;

					boolean firstIsComposite = false;
					boolean secondIsComposite = false;

					if (e1 instanceof Row) {
						g1 = ((Row) e1).getRowGroup();
					}
					if (e2 instanceof Row) {
						g2 = ((Row) e2).getRowGroup();
					}

					if (e1 instanceof CompositeRow) {
						final CompositeRow tmp = ((CompositeRow) e1);

						if (tmp.getPreviousRow() != null) {
							g1 = tmp.getPreviousRow().getRowGroup();
							e1 = tmp.getPreviousRow();
						} else if (tmp.getPinnedRow() != null) {
							g1 = tmp.getPinnedRow().getRowGroup();
							e1 = tmp.getPinnedRow();
						}

						firstIsComposite = true;
					}

					if (e2 instanceof CompositeRow) {
						final CompositeRow tmp = ((CompositeRow) e2);

						if (tmp.getPreviousRow() != null) {
							g2 = tmp.getPreviousRow().getRowGroup();
							e2 = tmp.getPreviousRow();
						} else if (tmp.getPinnedRow() != null) {
							g2 = tmp.getPinnedRow().getRowGroup();
							e2 = tmp.getPinnedRow();
						}

						secondIsComposite = true;
					}
					/**
					 * FM This will change the sorting order and will display the Total Delta at the top of the View Changed Integer.MAX_VALUE to -1.
					 */
					if (e1 instanceof List) {
						return -1;
					}
					// FM Changed Integer.MIN_VALUE to 1.
					if (e2 instanceof List) {
						return 1;
					}

					if (g1 == g2 && g1 != null) {
						final int res = vc.compare(viewer, e1, e2);

						if (e1 instanceof Row && e2 instanceof Row) {
							final Row r1 = (Row) e1;
							final Row r2 = (Row) e2;

							if (!(r1.isReference() && r2.isReference())) {
								if (r1.isReference()) {
									return -1;
								} else if (r2.isReference()) {
									return 1;
								}
							}

							if (!(firstIsComposite && secondIsComposite)) {
								if (firstIsComposite) {
									return 1;
								} else if (secondIsComposite) {
									return -1;
								}
							}
						}
						return res;
					} else {
						final Object rd1 = (g1 == null || g1.getRows().isEmpty()) ? e1 : g1.getRows().get(0);
						final Object rd2 = (g2 == null || g2.getRows().isEmpty()) ? e2 : g2.getRows().get(0);
						return vc.compare(viewer, rd1, rd2);
					}
				}
			};
		}

	}

	public void setDiffMode(final boolean enabled) {

		if (enabled) {
			final GridColumn[] columns = viewer.getGrid().getColumns();
			for (final GridColumn column : columns) {
				column.setCellRenderer(new GroupAlternatingRowCellRenderer());
			}
			viewer.setComparator(getGroupDeltaComparator());
		} else {
			final GridColumn[] columns = viewer.getGrid().getColumns();
			for (final GridColumn column : columns) {
				column.setCellRenderer(new DefaultCellRenderer());
				if (defaultViewerComparator != null) {
					viewer.setComparator(defaultViewerComparator);
				}
			}
		}
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			final ColumnBlock[] initialReverseSortOrder = { //
					getBlockManager().getBlockByID("com.mmxlabs.lingo.reports.components.columns.fleet.vessel") //
			};

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
							sortingSupport.sortColumnsBy(handler.column.getColumn(), false);
						}
					}
				}
			}

			return adapter.cast(ReportContentsGenerators.createContentsFor(selectedScenariosServiceListener, viewer.getGrid()));
		}

		return super.getAdapter(adapter);
	}

	/**
	 * Check the view extension point to see if we can enable the customise dialog
	 * 
	 * @return
	 */
	@Override
	protected boolean checkCustomisable() {

		if (initialStates != null) {

			for (final IFleetBasedReportInitialStateExtension ext : initialStates) {

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

	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectedDataProviderChanged(@NonNull final ISelectedDataProvider selectedDataProvider, final boolean block) {

			final TransformedSelectedDataProvider newSelectedDataProvider = new TransformedSelectedDataProvider(selectedDataProvider);
			final Runnable r = () -> {
				setCurrentSelectedDataProvider(newSelectedDataProvider);

				final boolean isMultiple = newSelectedDataProvider.getAllScenarioResults().size() > 1;
				final boolean inPinDiffMode = newSelectedDataProvider.inPinDiffMode();

				for (final ColumnBlock handler : builder.getBlockManager().getBlocksInVisibleOrder()) {
					if (handler != null) {
						handler.setViewState(isMultiple, inPinDiffMode);
					}
				}
				final VesselSummaryReportTransformer transformer = new VesselSummaryReportTransformer(builder);
				final List<Object> rows;
				if (inPinDiffMode) {
					rows = transformer.generatePinDiffRows(selectedDataProvider.getPinnedScenarioResult(), selectedDataProvider.getOtherScenarioResults().get(0));
				} else {
					rows = transformer.generateSimpleRows(selectedDataProvider.getAllScenarioResults());
				}

				setDiffMode(inPinDiffMode);
				ViewerHelper.setInput(viewer, true, rows);
				viewer.setSelection(SelectionHelper.adaptSelection(newSelectedDataProvider.getSelectedObjects()));
			};
			ViewerHelper.runIfViewerValid(viewer, block, r);
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			ViewerHelper.refresh(viewer, true);
		}

		@Override
		public void selectedObjectChanged(final MPart source, final ISelection selection) {
			selectionManager.setViewerSelection(selection, false);
			ViewerHelper.refresh(viewer, false);
		}
	};

	@Override
	public void initPartControl(final Composite parent) {

		final IEclipseContext e4Context = getSite().getService(IEclipseContext.class);
		this.scenarioComparisonService = e4Context.getActive(ScenarioComparisonService.class);

		super.initPartControl(parent);

		// Add diff button
		final Action action = new DiffAction(viewer, this);
		final IActionBars actionBars = getViewSite().getActionBars();
		final IToolBarManager toolBar = actionBars.getToolBarManager();
		toolBar.add(action);

		viewer.setContentProvider(new ArrayContentProvider());

		// Add a filter to only show certain rows.
		viewer.setFilters(super.filterSupport.createViewerFilter(), new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				Collection<Object> selectedElements = currentSelectedDataProvider.getChangeSetSelection();
				if (selectedElements == null) {
					selectedElements = Collections.emptyList();
				}

				if (scenarioComparisonService.getDiffOptions().isFilterSelectedSequences() && !selectedElements.isEmpty()) {

					if (element instanceof CompositeRow) {
						{
							final Row row = ((CompositeRow) element).getPreviousRow();
							if (row != null) {
								final Sequence sequence = row.getSequence();
								if (checkVesselNameAndCharterNumberEquality(sequence, selectedElements)) {
									return true;
								}

								if (selectedElements.contains(sequence)) {
									return true;
								}
							}

						}
						{
							final Row row = ((CompositeRow) element).getPinnedRow();
							if (row != null) {
								final Sequence sequence = row.getSequence();
								if (checkVesselNameAndCharterNumberEquality(sequence, selectedElements)) {
									return true;
								}

								if (selectedElements.contains(sequence)) {
									return true;
								}
							}

						}
						return false;
					} else if (element instanceof Row row) {

						if (diffMode) {
							return false;
						}
						//

						final Sequence sequence = row.getSequence();
						//
						if (checkVesselNameAndCharterNumberEquality(sequence, selectedElements)) {
							return true;
						}

						if (selectedElements.contains(sequence)) {
							return true;
						}

						return false;
					}
				}

				if (element instanceof Row row) {
					//
					if (diffMode) {
						return false;
					}
					//
					// Filter out reference scenario if required
					if (!builder.getDiffFilterInfo().contains(AbstractReportBuilder.DIFF_FILTER_PINNDED_SCENARIO.id)) {
						if (row.isReference()) {
							return false;
						}
					}
					// Only show visible rows
					return true;
				}

				if (element instanceof CompositeRow) {
					{
						final Row row = ((CompositeRow) element).getPreviousRow();
						if (row != null) {
							final Sequence sequence = row.getSequence();
							if (selectedElements.contains(sequence)) {
								return true;
							}
						}
					}
					{
						final Row row = ((CompositeRow) element).getPinnedRow();
						if (row != null) {
							final Sequence sequence = row.getSequence();
							if (selectedElements.contains(sequence)) {
								return true;
							}
						}
					}

					return false;

				}

				return true;
			}
		});

		// // Try and set initial selection. Do not abort on exceptions
		selectionManager = new ReentrantSelectionManager(viewer, selectedScenariosServiceListener, scenarioComparisonService, false);
		try {
			scenarioComparisonService.triggerListener(selectedScenariosServiceListener, false);
		} catch (Exception e) {
			// Ignore any initial issues.
		}
	}

	public void triggerSeletedScenariosServiceListener() {
		scenarioComparisonService.triggerListener(selectedScenariosServiceListener, false);
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
	 * Examine the view extension point to determine the default set of columns, order,row types and diff options.
	 */
	@Override
	protected void setInitialState() {

		if (initialStates != null) {

			for (final IFleetBasedReportInitialStateExtension ext : initialStates) {

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
								case "timecharter":
									rowFilter.add(VesselSummaryReportBuilder.ROW_FILTER_TIME_CHARTERS.id);
									break;
								case "spotcharter":
									rowFilter.add(VesselSummaryReportBuilder.ROW_FILTER_SPOT_CHARTER_INS.id);
									break;
								case "owned":
									rowFilter.add(VesselSummaryReportBuilder.ROW_FILTER_OWNED.id);
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
		dialog.addCheckBoxInfo("Show rows for", builder.ROW_FILTER_ALL, builder::getRowFilterInfo);
		dialog.addCheckBoxInfo("In diff mode", builder.DIFF_FILTER_ALL, builder::getDiffFilterInfo);
	}

	@Override
	protected void postDialogOpen(final ColumnConfigurationDialog dialog) {
		builder.refreshDiffOptions();
		// Update options state
		// table.getOptions().setShowPinnedScenario(!builder.getDiffFilterInfo().contains(ScheduleBasedReportBuilder.DIFF_FILTER_PINNDED_SCENARIO));

	}

	/**
	 * Examine the eclipse registry for defined columns for this report and hook them in.
	 */
	@Override
	protected void registerReportColumns() {

		final EMFReportColumnManager manager = new EMFReportColumnManager();

		// Find any shared column factories and install.
		final Map<String, IVesselSummaryColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final IFleetBasedColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		// Now find the column definitions themselves.
		if (columnExtensions != null) {

			for (final IFleetBasedColumnExtension ext : columnExtensions) {
				IVesselSummaryColumnFactory factory;
				if (ext.getHandlerID() != null) {
					factory = handlerMap.get(ext.getHandlerID());
				} else {
					factory = ext.createFactory();
				}
				if (factory != null) {
					factory.registerColumn(ext.getColumnID(), manager, builder);
				}
			}
		}

		// Create the actual columns instances.
		manager.addColumns(VesselSummaryReportBuilder.VESSEL_SUMMARY_REPORT_TYPE_ID, getBlockManager());
	}

	// private void createDeltaRowGroup(List<CompositeRow> compositeRows) {
	// for (CompositeRow compositeRow : compositeRows) {
	// RowGroup rowGroup = ScheduleReportFactory.eINSTANCE.createRowGroup();
	//
	// Row previousRow = compositeRow.getPreviousRow();
	// Row pinnedRow = compositeRow.getPinnedRow();
	//
	// if (previousRow != null) {
	// previousRow.setRowGroup(rowGroup);
	// }
	//
	// if (pinnedRow != null) {
	// pinnedRow.setRowGroup(rowGroup);
	// }
	// }
	// }
	//
	private boolean checkVesselNameAndCharterNumberEquality(final Sequence sequence, final Collection<Object> selectedElement) {
		final Pair<String, Integer> v1 = getVesselNameAndCharterNumber(sequence);
		if (v1 != null) {
			for (final Object object : selectedElement) {
				if (object instanceof Sequence) {
					final Sequence selectedSequence = (Sequence) object;
					final Pair<String, Integer> v2 = getVesselNameAndCharterNumber(selectedSequence);
					if (v2 != null) {
						return v1.getFirst().equals(v2.getFirst()) && v1.getSecond().equals(v2.getSecond());
					}
				}
			}
		}
		return false;
	}

	private Pair<String, Integer> getVesselNameAndCharterNumber(final Sequence sequence) {
		if (sequence != null) {
			final VesselCharter vesselCharter = sequence.getVesselCharter();
			if (vesselCharter != null) {
				final com.mmxlabs.models.lng.fleet.Vessel vessel = vesselCharter.getVessel();
				if (vessel != null) {
					return Pair.of(vessel.getName(), vesselCharter.getCharterNumber());
				}
			} else {
				final CharterInMarket charterInMarket = sequence.getCharterInMarket();
				if (charterInMarket != null) {
					return Pair.of(charterInMarket.getName(), sequence.getSpotIndex());
				}
			}
		}
		return null;
	}

	@Override
	protected boolean refreshOnSelectionChange() {
		return true;
	}

	@Override
	protected DiffingGridTableViewerColumnFactory createColumnFactory() {
		return new DiffingGridTableViewerColumnFactory(viewer, sortingSupport, filterSupport, () -> copyPasteMode, this::applyColour);
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.GroupAlternatingRowCellRenderer;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
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
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * A customisable report for fleet based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available for
 * the user to change the default settings.
 */
public class ConfigurableFleetReportView extends AbstractConfigurableGridReportView {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurableFleetReportView.class);

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView";

	private final FleetBasedReportBuilder builder;

	private boolean diffMode = false;

	private ViewerComparator defaultViewerComparator = null;

	@Inject(optional = true)
	private Iterable<IFleetBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IFleetBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IFleetBasedReportInitialStateExtension> initialStates;

	@Inject
	@NonNull
	private ScenarioComparisonService scenarioComparisonService;

	@Inject
	@NonNull
	private SelectedScenariosService selectedScenariosService;

	@Inject
	public ConfigurableFleetReportView(final FleetBasedReportBuilder builder) {
		super("com.mmxlabs.lingo.doc.Reports_VesselSummary");

		// Setup the builder hooks.
		this.builder = builder;
		builder.setBlockManager(getBlockManager());
	}

	public void toggleDiffMode() {
		diffMode = !diffMode;
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

					Boolean firstIsComposite = false;
					Boolean secondIsComposite = false;

					if (e1 instanceof Row) {
						g1 = ((Row) e1).getRowGroup();
					}
					if (e2 instanceof Row) {
						g2 = ((Row) e2).getRowGroup();
					}

					if (e1 instanceof CompositeRow) {
						CompositeRow tmp = ((CompositeRow) e1);

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
						CompositeRow tmp = ((CompositeRow) e2);

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
						int res = vc.compare(viewer, e1, e2);

						if (e1 instanceof Row && e2 instanceof Row) {
							Row r1 = (Row) e1;
							Row r2 = (Row) e2;

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

	public void setDiffMode(boolean enabled) {

		if (enabled) {
			GridColumn[] columns = viewer.getGrid().getColumns();
			for (GridColumn column : columns) {
				column.setCellRenderer(new GroupAlternatingRowCellRenderer());
			}
			viewer.setComparator(getGroupDeltaComparator());
		} else {
			GridColumn[] columns = viewer.getGrid().getColumns();
			for (GridColumn column : columns) {
				column.setCellRenderer(new DefaultCellRenderer());
				if (defaultViewerComparator != null) {
					viewer.setComparator(defaultViewerComparator);
				}
			}
		}
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		// if (Table.class.isAssignableFrom(adapter)) {
		// final Object input = viewer.getInput();
		// if (input instanceof IEMFObservable) {
		// final IEMFObservable observable = (IEMFObservable) input;
		// return observable.getObserved();
		// }
		// return input;
		// }

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getHTMLContents() {
					return contents;
				}
			};
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

	private final IScenarioComparisonServiceListener scenarioComparisonServiceListener = new IScenarioComparisonServiceListener() {

		@Override
		public void compareDataUpdate(@NonNull final ISelectedDataProvider selectedDataProvider, @NonNull final ScenarioResult pin, @NonNull final ScenarioResult other, @NonNull final Table table,
				@NonNull final List<LNGScenarioModel> rootObjects, @NonNull final Map<EObject, Set<EObject>> equivalancesMap) {
			ViewerHelper.refresh(viewer, true);
		}

		@Override
		public void multiDataUpdate(@NonNull final ISelectedDataProvider selectedDataProvider, @NonNull final Collection<ScenarioResult> others, @NonNull final Table table,
				@NonNull final List<LNGScenarioModel> rootObjects) {
			ViewerHelper.refresh(viewer, true);
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			ViewerHelper.refresh(viewer, true);
		}

	};

	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {

			final Runnable r = new Runnable() {
				@Override
				public void run() {
					final Table table = ScheduleReportFactory.eINSTANCE.createTable();

					final FleetReportTransformer transformer = new FleetReportTransformer(table, builder);
					final IScenarioInstanceElementCollector elementCollector = transformer.getElementCollector(ConfigurableFleetReportView.this);

					elementCollector.beginCollecting(pinned != null);
					if (pinned != null) {
						elementCollector.collectElements(pinned, true);
					}
					for (final ScenarioResult other : others) {
						elementCollector.collectElements(other, false);
					}
					elementCollector.endCollecting();

					setCurrentSelectedDataProvider(new TransformedSelectedDataProvider(selectedDataProvider));

					int numberOfRow = table.getRows().size();
					if (pinned != null) {
						numberOfRow += table.getCompositeRows().size();// + 1; FM removed extra row
					}

					List<Object> rows = new ArrayList<>(numberOfRow);

					if ((pinned != null && !diffMode) || (pinned == null)) {
						rows.addAll(table.getRows());
					}

					if (pinned != null) {

						// List<CompositeRow> compositeRows = new ArrayList<>(table.getCompositeRows());
						List<CompositeRow> compositeRows = new ArrayList<>(table.getCompositeRowsWithPartials());

						createDeltaRowGroup(compositeRows);

						rows.addAll(compositeRows);
						rows.add(compositeRows);

						if (diffMode) {
							setDiffMode(false);
						} else {
							setDiffMode(true);
						}
					} else {
						setDiffMode(false);
					}

					ViewerHelper.setInput(viewer, true, rows);
				}
			};

			RunnerHelper.exec(r, block);
		}
	};

	@Override
	public void initPartControl(final Composite parent) {

		super.initPartControl(parent);

		scenarioComparisonService.addListener(scenarioComparisonServiceListener);
		selectedScenariosService.addListener(selectedScenariosServiceListener);

		// Add diff button
		Action action = new DiffAction(viewer, this);
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBar = actionBars.getToolBarManager();
		toolBar.add(action);

		viewer.setContentProvider(new ArrayContentProvider());

		// Add a filter to only show certain rows.
		viewer.setFilters(super.filterSupport.createViewerFilter(), new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (scenarioComparisonService.getDiffOptions().isFilterSelectedSequences() && !scenarioComparisonService.getSelectedElements().isEmpty()) {

					if (element instanceof CompositeRow) {
						final Row row = ((CompositeRow) element).getPreviousRow();
						if (row != null) {
							final Sequence sequence = row.getSequence();
							if (checkVesselNameEquality(sequence, scenarioComparisonService.getSelectedElements())) {
								return row.isVisible();
							}

							if (!scenarioComparisonService.getSelectedElements().contains(row.getSequence())) {
								return false;
							}
						}
					} else {
						// What should we do if the row is null?
					}

					if (element instanceof Row) {
						final Row row = (Row) element;
						final Sequence sequence = row.getSequence();

						if (checkVesselNameEquality(sequence, scenarioComparisonService.getSelectedElements())) {
							return row.isVisible();
						}

						if (!scenarioComparisonService.getSelectedElements().contains(row.getSequence())) {
							return false;
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
					// Only show visible rows
					return row.isVisible();
				}

				if (element instanceof CompositeRow) {
					Row row = ((CompositeRow) element).getPreviousRow();
					if (row == null) {
						row = ((CompositeRow) element).getPinnedRow();
					}

					if (row != null) {
						return row.isVisible();
					}
				}

				return true;
			}
		});

		// Try and set initial selection. Do not abort on exceptions
		try {
			selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void triggerSeletedScenariosServiceListener() {
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	@Override
	public void dispose() {
		selectedScenariosService.removeListener(selectedScenariosServiceListener);
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
									rowFilter.add(FleetBasedReportBuilder.ROW_FILTER_TIME_CHARTERS.id);
									break;
								case "spotcharter":
									rowFilter.add(FleetBasedReportBuilder.ROW_FILTER_SPOT_CHARTER_INS.id);
									break;
								case "owned":
									rowFilter.add(FleetBasedReportBuilder.ROW_FILTER_OWNED.id);
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
		dialog.addCheckBoxInfo("Show rows for", builder.ROW_FILTER_ALL, () -> builder.getRowFilterInfo());
		dialog.addCheckBoxInfo("In diff mode", builder.DIFF_FILTER_ALL, () -> builder.getDiffFilterInfo());
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
		final Map<String, IFleetColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final IFleetBasedColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		// Now find the column definitions themselves.
		if (columnExtensions != null) {

			for (final IFleetBasedColumnExtension ext : columnExtensions) {
				IFleetColumnFactory factory;
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
		manager.addColumns(FleetBasedReportBuilder.FLEET_REPORT_TYPE_ID, getBlockManager());
	}

	private void createDeltaRowGroup(List<CompositeRow> compositeRows) {
		for (CompositeRow compositeRow : compositeRows) {
			RowGroup rowGroup = ScheduleReportFactory.eINSTANCE.createRowGroup();

			Row previousRow = compositeRow.getPreviousRow();
			Row pinnedRow = compositeRow.getPinnedRow();

			if (previousRow != null) {
				previousRow.setRowGroup(rowGroup);
			}

			if (pinnedRow != null) {
				pinnedRow.setRowGroup(rowGroup);
			}
		}
	}

	private boolean checkVesselNameEquality(Sequence sequence, Collection<Object> selectedElement) {
		String name1 = getVesselName(sequence);
		if (name1 != null) {
			for (Object object : selectedElement) {
				if (object instanceof Sequence) {
					Sequence selectedSequence = (Sequence) object;
					String name2 = getVesselName(selectedSequence);
					if (name2 != null) {
						return name1.equals(name2);
					}
				}
			}
		}
		return false;
	}

	private String getVesselName(Sequence sequence) {
		if (sequence != null) {
			final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
			if (vesselAvailability != null) {
				final com.mmxlabs.models.lng.fleet.Vessel vessel = vesselAvailability.getVessel();
				if (vessel != null) {
					final String vesselName = vessel.getName();
					return vesselName;
				}
			}
		}
		return null;
	}
}

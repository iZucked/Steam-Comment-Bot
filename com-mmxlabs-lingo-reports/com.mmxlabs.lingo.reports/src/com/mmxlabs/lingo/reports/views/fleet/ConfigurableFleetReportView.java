/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableGridReportView;
import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension.InitialDiffOption;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.IFleetBasedReportInitialStateExtension.InitialRowType;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * A customisable report for fleet based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available for
 * the user to change the default settings.
 */
public class ConfigurableFleetReportView extends AbstractConfigurableGridReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView";

	private final FleetBasedReportBuilder builder;

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
				public String getStringContents() {
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
		public void compareDataUpdate(@NonNull final ISelectedDataProvider selectedDataProvider, @NonNull final ScenarioInstance pin, @NonNull final ScenarioInstance other, @NonNull final Table table,
				@NonNull final List<LNGScenarioModel> rootObjects, @NonNull final Map<EObject, Set<EObject>> equivalancesMap) {
			ViewerHelper.refresh(viewer, true);
		}

		@Override
		public void multiDataUpdate(@NonNull final ISelectedDataProvider selectedDataProvider, @NonNull final Collection<ScenarioInstance> others, @NonNull final Table table,
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
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioInstance pinned, final Collection<ScenarioInstance> others, final boolean block) {

			final Runnable r = new Runnable() {
				@Override
				public void run() {
					final Table table = ScheduleReportFactory.eINSTANCE.createTable();

					final FleetReportTransformer transformer = new FleetReportTransformer(table, builder);
					final IScenarioInstanceElementCollector elementCollector = transformer.getElementCollector(ConfigurableFleetReportView.this);

					elementCollector.beginCollecting(pinned != null);
					if (pinned != null) {
						elementCollector.collectElements(pinned, (LNGScenarioModel) pinned.getInstance(), true);
					}
					for (final ScenarioInstance other : others) {
						elementCollector.collectElements(other, (LNGScenarioModel) other.getInstance(), false);
					}
					elementCollector.endCollecting();
					ViewerHelper.setInput(viewer, true, table.getRows());
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

		viewer.setContentProvider(new ArrayContentProvider());

		// Add a filter to only show certain rows.
		viewer.setFilters(new ViewerFilter[] { super.filterSupport.createViewerFilter(), new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (scenarioComparisonService.getDiffOptions().isFilterSelectedSequences() && !scenarioComparisonService.getSelectedElements().isEmpty()) {
					if (element instanceof Row) {
						final Row row = (Row) element;
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
				return true;
			}
		} });

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
		dialog.addCheckBoxInfo("Show rows for", builder.ROW_FILTER_ALL, builder.getRowFilterInfo());
		dialog.addCheckBoxInfo("In diff mode", builder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
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
}

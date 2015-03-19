/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.IEMFObservable;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
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
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

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

	private FleetReportTransformer transformer;

	// New diff stuff
	private IPartListener listener;
	private static final String SCHEDULE_VIEW_ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";
	private IViewPart scheduleView;
	private Table scheduleReportTable;

	@Inject
	public ConfigurableFleetReportView(final FleetBasedReportBuilder builder) {
		super(ID);

		// Setup the builder hooks.
		this.builder = builder;
		builder.setBlockManager(getBlockManager());
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {
		if (Table.class.isAssignableFrom(adapter)) {
			final Object input = viewer.getInput();
			if (input instanceof IEMFObservable) {
				final IEMFObservable observable = (IEMFObservable) input;
				return observable.getObserved();
			}
			return input;
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

	@Override
	public void initPartControl(final Composite parent) {
		super.initPartControl(parent);

		// Add a filter to only show certain rows.

		viewer.setFilters(new ViewerFilter[] { super.filterSupport.createViewerFilter(), new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (scheduleReportTable != null && scheduleReportTable.getOptions().isFilterSelectedSequences() && !scheduleReportTable.getSelectedElements().isEmpty()) {
					if (element instanceof Row) {
						Row row = (Row) element;
						if (!scheduleReportTable.getSelectedElements().contains(row.getSequence())) {
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

		hookToScheduleView();

	}

	@Override
	public void dispose() {

		if (scheduleReportTable != null) {
			scheduleReportTable.eAdapters().remove(adapter);
		}

		if (listener != null) {
			getViewSite().getPage().removePartListener(listener);
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
		for (final Row row : result) {
			setInputEquivalents(row, row.getInputEquivalents());
		}
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {

		transformer = new FleetReportTransformer(table, builder);
		return transformer.getElementCollector(this);
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

	// not always disposed
	private final EContentAdapter adapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (notification.getFeature() == ScheduleReportPackage.Literals.DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES) {
				// viewer.setSelection(viewer.getSelection());
				viewer.refresh();
			}
			if (notification.getFeature() == ScheduleReportPackage.Literals.TABLE__SELECTED_ELEMENTS) {

				// Copy across data
				table.getSelectedElements().clear();
				table.getSelectedElements().addAll(scheduleReportTable.getSelectedElements());
				// viewer.setSelection(viewer.getSelection());
				viewer.refresh();
			}
		}
	};

	protected void hookToScheduleView() {
		listener = new IPartListener() {

			@Override
			public void partOpened(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = viewPart;
						observeInput((Table) scheduleView.getAdapter(Table.class));
					}
				}
			}

			private void observeInput(final Table table) {

				if (ConfigurableFleetReportView.this.scheduleReportTable != null) {
					ConfigurableFleetReportView.this.scheduleReportTable.eAdapters().remove(adapter);
				}
				ConfigurableFleetReportView.this.scheduleReportTable = table;
				if (ConfigurableFleetReportView.this.scheduleReportTable != null) {
					ConfigurableFleetReportView.this.scheduleReportTable.eAdapters().add(adapter);
				}
			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {

			}

			@Override
			public void partClosed(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = null;
						observeInput(null);
					}
				}

			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {

			}

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = viewPart;
						observeInput((Table) scheduleView.getAdapter(Table.class));
					}
				}
			}
		};
		getViewSite().getPage().addPartListener(listener);
		for (final IViewReference view : getViewSite().getPage().getViewReferences()) {
			if (view.getId().equals(SCHEDULE_VIEW_ID)) {
				listener.partOpened(view.getView(false));
			}
		}
	}
}

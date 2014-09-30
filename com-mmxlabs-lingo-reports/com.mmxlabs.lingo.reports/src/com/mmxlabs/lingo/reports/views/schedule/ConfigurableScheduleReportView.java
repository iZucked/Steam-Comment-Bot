/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.IMemento;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableReportView;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialDiffOption;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialRowType;

/**
 * A customisable report for schedule based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available
 * for the user to change the default settings.
 */
public class ConfigurableScheduleReportView extends AbstractConfigurableReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.schedule.ConfigurableScheduleReportView";

	private final ScheduleBasedReportBuilder builder;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedReportInitialStateExtension> initialStates;

	@Inject
	public ConfigurableScheduleReportView(final ScheduleBasedReportBuilder builder) {
		super(ID);

		// Setup the builder hooks.
		this.builder = builder;
		builder.setReport(this);
		builder.setPinDiffModeHelper(pinDiffModeHelper);
	}

	@Override
	public void saveConfigState(final IMemento configMemento) {
		if (configMemento != null) {
			builder.saveToMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
		}
	}

	protected void initConfigMemento(IMemento configMemento) {
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
	protected void setInitialState() {

		if (initialStates != null) {

			for (final IScheduleBasedReportInitialStateExtension ext : initialStates) {

				final String viewId = ext.getViewID();

				// Is this a matching view definition?
				if (viewId != null && viewId.equals(getViewSite().getId())) {
					// Get visibile columns and order
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
						final List<String> rowFilter = new ArrayList<>(ScheduleBasedReportBuilder.ROW_FILTER_ALL.length);
						final InitialRowType[] initialRows = ext.getInitialRows();
						if (initialRows != null) {
							for (final InitialRowType row : initialRows) {

								switch (row.getRowType()) {
								case "cargo":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_CARGO_ROW);
									break;
								case "long":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_LONG_CARGOES);
									break;
								case "short":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_SHORT_CARGOES);
									break;
								case "virtualcharters":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_CHARTER_OUT_ROW);
									break;
								case "event":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_EVENT_ROW);
									break;
								case "orphanlegs":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_START_ROW);
									break;
								}
							}
						}
						builder.setRowFilter(rowFilter.toArray(new String[0]));
					}
					// Get diff options
					{
						final List<String> diffOptions = new ArrayList<>(ScheduleBasedReportBuilder.DIFF_FILTER_ALL.length);
						final InitialDiffOption[] initialDiffOptions = ext.getInitialDiffOptions();
						if (initialDiffOptions != null) {
							for (final InitialDiffOption diffOption : initialDiffOptions) {

								switch (diffOption.getOption()) {

								case "vessel":
									diffOptions.add(ScheduleBasedReportBuilder.DIFF_FILTER_VESSEL_CHANGES);
									break;
								case "scenario":
									diffOptions.add(ScheduleBasedReportBuilder.DIFF_FILTER_PINNDED_SCENARIO);
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

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return builder.createPNLColumnssContentProvider(superProvider);
	}

	@Override
	protected void processInputs(final Object[] result) {
		builder.processInputs(result);
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return builder.getElementCollector();
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	@Override
	public String getElementKey(final EObject element) {
		return builder.getElementKey(element);
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return builder.isElementDifferent(pinnedObject, otherObject);
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

	protected void addDialogCheckBoxes(final ColumnConfigurationDialog dialog) {
		dialog.addCheckBoxInfo("Show rows for", ScheduleBasedReportBuilder.ROW_FILTER_ALL, builder.getRowFilterInfo());
		dialog.addCheckBoxInfo("In diff mode", ScheduleBasedReportBuilder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
	}

	protected void postDialogOpen(final ColumnConfigurationDialog dialog) {
		builder.refreshDiffOptions();

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
		manager.addColumns(ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID, this);
	}

}

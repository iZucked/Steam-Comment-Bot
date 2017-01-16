/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableGridReportView;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * 
 */
public class PortRotationReportView extends AbstractConfigurableGridReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.PortRotationReportView";

	private final PortRotationBasedReportBuilder builder;

	@Inject(optional = true)
	private Iterable<IPortRotationBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IPortRotationBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IPortRotationBasedReportInitialStateExtension> initialStates;

	private PortRotationsReportTransformer transformer;


	private List<Object> elements;

	@Inject
	private SelectedScenariosService selectedScenariosService;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioInstance pinned, final Collection<@NonNull ScenarioInstance> others, final boolean block) {

			ViewerHelper.setInput(viewer, block, () -> {
				elements.clear();
				elementCollector.beginCollecting(pinned != null);
				if (pinned != null) {
					elementCollector.collectElements(pinned, selectedDataProvider.getScenarioModel(pinned), true);
				}
				for (final ScenarioInstance other : others) {
					elementCollector.collectElements(other, selectedDataProvider.getScenarioModel(other), false);
				}
				elementCollector.endCollecting();
				return elements;
			});
		}
	};

	private IScenarioInstanceElementCollector elementCollector;

	@Inject
	public PortRotationReportView(final PortRotationBasedReportBuilder builder) {

		super("com.mmxlabs.lingo.doc.Reports_PortRotationReportView");
		this.builder = builder;
		builder.setBlockManager(getBlockManager());
		builder.setReport(this);

	}

	/**
	 * Check the view extension point to see if we can enable the customise dialog
	 * 
	 * @return
	 */
	@Override
	protected boolean checkCustomisable() {

		if (initialStates != null) {

			for (final IPortRotationBasedReportInitialStateExtension ext : initialStates) {

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

			for (final IPortRotationBasedReportInitialStateExtension ext : initialStates) {

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

					// // Get row types
					// {
					// final List<String> rowFilter = new ArrayList<>(builder.ROW_FILTER_ALL.length);
					// final InitialRowType[] initialRows = ext.getInitialRows();
					// if (initialRows != null) {
					// for (final InitialRowType row : initialRows) {
					//
					// switch (row.getRowType()) {
					// case "spotcharters":
					// rowFilter.add(PortRotationBasedReportBuilder.ROW_FILTER_XXXX.id);
					// break;
					// }
					// }
					// }
					// builder.setRowFilter(rowFilter.toArray(new String[0]));
					// }
					// // Get diff options
					// {
					// final List<String> diffOptions = new ArrayList<>(builder.DIFF_FILTER_ALL.length);
					// final InitialDiffOption[] initialDiffOptions = ext.getInitialDiffOptions();
					// if (initialDiffOptions != null) {
					// for (final InitialDiffOption diffOption : initialDiffOptions) {
					//
					// switch (diffOption.getOption()) {
					//
					// case "scenario":
					// diffOptions.add(AbstractReportBuilder.DIFF_FILTER_PINNDED_SCENARIO.id);
					// break;
					// }
					// }
					// }
					// builder.setDiffFilter(diffOptions.toArray(new String[0]));
					// }
					break;
				}
			}
		}
	}

	public void processInputs(final List<?> result) {
		clearInputEquivalents();
		for (final Object event : result) {
			if (event instanceof SlotVisit) {
				setInputEquivalents(event, Arrays.asList(new Object[] { ((SlotVisit) event).getSlotAllocation().getCargoAllocation() }));
			} else if (event instanceof VesselEventVisit) {
				setInputEquivalents(event, Arrays.asList(new Object[] { ((VesselEventVisit) event).getVesselEvent() }));
			} else {
				setInputEquivalents(event, Collections.emptyList());
			}
		}
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected void addDialogCheckBoxes(final ColumnConfigurationDialog dialog) {
		// dialog.addCheckBoxInfo("Show rows for", builder.ROW_FILTER_ALL, builder.getRowFilterInfo());
		// dialog.addCheckBoxInfo("In diff mode", builder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
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
		final Map<String, IPortRotationColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final IPortRotationBasedColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		// Now find the column definitions themselves.
		if (columnExtensions != null) {

			for (final IPortRotationBasedColumnExtension ext : columnExtensions) {
				IPortRotationColumnFactory factory;
				if (ext.getHandlerID() != null) {
					factory = handlerMap.get(ext.getHandlerID());
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					final String columnID = ext.getColumnID();
					factory.registerColumn(columnID, manager, builder);
				}
			}
		}

		// Create the actual columns instances.
		manager.addColumns(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, getBlockManager());
	}


	@Override
	public void initPartControl(final Composite parent) {
		elements = new LinkedList<Object>();
		transformer = new PortRotationsReportTransformer(builder);
		elementCollector = transformer.getElementCollector(elements, this);

		super.initPartControl(parent);
		final ColumnBlock[] initialReverseSortOrder = { getBlockManager().getBlockByID("com.mmxlabs.lingo.reports.components.columns.portrotation.startdate"),
				getBlockManager().getBlockByID("com.mmxlabs.lingo.reports.components.columns.portrotation.vessel"),
				getBlockManager().getBlockByID("com.mmxlabs.lingo.reports.components.columns.portrotation.schedule") };
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
		viewer.setContentProvider(new ArrayContentProvider());
		ViewerHelper.setInput(viewer, true, elements);

		selectedScenariosService.addListener(selectedScenariosServiceListener);

		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	@Override
	public void dispose() {
		selectedScenariosService.removeListener(selectedScenariosServiceListener);
		super.dispose();
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

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
}

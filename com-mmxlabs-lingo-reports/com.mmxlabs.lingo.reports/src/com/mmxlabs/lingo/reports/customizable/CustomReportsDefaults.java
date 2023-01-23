/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.lingo.reports.components.ReportComponentModule;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.lingo.reports.views.schedule.IScheduleColumnFactory;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnOverrideExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialDiffOption;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialRowType;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.rcp.common.ServiceHelper;

public class CustomReportsDefaults {
	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnExtension> columnExtensions;
	
	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnOverrideExtension> columnOverrideExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedReportInitialStateExtension> initialStates;

	private ColumnBlockManager blockManager = new ColumnBlockManager();

	List<ColumnBlock> columnDefinitions;
	
	List<String> diffOptions;
	
	List<String> rowFilter;
	
	public CustomReportsDefaults() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final Injector injector = Guice.createInjector(Peaberry.osgiModule(bundleContext, eclipseRegistry()), new ReportComponentModule());
		injector.injectMembers(this);
		registerReportColumns();
		setInitialState();
		this.columnDefinitions = blockManager.getBlocksInVisibleOrder();
	}
	
	protected void registerReportColumns() {

		final ScheduleBasedReportBuilder builder = new ScheduleBasedReportBuilder();
		
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
		manager.addColumns(ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID, blockManager);			
	}
			
	ColumnBlockManager getBlockManager() {
		return blockManager;
	}
	
	protected void setInitialState() {
		if (initialStates != null) {
			for (final IScheduleBasedReportInitialStateExtension ext : initialStates) {
				final String viewId = ext.getViewID();

				// Is this a matching view definition?
				if (viewId != null && viewId.equals(ScheduleSummaryReport.ID)) {
					// Get visible columns and order
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
					// Get row types
					rowFilter = new ArrayList<>();
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
					// Get diff options
					diffOptions = new ArrayList<>();
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
					break;
				}
			}
		}
	}

	public List<ColumnBlock> getColumnDefinitions() {
		return this.columnDefinitions;
	}
	
	public List<String> getDiffOptions() {
		return this.diffOptions;
	}
	
	public List<String> getRowFilters() {
		return this.rowFilter;
	}
	
	public boolean isVisible(ColumnBlock column) {
		return blockManager.getBlockVisible(column);
	}
}
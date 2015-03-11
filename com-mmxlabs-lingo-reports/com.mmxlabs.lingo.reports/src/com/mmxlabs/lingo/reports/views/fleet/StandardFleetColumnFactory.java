/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import static com.mmxlabs.lingo.reports.views.fleet.FleetBasedReportBuilder.FLEET_REPORT_TYPE_ID;

import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnBlockManager;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.components.SimpleEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.fleet.formatters.BaseFuelCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.CanalCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.GeneratedCharterDaysFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.GeneratedCharterRevenueFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.LNGCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.PortCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.VesselFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.provider.PinnedScheduleFormatter;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class StandardFleetColumnFactory implements IFleetColumnFactory {

	private static final String COLUMN_BLOCK_GCO_DELTA_VESSEL = "com.mmxlabs.lingo.reports.components.columns.fleet.diff_gco";
	private static final String COLUMN_BLOCK_GCO_DELTA_SELECTION = "com.mmxlabs.lingo.reports.components.columns.fleet.diff_selection_gco";

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final FleetBasedReportBuilder builder) {

		final Image pinImage = Activator.getDefault().getImageRegistry().get(Activator.Implementation.IMAGE_PINNED_ROW);

		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.fleet.schedule":
			final PinnedScheduleFormatter formatter = new PinnedScheduleFormatter(pinImage);
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Scenario", "The scenario name. Only shown when multiple scenarios are selected",
					ColumnType.MULTIPLE, formatter, ScheduleReportPackage.Literals.ROW__SCENARIO, ScenarioServicePackage.eINSTANCE.getContainer_Name()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.vessel":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Vessel", "Vessel name", ColumnType.NORMAL, new VesselFormatter(),
					ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.canalcosts":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Route Costs", "Total route costs", ColumnType.NORMAL, new CanalCostFormatter(true),
					ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.fleet.lngcosts":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "LNG Costs", "Total LNG BOG costs", ColumnType.NORMAL, new LNGCostFormatter(true),
					ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.fleet.basefuelcosts":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Base Fuel Costs", "Total base fuel costs", ColumnType.NORMAL,
					new BaseFuelCostFormatter(true), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.fleet.portcosts":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Port Costs", "Total port costs", ColumnType.NORMAL, new PortCostFormatter(true),
					ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.chartercosts":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Charter Costs", "Total chartering costs", ColumnType.NORMAL, new PortCostFormatter(true),
					ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.gco_days":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "GCO Days", "Total generated charter out days", ColumnType.NORMAL,
					new GeneratedCharterDaysFormatter(false, false)));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.gco_revenue":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "GCO Revenue", "Total generated charter out revenue", ColumnType.NORMAL,
					new GeneratedCharterRevenueFormatter(true, false, false)));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.diff_gco": {

			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_GCO_DELTA_VESSEL);
					if (block == null) {
						block = blockManager.createBlock(COLUMN_BLOCK_GCO_DELTA_VESSEL, "GCO delta", ColumnType.DIFF);
					}
					block.setPlaceholder(true);

					blockManager.createColumn(block, "Days", new GeneratedCharterDaysFormatter(true, false));
					blockManager.createColumn(block, "Revenue", new GeneratedCharterRevenueFormatter(true, true, false));

					return null;
				}
			});
		}
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.diff_selection_gco":
			columnManager.registerColumn(FLEET_REPORT_TYPE_ID, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_GCO_DELTA_SELECTION);
					if (block == null) {
						block = blockManager.createBlock(COLUMN_BLOCK_GCO_DELTA_SELECTION, "GCO delta (change set)", ColumnType.DIFF);
					}
					block.setPlaceholder(true);

					blockManager.createColumn(block, "Days", new GeneratedCharterDaysFormatter(true, true));
					blockManager.createColumn(block, "Revenue", new GeneratedCharterRevenueFormatter(true, true, true));

					return null;
				}
			});
		}
	}
}

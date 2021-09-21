/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import static com.mmxlabs.lingo.reports.views.fleet.VesselSummaryReportBuilder.VESSEL_SUMMARY_REPORT_TYPE_ID;

import org.eclipse.swt.graphics.Image;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.PinnedScheduleFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.BallastBonusFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.BaseFuelCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.CanalCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.CharterCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.CharterLengthDaysFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.CooldownCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.GeneratedCharterDaysFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.GeneratedCharterRevenueFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.HeelCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.HeelRevenueFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.LNGCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.NOfCargoesFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.PortCostFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.RepositioningFeeFormatter;
import com.mmxlabs.lingo.reports.views.fleet.formatters.TotalWithBOGFormatter;
import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.formatters.ICostTypeFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.models.ui.tabular.columngeneration.EmfBlockColumnFactory;
import com.mmxlabs.models.ui.tabular.columngeneration.SimpleEmfBlockColumnFactory;

public class StandardVesselSummaryColumnFactory implements IVesselSummaryColumnFactory {

	private static final String COLUMN_BLOCK_GCO_VESSEL = "com.mmxlabs.lingo.reports.components.columns.fleet.gco";

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final VesselSummaryReportBuilder builder) {

		final Image pinImage = Activator.getDefault().getImageRegistry().get(Activator.Implementation.IMAGE_PINNED_ROW);

		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.fleet.schedule":
			final PinnedScheduleFormatter formatter = new PinnedScheduleFormatter(pinImage);
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Scenario", "The scenario name. Only shown when multiple scenarios are selected",
					ColumnType.MULTIPLE, formatter, ScheduleReportPackage.Literals.ROW__SCENARIO_NAME));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.vessel":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Vessel", "Vessel name", ColumnType.NORMAL, new VesselAssignmentFormatter(), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.canalcosts":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Route ($)", "Total route costs", ColumnType.NORMAL,
					new CanalCostFormatter(ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.fleet.lngcosts":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "LNG ($)", "Total LNG BOG costs", ColumnType.NORMAL,
					new LNGCostFormatter(ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.fleet.basefuelcosts":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Base Fuel ($)", "Total base fuel costs", ColumnType.NORMAL,
					new BaseFuelCostFormatter(ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.fleet.portcosts":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Port ($)", "Total port costs", ColumnType.NORMAL,
					new PortCostFormatter(ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.chartercosts":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Charter ($)", "Total chartering costs", ColumnType.NORMAL,
					new CharterCostFormatter(false, CostFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.heel_revenue":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Heel Revenue ($)", null, ColumnType.NORMAL,
					new HeelRevenueFormatter(false, ICostTypeFormatter.Type.REVENUE), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.heel_cost":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Heel Cost ($)", null, ColumnType.NORMAL,
					new HeelCostFormatter(false, ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.ballast_bonus":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast bonus ($)", null, ColumnType.NORMAL,
					new BallastBonusFormatter(false, CostFormatter.Type.REVENUE), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.repositioning_fee":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Repositioning fee ($)", null, ColumnType.NORMAL,
					new RepositioningFeeFormatter(false, ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.number_cargoes":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "#Cargoes", null, ColumnType.NORMAL, new NOfCargoesFormatter(), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.total_without_BOG":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sum - BOG($)", null, ColumnType.NORMAL,
					new TotalWithBOGFormatter(false, false, ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break; // Was Total cost before
		case "com.mmxlabs.lingo.reports.components.columns.fleet.total_with_BOG":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sum ($)", null, ColumnType.NORMAL,
					new TotalWithBOGFormatter(false, true, ICostTypeFormatter.Type.COST), ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break; // Was Total cost BOG
		case "com.mmxlabs.lingo.reports.components.columns.fleet.cooldowncosts":
			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Cooldown ($)", "Total cooldown costs", ColumnType.NORMAL, new CooldownCostFormatter(false),
					ScheduleReportPackage.Literals.ROW__SEQUENCE));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.fleet.charterlength":
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CHARTER_LENGTH)) {
				columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID,
						new SimpleEmfBlockColumnFactory(columnID, "Length", "Total Charter length in days", ColumnType.NORMAL, new CharterLengthDaysFormatter()));
			}
			break;
		case COLUMN_BLOCK_GCO_VESSEL: {

			columnManager.registerColumn(VESSEL_SUMMARY_REPORT_TYPE_ID, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_GCO_VESSEL);
					if (block == null) {
						block = blockManager.createBlock(COLUMN_BLOCK_GCO_VESSEL, "Charter Out (virtual)", ColumnType.NORMAL);
					}
					block.setPlaceholder(true);
					block.setForceGroup(true);

					blockManager.createColumn(block, "Days", new GeneratedCharterDaysFormatter());
					blockManager.createColumn(block, "Revenue", new GeneratedCharterRevenueFormatter(ICostTypeFormatter.Type.REVENUE));

					return null;
				}
			});
		}
			break;
		}
	}
}

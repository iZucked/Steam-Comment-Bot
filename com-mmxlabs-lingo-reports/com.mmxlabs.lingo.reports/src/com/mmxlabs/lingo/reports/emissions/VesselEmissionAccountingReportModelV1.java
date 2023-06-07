/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

/**
 * Emissions report data
 */
@SchemaVersion(1)
public class VesselEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel {
	
	@ColumnName("Base Fuel")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long baseFuelEmission;
	
	@ColumnName("BOG")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long bogEmission;
	
	@ColumnName("Pilot Light")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long pilotLightEmission;
	
	@LingoIgnore
	@ColumnName("CII")
	@ColumnOrderLevel(ColumnOrder.LATER_LEVEL)
	public Long attainedCII;
}

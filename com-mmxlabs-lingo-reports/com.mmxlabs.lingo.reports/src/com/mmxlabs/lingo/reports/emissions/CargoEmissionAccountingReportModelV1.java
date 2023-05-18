/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

/**
 * Cargo emissions report data
 * YES it looks exactly the same as Vessel emissions report model
 * But it will most probably be changed
 */
@SchemaVersion(1)
public class CargoEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel {
	
	@ColumnName("Base Fuel")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long baseFuelEmission;
	
	@ColumnName("BOG")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long bogEmission;
	
	@ColumnName("Pilot Light")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long pilotLightEmission;
}

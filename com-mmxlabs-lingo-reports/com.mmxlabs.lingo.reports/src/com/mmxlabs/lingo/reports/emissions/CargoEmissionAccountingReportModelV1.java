/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

@SchemaVersion(1)
public class CargoEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel {
	
	@ColumnName("Base Fuel")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long baseFuelEmission;
	
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long nbo;
	
	@ColumnName("HFO")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long hfo;
	
	@ColumnName("Pilot Light")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long pilotLightEmission;
}

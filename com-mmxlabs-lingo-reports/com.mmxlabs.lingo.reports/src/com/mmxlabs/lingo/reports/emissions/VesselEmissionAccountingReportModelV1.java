/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

/**
 * Emissions report data
 */
@SchemaVersion(1)
public class VesselEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel {
	
	@ColumnName("Base Fuel")
	public Long baseFuelEmission;
	
	@ColumnName("BOG")
	public Long bogEmission;
	
	@ColumnName("Pilot Light")
	public Long pilotLightEmission;
	
	@ColumnName("Attained CII")
	public Long attainedCII;
}

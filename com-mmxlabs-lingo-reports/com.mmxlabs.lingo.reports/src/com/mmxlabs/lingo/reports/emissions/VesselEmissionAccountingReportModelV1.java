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
	
	// Base Fuel
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public String baseFuelType;
	
	// Base Fuel
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long baseFuelConsumed;
	
	// Base Fuel
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long baseFuelEmissions;
	
	// LNG
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long consumedNBO;
	
	// LNG
	@ColumnName("FBO")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long consumedFBO;
	
	// LNG
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long emissionsLNG;
	
	// Pilot Light
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long pilotLughtFuelType;
	
	// Pilot Light
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long pilotLightFuelConsumption;
	
	// Pilot Light
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long pilotLightEmission;
	
	@LingoIgnore
	@ColumnName("CII")
	@ColumnOrderLevel(ColumnOrder.LATER_LEVEL)
	public Long attainedCII;
}

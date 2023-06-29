/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.lingo.reports.emissions.columns.ColumnGroup;
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
	
	private static final String BASE_FUEL_GROUP = "BASE_FUEL_GROUP";
	private static final String BASE_FUEL_TITLE = "Base Fuel";
	private static final String LNG_GROUP = "LNG_GROUP";
	private static final String LNG_TITLE = "LNG";
	private static final String PILOT_LIGHT_GROUP = "PILOT_LIGHT_GROUP";
	private static final String PILOT_LIGHT_TITLE = "Pilot Light";
	
	// Base Fuel
	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.EARLY_LEVEL)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public String baseFuelType;
	
	// Base Fuel
	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.EARLY_LEVEL)
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long baseFuelConsumed;
	
	// Base Fuel
	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.EARLY_LEVEL)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long baseFuelEmissions;
	
	// LNG
	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.MIDDLE_LEVEL)
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long consumedNBO;
	
	// LNG
	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.MIDDLE_LEVEL)
	@ColumnName("FBO")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long consumedFBO;
	
	// LNG
	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.MIDDLE_LEVEL)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long emissionsLNG;
	
	// Pilot Light
	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.LATER_LEVEL)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long pilotLughtFuelType;
	
	// Pilot Light
	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.LATER_LEVEL)
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long pilotLightFuelConsumption;
	
	// Pilot Light
	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.LATER_LEVEL)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.EARLY_LEVEL)
	public Long pilotLightEmission;
	
	@LingoIgnore
	@ColumnName("CII")
	@ColumnOrderLevel(ColumnOrder.LATER_LEVEL)
	public Long attainedCII;
}

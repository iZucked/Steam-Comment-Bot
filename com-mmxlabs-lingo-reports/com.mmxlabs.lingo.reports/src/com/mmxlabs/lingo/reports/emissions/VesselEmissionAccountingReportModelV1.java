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
	private static final String METHANE_SLIP_GROUP_ID = "METHANE_SLIP_GROUP_ID";
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.FIRST)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public String eventType;
	
	// Base Fuel
	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.THIRD)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public String baseFuelType;
	
	// Base Fuel
	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.THIRD)
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.SECOND)
	public Long baseFuelConsumed;
	
	// Base Fuel
	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.THIRD)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long baseFuelEmissions;
	
	// LNG
	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long consumedNBO;
	
	// LNG
	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("FBO")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long consumedFBO;
	
	// LNG
	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long emissionsLNG;
	
	// Pilot Light
	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.FIFTH)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public String pilotLightFuelType;
	
	// Pilot Light
	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.FIFTH)
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long pilotLightFuelConsumption;
	
	// Pilot Light
	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.FIFTH)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long pilotLightEmission;
	
	@ColumnGroup(id = METHANE_SLIP_GROUP_ID, headerTitle = "", position = ColumnOrder.THIRD_FROM_END)
	@ColumnName("Methane Slip")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long methaneSlip;
}

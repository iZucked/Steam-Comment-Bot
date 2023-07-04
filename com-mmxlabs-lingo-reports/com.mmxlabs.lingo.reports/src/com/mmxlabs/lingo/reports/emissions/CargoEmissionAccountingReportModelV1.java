/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.lingo.reports.emissions.columns.ColumnGroup;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

@SchemaVersion(1)
public class CargoEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel {
	
	private static final String EMISSION_TYPES_GROUP = "EMISSIONS_TYPES_GROUP";
	private static final String SHIPPING_GROUP = "SHIPPING_GROUP";
	private static final String SHIPPING_TITLE = "Shipping";
	private static final String OTHER_GROUP = "OTHER_GROUP";
	private static final String CH4_GROUP_ID = "CH4_GROUP";
	private static final String CH4_GROUP_TITLE = "CH4";
	
	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.THIRD)
	@ColumnName("Upstream")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long upstreamEmission;
	
	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.THIRD)
	@ColumnName("Pipeline")
	@ColumnOrderLevel(ColumnOrder.SECOND)
	public Long pipelineEmission;
	
	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.THIRD)
	@ColumnName("Liquefaction")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long liquefactionEmission;
	
	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("Base Fuel")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long baseFuelEmission;
	
	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.SECOND)
	public Long nbo;
	
	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("FBO")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long fbo;
	
	@ColumnGroup(id = OTHER_GROUP, headerTitle = "", position = ColumnOrder.FIFTH)
	@ColumnName("Pilot Light")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long pilotLightEmission;
	
	@ColumnGroup(id = CH4_GROUP_ID, headerTitle = CH4_GROUP_TITLE, position = ColumnOrder.THIRD_FROM_END)
	@ColumnName("Methane Slip")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long methaneSlip;
	
	@ColumnGroup(id = CH4_GROUP_ID, headerTitle = CH4_GROUP_TITLE, position = ColumnOrder.THIRD_FROM_END)
	@ColumnName("CO2e")
	@ColumnOrderLevel(ColumnOrder.LAST)
	public Long methaneSlipEmissionsCO2;
	
	
}

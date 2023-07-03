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
	
	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.EARLY_LEVEL)
	@ColumnName("Upstream")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long upstreamEmission;
	
	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.EARLY_LEVEL)
	@ColumnName("Pipeline")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long pipelineEmission;
	
	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.EARLY_LEVEL)
	@ColumnName("Liquefaction")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long liquefactionEmission;
	
	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.MIDDLE_LEVEL)
	@ColumnName("Base Fuel")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long baseFuelEmission;
	
	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.MIDDLE_LEVEL)
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long nbo;
	
	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.MIDDLE_LEVEL)
	@ColumnName("FBO")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long fbo;
	
	@ColumnGroup(id = OTHER_GROUP, headerTitle = "", position = ColumnOrder.LATER_LEVEL)
	@ColumnName("Pilot Light")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long pilotLightEmission;
}

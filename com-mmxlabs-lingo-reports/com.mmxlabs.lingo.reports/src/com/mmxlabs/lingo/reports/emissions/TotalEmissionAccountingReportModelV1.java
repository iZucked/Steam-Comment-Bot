/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

/**
 * Cargo emissions report data
 * YES it looks exactly the same as Vessel emissions report model
 * But it will most probably be changed
 */
@SchemaVersion(1)
public class TotalEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel {
	
	@ColumnName("Upstream")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long upstreamEmission;
	
	@ColumnName("Pipeline")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long pipelineEmission;
	
	@ColumnName("Liquefaction")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long liquefactionEmission;
	
	@ColumnName("Shipping")
	@ColumnOrderLevel(ColumnOrder.MIDDLE_LEVEL)
	public Long shippingEmission;
	
	@JsonIgnore
	@LingoIgnore
	public double upstreamEmissionRate;
	
	@JsonIgnore
	@LingoIgnore
	public double pipelineEmissionRate;
	
	@JsonIgnore
	@LingoIgnore
	public double liquefactionEmissionRate;
}

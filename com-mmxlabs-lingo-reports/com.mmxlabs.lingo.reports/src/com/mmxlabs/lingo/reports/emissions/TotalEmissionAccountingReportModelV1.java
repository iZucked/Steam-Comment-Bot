/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnSortOrder;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoEquivalents;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;
import com.mmxlabs.models.lng.schedule.Schedule;

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
}

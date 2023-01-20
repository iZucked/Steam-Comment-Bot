/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnSortOrder;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoEquivalents;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

/**
 * Cargo emissions report data
 * YES it looks exactly the same as Vessel emissions report model
 * But it will most probably be changed
 */
@SchemaVersion(1)
public class CargoEmissionAccountingReportModelV1{

	@JsonIgnore
	@LingoEquivalents
	@LingoIgnore
	public Set<Object> equivalents = new HashSet<>();
	
	@ColumnName("Vessel")
	public String vesselName;
	
	@ColumnName("ID")
	public String eventID;
	
	@ColumnName("Start")
	@HubFormat("DD/MM/YY")
	@LingoFormat("dd/MM/yy")

	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventStart;
	
	@ColumnName("End")
	@HubFormat("DD/MM/YY")
	@LingoFormat("dd/MM/yy")

	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventEnd;
	
	@ColumnName("Base Fuel")
//	@LingoFormat("#.###")
//	@HubFormat("{\"thousandSeparated\": true, \"pattern\": \"0.00\", \"mantissa\": 2}")
	public Integer baseFuelEmission;
	
	@ColumnName("BOG")
	public Integer bogEmission;
	
	@ColumnName("Pilot Light")
	public Integer pilotLightEmission;
	
	@ColumnName("Total CO2e kg")
	public Integer totalEmission;
	
	@JsonIgnore
	@LingoIgnore
	public double baseFuelEmissionRate;
	@JsonIgnore
	@LingoIgnore
	public double bogEmissionRate;
	@JsonIgnore
	@LingoIgnore
	public double pilotLightEmissionRate;

	public static void main(String args[]) throws Exception {

		SchemaGenerator gen = new SchemaGenerator();
		String json = gen.generateHubSchema(CargoEmissionAccountingReportModelV1.class, Mode.FULL);
		System.out.println(json);
	}
}

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
	@HubFormat("DD/MM/YYYY HH:mm")
	@LingoFormat("dd/MM/yyyy HH:mm")

	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventStart;
	
	@ColumnName("End")
	@HubFormat("DD/MM/YYYY HH:mm")
	@LingoFormat("dd/MM/yyyy HH:mm")

	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventEnd;
	
	@ColumnName("Base Fuel CF, kg")
//	@LingoFormat("#.###")
//	@HubFormat("{\"thousandSeparated\": true, \"pattern\": \"0.00\", \"mantissa\": 2}")
	public Double baseFuelEmission;
	
	@ColumnName("BOG CF, kg")
	public Double bogEmission;
	
	@ColumnName("Pilot Light CF, kg")
	public Double pilotLightEmission;
	
	@ColumnName("Total CF, kg")
	public Double totalEmission;
	
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
		String json = gen.generateHubSchema(CargoEmissionAccountingReportModelV1.class);
		System.out.println(json);
	}
}

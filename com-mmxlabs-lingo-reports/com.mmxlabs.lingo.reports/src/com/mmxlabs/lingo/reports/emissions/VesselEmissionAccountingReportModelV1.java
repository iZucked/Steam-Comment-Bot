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
 * Emissions report data
 */
@SchemaVersion(1)
public class VesselEmissionAccountingReportModelV1 implements IVesselEmission, IEmissionReportIDData {

	@JsonIgnore
	@LingoEquivalents
	@LingoIgnore
	public Set<Object> equivalents = new HashSet<>();
	
	@JsonIgnore
	@LingoIgnore
	public boolean isPinned = false;
	@JsonIgnore
	@LingoIgnore
	public Schedule schedule;
	@JsonIgnore
	@LingoIgnore
	public String otherID;
	
	@ColumnName("Scenario")
	public String scenarioName;
	
	@ColumnName("Vessel")
	public String vesselName;
	
	@ColumnName("Event ID")
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
	public Long baseFuelEmission;
	
	@ColumnName("BOG")
	public Long bogEmission;
	
	@ColumnName("Pilot Light")
	public Long pilotLightEmission;
	
	@ColumnName("Total CO2e kg")
	public Long totalEmission;
	
	@JsonIgnore
	@LingoIgnore
	public double baseFuelEmissionRate;
	@JsonIgnore
	@LingoIgnore
	public double bogEmissionRate;
	@JsonIgnore
	@LingoIgnore
	public double pilotLightEmissionRate;
	
	@JsonIgnore
	@Override
	public double getBaseFuelEmissionRate() {
		return baseFuelEmissionRate;
	}

	@JsonIgnore
	@Override
	public double getBOGEmissionRate() {
		return bogEmissionRate;
	}

	@JsonIgnore
	@Override
	public double getPilotLightEmissionRate() {
		return pilotLightEmissionRate;
	}

	@JsonIgnore
	@Override
	public boolean isPinned() {
		return this.isPinned;
	}

	@JsonIgnore
	@Override
	public Schedule getSchedule() {
		return this.schedule;
	}
	
	@JsonIgnore
	@Override
	public String getScenarioName() {
		return scenarioName;
	}

	@Override
	public String getVesselName() {
		return vesselName;
	}

	@Override
	public String getEventID() {
		return eventID;
	}

	@JsonIgnore
	@Override
	public String getOtherID() {
		return otherID;
	}

	public static void main(String args[]) throws Exception {

		SchemaGenerator gen = new SchemaGenerator();
		String json = gen.generateHubSchema(VesselEmissionAccountingReportModelV1.class, Mode.FULL);
		System.out.println(json);
	}
	
	@JsonIgnore
	@LingoIgnore
	public int group = Integer.MAX_VALUE;
	
	@JsonIgnore
	@Override
	public int getGroup() {
		return group;
	}
	
	public void setGroup(int group) {
		this.group = group;
	}
}

package com.mmxlabs.lingo.reports.emissions;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnGroup;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnSortOrder;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoEquivalents;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.models.lng.schedule.Schedule;

public class AbstractEmissionAccountingReportModel implements IEmissionReportIDData, IVesselEmission {
	
	private static final String ID_COLUMN_GROUP = "START_COLUMN_GROUP";

	@JsonIgnore
	@LingoEquivalents
	@LingoIgnore
	public Set<Object> equivalents = new HashSet<>();
	
	@JsonIgnore
	@LingoIgnore
	public boolean isPinnedFlag = false;
	@JsonIgnore
	@LingoIgnore
	public Schedule schedule;
	@JsonIgnore
	@LingoIgnore
	public String otherID;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.ID_LEVEL)
	@ColumnName("Scenario")
	@ColumnOrderLevel(ColumnOrder.ID_LEVEL)
	public String scenarioName;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.ID_LEVEL)
	@ColumnName("Vessel")
	@ColumnOrderLevel(ColumnOrder.ID_LEVEL)
	public String vesselName;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.ID_LEVEL)
	@ColumnName("ID")
	@ColumnOrderLevel(ColumnOrder.ID_LEVEL)
	public String eventID;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.ID_LEVEL)
	@ColumnName("Start")
	@HubFormat("DD/MM/YY")
	@LingoFormat("dd/MM/yy")
	@ColumnOrderLevel(ColumnOrder.EARLY_START_DATE)
	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventStart;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.ID_LEVEL)
	@ColumnName("End")
	@ColumnOrderLevel(ColumnOrder.EARLY_END_DATE)
	@HubFormat("DD/MM/YY")
	@LingoFormat("dd/MM/yy")
	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventEnd;
	
	@JsonIgnore
	@LingoIgnore
	@ColumnName("CH4")
	@ColumnOrderLevel(ColumnOrder.END)
	public Long methaneSlip;
	
	@ColumnGroup(id = "TOTAL_EMISSIONS_GROUP", headerTitle = "", position = ColumnOrder.END)
	@ColumnName("Total CO2e t")
	@ColumnOrderLevel(ColumnOrder.END)
	public Long totalEmission;
	
	@JsonIgnore
	@Override
	public boolean isPinned() {
		return this.isPinnedFlag;
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

	@JsonIgnore
	@LingoIgnore
	public int group = Integer.MAX_VALUE;
	
	@JsonIgnore
	@Override
	public int getGroup() {
		return group;
	}
	
	@Override
	public void setGroup(int group) {
		this.group = group;
	}

	@JsonIgnore
	@LingoIgnore
	public double methaneSlipRate;

	@JsonIgnore
	@Override
	public double getMethaneSlipRate() {
		return methaneSlipRate;
	}
}

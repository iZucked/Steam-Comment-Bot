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
	
	protected static final String ID_COLUMN_GROUP = "START_COLUMN_GROUP";
	private static final String TOTAL_EMISSIONS_GROUP_ID = "TOTAL_EMISSIONS_GROUP";
	private static final String CII_GROUP_ID = "CII_GROUP_ID";
	private static final String CII_GROUP_TITLE = "CII";

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
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.FIRST)
	@ColumnName("Scenario")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public String scenarioName;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.FIRST)
	@ColumnName("Vessel")
	@ColumnOrderLevel(ColumnOrder.SECOND)
	public String vesselName;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.FIRST)
	@ColumnName("ID")
	@ColumnOrderLevel(ColumnOrder.THIRD_FROM_END)
	public String eventID;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.FIRST)
	@ColumnName("Start")
	@HubFormat("DD/MM/YY")
	@LingoFormat("dd/MM/yy")
	@ColumnOrderLevel(ColumnOrder.ONE_BUT_LAST)
	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventStart;
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.FIRST)
	@ColumnName("End")
	@ColumnOrderLevel(ColumnOrder.LAST)
	@HubFormat("DD/MM/YY")
	@LingoFormat("dd/MM/yy")
	@ColumnSortOrder(value = 1)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime eventEnd;
	
	@ColumnGroup(id = TOTAL_EMISSIONS_GROUP_ID, headerTitle = "", position = ColumnOrder.ONE_BUT_LAST)
	@ColumnName("Total")
	@ColumnOrderLevel(ColumnOrder.LAST)
	public Long totalEmission;
	
	@ColumnGroup(id = CII_GROUP_ID, headerTitle = CII_GROUP_TITLE, position = ColumnOrder.LAST)
	@ColumnName("Value")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long ciiValue;
	
	@ColumnGroup(id = CII_GROUP_ID, headerTitle = CII_GROUP_TITLE, position = ColumnOrder.LAST)
	@ColumnName("Grade")
	@ColumnOrderLevel(ColumnOrder.LAST)
	public String ciiGrade;
	
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

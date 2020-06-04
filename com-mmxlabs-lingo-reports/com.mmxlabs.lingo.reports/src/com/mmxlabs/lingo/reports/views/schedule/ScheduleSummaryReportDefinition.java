/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleSummaryReportDefinition implements Cloneable {
	String uuid;
	String name;
	String type;
	final List<String> filters = new ArrayList<>();
	final List<String> columns = new ArrayList<>();
	final List<String> diffOptions = new ArrayList<>();

	@Override
	public Object clone() {
		ScheduleSummaryReportDefinition rd = new ScheduleSummaryReportDefinition();
		rd.uuid = uuid;
		rd.name = name;
		rd.type = type;
		rd.filters.addAll(filters);
		rd.columns.addAll(columns);
		rd.diffOptions.addAll(diffOptions);
		return rd;
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public List<String> getFilters() {
		return filters;
	}
	
	public List<String> getColumns() {
		return columns;
	}
	
	public List<String> getDiffOptions() {
		return diffOptions;
	}
}

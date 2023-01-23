/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import java.util.ArrayList;
import java.util.List;

public class CustomReportDefinition {
	private String uuid;
	private String name;
	private String type;
	private final List<String> filters = new ArrayList<>();
	private final List<String> columns = new ArrayList<>();
	private final List<String> diffOptions = new ArrayList<>();

	public CustomReportDefinition copy() {
		final CustomReportDefinition rd = new CustomReportDefinition();
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

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.Objects;

public class ScheduleChartRowKey {
	
	private final String name;
	private final Object data;
	
	public ScheduleChartRowKey(final String name, final Object data) {
		this.name = name;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}
	
	public Object getData() {
		return data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(data);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleChartRowKey other = (ScheduleChartRowKey) obj;
		return Objects.equals(data, other.data);
	}

}

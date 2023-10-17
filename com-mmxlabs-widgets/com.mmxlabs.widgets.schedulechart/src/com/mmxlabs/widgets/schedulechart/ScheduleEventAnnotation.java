/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleEventAnnotation {
	
	protected final List<LocalDateTime> dates;
	private final Object data;
	private boolean visible;
	
	public ScheduleEventAnnotation(final List<LocalDateTime> dates, final Object data) {
		this.dates = dates;
		this.data = data;
	}
	
	public ScheduleEventAnnotation(final ScheduleEventAnnotation sea) {
		this.data = sea.getData();
		this.dates = sea.getDates();
	}
	
	public ScheduleEventAnnotation(final Object data) {
		this(Collections.emptyList(), data);
	}

	public Object getData() {
		return data;
	}

	public List<LocalDateTime> getDates() {
		return new ArrayList<>(dates);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}

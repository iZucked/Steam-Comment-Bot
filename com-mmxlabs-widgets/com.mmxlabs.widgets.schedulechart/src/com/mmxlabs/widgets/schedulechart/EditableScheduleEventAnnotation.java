/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EditableScheduleEventAnnotation extends ScheduleEventAnnotation {

	public EditableScheduleEventAnnotation(List<LocalDateTime> dates, Object data) {
		super(new ArrayList<>(dates), data);
	}
	
	@Override
	public List<LocalDateTime> getDates() {
		return dates;
	}
	
	public void setDate(int index, LocalDateTime d) {
		dates.set(index, d);
	}

}

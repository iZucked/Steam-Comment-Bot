/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.views.schedule.IScheduleColumnFactory;

@ExtensionBean("com.mmxlabs.lingo.reports.ScheduleBasedColumnOverride")
public interface IScheduleBasedColumnOverrideExtension {

	@MapName("column")
	String getColumnID();

	@MapName("name")
	String getNameOverride();

}

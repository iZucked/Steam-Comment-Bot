/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.views.schedule.IScheduleColumnFactory;

@ExtensionBean("com.mmxlabs.lingo.reports.ScheduleBasedColumn")
public interface IScheduleBasedColumnExtension {

	@MapName("id")
	String getColumnID();

	@MapName("handlerID")
	String getHandlerID();

	@MapName("class")
	IScheduleColumnFactory getFactory();

}

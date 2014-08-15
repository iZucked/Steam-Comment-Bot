/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.ScheduleBasedColumn")
public interface IScheduleBasedColumnExtension {

	@MapName("id")
	String getColumnID();

	@MapName("handlerID")
	String getHandlerID();

	@MapName("class")
	IScheduleColumnFactory getFactory();

}

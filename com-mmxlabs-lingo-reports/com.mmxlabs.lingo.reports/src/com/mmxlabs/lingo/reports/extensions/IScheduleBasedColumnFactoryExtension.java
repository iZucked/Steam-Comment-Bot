/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.ScheduleBasedColumnFactory")
public interface IScheduleBasedColumnFactoryExtension {

	@MapName("handlerID")
	String getHandlerID();

	@MapName("factory")
	IScheduleColumnFactory getFactory();

}

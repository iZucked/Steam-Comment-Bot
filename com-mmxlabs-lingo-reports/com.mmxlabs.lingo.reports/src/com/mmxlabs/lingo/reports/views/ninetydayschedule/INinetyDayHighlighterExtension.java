/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

@ExtensionBean("com.mmxlabs.lingo.reports.NinetyDayHighlighter")
public interface INinetyDayHighlighterExtension {

	@MapName("class")
	String getID();

	/**
	 * Create a new instance. Note method needs to start with "create" to avoid Peaberry caching the result
	 * 
	 * @return
	 */
	@MapName("class")
	IScheduleEventStylingProvider createInstance();
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.scheduleview.views.IScheduleViewColourScheme;

@ExtensionBean("com.mmxlabs.lingo.reports.scheduleview.schedulerViewColourScheme")
public interface ISchedulerViewColourSchemeExtension {

	@MapName("id")
	String getID();

	@MapName("name")
	String getName();

	/**
	 * Create a new instance. Note method needs to start with "create" to avoid Peaberry caching the result
	 * 
	 * @return
	 */
	@MapName("class")
	IScheduleViewColourScheme createInstance();
	
	@MapName("isHighlighter")
	String isHighlighter();
}

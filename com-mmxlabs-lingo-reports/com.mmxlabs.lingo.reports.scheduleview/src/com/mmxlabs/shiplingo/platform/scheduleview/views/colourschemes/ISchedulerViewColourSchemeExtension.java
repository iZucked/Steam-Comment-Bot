/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

@ExtensionBean("com.mmxlabs.shiplingo.platform.scheduleview.schedulerViewColourScheme")
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

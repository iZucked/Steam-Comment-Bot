/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.highlighters;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;

@NonNullByDefault
public interface ParameterisedColourScheme extends IScheduleEventStylingProvider {

	List<NamedObject> getOptions(ScheduleChartViewer<?> viewer);

	void selectOption(@Nullable NamedObject object);

	@Nullable
	NamedObject getOption();

}

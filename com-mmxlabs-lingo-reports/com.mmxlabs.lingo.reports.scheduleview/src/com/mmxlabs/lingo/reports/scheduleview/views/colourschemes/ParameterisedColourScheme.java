/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import java.util.List;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.mmxcore.NamedObject;

public abstract class ParameterisedColourScheme extends ColourScheme {

	public abstract List<NamedObject> getOptions(GanttChartViewer viewer);

	public abstract void selectOption(Object object);

}

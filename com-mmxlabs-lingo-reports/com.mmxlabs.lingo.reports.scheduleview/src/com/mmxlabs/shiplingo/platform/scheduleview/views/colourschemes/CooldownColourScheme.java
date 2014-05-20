/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Idle;

/**
 * A colour scheme which highlights cooldown in red
 * 
 * @author hinton
 * 
 */
public class CooldownColourScheme extends ColourScheme {

	private GanttChartViewer viewer;

	@Override
	public String getName() {
		return "Cooldown";
	}

	@Override
	public GanttChartViewer getViewer() {
		return viewer;
	}

	@Override
	public void setViewer(final GanttChartViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Cooldown) {
			return ColorCache.getColor(255, 0, 0);
		}
		if (element instanceof Idle) {
			return ColorCache.getColor(0, 0, 255);
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {
		return null;
	}
	

	@Override 
	public int getBorderWidth(final Object element) {
		return 1;
	}
}

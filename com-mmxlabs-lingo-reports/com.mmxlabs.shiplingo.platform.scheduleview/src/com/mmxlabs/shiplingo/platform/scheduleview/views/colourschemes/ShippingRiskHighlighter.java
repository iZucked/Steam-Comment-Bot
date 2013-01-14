/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Warning_Yellow;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.findIdleForJourney;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.findJourneyForIdle;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isRiskyVoyage;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;

public class ShippingRiskHighlighter extends ColourScheme {

	private static final float threshold = 0.95f;
	private static final float speed = 19.0f;

	private GanttChartViewer viewer;

	@Override
	public String getName() {
		return "Shipping Risk";
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
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;

			if (isRiskyVoyage(journey, findIdleForJourney(journey), speed, threshold)) {
				return ColorCache.getColor(Warning_Yellow);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;

			if (isRiskyVoyage(findJourneyForIdle(idle), idle, speed, threshold)) {
				return ColorCache.getColor(Warning_Yellow);
			}
		}
		return null;
	}
}

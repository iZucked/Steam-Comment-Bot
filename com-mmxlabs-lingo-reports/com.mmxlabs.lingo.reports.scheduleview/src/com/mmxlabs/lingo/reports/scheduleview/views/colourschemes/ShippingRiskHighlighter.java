/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.ColourPalette.Warning_Yellow;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.findJourneyForIdle;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isJourneyTight;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.lingo.reports.preferences.PreferenceConstants;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;

public class ShippingRiskHighlighter extends ColourScheme {

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
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		int tightLeewayInDays = prefs.getInt(PreferenceConstants.P_LEEWAY_DAYS, 1);
		
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			
			if (isJourneyTight(journey, tightLeewayInDays * 24)) {
				return ColorCache.getColor(Warning_Yellow);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;

			if (isJourneyTight(findJourneyForIdle(idle), tightLeewayInDays * 24)) {
				return ColorCache.getColor(Warning_Yellow);
			}
		}
		return null;
	}

}

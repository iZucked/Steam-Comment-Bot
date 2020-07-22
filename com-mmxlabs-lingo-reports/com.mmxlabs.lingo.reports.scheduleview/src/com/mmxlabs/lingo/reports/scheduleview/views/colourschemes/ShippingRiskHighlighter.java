/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.findJourneyForIdle;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isJourneyTight;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.lingo.reports.preferences.PreferenceConstants;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;

public class ShippingRiskHighlighter extends ColourScheme {

	@Override
	public String getName() {
		return "Shipping Risk";
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
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Tight_Warning, ColourElements.Background);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;

			if (isJourneyTight(findJourneyForIdle(idle), tightLeewayInDays * 24)) {

				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Tight_Warning, ColourElements.Background);
			}
		}
		return null;
	}

}

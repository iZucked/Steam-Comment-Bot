/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.highlighters;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.lingo.reports.preferences.PreferenceConstants;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.AbstractScheduleEventStylingProvider;

public class ShippingRiskHighlighter extends AbstractScheduleEventStylingProvider {

	public ShippingRiskHighlighter() {
		super("Shipping Risk");
	}

	@Override
	public @Nullable Color getBackgroundColour(final DrawableScheduleEvent event, @Nullable final Color defaultColor) {

		final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		final int tightLeewayInDays = prefs.getInt(PreferenceConstants.P_LEEWAY_DAYS, 1);

		if (event.getScheduleEvent().getData() instanceof final Journey journey) {
			if (isJourneyTight(journey, tightLeewayInDays * 24)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Tight_Warning, ColourElements.Background);
			}
		} else if (event.getScheduleEvent().getData() instanceof final Idle idle) {
			if (isJourneyTight(findJourneyForIdle(idle), tightLeewayInDays * 24)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Tight_Warning, ColourElements.Background);
			}
		}
		return defaultColor;

	}

	/**
	 * Checks to see if a journey is "tight", i.e. when idle time after the journey is taken into consideration, is there less than a specified amount of leeway travelling at a vessel-specified
	 * "service speed"?
	 * 
	 * @param journey
	 * @return
	 */
	private static boolean isJourneyTight(final Journey journey, final int leewayInHrs) {
		if (journey == null) {
			return false;
		}

		final int distance = journey.getDistance();
		int journeyPlusIdleTime = journey.getDuration();

		final Sequence sequence = journey.getSequence();
		if (sequence == null) {
			return false;
		}
		Vessel vessel = null;

		// get vessel directly from the sequence if it is a spot charter
		final CharterInMarket charterInMarket = sequence.getCharterInMarket();
		if (charterInMarket != null) {
			vessel = charterInMarket.getVessel();
		}
		// otherwise get it from the vessel allocation
		if (vessel == null) {
			final VesselCharter avail = sequence.getVesselCharter();
			if (avail == null) {
				return false;
			}
			vessel = avail.getVessel();

		}
		if (vessel == null) {
			return false;
		}

		final VesselStateAttributes attr = journey.isLaden() ? vessel.getLadenAttributes() : vessel.getBallastAttributes();
		final double speed = attr.getVesselOrDelegateServiceSpeed();

		final Event nextEvent = journey.getNextEvent();
		if (nextEvent instanceof Idle) {
			journeyPlusIdleTime += nextEvent.getDuration();
		}

		final double serviceTime = distance / speed;

		return journeyPlusIdleTime - serviceTime < leewayInHrs;
	}

	private static Journey findJourneyForIdle(final Idle idle) {
		final Sequence sequence = idle.getSequence();
		final int index = sequence.getEvents().indexOf(idle);
		if (index != -1 && index - 1 >= 0) {
			final Event event = sequence.getEvents().get(index - 1);
			if (event instanceof final Journey journey) {
				return journey;
			}
		}
		return null;
	}

}

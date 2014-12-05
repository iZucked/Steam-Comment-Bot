package com.mmxlabs.lingo.reports.views.vertical;

import org.joda.time.LocalDate;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class to allow EventProvider objects to filter out particular events from their results.
 * 
 * @author mmxlabs
 * 
 */
public interface EventFilter {
	boolean isEventFiltered(LocalDate date, Event event);
}
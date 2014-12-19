/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.filters;

import org.eclipse.jdt.annotation.NonNull;
import org.joda.time.LocalDate;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class to allow EventProvider objects to filter out particular events from their results.
 * 
 * @author mmxlabs
 * 
 */
public interface EventFilter {
	boolean isEventFiltered(@NonNull LocalDate date, @NonNull Event event);
}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.labellers;

import java.time.LocalDate;

import com.mmxlabs.models.lng.schedule.Event;

public interface IBorderProvider {

	public static final int NONE = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int TOP = 4;
	public static final int BOTTOM = 8;

	public static final int ALL = LEFT | RIGHT | TOP | BOTTOM;

	int getBorders(LocalDate date, Event event);

}

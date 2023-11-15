/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

public abstract class AbstractScheduleEventStylingProvider implements IScheduleEventStylingProvider {

	private final String name;
	private String id;

	protected AbstractScheduleEventStylingProvider(final String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setID(final String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}
}

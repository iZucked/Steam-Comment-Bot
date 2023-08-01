/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.models.lng.schedule.Schedule;

public interface IEmissionReportIDData {
	boolean isPinned();
	Schedule getSchedule();
	String getScenarioName();
	String getVesselName();
	String getEventID();
	String getOtherID();
	// grouping data
	int getGroup();
	void setGroup(int group);
}
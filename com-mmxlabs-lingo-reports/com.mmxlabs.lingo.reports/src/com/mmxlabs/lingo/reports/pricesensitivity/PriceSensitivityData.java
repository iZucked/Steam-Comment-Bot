/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.pricesensitivity;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;

public class PriceSensitivityData {
	public final ScenarioResult scenarioResult;
	public String key;
	public final List<PriceSensitivityData> children = new LinkedList<>();
	public long maxPnl;
	public long minPnl;
	public long averagePnl;
	public double variance;
	public Schedule schedule;
	public String loadId;
	public String dischargeId;
	public PriceSensitivityData parent = null;
	
	
	
	public PriceSensitivityData(@NonNull final ScenarioResult scenarioResult, @NonNull Schedule schedule) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		
		
	}
}

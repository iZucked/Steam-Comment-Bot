/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;

import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

public class GeneratedCharterOutLeg {
	private Integer distance;
	private ERouteOption route;
	private Integer shortestTime;
	private Integer panamaIdleHours;
	
	public GeneratedCharterOutLeg(Integer distance, ERouteOption route, Integer shortestTime, Integer panamaIdleHours) {
		this.distance = distance;
		this.route = route;
		this.shortestTime = shortestTime;
		this.panamaIdleHours = panamaIdleHours;
	}
	
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public ERouteOption getRoute() {
		return route;
	}
	public void setRoute(ERouteOption route) {
		this.route = route;
	}
	public Integer getShortestTime() {
		return shortestTime;
	}
	public void setShortestTime(Integer shortestTime) {
		this.shortestTime = shortestTime;
	}
	public Integer getPanamaIdleHours() {
		return panamaIdleHours;
	}
	public void setPanamaIdleHours(Integer panamaIdleHours) {
		this.panamaIdleHours = panamaIdleHours;
	}
}

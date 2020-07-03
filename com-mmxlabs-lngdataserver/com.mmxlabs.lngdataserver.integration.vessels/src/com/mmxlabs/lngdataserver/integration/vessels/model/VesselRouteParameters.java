/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.model;

public class VesselRouteParameters {
	private String route;
	private int extraTransitTimeInHours;

	private double ladenNBORate;
	private double ballastNBORate;

	private double ladenBunkerRate;
	private double ballastBunkerRate;

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public int getExtraTransitTimeInHours() {
		return extraTransitTimeInHours;
	}

	public void setExtraTransitTimeInHours(int extraTransitTimeInHours) {
		this.extraTransitTimeInHours = extraTransitTimeInHours;
	}

	public double getLadenNBORate() {
		return ladenNBORate;
	}

	public void setLadenNBORate(double ladenNBORate) {
		this.ladenNBORate = ladenNBORate;
	}

	public double getBallastNBORate() {
		return ballastNBORate;
	}

	public void setBallastNBORate(double ballastNBORate) {
		this.ballastNBORate = ballastNBORate;
	}

	public double getLadenBunkerRate() {
		return ladenBunkerRate;
	}

	public void setLadenBunkerRate(double ladenBunkerRate) {
		this.ladenBunkerRate = ladenBunkerRate;
	}

	public double getBallastBunkerRate() {
		return ballastBunkerRate;
	}

	public void setBallastBunkerRate(double ballastBunkerRate) {
		this.ballastBunkerRate = ballastBunkerRate;
	}
}

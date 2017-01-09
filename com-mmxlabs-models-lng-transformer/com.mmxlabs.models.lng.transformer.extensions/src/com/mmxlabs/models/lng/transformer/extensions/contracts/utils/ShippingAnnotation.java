/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.utils;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

public class ShippingAnnotation {
	public IPort loadPort;
	public IPort dischargePort;

	public int ladenDistance;
	public int ballastDistance;

	public int loadTime;
	public int dischargeTime;

	public int ladenTravelTime;
	public int ballastTravelTime;

	public int ladenTotalTravelTime;
	public int ballastTotalTravelTime;

	public int ladenIdleTime;
	public int ballastIdleTime;

	public int ladenSpeed;
	public int ballastSpeed;

	public long ladenNBO;
	public long ballastNBO;

	public long ladenIdleNBO;
	public long ballastIdleNBO;

	public long ladenFBO;
	public long ballastFBO;

	public long loadBaseFuel;
	public long dischargeBaseFuel;

	public long ladenBaseFuel;
	public long ballastBaseFuel;

	public long ladenIdleBaseFuel;
	public long ballastIdleBaseFuel;

	public long loadPortCost;
	public long dischargePortCost;

	public int totalShippingHours;
	public int totalCharteringHours;

	public long loadVolume;
	public long dischargeVolume;
		
	public long ladenBunkeringCost;
	public long ballastBunkeringCost;

	public long endHeel;
	public ERouteOption ladenRoute;
	public ERouteOption ballastRoute;
	public long ladenCanalCosts;
	public long ballastCanalCosts;

}
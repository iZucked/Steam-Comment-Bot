/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

public class NotionalJourneyBallastBonusRuleAnnotation implements IBallastBonusRuleAnnotation {
	public IPort returnPort = null;
	public int distance = 0;
	public int totalTimeInHours = 0;
	public long totalFuelUsed = 0;
	public int fuelPrice = 0;
	public long totalFuelCost = 0;
	public long hireRate = 0;
	public long totalHireCost = 0;
	public ERouteOption route = null;
	public long canalCost = 0;
}

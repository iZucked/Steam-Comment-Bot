/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations;

import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
/***
 * A notional journey charter contract term
 * @author FM
 *
 */
public abstract class NotionalJourneyTermAnnotation implements ICharterContractTermAnnotation {
	public long lumpSum = 0;
	public int distance = 0;
	public int totalTimeInHours = 0;
	public long totalFuelUsed = 0;
	public int fuelPrice = 0;
	public long totalFuelCost = 0;
	public long hireRate = 0;
	public long totalHireCost = 0;
	public ERouteOption route = null;
	public long canalCost = 0;
	
	public long totalCost = 0;
}

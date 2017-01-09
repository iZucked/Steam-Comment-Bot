/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.notional;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

/**
 * A annotation class for single load, single discharge voyages.
 * 
 */
public class LDShippingAnnotation {

	public int startOfLoading;
	public int completionOfLoading;
	public int startOfDischarge;
	public int completionOfDischarge;
	public int nextPortDate;

	public int loadHireHours;
	public int ladenHireHours;
	public int dischargeHireHours;
	public int ballastHireHours;

	public IPort loadPort;
	public IPort dischargePort;
	public IPort returnPort;

	public long charterRatePerDay;
	public long loadCharterCost;
	public long ladenCharterCost;
	public long dischargeCharterCost;
	public long ballastCharterCost;

	public long loadBunkersInMT;
	public long ladenBunkersInMT;
	public long dischargeBunkersInMT;
	public long ballastBunkersInMT;

	public int bunkerPricePerMT;

	public long loadBunkersCost;
	public long ladenBunkersCost;
	public long dischargeBunkersCost;
	public long ballastBunkersCost;

	public int cargoCV;

	public int lngCostPerMMBTu;
	public long ladenNBOInMMBTu;
	public long ballastNBOInMMBTu;

	public long ladenFBOInMMBTu;
	public long ballastFBOInMMBTu;

	public long ladenBOInMMBTu;
	public long ballastBOInMMBTu;

	public long ladenBOCost;
	public long ballastBOCost;

	public long cooldownCost;

	public long loadPortCosts;
	public long dischargePortCosts;

	public ERouteOption ladenRoute;
	public ERouteOption ballastRoute;

	public int ladenDistance;
	public int ballastDistance;

	public long ladenCanalCosts;
	public long ballastCanalCosts;

	public int ladenSpeed;
	public int ballastSpeed;

	public long loadMiscCosts;
	public long ladenMiscCosts;
	public long dischargeMiscCosts;
	public long ballastMiscCosts;

	public long ladenCostsExcludingBOG = 0;
	public long ballastCostsExcludingBOG = 0;
	public long ladenCostsIncludingBOG = 0;
	public long ballastCostsIncludingBOG = 0;
	
	public long returnGasInMMBTu = 0;
	public long ladenBOInM3;
	public long ballastBOInM3;

}
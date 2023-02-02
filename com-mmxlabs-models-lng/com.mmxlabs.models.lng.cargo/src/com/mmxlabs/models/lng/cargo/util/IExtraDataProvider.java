package com.mmxlabs.models.lng.cargo.util;

import java.time.ZonedDateTime;
import java.util.List;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public interface IExtraDataProvider {

	List<VesselCharter> getExtraVesselCharters();

	List<CharterInMarketOverride> getExtraCharterInMarketOverrides();

	List<CharterInMarket> getExtraCharterInMarkets();

	List<LoadSlot> getExtraLoads();

	List<DischargeSlot> getExtraDischarges();

	List<VesselEvent> getExtraVesselEvents();

	List<SpotMarket> getExtraSpotCargoMarkets();

	List<ZonedDateTime> getExtraDates();

	// List<CommodityCurveOverlay> getExtraPriceCurves();

}

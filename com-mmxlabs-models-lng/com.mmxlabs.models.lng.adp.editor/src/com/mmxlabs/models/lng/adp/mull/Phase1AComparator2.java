package com.mmxlabs.models.lng.adp.mull;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

public class Phase1AComparator2 implements Comparator<CargoBlueprint> {

	YearMonth fromYm;
	YearMonth toYm;
	final Map<BaseLegalEntity, Map<YearMonth, Long>> monthEndEntitlements;
	final int fullCargoLotValue;

	public Phase1AComparator2(final YearMonth fromYm, final YearMonth toYm, final Map<BaseLegalEntity, Map<YearMonth, Long>> monthEndEntitlements, final int fullCargoLotValue) {
		this.fromYm = fromYm;
		this.toYm = toYm;
		this.monthEndEntitlements = monthEndEntitlements;
		this.fullCargoLotValue = fullCargoLotValue;
	}

	public void init(final YearMonth fromYm, final YearMonth toYm) {
		this.fromYm = fromYm;
		this.toYm = toYm;
	}

	@Override
	public int compare(CargoBlueprint cb1, CargoBlueprint cb2) {
		long violations1 = 0L;
		long violations2 = 0L;
		final int allocatedVolume1 = cb1.getAllocatedVolume();
		final int allocatedVolume2 = cb2.getAllocatedVolume();
		for (YearMonth ym = fromYm; ym.isBefore(toYm); ym = ym.plusMonths(1)) {
			final long newEntitlement1 = monthEndEntitlements.get(cb1.getEntity()).get(ym).longValue() + allocatedVolume1;
			final long thisViolationScore1 = Math.abs(Math.max(fullCargoLotValue, newEntitlement1) + Math.min(-fullCargoLotValue, newEntitlement1));
			violations1 += thisViolationScore1;

			final long newEntitlement2 = monthEndEntitlements.get(cb2.getEntity()).get(ym).longValue() + allocatedVolume2;
			final long thisViolationScore2 = Math.abs(Math.max(fullCargoLotValue, newEntitlement2) + Math.min(-fullCargoLotValue, newEntitlement2));
			violations2 += thisViolationScore2;
		}
		if (violations1 < violations2) {
			return -1;
		} else if (violations2 < violations1) {
			return 1;
		}

		long entitlementSum1 = 0L;
		long entitlementSum2 = 0L;

		for (YearMonth ym = fromYm; ym.isBefore(toYm); ym = ym.plusMonths(1)) {
			entitlementSum1 += monthEndEntitlements.get(cb1.getEntity()).get(ym).longValue() + allocatedVolume1;
			entitlementSum2 += monthEndEntitlements.get(cb2.getEntity()).get(ym).longValue() + allocatedVolume2;
		}
		return Long.compare(Math.abs(entitlementSum1), Math.abs(entitlementSum2));
	}

}

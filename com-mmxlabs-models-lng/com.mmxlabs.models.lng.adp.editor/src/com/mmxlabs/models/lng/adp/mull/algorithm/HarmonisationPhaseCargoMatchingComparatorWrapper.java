/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public class HarmonisationPhaseCargoMatchingComparatorWrapper {

	private final HarmonisationPhaseCargoMatchingComparator comparator;

	private class HarmonisationPhaseCargoMatchingComparator implements Comparator<ICargoBlueprint> {

		final Map<BaseLegalEntity, Map<YearMonth, Long>> monthEndEntitlements;
		final int fullCargoLotValue;

		boolean negateAllocation = false;
		// Arbitrary non-null year months that should be overwritten in use
		YearMonth fromYm = YearMonth.of(1970,1);
		YearMonth toYm = YearMonth.of(1970, 1);
		
		public HarmonisationPhaseCargoMatchingComparator(final Map<BaseLegalEntity, Map<YearMonth, Long>> monthEndEntitlements, final int fullCargoLotValue) {
			this.monthEndEntitlements = monthEndEntitlements;
			this.fullCargoLotValue = fullCargoLotValue;
		}
		
		public void setFromYm(final YearMonth fromYm) {
			this.fromYm = fromYm;
		}

		public void setToYm(final YearMonth toYm) {
			this.toYm = toYm;
		}
	
		public void setNegateAllocation(final boolean negateAllocation) {
			this.negateAllocation = negateAllocation;
		}
		
		@Override
		public int compare(final ICargoBlueprint cb1, final ICargoBlueprint cb2) {
			long violations1 = 0L;
			long violations2 = 0L;
			
			final int allocatedVolume1;
			final int allocatedVolume2;
			if (negateAllocation) {
				allocatedVolume1 = -cb1.getAllocatedVolume();
				allocatedVolume2 = -cb2.getAllocatedVolume();
			} else {
				allocatedVolume1 = cb1.getAllocatedVolume();
				allocatedVolume2 = cb2.getAllocatedVolume();
			}

			for (YearMonth ym = fromYm; ym.isBefore(toYm); ym = ym.plusMonths(1L)) {
				violations1 += calculateViolationScore(ym, allocatedVolume1, cb1);
				violations2 += calculateViolationScore(ym, allocatedVolume2, cb2);
			}
			
			if (violations1 < violations2) {
				return -1;
			} else if (violations2 < violations1) {
				return 1;
			}

			long entitlementSum1 = 0L;
			long entitlementSum2 = 0L;
			for (YearMonth ym = fromYm; ym.isBefore(toYm); ym = ym.plusMonths(1L)) {
				entitlementSum1 += monthEndEntitlements.get(cb1.getMudContainer().getEntity()).get(ym).longValue() + allocatedVolume1;
				entitlementSum2 += monthEndEntitlements.get(cb2.getMudContainer().getEntity()).get(ym).longValue() + allocatedVolume2;
			}
			return Long.compare(Math.abs(entitlementSum1), Math.abs(entitlementSum2));
		}
		
		private long calculateViolationScore(final YearMonth ym, final int allocatedVolume, final ICargoBlueprint cargoBlueprint) {
			final long newEntitlement = monthEndEntitlements.get(cargoBlueprint.getMudContainer().getEntity()).get(ym).longValue() + allocatedVolume;
			return Math.abs(Math.max(fullCargoLotValue, newEntitlement) + Math.min(-fullCargoLotValue, newEntitlement));
		}
		
	}

	public HarmonisationPhaseCargoMatchingComparatorWrapper(final Map<BaseLegalEntity, Map<YearMonth, Long>> monthEndEntitlements, final GlobalStatesContainer globalStates) {
		final int fullCargoLotValue = globalStates.getMullGlobalState().getFullCargoLotValue();
		this.comparator = new HarmonisationPhaseCargoMatchingComparator(monthEndEntitlements, fullCargoLotValue);
	}
	
	
	public Comparator<ICargoBlueprint> getComparator(final YearMonth fromYm, final YearMonth toYm, final boolean negateAllocation) {
		comparator.setFromYm(fromYm);
		comparator.setToYm(toYm);
		comparator.setNegateAllocation(negateAllocation);
		return comparator;
	}
}

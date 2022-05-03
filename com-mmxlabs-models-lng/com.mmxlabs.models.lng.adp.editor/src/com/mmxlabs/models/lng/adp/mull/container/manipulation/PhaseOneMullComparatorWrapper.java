/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class PhaseOneMullComparatorWrapper implements IMullComparatorWrapper {

	private final PhaseOneComparator comparator;

	public PhaseOneMullComparatorWrapper(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime, final int defaultAllocationDrop,
			final Set<Vessel> firstPartyVessels, final int fullCargoLotValue, final int loadDuration) {
		this.comparator = new PhaseOneComparator(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop, firstPartyVessels, fullCargoLotValue, loadDuration);
	}

	private class PhaseOneComparator implements Comparator<IMudContainer> {

		final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime;
		final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime;
		final int defaultAllocationDrop;
		final Set<Vessel> firstPartyVessels;
		final int fullCargoLotValue;
		final int loadDuration;

		// Arbitrary non-null initialisation value should be updated before calling compare
		private LocalDateTime currentDateTime = LocalDateTime.of(1970, 1, 1, 0, 0);

		public PhaseOneComparator(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime, final int defaultAllocationDrop,
				final Set<Vessel> firstPartyVessels, final int fullCargoLotValue, final int loadDuration) {
			this.vesselToMostRecentUseDateTime = vesselToMostRecentUseDateTime;
			this.vesselToNextForwardUseTime = vesselToNextForwardUseTime;
			this.defaultAllocationDrop = defaultAllocationDrop;
			this.firstPartyVessels = firstPartyVessels;
			this.fullCargoLotValue = fullCargoLotValue;
			this.loadDuration = loadDuration;
		}

		public void setCurrentDateTime(final LocalDateTime currentDateTime) {
			this.currentDateTime = currentDateTime;
		}

		@Override
		public int compare(final IMudContainer mc0, final IMudContainer mc1) {
			final Long allocation0 = mc0.getRunningAllocation();
			final Long allocation1 = mc1.getRunningAllocation();
			final int expectedAllocationDrop0 = mc0.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop, loadDuration, firstPartyVessels,
					currentDateTime);
			final int expectedAllocationDrop1 = mc1.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop, loadDuration, firstPartyVessels,
					currentDateTime);
			final int beforeDrop0 = mc0.getCurrentMonthAbsoluteEntitlement();
			final int beforeDrop1 = mc1.getCurrentMonthAbsoluteEntitlement();
			final int afterDrop0 = beforeDrop0 - expectedAllocationDrop0;
			final int afterDrop1 = beforeDrop1 - expectedAllocationDrop1;

			final boolean belowLower0 = afterDrop0 < -fullCargoLotValue;
			final boolean belowLower1 = afterDrop1 < -fullCargoLotValue;
			final boolean aboveUpper0 = afterDrop0 > fullCargoLotValue;
			final boolean aboveUpper1 = afterDrop1 > fullCargoLotValue;
			if (belowLower0) {
				if (belowLower1) {
					if (afterDrop0 < afterDrop1) {
						return -1;
					} else {
						return 1;
					}
				} else {
					return -1;
				}
			} else {
				if (belowLower1) {
					return 1;
				} else {
					if (aboveUpper0) {
						if (aboveUpper1) {
							if (allocation0 > allocation1) {
								return 1;
							} else {
								return -1;
							}
						} else {
							return 1;
						}
					} else {
						if (aboveUpper1) {
							return -1;
						} else {
							if (beforeDrop0 > fullCargoLotValue) {
								if (beforeDrop1 > beforeDrop0) {
									return -1;
								} else {
									return 1;
								}
							} else {
								if (beforeDrop1 > fullCargoLotValue) {
									return -1;
								} else {
									if (allocation0 > allocation1) {
										return 1;
									} else {
										return -1;
									}
								}
							}
						}
					}
				}
			}
		}

	}

	@Override
	public Comparator<IMudContainer> getComparator(final LocalDateTime currentDateTime) {
		this.comparator.setCurrentDateTime(currentDateTime);
		return this.comparator;
	}

}

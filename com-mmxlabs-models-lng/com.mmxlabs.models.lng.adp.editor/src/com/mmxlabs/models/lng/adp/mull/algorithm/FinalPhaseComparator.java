package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.Comparator;

import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;

public class FinalPhaseComparator implements Comparator<IMudContainer> {

	private final int allocationDrop;
	private final int fullCargoLotValue;
	
	public FinalPhaseComparator(final int allocationDrop, final int fullCargoLotValue) {
		this.allocationDrop = allocationDrop;
		this.fullCargoLotValue = fullCargoLotValue;
	}

	@Override
	public int compare(IMudContainer mc0, IMudContainer mc1) {
		final Long allocation0 = mc0.getRunningAllocation();
		final Long allocation1 = mc1.getRunningAllocation();
		final int expectedAllocationDrop0 = allocationDrop;
		final int expectedAllocationDrop1 = allocationDrop;
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

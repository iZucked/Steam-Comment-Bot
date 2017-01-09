/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.microcases.pricebasedwindows;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.PriceBasedSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class TimeWindowsTestsUtils {

	public static IPortTimeWindowsRecord getIPortTimeWindowsRecord(String loadName, PriceBasedSequenceScheduler priceBasedSequenceScheduler) {
		IPortTimeWindowsRecord interestingPortTimeWindowsRecord = null;
		List<List<IPortTimeWindowsRecord>> portTimeWindowsRecords = priceBasedSequenceScheduler.getPortTimeWindowsRecords();
		for (List<IPortTimeWindowsRecord> upperList : portTimeWindowsRecords) {
			for (IPortTimeWindowsRecord portTimeWindowsRecord : upperList) {
				for (IPortSlot slot : portTimeWindowsRecord.getSlots()) {
					if (slot.getId().contains(loadName)) {
						interestingPortTimeWindowsRecord = portTimeWindowsRecord;
					}
				}
			}
		}
		return interestingPortTimeWindowsRecord;
	}

	public static IVesselAvailability getIVesselAvailabilityWithName(String name, EList<VesselAvailability> namedObjects, ModelEntityMap mem) {
		for (VesselAvailability object : namedObjects) {
			if (object.getVessel().getName().contains(name))
				return mem.getOptimiserObject(object, IVesselAvailability.class);
		}
		return null;
	}

}

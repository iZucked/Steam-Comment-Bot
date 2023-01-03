/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class TimeWindowsTestsUtils {

	public static IPortTimeWindowsRecord getIPortTimeWindowsRecord(String loadName, ScheduledTimeWindows scheduledTimeWindows) {
		IPortTimeWindowsRecord interestingPortTimeWindowsRecord = null;
		Map<IResource, List<IPortTimeWindowsRecord>> portTimeWindowsRecords = scheduledTimeWindows.getTrimmedTimeWindowsMap();
		for (Map.Entry<IResource, List<IPortTimeWindowsRecord>> upperList : portTimeWindowsRecords.entrySet()) {
			for (IPortTimeWindowsRecord portTimeWindowsRecord : upperList.getValue()) {
				for (IPortSlot slot : portTimeWindowsRecord.getSlots()) {
					if (slot.getId().contains(loadName)) {
						interestingPortTimeWindowsRecord = portTimeWindowsRecord;
					}
				}
			}
		}
		return interestingPortTimeWindowsRecord;
	}

	public static IVesselCharter getIVesselCharterWithName(String name, EList<VesselCharter> namedObjects, ModelEntityMap mem) {
		for (VesselCharter object : namedObjects) {
			if (object.getVessel().getName().contains(name))
				return mem.getOptimiserObject(object, IVesselCharter.class);
		}
		return null;
	}

}

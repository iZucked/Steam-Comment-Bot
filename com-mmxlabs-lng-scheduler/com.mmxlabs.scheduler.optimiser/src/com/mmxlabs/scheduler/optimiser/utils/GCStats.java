/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.utils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

public class GCStats {

	private static long lastCollectionTime = 0;
	
	public static void printGCTime() {
		long collectionTime = getGarbageCollectionTime();
		long printCollectionTime = collectionTime;
		if (lastCollectionTime != 0) printCollectionTime -= lastCollectionTime;
		System.out.println("Total time spent in GC: "+printCollectionTime+" milliseconds");
		lastCollectionTime = collectionTime;
	}
	
	public static long getGCTimeInMillis() {
		long collectionTime = getGarbageCollectionTime();
		long printCollectionTime = collectionTime;
		if (lastCollectionTime != 0) printCollectionTime -= lastCollectionTime;
		lastCollectionTime = collectionTime;
		return printCollectionTime;
	}
	
	private static long getGarbageCollectionTime() {
	    long collectionTime = 0;
	    for (GarbageCollectorMXBean garbageCollectorMXBean : ManagementFactory.getGarbageCollectorMXBeans()) {
	        collectionTime += garbageCollectorMXBean.getCollectionTime();
	    }
	    return collectionTime;
	}
}

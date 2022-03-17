/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

/**
 * Utility class to store memory usage info. 
 * This is used primarily by loggers.
 * 
 * @author simonmcgregor
 *
 */
public class MemoryUsageInfo {
	long totalHeapMemory;
	long freeHeapMemory;
	
	public MemoryUsageInfo(long free, long total) {
		totalHeapMemory = total;
		freeHeapMemory = free;
	}

	public long getFreeHeapMemory() {
		return freeHeapMemory;
	}

	public long getTotalHeapMemory() {
		return totalHeapMemory;
	}


}
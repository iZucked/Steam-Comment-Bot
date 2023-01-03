/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.utils;

public class SystemUtils {

	private SystemUtils() {
		
	}
	
	public static boolean isDebugEnabled() {
		boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") >= 0;
		return isDebug;
	}
}

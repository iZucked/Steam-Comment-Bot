package com.mmxlabs.scheduler.optimiser.utils;

public class SystemUtils {

	public static boolean isDebugEnabled() {
		boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
		return isDebug;
	}
}

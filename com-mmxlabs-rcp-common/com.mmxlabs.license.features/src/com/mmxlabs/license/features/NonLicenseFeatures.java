/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.license.features;

/**
 * Features that we don't want to make license features and we just want to be able to turn on (or off) in the future. 
 * This maybe used when moving features from being license features to being non-license features or just for adding
 * a switch to turn on legacy code, until we decide that it is safe to remove the legacy code from the code base.
 */
public class NonLicenseFeatures {

	private static boolean southboundIdleTimeRuleEnabled = true;
	
	public static void setSouthboundIdleTimeRuleEnabled(boolean enabled) {
		southboundIdleTimeRuleEnabled = enabled;
	}
	
	public static boolean isSouthboundIdleTimeRuleEnabled() {
		return southboundIdleTimeRuleEnabled; 
	}
}

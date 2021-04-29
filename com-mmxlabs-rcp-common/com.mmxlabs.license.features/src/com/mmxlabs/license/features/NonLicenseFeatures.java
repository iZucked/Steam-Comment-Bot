/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.license.features;

/**
 * Features that we don't want to make license features and we just want to be able to turn on (or off) in the future. 
 * This maybe used when moving features from being license features to being non-license features or just for adding
 * a switch to turn on legacy code, until we decide that it is safe to remove the legacy code from the code base.
 */
public class NonLicenseFeatures {
	
	/**
	 * Is now removed.
	 * @return
	 * @deprecated Southbound non idle rule mode no longer exists.
	 */
	public static boolean isSouthboundIdleTimeRuleEnabled() {
		return false; 
	}
}

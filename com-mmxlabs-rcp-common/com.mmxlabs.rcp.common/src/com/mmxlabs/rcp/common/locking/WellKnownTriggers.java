/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.locking;

/**
 * Class to store wellk now global triggers
 * 
 * @author Simon Goodall
 *
 */
public final class WellKnownTriggers {

	public static final SingleUseTrigger WORKSPACE_STARTED = new SingleUseTrigger();

	
	/**
	 * A trigger point used to delay e.g. scenario services and hub data downloaders to wait until the re-encryption process completes
	 */
	public static final SingleUseTrigger WORKSPACE_DATA_ENCRYPTION_CHECK = new SingleUseTrigger();
}

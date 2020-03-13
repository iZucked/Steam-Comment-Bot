/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.util;

import com.mmxlabs.models.ui.validation.ValidationGroup;

/**
 * Shared class of validation constants. It is here rather than in a separate types.validation plugin as it would be the only class...
 * 
 * @author Simon Goodall
 *
 */
public final class ValidationConstants {
	private ValidationConstants() {

	}

	public static final ValidationGroup TAG_TRAVEL_TIME = new ValidationGroup("Travel time", (short) 100);
	public static final ValidationGroup TAG_VOLUME_MISMATCH = new ValidationGroup("Volume Mismatch", (short) 200);
	public static final ValidationGroup TAG_NOMINAL_VESSELS = new ValidationGroup("Nominal cargoes", (short) 300);
	public static final ValidationGroup TAG_ADP = new ValidationGroup("ADP", (short) 400);
	public static final ValidationGroup TAG_EVALUATED_SCHEDULE = new ValidationGroup("Last evaluated schedule", Short.MAX_VALUE, true);
	
}

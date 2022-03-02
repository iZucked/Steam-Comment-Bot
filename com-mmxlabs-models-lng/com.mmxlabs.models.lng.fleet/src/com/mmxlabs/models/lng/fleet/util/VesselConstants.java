/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.jdt.annotation.NonNull;

public class VesselConstants {

	public static final char MMX_REFERENCE_INTERNAL_NAME_PREFIX = '<';
	public static final char MMX_REFERENCE_INTERNAL_NAME_SUFFIX = '>';

	public static final String REGEXP_MMX_PROVIDED_VESSEL_NAME = String.format(".*[%c%c].*", MMX_REFERENCE_INTERNAL_NAME_PREFIX, MMX_REFERENCE_INTERNAL_NAME_SUFFIX);

	private VesselConstants() {
	}

	public static @NonNull String convertMMXReferenceNameToInternalName(@NonNull final String vesselName) {
		return String.format("%c%s%c", MMX_REFERENCE_INTERNAL_NAME_PREFIX, vesselName, MMX_REFERENCE_INTERNAL_NAME_SUFFIX);
	}
}

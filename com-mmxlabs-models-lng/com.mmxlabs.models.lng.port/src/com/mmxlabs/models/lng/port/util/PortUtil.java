/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Port;

public class PortUtil {

	private PortUtil() {
	}

	public static String getShortOrFullName(@NonNull final Port port) {
		final String shortName = port.getShortName();
		if (shortName == null || shortName.isBlank()) {
			return port.getName();
		}
		return shortName;
	}
}

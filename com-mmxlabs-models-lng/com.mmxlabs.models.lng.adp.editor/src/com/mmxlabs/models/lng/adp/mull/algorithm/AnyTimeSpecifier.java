/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class AnyTimeSpecifier implements ILiftTimeSpecifier {

	@Override
	public boolean isValidLiftTime(final LocalDateTime potentialLiftTime) {
		return true;
	}

}

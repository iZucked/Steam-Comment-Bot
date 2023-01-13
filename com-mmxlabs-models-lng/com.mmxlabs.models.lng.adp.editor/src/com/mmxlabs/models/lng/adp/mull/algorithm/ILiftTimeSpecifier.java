/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ILiftTimeSpecifier {

	boolean isValidLiftTime(LocalDateTime potentialLiftTime);
}

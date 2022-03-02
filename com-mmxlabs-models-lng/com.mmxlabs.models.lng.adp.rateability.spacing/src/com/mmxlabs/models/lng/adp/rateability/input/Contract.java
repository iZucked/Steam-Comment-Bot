/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.input;

import java.util.Optional;
import java.util.OptionalInt;

@Deprecated
public class Contract {
	public String name;
	public int contractSpacing;
	public boolean isFOB;
	public int interval;
	public OptionalInt maxInterval = OptionalInt.empty();
	public Optional<RateabilityConstraint> rateabilityConstraint = Optional.empty();
	public Optional<String> port = Optional.empty();
	public OptionalInt turnaroundTime = OptionalInt.empty();
}

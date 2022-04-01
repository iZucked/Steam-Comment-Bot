/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.input;

import java.util.List;
import java.util.Optional;


@Deprecated
public class Cargo {
	public String contract;
	public Optional<List<Integer>> minLoadDate = Optional.empty();
	public Optional<List<Integer>> maxLoadDate = Optional.empty();
	public Optional<List<Integer>> minDischargeDate = Optional.empty();
	public Optional<List<Integer>> maxDischargeDate = Optional.empty();
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.Random;

public class BreakdownSearchData {
	private BreakdownSearchStatistics searchStatistics;
	private Random random;
	
	public BreakdownSearchData(BreakdownSearchStatistics searchStatistics, Random random) {
		this.searchStatistics = searchStatistics;
		this.random = random;
	}
	
	public BreakdownSearchStatistics getSearchStatistics() {
		return searchStatistics;
	}
	public void setSearchStatistics(BreakdownSearchStatistics searchStatistics) {
		this.searchStatistics = searchStatistics;
	}
	public Random getRandom() {
		return random;
	}
	public void setRandom(Random random) {
		this.random = random;
	}
	
}

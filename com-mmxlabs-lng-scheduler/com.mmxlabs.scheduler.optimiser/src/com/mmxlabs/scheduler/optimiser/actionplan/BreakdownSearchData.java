/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionplan;

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

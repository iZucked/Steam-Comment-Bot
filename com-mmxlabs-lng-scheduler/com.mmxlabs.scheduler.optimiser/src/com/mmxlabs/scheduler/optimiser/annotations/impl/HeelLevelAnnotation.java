/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;

/**
 * @author Simon Goodall
 * 
 */
public final class HeelLevelAnnotation implements IHeelLevelAnnotation {
	private final long startHeelInM3;
	private final long endHeelInM3;

	public HeelLevelAnnotation(final long startHeelInM3, final long endHeelInM3) {
		this.startHeelInM3 = startHeelInM3;
		this.endHeelInM3 = endHeelInM3;
	}

	@Override
	public long getStartHeelInM3() {
		return startHeelInM3;
	}

	@Override
	public long getEndHeelInM3() {
		return endHeelInM3;
	}

	@Override
	public String toString() {
		return String.format("Heel [start: %d, end %d]", startHeelInM3, endHeelInM3);
	}
}

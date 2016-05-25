/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(startHeelInM3, endHeelInM3);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof IHeelLevelAnnotation) {
			IHeelLevelAnnotation other = (IHeelLevelAnnotation) obj;
			return this.startHeelInM3 == other.getStartHeelInM3() && this.endHeelInM3 == other.getEndHeelInM3();
		}

		return false;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

public class HeelRecord {
	private final long heelAtStart;
	private final long heelAtEnd;

	public HeelRecord(final long heelAtStart, final long heelAtEnd) {
		this.heelAtStart = heelAtStart;
		this.heelAtEnd = heelAtEnd;
	}

	public long getHeelAtStartInM3() {
		return heelAtStart;
	}

	public long getHeelAtEndInM3() {
		return heelAtEnd;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof HeelRecord) {
			HeelRecord other = (HeelRecord) obj;
			if (heelAtStart != other.heelAtStart) {
				return false;
			}
			if (heelAtEnd != other.heelAtEnd) {
				return false;
			}

			return true;
		}

		return false;
	}
}
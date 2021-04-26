/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import com.google.common.base.Objects;

public final class PreviousHeelRecord {
	public final long heelVolumeInM3;
	public final int lastHeelPricePerMMBTU;
	public final int lastCV;
	public final boolean forcedCooldown;

	/**
	 * Default constructor - more suitable for unit tests
	 */
	public PreviousHeelRecord() {
		this(0, 0, 0, false);
	}

	public PreviousHeelRecord(final long heelVolumeInM3, final int lastHeelPricePerMMBTU, final int lastCV, final boolean forcedCooldown) {
		this.heelVolumeInM3 = heelVolumeInM3;
		this.lastHeelPricePerMMBTU = lastHeelPricePerMMBTU;
		this.lastCV = lastCV;
		this.forcedCooldown = forcedCooldown;
	}
	
	public PreviousHeelRecord(final PreviousHeelRecord other) {
		this.heelVolumeInM3 = other.heelVolumeInM3;
		this.lastHeelPricePerMMBTU = other.lastHeelPricePerMMBTU;
		this.lastCV = other.lastCV;
		this.forcedCooldown = other.forcedCooldown;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof PreviousHeelRecord) {
			final PreviousHeelRecord other = (PreviousHeelRecord) obj;
			return forcedCooldown == other.forcedCooldown //
					&& lastCV == other.lastCV //
					&& heelVolumeInM3 == other.heelVolumeInM3 //
					&& lastHeelPricePerMMBTU == other.lastHeelPricePerMMBTU;

		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(forcedCooldown, lastCV, lastHeelPricePerMMBTU, heelVolumeInM3);
	}
}
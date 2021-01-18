/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import com.google.common.base.Objects;

public class PreviousHeelRecord {
	public long heelVolumeInM3;
	public int lastHeelPricePerMMBTU = 0;
	public int lastCV = 0;
	public boolean forcedCooldown;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof PreviousHeelRecord) {
			PreviousHeelRecord other = (PreviousHeelRecord) obj;
			return forcedCooldown == other.forcedCooldown && lastCV == other.lastCV //
					&& heelVolumeInM3 == other.heelVolumeInM3 && lastHeelPricePerMMBTU == other.lastHeelPricePerMMBTU;

		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(forcedCooldown, lastCV, lastHeelPricePerMMBTU, heelVolumeInM3);
	}
}
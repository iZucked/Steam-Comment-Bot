package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

public class ValueMatrixResultDiffData {

	private final boolean hasBaseLoadPriceDiff;
	private final boolean hasSwapLoadPriceDiff;
	private final boolean hasBaseLoadVolumeDiff;
	private final boolean hasSwapLoadVolumeDiff;
	private final boolean hasBaseDischargeVolumeDiff;
	private final boolean hasSwapDischargeVolumeDiff;
	private final boolean hasDesSaleMarketVolumeDiff;
	private final boolean hasPrecedingKnockOnDiff;
	private final boolean hasSucceedingKnockOnDiff;

	public ValueMatrixResultDiffData(final boolean hasBaseLoadPriceDiff, final boolean hasSwapLoadPriceDiff, final boolean hasBaseLoadVolumeDiff, final boolean hasSwapLoadVolumeDiff,
			final boolean hasBaseDischargeVolumeDiff, final boolean hasSwapDischargeVolumeDiff, final boolean hasDesSaleMarketVolumeDiff, final boolean hasPrecedingKnockOnDiff,
			final boolean hasSucceedingKnockOnDiff) {
		this.hasBaseLoadPriceDiff = hasBaseLoadPriceDiff;
		this.hasSwapLoadPriceDiff = hasSwapLoadPriceDiff;
		this.hasBaseLoadVolumeDiff = hasBaseLoadVolumeDiff;
		this.hasSwapLoadVolumeDiff = hasSwapLoadVolumeDiff;
		this.hasBaseDischargeVolumeDiff = hasBaseDischargeVolumeDiff;
		this.hasSwapDischargeVolumeDiff = hasSwapDischargeVolumeDiff;
		this.hasDesSaleMarketVolumeDiff = hasDesSaleMarketVolumeDiff;
		this.hasPrecedingKnockOnDiff = hasPrecedingKnockOnDiff;
		this.hasSucceedingKnockOnDiff = hasSucceedingKnockOnDiff;
	}

	public boolean hasBaseLoadPriceDiff() {
		return this.hasBaseLoadPriceDiff;
	}

	public boolean hasSwapLoadPriceDiff() {
		return this.hasSwapLoadPriceDiff;
	}

	public boolean hasBaseLoadVolumeDiff() {
		return this.hasBaseLoadVolumeDiff;
	}

	public boolean hasSwapLoadVolumeDiff() {
		return this.hasSwapLoadVolumeDiff;
	}

	public boolean hasBaseDischargeVolumeDiff() {
		return this.hasBaseDischargeVolumeDiff;
	}

	public boolean hasSwapDischargeVolumeDiff() {
		return this.hasSwapDischargeVolumeDiff;
	}

	public boolean hasDesSaleMarketVolumeDiff() {
		return this.hasDesSaleMarketVolumeDiff;
	}

	public boolean hasPrecedingKnockOnDiff() {
		return this.hasPrecedingKnockOnDiff;
	}

	public boolean hasSucceedingKnockOnDiff() {
		return this.hasSucceedingKnockOnDiff;
	}
}

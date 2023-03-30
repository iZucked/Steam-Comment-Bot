/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;

public class SwapValueChangeKey {

	private final double baseLoadPrice;
	private final double swapLoadPrice;
	private final long baseLoadVolume;
	private final long swapLoadVolume;
	private final long baseDischargeVolume;
	private final long swapDischargeVolume;
	private final long desSaleMarketVolume;
	private final long precedingKnockOn;
	private final long succeedingKnockOn;

	public SwapValueChangeKey(@NonNull final SwapValueMatrixResult result) {
		this.baseLoadPrice = result.getBaseCargo().getLoadPrice();
		this.swapLoadPrice = result.getSwapDiversionCargo().getLoadPrice();
		this.baseLoadVolume = result.getBaseCargo().getLoadVolume();
		this.swapLoadVolume = result.getSwapDiversionCargo().getLoadVolume();
		this.baseDischargeVolume = result.getBaseCargo().getDischargeVolume();
		this.swapDischargeVolume = result.getSwapBackfillCargo().getVolume();
		this.desSaleMarketVolume = result.getSwapDiversionCargo().getDischargeVolume();
		this.precedingKnockOn = result.getSwapPrecedingPnl() - result.getBasePrecedingPnl();
		this.succeedingKnockOn = result.getSwapSucceedingPnl() - result.getBaseSucceedingPnl();
	}

	public long getBaseDischargeVolume() {
		return baseDischargeVolume;
	}

	public double getBaseLoadPrice() {
		return baseLoadPrice;
	}

	public long getBaseLoadVolume() {
		return baseLoadVolume;
	}

	public long getDesSaleMarketVolume() {
		return desSaleMarketVolume;
	}

	public long getPrecedingKnockOn() {
		return precedingKnockOn;
	}

	public long getSucceedingKnockOn() {
		return succeedingKnockOn;
	}

	public long getSwapDischargeVolume() {
		return swapDischargeVolume;
	}

	public double getSwapLoadPrice() {
		return swapLoadPrice;
	}

	public long getSwapLoadVolume() {
		return swapLoadVolume;
	}

	@Override
	public int hashCode() {
		return Objects.hash( //
				baseLoadPrice, swapLoadPrice, //
				baseLoadVolume, swapLoadVolume, //
				baseDischargeVolume, swapDischargeVolume, //
				desSaleMarketVolume, //
				precedingKnockOn, succeedingKnockOn //
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return obj instanceof @NonNull final SwapValueChangeKey otherKey //
				&& this.baseLoadPrice == otherKey.getBaseLoadPrice() //
				&& this.swapLoadPrice == otherKey.getSwapLoadPrice() //
				&& this.baseLoadVolume == otherKey.getBaseLoadVolume() //
				&& this.swapLoadVolume == otherKey.getSwapLoadVolume() //
				&& this.baseDischargeVolume == otherKey.getBaseDischargeVolume() //
				&& this.swapDischargeVolume == otherKey.getSwapDischargeVolume() //
				&& this.desSaleMarketVolume == otherKey.getDesSaleMarketVolume() //
				&& this.precedingKnockOn == otherKey.getPrecedingKnockOn() //
				&& this.succeedingKnockOn == otherKey.getSucceedingKnockOn() //
		;
	}
}

/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * @author hinton
 *
 */
public class AllocationAnnotation implements IAllocationAnnotation {
	private long dischargeVolume;
	private long dischargeCV;
	private int unitProfit;
	private long totalProfit;
	public long getDischargeVolume() {
		return dischargeVolume;
	}
	public void setDischargeVolume(long dischargeVolume) {
		this.dischargeVolume = dischargeVolume;
	}
	public long getDischargeCV() {
		return dischargeCV;
	}
	public void setDischargeCV(long dischargeCV) {
		this.dischargeCV = dischargeCV;
	}
	public int getUnitProfit() {
		return unitProfit;
	}
	public void setUnitProfit(int unitProfit) {
		this.unitProfit = unitProfit;
	}
	public long getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(long totalProfit) {
		this.totalProfit = totalProfit;
	}
}

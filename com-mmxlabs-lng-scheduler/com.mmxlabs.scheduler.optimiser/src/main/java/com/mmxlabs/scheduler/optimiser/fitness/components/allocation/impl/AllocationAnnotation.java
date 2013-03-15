/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * @author hinton
 * 
 */
public class AllocationAnnotation implements IAllocationAnnotation {
	private ILoadOption loadSlot;
	private IDischargeOption dischargeSlot;

	private long fuelVolumeInM3;
	private long dischargeVolumeInM3;

	private int loadTime;
	private int dischargeTime;

	private int loadPricePerM3;
	private int dischargePricePerM3;

	@Override
	public ILoadOption getLoadOption() {
		return loadSlot;
	}

	public void setLoadSlot(final ILoadOption loadSlot) {
		this.loadSlot = loadSlot;
	}

	@Override
	public IDischargeOption getDischargeOption() {
		return dischargeSlot;
	}

	public void setDischargeSlot(final IDischargeOption dischargeSlot) {
		this.dischargeSlot = dischargeSlot;
	}

	@Override
	public long getDischargeVolumeInM3() {
		return dischargeVolumeInM3;
	}

	public void setDischargeVolumeInM3(final long dischargeVolume) {
		this.dischargeVolumeInM3 = dischargeVolume;
	}

	@Override
	public long getFuelVolumeInM3() {
		return fuelVolumeInM3;
	}

	public void setFuelVolumeInM3(final long fuelVolume) {
		this.fuelVolumeInM3 = fuelVolume;
	}

	@Override
	public long getLoadVolumeInM3() {
		return getFuelVolumeInM3() + getDischargeVolumeInM3();
	}

	@Override
	public int getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(final int loadTime) {
		this.loadTime = loadTime;
	}

	@Override
	public int getDischargeTime() {
		return dischargeTime;
	}

	public void setDischargeTime(final int dischargeTime) {
		this.dischargeTime = dischargeTime;
	}

	@Override
	public int getLoadPricePerM3() {
		return loadPricePerM3;
	}

	public void setLoadPricePerM3(final int loadM3Price) {
		this.loadPricePerM3 = loadM3Price;
	}

	@Override
	public int getDischargePricePerM3() {
		return dischargePricePerM3;
	}

	public void setDischargePricePerM3(final int dischargeM3Price) {
		this.dischargePricePerM3 = dischargeM3Price;
	}

	@Override
	public String toString() {
		return loadSlot.getId() + "@" + loadTime + " to " + dischargeSlot.getId() + "@" + dischargeTime + ", loaded " + getLoadVolumeInM3() + ", used " + getFuelVolumeInM3()
				+ " for fuel, discharged " + getDischargeVolumeInM3();
	}
}

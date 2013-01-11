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

	private long fuelVolume;
	private long dischargeVolume;

	private int loadTime;
	private int dischargeTime;

	private int loadM3Price;
	private int dischargeM3Price;

	@Override
	public long getDischargeVolume() {
		return dischargeVolume;
	}

	public void setDischargeVolume(final long dischargeVolume) {
		this.dischargeVolume = dischargeVolume;
	}

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
	public long getFuelVolume() {
		return fuelVolume;
	}

	public void setFuelVolume(final long fuelVolume) {
		this.fuelVolume = fuelVolume;
	}

	@Override
	public long getLoadVolume() {
		return getFuelVolume() + getDischargeVolume();
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
	public int getLoadM3Price() {
		return loadM3Price;
	}

	public void setLoadM3Price(final int loadM3Price) {
		this.loadM3Price = loadM3Price;
	}

	@Override
	public int getDischargeM3Price() {
		return dischargeM3Price;
	}

	public void setDischargeM3Price(final int dischargeM3Price) {
		this.dischargeM3Price = dischargeM3Price;
	}

	@Override
	public String toString() {
		return loadSlot.getId() + "@" + loadTime + " to " + dischargeSlot.getId() + "@" + dischargeTime + ", loaded " + getLoadVolume() + ", used " + getFuelVolume() + " for fuel, discharged "
				+ getDischargeVolume();
	}
}

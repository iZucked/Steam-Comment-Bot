/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * @author hinton
 *
 */
public class AllocationAnnotation implements IAllocationAnnotation {
	private ILoadSlot loadSlot;
	private IDischargeSlot dischargeSlot;
	
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
	public void setDischargeVolume(long dischargeVolume) {
		this.dischargeVolume = dischargeVolume;
	}
	
	@Override
	public ILoadSlot getLoadSlot() {
		return loadSlot;
	}
	public void setLoadSlot(ILoadSlot loadSlot) {
		this.loadSlot = loadSlot;
	}
	@Override
	public IDischargeSlot getDischargeSlot() {
		return dischargeSlot;
	}
	public void setDischargeSlot(IDischargeSlot dischargeSlot) {
		this.dischargeSlot = dischargeSlot;
	}
	@Override
	public long getFuelVolume() {
		return fuelVolume;
	}
	public void setFuelVolume(long fuelVolume) {
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
	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
	}
	@Override
	public int getDischargeTime() {
		return dischargeTime;
	}
	public void setDischargeTime(int dischargeTime) {
		this.dischargeTime = dischargeTime;
	}
	@Override
	public int getLoadM3Price() {
		return loadM3Price;
	}
	public void setLoadM3Price(int loadM3Price) {
		this.loadM3Price = loadM3Price;
	}
	@Override
	public int getDischargeM3Price() {
		return dischargeM3Price;
	}
	public void setDischargeM3Price(int dischargeM3Price) {
		this.dischargeM3Price = dischargeM3Price;
	}
	
	@Override
	public String toString() {
		return loadSlot.getId() + "@" + loadTime + " to " + dischargeSlot.getId() + "@"+dischargeTime+", loaded " + getLoadVolume() + ", used " + getFuelVolume() + " for fuel, discharged " + getDischargeVolume();
	}
}

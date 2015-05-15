/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class GeneratedCharterOutVesselEvent implements IGeneratedCharterOutVesselEvent {
	private ITimeWindow timeWindow;
	private int durationHours;
	private IPort startPort, endPort;
	private long maxHeelOut;
	private int heelCVValue;
	private int heelUnitPrice;
	private long hireCost;
	private long repositioning;

	public GeneratedCharterOutVesselEvent() {
		super();
	}

	@Override
	public void setTimeWindow(final ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	@Override
	public void setDurationHours(final int durationHours) {
		this.durationHours = durationHours;
	}

	@Override
	public void setStartPort(final IPort startPort) {
		this.startPort = startPort;
	}

	@Override
	public void setEndPort(final IPort endPort) {
		this.endPort = endPort;
	}

	public void setMaxHeelOut(final long maxHeelOut) {
		this.maxHeelOut = maxHeelOut;
	}

	public void setHeelCVValue(final int heelCVValue) {
		this.heelCVValue = heelCVValue;
	}

	public void setHeelUnitPrice(final int heelUnitPrice) {
		this.heelUnitPrice = heelUnitPrice;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public int getDurationHours() {
		return durationHours;
	}

	@Override
	public IPort getStartPort() {
		return startPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.IVesselEvent#getEndPort()
	 */
	@Override
	public IPort getEndPort() {
		return endPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.IVesselEvent#getMaxHeelOut()
	 */
	@Override
	public long getHeelLimit() {
		return maxHeelOut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.IVesselEvent#getHeelCVValue()
	 */
	@Override
	public int getHeelCVValue() {
		return heelCVValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.IHeelOptions#getHeelUnitPrice()
	 */
	@Override
	public int getHeelUnitPrice() {
		return heelUnitPrice;
	}

	/**
	 */
	@Override
	public long getHireOutRevenue() {
		return hireCost;
	}

	/**
	 */
	@Override
	public long getRepositioning() {
		return repositioning;
	}

	/**
	 */
	@Override
	public void setHireOutRevenue(final long hireCost) {
		this.hireCost = hireCost;
	}

	/**
	 */
	@Override
	public void setRepositioning(final long repositioning) {
		this.repositioning = repositioning;
	}
	
	@Override
	public void setHeelOptions(int pricePerMBTU, int cv, long volumeInM3) {
		setHeelUnitPrice(pricePerMBTU);
		setHeelCVValue(cv);
		setMaxHeelOut(volumeInM3);
	}

}

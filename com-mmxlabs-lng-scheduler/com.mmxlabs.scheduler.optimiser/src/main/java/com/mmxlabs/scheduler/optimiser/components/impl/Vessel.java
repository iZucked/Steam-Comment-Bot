/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

/**
 * Default implementation of {@link IVessel}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Vessel extends IndexedObject implements IVessel {

	public Vessel(final IIndexingContext provider) {
		super(provider);
	}

	private String name;

	private IVesselClass vesselClass;

	private VesselInstanceType vesselInstanceType = VesselInstanceType.UNKNOWN;

	private int hourlyCharterOutPrice;

	private int hourlyCharterInPrice;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public IVesselClass getVesselClass() {
		return vesselClass;
	}

	public void setVesselClass(final IVesselClass vesselClass) {
		this.vesselClass = vesselClass;
	}

	@Override
	public VesselInstanceType getVesselInstanceType() {
		return vesselInstanceType;
	}

	public void setVesselInstanceType(final VesselInstanceType vesselInstanceType) {
		this.vesselInstanceType = vesselInstanceType;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int getHourlyCharterOutPrice() {
		return hourlyCharterOutPrice;
	}

	public void setHourlyCharterOutPrice(final int hourlyCharterOutPrice) {
		this.hourlyCharterOutPrice = hourlyCharterOutPrice;
	}

	@Override
	public int getHourlyCharterInPrice() {
		return hourlyCharterInPrice;
	}

	public void setHourlyCharterInPrice(final int hourlyCharterInPrice) {
		this.hourlyCharterInPrice = hourlyCharterInPrice;
	}
}

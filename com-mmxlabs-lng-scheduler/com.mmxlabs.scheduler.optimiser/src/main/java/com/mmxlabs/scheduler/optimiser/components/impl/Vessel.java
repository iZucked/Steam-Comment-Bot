/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedOrderedSequenceElementsEditor;
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

	public Vessel(IIndexingContext provider) {
		super(provider);
	}

	private String name;

	private IVesselClass vesselClass;

	private VesselInstanceType vesselInstanceType = VesselInstanceType.UNKNOWN;

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

	public void setVesselInstanceType(
			final VesselInstanceType vesselInstanceType) {
		this.vesselInstanceType = vesselInstanceType;
	}

	public String toString() {
		return getName();
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LightWeightCargoDetails {
	PortType type;
	boolean associatedToVessel;
	int vessel;
	
	public LightWeightCargoDetails(PortType type, int vessel) {
		this.type = type;
		this.associatedToVessel = true;
		this.vessel = vessel;
	}
	
	public LightWeightCargoDetails(PortType type) {
		this.type = type;
		this.associatedToVessel = false;
	}
	
	public PortType getType() {
		return type;
	}
	
	public void setType(PortType type) {
		this.type = type;
	}
	
	public boolean isAssociatedToVessel() {
		return associatedToVessel;
	}
	
	public int getVessel() {
		return vessel;
	}
	
	public void setVessel(int vessel) {
		this.vessel = vessel;
		this.associatedToVessel = true;
	}	
}

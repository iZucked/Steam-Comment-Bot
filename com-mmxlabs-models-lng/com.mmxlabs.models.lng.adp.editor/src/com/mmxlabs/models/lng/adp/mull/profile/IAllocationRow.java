/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public interface IAllocationRow {
	
	public int getWeight();
	
	public List<Vessel> getVessels();
}

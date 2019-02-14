/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

/**
 * 
 * @author Alex Churchill
 */
public class VesselUtilisationFitnessCoreFactory implements IFitnessCoreFactory {

	public static final String NAME = "UtilisationFitnessCore";

	@Override
	public String getFitnessCoreName() {
		return NAME;
	}

	@Override
	public Collection<String> getFitnessComponentNames() {
		return Collections.singleton(NAME);
	}

	@Override
	public IFitnessCore instantiate() {
		return new VesselUtilisationFitnessCore(NAME);
	}
}

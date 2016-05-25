/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

/**
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public class NonOptionalSlotFitnessCoreFactory implements IFitnessCoreFactory {

	@NonNull
	public static final String NAME = "NonOptionalSlotFitness";

	@Override
	@NonNull
	public String getFitnessCoreName() {
		return NAME;
	}

	@Override
	@NonNull
	public Collection<String> getFitnessComponentNames() {
		return CollectionsUtil.makeArrayList(NAME);
	}

	@Override
	@NonNull
	public IFitnessCore instantiate() {
		return new NonOptionalSlotFitnessCore(NAME);
	}
}

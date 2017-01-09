/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} definition providing a {@link PortType} for sequence elements. This is intended to be combined with a {@link IConstraintChecker} or {@link IFitnessCore} to enforce a
 * particular sequencing of port types.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortTypeProvider extends IDataComponentProvider {

	/**
	 * Return the type of port for the given sequence element. Returns {@link PortType#Unknown} if not specified.
	 * 
	 * @param sequenceElement
	 * @return
	 */
	@NonNull
	PortType getPortType(@NonNull ISequenceElement sequenceElement);
}

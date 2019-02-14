/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * A Data provider to return permitted vessels and vessel classes for a port slot. A null result mean no restriction where as an empty result means nothing is permitted.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public interface IAllowedVesselProvider extends IDataComponentProvider {

	@Nullable
	Collection<IVessel> getPermittedVessels(IPortSlot portSlot);

	boolean isPermittedOnVessel(IPortSlot portSlot, @Nullable IVessel vessel);

}

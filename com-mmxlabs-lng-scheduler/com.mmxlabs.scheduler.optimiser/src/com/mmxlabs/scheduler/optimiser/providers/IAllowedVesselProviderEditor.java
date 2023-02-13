/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

@NonNullByDefault
public interface IAllowedVesselProviderEditor extends IAllowedVesselProvider {

	void setPermittedVesselAndClasses(IPortSlot portSlot, @Nullable Collection<IVessel> permittedVessels);
}

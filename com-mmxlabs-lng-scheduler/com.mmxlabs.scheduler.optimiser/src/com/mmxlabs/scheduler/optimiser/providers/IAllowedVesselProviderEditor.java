/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

@NonNullByDefault
public interface IAllowedVesselProviderEditor extends IAllowedVesselProvider {

	void setPermittedVesselAndClasses(IPortSlot portSlot, @Nullable Collection<IVessel> permittedVessels, @Nullable Collection<IVesselClass> permittedVesselClasses);
}

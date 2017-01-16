/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 */
public interface IShippingHoursRestrictionProvider extends IDataComponentProvider {

	/**
	 * Constant indicating no restriction has been defined.
	 */
	final int RESTRICTION_UNDEFINED = 0;

	boolean isDivertable(@NonNull ISequenceElement element);

	/**
	 * Returns the number of hours for the shipping restriction. If not specified, then {@link #RESTRICTION_UNDEFINED} will be returned.
	 * 
	 * @param element
	 * @return
	 */
	int getShippingHoursRestriction(@NonNull ISequenceElement element);

	/**
	 * Returns the {@link ITimeWindow} for the FOB load for a DES Purchase or the DES Sale time for a FOB Sale. This is the base time for the shipping restriction.
	 * 
	 * @param element
	 * @return
	 */
	ITimeWindow getBaseTime(@NonNull ISequenceElement element);

	/**
	 * Returns the reference speed to use for the minimum travel time between load and discharge events for divertible non-shipped cargoes.
	 * 
	 * @param vessel
	 * @return
	 */
	int getReferenceSpeed(@NonNull IVessel vessel, @NonNull VesselState vesselState);

	@NonNull
	Collection<ERouteOption> getDivertableDESAllowedRoutes(@NonNull ILoadOption loadOption);
}

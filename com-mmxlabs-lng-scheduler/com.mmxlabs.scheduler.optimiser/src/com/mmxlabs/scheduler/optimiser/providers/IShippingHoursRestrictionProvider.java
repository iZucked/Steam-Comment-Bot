package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

public interface IShippingHoursRestrictionProvider extends IDataComponentProvider {

	/**
	 * Constant indicating no restriction has been defined.
	 */
	final int RESTRICTION_UNDEFINED = 0;

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
}

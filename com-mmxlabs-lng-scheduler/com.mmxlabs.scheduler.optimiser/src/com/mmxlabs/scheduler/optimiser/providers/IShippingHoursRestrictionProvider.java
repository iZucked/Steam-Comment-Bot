package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

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
}

package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.inject.scopes.NotInjectedScope;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

@NotInjectedScope
public interface IPanamaAllowedBookingsProvider {
	@NonNullByDefault
	boolean isPanamaBookingAllowed(IRouteOptionBooking booking);
}

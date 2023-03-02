package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.inject.scopes.NotInjectedScope;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

@NotInjectedScope
public class AllowedPanamaBookingsProviderImpl implements IAllowedPanamaBookingsProvider {

	private final @NonNull Set<@NonNull IRouteOptionBooking> allowedBookings = new HashSet<>();

	public void setAllowedBookings(@NonNull Collection<@NonNull IRouteOptionBooking> bookings) {
		allowedBookings.clear();
		allowedBookings.addAll(bookings);	
	}

	@Override
	public @NonNull Collection<@NonNull IRouteOptionBooking> getAllowedPanamaBookings() {
		return allowedBookings;
	}
	
}

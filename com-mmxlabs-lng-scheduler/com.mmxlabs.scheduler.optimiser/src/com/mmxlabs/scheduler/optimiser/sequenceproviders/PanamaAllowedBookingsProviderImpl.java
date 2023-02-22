package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.inject.scopes.NotInjectedScope;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

@NotInjectedScope
public class PanamaAllowedBookingsProviderImpl implements IPanamaAllowedBookingsProvider {

	private final Set<IRouteOptionBooking> allowedBookings = new HashSet<>();
	
	@Override
	public boolean isPanamaBookingAllowed(IRouteOptionBooking booking) {
		return allowedBookings.contains(booking);
	}

	public void setAllowedBookings(@NonNull Collection<@NonNull IRouteOptionBooking> bookings) {
		allowedBookings.clear();
		allowedBookings.addAll(bookings);
		
	}


}

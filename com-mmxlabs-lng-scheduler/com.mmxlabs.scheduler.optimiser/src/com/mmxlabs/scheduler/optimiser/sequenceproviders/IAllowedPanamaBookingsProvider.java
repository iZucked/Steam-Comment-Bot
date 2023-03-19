/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.inject.scopes.NotInjectedScope;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

@NotInjectedScope
@NonNullByDefault
public interface IAllowedPanamaBookingsProvider {
	Collection<IRouteOptionBooking> getAllowedPanamaBookings();
}

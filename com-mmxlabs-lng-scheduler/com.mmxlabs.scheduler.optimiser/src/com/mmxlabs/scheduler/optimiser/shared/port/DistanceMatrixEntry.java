/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

/**
 * Class representing an entry from a {@link IDistanceMatrixProvider}.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class DistanceMatrixEntry {

	private final ERouteOption route;

	private final IPort from;

	private final IPort to;

	private final int distance;

	public DistanceMatrixEntry(final ERouteOption route, final IPort from, final IPort to, final int distance) {
		this.route = route;
		this.from = from;
		this.to = to;
		this.distance = distance;
	}

	public final ERouteOption getRoute() {
		return route;
	}

	public final IPort getFrom() {
		return from;
	}

	public final IPort getTo() {
		return to;
	}

	public final int getDistance() {
		return distance;
	}
}
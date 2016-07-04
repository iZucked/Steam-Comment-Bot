/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

public enum RouteChoice {
	Direct, Suez, Panama, Optimal;

	@NonNull
	public static RouteChoice mapToChoice(@NonNull ERouteOption eRouteOption) {
		switch (eRouteOption) {
		case DIRECT:
			return RouteChoice.Direct;
		case PANAMA:
			return RouteChoice.Panama;
		case SUEZ:
			return RouteChoice.Suez;
		}
		throw new IllegalArgumentException("Unknown route option");
	}

	@NonNull
	public static ERouteOption mapToOption(@NonNull RouteChoice routeChoice) {
		switch (routeChoice) {
		case Direct:
			return ERouteOption.DIRECT;
		case Panama:
			return ERouteOption.PANAMA;
		case Suez:
			return ERouteOption.SUEZ;
		case Optimal:
			throw new IllegalArgumentException("Optimal is not a valid route choice");
		}
		throw new IllegalArgumentException("Unknown route choice");
	}

}

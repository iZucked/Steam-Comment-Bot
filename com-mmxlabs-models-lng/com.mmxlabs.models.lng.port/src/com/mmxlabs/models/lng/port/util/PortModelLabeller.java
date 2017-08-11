package com.mmxlabs.models.lng.port.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.RouteOption;

public class PortModelLabeller {
	public static @NonNull String getName(@NonNull RouteOption routeOption) {
		switch (routeOption) {
		case DIRECT:
			return "Direct";
		case PANAMA:
			return "Panama";
		case SUEZ:
			return "Suez";
		}
		throw new IllegalStateException();
	}
}

package com.mmxlabs.lngdataserver.lng.importers.distances;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lngdataserver.integration.distances.Via;
import com.mmxlabs.models.lng.port.Route;

@NonNullByDefault
public class ViaKeyMapper {
	public static String getKey(final Via via) {
		switch (via) {
		case Direct:
			return "DIRECT";
		case PanamaCanal:
			return "PANAMA";
		case SuezCanal:
			return "SUEZ";
		}
		throw new IllegalArgumentException();
	}

	public static Via getVia(final String name) {
		switch (name.toUpperCase()) {
		case "DIRECT":
			return Via.Direct;
		case "PANAMA":
			return Via.PanamaCanal;
		case "SUEZ":
			return Via.SuezCanal;
		}
		throw new IllegalArgumentException();
	}

	public static Via mapVia(final Route route) {
		switch (route.getRouteOption()) {
		case DIRECT:
			return Via.Direct;
		case PANAMA:
			return Via.PanamaCanal;
		case SUEZ:
			return Via.SuezCanal;
		}
		throw new IllegalStateException();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;

@NonNullByDefault
public class PortModelLabeller {

	private PortModelLabeller() {
	}

	public static String getName(final RouteOption routeOption) {
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

	public static String getName(final CanalEntry canalEntry) {
		switch (canalEntry) {
		case NORTHSIDE:
			return "Northside";
		case SOUTHSIDE:
			return "Southside";
		}
		throw new IllegalStateException();
	}

	// Returns directions labels (opposite to entry point, i.e. northbound direction
	// is southside entry)
	public static String getDirection(final CanalEntry canalEntry) {
		switch (canalEntry) {
		case NORTHSIDE:
			return "Southbound";
		case SOUTHSIDE:
			return "Northbound";
		}
		throw new IllegalStateException();
	}

	public static String getUserName(final RouteOption routeOption, final CanalEntry canalEntry, final PortModel portModel) {
		for (Route route : portModel.getRoutes()) {
			if (route.getRouteOption() == routeOption) {
				EntryPoint entryPoint = null;
				switch (canalEntry) {
				case NORTHSIDE:
					entryPoint = route.getNorthEntrance();
				case SOUTHSIDE:
					entryPoint = route.getSouthEntrance();
				}
				if (entryPoint != null) {
					return entryPoint.getName();
				}
			}
		}
		return "";
	}

	public static String getShortName(final RouteOption routeOption) {
		switch (routeOption) {
		case DIRECT:
			return "Dir";
		case PANAMA:
			return "Pan";
		case SUEZ:
			return "Suez";
		}
		throw new IllegalStateException();
	}
}

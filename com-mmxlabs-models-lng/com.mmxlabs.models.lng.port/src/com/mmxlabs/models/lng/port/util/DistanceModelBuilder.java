/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;

public class DistanceModelBuilder {

	public static final String TIMEZONE_UTC = "UTC";

	private final @NonNull PortModel portModel;

	public DistanceModelBuilder(@NonNull final PortModel portModel) {
		this.portModel = portModel;
	}

	// public @NonNull DistanceModel getDistanceModel() {
	// return distanceModel;
	// }

	public void createDistanceMatrix(RouteOption routeOption) {
		// DO nothing for now

	}

	// @NonNull
	// public DistanceMatrix createDistanceMatrix(final RouteOption option) {
	// final DistanceMatrix matrices = PortFactory.eINSTANCE.createDistanceMatrix();
	// matrices.setRouteOption(option);
	// distanceModel.getMatrices().add(matrices);
	// return matrices;
	// }

	public void setPortToPortDistance(@NonNull final Port from, @NonNull final Port to, final int directDistance, final int suezDistance, final int panamaDistance, final boolean biDirectional) {

		for (final Route route : portModel.getRoutes()) {
			boolean foundForwardDistance = false;
			boolean foundReverseDistance = false;
			for (final RouteLine RouteLine : route.getLines()) {
				if (Objects.equals(RouteLine.getFrom(), from) && Objects.equals(RouteLine.getTo(), to)) {
					foundForwardDistance = true;
					setDistance(directDistance, suezDistance, panamaDistance, route.getRouteOption(), RouteLine);
				}
				if (biDirectional) {
					if (Objects.equals(RouteLine.getFrom(), to) && Objects.equals(RouteLine.getTo(), from)) {
						foundReverseDistance = true;
						setDistance(directDistance, suezDistance, panamaDistance, route.getRouteOption(), RouteLine);
					}
				}
			}
			// Add missing distance lines if needed
			if (!foundForwardDistance) {
				final RouteLine line = PortFactory.eINSTANCE.createRouteLine();
				line.setFrom(from);
				line.setTo(to);
				setDistance(directDistance, suezDistance, panamaDistance, route.getRouteOption(), line);
				route.getLines().add(line);
			}
			if (biDirectional && !foundReverseDistance) {
				final RouteLine line = PortFactory.eINSTANCE.createRouteLine();
				line.setFrom(to);
				line.setTo(from);
				setDistance(directDistance, suezDistance, panamaDistance, route.getRouteOption(), line);
				route.getLines().add(line);
			}
		}
	}

	protected void setDistance(final int directDistance, final int suezDistance, final int panamaDistance, final @NonNull RouteOption routeOptionOption, @NonNull final RouteLine routeLine) {
		switch (routeOptionOption) {
		case DIRECT:
			routeLine.setDistance(directDistance);
			break;
		case PANAMA:
			routeLine.setDistance(panamaDistance);
			break;
		case SUEZ:
			routeLine.setDistance(suezDistance);
			break;
		default:
			throw new IllegalStateException();
		}
	}

	public boolean setPortToPortDistance(@NonNull Port from, @NonNull Port to, RouteOption routeOption, int distance, boolean biDirectional) {
		int setDistance = 0;
		for (final Route route : portModel.getRoutes()) {
			if (route.getRouteOption() != routeOption) {
				continue;
			}
			boolean foundForwardDistance = false;
			boolean foundReverseDistance = false;
			for (final RouteLine RouteLine : route.getLines()) {
				if (Objects.equals(RouteLine.getFrom(), from) && Objects.equals(RouteLine.getTo(), to)) {
					foundForwardDistance = true;
					RouteLine.setDistance(distance);
					++setDistance;
				}
				if (biDirectional) {
					if (Objects.equals(RouteLine.getFrom(), to) && Objects.equals(RouteLine.getTo(), from)) {
						foundReverseDistance = true;
						RouteLine.setDistance(distance);
						++setDistance;
					}
				}
			}
			// Add missing distance lines if needed
			if (!foundForwardDistance) {
				final RouteLine RouteLine = PortFactory.eINSTANCE.createRouteLine();
				RouteLine.setFrom(from);
				RouteLine.setTo(to);
				RouteLine.setDistance(distance);
				route.getLines().add(RouteLine);
				++setDistance;
			}
			if (biDirectional && !foundReverseDistance) {
				final RouteLine RouteLine = PortFactory.eINSTANCE.createRouteLine();
				RouteLine.setFrom(to);
				RouteLine.setTo(from);
				RouteLine.setDistance(distance);
				route.getLines().add(RouteLine);
				++setDistance;
			}
		}

		return biDirectional ? (setDistance == 2) : (setDistance == 1);
	}
}

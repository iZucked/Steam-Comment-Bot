/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.shared.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.mmxlabs.common.Association;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.shared.SharedPortDistanceDataBuilder;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class LNGSharedDataTransformer {

	@Inject
	@NonNull
	private SharedPortDistanceDataBuilder portDistanceBuilder;

	public void transform(@NonNull PortModel portModel) {
		/**
		 * Bidirectionally maps EMF {@link Port} Models to {@link IPort}s in the builder.
		 */
		final Association<Port, IPort> portAssociation = new Association<Port, IPort>();

		for (final Port ePort : portModel.getPorts()) {
			final IPort port;
			if (ePort.getLocation() != null) {
				final Location loc = ePort.getLocation();
				port = portDistanceBuilder.createPort(ePort.getName(), (float) loc.getLat(), (float) loc.getLon(), ePort.getTimeZone());
			} else {
				port = portDistanceBuilder.createPort(ePort.getName(), ePort.getTimeZone());
			}
			portAssociation.add(ePort, port);
		}

		final LinkedHashSet<RouteOption> orderedKeys = Sets.newLinkedHashSet();

		orderedKeys.add(RouteOption.DIRECT);
		orderedKeys.add(RouteOption.SUEZ);
		if (LicenseFeatures.isPermitted("features:panama-canal")) {
			orderedKeys.add(RouteOption.PANAMA);
		}

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create the default distance matrix.
		 */
		final Set<RouteOption> seenRoutes = new HashSet<>();
		for (final Route r : portModel.getRoutes()) {
			seenRoutes.add(r.getRouteOption());
			for (final RouteLine dl : r.getLines()) {
				IPort from, to;
				from = portAssociation.lookupNullChecked(dl.getFrom());
				to = portAssociation.lookupNullChecked(dl.getTo());

				final int distance = dl.getFullDistance();

				portDistanceBuilder.setPortToPortDistance(from, to, mapRouteOption(r), distance);
			}
		}
		// Filter out unused routes
		orderedKeys.retainAll(seenRoutes);

		// Fix sort order for distance iteration
		final String[] preSortedKeys = orderedKeys.stream() //
				.map(RouteOption::getName)//
				.collect(Collectors.toList()) //
				.toArray(new String[orderedKeys.size()]);

		portDistanceBuilder.setPreSortedKeys(preSortedKeys);

		portDistanceBuilder.done();
	}

	@NonNull
	private static ERouteOption mapRouteOption(@NonNull final Route route) {
		final RouteOption routeOption = route.getRouteOption();
		switch (routeOption) {
		case DIRECT:
			return ERouteOption.DIRECT;
		case PANAMA:
			return ERouteOption.PANAMA;
		case SUEZ:
			return ERouteOption.SUEZ;
		}
		throw new IllegalStateException();
	}
}

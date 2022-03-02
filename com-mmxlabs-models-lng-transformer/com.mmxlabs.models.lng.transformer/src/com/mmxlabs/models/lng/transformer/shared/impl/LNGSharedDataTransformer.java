/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.shared.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.mmxlabs.common.Association;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
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

	public void transform(@NonNull final PortModel portModel, @NonNull final ModelDistanceProvider modelDistanceProvider) {
		/**
		 * Bidirectionally maps EMF {@link Port} Models to {@link IPort}s in the builder.
		 */
		final Association<Port, IPort> portAssociation = new Association<>();

		// Hint to pre-allocate ram
		portDistanceBuilder.setExpectPortCount(portModel.getPorts().size());
		final Map<String, IPort> portMap = new HashMap<>();

		for (final Port ePort : portModel.getPorts()) {
			final IPort port;
			final Location loc = ePort.getLocation();
			port = portDistanceBuilder.createPort(ePort.getName(), loc.getMmxId(), loc.getLat(), loc.getLon(), loc.getTimeZone());
			portAssociation.add(ePort, port);
			portMap.put(loc.getMmxId(), port);
		}

		final LinkedHashSet<RouteOption> orderedKeys = Sets.newLinkedHashSet();

		orderedKeys.add(RouteOption.DIRECT);
		orderedKeys.add(RouteOption.SUEZ);
		orderedKeys.add(RouteOption.PANAMA);

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create the default distance matrix.
		 */
		final Set<RouteOption> seenRoutes = new HashSet<>();

		for (final Port eFrom : portModel.getPorts()) {
			for (final Port eTo : portModel.getPorts()) {
				if (eFrom == eTo) {
					continue;
				}
				for (final RouteOption routeOption : orderedKeys) {
					final int distance = modelDistanceProvider.getDistance(eFrom, eTo, routeOption);
					if (distance != Integer.MAX_VALUE) {
						final @NonNull ERouteOption mapRouteOption = mapRouteOption(routeOption);
						final IPort from = portMap.get(eFrom.getLocation().getMmxId());
						final IPort to = portMap.get(eTo.getLocation().getMmxId());

						portDistanceBuilder.setPortToPortDistance(from, to, mapRouteOption, distance);

						seenRoutes.add(routeOption);
					}
				}
			}
		}

		// Filter out unused routes
		orderedKeys.retainAll(seenRoutes);

		// Fix sort order for distance iteration
		final ERouteOption[] preSortedKeys = orderedKeys.stream() //
				.map(r -> mapRouteOption(r))//
				.collect(Collectors.toList()) //
				.toArray(new ERouteOption[orderedKeys.size()]);

		portDistanceBuilder.setPreSortedRoutes(preSortedKeys);
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

	@NonNull
	private static ERouteOption mapRouteOption(@NonNull final RouteOption routeOption) {
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

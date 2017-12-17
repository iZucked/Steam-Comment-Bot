package com.mmxlabs.lngdataserver.lng.importers.distances;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.distances.IDistanceProvider;
import com.mmxlabs.lngdataserver.distances.exceptions.LocationNotFoundException;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.impl.PortFactoryImpl;

public class DistancesToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistancesToScenarioCopier.class);

	public static Pair<Command, Map<RouteOption, List<RouteLine>>> getUpdateDistancesCommand(@NonNull final EditingDomain editingDomain, @NonNull final IDistanceProvider distanceProvider,
			@NonNull final PortModel portModel) {

		final CompoundCommand cc = new CompoundCommand();

		final Map<RouteOption, List<RouteLine>> lostLines = new HashMap<>();

		for (final Route route : portModel.getRoutes()) {
			final Map<Pair<Port, Port>, RouteLine> indexedRouteLines = new HashMap<>();
			for (final RouteLine current : route.getLines()) {
				indexedRouteLines.put(new Pair<Port, Port>(current.getFrom(), current.getTo()), current);
			}
			LOGGER.debug(indexedRouteLines.size() + " routes in old route " + route);
			final List<RouteLine> toAdd = new LinkedList<RouteLine>();
			for (final Port from : portModel.getPorts()) {
				for (final Port to : portModel.getPorts()) {
					// skip identity
					if (from.equals(to)) {
						continue;
					}
					final RouteLine rl = PortFactoryImpl.eINSTANCE.createRouteLine();
					rl.setFrom(from);
					rl.setTo(to);
					try {
						Location fromLoc = from.getLocation();
						Location toLoc = to.getLocation();
						final int distance = distanceProvider.getDistance(fromLoc.getMmxId(), toLoc.getMmxId(), ViaKeyMapper.mapVia(route));
						if (distance != Integer.MAX_VALUE) {
							rl.setDistance(distance);
							toAdd.add(rl);

							// keep track of potentially lost route lines
							indexedRouteLines.remove(new Pair(from, to));
						}
					} catch (final LocationNotFoundException e) {
						// no action needed
					}
				}
			}
			LOGGER.debug(indexedRouteLines.size() + " routes not added after import in " + route);
			indexedRouteLines.forEach((k, v) -> lostLines.computeIfAbsent(route.getRouteOption(), ik -> new LinkedList<>()).add(v));
			if (!route.getLines().isEmpty()) {
				cc.append(RemoveCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__LINES, route.getLines()));
			}
			if (!toAdd.isEmpty()) {
				cc.append(AddCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__LINES, toAdd));
			}
		}
		return new Pair<Command, Map<RouteOption, List<RouteLine>>>(cc, lostLines);
	}

}

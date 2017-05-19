/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.CheckingIndexingContext;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.XYPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProviderEditor;

/**
 * 
 * @author Simon Goodall
 */
public final class SharedPortDistanceDataBuilder {

	/**
	 * A "virtual" port which is zero distance from all other ports, to be used in cases where a vessel can be in any location. This can be replaced with a real location at a later date, after running
	 * an optimisation.
	 */
	private IPort ANYWHERE;

	@NonNull
	private final IIndexingContext indexingContext = new CheckingIndexingContext();

	@Inject
	@NonNull
	private IPortProviderEditor portProvider;

	@Inject
	@NonNull
	private IndexedMultiMatrixProvider<IPort, Integer> portDistanceProvider;

	public SharedPortDistanceDataBuilder() {
		indexingContext.registerType(Port.class);
	}

	// @Inject to trigger call after constructor
	@Inject
	public void init() {
		// Initialise routes and matrix editors
		for (final ERouteOption route : ERouteOption.values()) {
			portDistanceProvider.set(route.name(), new IndexedMatrixEditor<>(Integer.MAX_VALUE));
		}
		// Create the anywhere port
		ANYWHERE = createPort("ANYWHERE", "UTC"/* no timezone */);
		portProvider.setAnywherePort(ANYWHERE);
		for (final ERouteOption route : ERouteOption.values()) {
			setPortToPortDistance(ANYWHERE, ANYWHERE, route, 0);
		}
	}

	public void setRouteWaypointPort(@NonNull final ERouteOption routeOption, @NonNull final IPort mergedPort, @NonNull final IPort entryPortA, @NonNull final IPort entryPortB) {

	}

	@NonNull
	public IPort createPort(final @NonNull String name, final @NonNull String timezoneId) {

		final Port port = new Port(indexingContext);
		buildPort(port, name, timezoneId);

		portProvider.addPort(port, name, /* mmxID */ name);

		return port;
	}

	@NonNull
	public IXYPort createPort(final @NonNull String name, final float x, final float y, final @NonNull String timezoneId) {

		final XYPort port = new XYPort(indexingContext);
		buildPort(port, name, timezoneId);
		port.setX(x);
		port.setY(y);

		portProvider.addPort(port, name, /* mmxID */ name);

		return port;
	}

	/**
	 * Method to set common properties etc for {@link Port} implementations.
	 * 
	 * @param port
	 * @param name
	 * @param timezoneId
	 */
	private void buildPort(@NonNull final Port port, final @NonNull String name, final @NonNull String timezoneId) {

		port.setName(name);
		port.setTimeZoneId(timezoneId);

		// Pin variable for null analysis...
		final IPort localANYWHERE = ANYWHERE;
		if (localANYWHERE != null) {
			setPortToPortDistance(port, localANYWHERE, ERouteOption.DIRECT, 0);
			setPortToPortDistance(localANYWHERE, port, ERouteOption.DIRECT, 0);
		}

		// travel time from A to A should be zero, right?
		this.setPortToPortDistance(port, port, ERouteOption.DIRECT, 0);
	}

	public void setPortToPortDistance(@NonNull final IPort from, @NonNull final IPort to, @NonNull final ERouteOption route, final int distance) {

		assert (portDistanceProvider.containsKey(route.name()));

		final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider.get(route.name());
		matrix.set(from, to, distance);
	}

	/**
	 * Set the deterministic order of route keys for use in iterators.
	 * 
	 * @param preSortedKeys
	 */
	public void setPreSortedKeys(final String[] preSortedKeys) {
		portDistanceProvider.setPreSortedKeys(preSortedKeys);
	}

	public void done() {
		portDistanceProvider.cacheExtremalValues(portProvider.getAllPorts());
	}
}

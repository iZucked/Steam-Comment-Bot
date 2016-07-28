/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProvider;

public class DefaultDistanceProviderImplTest {
	@Test
	public void testGetRouteAvailable() {

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		Assert.assertEquals(Integer.MIN_VALUE, distanceProvider.getRouteAvailableFrom(ERouteOption.DIRECT));
		Assert.assertEquals(Integer.MIN_VALUE, distanceProvider.getRouteAvailableFrom(ERouteOption.SUEZ));
		Assert.assertEquals(Integer.MIN_VALUE, distanceProvider.getRouteAvailableFrom(ERouteOption.PANAMA));

		distanceProvider.setRouteAvailableFrom(ERouteOption.PANAMA, 10);

		Assert.assertEquals(Integer.MIN_VALUE, distanceProvider.getRouteAvailableFrom(ERouteOption.DIRECT));
		Assert.assertEquals(Integer.MIN_VALUE, distanceProvider.getRouteAvailableFrom(ERouteOption.SUEZ));
		Assert.assertEquals(10, distanceProvider.getRouteAvailableFrom(ERouteOption.PANAMA));
	}

	@Test
	public void testIsRouteAvailable() {

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		Assert.assertTrue(distanceProvider.isRouteAvailable(ERouteOption.DIRECT, 0));
		Assert.assertTrue(distanceProvider.isRouteAvailable(ERouteOption.SUEZ, 0));
		Assert.assertTrue(distanceProvider.isRouteAvailable(ERouteOption.PANAMA, 0));

		distanceProvider.setRouteAvailableFrom(ERouteOption.PANAMA, 10);

		Assert.assertTrue(distanceProvider.isRouteAvailable(ERouteOption.DIRECT, 0));
		Assert.assertTrue(distanceProvider.isRouteAvailable(ERouteOption.SUEZ, 0));

		Assert.assertFalse(distanceProvider.isRouteAvailable(ERouteOption.PANAMA, 0));
		Assert.assertTrue(distanceProvider.isRouteAvailable(ERouteOption.PANAMA, 10));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetOpenDistance() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		Mockito.when(directMatrix.get(port1, port2)).thenReturn(10);
		Mockito.when(suezMatrix.get(port1, port2)).thenReturn(20);
		Mockito.when(panamaMatrix.get(port1, port2)).thenReturn(30);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		Assert.assertEquals(10, distanceProvider.getOpenDistance(ERouteOption.DIRECT, port1, port2));
		Assert.assertEquals(20, distanceProvider.getOpenDistance(ERouteOption.SUEZ, port1, port2));
		Assert.assertEquals(30, distanceProvider.getOpenDistance(ERouteOption.PANAMA, port1, port2));

		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getOpenDistance(ERouteOption.DIRECT, port2, port1));
		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getOpenDistance(ERouteOption.SUEZ, port2, port1));
		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getOpenDistance(ERouteOption.PANAMA, port2, port1));
	}

	@Test
	public void testGetDistance() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		Mockito.when(directMatrix.get(port1, port2)).thenReturn(10);
		Mockito.when(suezMatrix.get(port1, port2)).thenReturn(20);
		Mockito.when(panamaMatrix.get(port1, port2)).thenReturn(30);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		// No restriction set, all open
		Assert.assertEquals(10, distanceProvider.getDistance(ERouteOption.DIRECT, port1, port2, 100, null));
		Assert.assertEquals(20, distanceProvider.getDistance(ERouteOption.SUEZ, port1, port2, 100, null));
		Assert.assertEquals(30, distanceProvider.getDistance(ERouteOption.PANAMA, port1, port2, 100, null));

		// Open Suez at time 101
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, 101);
		Assert.assertEquals(10, distanceProvider.getDistance(ERouteOption.DIRECT, port1, port2, 100, null));
		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getDistance(ERouteOption.SUEZ, port1, port2, 100, null));
		Assert.assertEquals(20, distanceProvider.getDistance(ERouteOption.SUEZ, port1, port2, 101, null));
		Assert.assertEquals(30, distanceProvider.getDistance(ERouteOption.PANAMA, port1, port2, 100, null));

		// Open Panama at time 150
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, Integer.MIN_VALUE);
		distanceProvider.setRouteAvailableFrom(ERouteOption.PANAMA, 150);

		Assert.assertEquals(10, distanceProvider.getDistance(ERouteOption.DIRECT, port1, port2, 100, null));
		Assert.assertEquals(20, distanceProvider.getDistance(ERouteOption.SUEZ, port1, port2, 100, null));
		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getDistance(ERouteOption.PANAMA, port1, port2, 149, null));
		Assert.assertEquals(30, distanceProvider.getDistance(ERouteOption.PANAMA, port1, port2, 150, null));
	}

	@Test
	public void testGetDistanceValues() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		Mockito.when(directMatrix.get(port1, port2)).thenReturn(10);
		Mockito.when(suezMatrix.get(port1, port2)).thenReturn(20);
		Mockito.when(panamaMatrix.get(port1, port2)).thenReturn(30);

		final Collection<MatrixEntry<IPort, Integer>> matrixDistanceValues = new LinkedList<>();
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.DIRECT.name(), port1, port2, 10));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.SUEZ.name(), port1, port2, 20));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.PANAMA.name(), port1, port2, 30));

		Mockito.when(matrixProvider.getValues(port1, port2)).thenReturn(matrixDistanceValues);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		// No restriction set, all open
		verifyListResult(10, 20, 30, distanceProvider.getDistanceValues(port1, port2, 100, null));

		// Open Suez at time 101
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, 101);

		verifyListResult(10, null, 30, distanceProvider.getDistanceValues(port1, port2, 100, null));
		verifyListResult(10, 20, 30, distanceProvider.getDistanceValues(port1, port2, 101, null));

		// Open Panama at time 150
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, Integer.MIN_VALUE);
		distanceProvider.setRouteAvailableFrom(ERouteOption.PANAMA, 150);

		verifyListResult(10, 20, null, distanceProvider.getDistanceValues(port1, port2, 149, null));
		verifyListResult(10, 20, 30, distanceProvider.getDistanceValues(port1, port2, 150, null));

		// Open Direct at time 200
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, Integer.MIN_VALUE);
		distanceProvider.setRouteAvailableFrom(ERouteOption.PANAMA, Integer.MIN_VALUE);
		distanceProvider.setRouteAvailableFrom(ERouteOption.DIRECT, 200);

		verifyListResult(null, 20, 30, distanceProvider.getDistanceValues(port1, port2, 199, null));
		verifyListResult(10, 20, 30, distanceProvider.getDistanceValues(port1, port2, 200, null));
	}

	@Test
	public void testGetTravelTime() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		Mockito.when(directMatrix.get(port1, port2)).thenReturn(10);
		Mockito.when(suezMatrix.get(port1, port2)).thenReturn(20);
		Mockito.when(panamaMatrix.get(port1, port2)).thenReturn(30);

		final Collection<MatrixEntry<IPort, Integer>> matrixDistanceValues = new LinkedList<>();
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.DIRECT.name(), port1, port2, 10));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.SUEZ.name(), port1, port2, 20));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.PANAMA.name(), port1, port2, 30));

		Mockito.when(matrixProvider.getValues(port1, port2)).thenReturn(matrixDistanceValues);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		IVessel vessel = Mockito.mock(IVessel.class);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.DIRECT, vessel)).thenReturn(0);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(6);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.PANAMA, vessel)).thenReturn(12);

		// No restriction set, all open
		// 10 knots speed
		Assert.assertEquals(1, distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, port1, port2, 100, 10_000));
		Assert.assertEquals(2 + 6, distanceProvider.getTravelTime(ERouteOption.SUEZ, vessel, port1, port2, 100, 10_000));
		Assert.assertEquals(3 + 12, distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, port1, port2, 100, 10_000));

		// Open Suez at time 101
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, 101);
		Assert.assertEquals(1, distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, port1, port2, 100, 10_000));
		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getTravelTime(ERouteOption.SUEZ, vessel, port1, port2, 100, 10_000));
		Assert.assertEquals(2 + 6, distanceProvider.getTravelTime(ERouteOption.SUEZ, vessel, port1, port2, 101, 10_000));
		Assert.assertEquals(3 + 12, distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, port1, port2, 100, 10_000));

		// Open Panama at time 150
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, Integer.MIN_VALUE);
		distanceProvider.setRouteAvailableFrom(ERouteOption.PANAMA, 150);

		Assert.assertEquals(1, distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, port1, port2, 149, 10_000));
		Assert.assertEquals(2 + 6, distanceProvider.getTravelTime(ERouteOption.SUEZ, vessel, port1, port2, 149, 10_000));
		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, port1, port2, 149, 10_000));
		Assert.assertEquals(3 + 12, distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, port1, port2, 150, 10_000));

		// Open Direct at time 200
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, Integer.MIN_VALUE);
		distanceProvider.setRouteAvailableFrom(ERouteOption.PANAMA, Integer.MIN_VALUE);
		distanceProvider.setRouteAvailableFrom(ERouteOption.DIRECT, 200);

		Assert.assertEquals(Integer.MAX_VALUE, distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, port1, port2, 199, 10_000));
		Assert.assertEquals(1, distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, port1, port2, 200, 10_000));
		Assert.assertEquals(2 + 6, distanceProvider.getTravelTime(ERouteOption.SUEZ, vessel, port1, port2, 200, 10_000));
		Assert.assertEquals(3 + 12, distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, port1, port2, 199, 10_000));
	}

	@Test
	public void testGetQuickestTravelTime() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		Mockito.when(directMatrix.get(port1, port2)).thenReturn(40);
		Mockito.when(suezMatrix.get(port1, port2)).thenReturn(20);
		Mockito.when(panamaMatrix.get(port1, port2)).thenReturn(30);

		final Collection<MatrixEntry<IPort, Integer>> matrixDistanceValues = new LinkedList<>();
		// Make direct longest!
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.DIRECT.name(), port1, port2, 40));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.SUEZ.name(), port1, port2, 20));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.PANAMA.name(), port1, port2, 30));

		Mockito.when(matrixProvider.getValues(port1, port2)).thenReturn(matrixDistanceValues);
		Mockito.when(matrixProvider.getKeys()).thenReturn(new String[] { ERouteOption.DIRECT.name(), ERouteOption.SUEZ.name(), ERouteOption.PANAMA.name() });

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		IVessel vessel = Mockito.mock(IVessel.class);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.DIRECT, vessel)).thenReturn(0);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.PANAMA, vessel)).thenReturn(12);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(6);
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(1);
		Assert.assertEquals(new Pair<>(ERouteOption.SUEZ, 2 + 1), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));

		// Close Suez
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, 101);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(1);
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));

		// Close Direct and Suez
		distanceProvider.setRouteAvailableFrom(ERouteOption.SUEZ, 101);
		distanceProvider.setRouteAvailableFrom(ERouteOption.DIRECT, 101);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(1);
		Assert.assertEquals(new Pair<>(ERouteOption.PANAMA, 3 + 12), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));

	}

	@Test
	public void testGetQuickestTravelTimeWithRouteExclusion() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IMultiMatrixProvider<IPort, Integer> matrixProvider = Mockito.mock(IMultiMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		final IMatrixProvider<IPort, Integer> directMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> suezMatrix = Mockito.mock(IMatrixProvider.class);
		final IMatrixProvider<IPort, Integer> panamaMatrix = Mockito.mock(IMatrixProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT.name())).thenReturn(directMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ.name())).thenReturn(suezMatrix);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA.name())).thenReturn(panamaMatrix);

		Mockito.when(directMatrix.get(port1, port2)).thenReturn(40);
		Mockito.when(suezMatrix.get(port1, port2)).thenReturn(20);
		Mockito.when(panamaMatrix.get(port1, port2)).thenReturn(30);

		final Collection<MatrixEntry<IPort, Integer>> matrixDistanceValues = new LinkedList<>();
		// Make direct longest!
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.DIRECT.name(), port1, port2, 40));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.SUEZ.name(), port1, port2, 20));
		matrixDistanceValues.add(new MatrixEntry<>(ERouteOption.PANAMA.name(), port1, port2, 30));

		Mockito.when(matrixProvider.getValues(port1, port2)).thenReturn(matrixDistanceValues);
		Mockito.when(matrixProvider.getKeys()).thenReturn(new String[] { ERouteOption.DIRECT.name(), ERouteOption.SUEZ.name(), ERouteOption.PANAMA.name() });

		IVessel vessel = Mockito.mock(IVessel.class);
		IVessel vessel2 = Mockito.mock(IVessel.class);
		
		HashMapRouteExclusionProvider routeExclusionProvider = new HashMapRouteExclusionProvider();
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.PANAMA));
//		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet());
		DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider, routeExclusionProvider);


		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.DIRECT, vessel)).thenReturn(0);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.PANAMA, vessel)).thenReturn(12);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(6);
		// try to exclude Direct (but direct is always open!)
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.DIRECT));
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));

		// Suez is open
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(1);
		Assert.assertEquals(new Pair<>(ERouteOption.SUEZ, 2 + 1), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));

		// exclude Suez
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.SUEZ));
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel2)).thenReturn(1);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(1);
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));
		Assert.assertEquals(new Pair<>(ERouteOption.SUEZ, 2 + 1), distanceProvider.getQuickestTravelTime(vessel2, port1, port2, 100, 10_000));

		// Exclude Suez, open panama
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.PANAMA, vessel)).thenReturn(0);
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.SUEZ));
		Assert.assertEquals(new Pair<>(ERouteOption.PANAMA, 3 + 0), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));

		// Exclude Panama and Suez
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.SUEZ, ERouteOption.PANAMA));
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 100, 10_000));
	}
	
	private void verifyListResult(final Integer directDistance, final Integer suezDistance, final Integer panamaDistance, final List<Pair<ERouteOption, Integer>> results) {

		Assert.assertNotNull(results);

		boolean foundDirect = false;
		boolean foundSuez = false;
		boolean foundPanama = false;

		for (final Pair<ERouteOption, Integer> result : results) {
			final ERouteOption option = result.getFirst();
			if (option == ERouteOption.DIRECT) {
				if (directDistance == null) {
					Assert.fail("Unexpected direct distance");
				} else {
					final int distance = result.getSecond();
					Assert.assertEquals(directDistance.intValue(), distance);
				}
				if (foundDirect) {
					Assert.fail("Direct distance has multiple results");
				}
				foundDirect = true;
			}

			else if (option == ERouteOption.SUEZ) {
				if (suezDistance == null) {
					Assert.fail("Unexpected Suez distance");
				} else {
					final int distance = result.getSecond();
					Assert.assertEquals(suezDistance.intValue(), distance);
				}
				if (foundSuez) {
					Assert.fail("Suez distance has multiple results");
				}
				foundSuez = true;
			} else if (option == ERouteOption.PANAMA) {
				if (panamaDistance == null) {
					Assert.fail("Unexpected Panama distance");
				} else {
					final int distance = result.getSecond();
					Assert.assertEquals(panamaDistance.intValue(), distance);
				}
				if (foundPanama) {
					Assert.fail("Panama distance has multiple results");
				}
				foundPanama = true;
			} else {
				Assert.fail("Unexpected option");
			}
		}

		if (directDistance != null) {
			Assert.assertTrue(foundDirect);
		}
		if (suezDistance != null) {
			Assert.assertTrue(foundSuez);
		}
		if (panamaDistance != null) {
			Assert.assertTrue(foundPanama);
		}
	}

	private DefaultDistanceProviderImpl createDistanceProvider(final IMultiMatrixProvider<IPort, Integer> matrixProvider, final IRouteCostProvider routeCostProvider) {
		return createDistanceProvider(matrixProvider, routeCostProvider, new HashMapRouteExclusionProvider());
	}
	
	private DefaultDistanceProviderImpl createDistanceProvider(final IMultiMatrixProvider<IPort, Integer> matrixProvider, final IRouteCostProvider routeCostProvider, final IRouteExclusionProvider routeExclusionProvider) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(new TypeLiteral<IMultiMatrixProvider<IPort, Integer>>() {
				}).toInstance(matrixProvider);
				bind(IRouteCostProvider.class).toInstance(routeCostProvider);
				bind(IRouteExclusionProvider.class).toInstance(routeExclusionProvider);
			}

		});

		return injector.getInstance(DefaultDistanceProviderImpl.class);
	}
}

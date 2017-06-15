/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;

public class DefaultDistanceProviderImplTest {

	@Test
	public void testGetOpenDistance() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IDistanceMatrixProvider matrixProvider = Mockito.mock(IDistanceMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);

		Mockito.when(matrixProvider.get(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(Integer.MAX_VALUE);
		Mockito.when(matrixProvider.get(ERouteOption.DIRECT, port1, port2)).thenReturn(10);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ, port1, port2)).thenReturn(20);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA, port1, port2)).thenReturn(30);

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

		final IDistanceMatrixProvider matrixProvider = Mockito.mock(IDistanceMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);

		Mockito.when(matrixProvider.get(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(Integer.MAX_VALUE);
		Mockito.when(matrixProvider.get(ERouteOption.DIRECT, port1, port2)).thenReturn(10);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ, port1, port2)).thenReturn(20);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA, port1, port2)).thenReturn(30);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		// No restriction set, all open
		Assert.assertEquals(10, distanceProvider.getDistance(ERouteOption.DIRECT, port1, port2, null));
		Assert.assertEquals(20, distanceProvider.getDistance(ERouteOption.SUEZ, port1, port2, null));
		Assert.assertEquals(30, distanceProvider.getDistance(ERouteOption.PANAMA, port1, port2, null));
	}

	@Test
	public void testGetDistanceValues() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IDistanceMatrixProvider matrixProvider = Mockito.mock(IDistanceMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);

		Mockito.when(matrixProvider.get(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(Integer.MAX_VALUE);
		Mockito.when(matrixProvider.get(ERouteOption.DIRECT, port1, port2)).thenReturn(10);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ, port1, port2)).thenReturn(20);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA, port1, port2)).thenReturn(30);

		final List<DistanceMatrixEntry> matrixDistanceValues = new LinkedList<>();
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.DIRECT, port1, port2, 10));
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.SUEZ, port1, port2, 20));
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.PANAMA, port1, port2, 30));

		// Create mutable copy
		Mockito.when(matrixProvider.getValues(port1, port2)).then(new Answer<List<DistanceMatrixEntry>>() {

			@Override
			public List<DistanceMatrixEntry> answer(InvocationOnMock invocation) throws Throwable {
				return new LinkedList<>(matrixDistanceValues);
			}

		});

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		// No restriction set, all open
		verifyListResult(10, 20, 30, distanceProvider.getDistanceValues(port1, port2, null));
	}

	@Test
	public void testGetTravelTime() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IDistanceMatrixProvider matrixProvider = Mockito.mock(IDistanceMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT, port1, port2)).thenReturn(10);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ, port1, port2)).thenReturn(20);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA, port1, port2)).thenReturn(30);

		final List<DistanceMatrixEntry> matrixDistanceValues = new LinkedList<>();
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.DIRECT, port1, port2, 10));
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.SUEZ, port1, port2, 20));
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.PANAMA, port1, port2, 30));

		Mockito.when(matrixProvider.getValues(port1, port2)).thenReturn(matrixDistanceValues);

		final DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider);

		IVessel vessel = Mockito.mock(IVessel.class);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.DIRECT, vessel)).thenReturn(0);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(6);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.PANAMA, vessel)).thenReturn(12);

		// No restriction set, all open
		// 10 knots speed
		Assert.assertEquals(1, distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, port1, port2, 10_000));
		Assert.assertEquals(2 + 6, distanceProvider.getTravelTime(ERouteOption.SUEZ, vessel, port1, port2, 10_000));
		Assert.assertEquals(3 + 12, distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, port1, port2, 10_000));

	}

	@Test
	public void testGetQuickestTravelTimeWithRouteExclusion() {
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final IDistanceMatrixProvider matrixProvider = Mockito.mock(IDistanceMatrixProvider.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);

		Mockito.when(matrixProvider.get(ERouteOption.DIRECT, port1, port2)).thenReturn(40);
		Mockito.when(matrixProvider.get(ERouteOption.SUEZ, port1, port2)).thenReturn(20);
		Mockito.when(matrixProvider.get(ERouteOption.PANAMA, port1, port2)).thenReturn(30);

		final List<DistanceMatrixEntry> matrixDistanceValues = new LinkedList<>();
		// Make direct longest!
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.DIRECT, port1, port2, 40));
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.SUEZ, port1, port2, 20));
		matrixDistanceValues.add(new DistanceMatrixEntry(ERouteOption.PANAMA, port1, port2, 30));

		Mockito.when(matrixProvider.getValues(port1, port2)).thenReturn(matrixDistanceValues);
		Mockito.when(matrixProvider.getRoutes()).thenReturn(new ERouteOption[] { ERouteOption.DIRECT, ERouteOption.SUEZ, ERouteOption.PANAMA });

		IVessel vessel = Mockito.mock(IVessel.class);
		IVessel vessel2 = Mockito.mock(IVessel.class);

		HashMapRouteExclusionProvider routeExclusionProvider = new HashMapRouteExclusionProvider();
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.PANAMA));
		DefaultDistanceProviderImpl distanceProvider = createDistanceProvider(matrixProvider, routeCostProvider, routeExclusionProvider);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.DIRECT, vessel)).thenReturn(0);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.PANAMA, vessel)).thenReturn(12);

		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(6);
		// try to exclude Direct (but direct is always open!)
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.DIRECT));
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 10_000));

		// Suez is open
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(1);
		Assert.assertEquals(new Pair<>(ERouteOption.SUEZ, 2 + 1), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 10_000));

		// exclude Suez
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.SUEZ));
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel2)).thenReturn(1);
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.SUEZ, vessel)).thenReturn(1);
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 10_000));
		Assert.assertEquals(new Pair<>(ERouteOption.SUEZ, 2 + 1), distanceProvider.getQuickestTravelTime(vessel2, port1, port2, 10_000));

		// Exclude Suez, open panama
		Mockito.when(routeCostProvider.getRouteTransitTime(ERouteOption.PANAMA, vessel)).thenReturn(0);
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.SUEZ));
		Assert.assertEquals(new Pair<>(ERouteOption.PANAMA, 3 + 0), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 10_000));

		// Exclude Panama and Suez
		routeExclusionProvider.setExcludedRoutes(vessel, Sets.newHashSet(ERouteOption.SUEZ, ERouteOption.PANAMA));
		Assert.assertEquals(new Pair<>(ERouteOption.DIRECT, 4), distanceProvider.getQuickestTravelTime(vessel, port1, port2, 10_000));
	}

	private void verifyListResult(final Integer directDistance, final Integer suezDistance, final Integer panamaDistance, final List<DistanceMatrixEntry> results) {

		Assert.assertNotNull(results);

		boolean foundDirect = false;
		boolean foundSuez = false;
		boolean foundPanama = false;

		for (final DistanceMatrixEntry result : results) {
			final ERouteOption option = result.getRoute();
			if (option == ERouteOption.DIRECT) {
				if (directDistance == null) {
					Assert.fail("Unexpected direct distance");
				} else {
					final int distance = result.getDistance();
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
					final int distance = result.getDistance();
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
					final int distance = result.getDistance();
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

	private DefaultDistanceProviderImpl createDistanceProvider(final IDistanceMatrixProvider matrixProvider, final IRouteCostProvider routeCostProvider) {
		return createDistanceProvider(matrixProvider, routeCostProvider, new HashMapRouteExclusionProvider());
	}

	private DefaultDistanceProviderImpl createDistanceProvider(final IDistanceMatrixProvider matrixProvider, final IRouteCostProvider routeCostProvider,
			final IRouteExclusionProvider routeExclusionProvider) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IDistanceMatrixProvider.class).toInstance(matrixProvider);
				bind(IRouteCostProvider.class).toInstance(routeCostProvider);
				bind(IRouteExclusionProvider.class).toInstance(routeExclusionProvider);
			}

		});

		return injector.getInstance(DefaultDistanceProviderImpl.class);
	}
}

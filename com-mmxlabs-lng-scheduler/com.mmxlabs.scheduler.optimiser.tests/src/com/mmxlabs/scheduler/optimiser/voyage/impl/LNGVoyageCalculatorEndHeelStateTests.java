/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class LNGVoyageCalculatorEndHeelStateTests {

	@Test
	public void testNoVoyage() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		int heelState = calc.getExpectedCargoEndHeelState(12, null);
		Assert.assertEquals(LNGVoyageCalculator.STATE_COLD_NO_VOYAGE, heelState);
	}

	@Test
	public void testCooldown() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setCooldownPerformed(true);

		int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assert.assertEquals(LNGVoyageCalculator.STATE_COLD_COOLDOWN, heelState);
	}

	@Test
	public void testZeroLengthBallast() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assert.assertEquals(LNGVoyageCalculator.STATE_COLD_NO_VOYAGE, heelState);
	}

	@Test
	public void testArriveWithMinHeel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setIdleTime(24);

		// Need some NBO
		ballastDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 24);
		ballastDetails.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, 24);

		int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assert.assertEquals(LNGVoyageCalculator.STATE_COLD_MIN_HEEL, heelState);
	}

	@Test
	public void testArriveWithinWarmingTimeOnIdle() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setIdleTime(6);

		// Need some NBO
		ballastDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 24);
		ballastDetails.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, 0);

		int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assert.assertEquals(LNGVoyageCalculator.STATE_COLD_WARMING_TIME, heelState);
	}

	@Test
	public void testArriveWithinWarmingTimeOnTravel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(6);
		ballastDetails.setIdleTime(0);

		// Need some NBO
		ballastDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 0);
		ballastDetails.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, 0);

		int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assert.assertEquals(LNGVoyageCalculator.STATE_COLD_WARMING_TIME, heelState);
	}

	@Test
	public void testArrivedWWarmOnTravel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setIdleTime(0);

		// Need some NBO
		ballastDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 0);
		ballastDetails.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, 0);

		int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assert.assertEquals(LNGVoyageCalculator.STATE_WARM, heelState);
	}

	@Test
	public void testArrivedWWarmOnIdle() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setIdleTime(24);

		// Need some NBO
		ballastDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 24);
		ballastDetails.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, 0);

		int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assert.assertEquals(LNGVoyageCalculator.STATE_WARM, heelState);
	}
}

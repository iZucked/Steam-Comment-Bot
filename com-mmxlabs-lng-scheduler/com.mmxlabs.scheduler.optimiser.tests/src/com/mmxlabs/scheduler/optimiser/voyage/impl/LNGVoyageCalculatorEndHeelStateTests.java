/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;

public class LNGVoyageCalculatorEndHeelStateTests {

	@Test
	public void testNoVoyage() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final int heelState = calc.getExpectedCargoEndHeelState(12, null);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_COLD_NO_VOYAGE, heelState);
	}

	@Test
	public void testCooldown() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		final VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setCooldownPerformed(true);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_COLD_COOLDOWN, heelState);
	}

	@Test
	public void testZeroLengthBallast() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		final VoyageDetails ballastDetails = new VoyageDetails(options);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_COLD_NO_VOYAGE, heelState);
	}

	@Test
	public void testArriveWithMinHeel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		options.setAvailableTime(48);
		final VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setTravelNBOHours(24);
		ballastDetails.setIdleTime(24);
		ballastDetails.setIdleNBOHours(24);

		// Need some NBO
		ballastDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 24);
		ballastDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 24);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_COLD_MIN_HEEL, heelState);
	}

	@Test
	public void testArriveWithinWarmingTimeOnIdle() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		options.setAvailableTime(30);

		final VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setTravelNBOHours(24);

		ballastDetails.setIdleTime(6);
		ballastDetails.setIdleNBOHours(0);

		// Need some NBO
		ballastDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 24);
		ballastDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 0);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_COLD_WARMING_TIME, heelState);
	}

	@Test
	public void testArriveWithinWarmingTimeOnTravel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		options.setAvailableTime(6);
		final VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(6);
		ballastDetails.setTravelNBOHours(0);

		ballastDetails.setIdleTime(0);
		ballastDetails.setIdleNBOHours(0);

		// Need some NBO
		ballastDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 0);
		ballastDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 0);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_COLD_WARMING_TIME, heelState);
	}

	@Test
	public void testArrivedWWarmOnTravel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		options.setAvailableTime(24);
		final VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setTravelNBOHours(0);
		ballastDetails.setIdleTime(0);

		// Need some NBO
		ballastDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 0);
		ballastDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 0);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_WARM, heelState);
	}

	@Test
	public void testArrivedWWarmOnIdle() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		options.setAvailableTime(48);
		final VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setTravelNBOHours(0);

		ballastDetails.setIdleTime(24);
		ballastDetails.setIdleNBOHours(0);

		// Need some NBO
		ballastDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 24);
		ballastDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 0);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_WARM, heelState);
	}
	
	
	@Test
	public void testArrivedWarmDueToCanal() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IPortSlot from = Mockito.mock(IPortSlot.class);
		final IPortSlot to = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(from, to);
		options.setAvailableTime(24);
		final VoyageDetails ballastDetails = new VoyageDetails(options);

		ballastDetails.setTravelTime(24);
		ballastDetails.setTravelNBOHours(11);
		ballastDetails.setRouteAdditionalNBOHours(0);

		ballastDetails.setIdleTime(0);
		ballastDetails.setIdleNBOHours(0);

		// Need some NBO
		ballastDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 24);
		ballastDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 0);

		final int heelState = calc.getExpectedCargoEndHeelState(12, ballastDetails);
		Assertions.assertEquals(LNGVoyageCalculator.STATE_WARM, heelState);
	}
}

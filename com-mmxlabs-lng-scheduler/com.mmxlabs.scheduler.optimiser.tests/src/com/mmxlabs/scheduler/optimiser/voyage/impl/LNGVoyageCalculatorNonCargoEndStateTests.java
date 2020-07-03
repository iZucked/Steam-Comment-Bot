/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;

public class LNGVoyageCalculatorNonCargoEndStateTests {

	@Test
	public void testVoyageRangeToOpen_OK_LNGUsed() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 100_000L;
		final long startHeelRange[] = new long[] { 0L, 100_000L };

		IHeelOptionSupplierPortSlot from = Mockito.mock(IHeelOptionSupplierPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		IHeelOptionSupplier heelOptionsSupplier = Mockito.mock(IHeelOptionSupplier.class);
		Mockito.when(heelOptionsSupplier.getMinimumHeelAvailableInM3()).thenReturn(50_000L);
		Mockito.when(heelOptionsSupplier.getMaximumHeelAvailableInM3()).thenReturn(100_000L);

		Mockito.when(from.getHeelOptionsSupplier()).thenReturn(heelOptionsSupplier);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, 500_000, LNGVoyageCalculator.STATE_COLD_WARMING_TIME);
		Assertions.assertEquals(0, violationCount);

		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		Assertions.assertEquals(100_000L, voyagePlan.getStartingHeelInM3());
		Assertions.assertEquals(0L, voyagePlan.getRemainingHeelInM3());
	}

	@Test
	public void testVoyageRangeToOpen_OK_LNGNotUsed() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 0L;
		final long startHeelRange[] = new long[] { 0L, 100_000L };

		IHeelOptionSupplierPortSlot from = Mockito.mock(IHeelOptionSupplierPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		IHeelOptionSupplier heelOptionsSupplier = Mockito.mock(IHeelOptionSupplier.class);
		Mockito.when(heelOptionsSupplier.getMinimumHeelAvailableInM3()).thenReturn(0_000L);
		Mockito.when(heelOptionsSupplier.getMaximumHeelAvailableInM3()).thenReturn(100_000L);

		Mockito.when(from.getHeelOptionsSupplier()).thenReturn(heelOptionsSupplier);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, 500_000, LNGVoyageCalculator.STATE_WARM);
		Assertions.assertEquals(0, violationCount);

		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		Assertions.assertEquals(0L, voyagePlan.getStartingHeelInM3());
		Assertions.assertEquals(0L, voyagePlan.getRemainingHeelInM3());
	}

	@Test
	public void testVoyageRangeToOpen_Fail_LNGNotUsed_MinHeel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 0L;
		final long startHeelRange[] = new long[] { 50_000L, 100_000L };

		IHeelOptionSupplierPortSlot from = Mockito.mock(IHeelOptionSupplierPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		IHeelOptionSupplier heelOptionsSupplier = Mockito.mock(IHeelOptionSupplier.class);
		Mockito.when(heelOptionsSupplier.getMinimumHeelAvailableInM3()).thenReturn(50_000L);
		Mockito.when(heelOptionsSupplier.getMaximumHeelAvailableInM3()).thenReturn(100_000L);

		Mockito.when(from.getHeelOptionsSupplier()).thenReturn(heelOptionsSupplier);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, -1, -1);
		Assertions.assertEquals(100, violationCount);

		// Assertions.assertEquals(0L, voyagePlan.getLNGFuelVolume());
		// Assertions.assertEquals(0L, voyagePlan.getStartingHeelInM3());
		// Assertions.assertEquals(0L, voyagePlan.getRemainingHeelInM3());
	}

	@Test
	public void testVoyageRangeToOpen_Fail_NotEnoughHeel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 100_000L;
		final long startHeelRange[] = new long[] { 0L, 90_000L };

		IHeelOptionSupplierPortSlot from = Mockito.mock(IHeelOptionSupplierPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		IHeelOptionSupplier heelOptionsSupplier = Mockito.mock(IHeelOptionSupplier.class);
		Mockito.when(heelOptionsSupplier.getMinimumHeelAvailableInM3()).thenReturn(50_000L);
		Mockito.when(heelOptionsSupplier.getMaximumHeelAvailableInM3()).thenReturn(90_000L);

		Mockito.when(from.getHeelOptionsSupplier()).thenReturn(heelOptionsSupplier);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, -1, -1);
		Assertions.assertEquals(300, violationCount);

		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getStartingHeelInM3());
		Assertions.assertEquals(0L, voyagePlan.getRemainingHeelInM3());
	}

	@Test
	public void testVoyageRangeToOpen_Fail_MinEndHeelIncreaseStartHeelPastMaximum() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 10_000L;
		final long startHeelRange[] = new long[] { 0L, 50_000L };

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IHeelOptionConsumerPortSlot to = Mockito.mock(IHeelOptionConsumerPortSlot.class);

		IHeelOptionConsumer heelOptionConsumer = Mockito.mock(IHeelOptionConsumer.class);
		Mockito.when(heelOptionConsumer.getMinimumHeelAcceptedInM3()).thenReturn(50_000L);
		Mockito.when(heelOptionConsumer.getMaximumHeelAcceptedInM3()).thenReturn(100_000L);
		Mockito.when(heelOptionConsumer.getExpectedTankState()).thenReturn(VesselTankState.MUST_BE_COLD);

		Mockito.when(to.getHeelOptionsConsumer()).thenReturn(heelOptionConsumer);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, -1, -1);
		Assertions.assertEquals(1, violationCount);

		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		// Heel up to max start heel.
		Assertions.assertEquals(50_000L, voyagePlan.getStartingHeelInM3());
		// End heel under min requirement, but as close as possible
		Assertions.assertEquals(40_000L, voyagePlan.getRemainingHeelInM3());
	}

	@Test
	public void testVoyageRangeToOpen_OK_EndEither_NoEndHeel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 10_000L;
		final long startHeelRange[] = new long[] { 0L, 50_000L };

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IHeelOptionConsumerPortSlot to = Mockito.mock(IHeelOptionConsumerPortSlot.class);

		IHeelOptionConsumer heelOptionConsumer = Mockito.mock(IHeelOptionConsumer.class);
		Mockito.when(heelOptionConsumer.getMinimumHeelAcceptedInM3()).thenReturn(50_000L);
		Mockito.when(heelOptionConsumer.getMaximumHeelAcceptedInM3()).thenReturn(100_000L);
		Mockito.when(heelOptionConsumer.getExpectedTankState()).thenReturn(VesselTankState.EITHER);

		Mockito.when(to.getHeelOptionsConsumer()).thenReturn(heelOptionConsumer);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, -1, -1);
		Assertions.assertEquals(0, violationCount);

		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		Assertions.assertEquals(10_000L, voyagePlan.getStartingHeelInM3());
		// End with zero heel even though min is non-zero as we are in Either end state
		Assertions.assertEquals(0L, voyagePlan.getRemainingHeelInM3());
	}

	@Test
	public void testVoyageRangeToOpen_OK_EndEither_EndHeel() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 10_000L;
		final long startHeelRange[] = new long[] { 20_000L, 50_000L };

		IPortSlot from = Mockito.mock(IPortSlot.class);
		IHeelOptionConsumerPortSlot to = Mockito.mock(IHeelOptionConsumerPortSlot.class);

		IHeelOptionConsumer heelOptionConsumer = Mockito.mock(IHeelOptionConsumer.class);
		Mockito.when(heelOptionConsumer.getMinimumHeelAcceptedInM3()).thenReturn(10_000L);
		Mockito.when(heelOptionConsumer.getMaximumHeelAcceptedInM3()).thenReturn(100_000L);
		Mockito.when(heelOptionConsumer.getExpectedTankState()).thenReturn(VesselTankState.EITHER);

		Mockito.when(to.getHeelOptionsConsumer()).thenReturn(heelOptionConsumer);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, -1, -1);
		Assertions.assertEquals(0, violationCount);

		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		Assertions.assertEquals(20_000L, voyagePlan.getStartingHeelInM3());
		// End with min heel
		Assertions.assertEquals(10_000L, voyagePlan.getRemainingHeelInM3());
	}

	@Test
	public void testVoyageRange_EndCooldown() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 284__158L;
		final long startHeelRange[] = new long[] { 3_000__000L, 4_000__000L };
		
		IHeelOptionSupplierPortSlot from = Mockito.mock(IHeelOptionSupplierPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);
		
		IHeelOptionSupplier heelOptionsSupplier = Mockito.mock(IHeelOptionSupplier.class);
		Mockito.when(heelOptionsSupplier.getMinimumHeelAvailableInM3()).thenReturn(3_000__000L);
		Mockito.when(heelOptionsSupplier.getMaximumHeelAvailableInM3()).thenReturn(4_000__000L);
		
		Mockito.when(from.getHeelOptionsSupplier()).thenReturn(heelOptionsSupplier);
		
		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);
		ballastDetails.setCooldownPerformed(true);
		
		int voyageDuration = 72;
		
		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);
		
		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, LNGVoyageCalculator.STATE_COLD_COOLDOWN, 450_000);
		Assertions.assertEquals(1, violationCount);
		
		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getStartingHeelInM3());
		Assertions.assertEquals(0L, voyagePlan.getRemainingHeelInM3());
	}
	@Test
	public void testVoyageRange_EndWarm() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		long lngCommitmentInM3 = 284__158L;
		final long startHeelRange[] = new long[] { 3_000__000L, 4_000__000L };

		IHeelOptionSupplierPortSlot from = Mockito.mock(IHeelOptionSupplierPortSlot.class);
		IPortSlot to = Mockito.mock(IPortSlot.class);

		IHeelOptionSupplier heelOptionsSupplier = Mockito.mock(IHeelOptionSupplier.class);
		Mockito.when(heelOptionsSupplier.getMinimumHeelAvailableInM3()).thenReturn(3_000__000L);
		Mockito.when(heelOptionsSupplier.getMaximumHeelAvailableInM3()).thenReturn(4_000__000L);

		Mockito.when(from.getHeelOptionsSupplier()).thenReturn(heelOptionsSupplier);

		VoyageOptions options = new VoyageOptions(from, to);
		VoyageDetails ballastDetails = new VoyageDetails(options);
		ballastDetails.setCooldownPerformed(true);

		int voyageDuration = 72;

		// No input values needed. We just need to check outputs.
		VoyagePlan voyagePlan = new VoyagePlan();
		// These are the values that can be modified
		voyagePlan.setRemainingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setStartingHeelInM3(Long.MAX_VALUE);
		voyagePlan.setLNGFuelVolume(Long.MAX_VALUE);

		// No violations
		final int violationCount = calc.calculateNonCargoEndState(voyagePlan, ballastDetails, voyageDuration, startHeelRange, lngCommitmentInM3, LNGVoyageCalculator.STATE_WARM, 450_000);
		Assertions.assertEquals(1, violationCount);

		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getLNGFuelVolume());
		Assertions.assertEquals(lngCommitmentInM3, voyagePlan.getStartingHeelInM3());
		Assertions.assertEquals(0L, voyagePlan.getRemainingHeelInM3());
	}
}

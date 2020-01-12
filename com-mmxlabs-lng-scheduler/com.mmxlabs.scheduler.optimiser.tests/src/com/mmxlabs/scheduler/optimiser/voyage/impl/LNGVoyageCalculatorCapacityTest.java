/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

public class LNGVoyageCalculatorCapacityTest {

	@Test
	public void testCheckCargoCapacityViolations_Unshipped_Pass() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		// Strictly one of these should be an *Option variant, but it does not matter for this code.
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		// Replace value above
		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 100_000L, 100_000L);

		// No voyage, no boil-off or heel requirements.
		long startHeel = 0;
		long endHeel = 0;
		long lngCommitmentInM3 = 0;
		final long[] remainingHeelInM3 = new long[2];

		// E.g. nominal vessel capacity.
		long cargoCapacityInM3 = 140_000;

		// No violations
		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(0, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_Unshipped_VesselCapacity_1() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		// Strictly one of these should be an *Option variant, but it does not matter for this code.
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		// Replace value above
		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 100_000L, 100_000L);

		// No voyage, no boil-off or heel requirements.
		long startHeel = 0;
		long endHeel = 0;
		long lngCommitmentInM3 = 0;
		final long[] remainingHeelInM3 = new long[2];

		// E.g. nominal vessel capacity.
		long cargoCapacityInM3 = 90_000;

		// Lower vessel capacity should trigger issues on both load and discharge limits
		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(2, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_Unshipped_VesselCapacity_2() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		// Strictly one of these should be an *Option variant, but it does not matter for this code.
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		// Replace value above
		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);

		// No voyage, no boil-off or heel requirements.
		long startHeel = 0;
		long endHeel = 0;
		long lngCommitmentInM3 = 0;
		final long[] remainingHeelInM3 = new long[2];

		// E.g. nominal vessel capacity.
		long cargoCapacityInM3 = 90_000;

		// Lower vessel capacity should trigger issues on just load limit
		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(1, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_SimpleVoyage_Pass() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);

		long startHeel = 5_000L;
		long endHeel = 5_000L;
		long lngCommitmentInM3 = 40_000L;
		long cargoCapacityInM3 = 140_000L;
		final long[] remainingHeelInM3 = new long[] { 5_000L, 5_000L };

		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(0, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_SimpleVoyage_Fail_CargoCapacity() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);

		long startHeel = 5_000L;
		long endHeel = 5_000L;
		long lngCommitmentInM3 = 40_000L;
		long cargoCapacityInM3 = 100_000;
		final long[] remainingHeelInM3 = new long[] { 5_000L, 5_000L };

		// Vessel cannot meet min load + starting heel
		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(1, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_SimpleVoyage_Fail_MaxLoad() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);

		long startHeel = 5_000L;
		long endHeel = 10_000L;
		long lngCommitmentInM3 = 50_000L;
		long cargoCapacityInM3 = 140_000;
		final long[] remainingHeelInM3 = new long[] { 10_000L, 10_000L };

		// Vessel max load does not cover heel , boil-off and min discharge requirements.
		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(1, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_SimpleVoyage_Fail_EndHeel_1() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);
		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);

		long startHeel = 5_000L;
		long endHeel = 5_000L;
		long lngCommitmentInM3 = 40_000L;
		long cargoCapacityInM3 = 140_000;
		final long[] remainingHeelInM3 = new long[] { 6_000L, 6_000L };

		// End heel is under range
		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(1, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_SimpleVoyage_Fail_EndHeel_2() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);
		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 50_000L, 100_000L);

		long startHeel = 5_000L;
		long endHeel = 5_000L;
		long lngCommitmentInM3 = 40_000L;
		long cargoCapacityInM3 = 140_000;
		final long[] remainingHeelInM3 = new long[] { 4_000L, 4_000L };

		// End heel is over range
		final int violationCount = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(1, violationCount);
	}

	@Test
	public void testCheckCargoCapacityViolations_SimpleVoyage_Fail_MinDischarge() {
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		loadSlot.setVolumeLimits(true, 100_000L, 100_000L);
		dischargeSlot.setVolumeLimits(true, 100_000L, 100_000L);

		long startHeel = 5_000L;
		long endHeel = 5_000L;
		long lngCommitmentInM3 = 40_000L;
		long cargoCapacityInM3 = 140_000;
		final long[] remainingHeelInM3 = new long[] { 5_000L, 5_000L };

		// MIN_DISCHARGE
		final int violationCountC = calc.checkCargoCapacityViolations(startHeel, lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, endHeel, remainingHeelInM3, null);
		Assertions.assertEquals(1, violationCountC);
	}

}

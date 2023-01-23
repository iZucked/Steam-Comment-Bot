/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class InPortBoilOffLDUnconstrainedTests extends InPortBoilOffTests {

	public static Collection testCases() {
		return Arrays.asList(new Object[][] {
				// Name, loadPortBoilOff, dischargePortBoilOff, vesselCapacity, expectedViolations, compensateForBoilOff, dischargeHours, startHeel, endHeel
				{ "BasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[] {}, false, 24, 0, 0 }, // Base case
				{ "BasicWithBoilOff", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, false, 24, 0, 0 }, // Base Boil off case
				{ "CompensationWithBoilOffOverCapped", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, true, 24, 0, 0 }, // Boil off with compensation on, vessel capacity too large to allow
				// compensate.
				{ "CompensationWithBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[] {}, true, 24, 0, 0 }, // Boil off with compensation on, vessel capacity sufficiently low to allow it.
				{ "CompensationWithExcessiveBoilOff", 200_000, 2_000, 100_000, new CapacityViolationType[] { CapacityViolationType.MAX_LOAD }, true, 24, 0, 0 }, // Excessive boil off, causes MAX_LOAD
																																									// issue
				{ "LongDurationDischargeBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[] {}, true, 480, 0, 0 }, // Extended duration discharge event, within fuel limits.
				{ "BoilOffWithHeel", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, true, 24, 1000, 2000 }, // Check boiloff compensation if start and end heel are included
				{ "BoilOffWithCappedHeel", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, true, 24, 145_000, 2000 }, // Check that only boiloff volume is added when compensating for a full
																																// heel.
		});
	}

	@ParameterizedTest
	@MethodSource("testCases")
	@Tag(TestCategories.MICRO_TEST)
	public void LDBoilOffTest(final String name, final int loadPortBoilOff, final int dischargePortBoilOff, final int vesselCapacity, final CapacityViolationType[] expectedViolations,
			final boolean compensateForBoilOff, final int dischargeHours, final int expectedStartHeelInM3, final int expectedEndHeelInM3) throws Exception {

		portB.setDischargeDuration(dischargeHours);
		vessel.setCapacity(vesselCapacity);

		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, "9") //
				.build() //
				.withVesselAssignment(vesselCharter1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartHeel((double) expectedStartHeelInM3, (double) expectedStartHeelInM3, 22.8, "9")//
				.withEndHeel(expectedEndHeelInM3, expectedEndHeelInM3, expectedEndHeelInM3 > 0 ? EVesselTankState.MUST_BE_COLD : EVesselTankState.MUST_BE_WARM, null)//
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 7, 0, 0), LocalDateTime.of(2015, 12, 4, 13, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 30, 0, 0, 0)).build();

		changeBoilOffRates(loadPortBoilOff, dischargePortBoilOff);
		final SimpleCargoAllocation result = runScenario(compensateForBoilOff, false);

		final int actualLoadVolumeInM3 = result.getLoadVolume();
		final int actualDischargeVolumeInM3 = result.getDischargeVolume();
		final int actualPhysicalLoadVolumeInM3 = result.getPhysicalLoadVolume();
		final int actualPhysicalDischargeVolumeInM3 = result.getPhysicalDischargeVolume();

		final int actualStartHeelInM3 = result.getStartHeel();
		final int actualEndHeelInM3 = result.getEndHeel();

		final int portLoadMaxInM3 = result.getLoadAllocation().getSlot().getSlotOrDelegateMaxQuantity();
		final int journeyFuelInM3 = result.getJourneyFuelVolumeInM3();

		int expectedLoadVolumeInM3 = Math.min(vesselCapacity, portLoadMaxInM3);
		// Compensate for boiloff?
		if (compensateForBoilOff && vesselCapacity < portLoadMaxInM3) {
			expectedLoadVolumeInM3 += loadPortBoilOff;
		}

		// Is the boiloff in excess of vessel capacity? Pass theoretical journey volume.
		if (loadPortBoilOff >= vesselCapacity || dischargePortBoilOff >= vesselCapacity) {
			expectedLoadVolumeInM3 = loadPortBoilOff + (dischargePortBoilOff * dischargeHours) / 24 + journeyFuelInM3;
		}
		// Capped start heel
		if (expectedStartHeelInM3 == 145_000) {
			expectedLoadVolumeInM3 = expectedStartHeelInM3 + loadPortBoilOff;
		}

		final int expectedPhysicalLoadVolumeInM3 = expectedLoadVolumeInM3 - loadPortBoilOff;

		int expectedDischargeVolumeInM3 = expectedLoadVolumeInM3 - loadPortBoilOff - journeyFuelInM3 - (dischargePortBoilOff * dischargeHours) / 24;
		// Capped start heel
		if (expectedStartHeelInM3 == 145_000) {
			expectedDischargeVolumeInM3 = expectedDischargeVolumeInM3 - dischargePortBoilOff;
		}

		Assertions.assertEquals(expectedLoadVolumeInM3, actualLoadVolumeInM3 + actualStartHeelInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedDischargeVolumeInM3, actualDischargeVolumeInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedPhysicalLoadVolumeInM3, actualPhysicalLoadVolumeInM3 + actualStartHeelInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedDischargeVolumeInM3, actualPhysicalDischargeVolumeInM3, ROUNDING_EPSILON);

		final int expectedViolationCount = expectedViolations.length;
		final int actualViolationCount = result.getViolationsCount();
		Assertions.assertEquals(expectedViolationCount, actualViolationCount);

		// CompensationWIthExcessiveBoilOff
		if (actualViolationCount > 0) {
			final CapacityViolationType actualViolation = result.getLoadViolations().get(0).getKey();
			Assertions.assertEquals(expectedViolations[0], actualViolation);

		}
	}
}

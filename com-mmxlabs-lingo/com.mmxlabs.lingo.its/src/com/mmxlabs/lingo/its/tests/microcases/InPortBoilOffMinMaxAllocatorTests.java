/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * Test cases for load and discharge events with NBO when using MinMaxUnconstrainedVolumeAllocator.
 * 
 * @author NSteadman
 */
public class InPortBoilOffMinMaxAllocatorTests extends InPortBoilOffTests {

	public static Collection testCases() {
		return Arrays.asList(new Object[][] {
				// Name, loadPortBoilOff, dischargePortBoilOff, vesselCapacity, expectedViolations, compensateForBoilOff, dischargeHours, priceExpression
				{ "BasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[] {}, false, 24, "9" }, // Base case
				{ "BasicWithBoilOff", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, false, 24, "9" }, // Boil off with no compensation
				{ "CompensationWithBoilOffOverCapped", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, true, 24, "9" }, // Attempt compensation, vessel capacity larger than slot max. No
																																// compensation.
				{ "CompensationWithBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[] {}, true, 24, "9" }, // Attempt compensation, works.
				{ "CompensationWithExcessiveBoilOff", 200_000, 2_000, 100_000, new CapacityViolationType[] { CapacityViolationType.MAX_LOAD }, true, 24, "9" }, // Boil off too large, cause violations.
				{ "LongDurationDischargeBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[] {}, true, 480, "9" }, // Extended duration discharge event.
				{ "MinBasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[] {}, false, 24, "7" }, // No Boil off, execute min load due to poor sales price.
				{ "MinBasicWithBoilOff", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, false, 24, "7" }, // Boil off, no compensation, execute min load due to poor sales price.
				{ "MinBasicWithCompensation", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, true, 24, "7" }, }); // Boil off with compensation, execute min load due to poor sales price.
	}

	@ParameterizedTest
	@MethodSource("testCases")
	@Tag(TestCategories.MICRO_TEST)
	public void LDBoilOffTest(final String name, final int loadPortBoilOff, final int dischargePortBoilOff, final int vesselCapacity, final CapacityViolationType[] expectedViolations,
			final boolean compensateForBoilOff, final int dischargeHours, final String priceExpression) throws Exception {

		portB.setDischargeDuration(dischargeHours);
		vessel.setCapacity(vesselCapacity);

		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, priceExpression) //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		changeBoilOffRates(loadPortBoilOff, dischargePortBoilOff);
		final SimpleCargoAllocation result = runScenario(compensateForBoilOff, true);

		final int actualLoadVolumeInM3 = result.getLoadVolume();
		final int actualDischargeVolumeInM3 = result.getDischargeVolume();
		int actualPhysicalLoadVolumeInM3 = result.getPhysicalLoadVolume();
		int actualPhysicalDischargeVolumeInM3 = result.getPhysicalDischargeVolume();

		final int portLoadMaxInM3 = result.getLoadAllocation().getSlot().getSlotOrDelegateMaxQuantity();
		final int journeyFuelInM3 = result.getJourneyFuelVolumeInM3();

		int actualStartHeelInM3 = result.getStartHeel();
		int expectedStartHeelInM3 = 0;

		int expectedLoadVolumeInM3 = Math.min(vesselCapacity, portLoadMaxInM3) + expectedStartHeelInM3;
		// Compensate for boiloff?
		if (compensateForBoilOff && vesselCapacity < portLoadMaxInM3) {
			expectedLoadVolumeInM3 += loadPortBoilOff;
		}
		// Is the boiloff in excess of vessel capacity? Or is sale price lower than purchase ? Pass theoretical journey volume.
		if (loadPortBoilOff >= vesselCapacity || dischargePortBoilOff >= vesselCapacity || Integer.parseInt(priceExpression) < 9) {
			expectedLoadVolumeInM3 = loadPortBoilOff + (dischargePortBoilOff * dischargeHours) / 24 + journeyFuelInM3;
		}

		int expectedPhysicalLoadVolumeInM3 = expectedLoadVolumeInM3 - loadPortBoilOff;

		final int expectedDischargeVolumeInM3 = expectedLoadVolumeInM3 - loadPortBoilOff - journeyFuelInM3 - (dischargePortBoilOff * dischargeHours) / 24;
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

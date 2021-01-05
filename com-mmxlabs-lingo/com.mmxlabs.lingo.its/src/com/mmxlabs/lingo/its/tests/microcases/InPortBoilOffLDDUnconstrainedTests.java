/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * Test cases for load and discharge events with NBO when using Load-Discharge-Discharge.
 * 
 * @author NSteadman
 */
@ExtendWith(ShiroRunner.class)
public class InPortBoilOffLDDUnconstrainedTests extends InPortBoilOffTests {

	public static Collection testCases() {
		return Arrays.asList(new Object[][] {
				// Name, loadPortBoilOff, dischargePortBoilOff, vesselCapacity, expectedViolations, compensateForBoilOff, portDischargeVolumes
				{ "BasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[] {}, false, new int[] { 60_000, 71_000, 60_000, 71_000 } }, // Base case
				{ "BasicWithBoilOff", 1_000, 1_000, 145_000, new CapacityViolationType[] {}, false, new int[] { 60_000, 71_000, 60_000, 71_000 } }, // Acceptable levels of boil off, no compensation
				{ "BasicWithBoilOffSecondExceeded", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, false, new int[] { 60_000, 71_000, 60_000, 71_000 } }, // Boil off high enough that cannot
																																									// make max volume on both
																																									// discharges, no compensation
				{ "BasicWithBoilOffBothExceeded", 1_000, 20_000, 145_000, new CapacityViolationType[] { CapacityViolationType.MIN_DISCHARGE }, false, new int[] { 60_000, 71_000, 60_000, 71_000 } }, // Boil
																																																		// off
																																																		// sufficiently
																																																		// high
																																																		// to
																																																		// prevent
																																																		// achieving
																																																		// min
																																																		// discharge
																																																		// on
																																																		// both
																																																		// slots
				{ "DoubleMinFailure", 1_000, 1_000, 100_000, new CapacityViolationType[] { CapacityViolationType.MIN_DISCHARGE }, false, new int[] { 60_000, 71_000, 60_000, 71_000 } }, // Boil off
																																															// sufficiently
																																															// high to
																																															// prevent
																																															// achieving
																																															// min
																																															// discharge
																																															// on both
																																															// slots
				{ "CompensationWithBoilOff", 1_000, 1_000, 135_000, new CapacityViolationType[] {}, true, new int[] { 60_000, 71_000, 60_000, 71_000 } }, // Boil off with compensation turned on.

		});
	}

	@ParameterizedTest
	@MethodSource("testCases")
	@Tag(TestCategories.MICRO_TEST)
	public void LDBoilOffTest(final String name, final int loadPortBoilOff, final int dischargePortBoilOff, final int vesselCapacity, final CapacityViolationType[] expectedViolations,
			final boolean compensateForBoilOff, final int[] portDischargeRanges) throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();
		
		vessel.setCapacity(vesselCapacity);
		vessel.setFillCapacity(1.0);

		final Port portC = portFinder.findPortById(InternalDataConstants.PORT_PECEM);

		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, "9") //
				.withVolumeLimits(portDischargeRanges[0], portDischargeRanges[1], VolumeUnits.M3)//
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 20), portC, null, entity, "9") //
				.withVolumeLimits(portDischargeRanges[2], portDischargeRanges[3], VolumeUnits.M3) //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		changeBoilOffRates(loadPortBoilOff, dischargePortBoilOff);
		final SimpleCargoAllocation result = runScenario(compensateForBoilOff, false);

		final int actualLoadVolumeInM3 = result.getLoadVolume();
		final int actualDischargeVolumeInM3 = result.getDischargeVolume();
		final int actualDischargeVolumeBInM3 = result.getDischargeVolumeB();
		final int actualPhysicalLoadVolumeInM3 = result.getPhysicalLoadVolume();
		final int actualPhysicalDischargeVolumeInM3 = result.getPhysicalDischargeVolume();
		final int actualPhysicalDischargeVolumeBInM3 = result.getPhysicalDischargeVolumeB();

		final int portLoadMaxInM3 = result.getLoadAllocation().getSlot().getSlotOrDelegateMaxQuantity();
		final int journeyFuelInM3 = result.getJourneyFuelVolumeInM3();

		final int actualStartHeelInM3 = result.getStartHeel();
		final int actualEndHeelInM3 = result.getEndHeel();
		final int expectedStartHeelInM3 = 0;
		final int expectedEndHeelInM3 = 0;

		// Fuel Cost if maxDischarges can be met
		final int targetTotalCost = portDischargeRanges[1] + portDischargeRanges[3] + journeyFuelInM3 + loadPortBoilOff + (2 * dischargePortBoilOff);
		// Difference between min and max discharge on second discharge slot
		final int dischargeBVolumeRange = portDischargeRanges[3] - portDischargeRanges[2];
		// Difference between min and max discharge on first discharge slot
		final int dischargeAVolumeRange = portDischargeRanges[1] - portDischargeRanges[0];

		int expectedLoadVolumeInM3 = Math.min(vesselCapacity - expectedStartHeelInM3, portLoadMaxInM3);
		// Compensate for boiloff?
		if (compensateForBoilOff && vesselCapacity < portLoadMaxInM3) {
			expectedLoadVolumeInM3 += loadPortBoilOff;
		}
		// Is the boiloff in excess of vessel capacity? Pass theoretical journey volume.
		if (loadPortBoilOff >= vesselCapacity || dischargePortBoilOff >= vesselCapacity) {
			expectedLoadVolumeInM3 = loadPortBoilOff + dischargePortBoilOff + journeyFuelInM3;
		}

		final int expectedPhysicalLoadVolumeInM3 = expectedLoadVolumeInM3 - loadPortBoilOff;

		// If the costs will be in excess of the available fuel
		boolean excessiveFuelCost = false;
		int fuelCostExcess = 0;
		if (targetTotalCost > expectedLoadVolumeInM3) {
			excessiveFuelCost = true;
			fuelCostExcess = targetTotalCost - expectedLoadVolumeInM3;
		}

		int expectedDischargeVolumeInM3 = portDischargeRanges[1];
		if (excessiveFuelCost) {

			if (fuelCostExcess > dischargeBVolumeRange) {
				// If excess is large enough to prevent the minimum discharge on both slots
				if (fuelCostExcess > dischargeAVolumeRange + dischargeBVolumeRange) {
					expectedDischargeVolumeInM3 = portDischargeRanges[0];
				} else {
					expectedDischargeVolumeInM3 = portDischargeRanges[1] - (fuelCostExcess - dischargeBVolumeRange);
				}
			}
		}

		int expectedDischargeVolumeBInM3 = portDischargeRanges[3] - journeyFuelInM3;
		if (excessiveFuelCost) {
			// Fuel excess doesn't prevent the second discharge slot meeting its minimum, first discharge unaffected
			if (fuelCostExcess <= dischargeBVolumeRange) {
				expectedDischargeVolumeBInM3 = portDischargeRanges[3] - fuelCostExcess;
				// Fuel excess causes the second discharge to use minimum discharge, first discharge must compensate
			} else if (fuelCostExcess < dischargeBVolumeRange + dischargeAVolumeRange) {
				expectedDischargeVolumeBInM3 = portDischargeRanges[2];
				// Fuel excess could cause both discharges to meet minimum volumes
			} else {
				expectedDischargeVolumeBInM3 = expectedLoadVolumeInM3 - loadPortBoilOff - portDischargeRanges[0] - (dischargePortBoilOff * 2) - journeyFuelInM3;
			}
		}

		Assertions.assertEquals(expectedLoadVolumeInM3, actualLoadVolumeInM3 + actualStartHeelInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedDischargeVolumeInM3, actualDischargeVolumeInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedDischargeVolumeBInM3, actualDischargeVolumeBInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedPhysicalLoadVolumeInM3, actualPhysicalLoadVolumeInM3 + actualStartHeelInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedDischargeVolumeInM3, actualPhysicalDischargeVolumeInM3, ROUNDING_EPSILON);
		Assertions.assertEquals(expectedDischargeVolumeBInM3, actualPhysicalDischargeVolumeBInM3, ROUNDING_EPSILON);

		final int expectedViolationCount = expectedViolations.length;
		final int actualViolationCount = result.getViolationsCount();
		Assertions.assertEquals(expectedViolationCount, actualViolationCount);

		// CompensationWIthExcessiveBoilOff
		if (actualViolationCount > 0) {
			final CapacityViolationType actualViolation = result.getDischargeViolationsB().get(0).getKey();
			Assertions.assertEquals(expectedViolations[0], actualViolation);

		}
	}
}

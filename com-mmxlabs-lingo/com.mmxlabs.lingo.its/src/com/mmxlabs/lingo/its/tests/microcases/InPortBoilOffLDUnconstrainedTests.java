package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;

@RunWith(Parameterized.class)
public class InPortBoilOffLDUnconstrainedTests extends InPortBoilOffTests {

	private String name;
	private int loadPortBoilOff;
	private int dischargePortBoilOff;
	private int vesselCapacity;
	private CapacityViolationType[] expectedViolations;
	private boolean compensateForBoilOff;
	private int dischargeHours;

	public InPortBoilOffLDUnconstrainedTests(String name, int loadPortBoilOff, int dischargePortBoilOff, int vesselCapacity, CapacityViolationType[] expectedViolations, boolean compensateForBoilOff,
			int dischargeHours) {
		this.name = name;
		this.loadPortBoilOff = loadPortBoilOff;
		this.dischargePortBoilOff = dischargePortBoilOff;
		this.vesselCapacity = vesselCapacity;
		this.expectedViolations = expectedViolations;
		this.compensateForBoilOff = compensateForBoilOff;
		this.dischargeHours = dischargeHours;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection testCases() {
		return Arrays.asList(new Object[][] {
				// Name, loadPortBoilOff, dischargePortBoilOff, vesselCapacity, expectedViolations, compensateForBoilOff, dischargeHours
				{ "BasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[] {}, false, 24 }, { "BasicWithBoilOff", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, false, 24 }, // Base case
				{ "CompensationWithBoilOffOverCapped", 1_000, 2_000, 145_000, new CapacityViolationType[] {}, true, 24 }, // Boil off with compensation on, vessel capacity too large to allow compensate.
				{ "CompensationWithBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[] {}, true, 24 }, // Boil off with compensation on, vessel capacity sufficiently low to allow it.
				{ "CompensationWithExcessiveBoilOff", 200_000, 2_000, 100_000, new CapacityViolationType[] { CapacityViolationType.MAX_LOAD }, true, 24 }, // Excessive boil off, causes MAX_LOAD issue 
				{ "LongDurationDischargeBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[] {}, true, 480 }, }); // Extended duration discharge event, within fuel limits.
	}

	@Test
	@Category({ MicroTest.class })
	public void LDBoilOffTest() throws Exception {

		portB.setDischargeDuration(dischargeHours);
		vessel.setCapacity(vesselCapacity);

		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, "9") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		changeBoilOffRates(loadPortBoilOff, dischargePortBoilOff);
		SimpleCargoAllocation result = runScenario(compensateForBoilOff, false);

		int actualLoadVolumeInM3 = result.getLoadVolume();
		int actualDischargeVolumeInM3 = result.getDischargeVolume();
		
		int actualStartHeelInM3 = result.getStartHeel();
		int actualEndHeelInM3 = result.getEndHeel();
		int expectedStartHeelInM3 = 0;
		int expectedEndHeelInM3 = 0;

		int portLoadMaxInM3 = result.getLoadAllocation().getSlot().getMaxQuantity();
		int journeyIdleFuelInM3 = result.getJourneyIdelFuelVolumeInM3();

		int expectedLoadVolumeInM3 = Math.min(vesselCapacity, portLoadMaxInM3) + expectedStartHeelInM3;
		// Compensate for boiloff?
		if (compensateForBoilOff && vesselCapacity < portLoadMaxInM3) {
			expectedLoadVolumeInM3 += loadPortBoilOff;
		}
		// Is the boiloff in excess of vessel capacity? Pass theoretical journey volume.
		if (loadPortBoilOff >= vesselCapacity || dischargePortBoilOff >= vesselCapacity) {
			expectedLoadVolumeInM3 = loadPortBoilOff + (dischargePortBoilOff * dischargeHours) / 24 + journeyIdleFuelInM3;
		}

		int expectedDischargeVolumeInM3 = expectedLoadVolumeInM3 - loadPortBoilOff - journeyIdleFuelInM3 - (dischargePortBoilOff * dischargeHours) / 24 - expectedEndHeelInM3;
		Assert.assertEquals(expectedLoadVolumeInM3, actualLoadVolumeInM3 + actualStartHeelInM3, ROUNDING_EPSILON);
		Assert.assertEquals(expectedDischargeVolumeInM3, actualDischargeVolumeInM3 - actualEndHeelInM3, ROUNDING_EPSILON);

		int expectedViolationCount = expectedViolations.length;
		int actualViolationCount = result.getViolationsCount();
		Assert.assertEquals(expectedViolationCount, actualViolationCount);

		// CompensationWIthExcessiveBoilOff
		if (actualViolationCount > 0) {
			CapacityViolationType actualViolation = result.getLoadViolations().get(0).getKey();
			Assert.assertEquals(expectedViolations[0], actualViolation);

		}
	}
}

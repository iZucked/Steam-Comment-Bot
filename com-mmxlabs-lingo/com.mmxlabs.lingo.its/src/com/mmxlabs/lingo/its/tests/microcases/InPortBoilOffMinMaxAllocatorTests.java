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
import com.mmxlabs.lingo.its.tests.microcases.InPortBoilOffTests.scenarioResult;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.FuelUnit;

@RunWith(Parameterized.class)
public class InPortBoilOffMinMaxAllocatorTests extends InPortBoilOffTests {
	
		private String name;
		private int loadPortBoilOff;
		private int dischargePortBoilOff;
		private int vesselCapacity;
		private CapacityViolationType[] expectedViolations;
		private boolean compensateForBoilOff;
		private int dischargeHours;
		private String priceExpression;
		
		
		public InPortBoilOffMinMaxAllocatorTests(String name, int loadPortBoilOff, int dischargePortBoilOff, int vesselCapacity, CapacityViolationType[] expectedViolations, boolean compensateForBoilOff, int dischargeHours, String priceExpression){
			this.name = name;
			this.loadPortBoilOff = loadPortBoilOff;
			this.dischargePortBoilOff = dischargePortBoilOff;
			this.vesselCapacity = vesselCapacity;
			this.expectedViolations = expectedViolations;
			this.compensateForBoilOff = compensateForBoilOff;
			this.dischargeHours = dischargeHours;
			this.priceExpression = priceExpression;
		}
		
		@Parameterized.Parameters(name = "{0}")
		public static Collection testCases() {
			return Arrays.asList(new Object[][] {
				// Name, loadPortBoilOff, dischargePortBoilOff, vesselCapacity, expectedViolations, compensateForBoilOff, dischargeHours, priceExpression
				{"BasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[]{}, false, 24, "9"},
				{"BasicWithBoilOff", 1_000, 2_000, 145_000, new CapacityViolationType[]{}, false, 24, "9"},
				{"CompensationWithBoilOffOverCapped", 1_000, 2_000, 145_000, new CapacityViolationType[]{}, true, 24, "9"},
				{"CompensationWithBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[]{}, true, 24, "9"},
				{"CompensationWithExcessiveBoilOff", 200_000, 2_000, 100_000, new CapacityViolationType[]{CapacityViolationType.MAX_LOAD}, true, 24, "9"},
				{"LongDurationDischargeBoilOff", 1_000, 2_000, 100_000, new CapacityViolationType[]{}, true, 480, "9"},
				{"MinBasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[]{}, false, 24, "7"},
				{"MinBasicWithBoilOff", 1_000, 2_000, 145_000, new CapacityViolationType[]{}, false, 24, "7"},
			});
		}
		
		@Test
		@Category({ MicroTest.class })
		public void LDBoilOffTest() throws Exception {
			
			portB.setDischargeDuration(dischargeHours);
			vessel.setCapacity(vesselCapacity);
			
			cargo1 = cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
					.build() //
					.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, priceExpression) //
					.build() //
					.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
					.withAssignmentFlags(false, false) //
					.build();
			
			changeBoilOffRates(loadPortBoilOff,dischargePortBoilOff);
			scenarioResult results = runScenario(compensateForBoilOff, true);
			double[] transferVolumes = results.getTransferVolumes();
			int[] slotMaximumVolumes = results.getSlotMaximumVolumes();
			int violationCount = results.getViolationCount();
			int nonTransferVolumes = results.getNonTransferFuel();
	
			int expectedLoadVolume = Math.min(vesselCapacity, slotMaximumVolumes[0]);
			// If restricted by vessel capacity rather than slot capacity
			if(expectedLoadVolume < slotMaximumVolumes[0]){
				expectedLoadVolume += loadPortBoilOff;
			}
			
			//If sell price is less than purchase price
			if(Integer.parseInt(priceExpression) < 9){
				expectedLoadVolume = nonTransferVolumes + loadPortBoilOff + dischargePortBoilOff;
			}
			
			if(loadPortBoilOff > vesselCapacity){
				Assert.assertNotEquals(expectedLoadVolume,transferVolumes[0], ROUNDING_EPSILON);
			} else {
				Assert.assertEquals(expectedLoadVolume,transferVolumes[0], ROUNDING_EPSILON);
			}
			
			int expectedDischargeVolume = Math.min(vesselCapacity, slotMaximumVolumes[0]) - loadPortBoilOff - nonTransferVolumes - (dischargePortBoilOff * (dischargeHours/24));
			// If restricted by vessel capacity rather than slot capacity
			if(expectedLoadVolume < slotMaximumVolumes[0]){
				expectedDischargeVolume += loadPortBoilOff;
			}
			
			//If sell price is less than purchase price
			if(Integer.parseInt(priceExpression) < 9){
				expectedDischargeVolume = 0;
			}
			
			if(loadPortBoilOff > vesselCapacity){
				Assert.assertNotEquals(expectedDischargeVolume,transferVolumes[1], ROUNDING_EPSILON);
			} else{
				Assert.assertEquals(expectedDischargeVolume, transferVolumes[1],ROUNDING_EPSILON);			
			}
			Assert.assertEquals(expectedViolations.length, violationCount);	
		}
}

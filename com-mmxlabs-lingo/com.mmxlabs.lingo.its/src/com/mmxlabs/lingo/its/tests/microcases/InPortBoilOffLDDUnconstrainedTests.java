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
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.types.VolumeUnits;

@RunWith(Parameterized.class)
public class InPortBoilOffLDDUnconstrainedTests extends InPortBoilOffTests {
	
		private String name;
		private int loadPortBoilOff;
		private int dischargePortBoilOff;
		private int vesselCapacity;
		private CapacityViolationType[] expectedViolations;
		private boolean compensateForBoilOff;
		private int[] portDischargeRanges;
		
		
		public InPortBoilOffLDDUnconstrainedTests(String name, int loadPortBoilOff, int dischargePortBoilOff, int vesselCapacity, CapacityViolationType[] expectedViolations, boolean compensateForBoilOff, int[] portDischargeRanges){
			this.name = name;
			this.loadPortBoilOff = loadPortBoilOff;
			this.dischargePortBoilOff = dischargePortBoilOff;
			this.vesselCapacity = vesselCapacity;
			this.expectedViolations = expectedViolations;
			this.compensateForBoilOff = compensateForBoilOff;
			this.portDischargeRanges = portDischargeRanges;
		}
		
		@Parameterized.Parameters(name = "{0}")
		public static Collection testCases() {
			return Arrays.asList(new Object[][] {
				// Name, loadPortBoilOff, dischargePortBoilOff, vesselCapacity, expectedViolations, compensateForBoilOff, portDischargeVolumes
				{"BasicNoBoilOff", 0, 0, 145_000, new CapacityViolationType[]{}, false, new int[]{60_000,70_000,60_000,70_000}},
				{"BasicWithBoilOff", 1_000, 1_000, 145_000, new CapacityViolationType[]{}, false, new int[]{60_000,70_000,60_000,70_000}},
				{"BasicWithBoilOffSecondExceeded", 1_000, 2_000, 145_000, new CapacityViolationType[]{}, false, new int[]{60_000,70_000,60_000,70_000}},
				{"BasicWithBoilOffBothExceeded", 1_000, 20_000, 145_000, new CapacityViolationType[]{CapacityViolationType.MIN_DISCHARGE}, false, new int[]{60_000,70_000,60_000,70_000}},
				{"CompensationWithBoilOff", 1_000, 1_000, 135_000, new CapacityViolationType[]{}, true, new int[]{60_000,70_000,60_000,70_000}},

			});
		}
		
		@Test
		@Category({ MicroTest.class })
		public void LDBoilOffTest() throws Exception {

			vessel.setCapacity(vesselCapacity);
			
			Port portC = portFinder.findPort("Petrobras Pecem LNG");
			portC.setTimeZone("UTC");
			
			cargo1 = cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
					.build() //
					.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, "9")
					.withVolumeLimits(portDischargeRanges[0], portDischargeRanges[1], VolumeUnits.M3)//
					.build() //
					.makeDESSale("D2", LocalDate.of(2015, 12, 20), portC, null, entity, "9")
					.withVolumeLimits(portDischargeRanges[2], portDischargeRanges[3], VolumeUnits.M3)
					.build()
					.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
					.withAssignmentFlags(false, false) //
					.build();	
			
			changeBoilOffRates(loadPortBoilOff,dischargePortBoilOff);
			scenarioResult results = runScenario(compensateForBoilOff, false);
			double[] transferVolumes = results.getTransferVolumes();
			int[] slotMaximumVolumes = results.getSlotMaximumVolumes();
			int violationCount = results.getViolationCount();
			int nonTransferVolumes = results.getNonTransferFuel();
			
			// Fuel Cost if maxDischarges can be met
			int targetTotalCost = portDischargeRanges[1] + portDischargeRanges[3] + nonTransferVolumes + loadPortBoilOff + (2*dischargePortBoilOff);
			// Difference between min and max discharge on second discharge slot
			int dischargeBVolumeRange = portDischargeRanges[3] - portDischargeRanges[2];
			// Difference between min and max discharge on first discharge slot
			int dischargeAVolumeRange = portDischargeRanges[1] - portDischargeRanges[0];

			int expectedLoadVolume = Math.min(vesselCapacity, slotMaximumVolumes[0]);
			// If restricted by vessel capacity rather than slot capacity, i.e. can compensate
			if(expectedLoadVolume < slotMaximumVolumes[0]){
				expectedLoadVolume += loadPortBoilOff;
			}
			
			if(loadPortBoilOff > vesselCapacity){
				Assert.assertNotEquals(expectedLoadVolume,transferVolumes[0], ROUNDING_EPSILON);
			} else {
				Assert.assertEquals(expectedLoadVolume,transferVolumes[0], ROUNDING_EPSILON);
			}	
			
			//If the costs will be in excess of the available fuel
			boolean excessiveFuelCost = false;
			int fuelCostExcess = 0;
			if(targetTotalCost > expectedLoadVolume){
				excessiveFuelCost = true;
				fuelCostExcess = targetTotalCost - expectedLoadVolume;
			}
			
			int expectedDischargeVolumeA = portDischargeRanges[1];
			if(excessiveFuelCost){
				
				if(fuelCostExcess > dischargeBVolumeRange){
					//If excess is large enough to prevent the minimum discharge on both slots
					if(fuelCostExcess > dischargeAVolumeRange + dischargeBVolumeRange){
						expectedDischargeVolumeA = portDischargeRanges[0];
					} else{
						expectedDischargeVolumeA = portDischargeRanges[1] - (fuelCostExcess - dischargeBVolumeRange);
					}
				}
			}
			
			if(loadPortBoilOff > vesselCapacity){
				Assert.assertNotEquals(expectedDischargeVolumeA,transferVolumes[1], ROUNDING_EPSILON);
			} else{
				Assert.assertEquals(expectedDischargeVolumeA, transferVolumes[1],ROUNDING_EPSILON);			
			}
			
			int expectedDischargeVolumeB = portDischargeRanges[3] - nonTransferVolumes;
			if(excessiveFuelCost){
				// Fuel excess doesn't prevent the second discharge slot meeting its minimum, first discharge unaffected
				if(fuelCostExcess <= dischargeBVolumeRange){
					expectedDischargeVolumeB = portDischargeRanges[3] - fuelCostExcess;
				// Fuel excess causes the second discharge to use minimum discharge, first discharge must compensate
				} else if(fuelCostExcess < dischargeBVolumeRange + dischargeAVolumeRange){
					expectedDischargeVolumeB = portDischargeRanges[2];
				// Fuel excess could cause both discharges to meet minimum volumes	
				} else {
					expectedDischargeVolumeB = expectedLoadVolume - loadPortBoilOff - portDischargeRanges[0] - (dischargePortBoilOff*2) - nonTransferVolumes;
				}
			}

			
			if(loadPortBoilOff > vesselCapacity){
				Assert.assertNotEquals(expectedDischargeVolumeB,transferVolumes[1], ROUNDING_EPSILON);
			} else{
				Assert.assertEquals(expectedDischargeVolumeB, transferVolumes[2],ROUNDING_EPSILON);			
			}
			Assert.assertEquals(expectedViolations.length, violationCount);	
		}
}

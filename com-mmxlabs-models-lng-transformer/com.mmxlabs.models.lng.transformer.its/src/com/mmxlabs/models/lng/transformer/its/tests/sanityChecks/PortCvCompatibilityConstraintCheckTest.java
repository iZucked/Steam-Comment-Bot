/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortCvCompatibilityConstraintChecker;

@RunWith(value = ShiroRunner.class)
public class PortCvCompatibilityConstraintCheckTest {
	final private float lowCv = 22f;
	final private float highCv = 24f;
	final private float midCv = (lowCv + highCv) / 2f;

	public SuboptimalScenarioTester getScenarioWithParams(final double smallLPCv, final double bigLPCv, final double smallDPMinCV, final double smallDPMaxCV, final double bigDPMinCV,
			final double bigDPMaxCV) {
		final SuboptimalScenarioTester result = new SuboptimalScenarioTester() {
			{
				// set load cv (from slot as it writes over the load port)
				((LoadSlot) smallToLargeCargo.getSlots().get(0)).setCargoCV(smallLPCv);
				((LoadSlot) largeToSmallCargo.getSlots().get(0)).setCargoCV(bigLPCv);

				// set discharge allowed ranges
				smallDischargePort.setMinCvValue(smallDPMinCV);
				smallDischargePort.setMaxCvValue(smallDPMaxCV);
				bigDischargePort.setMinCvValue(bigDPMinCV);
				bigDischargePort.setMaxCvValue(bigDPMaxCV);
			}
		};

		return result;

	}

	public SuboptimalScenarioTester maxRangeOnDischargeCvScenario() {
		return getScenarioWithParams(midCv, midCv, 0, 1000000, 0, 1000000);
	}

	public SuboptimalScenarioTester loadMidCvDischargeMidMinCvHighMaxCvScenario() {
		return getScenarioWithParams(midCv, midCv, lowCv, highCv, lowCv, highCv);
	}

	public SuboptimalScenarioTester loadLowCvDischargeMidMinCvScenario() {
		return getScenarioWithParams(lowCv, lowCv, midCv, highCv, midCv, highCv);
	}

	public SuboptimalScenarioTester constraintsShouldCreateSubOptimalOptimisationScenario() {
		return getScenarioWithParams(lowCv, midCv, midCv, highCv, lowCv, lowCv);
	}

	public SuboptimalScenarioTester constraintsShouldCreateSubOptimalOptimisationScenario2() {
		return getScenarioWithParams(lowCv, midCv, lowCv, highCv, 0, lowCv);
	}

	/*
	 * Test that the constraints are satisfied when the range is maximal
	 */
	@Test
	public void testConstraintCheckerPassesNoConstraints() {
		// generate a scenario with the max range on the discharge Cv limit
		// (therefore no discharge cv constraints)
		final SuboptimalScenarioTester sst = maxRangeOnDischargeCvScenario();

		// test the PortCvConstraintChecker
		sst.testConstraintCheckerPasses(new PortCvCompatibilityConstraintChecker("PortCvCompatibilityConstraintChecker"));
	}

	/*
	 * Test that the constraints are satisfied when the load is within range
	 */
	@Test
	public void testConstraintCheckerPassesSatisfiedConstraints() {
		// generate a scenario with the load Cv within the range of the
		// discharge Cv limits
		final SuboptimalScenarioTester sst = loadMidCvDischargeMidMinCvHighMaxCvScenario();

		// test the PortCvConstraintChecker
		sst.testConstraintCheckerPasses(new PortCvCompatibilityConstraintChecker("PortCvCompatibilityConstraintChecker"));
	}

	/*
	 * Test that the constraints are violated when the load is outside the range
	 */
	@Test
	public void testConstraintCheckerFailsLoadCVTooLarge() {
		// generate a scenario with the load Cv outside of the discharge Cv
		// limits
		final SuboptimalScenarioTester sst = loadLowCvDischargeMidMinCvScenario();

		// test the PortCvConstraintChecker
		sst.testConstraintCheckerFails(new PortCvCompatibilityConstraintChecker("PortCvCompatibilityConstraintChecker"));
	}

	/*
	 * Test that the constraints are violated when the load is outside the range
	 */
	@Test
	public void testNoConstraintsOptimisation() {
		// generate a suboptimal scenario
		final SuboptimalScenarioTester sst = new SuboptimalScenarioTester();

		// run the optimiser and check that the optimal wiring is produced
		sst.testOptimalWiringProduced();
	}

	/*
	 * Test that the case where no discharge Cv constraints are in place. The PnL-optimal wiring should be generated.
	 */
	@Test
	public void testConstraintsOptimalOptimisation() {
		// generate a suboptimal scenario
		final SuboptimalScenarioTester sst = maxRangeOnDischargeCvScenario();

		// run the optimiser and check that the optimal wiring is produced
		sst.testOptimalWiringProduced();
	}

	/*
	 * Tests the case where discharge Cv constraints are in place, which prevent the PnL-optimal wiring. The PnL-suboptimal wiring should be generated.
	 */
	@Test
	public void testConstraintsSubOptimalOptimisationScenario1() {
		// generate a suboptimal scenario
		final SuboptimalScenarioTester sst = constraintsShouldCreateSubOptimalOptimisationScenario2();

		// run the optimiser and check that a suboptimal wiring is produced
		sst.testSuboptimalWiringProduced();
	}

	/*
	 * Tests the case where discharge Cv constraints are in place, which prevent the PnL-optimal wiring. The PnL-suboptimal wiring should be generated.
	 */
	@Test
	public void testConstraintsSubOptimalOptimisationScenario2() {
		// generate a suboptimal scenario
		final SuboptimalScenarioTester sst = constraintsShouldCreateSubOptimalOptimisationScenario2();

		// run the optimiser and check that a suboptimal wiring is produced
		sst.testSuboptimalWiringProduced();
	}

}

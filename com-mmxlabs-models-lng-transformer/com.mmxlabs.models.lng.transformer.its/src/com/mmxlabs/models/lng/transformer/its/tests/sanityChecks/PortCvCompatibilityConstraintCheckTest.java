/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortCvCompatibilityConstraintChecker;

@ExtendWith(ShiroRunner.class)
public class PortCvCompatibilityConstraintCheckTest {

	private static final float LOW_CV = 22f;
	private static final float HIGH_CV = 24f;
	private static final float MID_CV = (LOW_CV + HIGH_CV) / 2f;

	public SuboptimalScenarioTester getScenarioWithParams(final double smallLPCv, final double bigLPCv, final double smallDPMinCV, final double smallDPMaxCV, final double bigDPMinCV,
			final double bigDPMaxCV) {
		final SuboptimalScenarioTester result = new SuboptimalScenarioTester();

		// set load cv (from slot as it writes over the load port)
		((LoadSlot) result.smallToLargeCargo.getSlots().get(0)).setCargoCV(smallLPCv);
		((LoadSlot) result.largeToSmallCargo.getSlots().get(0)).setCargoCV(bigLPCv);

		// set discharge allowed ranges
		result.smallDischargePort.setMinCvValue(smallDPMinCV);
		result.smallDischargePort.setMaxCvValue(smallDPMaxCV);
		result.bigDischargePort.setMinCvValue(bigDPMinCV);
		result.bigDischargePort.setMaxCvValue(bigDPMaxCV);

		return result;

	}

	public SuboptimalScenarioTester maxRangeOnDischargeCvScenario() {
		return getScenarioWithParams(MID_CV, MID_CV, 0, 1000000, 0, 1000000);
	}

	public SuboptimalScenarioTester loadMidCvDischargeMidMinCvHighMaxCvScenario() {
		return getScenarioWithParams(MID_CV, MID_CV, LOW_CV, HIGH_CV, LOW_CV, HIGH_CV);
	}

	public SuboptimalScenarioTester loadLowCvDischargeMidMinCvScenario() {
		return getScenarioWithParams(LOW_CV, LOW_CV, MID_CV, HIGH_CV, MID_CV, HIGH_CV);
	}

	public SuboptimalScenarioTester constraintsShouldCreateSubOptimalOptimisationScenario() {
		return getScenarioWithParams(LOW_CV, MID_CV, MID_CV, HIGH_CV, LOW_CV, LOW_CV);
	}

	public SuboptimalScenarioTester constraintsShouldCreateSubOptimalOptimisationScenario2() {
		return getScenarioWithParams(LOW_CV, MID_CV, LOW_CV, HIGH_CV, 0, LOW_CV);
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
	 * Tests the case where discharge CV constraints are in place, which prevent the PnL-optimal wiring. The PnL-suboptimal wiring should be generated.
	 */
	@Test
	public void testConstraintsSubOptimalOptimisationScenario1() {
		// generate a suboptimal scenario
		final SuboptimalScenarioTester sst = constraintsShouldCreateSubOptimalOptimisationScenario();

		// run the optimiser and check that a suboptimal wiring is produced
		sst.testSuboptimalWiringProduced();
	}

	/*
	 * Tests the case where discharge CV constraints are in place, which prevent the PnL-optimal wiring. The PnL-suboptimal wiring should be generated.
	 */
	@Test
	public void testConstraintsSubOptimalOptimisationScenario2() {
		// generate a suboptimal scenario
		final SuboptimalScenarioTester sst = constraintsShouldCreateSubOptimalOptimisationScenario2();

		// run the optimiser and check that a suboptimal wiring is produced
		sst.testSuboptimalWiringProduced();
	}

}

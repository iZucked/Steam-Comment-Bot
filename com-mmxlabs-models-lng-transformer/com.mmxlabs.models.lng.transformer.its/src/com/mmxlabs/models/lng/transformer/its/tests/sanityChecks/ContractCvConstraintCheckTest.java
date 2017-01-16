/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintChecker;

@RunWith(value = ShiroRunner.class)
public class ContractCvConstraintCheckTest {
	final private float lowCv = 22f;
	final private float highCv = 24f;
	final private float midCv = (lowCv + highCv) / 2f;

	public SuboptimalScenarioTester differentCvScenario() {
		SuboptimalScenarioTester result = new SuboptimalScenarioTester() {
			{
				((LoadSlot) smallToLargeCargo.getSlots().get(0)).setCargoCV(lowCv);
				((LoadSlot) largeToSmallCargo.getSlots().get(0)).setCargoCV(highCv);
			}
		};

		return result;
	}

	@Test
	public void testVariousConstraints() {
		final SuboptimalScenarioTester sst = differentCvScenario();
		sst.testConstraintCheckerPasses(new ContractCvConstraintChecker("ContractCvConstraintChecker"));

		SalesContract stlContract = (SalesContract) sst.smallToLargeCargo.getSlots().get(1).getContract();

		// check that the constraints aren't violated when CV values fall within specified ranges
		// (the stl cargo has CV of lowCv, and the lts cargo has CV of highCv)
		stlContract.setMinCvValue(lowCv - 1);
		stlContract.setMaxCvValue(midCv);
		sst.testConstraintCheckerPasses(new ContractCvConstraintChecker("ContractCvConstraintChecker"));

		// check that the constraints are violated when one CV value is too high
		stlContract.setMaxCvValue(lowCv - 1);
		sst.testConstraintCheckerFails(new ContractCvConstraintChecker("ContractCvConstraintChecker"));

		// check that the constraints are violated when one CV value is too low
		stlContract.unsetMaxCvValue();
		stlContract.setMinCvValue(midCv);
		sst.testConstraintCheckerFails(new ContractCvConstraintChecker("ContractCvConstraintChecker"));

		// check that the constraints aren't violated when the contracts don't have specified ranges
		stlContract.unsetMinCvValue();
		sst.testConstraintCheckerPasses(new ContractCvConstraintChecker("ContractCvConstraintChecker"));

	}

	/*
	 * Tests the case where no CV constraints are attached to the scenario. The PnL-optimal wiring should be generated.
	 */

	@Test
	public void testNoConstraintsOptimisation() {
		// generate a suboptimal scenario in which the small->large cargo has a low CV and the large->small cargo has a high CV
		final SuboptimalScenarioTester sst = differentCvScenario();
		((LoadSlot) sst.largeToSmallCargo.getSlots().get(0)).setCargoCV(highCv);

		DischargeSlot dischargeSlot1 = (DischargeSlot) sst.largeToSmallCargo.getSlots().get(1);
		DischargeSlot dischargeSlot2 = (DischargeSlot) sst.smallToLargeCargo.getSlots().get(1);

		sst.largeToSmallCargo.getSlots().set(1, dischargeSlot2);
		// As we have just swapped the slots, smallToLargeCargo no longer has a discharge slot, so we add rather than set
		sst.smallToLargeCargo.getSlots().add(1, dischargeSlot1);

		sst.testConstraintCheckerPasses(new ContractCvConstraintChecker("CvConstraintChecker"));

		// final SuboptimalScenarioTester sst = new SuboptimalScenarioTester();
		// run the optimiser and check that the optimal wiring is produced
		sst.testOptimalWiringProduced();
	}

	/*
	 * 
	 * Tests the case where a maximum CV constraint is attached to one of the sales contracts in the scenario. The PnL-suboptimal wiring should be generated.
	 */
	@Test
	public void testMaxCvConstraintOptimisation() {
		// generate a suboptimal scenario in which the small->large cargo has a low CV and the large->small cargo has a high CV
		final SuboptimalScenarioTester sst = differentCvScenario();

		// prohibit the PnL-optimal rewiring by setting the sales contract on the STL cargo to have a
		// max CV value which is too low for the LTS cargo
		SalesContract contract = (SalesContract) sst.smallToLargeCargo.getSlots().get(1).getContract();
		contract.setMaxCvValue(midCv);

		// test that the optimiser doesn't produce the prohibited wiring
		sst.testSuboptimalWiringProduced();
	}

}

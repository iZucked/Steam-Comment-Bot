/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.omg.PortableInterceptor.SUCCESSFUL;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.util.ScenarioUtils;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AbstractPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintChecker;

public class ContractCvConstraintCheckTest {
	final private float lowCv = 22f;
	final private float highCv = 24f;
	final private float midCv = (lowCv + highCv) / 2f;

	/**
	 * Creates a scenario object which has a strongly suboptimal wiring in PnL terms, ready to be rewired by the optimiser if constraints permit.
	 * 
	 * The scenario involves two load slots with large and small maximum cargo quantities respectively, and two discharge slots with matching large and small maximum cargo quantities. The optimum
	 * wiring (constraints permitting) is to wire the large load slot (at "bigLoadPort") to the large discharge slot (at "bigDischargePort") and the small load slot (at "smallLoadPort") to the small
	 * discharge slot (at "smallDischargePort"), to allow the maximum amount of cargo to be shipped in total. The returned scenario actually wires "bigLoadPort"->"smallDischargePort" and
	 * "smallLoadPort"->"bigDischargePort".
	 * 
	 * @author Simon McGregor
	 */
	public class SuboptimalScenarioTester {
		private static final int cscDischargePrice = 1;

		public Port smallLoadPort;
		public Port bigLoadPort;
		public Port smallDischargePort;
		public Port bigDischargePort;

		public Cargo smallToLargeCargo;
		public Cargo largeToSmallCargo;

		public LNGScenarioModel scenario;

		public SuboptimalScenarioTester() {
			final CustomScenarioCreator csc = new CustomScenarioCreator(cscDischargePrice);
			final int loadPrice = 1;
			final int dischargePrice = 70;
			final int fuelPrice = 1;
			final int smallQty = 50000;
			final int bigQty = 500000;

			smallLoadPort = ScenarioTools.createPort("smallLoadPort");
			bigLoadPort = ScenarioTools.createPort("bigLoadPort");
			smallDischargePort = ScenarioTools.createPort("smallDischargePort");
			bigDischargePort = ScenarioTools.createPort("bigDischargePort");

			// a list of ports to use in the scenario
			final Port[] ports = new Port[] { smallLoadPort, bigLoadPort, smallDischargePort, bigDischargePort };

			// Add the ports, and set the distances.
			SanityCheckTools.setPortDistances(csc, ports);

			// create a few vessels and add them to the list of vessels created.
			final int numOfClassOne = 2;

			// createVessels creates and adds the vessels to the scenario.
			csc.addVesselSimple("classOne", numOfClassOne, fuelPrice, 25, 1000000, 10, 10, 0, 500, false);

			final Date cargoStart = new Date(System.currentTimeMillis());

			// create two different cargoes
			smallToLargeCargo = csc.addCargo("S-L-Cargo", smallLoadPort, bigDischargePort, loadPrice, dischargePrice, 22, cargoStart, 50);
			largeToSmallCargo = csc.addCargo("L-S-Cargo", bigLoadPort, smallDischargePort, loadPrice, dischargePrice, 22, cargoStart, 50);

			// make sure they can be rewired
			smallToLargeCargo.setAllowRewiring(true);
			largeToSmallCargo.setAllowRewiring(true);

			// set up one cargo with a small load allowance and a large discharge allowance
			smallToLargeCargo.getSlots().get(0).setMaxQuantity(smallQty);
			smallToLargeCargo.getSlots().get(1).setMaxQuantity(bigQty);

			// set up one cargo with a large load allowance and a small discharge allowance
			largeToSmallCargo.getSlots().get(0).setMaxQuantity(bigQty);
			largeToSmallCargo.getSlots().get(1).setMaxQuantity(smallQty);

			// the csc by default will initialise all cargoes with the same purchase and sales contracts
			// we will give them separate contracts in case constraints are placed on the contracts
			smallToLargeCargo.getSlots().get(0).setContract(csc.addPurchaseContract("Other Purchase Contract"));
			smallToLargeCargo.getSlots().get(1).setContract(csc.addSalesContract("Other Sales Contract", cscDischargePrice));

			// build and run the scenario.
			scenario = csc.buildScenario();
		}

		/**
		 * Runs the optimiser on the scenario and tests whether the optimised wiring matches a specified expected wiring.
		 * 
		 * @param loadPorts
		 *            A list of load ports for the expected wiring, in the same order as the discharge ports they should be wired to.
		 * @param dischargePorts
		 *            A list of discharge ports for the expected wiring, in the same order as the load ports they should be wired to.
		 */
		public void testExpectedWiringProduced(Port[] loadPorts, Port[] dischargePorts) {
			Assert.assertEquals("Load port and discharge port lists should have same length", loadPorts.length, dischargePorts.length);

			final int n = loadPorts.length;

			// optimise the scenario
			ScenarioRunner runner = new ScenarioRunner((LNGScenarioModel) scenario);
			try {
				runner.init();
				runner.run();
			} catch (IncompleteScenarioException e) {
				// this exception should not occur
				Assert.assertTrue("Scenario runner failed to initialise artificial suboptimal scenario.", false);
			}

			final Schedule schedule = runner.getFinalSchedule();

			// set up an array storing whether load ports are assigned at all
			final boolean[] found = new boolean[n];
			for (int i = 0; i < n; i++) {
				found[i] = false;
			}

			// check that the cargo allocations wire load ports up with the expected discharge ports
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				Port loadPort = ca.getSlotAllocations().get(0).getPort();
				Port dischargePort = ca.getSlotAllocations().get(1).getPort();

				int index = Arrays.asList(loadPorts).indexOf(loadPort);
				Assert.assertTrue(String.format("Load port '%s' was assigned a wiring but the expected wiring does not contain it.", loadPort.getName()), index >= 0);

				if (index >= 0) {
					found[index] = true;
					Assert.assertTrue(
							String.format("Expected solution wires '%s' to '%s' but the allocation wires it to '%s'.", loadPort.getName(), dischargePorts[index].getName(), dischargePort.getName()),
							dischargePorts[index].equals(dischargePort));
				}
			}

			// check that all expected load ports were assigned
			for (int i = 0; i < n; i++) {
				Assert.assertTrue(String.format("Expected a wiring for load port '%s' but none assigned in solution.", loadPorts[i].getName()), found[i]);
			}

		}

		/**
		 * Runs the optimiser and tests that the post-optimiser wiring is the most profitable one. This should be the case unless the PnL-optimal wiring is prohibited by a constraint.
		 */
		public void testOptimalWiringProduced() {
			Port[] loadPorts = { smallLoadPort, bigLoadPort };
			Port[] dischargePorts = { smallDischargePort, bigDischargePort };

			testExpectedWiringProduced(loadPorts, dischargePorts);
		}

		/**
		 * Runs the optimiser and tests that the post-optimiser wiring is the less profitable one. This should be the case if the PnL-optimal wiring is prohibited by a constraint.
		 */
		public void testSuboptimalWiringProduced() {
			Port[] loadPorts = { smallLoadPort, bigLoadPort };
			Port[] dischargePorts = { bigDischargePort, smallDischargePort };

			testExpectedWiringProduced(loadPorts, dischargePorts);
		}

		/*
		 * public void testConstraintChecker(boolean expectedResult, IConstraintCheckerFactory factory) { IConstraintChecker checker = factory.instantiate();
		 * 
		 * final LNGTransformer transformer = new LNGTransformer(scenario, new ContractExtensionTestModule()); final IOptimisationData data = transformer.getOptimisationData();
		 * 
		 * transformer.getInjector().injectMembers(checker);
		 * 
		 * final List<ISequenceElement> elements = data.getSequenceElements(); final ISequence sequence = new ListSequence(elements);
		 * 
		 * final List<String> errors = new ArrayList<String>();
		 * 
		 * final boolean result = checker.checkSequence(sequence, null, errors);
		 * 
		 * final String format = "Constraint checker %s should %s on this scenario."; String failureMessage = String.format(format, checker.getName(), (expectedResult ? "pass" : "fail")); if (!result)
		 * { failureMessage = failureMessage + String.format(" Failed with %d error(s) (beginning '%s').", errors.size(), errors.get(0)); } Assert.assertEquals(failureMessage, expectedResult, result);
		 * 
		 * }
		 */

		/**
		 * Tests a particular constraint checker against the generated scenario and compares the result to the expected result of running that checker.
		 * 
		 * @param expectedResult
		 * @param checker
		 *            A new instance of the constraint checker (must be new since this method injects it with dependencies).
		 */
		// TODO: rewrite to take a constraint checker factory instead of a constraint checker, so that the expected parameter does not have weird not-initialised semantics
		public void testConstraintChecker(boolean expectedResult, AbstractPairwiseConstraintChecker checker) {
			final LNGTransformer transformer = new LNGTransformer(scenario, ScenarioUtils.createDefaultSettings(), new TransformerExtensionTestModule());
			final IOptimisationData data = transformer.getOptimisationData();

			transformer.getInjector().injectMembers(checker);
			final List<ISequenceElement> elements = data.getSequenceElements();
			final ISequence sequence = new ListSequence(elements);

			final List<String> errors = new ArrayList<String>();

			final boolean result = checker.checkSequence(sequence, null, errors);

			final String format = "Constraint checker %s should %s on this scenario.";
			String failureMessage = String.format(format, checker.getName(), (expectedResult ? "pass" : "fail"));
			if (!result) {
				failureMessage = failureMessage + String.format(" Failed with %d error(s) (beginning '%s').", errors.size(), errors.get(0));
			}
			Assert.assertEquals(failureMessage, expectedResult, result);

		}

		/**
		 * Tests to see that a constraint checker permits the generated scenario.
		 * 
		 * @param checker
		 */
		public void testConstraintCheckerPasses(AbstractPairwiseConstraintChecker checker) {
			testConstraintChecker(true, checker);
		}

		/**
		 * Tests to see that a constraint checker prohibits the generated scenario.
		 * 
		 * @param checker
		 */
		public void testConstraintCheckerFails(AbstractPairwiseConstraintChecker checker) {
			testConstraintChecker(false, checker);
		}

	}

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

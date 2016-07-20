/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;

import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AbstractPairwiseConstraintChecker;

/**
 * Creates a scenario object which has a strongly suboptimal wiring in PnL terms, ready to be rewired by the optimiser if constraints permit.
 * 
 * The scenario involves two load slots with large and small maximum cargo quantities respectively, and two discharge slots with matching large and small maximum cargo quantities. The optimum wiring
 * (constraints permitting) is to wire the large load slot (at "bigLoadPort") to the large discharge slot (at "bigDischargePort") and the small load slot (at "smallLoadPort") to the small discharge
 * slot (at "smallDischargePort"), to allow the maximum amount of cargo to be shipped in total. The returned scenario actually wires "bigLoadPort"->"smallDischargePort" and
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

		final LocalDateTime cargoStart = LocalDateTime.now();

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
	public void testExpectedWiringProduced(final Port[] loadPorts, final Port[] dischargePorts) {
		Assert.assertEquals("Load port and discharge port lists should have same length", loadPorts.length, dischargePorts.length);

		final int n = loadPorts.length;

		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			// optimise the scenario

			final OptimisationPlan plan = LNGScenarioRunnerUtils.createDefaultOptimisationPlan();
			for (OptimisationStage stage : plan.getStages()) {
				if (stage instanceof LocalSearchOptimisationStage) {
					LocalSearchOptimisationStage lsoStage = (LocalSearchOptimisationStage) stage;
					lsoStage.getAnnealingSettings().setIterations(10_000);
				}
			}
			final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, (LNGScenarioModel) scenario, plan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			runner.evaluateInitialState();
			runner.run();

			final Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);

			// set up an array storing whether load ports are assigned at all
			final boolean[] found = new boolean[n];
			for (int i = 0; i < n; i++) {
				found[i] = false;
			}

			// check that the cargo allocations wire load ports up with the expected discharge ports
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				final Port loadPort = ca.getSlotAllocations().get(0).getPort();
				final Port dischargePort = ca.getSlotAllocations().get(1).getPort();

				final int index = Arrays.asList(loadPorts).indexOf(loadPort);
				Assert.assertTrue(String.format("Load port '%s' was assigned a wiring but the expected wiring does not contain it.", loadPort.getName()), index >= 0);

				if (index >= 0) {
					found[index] = true;
					Assert.assertTrue(
							String.format("Expected solution wires '%s' to '%s' but the allocation wires it to '%s'. (load cv = %f; discharge min cv = %f; discharge max cv = %f", loadPort.getName(),
									dischargePorts[index].getName(), dischargePort.getName(), loadPort.getCvValue(), dischargePort.getMinCvValue(), dischargePort.getMaxCvValue()),
							dischargePorts[index].equals(dischargePort));
				}
			}

			// check that all expected load ports were assigned
			for (int i = 0; i < n; i++) {
				Assert.assertTrue(String.format("Expected a wiring for load port '%s' but none assigned in solution.", loadPorts[i].getName()), found[i]);
			}
		} finally {
			executorService.shutdownNow();
		}

	}

	/**
	 * Runs the optimiser and tests that the post-optimiser wiring is the most profitable one. This should be the case unless the PnL-optimal wiring is prohibited by a constraint.
	 */
	public void testOptimalWiringProduced() {
		final Port[] loadPorts = { smallLoadPort, bigLoadPort };
		final Port[] dischargePorts = { smallDischargePort, bigDischargePort };

		testExpectedWiringProduced(loadPorts, dischargePorts);
	}

	/**
	 * Runs the optimiser and tests that the post-optimiser wiring is the less profitable one. This should be the case if the PnL-optimal wiring is prohibited by a constraint.
	 */
	public void testSuboptimalWiringProduced() {
		final Port[] loadPorts = { smallLoadPort, bigLoadPort };
		final Port[] dischargePorts = { bigDischargePort, smallDischargePort };

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
	 * final String format = "Constraint checker %s should %s on this scenario."; String failureMessage = String.format(format, checker.getName(), (expectedResult ? "pass" : "fail")); if (!result) {
	 * failureMessage = failureMessage + String.format(" Failed with %d error(s) (beginning '%s').", errors.size(), errors.get(0)); } Assert.assertEquals(failureMessage, expectedResult, result);
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
	public void testConstraintChecker(final boolean expectedResult, final AbstractPairwiseConstraintChecker checker) {

		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		final Set<String> hints = LNGTransformerHelper.getHints(settings);
		final LNGDataTransformer transformer = new LNGDataTransformer(scenario, settings, ScenarioUtils.createDefaultSolutionBuilderSettings(), hints,
				LNGTransformerHelper.getOptimiserInjectorServices(new TransformerExtensionTestBootstrapModule(), null));

		// final LNGDataTransformer transformer = new LNGDataTransformer(scenario, ScenarioUtils.createDefaultSettings(), new TransformerExtensionTestBootstrapModule(), null);
		final IOptimisationData data = transformer.getOptimisationData();

		transformer.getInjector().injectMembers(checker);
		final List<ISequenceElement> elements = data.getSequenceElements();
		final ISequence sequence = new ListSequence(elements);

		final List<String> errors = new ArrayList<String>();

		final Resource fakeResource = new Resource(new SimpleIndexingContext(), "SG Testing Resource for null analysis - find a better way to obatin this.");
		final boolean result = checker.checkSequence(sequence, fakeResource, errors);

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
	public void testConstraintCheckerPasses(final AbstractPairwiseConstraintChecker checker) {
		testConstraintChecker(true, checker);
	}

	/**
	 * Tests to see that a constraint checker prohibits the generated scenario.
	 * 
	 * @param checker
	 */
	public void testConstraintCheckerFails(final AbstractPairwiseConstraintChecker checker) {
		testConstraintChecker(false, checker);
	}

}
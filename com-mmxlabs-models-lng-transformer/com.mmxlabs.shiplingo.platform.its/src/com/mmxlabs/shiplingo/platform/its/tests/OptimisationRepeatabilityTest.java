/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.manifest.ManifestPackage;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.its.tests.scenarios.AbstractOptimisationResultTester;

/**
 * Run a test scenario several times to try and see if it varies.
 * 
 * TODO: Share impl with {@link AbstractOptimisationResultTester}
 * 
 * @author Simon Goodall
 * 
 */
public class OptimisationRepeatabilityTest {

	@BeforeClass
	public static void registerEMF() {

		// Trigger EMF initialisation outside of eclipse environment.
		@SuppressWarnings("unused")
		Object instance = null;
		instance = MMXCorePackage.eINSTANCE;
		instance = ManifestPackage.eINSTANCE;
		instance = AnalyticsPackage.eINSTANCE;
		instance = CargoPackage.eINSTANCE;
		instance = CommercialPackage.eINSTANCE;
		instance = FleetPackage.eINSTANCE;
		instance = InputPackage.eINSTANCE;
		instance = OptimiserPackage.eINSTANCE;
		instance = PortPackage.eINSTANCE;
		instance = PricingPackage.eINSTANCE;
		instance = SchedulePackage.eINSTANCE;
		// Add other packages?
	}

	@Test
	public void testScenario() throws IOException, InterruptedException, MigrationException, IncompleteScenarioException {

		final URL url = getClass().getResource("/OptimisationRepeatabilityTest.scenario");

		testScenario(url, 5);
	}

	private void testScenario(final URL url, final int numTries) throws IOException, InterruptedException, MigrationException, IncompleteScenarioException {
		ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(URI.createURI(url.toString()), true);

		final MMXRootObject originalScenario = (MMXRootObject) instance.getInstance();

		final ScenarioRunner[] scenarioRunners = new ScenarioRunner[numTries];
		for (int i = 0; i < numTries; ++i) {

			// TODO: Does EcoreUtil.copy work -- do we need to do it here?
			final MMXRootObject copy = duplicate(originalScenario);

			scenarioRunners[i] = new ScenarioRunner(copy);
			scenarioRunners[i].init();

			if (i > 0) {
				// Ensure same initial schedules
				final MatchModel match = MatchService.doMatch(scenarioRunners[0].getIntialSchedule(), scenarioRunners[i].getIntialSchedule(), null);
				final DiffModel diff = DiffService.doDiff(match);
				final EList<DiffElement> differences = diff.getDifferences();
				// Dump any differences to std.err before asserting
				for (final DiffElement d : differences) {
					System.err.println(d.toString());
				}

				Assert.assertTrue("initial solution 0 and " + i + " should be the same", differences.isEmpty());
			}
		}

		// Run all the optimisations
		for (int i = 0; i < numTries; ++i) {
			scenarioRunners[i].run();
			if (i > 0) {
				// Ensure same final schedules
				final MatchModel match = MatchService.doMatch(scenarioRunners[0].getFinalSchedule(), scenarioRunners[i].getFinalSchedule(), null);
				final DiffModel diff = DiffService.doDiff(match);
				final EList<DiffElement> differences = diff.getDifferences();

				// Dump any differences to std.err before asserting
				for (final DiffElement d : differences) {
					System.err.println(d.toString());
				}

				Assert.assertTrue(differences.isEmpty());
			}
		}

		// Ensure source model is unchanged
		for (int i = 0; i < numTries; ++i) {
			final MatchModel match = MatchService.doMatch(originalScenario, scenarioRunners[i].getScenario(), null);
			final DiffModel diff = DiffService.doDiff(match);
			final EList<DiffElement> differences = diff.getDifferences();
			// Dump any differences to std.err before asserting
			for (final DiffElement d : differences) {
				System.err.println(d.toString());
			}

			Assert.assertTrue("final solution 0 and " + i + " should be the same", differences.isEmpty());
		}
	}

	MMXRootObject duplicate(MMXRootObject original) {
		final List<EObject> originalSubModels = new ArrayList<EObject>();
		for (final MMXSubModel subModel : original.getSubModels()) {
			originalSubModels.add(subModel.getSubModelInstance());
		}

		final Collection<EObject> duppedSubModels = EcoreUtil.copyAll(originalSubModels);

		MMXRootObject duplicate = MMXCoreFactory.eINSTANCE.createMMXRootObject();
		for (EObject eObject : duppedSubModels) {
			duplicate.addSubModel((UUIDObject) eObject);
		}

		resolve(duppedSubModels);

		return duplicate;

	}

	private void collect(final EObject object, final HashMap<String, UUIDObject> table) {
		if (object == null) {
			return;
		}
		if (object instanceof MMXObject)
			((MMXObject) object).collectUUIDObjects(table);
		else {
			for (final EObject o : object.eContents())
				collect(o, table);
		}
	}

	public void resolve(final Collection<EObject> parts) {
		final HashMap<String, UUIDObject> table = new HashMap<String, UUIDObject>();
		for (final EObject part : parts) {
			collect(part, table);
		}
		// now restore proxies
		for (final EObject part : parts) {
			resolve(part, table);
		}
	}

	private void resolve(final EObject part, final HashMap<String, UUIDObject> table) {
		if (part == null) {
			return;
		}
		if (part instanceof MMXObject) {
			((MMXObject) part).resolveProxies(table);
			((MMXObject) part).restoreProxies();
		} else {
			for (final EObject child : part.eContents())
				resolve(child, table);
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.IOException;
import java.net.URL;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.lingo.its.tests.scenarios.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.utils.MigrationHelper;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.scenario.service.manifest.ManifestPackage;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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
		instance = AssignmentPackage.eINSTANCE;
		instance = ParametersPackage.eINSTANCE;
		instance = PortPackage.eINSTANCE;
		instance = PricingPackage.eINSTANCE;
		instance = SchedulePackage.eINSTANCE;
		// Add other packages?
	}

	@Ignore
	@Test
	public void testScenario() throws IOException, InterruptedException, IncompleteScenarioException {

		final URL url = getClass().getResource("/OptimisationRepeatabilityTest.scenario");

		testScenario(url, 5);
	}

	private void testScenario(final URL url, final int numTries) throws IOException, InterruptedException, IncompleteScenarioException {
		final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(URI.createURI(url.toString()), false);

		MigrationHelper.migrateAndLoad(instance);

		// Initial duplicate to remove e.g. eResource references.
		final LNGScenarioModel originalScenario = EcoreUtil.copy((LNGScenarioModel) instance.getInstance());

		final ScenarioRunner[] scenarioRunners = new ScenarioRunner[numTries];
		for (int i = 0; i < numTries; ++i) {

			// TODO: Does EcoreUtil.copy work -- do we need to do it here?
			final LNGScenarioModel copy = EcoreUtil.copy(originalScenario);

			scenarioRunners[i] = new ScenarioRunner(copy);
			scenarioRunners[i].init();

			if (i > 0) {
				// Ensure same initial schedules
				final IComparisonScope scope = EMFCompare.createDefaultScope(scenarioRunners[i].getIntialSchedule(), scenarioRunners[0].getIntialSchedule());
				final Comparison comparison = EMFCompare.newComparator(scope).compare();

				final EList<Diff> differences = comparison.getDifferences();
				for (final Diff d : differences) {

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

				final IComparisonScope scope = EMFCompare.createDefaultScope(scenarioRunners[i].getFinalSchedule(), scenarioRunners[0].getFinalSchedule());
				final Comparison comparison = EMFCompare.newComparator(scope).compare();

				// Dump any differences to std.err before asserting
				final EList<Diff> differences = comparison.getDifferences();
				for (final Diff d : differences) {
					System.err.println(d.toString());
				}

				Assert.assertTrue(differences.isEmpty());
			}
		}

		// Ensure source model is unchanged
		for (int i = 0; i < numTries; ++i) {

			final IComparisonScope scope = EMFCompare.createDefaultScope(scenarioRunners[i].getScenario(), originalScenario);
			final Comparison comparison = EMFCompare.newComparator(scope).compare();

			final EList<Diff> differences = comparison.getDifferences();
			// Dump any differences to std.err before asserting
			for (final Diff d : differences) {
				System.err.println(d.toString());
			}

			Assert.assertTrue("final solution 0 and " + i + " should be the same: " + differences, differences.isEmpty());
		}
	}
}

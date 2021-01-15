/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.tests.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.InstanceData;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ConcurrentModelReferencesTest {

	@Test
	public void test() throws InterruptedException {
		// Repeat a few times
		for (int i = 0; i < 10; ++i) {
			doTest();
		}
	}

	@SuppressWarnings("null")
	public void doTest() throws InterruptedException {

		// Set up the scenario stuff
		final ScenarioService scenarioService = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		final ScenarioInstance instance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();

		scenarioService.getElements().add(instance);

		final IScenarioService mockedScenarioService = Mockito.mock(IScenarioService.class);
		scenarioService.setServiceRef(mockedScenarioService);

		// Runnable batch size
		final int numRunnables = 60;

		EContentAdapter contentAdapter = new EContentAdapter();

		final List<Runnable> runnables = new LinkedList<>();
		EditingDomain domain = Mockito.mock(EditingDomain.class);
		ScenarioModelRecord modelRecord = new ScenarioModelRecord(ManifestFactory.eINSTANCE.createManifest(), (r, m) -> {
			EObject eObject = EcoreFactory.eINSTANCE.createEObject();
			return new InstanceData(r, eObject, domain, new BasicCommandStack(), (mr, d) -> {
				// Save hook
			}, (d) -> {
				// Close hook
			});
		});
		modelRecord.setScenarioInstance(instance);

		SSDataManager.Instance.register(instance, modelRecord);

		for (int i = 0; i < numRunnables; ++i) {
			runnables.add(() -> {

				try (ModelReference ref = modelRecord.aquireReference("ConcurrentModelReferencesTest")) {
					try {
						Thread.sleep(new Random().nextInt(100) * 10L);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}

			});
		}

		// This block is disabled because it is known to be unsafe due to underlying EMF data structure choices.
		if (false) {
			// Add content adapters to the mix (they will add themselves to all contained objects in the tree)
			for (int i = 0; i < numRunnables; ++i) {
				runnables.add(() -> {

					// TODO: This can cause concurrency issues if the underlying array needs to grow.
					// This also needs to be synchronized!
					instance.eAdapters().add(contentAdapter);

					// Sleep before removing
					try {
						Thread.sleep(new Random().nextInt(100) * 10L);
					} catch (final Exception e) {
						e.printStackTrace();
					}

					instance.eAdapters().remove(contentAdapter);

				});
			}
		}
		// Randomise execution order
		Collections.shuffle(runnables);

		// Each runnable has a sleep up to 1 second. Add some overhead
		ConcurrencyTestUtil.assertConcurrent("Concurrency failure!", runnables, 3 * numRunnables * 1 * 2);
	}

}

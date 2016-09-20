/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.tests.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.ecore.util.EContentAdapter;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

public class ConcurrentModelReferencesTest {

	@Test
	public void test() throws InterruptedException {
		// Repeat a few times
		for (int i = 0; i < 10; ++i) {
			doTest();
		}
	}

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
		for (int i = 0; i < numRunnables; ++i) {
			runnables.add(() -> {
				final List<ModelReference> refs = instance.getModelReferences();

				try (ModelReference ref = instance.getReference("ConcurrentModelReferencesTest")) {
					try {
						Thread.sleep(new Random().nextInt(100) * 10);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}

			});
		}
		for (int i = 0; i < numRunnables; ++i) {
			runnables.add(() -> {
				final List<ModelReference> refs = instance.getModelReferences();

				synchronized (refs) {

					for (final ModelReference ref : refs) {
						try {
							Thread.sleep(new Random().nextInt(100) * 10);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
		// Add content adapters to the mix (they will add themselves to all contained objects in the tree)
		for (int i = 0; i < numRunnables; ++i) {
			runnables.add(() -> {

				instance.eAdapters().add(contentAdapter);

				// Sleep before removing
				try {
					Thread.sleep(new Random().nextInt(100) * 10);
				} catch (final Exception e) {
					e.printStackTrace();
				}

				instance.eAdapters().remove(contentAdapter);

			});
		}

		// Randomise execution order
		Collections.shuffle(runnables);

		// Each runnable has a sleep up to 1 second. Add some overhead
		ConcurrencyTestUtil.assertConcurrent("Concurrency failure!", runnables, 3 * numRunnables * 1 * 2);
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class ScenarioServiceCommandUtil {

	public static <U extends Container> @NonNull U executeAdd(final @NonNull Container instance, final @NonNull Supplier<U> factory) {

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(instance);
		if (scenarioService != null) {
			return scenarioService.executeAdd(instance, factory);
		} else {
			final U child = factory.get();
			instance.getElements().add(child);
			return child;
		}
	}

	public static <T extends Container> void execute(final @NonNull T instance, final @NonNull Consumer<T> c) {

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(instance);
		if (scenarioService != null) {
			scenarioService.execute(instance, c);
		} else {
			c.accept(instance);
		}
	}

	public static <T extends ScenarioFragment> void execute(final @NonNull T instance, final @NonNull Consumer<T> c) {

		final ScenarioInstance scenarioInstance = instance.getScenarioInstance();
		final IScenarioService scenarioService = scenarioInstance == null ? null : SSDataManager.Instance.findScenarioService(scenarioInstance);

		if (scenarioService != null) {
			scenarioService.execute(instance, c);
		} else {
			c.accept(instance);
		}
	}

	public static <T extends Container> void query(final @NonNull T instance, final @NonNull Consumer<T> c) {

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(instance);
		if (scenarioService != null) {
			scenarioService.query(instance, c);
		} else {
			c.accept(instance);
		}
	}

	public static ScenarioInstance fork(@NonNull final ScenarioInstance instance, final String finalNewName) throws Exception {
		return copyTo(instance, instance, finalNewName);
	}

	public static ScenarioInstance copyTo(@NonNull final ScenarioInstance original, final @NonNull Container destination, final @NonNull String finalNewName) throws Exception {
		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(destination);

		if (scenarioService == null) {
			throw new IllegalStateException();
		}

		final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(original);
		try (ModelReference ref = modelRecord.aquireReference("ScenarioServiceCommandUtil:1")) {

			// Duplicate the root object data
			final EObject rootObjectCopy = EcoreUtil.copy(ref.getInstance());

			// Create the scenario duplicate
			final ScenarioInstance theDupe = scenarioService.insert(destination, rootObjectCopy, dup -> {

				// Copy across various bits of information
				dup.getMetadata().setContentType(original.getMetadata().getContentType());
				dup.getMetadata().setCreated(original.getMetadata().getCreated());
				dup.getMetadata().setLastModified(new Date());
				dup.setName(finalNewName);

				// Copy version context information
				dup.setVersionContext(original.getVersionContext());
				dup.setScenarioVersion(original.getScenarioVersion());

				dup.setClientVersionContext(original.getClientVersionContext());
				dup.setClientScenarioVersion(original.getClientScenarioVersion());
			});

			return theDupe;
		}
	}
}

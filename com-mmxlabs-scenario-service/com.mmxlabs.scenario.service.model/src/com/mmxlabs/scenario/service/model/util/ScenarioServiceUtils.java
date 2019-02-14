/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public final class ScenarioServiceUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServiceUtils.class);

	public static final String FORK_PREFIX = "~";
	public static final String OPTIMISED_PREFIX = "O~";

	/**
	 * Event constant to indicate to parts to close any references to the scenario instance passed in with the event. E.g. the scenario is about to be deleted.
	 */
	public static final @NonNull String EVENT_CLOSING_SCENARIO_INSTANCE = "scenario-service-closing-scenario-instance";

	public static @NonNull String stripFileExtension(@NonNull final String currentName) {
		String newName = currentName;
		if (newName.toLowerCase().toLowerCase().endsWith(".lingo")) {
			// Guava 14+
			// newName = Files.getNameWithoutExtension(f);
			newName = newName.replaceAll("(?i).lingo", "");
		}
		return newName;
	}

	@NonNull
	public static String getForkName(@NonNull final String currentName) {
		return String.format("%s%s", FORK_PREFIX, currentName);
	}

	@NonNull
	public static String getOptimisedName(@NonNull final String currentName) {
		return String.format("%s%s", OPTIMISED_PREFIX, currentName);
	}

	@NonNull
	public static String getNextName(@NonNull final String currentName, @NonNull final Set<String> existingNames) {

		// Find a name that does not clash by appending a counter " (n)" if required
		String newName = currentName;
		int counter = 1;
		while (existingNames.contains(newName)) {
			newName = currentName + " (" + counter++ + ")";
		}
		return newName;
	}

	@NonNull
	public static Set<String> getExistingNames(@NonNull final Container parent) {

		final Set<String> existingNames = new HashSet<String>();
		for (final Container child : parent.getElements()) {
			if (child instanceof Folder) {
				existingNames.add(((Folder) child).getName());
			} else if (child instanceof ScenarioInstance) {
				existingNames.add(((ScenarioInstance) child).getName());
			}
		}
		if (parent instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) parent;
			scenarioInstance.getFragments().forEach(f -> existingNames.add(f.getName()));
		}
		return existingNames;
	}

	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioInstance scenario, @NonNull final Container destination, @NonNull final Set<String> existingNames,
			@Nullable IProgressMonitor progressMonitor) throws Exception {
		return copyScenario(scenario, destination, scenario.getName(), existingNames, progressMonitor);
	}

	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioModelRecord modelRecord, @NonNull final Container destination, final String currentName,
			@NonNull final Set<String> existingNames, @Nullable IProgressMonitor progressMonitor) throws Exception {

		final String newName = ScenarioServiceUtils.getNextName(currentName, existingNames);

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(destination);
		//
		if (scenarioService == null) {
			throw new IllegalStateException();
		}
		//
		// Create the scenario duplicate
		final ScenarioInstance theDupe = scenarioService.copyInto(destination, modelRecord, newName, progressMonitor);
		//
		return theDupe;
	}

	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioInstance scenario, @NonNull final Container destination, final String currentName, final Set<String> existingNames,
			@Nullable IProgressMonitor progressMonitor) throws Exception {

		// Some services can return null here pending some asynchronous update mechanism
		// Prefix "Copy of" only if we are copying within the container
		final boolean withinContainer = scenario.getParent() == destination;
		final String namePrefix = (withinContainer ? "Copy of " : "") + currentName;
		final String newName = ScenarioServiceUtils.getNextName(namePrefix, existingNames);
		@NonNull
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);

		return copyScenario(modelRecord, destination, newName, progressMonitor);
	}

	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioModelRecord modelRecord, @NonNull final Container destination, final String finalName,
			@Nullable IProgressMonitor progressMonitor) throws Exception {

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(destination);

		if (scenarioService == null) {
			throw new IllegalStateException();
		}
		return scenarioService.copyInto(destination, modelRecord, finalName, progressMonitor);

	}

	/**
	 * Creates a new {@link Folder} and adds it to the new destination {@link Container}
	 * 
	 * @param folder
	 * @param destination
	 * @param currentName
	 * @param existingNames
	 * @return
	 */
	@NonNull
	public static Folder copyFolder(@NonNull final Folder folder, @NonNull final Container destination, @NonNull final String currentName, @NonNull final Set<String> existingNames) {

		// Prefix "Copy of" only if we are copying within the container
		final boolean withinContainer = folder.getParent() == destination;
		final String namePrefix = (withinContainer ? "Copy of " : "") + currentName;
		final String newName = ScenarioServiceUtils.getNextName(namePrefix, existingNames);

		return executeAdd(destination, () -> {
			final Folder newFolder = ScenarioServiceFactory.eINSTANCE.createFolder();
			newFolder.setName(newName);
			newFolder.setMetadata(EcoreUtil.copy(folder.getMetadata()));
			return newFolder;
		});
	}

	public static void copyURIData(@NonNull final URI src, @NonNull final URI dest) throws IOException {
		copyURIData(new ExtensibleURIConverterImpl(), src, dest);
	}

	public static void copyURIData(@NonNull final URIConverter uc, @NonNull final URI src, @NonNull final URI dest) throws IOException {
		try (InputStream is = uc.createInputStream(src)) {
			try (OutputStream os = uc.createOutputStream(dest)) {
				ByteStreams.copy(is, os);
			}
		}
	}

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
}

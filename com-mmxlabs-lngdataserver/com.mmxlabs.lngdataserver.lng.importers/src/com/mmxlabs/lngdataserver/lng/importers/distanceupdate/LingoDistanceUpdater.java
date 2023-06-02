/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Functions;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.DataManifest;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.DistanceDataVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.Entry;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.PortReplacement;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.ui.UpdateDistancesWizard;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class LingoDistanceUpdater {

	private static final TypeReference<List<AtoBviaCLookupRecord>> TYPE_LIST = new TypeReference<List<AtoBviaCLookupRecord>>() {
	};
	private static final TypeReference<List<PortReplacement>> REPLACEMENT_LIST = new TypeReference<List<PortReplacement>>() {
	};

	public void importIntoScenario(final String filename, final ScenarioInstance scenarioInstance) throws IOException {
		final String baseURI = "archive:" + URI.createFileURI(filename).toString() + "!";

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

		DataManifest manifest;
		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/manifest.json"))) {
			manifest = mapper.readValue(inputStream, DataManifest.class);
		}

		final Map<String, Entry> entries = manifest.getEntries().stream().collect(Collectors.toMap(Entry::getType, Functions.identity()));

		final TypeReference<List<AtoBviaCLookupRecord>> TYPE_LIST = new TypeReference<List<AtoBviaCLookupRecord>>() {
		};
		// This code is was never really used in production, so just adding a stub version class for api changes
		// We should really find the version from the file.
		final DistanceDataVersion distanceDataVersion = new DistanceDataVersion();
		distanceDataVersion.setVersion("manual");

		final LocationsVersion locationsVersion;
		final List<AtoBviaCLookupRecord> distanceRecords;
		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entries.get("basic-locations").getPath()))) {
			locationsVersion = mapper.readValue(inputStream, LocationsVersion.class);
		}
		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entries.get("distance-lines").getPath()))) {
			distanceRecords = mapper.readValue(inputStream, TYPE_LIST);
		}

		importIntoScenario(scenarioInstance, distanceDataVersion, locationsVersion, distanceRecords, null, null);
	}

	public static void importLocalIntoScenario(final ScenarioInstance scenarioInstance) throws IOException {

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		final DistanceDataVersion dataVersion;
		final LocationsVersion locationsVersion;
		final List<AtoBviaCLookupRecord> distanceRecords;
		final List<AtoBviaCLookupRecord> manualDistanceRecords;
		final List<PortReplacement> portReplacements;
		try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/distances-version.json")) {
			dataVersion = mapper.readValue(inputStream, DistanceDataVersion.class);
		}
		try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/ports.json")) {
			locationsVersion = mapper.readValue(inputStream, LocationsVersion.class);
		}
		try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/distances.json")) {
			distanceRecords = mapper.readValue(inputStream, TYPE_LIST);
		}
		try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/distances-manual.json")) {
			manualDistanceRecords = mapper.readValue(inputStream, TYPE_LIST);
		}
		try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/port-replacements.json")) {
			portReplacements = mapper.readValue(inputStream, REPLACEMENT_LIST);
		}

		importIntoScenario(scenarioInstance, dataVersion, locationsVersion, distanceRecords, manualDistanceRecords, portReplacements);
	}

	public static void importIntoScenario(final ScenarioInstance scenarioInstance, final DistanceDataVersion dataVersion, final LocationsVersion locationsVersion,
			final List<AtoBviaCLookupRecord> distanceRecords, final List<AtoBviaCLookupRecord> manualRecords, final List<PortReplacement> portReplacements) throws IOException {
		// Check for null inputs
		if (locationsVersion == null || distanceRecords == null) {
			return;
		}
		RunnerHelper.asyncExec(() -> {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			try (IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider(LingoDistanceUpdater.class.getSimpleName())) {
				final ModelReference modelReference = sdp.getModelReference();
				modelReference.executeWithLock(true, () -> {
					try {

						final UpdateDistancesWizard wizard = new UpdateDistancesWizard(modelReference, dataVersion, locationsVersion, distanceRecords, manualRecords, portReplacements);
						if (activeWorkbenchWindow == null) {
							// action has been disposed
							return;
						}
						wizard.init(activeWorkbenchWindow.getWorkbench(), null);

						wizard.setForcePreviousAndNextButtons(true);

						final Shell parent = activeWorkbenchWindow.getShell();
						final WizardDialog dialog = new WizardDialog(parent, wizard);
						dialog.create();
						dialog.open();

					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			}

		});
	}
}

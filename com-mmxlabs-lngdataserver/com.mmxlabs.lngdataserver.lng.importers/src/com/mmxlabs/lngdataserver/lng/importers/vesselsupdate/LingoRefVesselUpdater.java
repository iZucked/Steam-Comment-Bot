/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vesselsupdate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.vesselsupdate.ui.UpdateVesselsWizard;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class LingoRefVesselUpdater {

	private static final TypeReference<List<AtoBviaCLookupRecord>> TYPE_LIST = new TypeReference<List<AtoBviaCLookupRecord>>() {
	};

	public static void importLocalIntoScenario(final ScenarioInstance scenarioInstance) throws IOException {

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());

		final VesselsVersion vesselRecords;
		try (InputStream inputStream = LingoRefVesselUpdater.class.getResourceAsStream("/vessel-classes.json")) {
			vesselRecords = mapper.readValue(inputStream, VesselsVersion.class);
		}

		importIntoScenario(scenarioInstance, vesselRecords);
	}

	public static void importIntoScenario(final ScenarioInstance scenarioInstance, final VesselsVersion vesselRecords) {
		// Check for null inputs
		if (vesselRecords == null) {
			return;
		}
		RunnerHelper.asyncExec(() -> {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			try (IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider(LingoRefVesselUpdater.class.getSimpleName())) {
				final ModelReference modelReference = sdp.getModelReference();
				modelReference.executeWithLock(true, () -> {
					try {

						final UpdateVesselsWizard wizard = new UpdateVesselsWizard(modelReference, vesselRecords);
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

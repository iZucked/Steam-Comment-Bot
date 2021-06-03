/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vesselsimport;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsIOConstants;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.vesselsimport.ui.ImportSingleVesselWizard;
import com.mmxlabs.lngdataserver.lng.importers.vesselsupdate.LingoRefVesselUpdater;
import com.mmxlabs.models.lng.fleet.util.IVesselImportCommandProvider;
import com.mmxlabs.models.util.importer.impl.EncoderUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class VesselImporterCommand implements IVesselImportCommandProvider {
	
	@Override
	public void run(@NonNull final ScenarioInstance scenarioInstance) {
		try {
			importSingleIntoScenario(scenarioInstance);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static void importSingleIntoScenario(final ScenarioInstance scenarioInstance) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());

		final VesselsVersion vesselRecords;
		try (InputStream inputStream = LingoRefVesselUpdater.class.getResourceAsStream(String.format("/%s", VesselsIOConstants.JSON_VESSELS_PHYSICAL))) {
			vesselRecords = mapper.readValue(inputStream, VesselsVersion.class);
			importSingleIntoScenario(scenarioInstance, vesselRecords);
		}
	}

	private static void importSingleIntoScenario(final ScenarioInstance scenarioInstance, final VesselsVersion vesselRecords) {
		if (vesselRecords == null) {
			return;
		}
		vesselRecords.getVessels().stream().forEach(v -> v.setName(EncoderUtil.decode(v.getName())));
		RunnerHelper.asyncExec(() -> {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			try (IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider(VesselImporterCommand.class.getSimpleName())) {
				final ModelReference modelReference = sdp.getModelReference();
				modelReference.executeWithLock(true, () -> {
					try {
						if (activeWorkbenchWindow == null) {
							return;
						}
						final ImportSingleVesselWizard wizard = new ImportSingleVesselWizard(modelReference, vesselRecords);
						wizard.init(activeWorkbenchWindow.getWorkbench(), null);
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

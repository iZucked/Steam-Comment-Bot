/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.exportWizards;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Export the selected scenario to the filesystem somehow.
 * 
 * @author hinton
 * 
 */

public class ExportLiNGOScenarioWizard extends Wizard implements IExportWizard {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportLiNGOScenarioWizard.class);

	private ExportLiNGOScenarioWizardPage exportPage;

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {

		setWindowTitle("Export LiNGO Scenario Wizard");
		exportPage = new ExportLiNGOScenarioWizardPage(selection);
	}

	@Override
	public boolean performFinish() {
		final File destination = exportPage.getOutputDirectory();
		final ScenarioInstance instance = exportPage.getScenarioInstance();

		exportData(instance, destination);
		return true;
	}

	public boolean exportData(final ScenarioInstance instance, File destination) {

		exportPage.saveDirectorySetting();

		// Release reference on block exit
		@NonNull
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);
		try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ExportLiNGOScenarioWizard")) {

			// Create parent directories if needed.
			destination.getParentFile().mkdirs();

			ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, destination);
		} catch (IOException e) {
			LOGGER.error("Error saving scenario to " + destination.getName(), e);
			return false;
		}

		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(exportPage);
	}
}
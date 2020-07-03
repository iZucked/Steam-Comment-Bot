/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.exportWizards;

import java.io.File;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.scenario.utils.ExportCSVBundleUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * Export the selected scenario to the filesystem somehow.
 * 
 * @author hinton
 * 
 */

public class ExportCSVWizard extends Wizard implements IExportWizard {

	private static final Logger log = LoggerFactory.getLogger(ExportCSVWizard.class);

	private ExportCSVWizardPage exportPage;
	
	private boolean isExportScenario = true;
	
	public ExportCSVWizard() {
		this(true);
	}
	
	public ExportCSVWizard(boolean isExportScenario) {
		this.isExportScenario = isExportScenario;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle(isExportScenario ? "CSV Export Wizard" : "All Curves Export Wizard");
		exportPage = new ExportCSVWizardPage(selection);
	}

	@Override
	public boolean performFinish() {
		final File outputDirectory = exportPage.getOutputDirectory();
		final Collection<ScenarioInstance> instances = exportPage.getScenarioInstance();
		final char delimiter = exportPage.getCsvDelimiter();
		final char decimalSeparator = exportPage.getDecimalSeparator();

		final exportInformation info = new exportInformation();
		info.outputDirectory = outputDirectory;
		info.instances = instances;
		info.delimiter = delimiter;
		info.decimalSeparator = decimalSeparator;

		exportData(info);
		return true;
	}

	public class exportInformation {
		public File outputDirectory;

		public Collection<ScenarioInstance> instances;
		public char delimiter;
		public char decimalSeparator;
	}

	public boolean exportData(final exportInformation info) {

		final Collection<ScenarioInstance> instances = info.instances;

		final boolean createExportDirectories = instances.size() > 1;
		for (final ScenarioInstance instance : instances) {
			// Release reference on block exit
			@NonNull
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
			try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ExportCSVWizard")) {
				final String name = instance.getName();
				exportScenario(scenarioDataProvider, info, createExportDirectories, name);
			}
		}
		exportPage.saveDirectorySetting();

		return true;
	}

	public void exportScenario(final IScenarioDataProvider scenarioDataProvider, final exportInformation info, final boolean createExportDirectories, final String name) {

		final File outputDirectory = info.outputDirectory;

		final char delimiter = info.delimiter;
		final char decimalSeparator = info.decimalSeparator;

		final File directory = createExportDirectories ? new File(outputDirectory, name) : outputDirectory;
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				System.out.println("No Dir");
				MessageDialog.openError(getShell(), "Export error", "Unable to create target directory");
				// return false;
			}
		}

		String baseURL = directory.toURI().toString() + "/";
		if (isExportScenario) {
			ExportCSVBundleUtil.exportScenario(scenarioDataProvider, delimiter, decimalSeparator, baseURL);
		} else {
			ExportCSVBundleUtil.exportAllCurves(scenarioDataProvider, delimiter, decimalSeparator, baseURL);
		}
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(exportPage);
	}
}
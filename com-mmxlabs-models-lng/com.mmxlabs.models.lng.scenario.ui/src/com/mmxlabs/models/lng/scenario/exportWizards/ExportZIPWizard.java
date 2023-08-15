/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.exportWizards;

import java.io.File;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

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

public class ExportZIPWizard extends Wizard implements IExportWizard {

	private ExportZIPWizardPage exportPage;
	
	private boolean isExportScenario = true;
	
	public ExportZIPWizard() {
		this(true);
	}
	
	public ExportZIPWizard(boolean isExportScenario) {
		this.isExportScenario = isExportScenario;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle(isExportScenario ? "ZIP Export Wizard" : "All Curves Export Wizard");
		exportPage = new ExportZIPWizardPage(selection);
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
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);
			try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ExportZIPWizard")) {
				final String name = instance.getName();
				ExportCSVBundleUtil.exportScenarioToZip(scenarioDataProvider, new File(info.outputDirectory, name+".zip"));
			}
		}
		exportPage.saveDirectorySetting();

		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(exportPage);
	}
}
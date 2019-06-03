/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.exportWizards;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultExportContext;
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

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {

		setWindowTitle("CSV Export Wizard");
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

		// Collection<ScenarioInstance> instances = info.instances;
		final char delimiter = info.delimiter;
		final char decimalSeparator = info.decimalSeparator;

		final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		final IMMXExportContext context = new DefaultExportContext(scenarioModel, scenarioDataProvider, decimalSeparator);
		final File directory = createExportDirectories ? new File(outputDirectory, name) : outputDirectory;
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				System.out.println("No Dir");
				MessageDialog.openError(getShell(), "Export error", "Unable to create target directory");
				// return false;
			}
		}

		// generate export files
		for (final UUIDObject modelInstance : getSubModels(scenarioDataProvider)) {
			final ISubmodelImporter importer = Activator.getDefault().getImporterRegistry().getSubmodelImporter(modelInstance.eClass());
			if (importer != null) {
				final Map<String, Collection<Map<String, String>>> outputs = new HashMap<String, Collection<Map<String, String>>>();

				importer.exportModel(modelInstance, outputs, context);

				for (final String key : outputs.keySet()) {
					final Collection<Map<String, String>> rows = outputs.get(key);
					final String friendlyName = importer.getRequiredInputs().get(key);
					final File outputFile = new File(directory, friendlyName + ".csv");

					// export CSV for this file
					writeCSV(rows, outputFile, delimiter);
				}
			}
		}

		final Collection<IExtraModelImporter> extra = Activator.getDefault().getImporterRegistry().getExtraModelImporters();
		for (final IExtraModelImporter importer : extra) {
			final Map<String, Collection<Map<String, String>>> outputs = new HashMap<String, Collection<Map<String, String>>>();
			importer.exportModel(outputs, context);
			for (final String key : outputs.keySet()) {
				final Collection<Map<String, String>> rows = outputs.get(key);
				if (rows != null) {
					final String friendlyName = importer.getRequiredInputs().get(key);
					final File outputFile = new File(directory, friendlyName + ".csv");

					// export CSV for this file
					writeCSV(rows, outputFile, delimiter);
				}
			}
		}
	}

	private List<UUIDObject> getSubModels(final IScenarioDataProvider scenarioDataProvider) {
		final List<UUIDObject> subModels = new ArrayList<UUIDObject>();

		subModels.add(ScenarioModelUtil.getPortModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getFleetModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getPricingModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getCostModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getCommercialModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		subModels.add(ScenarioModelUtil.getCargoModel(scenarioDataProvider));
		// subModels.add(scenarioModel.getActualsModel());
		subModels.add(ScenarioModelUtil.getScheduleModel(scenarioDataProvider));

		subModels.add(ScenarioModelUtil.findNominationsModel(scenarioDataProvider));
		
		// Remove any null references
		while (subModels.remove(null))
			;
		return subModels;
	}

	/**
	 * @param rows
	 * @param outputFile
	 */
	private void writeCSV(final Collection<Map<String, String>> rows, final File outputFile, final char delimiter) {
		if (rows.isEmpty())
			return;
		final LinkedHashSet<String> keys = new LinkedHashSet<String>();
		for (final Map<String, String> row : rows) {
			keys.addAll(row.keySet());
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			boolean firstKey = true;
			for (final String key : keys) {
				if (!firstKey)
					writer.write(delimiter);
				firstKey = false;
				writer.write(escape(key, delimiter));
			}
			for (final Map<String, String> row : rows) {
				writer.write("\n");
				firstKey = true;
				for (final String key : keys) {
					if (!firstKey)
						writer.write(delimiter);
					firstKey = false;
					writer.write(escape(row.get(key), delimiter));
				}
			}
		} catch (final IOException e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	private String escape(final String key, final char delimiter) {
		if (key == null)
			return "";
		String sub = key.trim().replace("\"", "\\\"");
		sub = sub.contains("" + delimiter) ? "\"" + sub + "\"" : sub;
		// Ensure string is quoted if there is a new line present.
		if (sub.contains("\n")) {
			sub = "\"" + sub + "\"";
		}
		return sub;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(exportPage);
	}
}
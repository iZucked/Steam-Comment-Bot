/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.exportWizards;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

/**
 * Export the selected scenario to the filesystem somehow.
 * 
 * @author hinton
 *
 */
public class ExportCSVWizard extends Wizard implements IExportWizard {
	private ExportCSVWizardPage exportPage;

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("CSV Export Wizard");
		exportPage = new ExportCSVWizardPage();
	}

	@Override
	public boolean performFinish() {
		final File outputDirectory = exportPage.getOutputDirectory();
		final List<IResource> l = exportPage.getSelectedResources();
		
		boolean createExportDirectories = l.size() > 1;
		
		for (final IResource resource : l) {
			final MMXRootObject rootObject = (MMXRootObject) resource.getAdapter(MMXRootObject.class);
			
			if (rootObject != null) {
				final File directory = createExportDirectories ? new File(outputDirectory, resource.getName()) : outputDirectory;
				if (!directory.exists()) {
					directory.mkdirs();
				}
				
				// generate export files
				for (final MMXSubModel subModel : rootObject.getSubModels()) {
					final UUIDObject modelInstance = subModel.getSubModelInstance();
					final ISubmodelImporter importer = Activator.getDefault().getImporterRegistry().getSubmodelImporter(modelInstance.eClass());
					if (importer != null) {
						final Map<String, Collection<Map<String, String>>> outputs = new HashMap<String, Collection<Map<String,String>>>();
						importer.exportModel(modelInstance, outputs);
						
						for (final String key : outputs.keySet()) {
							final Collection<Map<String, String>> rows = outputs.get(key);
							final String friendlyName = importer.getRequiredInputs().get(key);
							final File outputFile = new File(directory, friendlyName + ".csv");
							
							// export CSV for this file
							
							writeCSV(rows, outputFile);
						}
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @param rows
	 * @param outputFile
	 */
	private void writeCSV(Collection<Map<String, String>> rows, File outputFile) {
		if (rows.isEmpty()) return;
		final LinkedHashSet<String> keys = new LinkedHashSet<String>();
		for (final Map<String, String> row : rows) {
			keys.addAll(row.keySet());
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			boolean firstKey = true;
			for (final String key : keys) {
				if (!firstKey) writer.write(",");
				firstKey = false;
				writer.write(escape(key));
			}
			for (final Map<String, String> row : rows) {
				writer.write("\n");
				firstKey = true;
				for (final String key : keys) {
					if (!firstKey) writer.write(",");
					firstKey = false;
					writer.write(escape(row.get(key)));
				}
			}
		} catch (IOException e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private String escape(final String key) {
		if (key == null) return "";
		final String sub = key.trim().replace("\"", "\\\"");
		return sub.contains(",") ? "\""+sub+"\"" : sub;
	}

	@Override
	public void addPages() {
        super.addPages(); 
        addPage(exportPage);
	}
}
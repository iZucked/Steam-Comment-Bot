/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.Pair;

/**
 * Imports EObjects from a CSV file.
 * 
 * @author Tom Hinton
 * 
 */
public abstract class ImportCSVAction extends Action {
	public ImportCSVAction() {
		super("Import from CSV", AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
	}

	protected abstract EObject getToplevelObject();

	public abstract void addObjects(final Collection<EObject> newObjects);

	protected abstract EClass getImportClass();
	
	@Override
	public void run() {
		final FileDialog openDialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.OPEN);

		openDialog.setFilterExtensions(new String[] { "*.csv" });
		openDialog.setText("Choose a CSV file from which to import");
		final String inputFileName = openDialog.open();
		if (inputFileName == null)
			return;

		final List<DeferredReference> deferments = new ArrayList<DeferredReference>();
		final NamedObjectRegistry registry = new NamedObjectRegistry();

		registry.addEObjects(getToplevelObject());

		final EObjectImporter importer = EObjectImporterFactory.getInstance()
				.getImporter(getImportClass());

		try {
			final CSVReader reader = new CSVReader(inputFileName);
			final Collection<EObject> importedObjects = importer.importObjects(
					reader, deferments, registry);
			// Tell implementation to add these objects
			addObjects(importedObjects);
			// update references
			registry.addEObjects(getToplevelObject());
			// link up imported references

			final Map<Pair<EClass, String>, EObject> m = registry.getContents();
			for (final DeferredReference dr : deferments) {
				dr.setRegistry(m);
				dr.run();
			}

			// finally do any postprocessing
			// fixes dates and times, derives missing fields etc.
			for (final EObject object : importedObjects) {
				Postprocessor.getInstance().postprocess(object);
			}
		} catch (IOException e) {
		}

	}
}

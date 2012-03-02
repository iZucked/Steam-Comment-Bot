/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scenario.fleet.FleetPackage;
import scenario.port.PortPackage;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.platform.importer.ui.ImportWarningDialog;

/**
 * Imports EObjects from a CSV file.
 * 
 * @author Tom Hinton
 * 
 */
public abstract class ImportCSVAction extends Action {

	private static final Logger log = LoggerFactory.getLogger(ImportCSVAction.class);

	public ImportCSVAction() {
		super("Import from CSV", AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
	}

	protected abstract EObject getToplevelObject();

	public abstract void addObjects(final Collection<EObject> newObjects);

	protected abstract EClass getImportClass();

	@Override
	public void run() {
		final FileDialog openDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);

		openDialog.setFilterExtensions(new String[] { "*.csv" });
		openDialog.setText("Choose a CSV file from which to import " + getNiceName(getImportClass()));

		final String inputFileName = openDialog.open();
		if (inputFileName == null) {
			return;
		}

		final WarningCollector warningCollector = new WarningCollector();

		final WorkspaceJob job = new WorkspaceJob("Import CSV from " + inputFileName) {

			@Override
			public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {

				CSVReader reader = null;
				try {
					monitor.beginTask("Import CSV Data", 4);

					final List<DeferredReference> deferments = new ArrayList<DeferredReference>();
					monitor.subTask("Prepare names");
					final NamedObjectRegistry registry = new NamedObjectRegistry();
					monitor.worked(1);
					registry.addEObjects(getToplevelObject());
					monitor.subTask("Read CSV");
					final EObjectImporter importer = EObjectImporterFactory.getInstance().getImporter(getImportClass());
					importer.addImportWarningListener(warningCollector);
					reader = new CSVReader(inputFileName);
					final Collection<EObject> importedObjects = importer.importObjects(reader, deferments, registry);
					monitor.worked(1);
					// Tell implementation to add these objects
					monitor.subTask("Merge new data");
					for (final EObject object : importedObjects) {
						registry.addEObjects(object);
					}
					monitor.worked(1);
					// update references
					registry.addEObjects(getToplevelObject());
					// link up imported references

					final Map<Pair<EClass, String>, EObject> m = registry.getContents();
					for (final DeferredReference dr : deferments) {
						dr.setRegistry(m);
						dr.run();
					}

					monitor.worked(1);
					// finally do any postprocessing
					// fixes dates and times, derives missing fields etc.
					for (final EObject object : importedObjects) {
						Postprocessor.getInstance().postprocess(object);
					}

					addObjects(importedObjects);

					ImportUI.endImport();
					return Status.OK_STATUS;
				} catch (final IOException e) {
					ImportUI.endImport();
					return Status.CANCEL_STATUS;
				} finally {

					monitor.done();

					if (reader != null) {
						try {
							reader.close();
						} catch (final IOException e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			}
		};

		// Trigger UI for progress monitor
		job.setUser(true);
		// Schedule job for launching
		job.schedule();
		try {
			job.join();
			if (warningCollector.getWarnings().isEmpty() == false) {
				final ImportWarningDialog iwd = new ImportWarningDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				iwd.setWarnings(warningCollector.getWarnings());
				iwd.open();
			}
		} catch (final InterruptedException e) {

		}
	}

	private final Map<EClass, String> niceNames = CollectionsUtil.makeHashMap(PortPackage.eINSTANCE.getDistanceModel(), "Distance Matrix", FleetPackage.eINSTANCE.getVesselEvent(), "Vessel Events",
			FleetPackage.eINSTANCE.getVesselClass(), "Vessel Classes");

	/**
	 * @param importClass
	 * @return
	 */
	private String getNiceName(final EClass importClass) {
		if (niceNames.containsKey(importClass)) {
			return niceNames.get(importClass);
		} else {
			return importClass.getName() + "s";
		}
	}
}

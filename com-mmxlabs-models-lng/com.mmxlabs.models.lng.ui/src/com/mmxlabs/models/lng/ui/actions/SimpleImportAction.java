/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.FileDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

/**
 * @author hinton
 * 
 */
public class SimpleImportAction extends ImportAction {

	private static final Logger log = LoggerFactory.getLogger(SimpleImportAction.class);

	private final ScenarioTableViewer viewer;

	public SimpleImportAction(final IScenarioEditingLocation part, final ScenarioTableViewer viewer) {
		super(part);
		this.viewer = viewer;
	}

	@Override
	protected void doImportStages(final DefaultImportContext context) {
		final EObject container = viewer.getCurrentContainer();
		final EReference containment = viewer.getCurrentContainment();

		final IClassImporter importer = getImporter(containment);
		// open file picker

		final FileDialog fileDialog = new FileDialog(part.getShell());
		fileDialog.setFilterExtensions(new String[] { "*.csv" });
		final String path = fileDialog.open();

		if (path == null)
			return;

		CSVReader reader;
		try {
			reader = new CSVReader(new File(path));
			final Collection<EObject> importedObjects = importer.importObjects(containment.getEReferenceType(), reader, context);
			context.run();
			final Command cmd = mergeImports(container, containment, importedObjects);
			part.getEditingDomain().getCommandStack().execute(cmd);
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	protected IClassImporter getImporter(final EReference containment) {
		return Activator.getDefault().getImporterRegistry().getClassImporter(containment.getEReferenceType());
	}

	protected Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {
		return mergeLists(container, containment, new ArrayList<EObject>(imports));
	}
}

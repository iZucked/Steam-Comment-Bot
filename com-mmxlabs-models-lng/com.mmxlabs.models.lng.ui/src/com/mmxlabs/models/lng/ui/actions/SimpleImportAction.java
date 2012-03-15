/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.FileDialog;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

/**
 * @author hinton
 *
 */
public class SimpleImportAction extends ImportAction {
	private ScenarioTableViewer viewer;

	public SimpleImportAction(final JointModelEditorPart part, final ScenarioTableViewer viewer) {
		super(part);
		this.viewer = viewer;
	}

	@Override
	protected void doImportStages(final DefaultImportContext context) {
		final EObject container = viewer.getCurrentContainer();
		final EReference containment = viewer.getCurrentContainment();
		
		final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(containment.getEReferenceType());
		// open file picker

		final FileDialog fileDialog = new FileDialog(part.getSite().getShell());
		fileDialog.setFilterExtensions(new String[] {"*.csv"});
		final String path = fileDialog.open();
		
		if (path == null) return;
		
		CSVReader reader;
		try {
			reader = new CSVReader(path);
			final Collection<EObject> importedObjects = importer.importObjects(containment.getEReferenceType(), reader, context);
			context.run();
			part.getEditingDomain().getCommandStack().execute(mergeImports(container, containment, importedObjects));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	protected Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {
		return mergeLists(container, containment, new ArrayList<EObject>(imports));
	}
}

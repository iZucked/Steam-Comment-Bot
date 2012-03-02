/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

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

/**
 * Reflective export action, counterpart to {@link ImportCSVAction}
 * 
 * TODO support customized formats for contained objects (e.g. price curve export) this may not be possible because not all states are exportable to the required form (e.g. price curves with different
 * dates)
 * 
 * @author Tom Hinton
 * 
 */
public abstract class ExportCSVAction extends Action {
	public ExportCSVAction() {
		super("Export to CSV", AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/export_wiz.gif"));
	}

	@Override
	public void run() {
		final List<EObject> objects = getObjectsToExport();
		// check for variadicity
		// TODO only top level variation is supported
		// TODO if two sub-types have separate fields with the same name
		// there will be a problem
		if (objects.size() == 0) {
			return;
		}

		final Map<String, Collection<Map<String, String>>> result = ImporterUtil.getInstance().exportEObjects(getExportEClass(), getObjectsToExport());

		for (final Map.Entry<String, Collection<Map<String, String>>> entry : result.entrySet()) {
			final String group = entry.getKey();

			final FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
			fileDialog.setText("Choose export location for " + group + "s");
			fileDialog.setFileName(group + ".csv"); // TODO set a more sensible
													// name
			fileDialog.setOverwrite(true);
			fileDialog.setFilterExtensions(new String[] { "*.csv" });
			final String saveFilePath = fileDialog.open();
			if (saveFilePath == null) {
				return;
			}
			ImporterUtil.getInstance().writeObjects(saveFilePath, entry.getValue());
		}
	}

	public abstract List<EObject> getObjectsToExport();

	public abstract EClass getExportEClass();
}

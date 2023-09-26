/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.wizards.ScenarioServiceNewScenarioPage;

public class ImportZIPFilesPage extends ImportCSVFilesPage {

	@Override
	protected String SECTION_NAME() {
		return "ImportZIPFilesPage.section";
	}

	protected ImportZIPFilesPage(final String pageName, final ScenarioServiceNewScenarioPage mainPage) {
		super(pageName, mainPage);
		setTitle("Choose ZIP Files");
	}
	
	@Override
	protected void createSelectionDialog(Composite holder, String lastDirectoryName) {
		// Create selection dialogs
		final Button auto = new Button(holder, SWT.NONE);
		final Label directory = new Label(holder, SWT.NONE);
		auto.setText("Choose &Zip File...");
		directory.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		if (lastDirectoryName != null) {
			directory.setText(lastDirectoryName);
		}
		auto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				
				final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
				IDialogSettings section = dialogSettings.getSection(SECTION_NAME());
				final String filter = section.get(FILTER_DIRECTORY_KEY);
				// display file open dialog and then fill out files if the exist.
				final FileDialog dlg = new FileDialog(getShell());
				dlg.setFilterExtensions(new String[] { "*.zip",});
				if (filter != null) {
					dlg.setFilterPath(filter);
				}

				final String fn = dlg.open();
				
				if (fn != null) {
					File zipFile = new File(fn);
					if (zipFile.exists()) {
						join(subModelChunks, extraModelChunks).forEach(c -> c.setFromZip(fn));
					}
					directory.setText(fn);

					// Trigger 'Next' button focus
					setPageComplete(true);
					if (section == null) {
						section = dialogSettings.addNewSection(SECTION_NAME());
					}
					section.put(FILTER_DIRECTORY_KEY, fn);
				}
			}
		});
	}
}

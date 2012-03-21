/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.exportWizards;

import java.io.File;
import java.util.List;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;

/**
 * @author hinton
 *
 */
public class ExportCSVWizardPage extends WizardExportResourcesPage {
	private DirectoryFieldEditor editor;

	protected ExportCSVWizardPage() {
		super("Export Scenario as CSV", null);
	}

	@Override
	public void handleEvent(Event event) {
		
	}

	@Override
	protected void createDestinationGroup(Composite parent) {
		final Group destination = new Group(parent, SWT.NONE);
		destination.setText("Destination Directory");
		GridLayout layout = new GridLayout();
		destination.setLayout(layout);
		destination.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL));
		destination.setFont(parent.getFont());
		
		final DirectoryFieldEditor editor = new DirectoryFieldEditor("destination-directory", "Export to directory:", destination);
		
		this.editor = editor;
		editor.getTextControl(destination).addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				ExportCSVWizardPage.this.updatePageCompletion();
			}
		});
	}

	@Override
	protected void createOptionsGroup(Composite parent) {
		// TODO Auto-generated method stub
//		super.createOptionsGroup(parent);
	}

	@Override
	public List getSelectedResources() {
		return super.getSelectedResources();
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete() && new File(editor.getStringValue()).exists();
	}

	/**
	 * @return
	 */
	public File getOutputDirectory() {
		return new File(editor.getStringValue());
	}
}

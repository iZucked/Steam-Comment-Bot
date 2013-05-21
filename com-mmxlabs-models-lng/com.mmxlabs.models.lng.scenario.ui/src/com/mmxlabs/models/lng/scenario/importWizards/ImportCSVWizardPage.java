/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;


public class ImportCSVWizardPage extends WizardNewFileCreationPage {
	protected ImportWarningsPage previousPage;
	protected FileFieldEditor editor;

	public ImportCSVWizardPage(String pageName, IStructuredSelection selection, ImportWarningsPage warnings) {
		super(pageName, selection);
		setTitle(pageName); //NON-NLS-1
		setFileExtension("scn");
		setDescription("Import a file from the local file system into the workspace"); //NON-NLS-1
		this.previousPage = warnings;
	}
	
	 /* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createLinkTarget()
	 */
	@Override
	protected void createLinkTarget() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getNewFileLabel()
	 */
	@Override
	protected String getNewFileLabel() {
		return "Scenario Name:"; //NON-NLS-1
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validateLinkedResource()
	 */
	@Override
	protected IStatus validateLinkedResource() {
		return new Status(IStatus.OK, "com.mmxlabs.shiplingo.platform.models.manifest", IStatus.OK, "", null); //NON-NLS-1 //NON-NLS-2
	}
}

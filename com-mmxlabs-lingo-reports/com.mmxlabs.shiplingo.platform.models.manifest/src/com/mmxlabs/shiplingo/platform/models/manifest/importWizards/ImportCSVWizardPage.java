/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.importWizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;


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

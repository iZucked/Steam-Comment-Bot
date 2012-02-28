/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mmxlabs.models.lng.demo.importWizards;

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

import com.mmxlabs.models.lng.demo.DemoJointModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;


public class ImportCSVWizardPage extends WizardNewFileCreationPage {
	protected ImportCSVFilesPage previousPage;
	protected FileFieldEditor editor;

	public ImportCSVWizardPage(String pageName, IStructuredSelection selection, ImportCSVFilesPage previousPage) {
		super(pageName, selection);
		setTitle(pageName); //NON-NLS-1
		setDescription("Import a file from the local file system into the workspace"); //NON-NLS-1
		this.previousPage = previousPage;
	}
	
	 /* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createLinkTarget()
	 */
	@Override
	protected void createLinkTarget() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
	 */
	@Override
	protected InputStream getInitialContents() {
		try {
			final MMXRootObject root = previousPage.doImport();
			final File tempFile = File.createTempFile(UUID.randomUUID().toString(), "scn");
			tempFile.deleteOnExit();
			final DemoJointModel djm = new DemoJointModel(root, URI.createFileURI(tempFile.getCanonicalPath()));
			djm.save();
			return new FileInputStream(tempFile);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getNewFileLabel()
	 */
	@Override
	protected String getNewFileLabel() {
		return "New File Name:"; //NON-NLS-1
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validateLinkedResource()
	 */
	@Override
	protected IStatus validateLinkedResource() {
		return new Status(IStatus.OK, "com.mmxlabs.models.lng.demo", IStatus.OK, "", null); //NON-NLS-1 //NON-NLS-2
	}
}

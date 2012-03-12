/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.importWizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ImportCSVWizard extends Wizard implements IImportWizard {
	
	ImportCSVWizardPage mainPage;
	private ImportCSVFilesPage filesPage;
	private ImportWarningsPage warnings;

	public ImportCSVWizard() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		IFile file = mainPage.createNewFile();
        if (file == null)
            return false;
        return true;
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("File Import Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
		filesPage = new ImportCSVFilesPage("CSV Files");
		warnings = new ImportWarningsPage("Warnings", filesPage);
		mainPage = new ImportCSVWizardPage("Import CSV",selection, warnings); //NON-NLS-1
	}
	
	/* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    @Override
	public void addPages() {
        super.addPages(); 
        addPage(filesPage);
        addPage(warnings);
        addPage(mainPage);
    }
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.editor.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;
import org.eclipse.ui.dialogs.WizardResourceImportPage;

/**
 * A wizard which strips out parts of a scenario into a new one
 * @author Tom Hinton
 *
 */
public class ScenarioExportWizard extends Wizard implements INewWizard {
	WizardExportResourcesPage exportPage;
	WizardResourceImportPage importPage;
	WizardPage optionsPage;	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		exportPage = new WizardExportResourcesPage("Choose base scenario", selection) {
			@Override
			public void handleEvent(final Event event) {
				
			}
			@Override
			protected void createDestinationGroup(final Composite parent) {
				
			}
			@Override
			protected void createOptionsGroupButtons(final Group group) {
				group.setText("Elements to include in derived scenario");
				((GridLayout) group.getLayout()).numColumns = 4;
				new Button(group, SWT.CHECK).setText("Ports");
				new Button(group, SWT.CHECK).setText("Distance matrix");
				
				new Button(group, SWT.CHECK).setText("Vessel classes");
				new Button(group, SWT.CHECK).setText("Vessels");
				
				new Button(group, SWT.CHECK).setText("Entities");
				new Button(group, SWT.CHECK).setText("Markets");
				
				new Button(group, SWT.CHECK).setText("Contracts");
				new Button(group, SWT.CHECK).setText("Cargoes");
			}
		};
		
		addPage(exportPage);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}
}

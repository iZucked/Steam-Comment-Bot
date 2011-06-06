/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.editor.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;

/**
 * @author Tom Hinton
 *
 */
public class CSVExportWizard extends Wizard {

	public CSVExportWizard() {
		setWindowTitle("Export Scenario to CSV Files");
	}

	@Override
	public void addPages() {
		
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}

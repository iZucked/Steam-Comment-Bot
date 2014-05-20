/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 */
public class BulkImportWizard extends Wizard implements IImportWizard {

	private BulkImportPage bip;
	private List<ScenarioInstance> selectedScenarios;
	private String importFilename;
	private char csvSeparator;
	private char decimalSeparator;
	final private ScenarioInstance currentScenario;
	private int importedField;

	public BulkImportWizard(ScenarioInstance scenarioInstance, int fieldToImport) {
		currentScenario = scenarioInstance;
		importedField = fieldToImport;
		setWindowTitle("Bulk data import");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		bip = new BulkImportPage("selectScenarios", getImportedField(), currentScenario);
		this.setForcePreviousAndNextButtons(false);
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(bip);
	}

	@Override
	public boolean performFinish() {
		selectedScenarios = bip.getSelectedScenarios();
		importFilename = bip.getImportFilename();
		csvSeparator = bip.getCsvSeparator();
		decimalSeparator = bip.getDecimalSeparator();

		bip.saveDirectorySetting();

		return true;
	}

	public List<ScenarioInstance> getSelectedScenarios() {
		return selectedScenarios;
	}

	public String getImportFilename() {
		return importFilename;
	}

	public char getCsvSeparator() {
		return csvSeparator;
	}

	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	public int getImportedField() {
		return importedField;
	}

	@Override
	public boolean canFinish() {
		File file = new File(bip.getImportFilename());
		return file.isFile() && file.canRead();
	}

}

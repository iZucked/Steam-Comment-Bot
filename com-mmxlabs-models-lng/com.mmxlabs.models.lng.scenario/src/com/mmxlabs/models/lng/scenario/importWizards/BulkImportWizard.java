package com.mmxlabs.models.lng.scenario.importWizards;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.ImportedField;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * @since 3.0
 */
public class BulkImportWizard extends Wizard implements IImportWizard {

	private BulkImportPage bip;
	private List<ScenarioInstance> selectedScenarios;
	private String importFilename;
	private char csvSeparator;
	private ImportedField importedField;
	final private ScenarioInstance currentScenario;
	
	
	public BulkImportWizard(ScenarioInstance scenarioInstance) {
		currentScenario = scenarioInstance;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		bip = new BulkImportPage("selectScenarios", currentScenario);
		this.setForcePreviousAndNextButtons(false);
	}

	public void addPages() {
		super.addPages();
		addPage(bip);
	}
	
	
	@Override
	public boolean performFinish() {
		selectedScenarios = bip.getSelectedScenarios();
		importFilename = bip.getImportFilename();
		csvSeparator = bip.getCsvSeparator();
		importedField = bip.getImportedField();
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
	
	public ImportedField getImportedField() {
		return importedField;
	}
	
}

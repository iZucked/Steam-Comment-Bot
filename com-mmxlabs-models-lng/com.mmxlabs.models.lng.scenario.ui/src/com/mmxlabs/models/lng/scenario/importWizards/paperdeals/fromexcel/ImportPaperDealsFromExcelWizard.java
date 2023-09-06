package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.PaperDealsImportAction;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.ImportAction.ImportHooksProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ImportPaperDealsFromExcelWizard extends AbstractImportWizard {
	
	private ImportPaperDealsFromExcelPage importPage;

	public ImportPaperDealsFromExcelWizard(ScenarioInstance scenarioInstance, String windowTitle) {
		super(scenarioInstance, windowTitle);
	}

	@Override
	protected AbstractImportPage getImportPage(String pageName, ScenarioInstance currentScenario) {
		importPage = new ImportPaperDealsFromExcelPage(pageName, currentScenario);
		return importPage;
	}

	@Override
	protected ImportAction getImportAction(ImportHooksProvider ihp, IScenarioDataProvider scenarioDataProvider) {
		return new PaperDealsImportAction(ihp, ScenarioModelUtil.getCargoModel(scenarioDataProvider));
	}

	@Override
	public boolean performFinish() {
		System.out.println("Filename: " + importPage.getImportFilename());
		System.out.println("Worksheet: " + importPage.getSelectedWorksheetName());
		
		FileInputStream fileIn = null;
		ExcelReader excelReader = null;
		
		try {
			fileIn = new FileInputStream(importPage.getImportFilename());
		} catch (FileNotFoundException e1) {
			// TODO Handle exceptions properly
			return false;
		}
		
		try {
			excelReader = new ExcelReader(fileIn, importPage.getSelectedWorksheetName());
		} catch (IOException e) {
			// TODO Handle exceptions properly
			return false;
		}
		
		// Create paper deals
		

		return true;
	}
}

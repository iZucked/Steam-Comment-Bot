/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelReader;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ImportPaperDealsFromExcelPage extends AbstractImportPage {

	private Combo dropDownMenu;
	private Composite dropDownComposite;
	
	public ImportPaperDealsFromExcelPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName, currentScenario);
		setTitle("Select Excel file and worksheet");
		setDescription("Choose Excel file and respective worksheet from Excel file to import paper deals.");
	}
	
	@Override
	public String getItemDescription() {
		return "paper deals from excel";
	}
	
	public String getSelectedWorksheetName() {
		if(dropDownMenu != null)
			return dropDownMenu.getText();
		return "";
	}
	
	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		final GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		final GridData ld = new GridData();
		ld.verticalIndent = 0;
		ld.horizontalAlignment = SWT.FILL;
		ld.grabExcessHorizontalSpace = true;
		container.setLayoutData(ld);

		final Composite datafileC = new Composite(container, SWT.NONE);
		final GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		datafileC.setLayoutData(gd);

		importFileEditor = new FileFieldEditor("Workbook file", "Workbook file", datafileC);
		importFileEditor.setFileExtensions(new String[] { "*.xlsx", "*.xlsm", "*.xlsb" });
		importFileEditor.getTextControl(datafileC).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				// Populate drop down menu
				List<String> sheetNames = new ArrayList<>();
				
				// TODO: Handle exceptions correctly
				try {
					sheetNames = ExcelReader.getSheetNames(getImportFilename());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				dropDownMenu.setItems(sheetNames.stream().toArray(String[]::new));
				
				// Show worksheet drop down menu
				dropDownComposite.setVisible(true);
			}
		});
		
		datafileC.setVisible(!guided);

		// create a control to display the drop down menu
        dropDownComposite = new Composite(container, SWT.NONE);
        dropDownComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        dropDownComposite.setLayout(new GridLayout(2, false));

        Label label = new Label(dropDownComposite, SWT.LEFT);
        label.setText("Select worksheet: ");

        // Create the drop down menu for worksheets
        dropDownMenu = new Combo(dropDownComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        dropDownMenu.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        dropDownMenu.addModifyListener(new ModifyListener() {
        	@Override
			public void modifyText(final ModifyEvent e) {
        		// Let the finish button show
        		getContainer().updateButtons();
        	}
        });
        
        dropDownComposite.setVisible(false);

		setControl(container);
		control = container;
	}
}

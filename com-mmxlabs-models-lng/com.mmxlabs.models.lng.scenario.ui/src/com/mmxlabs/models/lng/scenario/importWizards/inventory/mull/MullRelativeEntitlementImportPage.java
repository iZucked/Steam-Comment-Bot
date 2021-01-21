package com.mmxlabs.models.lng.scenario.importWizards.inventory.mull;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.scenario.importWizards.inventory.InventoryImportPage.RadioSelectionGroup;
import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class MullRelativeEntitlementImportPage extends WizardPage {
	private static final String LAST_FILE = "lastFile";
	private static final String DELIMITER_KEY = "lastDelimiter";
	private static final String DECIMAL_SEPARATOR_KEY = "lastDecimalSeparator";
	private static final String SECTION_NAME = "MullRelativeEntitlementImportPage.section";
	
	public static final int CHOICE_COMMA = 0;
	public static final int CHOICE_SEMICOLON = CHOICE_COMMA + 1;
	public static final int CHOICE_PERIOD = CHOICE_COMMA + 1;
	
	protected Control control = null;
	
	protected RadioSelectionGroup csvSelectionGroup;
	protected RadioSelectionGroup decimalSelectionGroup;
	
	protected final ScenarioInstance currentScenario;
	protected FileFieldEditor importFileEditor;
	
	public MullRelativeEntitlementImportPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName);
		this.currentScenario = currentScenario;
		setTitle("Select data and scenarios");
		setDescription("Choose scenario(s) and a file for bulk data import.");
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		final IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		
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
		
		importFileEditor = new FileFieldEditor("Data file", "Data file", datafileC);
		importFileEditor.setFileExtensions(new String[] {"*.csv"});
		importFileEditor.getTextControl(datafileC).addModifyListener(event -> MullRelativeEntitlementImportPage.this.getContainer().updateButtons());
		if (section != null && section.get(LAST_FILE) != null) {
			importFileEditor.setStringValue(section.get(LAST_FILE));
		}
		
		csvSelectionGroup = new RadioSelectionGroup(container, "Format separator", SWT.NONE, new String[] {"comma (\",\")", "semicolon (\";\")" }, new int[] { CHOICE_COMMA, CHOICE_SEMICOLON });
		final GridData csvLayoutData = new GridData();
		csvLayoutData.grabExcessHorizontalSpace = true;
		csvLayoutData.horizontalAlignment = SWT.FILL;
		csvSelectionGroup.setLayoutData(csvLayoutData);
		
		decimalSelectionGroup = new RadioSelectionGroup(container, "Decimal separator", SWT.NONE, new String[] { "comma (\",\")", "period (\".\")" }, new int[] { CHOICE_COMMA, CHOICE_PERIOD });
		final GridData decimalLayoutData = new GridData();
		decimalLayoutData.grabExcessHorizontalSpace = true;
		decimalLayoutData.horizontalAlignment = SWT.FILL;
		decimalSelectionGroup.setLayoutData(decimalLayoutData);
		
		int delimiterValue = CHOICE_COMMA;
		if (section != null && section.get(DELIMITER_KEY) != null) {
			delimiterValue = section.getInt(DELIMITER_KEY);
		}
		int decimalValue = CHOICE_PERIOD;
		if (section != null && section.get(DECIMAL_SEPARATOR_KEY) != null) {
			decimalValue = section.getInt(DECIMAL_SEPARATOR_KEY);
		}
		
		csvSelectionGroup.setSelectedIndex(delimiterValue);
		decimalSelectionGroup.setSelectedIndex(decimalValue);
		
		setControl(container);
		control = container;
	}
	
	@Override
	public Control getControl() {
		return control;
	}
	
	public void saveDirectorySetting() {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		if (section == null) {
			section = dialogSettings.addNewSection(SECTION_NAME);
		}
		section.put(DELIMITER_KEY, csvSelectionGroup.getSelectedValue());
		section.put(DECIMAL_SEPARATOR_KEY, decimalSelectionGroup.getSelectedValue());
		section.put(LAST_FILE, importFileEditor.getStringValue());
	}
	
	public String getImportFilename() {
		return importFileEditor.getStringValue();
	}
	
	public char getCsvSeparator() {
		return csvSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : ';';
	}
	
	public char getDecimalSeparator() {
		return decimalSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : '.';
	}
}

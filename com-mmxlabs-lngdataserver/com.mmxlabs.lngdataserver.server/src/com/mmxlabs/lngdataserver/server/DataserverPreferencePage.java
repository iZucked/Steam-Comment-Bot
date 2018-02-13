package com.mmxlabs.lngdataserver.server;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DataserverPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage  {

	public DataserverPreferencePage() {
		super(GRID);
		setDescription("Data server - see sub pages");
	}

	@Override
	protected void createFieldEditors() {
		
	}

	@Override
	public void init(IWorkbench workbench) {
		
	}

}

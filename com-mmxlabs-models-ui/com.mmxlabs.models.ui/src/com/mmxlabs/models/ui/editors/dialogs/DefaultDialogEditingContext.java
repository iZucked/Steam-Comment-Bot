package com.mmxlabs.models.ui.editors.dialogs;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class DefaultDialogEditingContext implements IDialogEditingContext {
	private IDialogController dialogController;
	private IScenarioEditingLocation scenarioEditingLocation;
	
	public DefaultDialogEditingContext(IDialogController dc, IScenarioEditingLocation sel) {
		dialogController = dc;
		scenarioEditingLocation = sel;
	}
	
	@Override
	public IDialogController getDialogController() {
		return dialogController;
	}

	@Override
	public IScenarioEditingLocation getScenarioEditingLocation() {
		return scenarioEditingLocation;
	}

}

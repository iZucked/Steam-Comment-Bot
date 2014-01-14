package com.mmxlabs.models.ui.editors.dialogs;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class DefaultDialogEditingContext implements IDialogEditingContext {
	private final IDialogController dialogController;
	private final IScenarioEditingLocation scenarioEditingLocation;

	public DefaultDialogEditingContext(final IDialogController dc, final IScenarioEditingLocation sel) {
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

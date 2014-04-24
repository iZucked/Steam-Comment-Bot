package com.mmxlabs.models.ui.editors.dialogs;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public interface IDialogEditingContext {
	IDialogController getDialogController();

	IScenarioEditingLocation getScenarioEditingLocation();

	boolean isMultiEdit();
}

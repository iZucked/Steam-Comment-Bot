/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class DefaultDialogEditingContext implements IDialogEditingContext {
	private final IDialogController dialogController;
	private final IScenarioEditingLocation scenarioEditingLocation;
	private final boolean multiEdit;

	public DefaultDialogEditingContext(@NonNull final IDialogController dc, @NonNull final IScenarioEditingLocation sel, final boolean multiEdit) {
		dialogController = dc;
		scenarioEditingLocation = sel;
		this.multiEdit = multiEdit;
	}

	@Override
	public IDialogController getDialogController() {
		return dialogController;
	}

	@Override
	public IScenarioEditingLocation getScenarioEditingLocation() {
		return scenarioEditingLocation;
	}

	@Override
	public boolean isMultiEdit() {
		return multiEdit;
	}

}

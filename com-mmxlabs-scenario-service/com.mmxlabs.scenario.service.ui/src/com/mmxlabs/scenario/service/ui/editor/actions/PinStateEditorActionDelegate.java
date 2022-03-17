/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editor.actions;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.scenario.service.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class PinStateEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2 {
	private IScenarioServiceSelectionProvider selectionProvider;
	private IEditorPart targetEditor;
	private IAction action;
	@NonNull
	private final IScenarioServiceSelectionChangedListener selectionChangedListener = (a, b) -> updateActionState();

	@Override
	public void run(final IAction action) {

	}

	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {

	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.action = action;
		this.targetEditor = targetEditor;
		updateActionState();
	}

	private void updateActionState() {
		boolean enabled = false;
		boolean toggled = false;
		if (targetEditor != null && selectionProvider != null) {
			final IEditorInput input = targetEditor.getEditorInput();
			if (input instanceof IScenarioServiceEditorInput) {
				enabled = true;
				ScenarioResult pinned = selectionProvider.getPinned();
				if (pinned != null) {
					final ScenarioInstance instance = ((IScenarioServiceEditorInput) input).getScenarioInstance();
					if (instance == pinned.getScenarioInstance()) {
						toggled = true;
					}
				}
			}
		}
		if (action != null) {
			action.setEnabled(enabled);
			action.setChecked(toggled);
		}
	}

	@Override
	public void init(final IAction action) {
		selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
		selectionProvider.addSelectionChangedListener(selectionChangedListener);
	}

	@Override
	public void dispose() {
		if (selectionProvider != null) {
			selectionProvider.removeSelectionChangedListener(selectionChangedListener);
			selectionProvider = null;
		}
	}

	@Override
	public void runWithEvent(final IAction action, final Event event) {
		if (targetEditor != null) {
			final IEditorInput input = targetEditor.getEditorInput();
			if (input instanceof IScenarioServiceEditorInput) {
				final ScenarioInstance instance = ((IScenarioServiceEditorInput) input).getScenarioInstance();
				ScenarioResult pinned = selectionProvider.getPinned();
				if (pinned != null && pinned.getScenarioInstance() == instance) {
					selectionProvider.setPinned((ScenarioResult) null, false);
				} else {
					selectionProvider.setPinned(new ScenarioResultImpl(instance), false);
				}
			}
		}
	}
}

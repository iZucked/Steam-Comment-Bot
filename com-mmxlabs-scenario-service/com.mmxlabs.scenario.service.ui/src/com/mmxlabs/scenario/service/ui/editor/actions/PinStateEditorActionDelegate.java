/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editor.actions;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.ui.internal.Activator;
import com.mmxlabs.scenario.service.ui.internal.ScenarioServiceSelectionProvider;

public class PinStateEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2 {
	private ScenarioServiceSelectionProvider selectionProvider;
	private IEditorPart targetEditor;
	private IAction action;
	@NonNull
	private final IScenarioServiceSelectionChangedListener selectionChangedListener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> selected, boolean block) {

		}

		@Override
		public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioResult oldPin, final ScenarioResult newPin, boolean block) {

		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> deselected, boolean block) {

		}

		@Override
		public void selectionChanged(ScenarioResult pinned, Collection<ScenarioResult> others, boolean block) {
			updateActionState();
		}
	};

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
				if (selectionProvider.getPinnedInstance() != null) {
					final ScenarioInstance instance = ((IScenarioServiceEditorInput) input).getScenarioInstance();
					if (instance == selectionProvider.getPinnedInstance()) {
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
				if (selectionProvider.getPinnedInstance() == instance) {
					selectionProvider.setPinnedInstance((ScenarioResult) null);
				} else {
					selectionProvider.setPinnedInstance(instance);
				}
			}
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.util.EventObject;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public abstract class ScenarioInstanceViewWithUndoSupport extends ScenarioInstanceView implements CommandStackListener {

	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	protected void makeUndoActions() {

		final IActionBars actionBars = getViewSite().getActionBars();

		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		undoAction = new UndoAction();
		undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		redoAction = new RedoAction();
		redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		updateUndoActions(getEditingDomain());
	}

	@Override
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable Object target) {
		super.doDisplayScenarioInstance(scenarioInstance, rootObject, target);
		updateUndoActions(getEditingDomain());
	}

	@Override
	public void setFocus() {
		updateUndoActions(getEditingDomain());
	}

	protected void updateUndoActions(final EditingDomain editingDomain) {

		if (currentCommandStack != null) {
			currentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
		}
		if (undoAction != null) {
			undoAction.setEditingDomain(editingDomain);
			undoAction.setEnabled(editingDomain != null && editingDomain.getCommandStack().canUndo());
		}
		if (redoAction != null) {
			redoAction.setEditingDomain(editingDomain);
			redoAction.setEnabled(editingDomain != null && editingDomain.getCommandStack().canRedo());
		}
		if (editingDomain != null) {
			currentCommandStack = editingDomain.getCommandStack();
			currentCommandStack.addCommandStackListener(this);

			undoAction.update();
			redoAction.update();
		}
	}

	@Override
	public void commandStackChanged(final EventObject event) {
		if (undoAction != null) {
			undoAction.update();
		}
		if (redoAction != null) {
			redoAction.update();
		}
	}

	@Override
	public void dispose() {

		final CommandStack pCurrentCommandStack = currentCommandStack;
		if (pCurrentCommandStack != null) {
			pCurrentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
		}
		super.dispose();

	}

	@Override
	public void setLocked(final boolean locked) {

		// Disable while locked.
		if (locked) {
			updateUndoActions(null);
		} else {
			updateUndoActions(getEditingDomain());
		}

		super.setLocked(locked);
	}

}

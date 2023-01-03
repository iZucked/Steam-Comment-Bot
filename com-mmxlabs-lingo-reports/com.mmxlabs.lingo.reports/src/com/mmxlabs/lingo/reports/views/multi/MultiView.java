/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.multi;

import java.util.EventObject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.mmxlabs.lingo.reports.views.cargodiff.CargoDiffView;
import com.mmxlabs.lingo.reports.views.scenariodiff.ScenarioDiffReport;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Generalises ScenarioTableViewerView to allow multiple panes.
 * 
 * @author Simon McGregor
 */
public class MultiView extends ScenarioInstanceView implements CommandStackListener {

	// @
	private ScenarioDiffReport scenario_diff = new ScenarioDiffReport();
	private CargoDiffView cargo_diff = new CargoDiffView();

	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	@Override
	public void createPartControl(final Composite parent) {

		try {
			scenario_diff.init(getViewSite());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		try {
			cargo_diff.init(getViewSite());
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		scenario_diff.createPartControl(parent);
		cargo_diff.createPartControl(parent);

		final IActionBars actionBars = getViewSite().getActionBars();

		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		undoAction = new UndoAction();
		undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		redoAction = new RedoAction();
		redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		updateActions(null);

		listenToScenarioSelection();
	}

	protected Composite createChildControl(final Composite parent) {
		return new SashForm(parent, SWT.HORIZONTAL);
	}

	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance, @Nullable MMXRootObject rootObject, @Nullable Object targetObject) {

	}

	protected EReference[][] getPaneRootPaths() {
		return null;
	}

	private void updateActions(final EditingDomain editingDomain) {

		if (currentCommandStack != null) {
			currentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
		}

		undoAction.setEditingDomain(editingDomain);
		redoAction.setEditingDomain(editingDomain);

		undoAction.setEnabled(editingDomain != null && editingDomain.getCommandStack().canUndo());
		redoAction.setEnabled(editingDomain != null && editingDomain.getCommandStack().canRedo());

		if (editingDomain != null) {
			currentCommandStack = editingDomain.getCommandStack();
			currentCommandStack.addCommandStackListener(this);
			undoAction.update();
			redoAction.update();
		}
	}

	@Override
	public void setFocus() {

		scenario_diff.setFocus();
		cargo_diff.setFocus();
		updateActions(getEditingDomain());

	}

	@Override
	public void setLocked(final boolean locked) {

	}

	@Override
	public void openStatus(final IStatus status) {

	}

	@Override
	public void commandStackChanged(final EventObject event) {
		undoAction.update();
		redoAction.update();
	}

	@Override
	public void dispose() {
		if (currentCommandStack != null) {
			currentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
		}
		super.dispose();
	}
}

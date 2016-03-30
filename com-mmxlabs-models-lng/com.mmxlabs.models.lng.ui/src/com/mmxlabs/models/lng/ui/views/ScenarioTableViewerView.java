/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.views;

import java.util.EventObject;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Adds the assumption that the view will contain a {@link ScenarioTableViewerPane}
 * 
 * @author hinton
 * 
 */
public abstract class ScenarioTableViewerView<T extends ScenarioTableViewerPane> extends ScenarioInstanceView implements CommandStackListener {
	private Composite childComposite;
	private @Nullable T viewerPane;
	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	@Override
	public void createPartControl(final Composite parent) {
		this.childComposite = new Composite(parent, SWT.NONE);

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

	protected abstract T createViewerPane();

	protected abstract void initViewerPane(T pane);

	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance) {

		// Clear existing settings
		updateActions(null);
		if (instance != getScenarioInstance()) {
			// Pin the reference
			@Nullable
			final T nViewerPane = viewerPane;
			if (nViewerPane != null) {
				getSite().setSelectionProvider(null);
				nViewerPane.dispose();
				viewerPane = null;
			}

			final Composite parent = childComposite.getParent();
			childComposite.dispose();
			childComposite = new Composite(parent, SWT.NONE);
			childComposite.setLayout(new FillLayout());

			super.displayScenarioInstance(instance);
			if (instance != null) {
				// Pin the reference
				final T pViewerPane = createViewerPane();
				pViewerPane.setExternalMenuManager((MenuManager) getViewSite().getActionBars().getMenuManager());
				pViewerPane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
				pViewerPane.createControl(childComposite);
				pViewerPane.setLocked(isLocked());
				this.viewerPane = pViewerPane;
				initViewerPane(pViewerPane);
			}
			parent.layout(true);

			// re-enable!
			updateActions(getEditingDomain());
		}
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
		@Nullable
		T pViewerPane = viewerPane;
		if (pViewerPane != null) {
			pViewerPane.setFocus();
		} else if (childComposite != null) {
			childComposite.setFocus();
		}
		updateActions(getEditingDomain());
	}

	@Override
	public void setLocked(final boolean locked) {
		@Nullable
		T pViewerPane = viewerPane;
		if (pViewerPane != null) {
			pViewerPane.setLocked(locked);
		}

		// Disable while locked.
		if (locked) {
			updateActions(null);
		} else {
			updateActions(getEditingDomain());
		}

		super.setLocked(locked);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	protected void setSelection(final ISelection selection, final boolean reveal) {
		@Nullable
		T pViewerPane = viewerPane;
		if (pViewerPane != null) {
			pViewerPane.getScenarioViewer().setSelection(selection, reveal);
		}
	}

	@Override
	public void commandStackChanged(final EventObject event) {
		undoAction.update();
		redoAction.update();
	}

	@Override
	public void dispose() {
		CommandStack pCurrentCommandStack = currentCommandStack;
		if (pCurrentCommandStack != null) {
			pCurrentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
		}
		super.dispose();
	}
}

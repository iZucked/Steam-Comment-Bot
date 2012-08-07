/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceDiffingEditorInput;

public class JointModelEditorContributor extends MultiPageEditorActionBarContributor implements IPropertyListener, IEditorActionBarContributor {

	protected IEditorPart activeEditor;
	protected UndoAction undoAction;
	protected RedoAction redoAction;
	private Action applyAction;
	private Action cancelAction;
	private ActionContributionItem applyContributionItem;
	private ActionContributionItem cancelContributionItem;

	@Override
	public void init(final IActionBars actionBars, final IWorkbenchPage page) {
		super.init(actionBars);
		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		undoAction = new UndoAction();
		undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		redoAction = new RedoAction();
		redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		applyAction = new Action("Apply") {
			public void run() {
				if (activeEditor != null) {
					final IEditorInput editorInput = activeEditor.getEditorInput();
					if (editorInput instanceof IScenarioServiceDiffingEditorInput) {
						final IScenarioServiceDiffingEditorInput ssInput = (IScenarioServiceDiffingEditorInput) editorInput;
						ssInput.getDiffEditHandler().onPreEditorApply();
						activeEditor.getSite().getPage().closeEditor(activeEditor, false);
						ssInput.getDiffEditHandler().onEditorApply();
					}
				}
			}
		};

		cancelAction = new Action("Cancel") {
			public void run() {

				if (activeEditor != null) {
					final IEditorInput editorInput = activeEditor.getEditorInput();
					if (editorInput instanceof IScenarioServiceDiffingEditorInput) {
						final IScenarioServiceDiffingEditorInput ssInput = (IScenarioServiceDiffingEditorInput) editorInput;
						ssInput.getDiffEditHandler().onPreEditorCancel();
						activeEditor.getSite().getPage().closeEditor(activeEditor, false);
						ssInput.getDiffEditHandler().onEditorCancel();
					}
				}
			}
		};

		applyContributionItem = new ActionContributionItem(applyAction);
		cancelContributionItem = new ActionContributionItem(cancelAction);
	}

	@Override
	public void contributeToMenu(final IMenuManager menuManager) {
		super.contributeToMenu(menuManager);
	}

	@Override
	public void contributeToStatusLine(final IStatusLineManager statusLineManager) {

		// statusLineManager.add(applyContributionItem);
		// statusLineManager.add(cancelContributionItem);

		super.contributeToStatusLine(statusLineManager);
	}

	@Override
	public void contributeToToolBar(final IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);
	}

	public void shareGlobalActions(final IPage page, final IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
	}

	public IEditorPart getActiveEditor() {
		return activeEditor;
	}

	@Override
	public void setActiveEditor(final IEditorPart part) {
		super.setActiveEditor(part);

		if (part != activeEditor) {
			if (activeEditor != null) {
				deactivate();
			}

			if (part instanceof IEditingDomainProvider) {
				activeEditor = part;
				activate();

			}
		}
	}

	@Override
	public void setActivePage(final IEditorPart part) {
		// Do nothing
	}

	public void deactivate() {
		activeEditor.removePropertyListener(this);
		if (activeEditor != null) {
			final IEditorInput editorInput = activeEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceDiffingEditorInput) {
				getActionBars().getStatusLineManager().remove(applyContributionItem);
				getActionBars().getStatusLineManager().remove(cancelContributionItem);
				getActionBars().updateActionBars();
			}
		}
		undoAction.setActiveWorkbenchPart(null);
		redoAction.setActiveWorkbenchPart(null);

	}

	public void activate() {
		activeEditor.addPropertyListener(this);
		if (activeEditor != null) {
			final IEditorInput editorInput = activeEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceDiffingEditorInput) {
				getActionBars().getStatusLineManager().add(applyContributionItem);
				getActionBars().getStatusLineManager().add(cancelContributionItem);
				getActionBars().updateActionBars();
			}
		}
		undoAction.setActiveWorkbenchPart(activeEditor);
		redoAction.setActiveWorkbenchPart(activeEditor);

		update();
	}

	public void update() {

		undoAction.update();
		redoAction.update();

	}

	// /**
	// * This implements {@link org.eclipse.jface.action.IMenuListener} to help fill the context menus with contributions from the Edit menu.
	// */
	// public void menuAboutToShow(IMenuManager menuManager) {
	// // Add our standard marker.
	// //
	// if ((style & ADDITIONS_LAST_STYLE) == 0) {
	// menuManager.add(new Separator("additions"));
	// }
	// menuManager.add(new Separator("edit"));
	//
	// // Add the edit menu actions.
	// //
	// menuManager.add(new ActionContributionItem(undoAction));
	// menuManager.add(new ActionContributionItem(redoAction));
	// menuManager.add(new Separator());
	// menuManager.add(new ActionContributionItem(cutAction));
	// menuManager.add(new ActionContributionItem(copyAction));
	// menuManager.add(new ActionContributionItem(pasteAction));
	// menuManager.add(new Separator());
	// menuManager.add(new ActionContributionItem(deleteAction));
	// menuManager.add(new Separator());
	//
	// if ((style & ADDITIONS_LAST_STYLE) != 0) {
	// menuManager.add(new Separator("additions"));
	// menuManager.add(new Separator());
	// }
	// // Add our other standard marker.
	// //
	// menuManager.add(new Separator("additions-end"));
	//
	// addGlobalActions(menuManager);
	// }
	//
	// /**
	// * This inserts global actions before the "additions-end" separator.
	// */
	// protected void addGlobalActions(IMenuManager menuManager) {
	// String key = (style & ADDITIONS_LAST_STYLE) == 0 ? "additions-end" : "additions";
	// if (validateAction != null) {
	// menuManager.insertBefore(key, new ActionContributionItem(validateAction));
	// }
	//
	// if (controlAction != null) {
	// menuManager.insertBefore(key, new ActionContributionItem(controlAction));
	// }
	//
	// if (validateAction != null || controlAction != null) {
	// menuManager.insertBefore(key, new Separator());
	// }
	//
	// if (loadResourceAction != null) {
	// menuManager.insertBefore("additions-end", new ActionContributionItem(loadResourceAction));
	// menuManager.insertBefore("additions-end", new Separator());
	// }
	// }

	public void propertyChanged(final Object source, final int id) {
		update();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class JointModelEditorContributor extends MultiPageEditorActionBarContributor implements IPropertyListener, IEditorActionBarContributor {

	protected IEditorPart activeEditor;
	protected UndoAction undoAction;
	protected RedoAction redoAction;
	private IPartListener partListener;
	private IWorkbenchPage page;

	@Override
	public void init(final IActionBars actionBars, final IWorkbenchPage page) {
		this.page = page;
		super.init(actionBars, page);
		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		undoAction = new UndoAction();
		undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		redoAction = new RedoAction();
		redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		partListener = new IPartListener() {

			@Override
			public void partOpened(final IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partClosed(final IWorkbenchPart part) {
				if (part == activeEditor) {
					deactivate();
					activeEditor = null;
				}
			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {

			}

			@Override
			public void partActivated(final IWorkbenchPart part) {

			}
		};
		page.addPartListener(partListener);
	}

	@Override
	public void dispose() {
		if (page != null) {
			page.removePartListener(partListener);
			page = null;
		}
	}

	@Override
	public void contributeToToolBar(final IToolBarManager toolBarManager) {
		// Editor actions go here
		toolBarManager.add(new GroupMarker("autoevaluategroup"));
		toolBarManager.add(new GroupMarker("forkgroup"));
		toolBarManager.add(new GroupMarker("evaluategroup2"));
		toolBarManager.add(new GroupMarker("evaluategroup3"));
		toolBarManager.add(new GroupMarker("optimisationgroup"));
		toolBarManager.add(new GroupMarker("additions-end"));
		super.contributeToToolBar(toolBarManager);
	}

	@Override
	public void contributeToCoolBar(ICoolBarManager coolBarManager) {
		// coolBarManager.add(new GroupMarker("additions"));
		coolBarManager.add(new GroupMarker("autoevaluategroup"));
		coolBarManager.add(new GroupMarker("forkgroup"));
		coolBarManager.add(new GroupMarker("evaluategroup2"));
		coolBarManager.add(new GroupMarker("evaluategroup3"));
		coolBarManager.add(new GroupMarker("optimisationgroup"));
		coolBarManager.add(new GroupMarker("additions-end"));
		super.contributeToCoolBar(coolBarManager);
	}

	public void shareGlobalActions(final IPage page, final IActionBars actionBars) {
		// register undo and redo global (hotkey / menu) actions for any editor which
		// is controlled by the JointModelEditorContributor
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
		if (activeEditor != null) {
			activeEditor.removePropertyListener(this);
		}
		undoAction.setActiveWorkbenchPart(null);
		redoAction.setActiveWorkbenchPart(null);
		// While setActiveWorkbenchPart(editor) will set the editing domain, it does not clear it.
		undoAction.setEditingDomain(null);
		redoAction.setEditingDomain(null);

	}

	public void activate() {
		if (activeEditor != null) {
			activeEditor.addPropertyListener(this);
		}
		undoAction.setActiveWorkbenchPart(activeEditor);
		redoAction.setActiveWorkbenchPart(activeEditor);

		update();
	}

	public void update() {
		if (undoAction.getEditingDomain() != null) {
			undoAction.update();
		} else {
			undoAction.setEnabled(false);
		}
		if (redoAction.getEditingDomain() != null) {
			redoAction.update();
		} else {
			redoAction.setEnabled(false);
		}

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

	@Override
	public void propertyChanged(final Object source, final int id) {
		update();
	}
}

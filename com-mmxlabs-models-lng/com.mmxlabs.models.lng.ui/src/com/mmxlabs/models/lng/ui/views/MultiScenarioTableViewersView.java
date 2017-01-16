/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.views;

import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Generalises ScenarioTableViewerView to allow multiple panes.
 * 
 * @author Simon McGregor
 */
public abstract class MultiScenarioTableViewersView extends ScenarioInstanceView implements CommandStackListener {
	private Composite childComposite;
	private final LinkedList<ScenarioTableViewerPane> viewerPanes = new LinkedList<ScenarioTableViewerPane>();
	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	@Override
	public void createPartControl(final Composite parent) {
		childComposite = createChildControl(parent);

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
	protected void displayScenarioInstance(final ScenarioInstance instance) {
		updateActions(null);

		if (instance != getScenarioInstance()) {
			for (final ScenarioTableViewerPane pane : viewerPanes) {
				if (pane != null) {
					getSite().setSelectionProvider(null);
					pane.dispose();
				}
			}

			viewerPanes.clear();

			final Composite parent = childComposite.getParent();
			childComposite.dispose();
			childComposite = createChildControl(parent);
			childComposite.setLayout(new FillLayout());

			super.displayScenarioInstance(instance);
			if (instance != null) {
				viewerPanes.addAll(createViewerPanes());
				for (final ScenarioTableViewerPane pane : viewerPanes) {
					pane.createControl(childComposite);
					pane.setLocked(isLocked());

				}
				initViewerPanes(viewerPanes);

			}
			parent.layout(true);
			updateActions(getEditingDomain());

		}
	}

	abstract protected EReference[][] getPaneRootPaths();

	protected void initViewerPanes(final List<ScenarioTableViewerPane> panes) {

		final EditingDomain domain = getEditingDomain();
		final EReference[][] references = getPaneRootPaths();

		if (domain != null) {
			for (int i = 0; i < panes.size(); i++) {
				panes.get(i).init(Arrays.asList(references[i]), getAdapterFactory(), domain.getCommandStack());
				panes.get(i).getViewer().setInput(getRootObject());
			}
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
		if (childComposite != null) {
			childComposite.setFocus();
		}
		updateActions(getEditingDomain());

	}

	@Override
	public void setLocked(final boolean locked) {
		for (final ScenarioTableViewerPane pane : viewerPanes) {
			if (pane != null) {
				pane.setLocked(locked);
			}
		}

		// Disable while locked.
		if (locked) {
			updateActions(null);
		} else {
			updateActions(getEditingDomain());
		}

		super.setLocked(locked);
	}

	abstract protected List<ScenarioTableViewerPane> createViewerPanes();

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			final EObject target = dcsd.getTarget();

			final EReference[][] paths = getPaneRootPaths();

			for (int i = 0; i < paths.length; i++) {
				final EReference[] path = paths[i];
				final EClass rootClass = path[path.length - 1].getEReferenceType();

				// dcsd target matches the root class for this pane
				if (rootClass.isInstance(target)) {
					getSite().getPage().activate(this);
					if (viewerPanes.isEmpty() == false) {
						final StructuredSelection selection = new StructuredSelection(target);
						viewerPanes.get(i).getScenarioViewer().setSelection(selection, true);
					}
				}

			}
		}

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

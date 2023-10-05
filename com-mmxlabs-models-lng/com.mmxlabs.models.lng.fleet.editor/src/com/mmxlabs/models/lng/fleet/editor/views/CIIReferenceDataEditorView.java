/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.EventObject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class CIIReferenceDataEditorView extends ScenarioInstanceView implements CommandStackListener {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.fleet.editor.views.CIIReferenceDataEditorView";

	private EmbeddedDetailComposite ciiReferenceDataComposite;
	protected Composite childComposite;
	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	protected EObject getInput(final MMXRootObject rootObject) {
		if (rootObject != null) {
			CIIReferenceData refData = null;
			if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
				@NonNull
				FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
				refData = fleetModel.getCiiReferences();
				if (refData == null) {
					throw new IllegalStateException("CII Reference data should be instanciated");
				}
				return refData;
			}
		}
		return null;
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator dcsd) {
			final EObject target = dcsd.getTarget();
//			if (target instanceof CanalBookingSlot) {
//				getSite().getPage().activate(this);
//				// extend the EmbeddedDetailComposite to support the setSelection and setStatus
//				// setSelection(new StructuredSelection(target), true);
//			}
		}
	}

	@Override
	public void createPartControl(Composite parent) {

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

	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance, @Nullable final MMXRootObject rootObject, @Nullable final Object targetObject) {

		// Clear existing settings
		updateActions(null);
		if (instance != getScenarioInstance()) {
			cleanUpInstance(instance);

			if (ciiReferenceDataComposite != null) {
				getSite().setSelectionProvider(null);
				ciiReferenceDataComposite.dispose();
				ciiReferenceDataComposite = null;
			}

			final Composite parent = childComposite.getParent();
			childComposite.dispose();
			childComposite = new Composite(parent, SWT.NONE);
			childComposite.setLayout(new FillLayout());

			super.displayScenarioInstance(instance, rootObject, targetObject);
			if (instance != null) {
				// Pin the reference
				ciiReferenceDataComposite = new EmbeddedDetailComposite(childComposite, this);
				ciiReferenceDataComposite.setInput(getInput(getRootObject()));
			}
			parent.layout(true);

			// re-enable!
			updateActions(getEditingDomain());
		}
	}

	protected void cleanUpInstance(final ScenarioInstance instance) {

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
		if (ciiReferenceDataComposite != null) {
			ciiReferenceDataComposite.setLocked(locked);
		}

		// Disable while locked.
		if (locked) {
			updateActions(null);
		} else {
			updateActions(getEditingDomain());
		}

		super.setLocked(locked);
	}

	@Override
	public void commandStackChanged(final EventObject event) {
		undoAction.update();
		redoAction.update();
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
}
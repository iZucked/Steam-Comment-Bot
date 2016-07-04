/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.ui.ImageConstants;
import com.mmxlabs.models.lng.ui.LngUIActivator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * @author hinton
 * 
 */
public class DuplicateAction extends ScenarioModifyingAction {
	private IScenarioEditingLocation part;

	public DuplicateAction(final IScenarioEditingLocation part) {
		super("Duplicate selection");
		setImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE));
		setDisabledImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE_DISABLED));
		this.part = part;
	}

	@Override
	public void run() {
		final IStructuredSelection selection = (IStructuredSelection) getLastSelection();
		final DetailCompositeDialog dcd = new DetailCompositeDialog(part.getShell(), part.getDefaultCommandHandler());
		dcd.setReturnDuplicates(true);

		final ScenarioLock editorLock = part.getEditorLock();
		try {
			editorLock.claim();
			part.setDisableUpdates(true);
			dcd.open(part, part.getRootObject(), selection.toList());
		} finally {
			part.setDisableUpdates(false);
			editorLock.release();
		}

	}

	@Override
	protected boolean isApplicableToSelection(ISelection selection) {
		return (selection instanceof IStructuredSelection) && (!selection.isEmpty());
	}
}

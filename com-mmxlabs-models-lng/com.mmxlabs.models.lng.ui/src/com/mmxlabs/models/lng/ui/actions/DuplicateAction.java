/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;

/**
 * @author hinton
 *
 */
public class DuplicateAction extends ScenarioModifyingAction {
	private JointModelEditorPart part;

	public DuplicateAction(final JointModelEditorPart part) {
		super("Duplicate selection");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/fastview_restore.gif"));
		this.part = part;
	}
	
	@Override
	public void run() {
		final IStructuredSelection selection = (IStructuredSelection) getLastSelection();
		final DetailCompositeDialog dcd = new DetailCompositeDialog(part.getSite().getShell(), part.getDefaultCommandHandler());
		dcd.setReturnDuplicates(true);
		dcd.open(part, part.getRootObject(), selection.toList());
	}

	@Override
	protected boolean isApplicableToSelection(ISelection selection) {
		return (selection instanceof IStructuredSelection) && (!selection.isEmpty());
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.actions;

import java.security.acl.LastOwnerException;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.dialogs.WiringDialog;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

/**
 * @author hinton
 *
 */
public class RewireAction extends ScenarioModifyingAction {
	private JointModelEditorPart part;

	public RewireAction(final JointModelEditorPart part) {
		super("Rewire");
		this.part = part;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final WiringDialog dialog = new WiringDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		
		dialog.open(
				((IStructuredSelection)getLastSelection()).toList()
				,
				part.getEditingDomain()
				, part.getReferenceValueProviderCache().getReferenceValueProvider(CargoPackage.eINSTANCE.getSlot(), 
						CargoPackage.eINSTANCE.getSlot_Port()
						));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction#isApplicableToSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	protected boolean isApplicableToSelection(ISelection selection) {
		if (selection.isEmpty())
			return false;
		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).size() < 2)
				return false;
			for (final Object o : ((IStructuredSelection) selection).toArray()) {
				if (!(o instanceof Cargo)) {
					return false;
				}
			}
			return true;

		}
		return false;
	}

}

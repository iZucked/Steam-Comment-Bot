/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.dialogs.WiringDialog;
import com.mmxlabs.models.lng.cargo.ui.valueproviders.SlotPortValueProviderFactory;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

/**
 * Action to display wiring editor.
 * 
 * @author hinton
 * 
 */
public class RewireAction extends ScenarioModifyingAction {
	private final IScenarioEditingLocation part;

	public RewireAction(final IScenarioEditingLocation iScenarioEditingLocation) {
		super("Rewire");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/rewire_icon.png"));
		this.part = iScenarioEditingLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final WiringDialog dialog = new WiringDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());

		try {
			part.setDisableCommandProviders(true);
			part.setDisableUpdates(true);

			dialog.open(part.getRootObject(), ((IStructuredSelection) getLastSelection()).toList(), part.getEditingDomain(), new SlotPortValueProviderFactory().createReferenceValueProvider(
					CargoPackage.eINSTANCE.getSlot(), CargoPackage.eINSTANCE.getSlot_Port(), part.getRootObject()),
					part.getReferenceValueProviderCache().getReferenceValueProvider(CargoPackage.eINSTANCE.getSlot(), CargoPackage.eINSTANCE.getSlot_Contract()));
		} finally {
			part.setDisableCommandProviders(false);
			part.setDisableUpdates(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction#isApplicableToSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	protected boolean isApplicableToSelection(final ISelection selection) {
		if (selection.isEmpty())
			return false;
		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).isEmpty())
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

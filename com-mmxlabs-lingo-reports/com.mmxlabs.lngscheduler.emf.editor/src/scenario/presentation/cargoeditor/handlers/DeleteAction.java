/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import scenario.presentation.cargoeditor.handlers.delete.DeleteHelper;

/**
 * An action for deleting model elements; there's another built-in action by the
 * same name, which might do just as well?
 * 
 * @author Tom Hinton
 * 
 */
public abstract class DeleteAction extends Action {
	private final EditingDomain editingDomain;

	protected DeleteAction(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		setToolTipText("Delete Selection");
		setText("Delete Selection");
	}

	protected abstract Collection<EObject> getTargets();

	@Override
	public void run() {
		final Collection<EObject> target = getTargets();
		if (target.size() > 0) {
			editingDomain.getCommandStack().execute(
					DeleteHelper.createDeleteCommand(editingDomain, target));
		}
	}
}

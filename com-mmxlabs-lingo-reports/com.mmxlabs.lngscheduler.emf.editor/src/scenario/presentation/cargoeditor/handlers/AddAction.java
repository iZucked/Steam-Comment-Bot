/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * An action for adding model elements
 * 
 * @author Tom Hinton
 * 
 */
public abstract class AddAction extends Action {
	public AddAction(final EditingDomain editingDomain, final String name) {
		super();
		this.editingDomain = editingDomain;

		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		setToolTipText("Add " + name);
		setText("Add " + name);
	}

	/**
	 * Create an instance of the object to add, or return null to cancel the
	 * action
	 * 
	 * @return
	 */
	public abstract EObject createObject();

	public abstract Object getOwner();

	public abstract Object getFeature();

	private final EditingDomain editingDomain;

	@Override
	public void run() {
		final EObject object = createObject();
		if (object == null)
			return; // if cancelled, subclasses return null
		editingDomain.getCommandStack().execute(
				AddCommand.create(editingDomain, getOwner(), getFeature(),
						object));
	}
}

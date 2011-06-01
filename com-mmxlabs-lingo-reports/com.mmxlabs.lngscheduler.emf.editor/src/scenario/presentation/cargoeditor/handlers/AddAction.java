/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import java.util.Collections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;

import scenario.presentation.LngEditorPlugin;

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

		setImageDescriptor(LngEditorPlugin.Implementation
				.imageDescriptorFromPlugin(LngEditorPlugin.getPlugin()
						.getSymbolicName(), "/icons/add.gif"));
		setToolTipText("Add " + name);
		setText("Add " + name);
	}

	/**
	 * Create an instance of the object to add, or return null to cancel the
	 * action
	 * 
	 * @return
	 */
	protected abstract EObject createObject();

	protected abstract Object getOwner();

	protected abstract Object getFeature();

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

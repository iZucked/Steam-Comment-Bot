/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;

import scenario.presentation.LngEditorPlugin;

/**
 * An action for deleting model elements; there's another built-in action by the same name, which might do just as well?
 * @author Tom Hinton
 *
 */
public abstract class DeleteAction extends Action {
	private final EditingDomain editingDomain;
	protected DeleteAction(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
		setImageDescriptor(LngEditorPlugin.Implementation
				.imageDescriptorFromPlugin(LngEditorPlugin.getPlugin()
						.getSymbolicName(), "/icons/delete.gif"));
		setToolTipText("Delete Selection");
		setText("Delete Selection");
	}
	protected abstract Collection<EObject> getTargets();
	@Override
	public void run() {
		final Collection<EObject> target = getTargets();
		final Command deleteCommand = 
			editingDomain.createCommand(DeleteCommand.class, new CommandParameter(null, null, target));
		editingDomain.getCommandStack().execute(deleteCommand);
	}
}

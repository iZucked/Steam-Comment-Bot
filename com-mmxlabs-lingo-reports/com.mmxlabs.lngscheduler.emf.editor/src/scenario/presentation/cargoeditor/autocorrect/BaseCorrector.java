package scenario.presentation.cargoeditor.autocorrect;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.presentation.cargoeditor.autocorrect.AutoCorrector.ICorrector;

public abstract class BaseCorrector implements ICorrector {
	protected Command makeSetter(final EditingDomain editingDomain,
			final EObject object, final EAttribute attribute, final Object value) {
		final Command command = editingDomain.createCommand(SetCommand.class,
				new CommandParameter(object, attribute, value));
		((SetCommand) command).setLabel("Set " + attribute.getName() + " to "
				+ value == null ? "null " : value.toString());
		return command;
	}
}

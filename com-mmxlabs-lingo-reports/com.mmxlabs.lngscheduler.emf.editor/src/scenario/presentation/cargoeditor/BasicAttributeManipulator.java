/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Displays a textbox for editing an EAttribute.
 * 
 * @author hinton
 * 
 */
public class BasicAttributeManipulator implements ICellManipulator,
		ICellRenderer {

	protected final EStructuralFeature field;
	protected final EditingDomain editingDomain;

	public BasicAttributeManipulator(final EStructuralFeature field,
			final EditingDomain editingDomain) {
		super();
		this.field = field;
		this.editingDomain = editingDomain;
	}

	@Override
	public String render(final Object object) {
		return getValue(object).toString();
	}

	@Override
	public void setValue(final Object object, final Object value) {
		final Object currentValue = reallyGetValue(object);
		if ((currentValue == null && value == null) || ((currentValue != null && value != null) && currentValue.equals(value)))
			return;
		System.err.println(currentValue + " => " + value);
		final Command command = editingDomain.createCommand(SetCommand.class,
				new CommandParameter((EObject) object, field, value));
		((SetCommand) command).setLabel("Set " + field.getName() + " to "
				+ value == null ? "null " : value.toString());
		editingDomain.getCommandStack().execute(command);
	}

	@Override
	public CellEditor getCellEditor(final Composite c, final Object object) {
		return new TextCellEditor(c);
	}

	@Override
	public Object getValue(Object object) {
		return reallyGetValue(object);
	}
	
	private Object reallyGetValue(Object object) {
		return ((EObject) object).eGet(field);
	}

	@Override
	public boolean canEdit(Object object) {
		return true;
	}
}

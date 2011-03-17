/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.ListSelectionDialog;

/**
 * @author hinton
 * 
 */
public class MultipleReferenceManipulator extends DialogFeatureManipulator {
	private final IReferenceValueProvider valueProvider;
	private final EAttribute nameAttribute;

	public MultipleReferenceManipulator(final EStructuralFeature field,
			final EditingDomain editingDomain,
			final IReferenceValueProvider valueProvider,
			final EAttribute nameAttribute) {
		super(field, editingDomain);
		this.valueProvider = valueProvider;
		this.nameAttribute = nameAttribute;
	}

	@Override
	protected String renderValue(Object value) {
		EList<? extends EObject> selectedValues = (EList<? extends EObject>) value;
		final StringBuilder sb = new StringBuilder();
		for (final EObject obj : selectedValues) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(obj.eGet(nameAttribute).toString());
		}
		return sb.toString();
	}

	@Override
	public void setValue(Object object, Object value) {
		final EObject target = (EObject) object;
		final CompoundCommand cc = new CompoundCommand(
				CompoundCommand.LAST_COMMAND_ALL);

		// TODO: Check equals...
		// TODO set command name.
		cc.append(editingDomain.createCommand(
				RemoveCommand.class,
				new CommandParameter(target, field, (Collection<?>) target
						.eGet(field))));
		cc.append(editingDomain.createCommand(AddCommand.class,
				new CommandParameter(target, field, (Collection<?>) value)));
		editingDomain.getCommandStack().execute(cc);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow, Object object) {
		Iterable<? extends EObject> values = valueProvider.getAllowedValues(
				(EObject) object, field);

		ArrayList<EObject> options = new ArrayList<EObject>();
		for (final EObject v : values)
			options.add(v);

		ListSelectionDialog dlg = new ListSelectionDialog(
				cellEditorWindow.getShell(), options.toArray(),
				new ArrayContentProvider(), new LabelProvider() {

					@Override
					public String getText(Object element) {
						return ((EObject) element).eGet(nameAttribute)
								.toString();

					}
				}, "Select values:");
		dlg.setTitle("Value Selection");
		dlg.setInitialSelections(((Collection<?>) getValue(object)).toArray());
		dlg.setBlockOnOpen(true);
		dlg.open();
		Object[] result = dlg.getResult();
		if (result == null)
			return null;
		else
			return Arrays.asList(result);
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
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

import com.mmxlabs.common.Pair;

/**
 * @author hinton
 * 
 */
public class MultipleReferenceManipulator extends DialogFeatureManipulator {
	private final IReferenceValueProvider valueProvider;
	private EAttribute nameAttribute;

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
		List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		final StringBuilder sb = new StringBuilder();
		for (final EObject obj : selectedValues) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(obj.eGet(nameAttribute));
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

	@SuppressWarnings("unchecked")
	@Override
	protected Object openDialogBox(Control cellEditorWindow, Object object) {
		List<Pair<String, EObject>> options = valueProvider.getAlloweValues((EObject)object, field);
		
		if (options.size() > 0 && options.get(0).getSecond() == null)
			options.remove(0);
		
		ListSelectionDialog dlg = new ListSelectionDialog(
				cellEditorWindow.getShell(), options.toArray(),
				new ArrayContentProvider(), new LabelProvider() {

					@Override
					public String getText(Object element) {
						return ((Pair<String, ?> )element).getFirst();
					}
				}, "Select values:");
		dlg.setTitle("Value Selection");
		
		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<Pair<String, EObject>>();
		final Collection<EObject> sel = (Collection<EObject>) getValue(object);
		for (final Pair<String, EObject> p : options) {
			if (sel.contains(p.getSecond())) {
				selectedOptions.add(p);
			}
		}
		
		dlg.setInitialSelections(selectedOptions.toArray());
		dlg.setBlockOnOpen(true);
		dlg.open();
		Object[] result = dlg.getResult();
		if (result == null)
			return null;
		else {
			final ArrayList<EObject> resultList = new ArrayList<EObject>();
			for (final Object o : result) {
				resultList.add(((Pair<String, EObject>) o).getSecond());
			}
			return resultList;
		}
	}
}

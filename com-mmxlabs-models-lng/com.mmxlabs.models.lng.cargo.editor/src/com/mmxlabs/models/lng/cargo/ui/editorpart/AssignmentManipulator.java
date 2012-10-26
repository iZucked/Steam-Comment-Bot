package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

class AssignmentManipulator implements ICellRenderer, ICellManipulator {
	/**
	 * 
	 */
	private final IScenarioEditingLocation location;
	private final IReferenceValueProvider valueProvider;
	private List<Pair<String, EObject>> allowedValues;
	private final List<EObject> vessels = new ArrayList<EObject>();

	public AssignmentManipulator(final IScenarioEditingLocation location) {
		this.location = location;
		this.valueProvider = location.getReferenceValueProviderCache().getReferenceValueProvider(InputPackage.eINSTANCE.getElementAssignment(),
				InputPackage.eINSTANCE.getElementAssignment_Assignment());
		getValues();
	}

	private void getValues() {
		allowedValues = valueProvider.getAllowedValues(null, InputPackage.eINSTANCE.getAssignment_Vessels());
		vessels.clear();
		for (final Pair<String, EObject> p : allowedValues)
			vessels.add(p.getSecond());
	}

	@Override
	public void setValue(final Object object, final Object value) {
		// grar.
		final InputModel input = location.getRootObject().getSubModel(InputModel.class);
		if (input != null) {
			if (value == null || value.equals(-1))
				return;
			final AVesselSet set = (AVesselSet) vessels.get((Integer) value);

			location.getEditingDomain().getCommandStack().execute(AssignmentEditorHelper.reassignElement(location.getEditingDomain(), input, (UUIDObject) object, set));
		}
	}

	@Override
	public CellEditor getCellEditor(final Composite parent, final Object object) {
		getValues();
		final String[] items = new String[allowedValues.size() - 1];
		for (int i = 0; i < items.length; i++) {
			items[i] = allowedValues.get(i).getFirst();
		}
		return new ComboBoxCellEditor(parent, items);
	}

	@Override
	public Integer getValue(final Object object) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			final InputModel input = location.getRootObject().getSubModel(InputModel.class);
			if (input != null) {
				ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(input, cargo);
				if (assignment != null)
					return vessels.indexOf(assignment.getAssignment());
			}
		}

		return null;
	}

	@Override
	public boolean canEdit(final Object object) {
		return object instanceof Cargo;
	}

	@Override
	public String render(final Object object) {
		final Integer value = getValue(object);
		if (value == null || value == -1)
			return "";
		return allowedValues.get(value).getFirst();
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		return render(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return render(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return Collections.singleton(new Pair<Notifier, List<Object>>(location.getRootObject().getSubModel(InputModel.class), Collections.emptyList()));
	}
}
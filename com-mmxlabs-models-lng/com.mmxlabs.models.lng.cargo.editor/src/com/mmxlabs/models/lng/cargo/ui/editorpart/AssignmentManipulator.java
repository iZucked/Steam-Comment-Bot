/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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

	public AssignmentManipulator(final IScenarioEditingLocation location) {
		this.location = location;
		this.valueProvider = location.getReferenceValueProviderCache().getReferenceValueProvider(AssignmentPackage.eINSTANCE.getElementAssignment(),
				AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
	}

	private List<Pair<String, EObject>> getAllowedValues(final EObject target, List<Pair<String, EObject>> storage) {
		if (storage == null)
			storage = new ArrayList<Pair<String, EObject>>();
		else
			storage.clear();

		storage.addAll(valueProvider.getAllowedValues(target, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment()));

		return storage;
	}

	@Override
	public void setValue(final Object object, final Object value) {
		// grar.
		final AssignmentModel input = getAssignmentModel();
		if (input != null) {
			//
			if (value == null || value.equals(-1))
				return;

			allowedValues = getAllowedValues((EObject) object, allowedValues);

			// locate the appropriate value in the list of options
			final AVesselSet<Vessel> set = (AVesselSet<Vessel>) (allowedValues.get((Integer) value).getSecond());
			final EditingDomain ed = location.getEditingDomain();

			ed.getCommandStack().execute(AssignmentEditorHelper.reassignElement(ed, input, (UUIDObject) object, set));
		}
	}

	@Override
	public CellEditor getCellEditor(final Composite parent, final Object object) {
		allowedValues = getAllowedValues((EObject) object, allowedValues);
		final String[] items = new String[allowedValues.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = allowedValues.get(i).getFirst();
		}
		return new ComboBoxCellEditor(parent, items);
	}

	public AVesselSet<Vessel> getVessel(final Object object) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			final AssignmentModel input = getAssignmentModel();
			if (input != null) {
				final ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(input, cargo);
				if (assignment != null)
					return assignment.getAssignment();
			}
		}

		return null;

	}

	protected AssignmentModel getAssignmentModel() {
		if (location.getRootObject() instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) location.getRootObject();
			return lngScenarioModel.getPortfolioModel().getAssignmentModel();
		}
		return null;
	}

	@Override
	public Integer getValue(final Object object) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			final AssignmentModel input = getAssignmentModel();
			if (input != null) {
				final ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(input, cargo);

				allowedValues = getAllowedValues((EObject) object, allowedValues);
				for (int i = 0; i < allowedValues.size(); i++) {
					if (Equality.isEqual(allowedValues.get(i).getSecond(), assignment.getAssignment())) {
						return i;
					}
				}
			}
		}

		return null;
	}

	@Override
	public boolean canEdit(final Object object) {
		return object instanceof Cargo && (((Cargo) object).getCargoType() == CargoType.FLEET);
	}

	@Override
	public String render(final Object object) {
		// TODO: document this case
		if (object instanceof Cargo && (((Cargo) object).getCargoType() != CargoType.FLEET)) {
			return "";
		}
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			// get the VesselSet currently attached to the cargo
			final AVesselSet<Vessel> vs = getVessel(cargo);
			// by preference, find the string attached to this object by the value provider
			for (final Pair<String, EObject> pair : getAllowedValues(cargo, allowedValues)) {
				if (pair.getSecond() == vs)
					return pair.getFirst();
			}
			// if no string has been attached to the object, return a default string representation
			if (vs == null)
				// this case should not occur, since the value provider should offer null as an option
				return "(Null)";
			else
				// fall back on displaying the VesselSet's name
				return vs.getName();
		}

		// This case can happen in e.g. the wiring editor row slot only rows
		return "";
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
		return Collections.singleton(new Pair<Notifier, List<Object>>(getAssignmentModel(), Collections.emptyList()));
	}
}
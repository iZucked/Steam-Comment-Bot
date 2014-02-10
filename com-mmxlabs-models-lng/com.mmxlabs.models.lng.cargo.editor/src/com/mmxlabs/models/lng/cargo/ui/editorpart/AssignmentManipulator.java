/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
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
		this.valueProvider = location.getReferenceValueProviderCache().getReferenceValueProvider(CargoPackage.eINSTANCE.getAssignableElement(), CargoPackage.eINSTANCE.getAssignableElement_Assignment());
	}

	private List<Pair<String, EObject>> getAllowedValues(final EObject target, List<Pair<String, EObject>> storage) {
		if (storage == null) {
			storage = new ArrayList<Pair<String, EObject>>();
		} else {
			storage.clear();
		}
		storage.addAll(valueProvider.getAllowedValues(target, CargoPackage.eINSTANCE.getAssignableElement_Assignment()));

		return storage;
	}

	@Override
	public void setValue(final Object object, final Object value) {
		// grar.
		AssignableElement target = getTarget(object);
		allowedValues = getAllowedValues(target, allowedValues);

		// locate the appropriate value in the list of options
		final AVesselSet<Vessel> set = (AVesselSet<Vessel>) (allowedValues.get((Integer) value).getSecond());
		final EditingDomain ed = location.getEditingDomain();

		ed.getCommandStack().execute(SetCommand.create(ed, target, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, set));
	}

	@Override
	public CellEditor getCellEditor(final Composite parent, final Object object) {
		allowedValues = getAllowedValues(getTarget(object), allowedValues);
		final String[] items = new String[allowedValues.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = allowedValues.get(i).getFirst();
		}
		return new ComboBoxCellEditor(parent, items);
	}

	@Override
	public Integer getValue(final Object object) {
		final AssignableElement assignableElement = getTarget(object);
		allowedValues = getAllowedValues((EObject) assignableElement, allowedValues);
		for (int i = 0; i < allowedValues.size(); i++) {
			if (Equality.isEqual(allowedValues.get(i).getSecond(), assignableElement.getAssignment())) {
				return i;
			}
		}

		return 0;
	}

	@Override
	public boolean canEdit(final Object object) {

		return getTarget(object) != null;
	}

	@Override
	public String render(final Object object) {

		final AssignableElement target = getTarget(object);
		if (target == null) {
			return "";
		}

		// get the VesselSet currently attached to the cargo
		final AVesselSet<? extends Vessel> vs = target.getAssignment();
		// by preference, find the string attached to this object by the value provider
		for (final Pair<String, EObject> pair : getAllowedValues(target, allowedValues)) {
			if (pair.getSecond() == vs) {
				return pair.getFirst();
			}
		}
		// if no string has been attached to the object, return a default string representation
		if (vs == null) {
			// this case should not occur, since the value provider should offer null as an option
			return "(Null)";
		} else {
			// fall back on displaying the VesselSet's name
			return vs.getName();
		}

		// This case can happen in e.g. the wiring editor row slot only rows
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		return render(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return render(object);
	}

	/**
	 * Returns the object we expect the ElementAssignment to be linked to.
	 * 
	 * @param object
	 * @return
	 */
	protected @Nullable
	AssignableElement getTarget(@Nullable final Object object) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			for (final Slot s : cargo.getSlots()) {
				if (s instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) s;
					if (loadSlot.isDESPurchase()) {
						return loadSlot;
					}
				} else if (s instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) s;
					if (dischargeSlot.isFOBSale()) {
						return dischargeSlot;
					}
				}
			}
			return cargo;
		} else if (object instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) object;
			if (loadSlot.isDESPurchase()) {
				return loadSlot;
			}
		} else if (object instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) object;
			if (dischargeSlot.isFOBSale()) {
				return dischargeSlot;
			}
		}
		return null;
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
		return null;
	}
}
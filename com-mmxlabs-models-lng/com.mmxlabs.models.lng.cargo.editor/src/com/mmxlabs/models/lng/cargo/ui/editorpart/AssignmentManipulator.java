/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

class AssignmentManipulator implements ICellRenderer, ICellManipulator {
	/**
	 * 
	 */
	private final IScenarioEditingLocation location;
	private final IReferenceValueProvider slotValueProvider;
	private final IReferenceValueProvider cargoValueProvider;
	private List<Pair<String, EObject>> allowedValues;
	private EReference reference;

	public AssignmentManipulator(final IScenarioEditingLocation location) {
		this.location = location;
		this.cargoValueProvider = location.getReferenceValueProviderCache().getReferenceValueProvider(CargoPackage.eINSTANCE.getAssignableElement(),
				CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType());
		this.slotValueProvider = location.getReferenceValueProviderCache().getReferenceValueProvider(CargoPackage.eINSTANCE.getSlot(), CargoPackage.eINSTANCE.getSlot_NominatedVessel());
	}

	private List<Pair<String, EObject>> getAllowedValues(final EObject target, List<Pair<String, EObject>> storage) {
		if (storage == null) {
			storage = new ArrayList<Pair<String, EObject>>();
		} else {
			storage.clear();
		}

		if (target instanceof Slot) {
			storage.addAll(slotValueProvider.getAllowedValues(target, CargoPackage.eINSTANCE.getSlot_NominatedVessel()));
			reference = CargoPackage.Literals.SLOT__NOMINATED_VESSEL;
		} else if (target instanceof AssignableElement) {
			storage.addAll(cargoValueProvider.getAllowedValues(target, CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType()));
			reference = CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE;
		} else {
			reference = null;
		}

		return storage;
	}

	@Override
	public boolean isValueUnset(Object object) {
		return false;
	}

	@Override
	public void setValue(final Object object, final Object value) {
		// grar.
		final EditingDomain ed = location.getEditingDomain();
		allowedValues = getAllowedValues((EObject) object, allowedValues);

		if (reference != null) {
			// locate the appropriate value in the list of options
			ed.getCommandStack().execute(SetCommand.create(ed, object, reference, allowedValues.get((Integer) value).getSecond()));
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

	@Override
	public Integer getValue(final Object object) {
		allowedValues = getAllowedValues((EObject) object, allowedValues);
		for (int i = 0; i < allowedValues.size(); i++) {
			if (Equality.isEqual(allowedValues.get(i).getSecond(), ((EObject) object).eGet(reference))) {
				return i;
			}
		}

		return 0;
	}

	@Override
	public boolean canEdit(final Object object) {

		return true;
	}

	@Override
	public String render(final Object object) {
		if (object == null) {
			return null;
		}

		// Refresh cache / references
		allowedValues = getAllowedValues((EObject) object, allowedValues);
		// get the VesselSet currently attached to the cargo
		final Object current = ((EObject) object).eGet(reference);
		// by preference, find the string attached to this object by the value provider
		for (final Pair<String, EObject> pair : allowedValues) {
			if (pair.getSecond() == current) {

				// HACKY BIT FOR NOW to show nominal allocation
				if (object instanceof Cargo) {
					Cargo cargo = (Cargo) object;
					if (cargo.getVesselAssignmentType() instanceof CharterInMarket) {
						if (cargo.getSpotIndex() == -1) {
							return pair.getFirst().replace("spot", "nominal");
						} else {
							return pair.getFirst().replace("spot", "market");
						}
					}
				}

				return pair.getFirst();
			}
		}
		// if no string has been attached to the object, return a default string representation
		if (current == null) {
			// this case should not occur, since the value provider should offer null as an option
			return "(Null)";
		} else {
			assert false;
			// fall back on displaying the VesselSet's name
			return "Unknown vessel assignment";
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

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
		return null;
	}
}
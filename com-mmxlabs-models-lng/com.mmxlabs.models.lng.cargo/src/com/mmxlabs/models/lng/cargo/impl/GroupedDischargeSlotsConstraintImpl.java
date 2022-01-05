/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.GroupedDischargeSlotsConstraint;

import com.mmxlabs.models.lng.commercial.SalesContract;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grouped Discharge Slots Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class GroupedDischargeSlotsConstraintImpl extends GroupedSlotsConstraintImpl<SalesContract, DischargeSlot> implements GroupedDischargeSlotsConstraint {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupedDischargeSlotsConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.GROUPED_DISCHARGE_SLOTS_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<DischargeSlot> getSlots() {
		if (slots == null) {
			slots = new EObjectResolvingEList<DischargeSlot>(DischargeSlot.class, this, CargoPackage.GROUPED_DISCHARGE_SLOTS_CONSTRAINT__SLOTS);
		}
		return slots;
	}

} //GroupedDischargeSlotsConstraintImpl

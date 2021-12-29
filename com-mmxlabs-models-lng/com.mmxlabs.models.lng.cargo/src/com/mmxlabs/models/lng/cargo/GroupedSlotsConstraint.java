/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.commercial.Contract;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grouped Slots Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint#getSlots <em>Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint#getMinimumBound <em>Minimum Bound</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getGroupedSlotsConstraint()
 * @model
 * @generated
 */
public interface GroupedSlotsConstraint<U extends Contract, T extends Slot<U>> extends EObject {
	/**
	 * Returns the value of the '<em><b>Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slots</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getGroupedSlotsConstraint_Slots()
	 * @model
	 * @generated
	 */
	EList<T> getSlots();

	/**
	 * Returns the value of the '<em><b>Minimum Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Bound</em>' attribute.
	 * @see #setMinimumBound(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getGroupedSlotsConstraint_MinimumBound()
	 * @model required="true"
	 * @generated
	 */
	int getMinimumBound();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint#getMinimumBound <em>Minimum Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Bound</em>' attribute.
	 * @see #getMinimumBound()
	 * @generated
	 */
	void setMinimumBound(int value);

} // GroupedSlotsConstraint

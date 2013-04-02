/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.util.CargoSlotSorter;
import com.mmxlabs.models.lng.types.ACargo;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getSlots <em>Slots</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo()
 * @model
 * @generated
 */
public interface Cargo extends ACargo {
	/**
	 * Returns the value of the '<em><b>Allow Rewiring</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allow Rewiring</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allow Rewiring</em>' attribute.
	 * @see #isSetAllowRewiring()
	 * @see #unsetAllowRewiring()
	 * @see #setAllowRewiring(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_AllowRewiring()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	boolean isAllowRewiring();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Allow Rewiring</em>' attribute.
	 * @see #isSetAllowRewiring()
	 * @see #unsetAllowRewiring()
	 * @see #isAllowRewiring()
	 * @generated
	 */
	void setAllowRewiring(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAllowRewiring()
	 * @see #isAllowRewiring()
	 * @see #setAllowRewiring(boolean)
	 * @generated
	 */
	void unsetAllowRewiring();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Allow Rewiring</em>' attribute is set.
	 * @see #unsetAllowRewiring()
	 * @see #isAllowRewiring()
	 * @see #setAllowRewiring(boolean)
	 * @generated
	 */
	boolean isSetAllowRewiring();

	/**
	 * Returns the value of the '<em><b>Allowed Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_AllowedVessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getAllowedVessels();

	/**
	 * Returns the value of the '<em><b>Slots</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Slot}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.Slot#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * The slots are all the slots linked to this {@link Cargo}. These could be {@link LoadSlot}s or {@link DischargeSlot}s
	 *  - or any other slot implementation. Note this list is unsorted. Clients of this method should date sort the slots if required.
	 *  See {@link #getSortedSlots()} or {@link CargoSlotSorter} to help with this.
	 * </p>
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slots</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_Slots()
	 * @see com.mmxlabs.models.lng.cargo.Slot#getCargo
	 * @model opposite="cargo"
	 * @generated
	 */
	EList<Slot> getSlots();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	CargoType getCargoType();

	/**
	 * <!-- begin-user-doc -->
	 * Returns date sorted copy of the {@link #getSlots()} {@link List}. A new list is created each time this method is called.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Slot> getSortedSlots();

} // end of  Cargo

// finish type fixing

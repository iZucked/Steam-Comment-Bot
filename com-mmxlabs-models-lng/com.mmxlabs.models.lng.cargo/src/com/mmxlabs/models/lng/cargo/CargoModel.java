/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getLoadSlots <em>Load Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getDischargeSlots <em>Discharge Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargos <em>Cargos</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel()
 * @model
 * @generated
 */
public interface CargoModel extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Load Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.LoadSlot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_LoadSlots()
	 * @model containment="true"
	 * @generated
	 */
	EList<LoadSlot> getLoadSlots();

	/**
	 * Returns the value of the '<em><b>Discharge Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.DischargeSlot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_DischargeSlots()
	 * @model containment="true"
	 * @generated
	 */
	EList<DischargeSlot> getDischargeSlots();

	/**
	 * Returns the value of the '<em><b>Cargos</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Cargo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargos</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargos</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_Cargos()
	 * @model containment="true"
	 * @generated
	 */
	EList<Cargo> getCargos();

} // end of  CargoModel

// finish type fixing

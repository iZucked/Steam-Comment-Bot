/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import com.mmxlabs.models.lng.types.ACargo;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getCargoType <em>Cargo Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo()
 * @model
 * @generated
 */
public interface Cargo extends ACargo {
	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_LoadSlot()
	 * @model required="true"
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#getLoadSlot <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot</em>' reference.
	 * @see #getLoadSlot()
	 * @generated
	 */
	void setLoadSlot(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' reference.
	 * @see #setDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_DischargeSlot()
	 * @model required="true"
	 * @generated
	 */
	DischargeSlot getDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Cargo Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.CargoType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @see #setCargoType(CargoType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_CargoType()
	 * @model required="true"
	 * @generated
	 */
	CargoType getCargoType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#getCargoType <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @see #getCargoType()
	 * @generated
	 */
	void setCargoType(CargoType value);

} // end of  Cargo

// finish type fixing

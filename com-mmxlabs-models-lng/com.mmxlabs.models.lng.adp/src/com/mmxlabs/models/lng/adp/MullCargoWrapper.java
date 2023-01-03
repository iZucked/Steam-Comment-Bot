/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mull Cargo Wrapper</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullCargoWrapper#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullCargoWrapper#getDischargeSlot <em>Discharge Slot</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullCargoWrapper()
 * @model
 * @generated
 */
public interface MullCargoWrapper extends EObject {
	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullCargoWrapper_LoadSlot()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MullCargoWrapper#getLoadSlot <em>Load Slot</em>}' reference.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' reference.
	 * @see #setDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullCargoWrapper_DischargeSlot()
	 * @model
	 * @generated
	 */
	DischargeSlot getDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MullCargoWrapper#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(DischargeSlot value);

} // MullCargoWrapper

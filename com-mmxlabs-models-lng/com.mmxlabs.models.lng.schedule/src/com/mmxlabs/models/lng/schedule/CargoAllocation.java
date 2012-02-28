/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.cargo.Cargo;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVisit <em>Load Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVisit <em>Discharge Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVolume <em>Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVolume <em>Discharge Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation()
 * @model
 * @generated
 */
public interface CargoAllocation extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Load Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Visit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Visit</em>' reference.
	 * @see #setLoadVisit(SlotVisit)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_LoadVisit()
	 * @model required="true"
	 * @generated
	 */
	SlotVisit getLoadVisit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVisit <em>Load Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Visit</em>' reference.
	 * @see #getLoadVisit()
	 * @generated
	 */
	void setLoadVisit(SlotVisit value);

	/**
	 * Returns the value of the '<em><b>Discharge Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Visit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Visit</em>' reference.
	 * @see #setDischargeVisit(SlotVisit)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_DischargeVisit()
	 * @model required="true"
	 * @generated
	 */
	SlotVisit getDischargeVisit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVisit <em>Discharge Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Visit</em>' reference.
	 * @see #getDischargeVisit()
	 * @generated
	 */
	void setDischargeVisit(SlotVisit value);

	/**
	 * Returns the value of the '<em><b>Load Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Volume</em>' attribute.
	 * @see #setLoadVolume(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_LoadVolume()
	 * @model required="true"
	 * @generated
	 */
	int getLoadVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVolume <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Volume</em>' attribute.
	 * @see #getLoadVolume()
	 * @generated
	 */
	void setLoadVolume(int value);

	/**
	 * Returns the value of the '<em><b>Discharge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Volume</em>' attribute.
	 * @see #setDischargeVolume(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_DischargeVolume()
	 * @model required="true"
	 * @generated
	 */
	int getDischargeVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVolume <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Volume</em>' attribute.
	 * @see #getDischargeVolume()
	 * @generated
	 */
	void setDischargeVolume(int value);

	/**
	 * Returns the value of the '<em><b>Input Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Cargo</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Cargo</em>' reference.
	 * @see #isSetInputCargo()
	 * @see #unsetInputCargo()
	 * @see #setInputCargo(Cargo)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_InputCargo()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Cargo getInputCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Cargo</em>' reference.
	 * @see #isSetInputCargo()
	 * @see #unsetInputCargo()
	 * @see #getInputCargo()
	 * @generated
	 */
	void setInputCargo(Cargo value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInputCargo()
	 * @see #getInputCargo()
	 * @see #setInputCargo(Cargo)
	 * @generated
	 */
	void unsetInputCargo();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Input Cargo</em>' reference is set.
	 * @see #unsetInputCargo()
	 * @see #getInputCargo()
	 * @see #setInputCargo(Cargo)
	 * @generated
	 */
	boolean isSetInputCargo();

} // end of  CargoAllocation

// finish type fixing

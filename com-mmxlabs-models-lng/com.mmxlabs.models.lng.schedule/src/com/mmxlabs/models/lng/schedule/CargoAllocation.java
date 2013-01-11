/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVolume <em>Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVolume <em>Discharge Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLadenLeg <em>Laden Leg</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getBallastLeg <em>Ballast Leg</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLadenIdle <em>Laden Idle</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getBallastIdle <em>Ballast Idle</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation()
 * @model
 * @generated
 */
public interface CargoAllocation extends MMXObject, AdditionalDataHolder, ExtraDataContainer {
	/**
	 * Returns the value of the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Allocation</em>' reference.
	 * @see #setLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_LoadAllocation()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadAllocation <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Allocation</em>' reference.
	 * @see #getLoadAllocation()
	 * @generated
	 */
	void setLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Allocation</em>' reference.
	 * @see #setDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_DischargeAllocation()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeAllocation <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Allocation</em>' reference.
	 * @see #getDischargeAllocation()
	 * @generated
	 */
	void setDischargeAllocation(SlotAllocation value);

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

	/**
	 * Returns the value of the '<em><b>Laden Leg</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Leg</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Leg</em>' reference.
	 * @see #setLadenLeg(Journey)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_LadenLeg()
	 * @model required="true"
	 * @generated
	 */
	Journey getLadenLeg();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLadenLeg <em>Laden Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Leg</em>' reference.
	 * @see #getLadenLeg()
	 * @generated
	 */
	void setLadenLeg(Journey value);

	/**
	 * Returns the value of the '<em><b>Ballast Leg</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Leg</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Leg</em>' reference.
	 * @see #setBallastLeg(Journey)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_BallastLeg()
	 * @model required="true"
	 * @generated
	 */
	Journey getBallastLeg();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getBallastLeg <em>Ballast Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Leg</em>' reference.
	 * @see #getBallastLeg()
	 * @generated
	 */
	void setBallastLeg(Journey value);

	/**
	 * Returns the value of the '<em><b>Laden Idle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Idle</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Idle</em>' reference.
	 * @see #setLadenIdle(Idle)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_LadenIdle()
	 * @model required="true"
	 * @generated
	 */
	Idle getLadenIdle();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLadenIdle <em>Laden Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Idle</em>' reference.
	 * @see #getLadenIdle()
	 * @generated
	 */
	void setLadenIdle(Idle value);

	/**
	 * Returns the value of the '<em><b>Ballast Idle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Idle</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Idle</em>' reference.
	 * @see #setBallastIdle(Idle)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_BallastIdle()
	 * @model required="true"
	 * @generated
	 */
	Idle getBallastIdle();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getBallastIdle <em>Ballast Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Idle</em>' reference.
	 * @see #getBallastIdle()
	 * @generated
	 */
	void setBallastIdle(Idle value);

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence</em>' reference.
	 * @see #isSetSequence()
	 * @see #unsetSequence()
	 * @see #setSequence(Sequence)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_Sequence()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Sequence getSequence();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' reference.
	 * @see #isSetSequence()
	 * @see #unsetSequence()
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(Sequence value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSequence()
	 * @see #getSequence()
	 * @see #setSequence(Sequence)
	 * @generated
	 */
	void unsetSequence();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Sequence</em>' reference is set.
	 * @see #unsetSequence()
	 * @see #getSequence()
	 * @see #setSequence(Sequence)
	 * @generated
	 */
	boolean isSetSequence();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getType();

} // end of  CargoAllocation

// finish type fixing

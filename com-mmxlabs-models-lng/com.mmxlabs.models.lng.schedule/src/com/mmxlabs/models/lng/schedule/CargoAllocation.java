/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSlotAllocations <em>Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getCargoType <em>Cargo Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getBallastBonusFee <em>Ballast Bonus Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getRepositioningFee <em>Repositioning Fee</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation()
 * @model
 * @generated
 */
public interface CargoAllocation extends MMXObject, ProfitAndLossContainer, EventGrouping {
	/**
	 * Returns the value of the '<em><b>Slot Allocations</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.SlotAllocation}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Allocations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Allocations</em>' reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_SlotAllocations()
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation
	 * @model opposite="cargoAllocation" required="true"
	 * @generated
	 */
	EList<SlotAllocation> getSlotAllocations();

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
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_CargoType()
	 * @model
	 * @generated
	 */
	CargoType getCargoType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getCargoType <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @see #getCargoType()
	 * @generated
	 */
	void setCargoType(CargoType value);

	/**
	 * Returns the value of the '<em><b>Ballast Bonus Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus Fee</em>' attribute.
	 * @see #setBallastBonusFee(long)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_BallastBonusFee()
	 * @model
	 * @generated
	 */
	long getBallastBonusFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getBallastBonusFee <em>Ballast Bonus Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus Fee</em>' attribute.
	 * @see #getBallastBonusFee()
	 * @generated
	 */
	void setBallastBonusFee(long value);

	/**
	 * Returns the value of the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #setRepositioningFee(long)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_RepositioningFee()
	 * @model
	 * @generated
	 */
	long getRepositioningFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #getRepositioningFee()
	 * @generated
	 */
	void setRepositioningFee(long value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

} // end of  CargoAllocation

// finish type fixing

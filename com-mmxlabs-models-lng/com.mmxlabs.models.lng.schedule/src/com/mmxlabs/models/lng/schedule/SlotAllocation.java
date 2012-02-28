/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation()
 * @model
 * @generated
 */
public interface SlotAllocation extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #isSetSlot()
	 * @see #unsetSlot()
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_Slot()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #isSetSlot()
	 * @see #unsetSlot()
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSlot()
	 * @see #getSlot()
	 * @see #setSlot(Slot)
	 * @generated
	 */
	void unsetSlot();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Slot</em>' reference is set.
	 * @see #unsetSlot()
	 * @see #getSlot()
	 * @see #setSlot(Slot)
	 * @generated
	 */
	boolean isSetSlot();

	/**
	 * Returns the value of the '<em><b>Spot Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Market</em>' reference.
	 * @see #isSetSpotMarket()
	 * @see #unsetSpotMarket()
	 * @see #setSpotMarket(ASpotMarket)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_SpotMarket()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	ASpotMarket getSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Market</em>' reference.
	 * @see #isSetSpotMarket()
	 * @see #unsetSpotMarket()
	 * @see #getSpotMarket()
	 * @generated
	 */
	void setSpotMarket(ASpotMarket value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSpotMarket()
	 * @see #getSpotMarket()
	 * @see #setSpotMarket(ASpotMarket)
	 * @generated
	 */
	void unsetSpotMarket();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Spot Market</em>' reference is set.
	 * @see #unsetSpotMarket()
	 * @see #getSpotMarket()
	 * @see #setSpotMarket(ASpotMarket)
	 * @generated
	 */
	boolean isSetSpotMarket();

} // end of  SlotAllocation

// finish type fixing

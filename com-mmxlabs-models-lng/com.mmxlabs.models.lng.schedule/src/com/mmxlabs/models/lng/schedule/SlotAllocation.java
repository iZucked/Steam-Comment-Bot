/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXObject;
import java.util.Calendar;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXObject;
import java.util.Calendar;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXObject;
import java.util.Calendar;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXObject;
import java.util.Calendar;
import java.util.Calendar;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.APort;
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
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeTransferred <em>Volume Transferred</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocation</em>' reference.
	 * @see #setCargoAllocation(CargoAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_CargoAllocation()
	 * @model required="true"
	 * @generated
	 */
	CargoAllocation getCargoAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Allocation</em>' reference.
	 * @see #getCargoAllocation()
	 * @generated
	 */
	void setCargoAllocation(CargoAllocation value);

	/**
	 * Returns the value of the '<em><b>Slot Visit</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.SlotVisit#getSlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Visit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Visit</em>' reference.
	 * @see #isSetSlotVisit()
	 * @see #unsetSlotVisit()
	 * @see #setSlotVisit(SlotVisit)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_SlotVisit()
	 * @see com.mmxlabs.models.lng.schedule.SlotVisit#getSlotAllocation
	 * @model opposite="slotAllocation" unsettable="true" required="true"
	 * @generated
	 */
	SlotVisit getSlotVisit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Visit</em>' reference.
	 * @see #isSetSlotVisit()
	 * @see #unsetSlotVisit()
	 * @see #getSlotVisit()
	 * @generated
	 */
	void setSlotVisit(SlotVisit value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSlotVisit()
	 * @see #getSlotVisit()
	 * @see #setSlotVisit(SlotVisit)
	 * @generated
	 */
	void unsetSlotVisit();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Slot Visit</em>' reference is set.
	 * @see #unsetSlotVisit()
	 * @see #getSlotVisit()
	 * @see #setSlotVisit(SlotVisit)
	 * @generated
	 */
	boolean isSetSlotVisit();

	/**
	 * Returns the value of the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Volume Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Transferred</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Transferred</em>' attribute.
	 * @see #setVolumeTransferred(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_VolumeTransferred()
	 * @model required="true"
	 * @generated
	 */
	int getVolumeTransferred();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeTransferred <em>Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Transferred</em>' attribute.
	 * @see #getVolumeTransferred()
	 * @generated
	 */
	void setVolumeTransferred(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	APort getPort();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.lng.schedule.Calendar" required="true"
	 * @generated
	 */
	Calendar getLocalStart();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.lng.schedule.Calendar" required="true"
	 * @generated
	 */
	Calendar getLocalEnd();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	AContract getContract();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

} // end of  SlotAllocation

// finish type fixing

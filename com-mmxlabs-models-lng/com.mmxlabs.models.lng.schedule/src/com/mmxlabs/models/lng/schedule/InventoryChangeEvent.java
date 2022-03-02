/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.cargo.InventoryEventRow;

import java.time.LocalDateTime;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Change Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getChangeQuantity <em>Change Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getCurrentLevel <em>Current Level</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getCurrentMin <em>Current Min</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getCurrentMax <em>Current Max</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getEvent <em>Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getSlotAllocation <em>Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getOpenSlotAllocation <em>Open Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#isBreachedMin <em>Breached Min</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#isBreachedMax <em>Breached Max</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent()
 * @model
 * @generated
 */
public interface InventoryChangeEvent extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_Date()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(LocalDateTime value);

	/**
	 * Returns the value of the '<em><b>Change Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Quantity</em>' attribute.
	 * @see #setChangeQuantity(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_ChangeQuantity()
	 * @model
	 * @generated
	 */
	int getChangeQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getChangeQuantity <em>Change Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Change Quantity</em>' attribute.
	 * @see #getChangeQuantity()
	 * @generated
	 */
	void setChangeQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Current Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Level</em>' attribute.
	 * @see #setCurrentLevel(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_CurrentLevel()
	 * @model
	 * @generated
	 */
	int getCurrentLevel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getCurrentLevel <em>Current Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Level</em>' attribute.
	 * @see #getCurrentLevel()
	 * @generated
	 */
	void setCurrentLevel(int value);

	/**
	 * Returns the value of the '<em><b>Current Min</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Min</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Min</em>' attribute.
	 * @see #setCurrentMin(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_CurrentMin()
	 * @model
	 * @generated
	 */
	int getCurrentMin();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getCurrentMin <em>Current Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Min</em>' attribute.
	 * @see #getCurrentMin()
	 * @generated
	 */
	void setCurrentMin(int value);

	/**
	 * Returns the value of the '<em><b>Current Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Max</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Max</em>' attribute.
	 * @see #setCurrentMax(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_CurrentMax()
	 * @model
	 * @generated
	 */
	int getCurrentMax();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getCurrentMax <em>Current Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Max</em>' attribute.
	 * @see #getCurrentMax()
	 * @generated
	 */
	void setCurrentMax(int value);

	/**
	 * Returns the value of the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event</em>' reference.
	 * @see #setEvent(InventoryEventRow)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_Event()
	 * @model
	 * @generated
	 */
	InventoryEventRow getEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getEvent <em>Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event</em>' reference.
	 * @see #getEvent()
	 * @generated
	 */
	void setEvent(InventoryEventRow value);

	/**
	 * Returns the value of the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Allocation</em>' reference.
	 * @see #setSlotAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_SlotAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getSlotAllocation <em>Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Allocation</em>' reference.
	 * @see #getSlotAllocation()
	 * @generated
	 */
	void setSlotAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Open Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open Slot Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Slot Allocation</em>' reference.
	 * @see #setOpenSlotAllocation(OpenSlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_OpenSlotAllocation()
	 * @model
	 * @generated
	 */
	OpenSlotAllocation getOpenSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#getOpenSlotAllocation <em>Open Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open Slot Allocation</em>' reference.
	 * @see #getOpenSlotAllocation()
	 * @generated
	 */
	void setOpenSlotAllocation(OpenSlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Breached Min</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Breached Min</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Breached Min</em>' attribute.
	 * @see #setBreachedMin(boolean)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_BreachedMin()
	 * @model
	 * @generated
	 */
	boolean isBreachedMin();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#isBreachedMin <em>Breached Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Breached Min</em>' attribute.
	 * @see #isBreachedMin()
	 * @generated
	 */
	void setBreachedMin(boolean value);

	/**
	 * Returns the value of the '<em><b>Breached Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Breached Max</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Breached Max</em>' attribute.
	 * @see #setBreachedMax(boolean)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryChangeEvent_BreachedMax()
	 * @model
	 * @generated
	 */
	boolean isBreachedMax();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent#isBreachedMax <em>Breached Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Breached Max</em>' attribute.
	 * @see #isBreachedMax()
	 * @generated
	 */
	void setBreachedMax(boolean value);

} // InventoryChangeEvent

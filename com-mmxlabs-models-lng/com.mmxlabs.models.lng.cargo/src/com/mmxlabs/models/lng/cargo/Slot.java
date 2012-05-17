/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import com.mmxlabs.models.lng.types.ASlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.port.Port;

import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getFixedPrice <em>Fixed Price</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot()
 * @model abstract="true"
 * @generated
 */
public interface Slot extends ASlot, ITimezoneProvider {
	/**
	 * Returns the value of the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Start</em>' attribute.
	 * @see #setWindowStart(Date)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowStart()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/lng/ui/datetime showTime='false'"
	 * @generated
	 */
	Date getWindowStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStart <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Start</em>' attribute.
	 * @see #getWindowStart()
	 * @generated
	 */
	void setWindowStart(Date value);

	/**
	 * Returns the value of the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Start Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Start Time</em>' attribute.
	 * @see #isSetWindowStartTime()
	 * @see #unsetWindowStartTime()
	 * @see #setWindowStartTime(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowStartTime()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getWindowStartTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Start Time</em>' attribute.
	 * @see #isSetWindowStartTime()
	 * @see #unsetWindowStartTime()
	 * @see #getWindowStartTime()
	 * @generated
	 */
	void setWindowStartTime(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetWindowStartTime()
	 * @see #getWindowStartTime()
	 * @see #setWindowStartTime(int)
	 * @generated
	 */
	void unsetWindowStartTime();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Window Start Time</em>' attribute is set.
	 * @see #unsetWindowStartTime()
	 * @see #getWindowStartTime()
	 * @see #setWindowStartTime(int)
	 * @generated
	 */
	boolean isSetWindowStartTime();

	/**
	 * Returns the value of the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size</em>' attribute.
	 * @see #isSetWindowSize()
	 * @see #unsetWindowSize()
	 * @see #setWindowSize(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowSize()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getWindowSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size</em>' attribute.
	 * @see #isSetWindowSize()
	 * @see #unsetWindowSize()
	 * @see #getWindowSize()
	 * @generated
	 */
	void setWindowSize(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetWindowSize()
	 * @see #getWindowSize()
	 * @see #setWindowSize(int)
	 * @generated
	 */
	void unsetWindowSize();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Window Size</em>' attribute is set.
	 * @see #unsetWindowSize()
	 * @see #getWindowSize()
	 * @see #setWindowSize(int)
	 * @generated
	 */
	boolean isSetWindowSize();

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Port()
	 * @model required="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(Contract)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Contract()
	 * @model required="true"
	 * @generated
	 */
	Contract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(Contract value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #isSetDuration()
	 * @see #unsetDuration()
	 * @see #setDuration(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Duration()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #isSetDuration()
	 * @see #unsetDuration()
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDuration()
	 * @see #getDuration()
	 * @see #setDuration(int)
	 * @generated
	 */
	void unsetDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Duration</em>' attribute is set.
	 * @see #unsetDuration()
	 * @see #getDuration()
	 * @see #setDuration(int)
	 * @generated
	 */
	boolean isSetDuration();

	/**
	 * Returns the value of the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Quantity</em>' attribute.
	 * @see #isSetMinQuantity()
	 * @see #unsetMinQuantity()
	 * @see #setMinQuantity(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_MinQuantity()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getMinQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #isSetMinQuantity()
	 * @see #unsetMinQuantity()
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinQuantity()
	 * @see #getMinQuantity()
	 * @see #setMinQuantity(int)
	 * @generated
	 */
	void unsetMinQuantity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Quantity</em>' attribute is set.
	 * @see #unsetMinQuantity()
	 * @see #getMinQuantity()
	 * @see #setMinQuantity(int)
	 * @generated
	 */
	boolean isSetMinQuantity();

	/**
	 * Returns the value of the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Quantity</em>' attribute.
	 * @see #isSetMaxQuantity()
	 * @see #unsetMaxQuantity()
	 * @see #setMaxQuantity(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_MaxQuantity()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getMaxQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #isSetMaxQuantity()
	 * @see #unsetMaxQuantity()
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxQuantity()
	 * @see #getMaxQuantity()
	 * @see #setMaxQuantity(int)
	 * @generated
	 */
	void unsetMaxQuantity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Quantity</em>' attribute is set.
	 * @see #unsetMaxQuantity()
	 * @see #getMaxQuantity()
	 * @see #setMaxQuantity(int)
	 * @generated
	 */
	boolean isSetMaxQuantity();

	/**
	 * Returns the value of the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fixed Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed Price</em>' attribute.
	 * @see #isSetFixedPrice()
	 * @see #unsetFixedPrice()
	 * @see #setFixedPrice(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_FixedPrice()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	double getFixedPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getFixedPrice <em>Fixed Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fixed Price</em>' attribute.
	 * @see #isSetFixedPrice()
	 * @see #unsetFixedPrice()
	 * @see #getFixedPrice()
	 * @generated
	 */
	void setFixedPrice(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getFixedPrice <em>Fixed Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFixedPrice()
	 * @see #getFixedPrice()
	 * @see #setFixedPrice(double)
	 * @generated
	 */
	void unsetFixedPrice();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getFixedPrice <em>Fixed Price</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fixed Price</em>' attribute is set.
	 * @see #unsetFixedPrice()
	 * @see #getFixedPrice()
	 * @see #setFixedPrice(double)
	 * @generated
	 */
	boolean isSetFixedPrice();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrPortDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrContractMinQuantity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrContractMaxQuantity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Date getWindowEndWithSlotOrPortTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Date getWindowStartWithSlotOrPortTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getSlotOrPortWindowSize();

} // end of  Slot

// finish type fixing

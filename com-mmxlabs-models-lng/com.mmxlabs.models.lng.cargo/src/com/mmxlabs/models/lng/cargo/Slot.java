/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import java.util.Date;

import org.eclipse.emf.common.util.EList;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isOptional <em>Optional</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getCargo <em>Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPricingEvent <em>Pricing Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPricingDate <em>Pricing Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isDivertible <em>Divertible</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getShippingDaysRestriction <em>Shipping Days Restriction</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedContracts <em>Restricted Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedPorts <em>Restricted Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getHedges <em>Hedges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationFee <em>Cancellation Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isOverrideRestrictions <em>Override Restrictions</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot()
 * @model abstract="true"
 * @generated
 */
public interface Slot extends UUIDObject, NamedObject, ITimezoneProvider, AssignableElement {
	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #isSetContract()
	 * @see #unsetContract()
	 * @see #setContract(Contract)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Contract()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Contract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #isSetContract()
	 * @see #unsetContract()
	 * @see #getContract()
	 * @generated
	 */
	void setContract(Contract value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetContract()
	 * @see #getContract()
	 * @see #setContract(Contract)
	 * @generated
	 */
	void unsetContract();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Contract</em>' reference is set.
	 * @see #unsetContract()
	 * @see #getContract()
	 * @see #setContract(Contract)
	 * @generated
	 */
	boolean isSetContract();

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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
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
//	Contract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
//	void setContract(Contract value);

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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0'"
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
	 * The default value is <code>"140000"</code>.
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
	 * @model default="140000" unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0'"
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
	 * Returns the value of the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optional</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optional</em>' attribute.
	 * @see #setOptional(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Optional()
	 * @model
	 * @generated
	 */
	boolean isOptional();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isOptional <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optional</em>' attribute.
	 * @see #isOptional()
	 * @generated
	 */
	void setOptional(boolean value);

	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #isSetPriceExpression()
	 * @see #unsetPriceExpression()
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PriceExpression()
	 * @model unsettable="true"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #isSetPriceExpression()
	 * @see #unsetPriceExpression()
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPriceExpression()
	 * @see #getPriceExpression()
	 * @see #setPriceExpression(String)
	 * @generated
	 */
	void unsetPriceExpression();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPriceExpression <em>Price Expression</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Price Expression</em>' attribute is set.
	 * @see #unsetPriceExpression()
	 * @see #getPriceExpression()
	 * @see #setPriceExpression(String)
	 * @generated
	 */
	boolean isSetPriceExpression();

	/**
	 * Returns the value of the '<em><b>Cargo</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.Cargo#getSlots <em>Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo</em>' reference.
	 * @see #setCargo(Cargo)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Cargo()
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getSlots
	 * @model opposite="slots"
	 * @generated
	 */
	Cargo getCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCargo <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo</em>' reference.
	 * @see #getCargo()
	 * @generated
	 */
	void setCargo(Cargo value);

	/**
	 * Returns the value of the '<em><b>Pricing Event</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.commercial.PricingEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pricing Event</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Event</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @see #isSetPricingEvent()
	 * @see #unsetPricingEvent()
	 * @see #setPricingEvent(PricingEvent)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PricingEvent()
	 * @model unsettable="true"
	 * @generated
	 */
	PricingEvent getPricingEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingEvent <em>Pricing Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Event</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @see #isSetPricingEvent()
	 * @see #unsetPricingEvent()
	 * @see #getPricingEvent()
	 * @generated
	 */
	void setPricingEvent(PricingEvent value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingEvent <em>Pricing Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPricingEvent()
	 * @see #getPricingEvent()
	 * @see #setPricingEvent(PricingEvent)
	 * @generated
	 */
	void unsetPricingEvent();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingEvent <em>Pricing Event</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Pricing Event</em>' attribute is set.
	 * @see #unsetPricingEvent()
	 * @see #getPricingEvent()
	 * @see #setPricingEvent(PricingEvent)
	 * @generated
	 */
	boolean isSetPricingEvent();

	/**
	 * Returns the value of the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pricing Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Date</em>' attribute.
	 * @see #isSetPricingDate()
	 * @see #unsetPricingDate()
	 * @see #setPricingDate(Date)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PricingDate()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getPricingDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingDate <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
 	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Date</em>' attribute.
	 * @see #isSetPricingDate()
	 * @see #unsetPricingDate()
	 * @see #getPricingDate()
	 * @generated
	 */
	void setPricingDate(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingDate <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPricingDate()
	 * @see #getPricingDate()
	 * @see #setPricingDate(Date)
	 * @generated
	 */
	void unsetPricingDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingDate <em>Pricing Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Pricing Date</em>' attribute is set.
	 * @see #unsetPricingDate()
	 * @see #getPricingDate()
	 * @see #setPricingDate(Date)
	 * @generated
	 */
	boolean isSetPricingDate();

	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Notes()
	 * @model
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

	/**
	 * Returns the value of the '<em><b>Divertible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Divertible</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Divertible</em>' attribute.
	 * @see #setDivertible(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Divertible()
	 * @model
	 * @generated
	 */
	boolean isDivertible();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isDivertible <em>Divertible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Divertible</em>' attribute.
	 * @see #isDivertible()
	 * @generated
	 */
	void setDivertible(boolean value);

	/**
	 * Returns the value of the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Days Restriction</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Days Restriction</em>' attribute.
	 * @see #setShippingDaysRestriction(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_ShippingDaysRestriction()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='###'"
	 * @generated
	 */
	int getShippingDaysRestriction();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getShippingDaysRestriction <em>Shipping Days Restriction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Days Restriction</em>' attribute.
	 * @see #getShippingDaysRestriction()
	 * @generated
	 */
	void setShippingDaysRestriction(int value);

	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #isSetEntity()
	 * @see #unsetEntity()
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Entity()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #isSetEntity()
	 * @see #unsetEntity()
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEntity()
	 * @see #getEntity()
	 * @see #setEntity(BaseLegalEntity)
	 * @generated
	 */
	void unsetEntity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getEntity <em>Entity</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Entity</em>' reference is set.
	 * @see #unsetEntity()
	 * @see #getEntity()
	 * @see #setEntity(BaseLegalEntity)
	 * @generated
	 */
	boolean isSetEntity();

	/**
	 * Returns the value of the '<em><b>Restricted Contracts</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.Contract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Contracts</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Contracts</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedContracts()
	 * @model
	 * @generated
	 */
	EList<Contract> getRestrictedContracts();

	/**
	 * Returns the value of the '<em><b>Restricted Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.Port}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedPorts()
	 * @model
	 * @generated
	 */
	EList<Port> getRestrictedPorts();

	/**
	 * Returns the value of the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Lists Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Lists Are Permissive</em>' attribute.
	 * @see #setRestrictedListsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedListsArePermissive()
	 * @model
	 * @generated
	 */
	boolean isRestrictedListsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Lists Are Permissive</em>' attribute.
	 * @see #isRestrictedListsArePermissive()
	 * @generated
	 */
	void setRestrictedListsArePermissive(boolean value);

	/**
	 * Returns the value of the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hedges</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hedges</em>' attribute.
	 * @see #setHedges(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Hedges()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='-###,###,##0'"
	 * @generated
	 */
	int getHedges();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getHedges <em>Hedges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hedges</em>' attribute.
	 * @see #getHedges()
	 * @generated
	 */
	void setHedges(int value);

	/**
	 * Returns the value of the '<em><b>Allowed Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}&lt;com.mmxlabs.models.lng.fleet.Vessel>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_AllowedVessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getAllowedVessels();

	/**
	 * Returns the value of the '<em><b>Cancellation Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cancellation Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cancellation Fee</em>' attribute.
	 * @see #isSetCancellationFee()
	 * @see #unsetCancellationFee()
	 * @see #setCancellationFee(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_CancellationFee()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='###,###,##0'"
	 * @generated
	 */
	int getCancellationFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationFee <em>Cancellation Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cancellation Fee</em>' attribute.
	 * @see #isSetCancellationFee()
	 * @see #unsetCancellationFee()
	 * @see #getCancellationFee()
	 * @generated
	 */
	void setCancellationFee(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationFee <em>Cancellation Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCancellationFee()
	 * @see #getCancellationFee()
	 * @see #setCancellationFee(int)
	 * @generated
	 */
	void unsetCancellationFee();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationFee <em>Cancellation Fee</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cancellation Fee</em>' attribute is set.
	 * @see #unsetCancellationFee()
	 * @see #getCancellationFee()
	 * @see #setCancellationFee(int)
	 * @generated
	 */
	boolean isSetCancellationFee();

	/**
	 * Returns the value of the '<em><b>Override Restrictions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Override Restrictions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Override Restrictions</em>' attribute.
	 * @see #setOverrideRestrictions(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_OverrideRestrictions()
	 * @model
	 * @generated
	 */
	boolean isOverrideRestrictions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isOverrideRestrictions <em>Override Restrictions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Override Restrictions</em>' attribute.
	 * @see #isOverrideRestrictions()
	 * @generated
	 */
	void setOverrideRestrictions(boolean value);

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

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	BaseLegalEntity getSlotOrDelegatedEntity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Contract> getSlotOrContractRestrictedContracts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Port> getSlotOrContractRestrictedPorts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrContractRestrictedListsArePermissive();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrContractCancellationFee();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	PricingEvent getSlotOrDelegatedPricingEvent();

} // end of  Slot

// finish type fixing

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getCn <em>Cn</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSizeUnits <em>Window Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowFlex <em>Window Flex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowFlexUnits <em>Window Flex Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeLimitsUnit <em>Volume Limits Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getOperationalTolerance <em>Operational Tolerance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isFullCargoLot <em>Full Cargo Lot</em>}</li>
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
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsOverride <em>Restricted Contracts Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedPorts <em>Restricted Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsOverride <em>Restricted Ports Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedVessels <em>Restricted Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsOverride <em>Restricted Vessels Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedSlots <em>Restricted Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedSlotsArePermissive <em>Restricted Slots Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getHedges <em>Hedges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMiscCosts <em>Misc Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getNominatedVessel <em>Nominated Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isCancelled <em>Cancelled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isWindowCounterParty <em>Window Counter Party</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot()
 * @model abstract="true"
 *        annotation="http://www.mmxlabs.com/models/featureOverride"
 * @generated
 */
public interface Slot<T extends Contract> extends UUIDObject, NamedObject, ITimezoneProvider {
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
	T getContract();

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
	void setContract(T value);

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
	 * Returns the value of the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Counterparty</em>' attribute.
	 * @see #isSetCounterparty()
	 * @see #unsetCounterparty()
	 * @see #setCounterparty(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Counterparty()
	 * @model unsettable="true"
	 * @generated
	 */
	String getCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCounterparty <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Counterparty</em>' attribute.
	 * @see #isSetCounterparty()
	 * @see #unsetCounterparty()
	 * @see #getCounterparty()
	 * @generated
	 */
	void setCounterparty(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCounterparty <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCounterparty()
	 * @see #getCounterparty()
	 * @see #setCounterparty(String)
	 * @generated
	 */
	void unsetCounterparty();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCounterparty <em>Counterparty</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Counterparty</em>' attribute is set.
	 * @see #unsetCounterparty()
	 * @see #getCounterparty()
	 * @see #setCounterparty(String)
	 * @generated
	 */
	boolean isSetCounterparty();

	/**
	 * Returns the value of the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cn</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cn</em>' attribute.
	 * @see #isSetCn()
	 * @see #unsetCn()
	 * @see #setCn(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Cn()
	 * @model unsettable="true"
	 * @generated
	 */
	String getCn();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCn <em>Cn</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cn</em>' attribute.
	 * @see #isSetCn()
	 * @see #unsetCn()
	 * @see #getCn()
	 * @generated
	 */
	void setCn(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCn <em>Cn</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCn()
	 * @see #getCn()
	 * @see #setCn(String)
	 * @generated
	 */
	void unsetCn();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCn <em>Cn</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cn</em>' attribute is set.
	 * @see #unsetCn()
	 * @see #getCn()
	 * @see #setCn(String)
	 * @generated
	 */
	boolean isSetCn();

	/**
	 * Returns the value of the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Start</em>' attribute.
	 * @see #setWindowStart(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowStart()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getWindowStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStart <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Start</em>' attribute.
	 * @see #getWindowStart()
	 * @generated
	 */
	void setWindowStart(LocalDate value);

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
	 * Returns the value of the '<em><b>Window Size Units</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #isSetWindowSizeUnits()
	 * @see #unsetWindowSizeUnits()
	 * @see #setWindowSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowSizeUnits()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
	 * @generated
	 */
	TimePeriod getWindowSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSizeUnits <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #isSetWindowSizeUnits()
	 * @see #unsetWindowSizeUnits()
	 * @see #getWindowSizeUnits()
	 * @generated
	 */
	void setWindowSizeUnits(TimePeriod value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSizeUnits <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetWindowSizeUnits()
	 * @see #getWindowSizeUnits()
	 * @see #setWindowSizeUnits(TimePeriod)
	 * @generated
	 */
	void unsetWindowSizeUnits();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSizeUnits <em>Window Size Units</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Window Size Units</em>' attribute is set.
	 * @see #unsetWindowSizeUnits()
	 * @see #getWindowSizeUnits()
	 * @see #setWindowSizeUnits(TimePeriod)
	 * @generated
	 */
	boolean isSetWindowSizeUnits();

	/**
	 * Returns the value of the '<em><b>Window Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Flex</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Flex</em>' attribute.
	 * @see #setWindowFlex(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowFlex()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='-##0'"
	 * @generated
	 */
	int getWindowFlex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowFlex <em>Window Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Flex</em>' attribute.
	 * @see #getWindowFlex()
	 * @generated
	 */
	void setWindowFlex(int value);

	/**
	 * Returns the value of the '<em><b>Window Flex Units</b></em>' attribute.
	 * The default value is <code>"HOURS"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Flex Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Flex Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setWindowFlexUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowFlexUnits()
	 * @model default="HOURS" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
	 * @generated
	 */
	TimePeriod getWindowFlexUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowFlexUnits <em>Window Flex Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Flex Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getWindowFlexUnits()
	 * @generated
	 */
	void setWindowFlexUnits(TimePeriod value);

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
	 * Returns the value of the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.VolumeUnits}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Limits Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Limits Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #isSetVolumeLimitsUnit()
	 * @see #unsetVolumeLimitsUnit()
	 * @see #setVolumeLimitsUnit(VolumeUnits)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VolumeLimitsUnit()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	VolumeUnits getVolumeLimitsUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeLimitsUnit <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Limits Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #isSetVolumeLimitsUnit()
	 * @see #unsetVolumeLimitsUnit()
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 */
	void setVolumeLimitsUnit(VolumeUnits value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeLimitsUnit <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVolumeLimitsUnit()
	 * @see #getVolumeLimitsUnit()
	 * @see #setVolumeLimitsUnit(VolumeUnits)
	 * @generated
	 */
	void unsetVolumeLimitsUnit();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeLimitsUnit <em>Volume Limits Unit</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Volume Limits Unit</em>' attribute is set.
	 * @see #unsetVolumeLimitsUnit()
	 * @see #getVolumeLimitsUnit()
	 * @see #setVolumeLimitsUnit(VolumeUnits)
	 * @generated
	 */
	boolean isSetVolumeLimitsUnit();

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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#,###,##0'"
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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#,###,##0'"
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
	 * Returns the value of the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operational Tolerance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operational Tolerance</em>' attribute.
	 * @see #isSetOperationalTolerance()
	 * @see #unsetOperationalTolerance()
	 * @see #setOperationalTolerance(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_OperationalTolerance()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='##0.#' unit='%' exportFormatString='#.###' unitPrefix='\261'"
	 * @generated
	 */
	double getOperationalTolerance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getOperationalTolerance <em>Operational Tolerance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operational Tolerance</em>' attribute.
	 * @see #isSetOperationalTolerance()
	 * @see #unsetOperationalTolerance()
	 * @see #getOperationalTolerance()
	 * @generated
	 */
	void setOperationalTolerance(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getOperationalTolerance <em>Operational Tolerance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetOperationalTolerance()
	 * @see #getOperationalTolerance()
	 * @see #setOperationalTolerance(double)
	 * @generated
	 */
	void unsetOperationalTolerance();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getOperationalTolerance <em>Operational Tolerance</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Operational Tolerance</em>' attribute is set.
	 * @see #unsetOperationalTolerance()
	 * @see #getOperationalTolerance()
	 * @see #setOperationalTolerance(double)
	 * @generated
	 */
	boolean isSetOperationalTolerance();

	/**
	 * Returns the value of the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Full Cargo Lot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Full Cargo Lot</em>' attribute.
	 * @see #isSetFullCargoLot()
	 * @see #unsetFullCargoLot()
	 * @see #setFullCargoLot(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_FullCargoLot()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isFullCargoLot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isFullCargoLot <em>Full Cargo Lot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Full Cargo Lot</em>' attribute.
	 * @see #isSetFullCargoLot()
	 * @see #unsetFullCargoLot()
	 * @see #isFullCargoLot()
	 * @generated
	 */
	void setFullCargoLot(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isFullCargoLot <em>Full Cargo Lot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFullCargoLot()
	 * @see #isFullCargoLot()
	 * @see #setFullCargoLot(boolean)
	 * @generated
	 */
	void unsetFullCargoLot();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isFullCargoLot <em>Full Cargo Lot</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Full Cargo Lot</em>' attribute is set.
	 * @see #unsetFullCargoLot()
	 * @see #isFullCargoLot()
	 * @see #setFullCargoLot(boolean)
	 * @generated
	 */
	boolean isSetFullCargoLot();

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
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
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
	 * @see #setPricingDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PricingDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPricingDate();

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
	void setPricingDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingDate <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPricingDate()
	 * @see #getPricingDate()
	 * @see #setPricingDate(LocalDate)
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
	 * @see #setPricingDate(LocalDate)
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
	 * @see #isSetDivertible()
	 * @see #unsetDivertible()
	 * @see #setDivertible(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Divertible()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isDivertible();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isDivertible <em>Divertible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Divertible</em>' attribute.
	 * @see #isSetDivertible()
	 * @see #unsetDivertible()
	 * @see #isDivertible()
	 * @generated
	 */
	void setDivertible(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isDivertible <em>Divertible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDivertible()
	 * @see #isDivertible()
	 * @see #setDivertible(boolean)
	 * @generated
	 */
	void unsetDivertible();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isDivertible <em>Divertible</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Divertible</em>' attribute is set.
	 * @see #unsetDivertible()
	 * @see #isDivertible()
	 * @see #setDivertible(boolean)
	 * @generated
	 */
	boolean isSetDivertible();

	/**
	 * Returns the value of the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Days Restriction</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Days Restriction</em>' attribute.
	 * @see #isSetShippingDaysRestriction()
	 * @see #unsetShippingDaysRestriction()
	 * @see #setShippingDaysRestriction(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_ShippingDaysRestriction()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='###'"
	 * @generated
	 */
	int getShippingDaysRestriction();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getShippingDaysRestriction <em>Shipping Days Restriction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Days Restriction</em>' attribute.
	 * @see #isSetShippingDaysRestriction()
	 * @see #unsetShippingDaysRestriction()
	 * @see #getShippingDaysRestriction()
	 * @generated
	 */
	void setShippingDaysRestriction(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getShippingDaysRestriction <em>Shipping Days Restriction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetShippingDaysRestriction()
	 * @see #getShippingDaysRestriction()
	 * @see #setShippingDaysRestriction(int)
	 * @generated
	 */
	void unsetShippingDaysRestriction();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getShippingDaysRestriction <em>Shipping Days Restriction</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Shipping Days Restriction</em>' attribute is set.
	 * @see #unsetShippingDaysRestriction()
	 * @see #getShippingDaysRestriction()
	 * @see #setShippingDaysRestriction(int)
	 * @generated
	 */
	boolean isSetShippingDaysRestriction();

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
	 * Returns the value of the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Contracts Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Contracts Are Permissive</em>' attribute.
	 * @see #isSetRestrictedContractsArePermissive()
	 * @see #unsetRestrictedContractsArePermissive()
	 * @see #setRestrictedContractsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedContractsArePermissive()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isRestrictedContractsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Contracts Are Permissive</em>' attribute.
	 * @see #isSetRestrictedContractsArePermissive()
	 * @see #unsetRestrictedContractsArePermissive()
	 * @see #isRestrictedContractsArePermissive()
	 * @generated
	 */
	void setRestrictedContractsArePermissive(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRestrictedContractsArePermissive()
	 * @see #isRestrictedContractsArePermissive()
	 * @see #setRestrictedContractsArePermissive(boolean)
	 * @generated
	 */
	void unsetRestrictedContractsArePermissive();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Restricted Contracts Are Permissive</em>' attribute is set.
	 * @see #unsetRestrictedContractsArePermissive()
	 * @see #isRestrictedContractsArePermissive()
	 * @see #setRestrictedContractsArePermissive(boolean)
	 * @generated
	 */
	boolean isSetRestrictedContractsArePermissive();

	/**
	 * Returns the value of the '<em><b>Restricted Contracts Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Contracts Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Contracts Override</em>' attribute.
	 * @see #setRestrictedContractsOverride(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedContractsOverride()
	 * @model
	 * @generated
	 */
	boolean isRestrictedContractsOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsOverride <em>Restricted Contracts Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Contracts Override</em>' attribute.
	 * @see #isRestrictedContractsOverride()
	 * @generated
	 */
	void setRestrictedContractsOverride(boolean value);

	/**
	 * Returns the value of the '<em><b>Restricted Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
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
	EList<APortSet<Port>> getRestrictedPorts();

	/**
	 * Returns the value of the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Ports Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Ports Are Permissive</em>' attribute.
	 * @see #isSetRestrictedPortsArePermissive()
	 * @see #unsetRestrictedPortsArePermissive()
	 * @see #setRestrictedPortsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedPortsArePermissive()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isRestrictedPortsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Ports Are Permissive</em>' attribute.
	 * @see #isSetRestrictedPortsArePermissive()
	 * @see #unsetRestrictedPortsArePermissive()
	 * @see #isRestrictedPortsArePermissive()
	 * @generated
	 */
	void setRestrictedPortsArePermissive(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRestrictedPortsArePermissive()
	 * @see #isRestrictedPortsArePermissive()
	 * @see #setRestrictedPortsArePermissive(boolean)
	 * @generated
	 */
	void unsetRestrictedPortsArePermissive();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Restricted Ports Are Permissive</em>' attribute is set.
	 * @see #unsetRestrictedPortsArePermissive()
	 * @see #isRestrictedPortsArePermissive()
	 * @see #setRestrictedPortsArePermissive(boolean)
	 * @generated
	 */
	boolean isSetRestrictedPortsArePermissive();

	/**
	 * Returns the value of the '<em><b>Restricted Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Ports Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Ports Override</em>' attribute.
	 * @see #setRestrictedPortsOverride(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedPortsOverride()
	 * @model
	 * @generated
	 */
	boolean isRestrictedPortsOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsOverride <em>Restricted Ports Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Ports Override</em>' attribute.
	 * @see #isRestrictedPortsOverride()
	 * @generated
	 */
	void setRestrictedPortsOverride(boolean value);

	/**
	 * Returns the value of the '<em><b>Restricted Slots</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Slot}<code>&lt;?&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Slots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Slots</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedSlots()
	 * @model
	 * @generated
	 */
	EList<Slot<?>> getRestrictedSlots();

	/**
	 * Returns the value of the '<em><b>Restricted Slots Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Slots Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Slots Are Permissive</em>' attribute.
	 * @see #setRestrictedSlotsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedSlotsArePermissive()
	 * @model
	 * @generated
	 */
	boolean isRestrictedSlotsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedSlotsArePermissive <em>Restricted Slots Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Slots Are Permissive</em>' attribute.
	 * @see #isRestrictedSlotsArePermissive()
	 * @generated
	 */
	void setRestrictedSlotsArePermissive(boolean value);

	/**
	 * Returns the value of the '<em><b>Restricted Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}<code>&lt;com.mmxlabs.models.lng.fleet.Vessel&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedVessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getRestrictedVessels();

	/**
	 * Returns the value of the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Vessels Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Vessels Are Permissive</em>' attribute.
	 * @see #isSetRestrictedVesselsArePermissive()
	 * @see #unsetRestrictedVesselsArePermissive()
	 * @see #setRestrictedVesselsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedVesselsArePermissive()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isRestrictedVesselsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Vessels Are Permissive</em>' attribute.
	 * @see #isSetRestrictedVesselsArePermissive()
	 * @see #unsetRestrictedVesselsArePermissive()
	 * @see #isRestrictedVesselsArePermissive()
	 * @generated
	 */
	void setRestrictedVesselsArePermissive(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRestrictedVesselsArePermissive()
	 * @see #isRestrictedVesselsArePermissive()
	 * @see #setRestrictedVesselsArePermissive(boolean)
	 * @generated
	 */
	void unsetRestrictedVesselsArePermissive();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Restricted Vessels Are Permissive</em>' attribute is set.
	 * @see #unsetRestrictedVesselsArePermissive()
	 * @see #isRestrictedVesselsArePermissive()
	 * @see #setRestrictedVesselsArePermissive(boolean)
	 * @generated
	 */
	boolean isSetRestrictedVesselsArePermissive();

	/**
	 * Returns the value of the '<em><b>Restricted Vessels Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Vessels Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Vessels Override</em>' attribute.
	 * @see #setRestrictedVesselsOverride(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_RestrictedVesselsOverride()
	 * @model
	 * @generated
	 */
	boolean isRestrictedVesselsOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsOverride <em>Restricted Vessels Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Vessels Override</em>' attribute.
	 * @see #isRestrictedVesselsOverride()
	 * @generated
	 */
	void setRestrictedVesselsOverride(boolean value);

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
	 * Returns the value of the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Misc Costs</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Misc Costs</em>' attribute.
	 * @see #setMiscCosts(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_MiscCosts()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='-###,###,##0'"
	 * @generated
	 */
	int getMiscCosts();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getMiscCosts <em>Misc Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Misc Costs</em>' attribute.
	 * @see #getMiscCosts()
	 * @generated
	 */
	void setMiscCosts(int value);

	///**
//	 * Returns the value of the '<em><b>Cancellation Fee</b></em>' attribute.
//	 * <!-- begin-user-doc -->
//	 * <p>
//	 * If the meaning of the '<em>Cancellation Fee</em>' attribute isn't clear,
//	 * there really should be more of a description here...
//	 * </p>
//	 * <!-- end-user-doc -->
//	 * @return the value of the '<em>Cancellation Fee</em>' attribute.
//	 * @see #isSetCancellationFee()
//	 * @see #unsetCancellationFee()
//	 * @see #setCancellationFee(int)
//	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_CancellationFee()
//	 * @model unsettable="true"
//	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='###,###,##0'"
//	 * @generated
//	 */
////	int getCancellationFee();
//
//	/**
//	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationFee <em>Cancellation Fee</em>}' attribute.
//	 * <!-- begin-user-doc -->
//	 * <!-- end-user-doc -->
//	 * @param value the new value of the '<em>Cancellation Fee</em>' attribute.
//	 * @see #isSetCancellationFee()
//	 * @see #unsetCancellationFee()
//	 * @see #getCancellationFee()
//	 * @generated
//	 */
//	void setCancellationFee(int value);

	//
//	/**
//	 * Returns the value of the '<em><b>Cancellation Fee</b></em>' attribute.
//	 * <!-- begin-user-doc -->
//	 * <p>
//	 * If the meaning of the '<em>Cancellation Fee</em>' attribute isn't clear,
//	 * there really should be more of a description here...
//	 * </p>
//	 * <!-- end-user-doc -->
//	 * @return the value of the '<em>Cancellation Fee</em>' attribute.
//	 * @see #isSetCancellationFee()
//	 * @see #unsetCancellationFee()
//	 * @see #setCancellationFee(int)
//	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_CancellationFee()
//	 * @model unsettable="true"
//	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='###,###,##0'"
//	 * @generated
//	 */
//	int getCancellationFee();
//
//	/**
//	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationFee <em>Cancellation Fee</em>}' attribute.
//	 * <!-- begin-user-doc -->
//	 * <!-- end-user-doc -->
//	 * @param value the new value of the '<em>Cancellation Fee</em>' attribute.
//	 * @see #isSetCancellationFee()
//	 * @see #unsetCancellationFee()
//	 * @see #getCancellationFee()
//	 * @generated
//	 */
//	void setCancellationFee(int value);

	/**
	 * Returns the value of the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cancellation Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cancellation Expression</em>' attribute.
	 * @see #isSetCancellationExpression()
	 * @see #unsetCancellationExpression()
	 * @see #setCancellationExpression(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_CancellationExpression()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getCancellationExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationExpression <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cancellation Expression</em>' attribute.
	 * @see #isSetCancellationExpression()
	 * @see #unsetCancellationExpression()
	 * @see #getCancellationExpression()
	 * @generated
	 */
	void setCancellationExpression(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationExpression <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCancellationExpression()
	 * @see #getCancellationExpression()
	 * @see #setCancellationExpression(String)
	 * @generated
	 */
	void unsetCancellationExpression();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationExpression <em>Cancellation Expression</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cancellation Expression</em>' attribute is set.
	 * @see #unsetCancellationExpression()
	 * @see #getCancellationExpression()
	 * @see #setCancellationExpression(String)
	 * @generated
	 */
	boolean isSetCancellationExpression();

	/**
	 * Returns the value of the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominated Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominated Vessel</em>' reference.
	 * @see #setNominatedVessel(Vessel)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_NominatedVessel()
	 * @model
	 * @generated
	 */
	Vessel getNominatedVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getNominatedVessel <em>Nominated Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nominated Vessel</em>' reference.
	 * @see #getNominatedVessel()
	 * @generated
	 */
	void setNominatedVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locked</em>' attribute.
	 * @see #setLocked(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Locked()
	 * @model
	 * @generated
	 */
	boolean isLocked();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isLocked <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Locked</em>' attribute.
	 * @see #isLocked()
	 * @generated
	 */
	void setLocked(boolean value);

	/**
	 * Returns the value of the '<em><b>Cancelled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cancelled</em>' attribute.
	 * @see #setCancelled(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_Cancelled()
	 * @model
	 * @generated
	 */
	boolean isCancelled();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isCancelled <em>Cancelled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cancelled</em>' attribute.
	 * @see #isCancelled()
	 * @generated
	 */
	void setCancelled(boolean value);

	/**
	 * Returns the value of the '<em><b>Window Counter Party</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Counter Party</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Counter Party</em>' attribute.
	 * @see #setWindowCounterParty(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowCounterParty()
	 * @model default="false"
	 * @generated
	 */
	boolean isWindowCounterParty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isWindowCounterParty <em>Window Counter Party</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Counter Party</em>' attribute.
	 * @see #isWindowCounterParty()
	 * @generated
	 */
	void setWindowCounterParty(boolean value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrDelegateDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrDelegateMinQuantity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrDelegateMaxQuantity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getSlotOrDelegateOperationalTolerance();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	VolumeUnits getSlotOrDelegateVolumeLimitsUnit();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getWindowEndWithSlotOrPortTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getWindowStartWithSlotOrPortTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getWindowEndWithSlotOrPortTimeWithFlex();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getWindowStartWithSlotOrPortTimeWithFlex();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getSlotOrDelegateWindowSize();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	TimePeriod getSlotOrDelegateWindowSizeUnits();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getWindowSizeInHours();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	BaseLegalEntity getSlotOrDelegateEntity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getSlotOrDelegateCancellationExpression();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	@NonNull
	PricingEvent getSlotOrDelegatePricingEvent();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	@NonNull
	ZonedDateTime getPricingDateAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	SlotContractParams getSlotContractParams();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getSlotOrDelegateCounterparty();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getSlotOrDelegateCN();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegateDivertible();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrDelegateShippingDaysRestriction();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegateFullCargoLot();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegateContractRestrictionsArePermissive();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegatePortRestrictionsArePermissive();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegateVesselRestrictionsArePermissive();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Contract> getSlotOrDelegateContractRestrictions();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<APortSet<Port>> getSlotOrDelegatePortRestrictions();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getSlotOrDelegateVesselRestrictions();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getSlotOrDelegateWithoutCPWindowSize();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getSlotOrDelegateWithoutCPDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getWindowSizeInHoursWithoutCP();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getWindowEndWithSlotOrPortTimeWithoutCP();

} // end of  Slot

// finish type fixing

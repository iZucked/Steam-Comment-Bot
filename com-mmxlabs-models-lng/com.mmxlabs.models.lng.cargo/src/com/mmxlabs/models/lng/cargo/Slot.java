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
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getMiscCosts <em>Misc Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isOverrideRestrictions <em>Override Restrictions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getNominatedVessel <em>Nominated Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowNominationDate <em>Window Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isWindowNominationIsDone <em>Window Nomination Is Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getWindowNominationComment <em>Window Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeNominationDate <em>Volume Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isVolumeNominationDone <em>Volume Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeNominationComment <em>Volume Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getVesselNominationDate <em>Vessel Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isVesselNominationDone <em>Vessel Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getVesselNominationComment <em>Vessel Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPortNominationDate <em>Port Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isPortNominationDone <em>Port Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPortNominationComment <em>Port Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPortLoadNominationDate <em>Port Load Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isPortLoadNominationDone <em>Port Load Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Slot#getPortLoadNominationComment <em>Port Load Nomination Comment</em>}</li>
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
	 * @model annotation="http://www.mmxlabs.com/models/overrideFeature"
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
	 * @model annotation="http://www.mmxlabs.com/models/overrideFeature"
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
	 * @model annotation="http://www.mmxlabs.com/models/overrideFeature"
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

	/**
	 * Returns the value of the '<em><b>Allowed Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}<code>&lt;com.mmxlabs.models.lng.fleet.Vessel&gt;</code>.
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
	 * <p>
	 * If the meaning of the '<em>Locked</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
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
	 * Returns the value of the '<em><b>Window Nomination Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Nomination Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Nomination Date</em>' attribute.
	 * @see #isSetWindowNominationDate()
	 * @see #unsetWindowNominationDate()
	 * @see #setWindowNominationDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowNominationDate()
	 * @model unique="false" unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getWindowNominationDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowNominationDate <em>Window Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Nomination Date</em>' attribute.
	 * @see #isSetWindowNominationDate()
	 * @see #unsetWindowNominationDate()
	 * @see #getWindowNominationDate()
	 * @generated
	 */
	void setWindowNominationDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowNominationDate <em>Window Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetWindowNominationDate()
	 * @see #getWindowNominationDate()
	 * @see #setWindowNominationDate(LocalDate)
	 * @generated
	 */
	void unsetWindowNominationDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowNominationDate <em>Window Nomination Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Window Nomination Date</em>' attribute is set.
	 * @see #unsetWindowNominationDate()
	 * @see #getWindowNominationDate()
	 * @see #setWindowNominationDate(LocalDate)
	 * @generated
	 */
	boolean isSetWindowNominationDate();

	/**
	 * Returns the value of the '<em><b>Window Nomination Is Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Nomination Is Done</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Nomination Is Done</em>' attribute.
	 * @see #setWindowNominationIsDone(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowNominationIsDone()
	 * @model unique="false"
	 * @generated
	 */
	boolean isWindowNominationIsDone();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isWindowNominationIsDone <em>Window Nomination Is Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Nomination Is Done</em>' attribute.
	 * @see #isWindowNominationIsDone()
	 * @generated
	 */
	void setWindowNominationIsDone(boolean value);

	/**
	 * Returns the value of the '<em><b>Window Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Nomination Counterparty</em>' attribute.
	 * @see #isSetWindowNominationCounterparty()
	 * @see #unsetWindowNominationCounterparty()
	 * @see #setWindowNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowNominationCounterparty()
	 * @model unique="false" unsettable="true"
	 * @generated
	 */
	boolean isWindowNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Nomination Counterparty</em>' attribute.
	 * @see #isSetWindowNominationCounterparty()
	 * @see #unsetWindowNominationCounterparty()
	 * @see #isWindowNominationCounterparty()
	 * @generated
	 */
	void setWindowNominationCounterparty(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetWindowNominationCounterparty()
	 * @see #isWindowNominationCounterparty()
	 * @see #setWindowNominationCounterparty(boolean)
	 * @generated
	 */
	void unsetWindowNominationCounterparty();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Window Nomination Counterparty</em>' attribute is set.
	 * @see #unsetWindowNominationCounterparty()
	 * @see #isWindowNominationCounterparty()
	 * @see #setWindowNominationCounterparty(boolean)
	 * @generated
	 */
	boolean isSetWindowNominationCounterparty();

	/**
	 * Returns the value of the '<em><b>Window Nomination Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Nomination Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Nomination Comment</em>' attribute.
	 * @see #setWindowNominationComment(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_WindowNominationComment()
	 * @model unique="false"
	 * @generated
	 */
	String getWindowNominationComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowNominationComment <em>Window Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Nomination Comment</em>' attribute.
	 * @see #getWindowNominationComment()
	 * @generated
	 */
	void setWindowNominationComment(String value);

	/**
	 * Returns the value of the '<em><b>Volume Nomination Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Nomination Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Nomination Date</em>' attribute.
	 * @see #isSetVolumeNominationDate()
	 * @see #unsetVolumeNominationDate()
	 * @see #setVolumeNominationDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VolumeNominationDate()
	 * @model unique="false" unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getVolumeNominationDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeNominationDate <em>Volume Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Nomination Date</em>' attribute.
	 * @see #isSetVolumeNominationDate()
	 * @see #unsetVolumeNominationDate()
	 * @see #getVolumeNominationDate()
	 * @generated
	 */
	void setVolumeNominationDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeNominationDate <em>Volume Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVolumeNominationDate()
	 * @see #getVolumeNominationDate()
	 * @see #setVolumeNominationDate(LocalDate)
	 * @generated
	 */
	void unsetVolumeNominationDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeNominationDate <em>Volume Nomination Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Volume Nomination Date</em>' attribute is set.
	 * @see #unsetVolumeNominationDate()
	 * @see #getVolumeNominationDate()
	 * @see #setVolumeNominationDate(LocalDate)
	 * @generated
	 */
	boolean isSetVolumeNominationDate();

	/**
	 * Returns the value of the '<em><b>Volume Nomination Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Nomination Done</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Nomination Done</em>' attribute.
	 * @see #setVolumeNominationDone(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VolumeNominationDone()
	 * @model unique="false"
	 * @generated
	 */
	boolean isVolumeNominationDone();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVolumeNominationDone <em>Volume Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Nomination Done</em>' attribute.
	 * @see #isVolumeNominationDone()
	 * @generated
	 */
	void setVolumeNominationDone(boolean value);

	/**
	 * Returns the value of the '<em><b>Volume Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Nomination Counterparty</em>' attribute.
	 * @see #isSetVolumeNominationCounterparty()
	 * @see #unsetVolumeNominationCounterparty()
	 * @see #setVolumeNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VolumeNominationCounterparty()
	 * @model unique="false" unsettable="true"
	 * @generated
	 */
	boolean isVolumeNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Nomination Counterparty</em>' attribute.
	 * @see #isSetVolumeNominationCounterparty()
	 * @see #unsetVolumeNominationCounterparty()
	 * @see #isVolumeNominationCounterparty()
	 * @generated
	 */
	void setVolumeNominationCounterparty(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVolumeNominationCounterparty()
	 * @see #isVolumeNominationCounterparty()
	 * @see #setVolumeNominationCounterparty(boolean)
	 * @generated
	 */
	void unsetVolumeNominationCounterparty();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Volume Nomination Counterparty</em>' attribute is set.
	 * @see #unsetVolumeNominationCounterparty()
	 * @see #isVolumeNominationCounterparty()
	 * @see #setVolumeNominationCounterparty(boolean)
	 * @generated
	 */
	boolean isSetVolumeNominationCounterparty();

	/**
	 * Returns the value of the '<em><b>Volume Nomination Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Nomination Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Nomination Comment</em>' attribute.
	 * @see #setVolumeNominationComment(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VolumeNominationComment()
	 * @model unique="false"
	 * @generated
	 */
	String getVolumeNominationComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeNominationComment <em>Volume Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Nomination Comment</em>' attribute.
	 * @see #getVolumeNominationComment()
	 * @generated
	 */
	void setVolumeNominationComment(String value);

	/**
	 * Returns the value of the '<em><b>Vessel Nomination Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Nomination Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Nomination Date</em>' attribute.
	 * @see #isSetVesselNominationDate()
	 * @see #unsetVesselNominationDate()
	 * @see #setVesselNominationDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VesselNominationDate()
	 * @model unique="false" unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getVesselNominationDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVesselNominationDate <em>Vessel Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Nomination Date</em>' attribute.
	 * @see #isSetVesselNominationDate()
	 * @see #unsetVesselNominationDate()
	 * @see #getVesselNominationDate()
	 * @generated
	 */
	void setVesselNominationDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVesselNominationDate <em>Vessel Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVesselNominationDate()
	 * @see #getVesselNominationDate()
	 * @see #setVesselNominationDate(LocalDate)
	 * @generated
	 */
	void unsetVesselNominationDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVesselNominationDate <em>Vessel Nomination Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Vessel Nomination Date</em>' attribute is set.
	 * @see #unsetVesselNominationDate()
	 * @see #getVesselNominationDate()
	 * @see #setVesselNominationDate(LocalDate)
	 * @generated
	 */
	boolean isSetVesselNominationDate();

	/**
	 * Returns the value of the '<em><b>Vessel Nomination Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Nomination Done</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Nomination Done</em>' attribute.
	 * @see #setVesselNominationDone(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VesselNominationDone()
	 * @model unique="false"
	 * @generated
	 */
	boolean isVesselNominationDone();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVesselNominationDone <em>Vessel Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Nomination Done</em>' attribute.
	 * @see #isVesselNominationDone()
	 * @generated
	 */
	void setVesselNominationDone(boolean value);

	/**
	 * Returns the value of the '<em><b>Vessel Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Nomination Counterparty</em>' attribute.
	 * @see #isSetVesselNominationCounterparty()
	 * @see #unsetVesselNominationCounterparty()
	 * @see #setVesselNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VesselNominationCounterparty()
	 * @model unique="false" unsettable="true"
	 * @generated
	 */
	boolean isVesselNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Nomination Counterparty</em>' attribute.
	 * @see #isSetVesselNominationCounterparty()
	 * @see #unsetVesselNominationCounterparty()
	 * @see #isVesselNominationCounterparty()
	 * @generated
	 */
	void setVesselNominationCounterparty(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVesselNominationCounterparty()
	 * @see #isVesselNominationCounterparty()
	 * @see #setVesselNominationCounterparty(boolean)
	 * @generated
	 */
	void unsetVesselNominationCounterparty();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Vessel Nomination Counterparty</em>' attribute is set.
	 * @see #unsetVesselNominationCounterparty()
	 * @see #isVesselNominationCounterparty()
	 * @see #setVesselNominationCounterparty(boolean)
	 * @generated
	 */
	boolean isSetVesselNominationCounterparty();

	/**
	 * Returns the value of the '<em><b>Vessel Nomination Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Nomination Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Nomination Comment</em>' attribute.
	 * @see #setVesselNominationComment(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_VesselNominationComment()
	 * @model unique="false"
	 * @generated
	 */
	String getVesselNominationComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getVesselNominationComment <em>Vessel Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Nomination Comment</em>' attribute.
	 * @see #getVesselNominationComment()
	 * @generated
	 */
	void setVesselNominationComment(String value);

	/**
	 * Returns the value of the '<em><b>Port Nomination Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Nomination Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Nomination Date</em>' attribute.
	 * @see #isSetPortNominationDate()
	 * @see #unsetPortNominationDate()
	 * @see #setPortNominationDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortNominationDate()
	 * @model unique="false" unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPortNominationDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortNominationDate <em>Port Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Nomination Date</em>' attribute.
	 * @see #isSetPortNominationDate()
	 * @see #unsetPortNominationDate()
	 * @see #getPortNominationDate()
	 * @generated
	 */
	void setPortNominationDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortNominationDate <em>Port Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPortNominationDate()
	 * @see #getPortNominationDate()
	 * @see #setPortNominationDate(LocalDate)
	 * @generated
	 */
	void unsetPortNominationDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortNominationDate <em>Port Nomination Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port Nomination Date</em>' attribute is set.
	 * @see #unsetPortNominationDate()
	 * @see #getPortNominationDate()
	 * @see #setPortNominationDate(LocalDate)
	 * @generated
	 */
	boolean isSetPortNominationDate();

	/**
	 * Returns the value of the '<em><b>Port Nomination Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Nomination Done</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Nomination Done</em>' attribute.
	 * @see #setPortNominationDone(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortNominationDone()
	 * @model unique="false"
	 * @generated
	 */
	boolean isPortNominationDone();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortNominationDone <em>Port Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Nomination Done</em>' attribute.
	 * @see #isPortNominationDone()
	 * @generated
	 */
	void setPortNominationDone(boolean value);

	/**
	 * Returns the value of the '<em><b>Port Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Nomination Counterparty</em>' attribute.
	 * @see #isSetPortNominationCounterparty()
	 * @see #unsetPortNominationCounterparty()
	 * @see #setPortNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortNominationCounterparty()
	 * @model unique="false" unsettable="true"
	 * @generated
	 */
	boolean isPortNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Nomination Counterparty</em>' attribute.
	 * @see #isSetPortNominationCounterparty()
	 * @see #unsetPortNominationCounterparty()
	 * @see #isPortNominationCounterparty()
	 * @generated
	 */
	void setPortNominationCounterparty(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPortNominationCounterparty()
	 * @see #isPortNominationCounterparty()
	 * @see #setPortNominationCounterparty(boolean)
	 * @generated
	 */
	void unsetPortNominationCounterparty();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port Nomination Counterparty</em>' attribute is set.
	 * @see #unsetPortNominationCounterparty()
	 * @see #isPortNominationCounterparty()
	 * @see #setPortNominationCounterparty(boolean)
	 * @generated
	 */
	boolean isSetPortNominationCounterparty();

	/**
	 * Returns the value of the '<em><b>Port Nomination Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Nomination Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Nomination Comment</em>' attribute.
	 * @see #setPortNominationComment(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortNominationComment()
	 * @model unique="false"
	 * @generated
	 */
	String getPortNominationComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortNominationComment <em>Port Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Nomination Comment</em>' attribute.
	 * @see #getPortNominationComment()
	 * @generated
	 */
	void setPortNominationComment(String value);

	/**
	 * Returns the value of the '<em><b>Port Load Nomination Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Load Nomination Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Load Nomination Date</em>' attribute.
	 * @see #isSetPortLoadNominationDate()
	 * @see #unsetPortLoadNominationDate()
	 * @see #setPortLoadNominationDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortLoadNominationDate()
	 * @model unique="false" unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPortLoadNominationDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortLoadNominationDate <em>Port Load Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Load Nomination Date</em>' attribute.
	 * @see #isSetPortLoadNominationDate()
	 * @see #unsetPortLoadNominationDate()
	 * @see #getPortLoadNominationDate()
	 * @generated
	 */
	void setPortLoadNominationDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortLoadNominationDate <em>Port Load Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPortLoadNominationDate()
	 * @see #getPortLoadNominationDate()
	 * @see #setPortLoadNominationDate(LocalDate)
	 * @generated
	 */
	void unsetPortLoadNominationDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortLoadNominationDate <em>Port Load Nomination Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port Load Nomination Date</em>' attribute is set.
	 * @see #unsetPortLoadNominationDate()
	 * @see #getPortLoadNominationDate()
	 * @see #setPortLoadNominationDate(LocalDate)
	 * @generated
	 */
	boolean isSetPortLoadNominationDate();

	/**
	 * Returns the value of the '<em><b>Port Load Nomination Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Load Nomination Done</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Load Nomination Done</em>' attribute.
	 * @see #setPortLoadNominationDone(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortLoadNominationDone()
	 * @model unique="false"
	 * @generated
	 */
	boolean isPortLoadNominationDone();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortLoadNominationDone <em>Port Load Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Load Nomination Done</em>' attribute.
	 * @see #isPortLoadNominationDone()
	 * @generated
	 */
	void setPortLoadNominationDone(boolean value);

	/**
	 * Returns the value of the '<em><b>Port Load Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Load Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Load Nomination Counterparty</em>' attribute.
	 * @see #isSetPortLoadNominationCounterparty()
	 * @see #unsetPortLoadNominationCounterparty()
	 * @see #setPortLoadNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortLoadNominationCounterparty()
	 * @model unique="false" unsettable="true"
	 * @generated
	 */
	boolean isPortLoadNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Load Nomination Counterparty</em>' attribute.
	 * @see #isSetPortLoadNominationCounterparty()
	 * @see #unsetPortLoadNominationCounterparty()
	 * @see #isPortLoadNominationCounterparty()
	 * @generated
	 */
	void setPortLoadNominationCounterparty(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPortLoadNominationCounterparty()
	 * @see #isPortLoadNominationCounterparty()
	 * @see #setPortLoadNominationCounterparty(boolean)
	 * @generated
	 */
	void unsetPortLoadNominationCounterparty();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port Load Nomination Counterparty</em>' attribute is set.
	 * @see #unsetPortLoadNominationCounterparty()
	 * @see #isPortLoadNominationCounterparty()
	 * @see #setPortLoadNominationCounterparty(boolean)
	 * @generated
	 */
	boolean isSetPortLoadNominationCounterparty();

	/**
	 * Returns the value of the '<em><b>Port Load Nomination Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Load Nomination Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Load Nomination Comment</em>' attribute.
	 * @see #setPortLoadNominationComment(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlot_PortLoadNominationComment()
	 * @model unique="false"
	 * @generated
	 */
	String getPortLoadNominationComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Slot#getPortLoadNominationComment <em>Port Load Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Load Nomination Comment</em>' attribute.
	 * @see #getPortLoadNominationComment()
	 * @generated
	 */
	void setPortLoadNominationComment(String value);

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
	boolean getSlotOrDelegateRestrictedListsArePermissive();

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
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getSlotOrDelegateWindowNominationDate();

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
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getSlotOrDelegatePortLoadNominationDate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getSlotOrDelegateVesselNominationDate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getSlotOrDelegateVolumeNominationDate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getSlotOrDelegatePortNominationDate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegateWindowNominationCounterparty();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegateVesselNominationCounterparty();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegateVolumeNominationCounterparty();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegatePortNominationCounterparty();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean getSlotOrDelegatePortLoadNominationCounterparty();

} // end of  Slot

// finish type fixing

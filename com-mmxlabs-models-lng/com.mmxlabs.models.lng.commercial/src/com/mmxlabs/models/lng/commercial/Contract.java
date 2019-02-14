/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import java.time.YearMonth;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getCode <em>Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getCn <em>Cn</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getContractYearStart <em>Contract Year Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getAllowedPorts <em>Allowed Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPreferredPort <em>Preferred Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getVolumeLimitsUnit <em>Volume Limits Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getOperationalTolerance <em>Operational Tolerance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedContracts <em>Restricted Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedPorts <em>Restricted Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPriceInfo <em>Price Info</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getContractType <em>Contract Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPricingEvent <em>Pricing Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getWindowNominationSize <em>Window Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getWindowNominationSizeUnits <em>Window Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getVesselNominationSize <em>Vessel Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getVesselNominationSizeUnits <em>Vessel Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getVolumeNominationSize <em>Volume Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getVolumeNominationSizeUnits <em>Volume Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPortNominationSize <em>Port Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPortNominationSizeUnits <em>Port Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isDivertible <em>Divertible</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getShippingDaysRestriction <em>Shipping Days Restriction</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPortLoadNominationSize <em>Port Load Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPortLoadNominationSizeUnits <em>Port Load Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract()
 * @model
 * @generated
 */
public interface Contract extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_Entity()
	 * @model required="true"
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Allowed Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_AllowedPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getAllowedPorts();

	/**
	 * Returns the value of the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Preferred Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Preferred Port</em>' reference.
	 * @see #setPreferredPort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PreferredPort()
	 * @model
	 * @generated
	 */
	Port getPreferredPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPreferredPort <em>Preferred Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Preferred Port</em>' reference.
	 * @see #getPreferredPort()
	 * @generated
	 */
	void setPreferredPort(Port value);

	/**
	 * Returns the value of the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Quantity</em>' attribute.
	 * @see #setMinQuantity(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_MinQuantity()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#,###,##0'"
	 * @generated
	 */
	int getMinQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(int value);

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
	 * @see #setMaxQuantity(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_MaxQuantity()
	 * @model default="140000" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#,###,##0'"
	 * @generated
	 */
	int getMaxQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(int value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_OperationalTolerance()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='##0.#' unit='%' exportFormatString='#.###' unitPrefix='\261'"
	 * @generated
	 */
	double getOperationalTolerance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getOperationalTolerance <em>Operational Tolerance</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getOperationalTolerance <em>Operational Tolerance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetOperationalTolerance()
	 * @see #getOperationalTolerance()
	 * @see #setOperationalTolerance(double)
	 * @generated
	 */
	void unsetOperationalTolerance();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getOperationalTolerance <em>Operational Tolerance</em>}' attribute is set.
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
	 * @see #setVolumeLimitsUnit(VolumeUnits)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_VolumeLimitsUnit()
	 * @model required="true"
	 * @generated
	 */
	VolumeUnits getVolumeLimitsUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getVolumeLimitsUnit <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Limits Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 */
	void setVolumeLimitsUnit(VolumeUnits value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedListsArePermissive()
	 * @model
	 * @generated
	 */
	boolean isRestrictedListsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Lists Are Permissive</em>' attribute.
	 * @see #isRestrictedListsArePermissive()
	 * @generated
	 */
	void setRestrictedListsArePermissive(boolean value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedContracts()
	 * @model
	 * @generated
	 */
	EList<Contract> getRestrictedContracts();

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getRestrictedPorts();

	/**
	 * Returns the value of the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Info</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Info</em>' containment reference.
	 * @see #setPriceInfo(LNGPriceCalculatorParameters)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PriceInfo()
	 * @model containment="true" required="true"
	 * @generated
	 */
	LNGPriceCalculatorParameters getPriceInfo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPriceInfo <em>Price Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Info</em>' containment reference.
	 * @see #getPriceInfo()
	 * @generated
	 */
	void setPriceInfo(LNGPriceCalculatorParameters value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_Notes()
	 * @model
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

	/**
	 * Returns the value of the '<em><b>Contract Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.commercial.ContractType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.ContractType
	 * @see #setContractType(ContractType)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_ContractType()
	 * @model
	 * @generated
	 */
	ContractType getContractType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getContractType <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.ContractType
	 * @see #getContractType()
	 * @generated
	 */
	void setContractType(ContractType value);

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
	 * @see #setPricingEvent(PricingEvent)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PricingEvent()
	 * @model required="true"
	 * @generated
	 */
	PricingEvent getPricingEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPricingEvent <em>Pricing Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Event</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @see #getPricingEvent()
	 * @generated
	 */
	void setPricingEvent(PricingEvent value);
//
//	/**
//	 * Returns the value of the '<em><b>Cancellation Fee</b></em>' attribute.
//	 * The default value is <code>"0"</code>.
//	 * <!-- begin-user-doc -->
//	 * <p>
//	 * If the meaning of the '<em>Cancellation Fee</em>' attribute isn't clear,
//	 * there really should be more of a description here...
//	 * </p>
//	 * <!-- end-user-doc -->
//	 * @return the value of the '<em>Cancellation Fee</em>' attribute.
//	 * @see #setCancellationFee(int)
//	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_CancellationFee()
//	 * @model default="0"
//	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='###,###,##0' unitPrefix='$'"
//	 * @generated
//	 */
//	int getCancellationFee();
//
//	/**
//	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getCancellationFee <em>Cancellation Fee</em>}' attribute.
//	 * <!-- begin-user-doc -->
//	 * <!-- end-user-doc -->
//	 * @param value the new value of the '<em>Cancellation Fee</em>' attribute.
//	 * @see #getCancellationFee()
//	 * @generated
//	 */
//	void setCancellationFee(int value);

	/**
	 * Returns the value of the '<em><b>Cancellation Expression</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cancellation Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cancellation Expression</em>' attribute.
	 * @see #setCancellationExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_CancellationExpression()
	 * @model default="0"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getCancellationExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getCancellationExpression <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cancellation Expression</em>' attribute.
	 * @see #getCancellationExpression()
	 * @generated
	 */
	void setCancellationExpression(String value);

	/**
	 * Returns the value of the '<em><b>Window Nomination Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Nomination Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Nomination Size</em>' attribute.
	 * @see #setWindowNominationSize(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_WindowNominationSize()
	 * @model unique="false"
	 * @generated
	 */
	int getWindowNominationSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getWindowNominationSize <em>Window Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Nomination Size</em>' attribute.
	 * @see #getWindowNominationSize()
	 * @generated
	 */
	void setWindowNominationSize(int value);

	/**
	 * Returns the value of the '<em><b>Window Nomination Size Units</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Nomination Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setWindowNominationSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_WindowNominationSizeUnits()
	 * @model unique="false"
	 * @generated
	 */
	TimePeriod getWindowNominationSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getWindowNominationSizeUnits <em>Window Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getWindowNominationSizeUnits()
	 * @generated
	 */
	void setWindowNominationSizeUnits(TimePeriod value);

	/**
	 * Returns the value of the '<em><b>Window Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Nomination Counterparty</em>' attribute.
	 * @see #setWindowNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_WindowNominationCounterparty()
	 * @model unique="false"
	 * @generated
	 */
	boolean isWindowNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Nomination Counterparty</em>' attribute.
	 * @see #isWindowNominationCounterparty()
	 * @generated
	 */
	void setWindowNominationCounterparty(boolean value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_Divertible()
	 * @model
	 * @generated
	 */
	boolean isDivertible();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isDivertible <em>Divertible</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_ShippingDaysRestriction()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='###'"
	 * @generated
	 */
	int getShippingDaysRestriction();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getShippingDaysRestriction <em>Shipping Days Restriction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Days Restriction</em>' attribute.
	 * @see #getShippingDaysRestriction()
	 * @generated
	 */
	void setShippingDaysRestriction(int value);

	/**
	 * Returns the value of the '<em><b>Port Load Nomination Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Load Nomination Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Load Nomination Size</em>' attribute.
	 * @see #setPortLoadNominationSize(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PortLoadNominationSize()
	 * @model unique="false"
	 * @generated
	 */
	int getPortLoadNominationSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPortLoadNominationSize <em>Port Load Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Load Nomination Size</em>' attribute.
	 * @see #getPortLoadNominationSize()
	 * @generated
	 */
	void setPortLoadNominationSize(int value);

	/**
	 * Returns the value of the '<em><b>Port Load Nomination Size Units</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Load Nomination Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Load Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setPortLoadNominationSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PortLoadNominationSizeUnits()
	 * @model unique="false"
	 * @generated
	 */
	TimePeriod getPortLoadNominationSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPortLoadNominationSizeUnits <em>Port Load Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Load Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getPortLoadNominationSizeUnits()
	 * @generated
	 */
	void setPortLoadNominationSizeUnits(TimePeriod value);

	/**
	 * Returns the value of the '<em><b>Port Load Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Load Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Load Nomination Counterparty</em>' attribute.
	 * @see #setPortLoadNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PortLoadNominationCounterparty()
	 * @model unique="false"
	 * @generated
	 */
	boolean isPortLoadNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Load Nomination Counterparty</em>' attribute.
	 * @see #isPortLoadNominationCounterparty()
	 * @generated
	 */
	void setPortLoadNominationCounterparty(boolean value);

	/**
	 * Returns the value of the '<em><b>Vessel Nomination Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Nomination Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Nomination Size</em>' attribute.
	 * @see #setVesselNominationSize(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_VesselNominationSize()
	 * @model unique="false"
	 * @generated
	 */
	int getVesselNominationSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getVesselNominationSize <em>Vessel Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Nomination Size</em>' attribute.
	 * @see #getVesselNominationSize()
	 * @generated
	 */
	void setVesselNominationSize(int value);

	/**
	 * Returns the value of the '<em><b>Vessel Nomination Size Units</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Nomination Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setVesselNominationSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_VesselNominationSizeUnits()
	 * @model unique="false"
	 * @generated
	 */
	TimePeriod getVesselNominationSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getVesselNominationSizeUnits <em>Vessel Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getVesselNominationSizeUnits()
	 * @generated
	 */
	void setVesselNominationSizeUnits(TimePeriod value);

	/**
	 * Returns the value of the '<em><b>Vessel Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Nomination Counterparty</em>' attribute.
	 * @see #setVesselNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_VesselNominationCounterparty()
	 * @model unique="false"
	 * @generated
	 */
	boolean isVesselNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Nomination Counterparty</em>' attribute.
	 * @see #isVesselNominationCounterparty()
	 * @generated
	 */
	void setVesselNominationCounterparty(boolean value);

	/**
	 * Returns the value of the '<em><b>Volume Nomination Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Nomination Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Nomination Size</em>' attribute.
	 * @see #setVolumeNominationSize(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_VolumeNominationSize()
	 * @model unique="false"
	 * @generated
	 */
	int getVolumeNominationSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getVolumeNominationSize <em>Volume Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Nomination Size</em>' attribute.
	 * @see #getVolumeNominationSize()
	 * @generated
	 */
	void setVolumeNominationSize(int value);

	/**
	 * Returns the value of the '<em><b>Volume Nomination Size Units</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Nomination Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setVolumeNominationSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_VolumeNominationSizeUnits()
	 * @model unique="false"
	 * @generated
	 */
	TimePeriod getVolumeNominationSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getVolumeNominationSizeUnits <em>Volume Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getVolumeNominationSizeUnits()
	 * @generated
	 */
	void setVolumeNominationSizeUnits(TimePeriod value);

	/**
	 * Returns the value of the '<em><b>Volume Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Nomination Counterparty</em>' attribute.
	 * @see #setVolumeNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_VolumeNominationCounterparty()
	 * @model unique="false"
	 * @generated
	 */
	boolean isVolumeNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Nomination Counterparty</em>' attribute.
	 * @see #isVolumeNominationCounterparty()
	 * @generated
	 */
	void setVolumeNominationCounterparty(boolean value);

	/**
	 * Returns the value of the '<em><b>Port Nomination Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Nomination Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Nomination Size</em>' attribute.
	 * @see #setPortNominationSize(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PortNominationSize()
	 * @model unique="false"
	 * @generated
	 */
	int getPortNominationSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPortNominationSize <em>Port Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Nomination Size</em>' attribute.
	 * @see #getPortNominationSize()
	 * @generated
	 */
	void setPortNominationSize(int value);

	/**
	 * Returns the value of the '<em><b>Port Nomination Size Units</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Nomination Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setPortNominationSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PortNominationSizeUnits()
	 * @model unique="false"
	 * @generated
	 */
	TimePeriod getPortNominationSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPortNominationSizeUnits <em>Port Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Nomination Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getPortNominationSizeUnits()
	 * @generated
	 */
	void setPortNominationSizeUnits(TimePeriod value);

	/**
	 * Returns the value of the '<em><b>Port Nomination Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Nomination Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Nomination Counterparty</em>' attribute.
	 * @see #setPortNominationCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PortNominationCounterparty()
	 * @model unique="false"
	 * @generated
	 */
	boolean isPortNominationCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Nomination Counterparty</em>' attribute.
	 * @see #isPortNominationCounterparty()
	 * @generated
	 */
	void setPortNominationCounterparty(boolean value);

	/**
	 * Returns the value of the '<em><b>Contract Year Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Year Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Year Start</em>' attribute.
	 * @see #setContractYearStart(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_ContractYearStart()
	 * @model
	 * @generated
	 */
	int getContractYearStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getContractYearStart <em>Contract Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract Year Start</em>' attribute.
	 * @see #getContractYearStart()
	 * @generated
	 */
	void setContractYearStart(int value);

	/**
	 * Returns the value of the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Code</em>' attribute.
	 * @see #setCode(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_Code()
	 * @model
	 * @generated
	 */
	String getCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getCode <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' attribute.
	 * @see #getCode()
	 * @generated
	 */
	void setCode(String value);

	/**
	 * Returns the value of the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Counterparty</em>' attribute.
	 * @see #setCounterparty(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_Counterparty()
	 * @model
	 * @generated
	 */
	String getCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getCounterparty <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Counterparty</em>' attribute.
	 * @see #getCounterparty()
	 * @generated
	 */
	void setCounterparty(String value);

	/**
	 * Returns the value of the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cn</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cn</em>' attribute.
	 * @see #setCn(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_Cn()
	 * @model
	 * @generated
	 */
	String getCn();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getCn <em>Cn</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cn</em>' attribute.
	 * @see #getCn()
	 * @generated
	 */
	void setCn(String value);

	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #setStartDate(YearMonth)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_StartDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(YearMonth value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(YearMonth)
	 * @generated
	 */
	void unsetStartDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getStartDate <em>Start Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Date</em>' attribute is set.
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(YearMonth)
	 * @generated
	 */
	boolean isSetStartDate();

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #isSetEndDate()
	 * @see #unsetEndDate()
	 * @see #setEndDate(YearMonth)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_EndDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #isSetEndDate()
	 * @see #unsetEndDate()
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(YearMonth value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndDate()
	 * @see #getEndDate()
	 * @see #setEndDate(YearMonth)
	 * @generated
	 */
	void unsetEndDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getEndDate <em>End Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Date</em>' attribute is set.
	 * @see #unsetEndDate()
	 * @see #getEndDate()
	 * @see #setEndDate(YearMonth)
	 * @generated
	 */
	boolean isSetEndDate();

} // end of  Contract

// finish type fixing

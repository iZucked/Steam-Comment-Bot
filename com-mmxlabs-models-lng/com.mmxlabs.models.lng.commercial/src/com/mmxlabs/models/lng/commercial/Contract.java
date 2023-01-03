/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;
import java.time.YearMonth;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

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
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isFullCargoLot <em>Full Cargo Lot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedContracts <em>Restricted Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedPorts <em>Restricted Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedVessels <em>Restricted Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPriceInfo <em>Price Info</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getContractType <em>Contract Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPricingEvent <em>Pricing Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getShippingDaysRestriction <em>Shipping Days Restriction</em>}</li>
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
	 * Returns the value of the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Full Cargo Lot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Full Cargo Lot</em>' attribute.
	 * @see #setFullCargoLot(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_FullCargoLot()
	 * @model
	 * @generated
	 */
	boolean isFullCargoLot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isFullCargoLot <em>Full Cargo Lot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Full Cargo Lot</em>' attribute.
	 * @see #isFullCargoLot()
	 * @generated
	 */
	void setFullCargoLot(boolean value);

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
	 * Returns the value of the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Contracts Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Contracts Are Permissive</em>' attribute.
	 * @see #setRestrictedContractsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedContractsArePermissive()
	 * @model
	 * @generated
	 */
	boolean isRestrictedContractsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Contracts Are Permissive</em>' attribute.
	 * @see #isRestrictedContractsArePermissive()
	 * @generated
	 */
	void setRestrictedContractsArePermissive(boolean value);

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
	 * Returns the value of the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Ports Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Ports Are Permissive</em>' attribute.
	 * @see #setRestrictedPortsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedPortsArePermissive()
	 * @model
	 * @generated
	 */
	boolean isRestrictedPortsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Ports Are Permissive</em>' attribute.
	 * @see #isRestrictedPortsArePermissive()
	 * @generated
	 */
	void setRestrictedPortsArePermissive(boolean value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedVessels()
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
	 * @see #setRestrictedVesselsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedVesselsArePermissive()
	 * @model
	 * @generated
	 */
	boolean isRestrictedVesselsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Vessels Are Permissive</em>' attribute.
	 * @see #isRestrictedVesselsArePermissive()
	 * @generated
	 */
	void setRestrictedVesselsArePermissive(boolean value);

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
	 * @model annotation="http://www.mmxlabs.com/models/validation ignore='true'"
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

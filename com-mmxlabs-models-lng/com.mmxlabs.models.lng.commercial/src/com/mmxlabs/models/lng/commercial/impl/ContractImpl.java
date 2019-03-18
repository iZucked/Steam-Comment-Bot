/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import java.time.YearMonth;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getCode <em>Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getCn <em>Cn</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getContractYearStart <em>Contract Year Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getAllowedPorts <em>Allowed Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPreferredPort <em>Preferred Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getVolumeLimitsUnit <em>Volume Limits Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getOperationalTolerance <em>Operational Tolerance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isFullCargoLot <em>Full Cargo Lot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getRestrictedContracts <em>Restricted Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getRestrictedPorts <em>Restricted Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPriceInfo <em>Price Info</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getContractType <em>Contract Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPricingEvent <em>Pricing Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getWindowNominationSize <em>Window Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getWindowNominationSizeUnits <em>Window Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getVesselNominationSize <em>Vessel Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getVesselNominationSizeUnits <em>Vessel Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getVolumeNominationSize <em>Volume Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getVolumeNominationSizeUnits <em>Volume Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPortNominationSize <em>Port Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPortNominationSizeUnits <em>Port Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isDivertible <em>Divertible</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getShippingDaysRestriction <em>Shipping Days Restriction</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPortLoadNominationSize <em>Port Load Nomination Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPortLoadNominationSizeUnits <em>Port Load Nomination Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContractImpl extends UUIDObjectImpl implements Contract {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCode() <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
	protected static final String CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCode() <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
	protected String code = CODE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCounterparty() <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final String COUNTERPARTY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCounterparty() <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCounterparty()
	 * @generated
	 * @ordered
	 */
	protected String counterparty = COUNTERPARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getCn() <em>Cn</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCn()
	 * @generated
	 * @ordered
	 */
	protected static final String CN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCn() <em>Cn</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCn()
	 * @generated
	 * @ordered
	 */
	protected String cn = CN_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity entity;

	/**
	 * The default value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected YearMonth startDate = START_DATE_EDEFAULT;

	/**
	 * This is true if the Start Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startDateESet;

	/**
	 * The default value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndDate()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth END_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndDate()
	 * @generated
	 * @ordered
	 */
	protected YearMonth endDate = END_DATE_EDEFAULT;

	/**
	 * This is true if the End Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endDateESet;

	/**
	 * The default value of the '{@link #getContractYearStart() <em>Contract Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractYearStart()
	 * @generated
	 * @ordered
	 */
	protected static final int CONTRACT_YEAR_START_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getContractYearStart() <em>Contract Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractYearStart()
	 * @generated
	 * @ordered
	 */
	protected int contractYearStart = CONTRACT_YEAR_START_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAllowedPorts() <em>Allowed Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> allowedPorts;

	/**
	 * The cached value of the '{@link #getPreferredPort() <em>Preferred Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreferredPort()
	 * @generated
	 * @ordered
	 */
	protected Port preferredPort;

	/**
	 * The default value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected int minQuantity = MIN_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_QUANTITY_EDEFAULT = 140000;

	/**
	 * The cached value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected int maxQuantity = MAX_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeLimitsUnit() <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 * @ordered
	 */
	protected static final VolumeUnits VOLUME_LIMITS_UNIT_EDEFAULT = VolumeUnits.M3;

	/**
	 * The cached value of the '{@link #getVolumeLimitsUnit() <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 * @ordered
	 */
	protected VolumeUnits volumeLimitsUnit = VOLUME_LIMITS_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getOperationalTolerance() <em>Operational Tolerance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationalTolerance()
	 * @generated
	 * @ordered
	 */
	protected static final double OPERATIONAL_TOLERANCE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getOperationalTolerance() <em>Operational Tolerance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationalTolerance()
	 * @generated
	 * @ordered
	 */
	protected double operationalTolerance = OPERATIONAL_TOLERANCE_EDEFAULT;

	/**
	 * This is true if the Operational Tolerance attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean operationalToleranceESet;

	/**
	 * The default value of the '{@link #isFullCargoLot() <em>Full Cargo Lot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFullCargoLot()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FULL_CARGO_LOT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFullCargoLot() <em>Full Cargo Lot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFullCargoLot()
	 * @generated
	 * @ordered
	 */
	protected boolean fullCargoLot = FULL_CARGO_LOT_EDEFAULT;

	/**
	 * The default value of the '{@link #isRestrictedListsArePermissive() <em>Restricted Lists Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRestrictedListsArePermissive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRestrictedListsArePermissive() <em>Restricted Lists Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRestrictedListsArePermissive()
	 * @generated
	 * @ordered
	 */
	protected boolean restrictedListsArePermissive = RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRestrictedContracts() <em>Restricted Contracts</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRestrictedContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<Contract> restrictedContracts;

	/**
	 * The cached value of the '{@link #getRestrictedPorts() <em>Restricted Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRestrictedPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> restrictedPorts;

	/**
	 * The cached value of the '{@link #getPriceInfo() <em>Price Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceInfo()
	 * @generated
	 * @ordered
	 */
	protected LNGPriceCalculatorParameters priceInfo;

	/**
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected String notes = NOTES_EDEFAULT;

	/**
	 * The default value of the '{@link #getContractType() <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractType()
	 * @generated
	 * @ordered
	 */
	protected static final ContractType CONTRACT_TYPE_EDEFAULT = ContractType.BOTH;

	/**
	 * The cached value of the '{@link #getContractType() <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractType()
	 * @generated
	 * @ordered
	 */
	protected ContractType contractType = CONTRACT_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPricingEvent() <em>Pricing Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingEvent()
	 * @generated
	 * @ordered
	 */
	protected static final PricingEvent PRICING_EVENT_EDEFAULT = PricingEvent.START_LOAD;

	/**
	 * The cached value of the '{@link #getPricingEvent() <em>Pricing Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingEvent()
	 * @generated
	 * @ordered
	 */
	protected PricingEvent pricingEvent = PRICING_EVENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getCancellationExpression() <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCancellationExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String CANCELLATION_EXPRESSION_EDEFAULT = "0";

	/**
	 * The cached value of the '{@link #getCancellationExpression() <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCancellationExpression()
	 * @generated
	 * @ordered
	 */
	protected String cancellationExpression = CANCELLATION_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowNominationSize() <em>Window Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationSize()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_NOMINATION_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowNominationSize() <em>Window Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationSize()
	 * @generated
	 * @ordered
	 */
	protected int windowNominationSize = WINDOW_NOMINATION_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowNominationSizeUnits() <em>Window Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationSizeUnits()
	 * @generated NOT
	 * @ordered
	 */
	protected static final TimePeriod WINDOW_NOMINATION_SIZE_UNITS_EDEFAULT = TimePeriod.DAYS;

	/**
	 * The cached value of the '{@link #getWindowNominationSizeUnits() <em>Window Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod windowNominationSizeUnits = WINDOW_NOMINATION_SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #isWindowNominationCounterparty() <em>Window Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWindowNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WINDOW_NOMINATION_COUNTERPARTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWindowNominationCounterparty() <em>Window Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWindowNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected boolean windowNominationCounterparty = WINDOW_NOMINATION_COUNTERPARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getVesselNominationSize() <em>Vessel Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationSize()
	 * @generated
	 * @ordered
	 */
	protected static final int VESSEL_NOMINATION_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVesselNominationSize() <em>Vessel Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationSize()
	 * @generated
	 * @ordered
	 */
	protected int vesselNominationSize = VESSEL_NOMINATION_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVesselNominationSizeUnits() <em>Vessel Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationSizeUnits()
	 * @generated NOT
	 * @ordered
	 */
	protected static final TimePeriod VESSEL_NOMINATION_SIZE_UNITS_EDEFAULT = TimePeriod.DAYS;

	/**
	 * The cached value of the '{@link #getVesselNominationSizeUnits() <em>Vessel Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod vesselNominationSizeUnits = VESSEL_NOMINATION_SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #isVesselNominationCounterparty() <em>Vessel Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VESSEL_NOMINATION_COUNTERPARTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVesselNominationCounterparty() <em>Vessel Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected boolean vesselNominationCounterparty = VESSEL_NOMINATION_COUNTERPARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeNominationSize() <em>Volume Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationSize()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_NOMINATION_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeNominationSize() <em>Volume Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationSize()
	 * @generated
	 * @ordered
	 */
	protected int volumeNominationSize = VOLUME_NOMINATION_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeNominationSizeUnits() <em>Volume Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationSizeUnits()
	 * @generated NOT
	 * @ordered
	 */
	protected static final TimePeriod VOLUME_NOMINATION_SIZE_UNITS_EDEFAULT = TimePeriod.DAYS;

	/**
	 * The cached value of the '{@link #getVolumeNominationSizeUnits() <em>Volume Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod volumeNominationSizeUnits = VOLUME_NOMINATION_SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #isVolumeNominationCounterparty() <em>Volume Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVolumeNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VOLUME_NOMINATION_COUNTERPARTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVolumeNominationCounterparty() <em>Volume Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVolumeNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected boolean volumeNominationCounterparty = VOLUME_NOMINATION_COUNTERPARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortNominationSize() <em>Port Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationSize()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_NOMINATION_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPortNominationSize() <em>Port Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationSize()
	 * @generated
	 * @ordered
	 */
	protected int portNominationSize = PORT_NOMINATION_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortNominationSizeUnits() <em>Port Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationSizeUnits()
	 * @generated NOT
	 * @ordered
	 */
	protected static final TimePeriod PORT_NOMINATION_SIZE_UNITS_EDEFAULT = TimePeriod.DAYS;

	/**
	 * The cached value of the '{@link #getPortNominationSizeUnits() <em>Port Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod portNominationSizeUnits = PORT_NOMINATION_SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #isPortNominationCounterparty() <em>Port Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PORT_NOMINATION_COUNTERPARTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPortNominationCounterparty() <em>Port Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected boolean portNominationCounterparty = PORT_NOMINATION_COUNTERPARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #isDivertible() <em>Divertible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDivertible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DIVERTIBLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDivertible() <em>Divertible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDivertible()
	 * @generated
	 * @ordered
	 */
	protected boolean divertible = DIVERTIBLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getShippingDaysRestriction() <em>Shipping Days Restriction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingDaysRestriction()
	 * @generated
	 * @ordered
	 */
	protected static final int SHIPPING_DAYS_RESTRICTION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getShippingDaysRestriction() <em>Shipping Days Restriction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingDaysRestriction()
	 * @generated
	 * @ordered
	 */
	protected int shippingDaysRestriction = SHIPPING_DAYS_RESTRICTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortLoadNominationSize() <em>Port Load Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationSize()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_LOAD_NOMINATION_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPortLoadNominationSize() <em>Port Load Nomination Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationSize()
	 * @generated
	 * @ordered
	 */
	protected int portLoadNominationSize = PORT_LOAD_NOMINATION_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortLoadNominationSizeUnits() <em>Port Load Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final TimePeriod PORT_LOAD_NOMINATION_SIZE_UNITS_EDEFAULT = TimePeriod.HOURS;

	/**
	 * The cached value of the '{@link #getPortLoadNominationSizeUnits() <em>Port Load Nomination Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod portLoadNominationSizeUnits = PORT_LOAD_NOMINATION_SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #isPortLoadNominationCounterparty() <em>Port Load Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortLoadNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PORT_LOAD_NOMINATION_COUNTERPARTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPortLoadNominationCounterparty() <em>Port Load Nomination Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortLoadNominationCounterparty()
	 * @generated
	 * @ordered
	 */
	protected boolean portLoadNominationCounterparty = PORT_LOAD_NOMINATION_COUNTERPARTY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseLegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (BaseLegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.CONTRACT__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEntity(BaseLegalEntity newEntity) {
		BaseLegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getAllowedPorts() {
		if (allowedPorts == null) {
			allowedPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.CONTRACT__ALLOWED_PORTS);
		}
		return allowedPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getPreferredPort() {
		if (preferredPort != null && preferredPort.eIsProxy()) {
			InternalEObject oldPreferredPort = (InternalEObject)preferredPort;
			preferredPort = (Port)eResolveProxy(oldPreferredPort);
			if (preferredPort != oldPreferredPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.CONTRACT__PREFERRED_PORT, oldPreferredPort, preferredPort));
			}
		}
		return preferredPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPreferredPort() {
		return preferredPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPreferredPort(Port newPreferredPort) {
		Port oldPreferredPort = preferredPort;
		preferredPort = newPreferredPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PREFERRED_PORT, oldPreferredPort, preferredPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinQuantity() {
		return minQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinQuantity(int newMinQuantity) {
		int oldMinQuantity = minQuantity;
		minQuantity = newMinQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__MIN_QUANTITY, oldMinQuantity, minQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxQuantity() {
		return maxQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxQuantity(int newMaxQuantity) {
		int oldMaxQuantity = maxQuantity;
		maxQuantity = newMaxQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__MAX_QUANTITY, oldMaxQuantity, maxQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getOperationalTolerance() {
		return operationalTolerance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperationalTolerance(double newOperationalTolerance) {
		double oldOperationalTolerance = operationalTolerance;
		operationalTolerance = newOperationalTolerance;
		boolean oldOperationalToleranceESet = operationalToleranceESet;
		operationalToleranceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__OPERATIONAL_TOLERANCE, oldOperationalTolerance, operationalTolerance, !oldOperationalToleranceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetOperationalTolerance() {
		double oldOperationalTolerance = operationalTolerance;
		boolean oldOperationalToleranceESet = operationalToleranceESet;
		operationalTolerance = OPERATIONAL_TOLERANCE_EDEFAULT;
		operationalToleranceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.CONTRACT__OPERATIONAL_TOLERANCE, oldOperationalTolerance, OPERATIONAL_TOLERANCE_EDEFAULT, oldOperationalToleranceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetOperationalTolerance() {
		return operationalToleranceESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFullCargoLot() {
		return fullCargoLot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFullCargoLot(boolean newFullCargoLot) {
		boolean oldFullCargoLot = fullCargoLot;
		fullCargoLot = newFullCargoLot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__FULL_CARGO_LOT, oldFullCargoLot, fullCargoLot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VolumeUnits getVolumeLimitsUnit() {
		return volumeLimitsUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeLimitsUnit(VolumeUnits newVolumeLimitsUnit) {
		VolumeUnits oldVolumeLimitsUnit = volumeLimitsUnit;
		volumeLimitsUnit = newVolumeLimitsUnit == null ? VOLUME_LIMITS_UNIT_EDEFAULT : newVolumeLimitsUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__VOLUME_LIMITS_UNIT, oldVolumeLimitsUnit, volumeLimitsUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRestrictedListsArePermissive() {
		return restrictedListsArePermissive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRestrictedListsArePermissive(boolean newRestrictedListsArePermissive) {
		boolean oldRestrictedListsArePermissive = restrictedListsArePermissive;
		restrictedListsArePermissive = newRestrictedListsArePermissive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE, oldRestrictedListsArePermissive, restrictedListsArePermissive));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Contract> getRestrictedContracts() {
		if (restrictedContracts == null) {
			restrictedContracts = new EObjectResolvingEList<Contract>(Contract.class, this, CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS);
		}
		return restrictedContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getRestrictedPorts() {
		if (restrictedPorts == null) {
			restrictedPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.CONTRACT__RESTRICTED_PORTS);
		}
		return restrictedPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LNGPriceCalculatorParameters getPriceInfo() {
		return priceInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPriceInfo(LNGPriceCalculatorParameters newPriceInfo, NotificationChain msgs) {
		LNGPriceCalculatorParameters oldPriceInfo = priceInfo;
		priceInfo = newPriceInfo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PRICE_INFO, oldPriceInfo, newPriceInfo);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceInfo(LNGPriceCalculatorParameters newPriceInfo) {
		if (newPriceInfo != priceInfo) {
			NotificationChain msgs = null;
			if (priceInfo != null)
				msgs = ((InternalEObject)priceInfo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.CONTRACT__PRICE_INFO, null, msgs);
			if (newPriceInfo != null)
				msgs = ((InternalEObject)newPriceInfo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.CONTRACT__PRICE_INFO, null, msgs);
			msgs = basicSetPriceInfo(newPriceInfo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PRICE_INFO, newPriceInfo, newPriceInfo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNotes(String newNotes) {
		String oldNotes = notes;
		notes = newNotes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractType getContractType() {
		return contractType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContractType(ContractType newContractType) {
		ContractType oldContractType = contractType;
		contractType = newContractType == null ? CONTRACT_TYPE_EDEFAULT : newContractType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__CONTRACT_TYPE, oldContractType, contractType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingEvent getPricingEvent() {
		return pricingEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingEvent(PricingEvent newPricingEvent) {
		PricingEvent oldPricingEvent = pricingEvent;
		pricingEvent = newPricingEvent == null ? PRICING_EVENT_EDEFAULT : newPricingEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PRICING_EVENT, oldPricingEvent, pricingEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCancellationExpression() {
		return cancellationExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCancellationExpression(String newCancellationExpression) {
		String oldCancellationExpression = cancellationExpression;
		cancellationExpression = newCancellationExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__CANCELLATION_EXPRESSION, oldCancellationExpression, cancellationExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowNominationSize() {
		return windowNominationSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowNominationSize(int newWindowNominationSize) {
		int oldWindowNominationSize = windowNominationSize;
		windowNominationSize = newWindowNominationSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE, oldWindowNominationSize, windowNominationSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getWindowNominationSizeUnits() {
		return windowNominationSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowNominationSizeUnits(TimePeriod newWindowNominationSizeUnits) {
		TimePeriod oldWindowNominationSizeUnits = windowNominationSizeUnits;
		windowNominationSizeUnits = newWindowNominationSizeUnits == null ? WINDOW_NOMINATION_SIZE_UNITS_EDEFAULT : newWindowNominationSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS, oldWindowNominationSizeUnits, windowNominationSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isWindowNominationCounterparty() {
		return windowNominationCounterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowNominationCounterparty(boolean newWindowNominationCounterparty) {
		boolean oldWindowNominationCounterparty = windowNominationCounterparty;
		windowNominationCounterparty = newWindowNominationCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY, oldWindowNominationCounterparty, windowNominationCounterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isDivertible() {
		return divertible;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDivertible(boolean newDivertible) {
		boolean oldDivertible = divertible;
		divertible = newDivertible;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__DIVERTIBLE, oldDivertible, divertible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getShippingDaysRestriction() {
		return shippingDaysRestriction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShippingDaysRestriction(int newShippingDaysRestriction) {
		int oldShippingDaysRestriction = shippingDaysRestriction;
		shippingDaysRestriction = newShippingDaysRestriction;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__SHIPPING_DAYS_RESTRICTION, oldShippingDaysRestriction, shippingDaysRestriction));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPortLoadNominationSize() {
		return portLoadNominationSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortLoadNominationSize(int newPortLoadNominationSize) {
		int oldPortLoadNominationSize = portLoadNominationSize;
		portLoadNominationSize = newPortLoadNominationSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE, oldPortLoadNominationSize, portLoadNominationSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getPortLoadNominationSizeUnits() {
		return portLoadNominationSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortLoadNominationSizeUnits(TimePeriod newPortLoadNominationSizeUnits) {
		TimePeriod oldPortLoadNominationSizeUnits = portLoadNominationSizeUnits;
		portLoadNominationSizeUnits = newPortLoadNominationSizeUnits == null ? PORT_LOAD_NOMINATION_SIZE_UNITS_EDEFAULT : newPortLoadNominationSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE_UNITS, oldPortLoadNominationSizeUnits, portLoadNominationSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPortLoadNominationCounterparty() {
		return portLoadNominationCounterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortLoadNominationCounterparty(boolean newPortLoadNominationCounterparty) {
		boolean oldPortLoadNominationCounterparty = portLoadNominationCounterparty;
		portLoadNominationCounterparty = newPortLoadNominationCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY, oldPortLoadNominationCounterparty, portLoadNominationCounterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVesselNominationSize() {
		return vesselNominationSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselNominationSize(int newVesselNominationSize) {
		int oldVesselNominationSize = vesselNominationSize;
		vesselNominationSize = newVesselNominationSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE, oldVesselNominationSize, vesselNominationSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getVesselNominationSizeUnits() {
		return vesselNominationSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselNominationSizeUnits(TimePeriod newVesselNominationSizeUnits) {
		TimePeriod oldVesselNominationSizeUnits = vesselNominationSizeUnits;
		vesselNominationSizeUnits = newVesselNominationSizeUnits == null ? VESSEL_NOMINATION_SIZE_UNITS_EDEFAULT : newVesselNominationSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS, oldVesselNominationSizeUnits, vesselNominationSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isVesselNominationCounterparty() {
		return vesselNominationCounterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselNominationCounterparty(boolean newVesselNominationCounterparty) {
		boolean oldVesselNominationCounterparty = vesselNominationCounterparty;
		vesselNominationCounterparty = newVesselNominationCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY, oldVesselNominationCounterparty, vesselNominationCounterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVolumeNominationSize() {
		return volumeNominationSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeNominationSize(int newVolumeNominationSize) {
		int oldVolumeNominationSize = volumeNominationSize;
		volumeNominationSize = newVolumeNominationSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE, oldVolumeNominationSize, volumeNominationSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getVolumeNominationSizeUnits() {
		return volumeNominationSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeNominationSizeUnits(TimePeriod newVolumeNominationSizeUnits) {
		TimePeriod oldVolumeNominationSizeUnits = volumeNominationSizeUnits;
		volumeNominationSizeUnits = newVolumeNominationSizeUnits == null ? VOLUME_NOMINATION_SIZE_UNITS_EDEFAULT : newVolumeNominationSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS, oldVolumeNominationSizeUnits, volumeNominationSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isVolumeNominationCounterparty() {
		return volumeNominationCounterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeNominationCounterparty(boolean newVolumeNominationCounterparty) {
		boolean oldVolumeNominationCounterparty = volumeNominationCounterparty;
		volumeNominationCounterparty = newVolumeNominationCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY, oldVolumeNominationCounterparty, volumeNominationCounterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPortNominationSize() {
		return portNominationSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortNominationSize(int newPortNominationSize) {
		int oldPortNominationSize = portNominationSize;
		portNominationSize = newPortNominationSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE, oldPortNominationSize, portNominationSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getPortNominationSizeUnits() {
		return portNominationSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortNominationSizeUnits(TimePeriod newPortNominationSizeUnits) {
		TimePeriod oldPortNominationSizeUnits = portNominationSizeUnits;
		portNominationSizeUnits = newPortNominationSizeUnits == null ? PORT_NOMINATION_SIZE_UNITS_EDEFAULT : newPortNominationSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE_UNITS, oldPortNominationSizeUnits, portNominationSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPortNominationCounterparty() {
		return portNominationCounterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortNominationCounterparty(boolean newPortNominationCounterparty) {
		boolean oldPortNominationCounterparty = portNominationCounterparty;
		portNominationCounterparty = newPortNominationCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PORT_NOMINATION_COUNTERPARTY, oldPortNominationCounterparty, portNominationCounterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getContractYearStart() {
		return contractYearStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContractYearStart(int newContractYearStart) {
		int oldContractYearStart = contractYearStart;
		contractYearStart = newContractYearStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__CONTRACT_YEAR_START, oldContractYearStart, contractYearStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCode(String newCode) {
		String oldCode = code;
		code = newCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__CODE, oldCode, code));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCounterparty() {
		return counterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCounterparty(String newCounterparty) {
		String oldCounterparty = counterparty;
		counterparty = newCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__COUNTERPARTY, oldCounterparty, counterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCn() {
		return cn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCn(String newCn) {
		String oldCn = cn;
		cn = newCn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__CN, oldCn, cn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getStartDate() {
		return startDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartDate(YearMonth newStartDate) {
		YearMonth oldStartDate = startDate;
		startDate = newStartDate;
		boolean oldStartDateESet = startDateESet;
		startDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__START_DATE, oldStartDate, startDate, !oldStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetStartDate() {
		YearMonth oldStartDate = startDate;
		boolean oldStartDateESet = startDateESet;
		startDate = START_DATE_EDEFAULT;
		startDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.CONTRACT__START_DATE, oldStartDate, START_DATE_EDEFAULT, oldStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetStartDate() {
		return startDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getEndDate() {
		return endDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndDate(YearMonth newEndDate) {
		YearMonth oldEndDate = endDate;
		endDate = newEndDate;
		boolean oldEndDateESet = endDateESet;
		endDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__END_DATE, oldEndDate, endDate, !oldEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetEndDate() {
		YearMonth oldEndDate = endDate;
		boolean oldEndDateESet = endDateESet;
		endDate = END_DATE_EDEFAULT;
		endDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.CONTRACT__END_DATE, oldEndDate, END_DATE_EDEFAULT, oldEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetEndDate() {
		return endDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.CONTRACT__PRICE_INFO:
				return basicSetPriceInfo(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.CONTRACT__NAME:
				return getName();
			case CommercialPackage.CONTRACT__CODE:
				return getCode();
			case CommercialPackage.CONTRACT__COUNTERPARTY:
				return getCounterparty();
			case CommercialPackage.CONTRACT__CN:
				return getCn();
			case CommercialPackage.CONTRACT__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CommercialPackage.CONTRACT__START_DATE:
				return getStartDate();
			case CommercialPackage.CONTRACT__END_DATE:
				return getEndDate();
			case CommercialPackage.CONTRACT__CONTRACT_YEAR_START:
				return getContractYearStart();
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				return getAllowedPorts();
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				if (resolve) return getPreferredPort();
				return basicGetPreferredPort();
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				return getMinQuantity();
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				return getMaxQuantity();
			case CommercialPackage.CONTRACT__VOLUME_LIMITS_UNIT:
				return getVolumeLimitsUnit();
			case CommercialPackage.CONTRACT__OPERATIONAL_TOLERANCE:
				return getOperationalTolerance();
			case CommercialPackage.CONTRACT__FULL_CARGO_LOT:
				return isFullCargoLot();
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				return isRestrictedListsArePermissive();
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				return getRestrictedContracts();
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				return getRestrictedPorts();
			case CommercialPackage.CONTRACT__PRICE_INFO:
				return getPriceInfo();
			case CommercialPackage.CONTRACT__NOTES:
				return getNotes();
			case CommercialPackage.CONTRACT__CONTRACT_TYPE:
				return getContractType();
			case CommercialPackage.CONTRACT__PRICING_EVENT:
				return getPricingEvent();
			case CommercialPackage.CONTRACT__CANCELLATION_EXPRESSION:
				return getCancellationExpression();
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE:
				return getWindowNominationSize();
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS:
				return getWindowNominationSizeUnits();
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY:
				return isWindowNominationCounterparty();
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE:
				return getVesselNominationSize();
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS:
				return getVesselNominationSizeUnits();
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY:
				return isVesselNominationCounterparty();
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE:
				return getVolumeNominationSize();
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS:
				return getVolumeNominationSizeUnits();
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY:
				return isVolumeNominationCounterparty();
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE:
				return getPortNominationSize();
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE_UNITS:
				return getPortNominationSizeUnits();
			case CommercialPackage.CONTRACT__PORT_NOMINATION_COUNTERPARTY:
				return isPortNominationCounterparty();
			case CommercialPackage.CONTRACT__DIVERTIBLE:
				return isDivertible();
			case CommercialPackage.CONTRACT__SHIPPING_DAYS_RESTRICTION:
				return getShippingDaysRestriction();
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE:
				return getPortLoadNominationSize();
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE_UNITS:
				return getPortLoadNominationSizeUnits();
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				return isPortLoadNominationCounterparty();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CommercialPackage.CONTRACT__NAME:
				setName((String)newValue);
				return;
			case CommercialPackage.CONTRACT__CODE:
				setCode((String)newValue);
				return;
			case CommercialPackage.CONTRACT__COUNTERPARTY:
				setCounterparty((String)newValue);
				return;
			case CommercialPackage.CONTRACT__CN:
				setCn((String)newValue);
				return;
			case CommercialPackage.CONTRACT__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case CommercialPackage.CONTRACT__START_DATE:
				setStartDate((YearMonth)newValue);
				return;
			case CommercialPackage.CONTRACT__END_DATE:
				setEndDate((YearMonth)newValue);
				return;
			case CommercialPackage.CONTRACT__CONTRACT_YEAR_START:
				setContractYearStart((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				getAllowedPorts().clear();
				getAllowedPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				setPreferredPort((Port)newValue);
				return;
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				setMinQuantity((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				setMaxQuantity((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__VOLUME_LIMITS_UNIT:
				setVolumeLimitsUnit((VolumeUnits)newValue);
				return;
			case CommercialPackage.CONTRACT__OPERATIONAL_TOLERANCE:
				setOperationalTolerance((Double)newValue);
				return;
			case CommercialPackage.CONTRACT__FULL_CARGO_LOT:
				setFullCargoLot((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				setRestrictedListsArePermissive((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				getRestrictedContracts().clear();
				getRestrictedContracts().addAll((Collection<? extends Contract>)newValue);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				getRestrictedPorts().clear();
				getRestrictedPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CommercialPackage.CONTRACT__PRICE_INFO:
				setPriceInfo((LNGPriceCalculatorParameters)newValue);
				return;
			case CommercialPackage.CONTRACT__NOTES:
				setNotes((String)newValue);
				return;
			case CommercialPackage.CONTRACT__CONTRACT_TYPE:
				setContractType((ContractType)newValue);
				return;
			case CommercialPackage.CONTRACT__PRICING_EVENT:
				setPricingEvent((PricingEvent)newValue);
				return;
			case CommercialPackage.CONTRACT__CANCELLATION_EXPRESSION:
				setCancellationExpression((String)newValue);
				return;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE:
				setWindowNominationSize((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS:
				setWindowNominationSizeUnits((TimePeriod)newValue);
				return;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY:
				setWindowNominationCounterparty((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE:
				setVesselNominationSize((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS:
				setVesselNominationSizeUnits((TimePeriod)newValue);
				return;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY:
				setVesselNominationCounterparty((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE:
				setVolumeNominationSize((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS:
				setVolumeNominationSizeUnits((TimePeriod)newValue);
				return;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY:
				setVolumeNominationCounterparty((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE:
				setPortNominationSize((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE_UNITS:
				setPortNominationSizeUnits((TimePeriod)newValue);
				return;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_COUNTERPARTY:
				setPortNominationCounterparty((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__DIVERTIBLE:
				setDivertible((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__SHIPPING_DAYS_RESTRICTION:
				setShippingDaysRestriction((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE:
				setPortLoadNominationSize((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE_UNITS:
				setPortLoadNominationSizeUnits((TimePeriod)newValue);
				return;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				setPortLoadNominationCounterparty((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CommercialPackage.CONTRACT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__CODE:
				setCode(CODE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__COUNTERPARTY:
				setCounterparty(COUNTERPARTY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__CN:
				setCn(CN_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case CommercialPackage.CONTRACT__START_DATE:
				unsetStartDate();
				return;
			case CommercialPackage.CONTRACT__END_DATE:
				unsetEndDate();
				return;
			case CommercialPackage.CONTRACT__CONTRACT_YEAR_START:
				setContractYearStart(CONTRACT_YEAR_START_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				getAllowedPorts().clear();
				return;
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				setPreferredPort((Port)null);
				return;
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				setMinQuantity(MIN_QUANTITY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				setMaxQuantity(MAX_QUANTITY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__VOLUME_LIMITS_UNIT:
				setVolumeLimitsUnit(VOLUME_LIMITS_UNIT_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__OPERATIONAL_TOLERANCE:
				unsetOperationalTolerance();
				return;
			case CommercialPackage.CONTRACT__FULL_CARGO_LOT:
				setFullCargoLot(FULL_CARGO_LOT_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				setRestrictedListsArePermissive(RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				getRestrictedContracts().clear();
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				getRestrictedPorts().clear();
				return;
			case CommercialPackage.CONTRACT__PRICE_INFO:
				setPriceInfo((LNGPriceCalculatorParameters)null);
				return;
			case CommercialPackage.CONTRACT__NOTES:
				setNotes(NOTES_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__CONTRACT_TYPE:
				setContractType(CONTRACT_TYPE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__PRICING_EVENT:
				setPricingEvent(PRICING_EVENT_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__CANCELLATION_EXPRESSION:
				setCancellationExpression(CANCELLATION_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE:
				setWindowNominationSize(WINDOW_NOMINATION_SIZE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS:
				setWindowNominationSizeUnits(WINDOW_NOMINATION_SIZE_UNITS_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY:
				setWindowNominationCounterparty(WINDOW_NOMINATION_COUNTERPARTY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE:
				setVesselNominationSize(VESSEL_NOMINATION_SIZE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS:
				setVesselNominationSizeUnits(VESSEL_NOMINATION_SIZE_UNITS_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY:
				setVesselNominationCounterparty(VESSEL_NOMINATION_COUNTERPARTY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE:
				setVolumeNominationSize(VOLUME_NOMINATION_SIZE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS:
				setVolumeNominationSizeUnits(VOLUME_NOMINATION_SIZE_UNITS_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY:
				setVolumeNominationCounterparty(VOLUME_NOMINATION_COUNTERPARTY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE:
				setPortNominationSize(PORT_NOMINATION_SIZE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE_UNITS:
				setPortNominationSizeUnits(PORT_NOMINATION_SIZE_UNITS_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_COUNTERPARTY:
				setPortNominationCounterparty(PORT_NOMINATION_COUNTERPARTY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__DIVERTIBLE:
				setDivertible(DIVERTIBLE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__SHIPPING_DAYS_RESTRICTION:
				setShippingDaysRestriction(SHIPPING_DAYS_RESTRICTION_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE:
				setPortLoadNominationSize(PORT_LOAD_NOMINATION_SIZE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE_UNITS:
				setPortLoadNominationSizeUnits(PORT_LOAD_NOMINATION_SIZE_UNITS_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				setPortLoadNominationCounterparty(PORT_LOAD_NOMINATION_COUNTERPARTY_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CommercialPackage.CONTRACT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CommercialPackage.CONTRACT__CODE:
				return CODE_EDEFAULT == null ? code != null : !CODE_EDEFAULT.equals(code);
			case CommercialPackage.CONTRACT__COUNTERPARTY:
				return COUNTERPARTY_EDEFAULT == null ? counterparty != null : !COUNTERPARTY_EDEFAULT.equals(counterparty);
			case CommercialPackage.CONTRACT__CN:
				return CN_EDEFAULT == null ? cn != null : !CN_EDEFAULT.equals(cn);
			case CommercialPackage.CONTRACT__ENTITY:
				return entity != null;
			case CommercialPackage.CONTRACT__START_DATE:
				return isSetStartDate();
			case CommercialPackage.CONTRACT__END_DATE:
				return isSetEndDate();
			case CommercialPackage.CONTRACT__CONTRACT_YEAR_START:
				return contractYearStart != CONTRACT_YEAR_START_EDEFAULT;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				return allowedPorts != null && !allowedPorts.isEmpty();
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				return preferredPort != null;
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				return minQuantity != MIN_QUANTITY_EDEFAULT;
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				return maxQuantity != MAX_QUANTITY_EDEFAULT;
			case CommercialPackage.CONTRACT__VOLUME_LIMITS_UNIT:
				return volumeLimitsUnit != VOLUME_LIMITS_UNIT_EDEFAULT;
			case CommercialPackage.CONTRACT__OPERATIONAL_TOLERANCE:
				return isSetOperationalTolerance();
			case CommercialPackage.CONTRACT__FULL_CARGO_LOT:
				return fullCargoLot != FULL_CARGO_LOT_EDEFAULT;
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				return restrictedListsArePermissive != RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT;
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				return restrictedContracts != null && !restrictedContracts.isEmpty();
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				return restrictedPorts != null && !restrictedPorts.isEmpty();
			case CommercialPackage.CONTRACT__PRICE_INFO:
				return priceInfo != null;
			case CommercialPackage.CONTRACT__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
			case CommercialPackage.CONTRACT__CONTRACT_TYPE:
				return contractType != CONTRACT_TYPE_EDEFAULT;
			case CommercialPackage.CONTRACT__PRICING_EVENT:
				return pricingEvent != PRICING_EVENT_EDEFAULT;
			case CommercialPackage.CONTRACT__CANCELLATION_EXPRESSION:
				return CANCELLATION_EXPRESSION_EDEFAULT == null ? cancellationExpression != null : !CANCELLATION_EXPRESSION_EDEFAULT.equals(cancellationExpression);
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE:
				return windowNominationSize != WINDOW_NOMINATION_SIZE_EDEFAULT;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS:
				return windowNominationSizeUnits != WINDOW_NOMINATION_SIZE_UNITS_EDEFAULT;
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY:
				return windowNominationCounterparty != WINDOW_NOMINATION_COUNTERPARTY_EDEFAULT;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE:
				return vesselNominationSize != VESSEL_NOMINATION_SIZE_EDEFAULT;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS:
				return vesselNominationSizeUnits != VESSEL_NOMINATION_SIZE_UNITS_EDEFAULT;
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY:
				return vesselNominationCounterparty != VESSEL_NOMINATION_COUNTERPARTY_EDEFAULT;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE:
				return volumeNominationSize != VOLUME_NOMINATION_SIZE_EDEFAULT;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS:
				return volumeNominationSizeUnits != VOLUME_NOMINATION_SIZE_UNITS_EDEFAULT;
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY:
				return volumeNominationCounterparty != VOLUME_NOMINATION_COUNTERPARTY_EDEFAULT;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE:
				return portNominationSize != PORT_NOMINATION_SIZE_EDEFAULT;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE_UNITS:
				return portNominationSizeUnits != PORT_NOMINATION_SIZE_UNITS_EDEFAULT;
			case CommercialPackage.CONTRACT__PORT_NOMINATION_COUNTERPARTY:
				return portNominationCounterparty != PORT_NOMINATION_COUNTERPARTY_EDEFAULT;
			case CommercialPackage.CONTRACT__DIVERTIBLE:
				return divertible != DIVERTIBLE_EDEFAULT;
			case CommercialPackage.CONTRACT__SHIPPING_DAYS_RESTRICTION:
				return shippingDaysRestriction != SHIPPING_DAYS_RESTRICTION_EDEFAULT;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE:
				return portLoadNominationSize != PORT_LOAD_NOMINATION_SIZE_EDEFAULT;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE_UNITS:
				return portLoadNominationSizeUnits != PORT_LOAD_NOMINATION_SIZE_UNITS_EDEFAULT;
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				return portLoadNominationCounterparty != PORT_LOAD_NOMINATION_COUNTERPARTY_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case CommercialPackage.CONTRACT__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return CommercialPackage.CONTRACT__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", code: ");
		result.append(code);
		result.append(", counterparty: ");
		result.append(counterparty);
		result.append(", cn: ");
		result.append(cn);
		result.append(", startDate: ");
		if (startDateESet) result.append(startDate); else result.append("<unset>");
		result.append(", endDate: ");
		if (endDateESet) result.append(endDate); else result.append("<unset>");
		result.append(", contractYearStart: ");
		result.append(contractYearStart);
		result.append(", minQuantity: ");
		result.append(minQuantity);
		result.append(", maxQuantity: ");
		result.append(maxQuantity);
		result.append(", volumeLimitsUnit: ");
		result.append(volumeLimitsUnit);
		result.append(", operationalTolerance: ");
		if (operationalToleranceESet) result.append(operationalTolerance); else result.append("<unset>");
		result.append(", fullCargoLot: ");
		result.append(fullCargoLot);
		result.append(", restrictedListsArePermissive: ");
		result.append(restrictedListsArePermissive);
		result.append(", notes: ");
		result.append(notes);
		result.append(", contractType: ");
		result.append(contractType);
		result.append(", pricingEvent: ");
		result.append(pricingEvent);
		result.append(", cancellationExpression: ");
		result.append(cancellationExpression);
		result.append(", windowNominationSize: ");
		result.append(windowNominationSize);
		result.append(", windowNominationSizeUnits: ");
		result.append(windowNominationSizeUnits);
		result.append(", windowNominationCounterparty: ");
		result.append(windowNominationCounterparty);
		result.append(", vesselNominationSize: ");
		result.append(vesselNominationSize);
		result.append(", vesselNominationSizeUnits: ");
		result.append(vesselNominationSizeUnits);
		result.append(", vesselNominationCounterparty: ");
		result.append(vesselNominationCounterparty);
		result.append(", volumeNominationSize: ");
		result.append(volumeNominationSize);
		result.append(", volumeNominationSizeUnits: ");
		result.append(volumeNominationSizeUnits);
		result.append(", volumeNominationCounterparty: ");
		result.append(volumeNominationCounterparty);
		result.append(", portNominationSize: ");
		result.append(portNominationSize);
		result.append(", portNominationSizeUnits: ");
		result.append(portNominationSizeUnits);
		result.append(", portNominationCounterparty: ");
		result.append(portNominationCounterparty);
		result.append(", divertible: ");
		result.append(divertible);
		result.append(", shippingDaysRestriction: ");
		result.append(shippingDaysRestriction);
		result.append(", portLoadNominationSize: ");
		result.append(portLoadNominationSize);
		result.append(", portLoadNominationSizeUnits: ");
		result.append(portLoadNominationSizeUnits);
		result.append(", portLoadNominationCounterparty: ");
		result.append(portLoadNominationCounterparty);
		result.append(')');
		return result.toString();
	}

} // end of ContractImpl

// finish type fixing

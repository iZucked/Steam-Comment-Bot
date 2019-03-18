/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.NominationUtils;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.MMXObject.DelegateInformation;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Slot</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getCn <em>Cn</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowStartTime <em>Window Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowSizeUnits <em>Window Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowFlex <em>Window Flex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowFlexUnits <em>Window Flex Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getVolumeLimitsUnit <em>Volume Limits Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getOperationalTolerance <em>Operational Tolerance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isFullCargoLot <em>Full Cargo Lot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getCargo <em>Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPricingEvent <em>Pricing Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPricingDate <em>Pricing Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isDivertible <em>Divertible</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getShippingDaysRestriction <em>Shipping Days Restriction</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getRestrictedContracts <em>Restricted Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getRestrictedPorts <em>Restricted Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getRestrictedSlots <em>Restricted Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isRestrictedSlotsArePermissive <em>Restricted Slots Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getHedges <em>Hedges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getMiscCosts <em>Misc Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isOverrideRestrictions <em>Override Restrictions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getNominatedVessel <em>Nominated Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowNominationDate <em>Window Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isWindowNominationIsDone <em>Window Nomination Is Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isWindowNominationCounterparty <em>Window Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getWindowNominationComment <em>Window Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getVolumeNominationDate <em>Volume Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isVolumeNominationDone <em>Volume Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isVolumeNominationCounterparty <em>Volume Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getVolumeNominationComment <em>Volume Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getVesselNominationDate <em>Vessel Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isVesselNominationDone <em>Vessel Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isVesselNominationCounterparty <em>Vessel Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getVesselNominationComment <em>Vessel Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPortNominationDate <em>Port Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isPortNominationDone <em>Port Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isPortNominationCounterparty <em>Port Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPortNominationComment <em>Port Nomination Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPortLoadNominationDate <em>Port Load Nomination Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isPortLoadNominationDone <em>Port Load Nomination Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#isPortLoadNominationCounterparty <em>Port Load Nomination Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl#getPortLoadNominationComment <em>Port Load Nomination Comment</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class SlotImpl<T extends Contract> extends UUIDObjectImpl implements Slot<T> {
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
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected T contract;

	/**
	 * This is true if the Contract reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean contractESet;

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
	 * This is true if the Counterparty attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean counterpartyESet;

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
	 * This is true if the Cn attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cnESet;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The default value of the '{@link #getWindowStart() <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate WINDOW_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWindowStart() <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWindowStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDate windowStart = WINDOW_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowStartTime() <em>Window Start Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWindowStartTime()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_START_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowStartTime() <em>Window Start Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWindowStartTime()
	 * @generated
	 * @ordered
	 */
	protected int windowStartTime = WINDOW_START_TIME_EDEFAULT;

	/**
	 * This is true if the Window Start Time attribute has been set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean windowStartTimeESet;

	/**
	 * The default value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected int windowSize = WINDOW_SIZE_EDEFAULT;

	/**
	 * This is true if the Window Size attribute has been set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean windowSizeESet;

	/**
	 * The default value of the '{@link #getWindowSizeUnits() <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final TimePeriod WINDOW_SIZE_UNITS_EDEFAULT = TimePeriod.HOURS;

	/**
	 * The cached value of the '{@link #getWindowSizeUnits() <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod windowSizeUnits = WINDOW_SIZE_UNITS_EDEFAULT;

	/**
	 * This is true if the Window Size Units attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean windowSizeUnitsESet;

	/**
	 * The default value of the '{@link #getWindowFlex() <em>Window Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowFlex()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_FLEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowFlex() <em>Window Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowFlex()
	 * @generated
	 * @ordered
	 */
	protected int windowFlex = WINDOW_FLEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowFlexUnits() <em>Window Flex Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowFlexUnits()
	 * @generated
	 * @ordered
	 */
	protected static final TimePeriod WINDOW_FLEX_UNITS_EDEFAULT = TimePeriod.HOURS;

	/**
	 * The cached value of the '{@link #getWindowFlexUnits() <em>Window Flex Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowFlexUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod windowFlexUnits = WINDOW_FLEX_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected int duration = DURATION_EDEFAULT;

	/**
	 * This is true if the Duration attribute has been set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean durationESet;

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
	 * This is true if the Volume Limits Unit attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean volumeLimitsUnitESet;

	/**
	 * The default value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected int minQuantity = MIN_QUANTITY_EDEFAULT;

	/**
	 * This is true if the Min Quantity attribute has been set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minQuantityESet;

	/**
	 * The default value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_QUANTITY_EDEFAULT = 140000;

	/**
	 * The cached value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected int maxQuantity = MAX_QUANTITY_EDEFAULT;

	/**
	 * This is true if the Max Quantity attribute has been set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxQuantityESet;

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
	 * This is true if the Full Cargo Lot attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fullCargoLotESet;

	/**
	 * The default value of the '{@link #isOptional() <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptional()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPTIONAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOptional() <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptional()
	 * @generated
	 * @ordered
	 */
	protected boolean optional = OPTIONAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * This is true if the Price Expression attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean priceExpressionESet;

	/**
	 * The cached value of the '{@link #getCargo() <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargo()
	 * @generated
	 * @ordered
	 */
	protected Cargo cargo;

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
	 * This is true if the Pricing Event attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pricingEventESet;

	/**
	 * The default value of the '{@link #getPricingDate() <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PRICING_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingDate() <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate pricingDate = PRICING_DATE_EDEFAULT;

	/**
	 * This is true if the Pricing Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pricingDateESet;

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
	 * This is true if the Divertible attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean divertibleESet;

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
	 * This is true if the Shipping Days Restriction attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean shippingDaysRestrictionESet;

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
	 * This is true if the Entity reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean entityESet;

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
	protected EList<Port> restrictedPorts;

	/**
	 * The cached value of the '{@link #getRestrictedSlots() <em>Restricted Slots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRestrictedSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot<?>> restrictedSlots;

	/**
	 * The default value of the '{@link #isRestrictedSlotsArePermissive() <em>Restricted Slots Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRestrictedSlotsArePermissive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RESTRICTED_SLOTS_ARE_PERMISSIVE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRestrictedSlotsArePermissive() <em>Restricted Slots Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRestrictedSlotsArePermissive()
	 * @generated
	 * @ordered
	 */
	protected boolean restrictedSlotsArePermissive = RESTRICTED_SLOTS_ARE_PERMISSIVE_EDEFAULT;

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
	 * The default value of the '{@link #getHedges() <em>Hedges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHedges()
	 * @generated
	 * @ordered
	 */
	protected static final int HEDGES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHedges() <em>Hedges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHedges()
	 * @generated
	 * @ordered
	 */
	protected int hedges = HEDGES_EDEFAULT;

	/**
	 * The default value of the '{@link #getMiscCosts() <em>Misc Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMiscCosts()
	 * @generated
	 * @ordered
	 */
	protected static final int MISC_COSTS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMiscCosts() <em>Misc Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMiscCosts()
	 * @generated
	 * @ordered
	 */
	protected int miscCosts = MISC_COSTS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAllowedVessels() <em>Allowed Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet<Vessel>> allowedVessels;

	/**
	 * The default value of the '{@link #getCancellationExpression() <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCancellationExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String CANCELLATION_EXPRESSION_EDEFAULT = null;

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
	 * This is true if the Cancellation Expression attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cancellationExpressionESet;

	/**
	 * The default value of the '{@link #isOverrideRestrictions() <em>Override Restrictions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOverrideRestrictions()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OVERRIDE_RESTRICTIONS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOverrideRestrictions() <em>Override Restrictions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOverrideRestrictions()
	 * @generated
	 * @ordered
	 */
	protected boolean overrideRestrictions = OVERRIDE_RESTRICTIONS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getNominatedVessel() <em>Nominated Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominatedVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel nominatedVessel;

	/**
	 * The default value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOCKED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected boolean locked = LOCKED_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowNominationDate() <em>Window Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate WINDOW_NOMINATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWindowNominationDate() <em>Window Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate windowNominationDate = WINDOW_NOMINATION_DATE_EDEFAULT;

	/**
	 * This is true if the Window Nomination Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean windowNominationDateESet;

	/**
	 * The default value of the '{@link #isWindowNominationIsDone() <em>Window Nomination Is Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWindowNominationIsDone()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WINDOW_NOMINATION_IS_DONE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWindowNominationIsDone() <em>Window Nomination Is Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWindowNominationIsDone()
	 * @generated
	 * @ordered
	 */
	protected boolean windowNominationIsDone = WINDOW_NOMINATION_IS_DONE_EDEFAULT;

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
	 * This is true if the Window Nomination Counterparty attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean windowNominationCounterpartyESet;

	/**
	 * The default value of the '{@link #getWindowNominationComment() <em>Window Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationComment()
	 * @generated
	 * @ordered
	 */
	protected static final String WINDOW_NOMINATION_COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWindowNominationComment() <em>Window Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowNominationComment()
	 * @generated
	 * @ordered
	 */
	protected String windowNominationComment = WINDOW_NOMINATION_COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeNominationDate() <em>Volume Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate VOLUME_NOMINATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVolumeNominationDate() <em>Volume Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate volumeNominationDate = VOLUME_NOMINATION_DATE_EDEFAULT;

	/**
	 * This is true if the Volume Nomination Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean volumeNominationDateESet;

	/**
	 * The default value of the '{@link #isVolumeNominationDone() <em>Volume Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVolumeNominationDone()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VOLUME_NOMINATION_DONE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVolumeNominationDone() <em>Volume Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVolumeNominationDone()
	 * @generated
	 * @ordered
	 */
	protected boolean volumeNominationDone = VOLUME_NOMINATION_DONE_EDEFAULT;

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
	 * This is true if the Volume Nomination Counterparty attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean volumeNominationCounterpartyESet;

	/**
	 * The default value of the '{@link #getVolumeNominationComment() <em>Volume Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationComment()
	 * @generated
	 * @ordered
	 */
	protected static final String VOLUME_NOMINATION_COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVolumeNominationComment() <em>Volume Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeNominationComment()
	 * @generated
	 * @ordered
	 */
	protected String volumeNominationComment = VOLUME_NOMINATION_COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getVesselNominationDate() <em>Vessel Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate VESSEL_NOMINATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVesselNominationDate() <em>Vessel Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate vesselNominationDate = VESSEL_NOMINATION_DATE_EDEFAULT;

	/**
	 * This is true if the Vessel Nomination Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean vesselNominationDateESet;

	/**
	 * The default value of the '{@link #isVesselNominationDone() <em>Vessel Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselNominationDone()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VESSEL_NOMINATION_DONE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVesselNominationDone() <em>Vessel Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselNominationDone()
	 * @generated
	 * @ordered
	 */
	protected boolean vesselNominationDone = VESSEL_NOMINATION_DONE_EDEFAULT;

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
	 * This is true if the Vessel Nomination Counterparty attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean vesselNominationCounterpartyESet;

	/**
	 * The default value of the '{@link #getVesselNominationComment() <em>Vessel Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationComment()
	 * @generated
	 * @ordered
	 */
	protected static final String VESSEL_NOMINATION_COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVesselNominationComment() <em>Vessel Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselNominationComment()
	 * @generated
	 * @ordered
	 */
	protected String vesselNominationComment = VESSEL_NOMINATION_COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortNominationDate() <em>Port Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PORT_NOMINATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPortNominationDate() <em>Port Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate portNominationDate = PORT_NOMINATION_DATE_EDEFAULT;

	/**
	 * This is true if the Port Nomination Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean portNominationDateESet;

	/**
	 * The default value of the '{@link #isPortNominationDone() <em>Port Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortNominationDone()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PORT_NOMINATION_DONE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPortNominationDone() <em>Port Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortNominationDone()
	 * @generated
	 * @ordered
	 */
	protected boolean portNominationDone = PORT_NOMINATION_DONE_EDEFAULT;

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
	 * This is true if the Port Nomination Counterparty attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean portNominationCounterpartyESet;

	/**
	 * The default value of the '{@link #getPortNominationComment() <em>Port Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationComment()
	 * @generated
	 * @ordered
	 */
	protected static final String PORT_NOMINATION_COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPortNominationComment() <em>Port Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortNominationComment()
	 * @generated
	 * @ordered
	 */
	protected String portNominationComment = PORT_NOMINATION_COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortLoadNominationDate() <em>Port Load Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PORT_LOAD_NOMINATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPortLoadNominationDate() <em>Port Load Nomination Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate portLoadNominationDate = PORT_LOAD_NOMINATION_DATE_EDEFAULT;

	/**
	 * This is true if the Port Load Nomination Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean portLoadNominationDateESet;

	/**
	 * The default value of the '{@link #isPortLoadNominationDone() <em>Port Load Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortLoadNominationDone()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PORT_LOAD_NOMINATION_DONE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPortLoadNominationDone() <em>Port Load Nomination Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortLoadNominationDone()
	 * @generated
	 * @ordered
	 */
	protected boolean portLoadNominationDone = PORT_LOAD_NOMINATION_DONE_EDEFAULT;

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
	 * This is true if the Port Load Nomination Counterparty attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean portLoadNominationCounterpartyESet;

	/**
	 * The default value of the '{@link #getPortLoadNominationComment() <em>Port Load Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationComment()
	 * @generated
	 * @ordered
	 */
	protected static final String PORT_LOAD_NOMINATION_COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPortLoadNominationComment() <em>Port Load Nomination Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortLoadNominationComment()
	 * @generated
	 * @ordered
	 */
	protected String portLoadNominationComment = PORT_LOAD_NOMINATION_COMMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.SLOT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getWindowStart() {
		return windowStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowStart(LocalDate newWindowStart) {
		LocalDate oldWindowStart = windowStart;
		windowStart = newWindowStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_START, oldWindowStart, windowStart));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowStartTime() {
		return windowStartTime;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowStartTime(int newWindowStartTime) {
		int oldWindowStartTime = windowStartTime;
		windowStartTime = newWindowStartTime;
		boolean oldWindowStartTimeESet = windowStartTimeESet;
		windowStartTimeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_START_TIME, oldWindowStartTime, windowStartTime, !oldWindowStartTimeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWindowStartTime() {
		int oldWindowStartTime = windowStartTime;
		boolean oldWindowStartTimeESet = windowStartTimeESet;
		windowStartTime = WINDOW_START_TIME_EDEFAULT;
		windowStartTimeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__WINDOW_START_TIME, oldWindowStartTime, WINDOW_START_TIME_EDEFAULT, oldWindowStartTimeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWindowStartTime() {
		return windowStartTimeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowSize(int newWindowSize) {
		int oldWindowSize = windowSize;
		windowSize = newWindowSize;
		boolean oldWindowSizeESet = windowSizeESet;
		windowSizeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_SIZE, oldWindowSize, windowSize, !oldWindowSizeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWindowSize() {
		int oldWindowSize = windowSize;
		boolean oldWindowSizeESet = windowSizeESet;
		windowSize = WINDOW_SIZE_EDEFAULT;
		windowSizeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__WINDOW_SIZE, oldWindowSize, WINDOW_SIZE_EDEFAULT, oldWindowSizeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWindowSize() {
		return windowSizeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getWindowSizeUnits() {
		return windowSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowSizeUnits(TimePeriod newWindowSizeUnits) {
		TimePeriod oldWindowSizeUnits = windowSizeUnits;
		windowSizeUnits = newWindowSizeUnits == null ? WINDOW_SIZE_UNITS_EDEFAULT : newWindowSizeUnits;
		boolean oldWindowSizeUnitsESet = windowSizeUnitsESet;
		windowSizeUnitsESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_SIZE_UNITS, oldWindowSizeUnits, windowSizeUnits, !oldWindowSizeUnitsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWindowSizeUnits() {
		TimePeriod oldWindowSizeUnits = windowSizeUnits;
		boolean oldWindowSizeUnitsESet = windowSizeUnitsESet;
		windowSizeUnits = WINDOW_SIZE_UNITS_EDEFAULT;
		windowSizeUnitsESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__WINDOW_SIZE_UNITS, oldWindowSizeUnits, WINDOW_SIZE_UNITS_EDEFAULT, oldWindowSizeUnitsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWindowSizeUnits() {
		return windowSizeUnitsESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowFlex() {
		return windowFlex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowFlex(int newWindowFlex) {
		int oldWindowFlex = windowFlex;
		windowFlex = newWindowFlex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_FLEX, oldWindowFlex, windowFlex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getWindowFlexUnits() {
		return windowFlexUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowFlexUnits(TimePeriod newWindowFlexUnits) {
		TimePeriod oldWindowFlexUnits = windowFlexUnits;
		windowFlexUnits = newWindowFlexUnits == null ? WINDOW_FLEX_UNITS_EDEFAULT : newWindowFlexUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_FLEX_UNITS, oldWindowFlexUnits, windowFlexUnits));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (T)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public T basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContract(T newContract) {
		T oldContract = contract;
		contract = newContract;
		boolean oldContractESet = contractESet;
		contractESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__CONTRACT, oldContract, contract, !oldContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetContract() {
		T oldContract = contract;
		boolean oldContractESet = contractESet;
		contract = null;
		contractESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__CONTRACT, oldContract, null, oldContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetContract() {
		return contractESet;
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
		boolean oldCounterpartyESet = counterpartyESet;
		counterpartyESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__COUNTERPARTY, oldCounterparty, counterparty, !oldCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCounterparty() {
		String oldCounterparty = counterparty;
		boolean oldCounterpartyESet = counterpartyESet;
		counterparty = COUNTERPARTY_EDEFAULT;
		counterpartyESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__COUNTERPARTY, oldCounterparty, COUNTERPARTY_EDEFAULT, oldCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCounterparty() {
		return counterpartyESet;
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
		boolean oldCnESet = cnESet;
		cnESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__CN, oldCn, cn, !oldCnESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCn() {
		String oldCn = cn;
		boolean oldCnESet = cnESet;
		cn = CN_EDEFAULT;
		cnESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__CN, oldCn, CN_EDEFAULT, oldCnESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCn() {
		return cnESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDuration(int newDuration) {
		int oldDuration = duration;
		duration = newDuration;
		boolean oldDurationESet = durationESet;
		durationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__DURATION, oldDuration, duration, !oldDurationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetDuration() {
		int oldDuration = duration;
		boolean oldDurationESet = durationESet;
		duration = DURATION_EDEFAULT;
		durationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__DURATION, oldDuration, DURATION_EDEFAULT, oldDurationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetDuration() {
		return durationESet;
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
		boolean oldVolumeLimitsUnitESet = volumeLimitsUnitESet;
		volumeLimitsUnitESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VOLUME_LIMITS_UNIT, oldVolumeLimitsUnit, volumeLimitsUnit, !oldVolumeLimitsUnitESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVolumeLimitsUnit() {
		VolumeUnits oldVolumeLimitsUnit = volumeLimitsUnit;
		boolean oldVolumeLimitsUnitESet = volumeLimitsUnitESet;
		volumeLimitsUnit = VOLUME_LIMITS_UNIT_EDEFAULT;
		volumeLimitsUnitESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__VOLUME_LIMITS_UNIT, oldVolumeLimitsUnit, VOLUME_LIMITS_UNIT_EDEFAULT, oldVolumeLimitsUnitESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVolumeLimitsUnit() {
		return volumeLimitsUnitESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinQuantity() {
		return minQuantity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinQuantity(int newMinQuantity) {
		int oldMinQuantity = minQuantity;
		minQuantity = newMinQuantity;
		boolean oldMinQuantityESet = minQuantityESet;
		minQuantityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MIN_QUANTITY, oldMinQuantity, minQuantity, !oldMinQuantityESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinQuantity() {
		int oldMinQuantity = minQuantity;
		boolean oldMinQuantityESet = minQuantityESet;
		minQuantity = MIN_QUANTITY_EDEFAULT;
		minQuantityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__MIN_QUANTITY, oldMinQuantity, MIN_QUANTITY_EDEFAULT, oldMinQuantityESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinQuantity() {
		return minQuantityESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxQuantity() {
		return maxQuantity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxQuantity(int newMaxQuantity) {
		int oldMaxQuantity = maxQuantity;
		maxQuantity = newMaxQuantity;
		boolean oldMaxQuantityESet = maxQuantityESet;
		maxQuantityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MAX_QUANTITY, oldMaxQuantity, maxQuantity, !oldMaxQuantityESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxQuantity() {
		int oldMaxQuantity = maxQuantity;
		boolean oldMaxQuantityESet = maxQuantityESet;
		maxQuantity = MAX_QUANTITY_EDEFAULT;
		maxQuantityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__MAX_QUANTITY, oldMaxQuantity, MAX_QUANTITY_EDEFAULT, oldMaxQuantityESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxQuantity() {
		return maxQuantityESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__OPERATIONAL_TOLERANCE, oldOperationalTolerance, operationalTolerance, !oldOperationalToleranceESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__OPERATIONAL_TOLERANCE, oldOperationalTolerance, OPERATIONAL_TOLERANCE_EDEFAULT, oldOperationalToleranceESet));
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
		boolean oldFullCargoLotESet = fullCargoLotESet;
		fullCargoLotESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__FULL_CARGO_LOT, oldFullCargoLot, fullCargoLot, !oldFullCargoLotESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetFullCargoLot() {
		boolean oldFullCargoLot = fullCargoLot;
		boolean oldFullCargoLotESet = fullCargoLotESet;
		fullCargoLot = FULL_CARGO_LOT_EDEFAULT;
		fullCargoLotESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__FULL_CARGO_LOT, oldFullCargoLot, FULL_CARGO_LOT_EDEFAULT, oldFullCargoLotESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetFullCargoLot() {
		return fullCargoLotESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isOptional() {
		return optional;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOptional(boolean newOptional) {
		boolean oldOptional = optional;
		optional = newOptional;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__OPTIONAL, oldOptional, optional));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		boolean oldPriceExpressionESet = priceExpressionESet;
		priceExpressionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PRICE_EXPRESSION, oldPriceExpression, priceExpression, !oldPriceExpressionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPriceExpression() {
		String oldPriceExpression = priceExpression;
		boolean oldPriceExpressionESet = priceExpressionESet;
		priceExpression = PRICE_EXPRESSION_EDEFAULT;
		priceExpressionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__PRICE_EXPRESSION, oldPriceExpression, PRICE_EXPRESSION_EDEFAULT, oldPriceExpressionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPriceExpression() {
		return priceExpressionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Cargo getCargo() {
		if (cargo != null && cargo.eIsProxy()) {
			InternalEObject oldCargo = (InternalEObject)cargo;
			cargo = (Cargo)eResolveProxy(oldCargo);
			if (cargo != oldCargo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__CARGO, oldCargo, cargo));
			}
		}
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo basicGetCargo() {
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCargo(Cargo newCargo, NotificationChain msgs) {
		Cargo oldCargo = cargo;
		cargo = newCargo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__CARGO, oldCargo, newCargo);
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
	public void setCargo(Cargo newCargo) {
		if (newCargo != cargo) {
			NotificationChain msgs = null;
			if (cargo != null)
				msgs = ((InternalEObject)cargo).eInverseRemove(this, CargoPackage.CARGO__SLOTS, Cargo.class, msgs);
			if (newCargo != null)
				msgs = ((InternalEObject)newCargo).eInverseAdd(this, CargoPackage.CARGO__SLOTS, Cargo.class, msgs);
			msgs = basicSetCargo(newCargo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__CARGO, newCargo, newCargo));
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
		boolean oldPricingEventESet = pricingEventESet;
		pricingEventESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PRICING_EVENT, oldPricingEvent, pricingEvent, !oldPricingEventESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPricingEvent() {
		PricingEvent oldPricingEvent = pricingEvent;
		boolean oldPricingEventESet = pricingEventESet;
		pricingEvent = PRICING_EVENT_EDEFAULT;
		pricingEventESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__PRICING_EVENT, oldPricingEvent, PRICING_EVENT_EDEFAULT, oldPricingEventESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPricingEvent() {
		return pricingEventESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPricingDate() {
		return pricingDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingDate(LocalDate newPricingDate) {
		LocalDate oldPricingDate = pricingDate;
		pricingDate = newPricingDate;
		boolean oldPricingDateESet = pricingDateESet;
		pricingDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PRICING_DATE, oldPricingDate, pricingDate, !oldPricingDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPricingDate() {
		LocalDate oldPricingDate = pricingDate;
		boolean oldPricingDateESet = pricingDateESet;
		pricingDate = PRICING_DATE_EDEFAULT;
		pricingDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__PRICING_DATE, oldPricingDate, PRICING_DATE_EDEFAULT, oldPricingDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPricingDate() {
		return pricingDateESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__NOTES, oldNotes, notes));
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
		boolean oldDivertibleESet = divertibleESet;
		divertibleESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__DIVERTIBLE, oldDivertible, divertible, !oldDivertibleESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetDivertible() {
		boolean oldDivertible = divertible;
		boolean oldDivertibleESet = divertibleESet;
		divertible = DIVERTIBLE_EDEFAULT;
		divertibleESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__DIVERTIBLE, oldDivertible, DIVERTIBLE_EDEFAULT, oldDivertibleESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetDivertible() {
		return divertibleESet;
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
		boolean oldShippingDaysRestrictionESet = shippingDaysRestrictionESet;
		shippingDaysRestrictionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION, oldShippingDaysRestriction, shippingDaysRestriction, !oldShippingDaysRestrictionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetShippingDaysRestriction() {
		int oldShippingDaysRestriction = shippingDaysRestriction;
		boolean oldShippingDaysRestrictionESet = shippingDaysRestrictionESet;
		shippingDaysRestriction = SHIPPING_DAYS_RESTRICTION_EDEFAULT;
		shippingDaysRestrictionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION, oldShippingDaysRestriction, SHIPPING_DAYS_RESTRICTION_EDEFAULT, oldShippingDaysRestrictionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetShippingDaysRestriction() {
		return shippingDaysRestrictionESet;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__ENTITY, oldEntity, entity));
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
		boolean oldEntityESet = entityESet;
		entityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__ENTITY, oldEntity, entity, !oldEntityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetEntity() {
		BaseLegalEntity oldEntity = entity;
		boolean oldEntityESet = entityESet;
		entity = null;
		entityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__ENTITY, oldEntity, null, oldEntityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetEntity() {
		return entityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Contract> getRestrictedContracts() {
		if (restrictedContracts == null) {
			restrictedContracts = new EObjectResolvingEList<Contract>(Contract.class, this, CargoPackage.SLOT__RESTRICTED_CONTRACTS);
		}
		return restrictedContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Port> getRestrictedPorts() {
		if (restrictedPorts == null) {
			restrictedPorts = new EObjectResolvingEList<Port>(Port.class, this, CargoPackage.SLOT__RESTRICTED_PORTS);
		}
		return restrictedPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Slot<?>> getRestrictedSlots() {
		if (restrictedSlots == null) {
			restrictedSlots = new EObjectResolvingEList<Slot<?>>(Slot.class, this, CargoPackage.SLOT__RESTRICTED_SLOTS);
		}
		return restrictedSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRestrictedSlotsArePermissive() {
		return restrictedSlotsArePermissive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRestrictedSlotsArePermissive(boolean newRestrictedSlotsArePermissive) {
		boolean oldRestrictedSlotsArePermissive = restrictedSlotsArePermissive;
		restrictedSlotsArePermissive = newRestrictedSlotsArePermissive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE, oldRestrictedSlotsArePermissive, restrictedSlotsArePermissive));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE, oldRestrictedListsArePermissive, restrictedListsArePermissive));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getHedges() {
		return hedges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHedges(int newHedges) {
		int oldHedges = hedges;
		hedges = newHedges;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__HEDGES, oldHedges, hedges));
	}
/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMiscCosts() {
		return miscCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMiscCosts(int newMiscCosts) {
		int oldMiscCosts = miscCosts;
		miscCosts = newMiscCosts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MISC_COSTS, oldMiscCosts, miscCosts));
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
		boolean oldCancellationExpressionESet = cancellationExpressionESet;
		cancellationExpressionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__CANCELLATION_EXPRESSION, oldCancellationExpression, cancellationExpression, !oldCancellationExpressionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCancellationExpression() {
		String oldCancellationExpression = cancellationExpression;
		boolean oldCancellationExpressionESet = cancellationExpressionESet;
		cancellationExpression = CANCELLATION_EXPRESSION_EDEFAULT;
		cancellationExpressionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__CANCELLATION_EXPRESSION, oldCancellationExpression, CANCELLATION_EXPRESSION_EDEFAULT, oldCancellationExpressionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCancellationExpression() {
		return cancellationExpressionESet;
	}

								/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isOverrideRestrictions() {
		return overrideRestrictions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOverrideRestrictions(boolean newOverrideRestrictions) {
		boolean oldOverrideRestrictions = overrideRestrictions;
		overrideRestrictions = newOverrideRestrictions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__OVERRIDE_RESTRICTIONS, oldOverrideRestrictions, overrideRestrictions));
	}

								/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getNominatedVessel() {
		if (nominatedVessel != null && nominatedVessel.eIsProxy()) {
			InternalEObject oldNominatedVessel = (InternalEObject)nominatedVessel;
			nominatedVessel = (Vessel)eResolveProxy(oldNominatedVessel);
			if (nominatedVessel != oldNominatedVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__NOMINATED_VESSEL, oldNominatedVessel, nominatedVessel));
			}
		}
		return nominatedVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetNominatedVessel() {
		return nominatedVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNominatedVessel(Vessel newNominatedVessel) {
		Vessel oldNominatedVessel = nominatedVessel;
		nominatedVessel = newNominatedVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__NOMINATED_VESSEL, oldNominatedVessel, nominatedVessel));
	}

								/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLocked() {
		return locked;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLocked(boolean newLocked) {
		boolean oldLocked = locked;
		locked = newLocked;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__LOCKED, oldLocked, locked));
	}

								/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getWindowNominationDate() {
		return windowNominationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowNominationDate(LocalDate newWindowNominationDate) {
		LocalDate oldWindowNominationDate = windowNominationDate;
		windowNominationDate = newWindowNominationDate;
		boolean oldWindowNominationDateESet = windowNominationDateESet;
		windowNominationDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_NOMINATION_DATE, oldWindowNominationDate, windowNominationDate, !oldWindowNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWindowNominationDate() {
		LocalDate oldWindowNominationDate = windowNominationDate;
		boolean oldWindowNominationDateESet = windowNominationDateESet;
		windowNominationDate = WINDOW_NOMINATION_DATE_EDEFAULT;
		windowNominationDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__WINDOW_NOMINATION_DATE, oldWindowNominationDate, WINDOW_NOMINATION_DATE_EDEFAULT, oldWindowNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWindowNominationDate() {
		return windowNominationDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isWindowNominationIsDone() {
		return windowNominationIsDone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowNominationIsDone(boolean newWindowNominationIsDone) {
		boolean oldWindowNominationIsDone = windowNominationIsDone;
		windowNominationIsDone = newWindowNominationIsDone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_NOMINATION_IS_DONE, oldWindowNominationIsDone, windowNominationIsDone));
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
		boolean oldWindowNominationCounterpartyESet = windowNominationCounterpartyESet;
		windowNominationCounterpartyESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_NOMINATION_COUNTERPARTY, oldWindowNominationCounterparty, windowNominationCounterparty, !oldWindowNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWindowNominationCounterparty() {
		boolean oldWindowNominationCounterparty = windowNominationCounterparty;
		boolean oldWindowNominationCounterpartyESet = windowNominationCounterpartyESet;
		windowNominationCounterparty = WINDOW_NOMINATION_COUNTERPARTY_EDEFAULT;
		windowNominationCounterpartyESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__WINDOW_NOMINATION_COUNTERPARTY, oldWindowNominationCounterparty, WINDOW_NOMINATION_COUNTERPARTY_EDEFAULT, oldWindowNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWindowNominationCounterparty() {
		return windowNominationCounterpartyESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getWindowNominationComment() {
		return windowNominationComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowNominationComment(String newWindowNominationComment) {
		String oldWindowNominationComment = windowNominationComment;
		windowNominationComment = newWindowNominationComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_NOMINATION_COMMENT, oldWindowNominationComment, windowNominationComment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getVolumeNominationDate() {
		return volumeNominationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeNominationDate(LocalDate newVolumeNominationDate) {
		LocalDate oldVolumeNominationDate = volumeNominationDate;
		volumeNominationDate = newVolumeNominationDate;
		boolean oldVolumeNominationDateESet = volumeNominationDateESet;
		volumeNominationDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VOLUME_NOMINATION_DATE, oldVolumeNominationDate, volumeNominationDate, !oldVolumeNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVolumeNominationDate() {
		LocalDate oldVolumeNominationDate = volumeNominationDate;
		boolean oldVolumeNominationDateESet = volumeNominationDateESet;
		volumeNominationDate = VOLUME_NOMINATION_DATE_EDEFAULT;
		volumeNominationDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__VOLUME_NOMINATION_DATE, oldVolumeNominationDate, VOLUME_NOMINATION_DATE_EDEFAULT, oldVolumeNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVolumeNominationDate() {
		return volumeNominationDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isVolumeNominationDone() {
		return volumeNominationDone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeNominationDone(boolean newVolumeNominationDone) {
		boolean oldVolumeNominationDone = volumeNominationDone;
		volumeNominationDone = newVolumeNominationDone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VOLUME_NOMINATION_DONE, oldVolumeNominationDone, volumeNominationDone));
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
		boolean oldVolumeNominationCounterpartyESet = volumeNominationCounterpartyESet;
		volumeNominationCounterpartyESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VOLUME_NOMINATION_COUNTERPARTY, oldVolumeNominationCounterparty, volumeNominationCounterparty, !oldVolumeNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVolumeNominationCounterparty() {
		boolean oldVolumeNominationCounterparty = volumeNominationCounterparty;
		boolean oldVolumeNominationCounterpartyESet = volumeNominationCounterpartyESet;
		volumeNominationCounterparty = VOLUME_NOMINATION_COUNTERPARTY_EDEFAULT;
		volumeNominationCounterpartyESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__VOLUME_NOMINATION_COUNTERPARTY, oldVolumeNominationCounterparty, VOLUME_NOMINATION_COUNTERPARTY_EDEFAULT, oldVolumeNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVolumeNominationCounterparty() {
		return volumeNominationCounterpartyESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVolumeNominationComment() {
		return volumeNominationComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeNominationComment(String newVolumeNominationComment) {
		String oldVolumeNominationComment = volumeNominationComment;
		volumeNominationComment = newVolumeNominationComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VOLUME_NOMINATION_COMMENT, oldVolumeNominationComment, volumeNominationComment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getVesselNominationDate() {
		return vesselNominationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselNominationDate(LocalDate newVesselNominationDate) {
		LocalDate oldVesselNominationDate = vesselNominationDate;
		vesselNominationDate = newVesselNominationDate;
		boolean oldVesselNominationDateESet = vesselNominationDateESet;
		vesselNominationDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VESSEL_NOMINATION_DATE, oldVesselNominationDate, vesselNominationDate, !oldVesselNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVesselNominationDate() {
		LocalDate oldVesselNominationDate = vesselNominationDate;
		boolean oldVesselNominationDateESet = vesselNominationDateESet;
		vesselNominationDate = VESSEL_NOMINATION_DATE_EDEFAULT;
		vesselNominationDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__VESSEL_NOMINATION_DATE, oldVesselNominationDate, VESSEL_NOMINATION_DATE_EDEFAULT, oldVesselNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVesselNominationDate() {
		return vesselNominationDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isVesselNominationDone() {
		return vesselNominationDone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselNominationDone(boolean newVesselNominationDone) {
		boolean oldVesselNominationDone = vesselNominationDone;
		vesselNominationDone = newVesselNominationDone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VESSEL_NOMINATION_DONE, oldVesselNominationDone, vesselNominationDone));
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
		boolean oldVesselNominationCounterpartyESet = vesselNominationCounterpartyESet;
		vesselNominationCounterpartyESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VESSEL_NOMINATION_COUNTERPARTY, oldVesselNominationCounterparty, vesselNominationCounterparty, !oldVesselNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVesselNominationCounterparty() {
		boolean oldVesselNominationCounterparty = vesselNominationCounterparty;
		boolean oldVesselNominationCounterpartyESet = vesselNominationCounterpartyESet;
		vesselNominationCounterparty = VESSEL_NOMINATION_COUNTERPARTY_EDEFAULT;
		vesselNominationCounterpartyESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__VESSEL_NOMINATION_COUNTERPARTY, oldVesselNominationCounterparty, VESSEL_NOMINATION_COUNTERPARTY_EDEFAULT, oldVesselNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVesselNominationCounterparty() {
		return vesselNominationCounterpartyESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVesselNominationComment() {
		return vesselNominationComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselNominationComment(String newVesselNominationComment) {
		String oldVesselNominationComment = vesselNominationComment;
		vesselNominationComment = newVesselNominationComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__VESSEL_NOMINATION_COMMENT, oldVesselNominationComment, vesselNominationComment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPortNominationDate() {
		return portNominationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortNominationDate(LocalDate newPortNominationDate) {
		LocalDate oldPortNominationDate = portNominationDate;
		portNominationDate = newPortNominationDate;
		boolean oldPortNominationDateESet = portNominationDateESet;
		portNominationDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_NOMINATION_DATE, oldPortNominationDate, portNominationDate, !oldPortNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPortNominationDate() {
		LocalDate oldPortNominationDate = portNominationDate;
		boolean oldPortNominationDateESet = portNominationDateESet;
		portNominationDate = PORT_NOMINATION_DATE_EDEFAULT;
		portNominationDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__PORT_NOMINATION_DATE, oldPortNominationDate, PORT_NOMINATION_DATE_EDEFAULT, oldPortNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPortNominationDate() {
		return portNominationDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPortNominationDone() {
		return portNominationDone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortNominationDone(boolean newPortNominationDone) {
		boolean oldPortNominationDone = portNominationDone;
		portNominationDone = newPortNominationDone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_NOMINATION_DONE, oldPortNominationDone, portNominationDone));
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
		boolean oldPortNominationCounterpartyESet = portNominationCounterpartyESet;
		portNominationCounterpartyESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_NOMINATION_COUNTERPARTY, oldPortNominationCounterparty, portNominationCounterparty, !oldPortNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPortNominationCounterparty() {
		boolean oldPortNominationCounterparty = portNominationCounterparty;
		boolean oldPortNominationCounterpartyESet = portNominationCounterpartyESet;
		portNominationCounterparty = PORT_NOMINATION_COUNTERPARTY_EDEFAULT;
		portNominationCounterpartyESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__PORT_NOMINATION_COUNTERPARTY, oldPortNominationCounterparty, PORT_NOMINATION_COUNTERPARTY_EDEFAULT, oldPortNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPortNominationCounterparty() {
		return portNominationCounterpartyESet;
	}

								/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPortNominationComment() {
		return portNominationComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortNominationComment(String newPortNominationComment) {
		String oldPortNominationComment = portNominationComment;
		portNominationComment = newPortNominationComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_NOMINATION_COMMENT, oldPortNominationComment, portNominationComment));
	}

	

								/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPortLoadNominationDate() {
		return portLoadNominationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortLoadNominationDate(LocalDate newPortLoadNominationDate) {
		LocalDate oldPortLoadNominationDate = portLoadNominationDate;
		portLoadNominationDate = newPortLoadNominationDate;
		boolean oldPortLoadNominationDateESet = portLoadNominationDateESet;
		portLoadNominationDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_LOAD_NOMINATION_DATE, oldPortLoadNominationDate, portLoadNominationDate, !oldPortLoadNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPortLoadNominationDate() {
		LocalDate oldPortLoadNominationDate = portLoadNominationDate;
		boolean oldPortLoadNominationDateESet = portLoadNominationDateESet;
		portLoadNominationDate = PORT_LOAD_NOMINATION_DATE_EDEFAULT;
		portLoadNominationDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__PORT_LOAD_NOMINATION_DATE, oldPortLoadNominationDate, PORT_LOAD_NOMINATION_DATE_EDEFAULT, oldPortLoadNominationDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPortLoadNominationDate() {
		return portLoadNominationDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPortLoadNominationDone() {
		return portLoadNominationDone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortLoadNominationDone(boolean newPortLoadNominationDone) {
		boolean oldPortLoadNominationDone = portLoadNominationDone;
		portLoadNominationDone = newPortLoadNominationDone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_LOAD_NOMINATION_DONE, oldPortLoadNominationDone, portLoadNominationDone));
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
		boolean oldPortLoadNominationCounterpartyESet = portLoadNominationCounterpartyESet;
		portLoadNominationCounterpartyESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY, oldPortLoadNominationCounterparty, portLoadNominationCounterparty, !oldPortLoadNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPortLoadNominationCounterparty() {
		boolean oldPortLoadNominationCounterparty = portLoadNominationCounterparty;
		boolean oldPortLoadNominationCounterpartyESet = portLoadNominationCounterpartyESet;
		portLoadNominationCounterparty = PORT_LOAD_NOMINATION_COUNTERPARTY_EDEFAULT;
		portLoadNominationCounterpartyESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY, oldPortLoadNominationCounterparty, PORT_LOAD_NOMINATION_COUNTERPARTY_EDEFAULT, oldPortLoadNominationCounterpartyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPortLoadNominationCounterparty() {
		return portLoadNominationCounterpartyESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPortLoadNominationComment() {
		return portLoadNominationComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortLoadNominationComment(String newPortLoadNominationComment) {
		String oldPortLoadNominationComment = portLoadNominationComment;
		portLoadNominationComment = newPortLoadNominationComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT_LOAD_NOMINATION_COMMENT, oldPortLoadNominationComment, portLoadNominationComment));
	}

								/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AVesselSet<Vessel>> getAllowedVessels() {
		if (allowedVessels == null) {
			allowedVessels = new EObjectResolvingEList<AVesselSet<Vessel>>(AVesselSet.class, this, CargoPackage.SLOT__ALLOWED_VESSELS);
		}
		return allowedVessels;
	}

	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public int getSlotOrDelegateDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.SLOT__DURATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public int getSlotOrDelegateMinQuantity() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.SLOT__MIN_QUANTITY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public int getSlotOrDelegateMaxQuantity() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.SLOT__MAX_QUANTITY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getSlotOrDelegateOperationalTolerance() {
		return (Double) eGetWithDefault(CargoPackage.Literals.SLOT__OPERATIONAL_TOLERANCE);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public VolumeUnits getSlotOrDelegateVolumeLimitsUnit() {
		return (VolumeUnits) eGetWithDefault(CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getWindowEndWithSlotOrPortTime() {
		final ZonedDateTime startTime = getWindowStartWithSlotOrPortTime();
		if (startTime == null) {
			return null;
		}
		
		return startTime.plusHours(getWindowSizeInHours());
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getWindowEndWithSlotOrPortTimeWithFlex() {
		ZonedDateTime endTime = getWindowEndWithSlotOrPortTime();
		if (endTime == null) {
			return null;
		}
		final int slotFlex = getWindowFlex();
		if (slotFlex > 0) {
			TimePeriod p  = getWindowFlexUnits();
			switch (p) {
			case DAYS:
				endTime = endTime.plusDays(slotFlex).minusHours(1);
				break;
			case HOURS:
				endTime = endTime.plusHours(slotFlex) ;
				break;
			case MONTHS:
				endTime  = endTime.plusMonths(slotFlex).minusHours(1);
				break;
			default:
				break;
			}
		}
		return endTime;
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ZonedDateTime getWindowStartWithSlotOrPortTime() {
		final LocalDate wStart = getWindowStart();
		if (wStart == null) {
			return null;
		}
		ZonedDateTime dateTime = wStart.atStartOfDay(ZoneId.of(getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart())));
		final int startTime = (Integer) eGetWithDefault(CargoPackage.eINSTANCE.getSlot_WindowStartTime());
		dateTime = dateTime.withHour(startTime);
		return dateTime;
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ZonedDateTime getWindowStartWithSlotOrPortTimeWithFlex() {
		ZonedDateTime startTime = getWindowStartWithSlotOrPortTime();
		if (startTime == null) {
			return null;
		}
		final int slotFlex = getWindowFlex();
		if (slotFlex < 0) {
			TimePeriod p  = getWindowFlexUnits();
			switch (p) {
			case DAYS:
				startTime = startTime.minusDays(slotFlex).plusHours(1);
				break;
			case HOURS:
				startTime = startTime.minusHours(slotFlex) ;
				break;
			case MONTHS:
				startTime  = startTime.minusMonths(slotFlex).plusHours(1);
				break;
			default:
				break;
			}
		}
		return startTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getSlotOrDelegateWindowSize() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TimePeriod getSlotOrDelegateWindowSizeUnits() {
		return (TimePeriod) eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getWindowSizeInHours() {
		ZonedDateTime start = getWindowStartWithSlotOrPortTime();
		ZonedDateTime end = start;
		TimePeriod p  = getSlotOrDelegateWindowSizeUnits();
		int windowSize = getSlotOrDelegateWindowSize();
		if (windowSize == 0) {
			return 0;
		}
		
		switch (p) {
		case DAYS:
			end = end.plusDays(windowSize).minusHours(1);
			break;
		case HOURS:
			end = end.plusHours(windowSize) ;
			break;
		case MONTHS:
			end = end.plusMonths(windowSize).minusHours(1);
			break;
		default:
			break;
		}
		
		return Hours.between(start, end);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BaseLegalEntity getSlotOrDelegateEntity() {
		return (BaseLegalEntity) eGetWithDefault(CargoPackage.Literals.SLOT__ENTITY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public EList<Contract> getSlotOrDelegateRestrictedContracts() {
		return (EList<Contract>) eGetWithDefault(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public EList<Port> getSlotOrDelegateRestrictedPorts() {
		return (EList<Port>) eGetWithDefault(CargoPackage.Literals.SLOT__RESTRICTED_PORTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getSlotOrDelegateRestrictedListsArePermissive() {
		return (Boolean) eGetWithDefault(CargoPackage.Literals.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getSlotOrDelegateCancellationExpression() {
		return (String) eGetWithDefault(CargoPackage.Literals.SLOT__CANCELLATION_EXPRESSION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public PricingEvent getSlotOrDelegatePricingEvent() {
		return (PricingEvent) eGetWithDefault(CargoPackage.Literals.SLOT__PRICING_EVENT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getPricingDateAsDateTime() {
		if (isSetPricingDate()) {
			final LocalDate ld = getPricingDate();
			return ld.atStartOfDay(ZoneId.of(getTimeZone(CargoPackage.Literals.SLOT__PRICING_DATE)));
		}
		throw new IllegalStateException("No pricing date");
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Nullable
	public SlotContractParams getSlotContractParams() {
		return SlotContractParamsHelper.findSlotContractParams(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getSlotOrDelegateCounterparty() {
		return (String) eGetWithDefault(CargoPackage.Literals.SLOT__COUNTERPARTY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getSlotOrDelegateCN() {
		return (String) eGetWithDefault(CargoPackage.Literals.SLOT__CN);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public LocalDate getSlotOrDelegateWindowNominationDate() {
		return (LocalDate) eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getSlotOrDelegateDivertible() {
		return (Boolean) eGetWithDefault(CargoPackage.Literals.SLOT__DIVERTIBLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getSlotOrDelegateShippingDaysRestriction() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public LocalDate getSlotOrDelegatePortLoadNominationDate() {
		return (LocalDate) eGetWithDefault(CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_DATE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public LocalDate getSlotOrDelegateVesselNominationDate() {
		return (LocalDate) eGetWithDefault(CargoPackage.Literals.SLOT__VESSEL_NOMINATION_DATE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public LocalDate getSlotOrDelegateVolumeNominationDate() {
		return (LocalDate) eGetWithDefault(CargoPackage.Literals.SLOT__VOLUME_NOMINATION_DATE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public LocalDate getSlotOrDelegatePortNominationDate() {
		return (LocalDate) eGetWithDefault(CargoPackage.Literals.SLOT__PORT_NOMINATION_DATE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getSlotOrDelegateWindowNominationCounterparty() {
		return (boolean) eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COUNTERPARTY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getSlotOrDelegateVesselNominationCounterparty() {
		return (boolean) eGetWithDefault(CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COUNTERPARTY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getSlotOrDelegateVolumeNominationCounterparty() {
		return (boolean) eGetWithDefault(CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COUNTERPARTY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getSlotOrDelegatePortNominationCounterparty() {
		return (boolean) eGetWithDefault(CargoPackage.Literals.SLOT__PORT_NOMINATION_COUNTERPARTY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getSlotOrDelegatePortLoadNominationCounterparty() {
		return (boolean) eGetWithDefault(CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean getSlotOrDelegateFullCargoLot() {
		return (boolean) eGetWithDefault(CargoPackage.Literals.SLOT__FULL_CARGO_LOT);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getTimeZone(EAttribute attribute) {
		final Port p = getPort();
		if (p == null) {
			return "UTC";
		}
		Location l = p.getLocation();
		if (l == null || l.getTimeZone() == null || l.getTimeZone().isEmpty()) {
			return "UTC";
		}
		return l.getTimeZone();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.SLOT__CARGO:
				if (cargo != null)
					msgs = ((InternalEObject)cargo).eInverseRemove(this, CargoPackage.CARGO__SLOTS, Cargo.class, msgs);
				return basicSetCargo((Cargo)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.SLOT__CARGO:
				return basicSetCargo(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.SLOT__NAME:
				return getName();
			case CargoPackage.SLOT__CONTRACT:
				if (resolve) return getContract();
				return basicGetContract();
			case CargoPackage.SLOT__COUNTERPARTY:
				return getCounterparty();
			case CargoPackage.SLOT__CN:
				return getCn();
			case CargoPackage.SLOT__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case CargoPackage.SLOT__WINDOW_START:
				return getWindowStart();
			case CargoPackage.SLOT__WINDOW_START_TIME:
				return getWindowStartTime();
			case CargoPackage.SLOT__WINDOW_SIZE:
				return getWindowSize();
			case CargoPackage.SLOT__WINDOW_SIZE_UNITS:
				return getWindowSizeUnits();
			case CargoPackage.SLOT__WINDOW_FLEX:
				return getWindowFlex();
			case CargoPackage.SLOT__WINDOW_FLEX_UNITS:
				return getWindowFlexUnits();
			case CargoPackage.SLOT__DURATION:
				return getDuration();
			case CargoPackage.SLOT__VOLUME_LIMITS_UNIT:
				return getVolumeLimitsUnit();
			case CargoPackage.SLOT__MIN_QUANTITY:
				return getMinQuantity();
			case CargoPackage.SLOT__MAX_QUANTITY:
				return getMaxQuantity();
			case CargoPackage.SLOT__OPERATIONAL_TOLERANCE:
				return getOperationalTolerance();
			case CargoPackage.SLOT__FULL_CARGO_LOT:
				return isFullCargoLot();
			case CargoPackage.SLOT__OPTIONAL:
				return isOptional();
			case CargoPackage.SLOT__PRICE_EXPRESSION:
				return getPriceExpression();
			case CargoPackage.SLOT__CARGO:
				if (resolve) return getCargo();
				return basicGetCargo();
			case CargoPackage.SLOT__PRICING_EVENT:
				return getPricingEvent();
			case CargoPackage.SLOT__PRICING_DATE:
				return getPricingDate();
			case CargoPackage.SLOT__NOTES:
				return getNotes();
			case CargoPackage.SLOT__DIVERTIBLE:
				return isDivertible();
			case CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION:
				return getShippingDaysRestriction();
			case CargoPackage.SLOT__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CargoPackage.SLOT__RESTRICTED_CONTRACTS:
				return getRestrictedContracts();
			case CargoPackage.SLOT__RESTRICTED_PORTS:
				return getRestrictedPorts();
			case CargoPackage.SLOT__RESTRICTED_SLOTS:
				return getRestrictedSlots();
			case CargoPackage.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE:
				return isRestrictedSlotsArePermissive();
			case CargoPackage.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				return isRestrictedListsArePermissive();
			case CargoPackage.SLOT__HEDGES:
				return getHedges();
			case CargoPackage.SLOT__MISC_COSTS:
				return getMiscCosts();
			case CargoPackage.SLOT__ALLOWED_VESSELS:
				return getAllowedVessels();
			case CargoPackage.SLOT__CANCELLATION_EXPRESSION:
				return getCancellationExpression();
			case CargoPackage.SLOT__OVERRIDE_RESTRICTIONS:
				return isOverrideRestrictions();
			case CargoPackage.SLOT__NOMINATED_VESSEL:
				if (resolve) return getNominatedVessel();
				return basicGetNominatedVessel();
			case CargoPackage.SLOT__LOCKED:
				return isLocked();
			case CargoPackage.SLOT__WINDOW_NOMINATION_DATE:
				return getWindowNominationDate();
			case CargoPackage.SLOT__WINDOW_NOMINATION_IS_DONE:
				return isWindowNominationIsDone();
			case CargoPackage.SLOT__WINDOW_NOMINATION_COUNTERPARTY:
				return isWindowNominationCounterparty();
			case CargoPackage.SLOT__WINDOW_NOMINATION_COMMENT:
				return getWindowNominationComment();
			case CargoPackage.SLOT__VOLUME_NOMINATION_DATE:
				return getVolumeNominationDate();
			case CargoPackage.SLOT__VOLUME_NOMINATION_DONE:
				return isVolumeNominationDone();
			case CargoPackage.SLOT__VOLUME_NOMINATION_COUNTERPARTY:
				return isVolumeNominationCounterparty();
			case CargoPackage.SLOT__VOLUME_NOMINATION_COMMENT:
				return getVolumeNominationComment();
			case CargoPackage.SLOT__VESSEL_NOMINATION_DATE:
				return getVesselNominationDate();
			case CargoPackage.SLOT__VESSEL_NOMINATION_DONE:
				return isVesselNominationDone();
			case CargoPackage.SLOT__VESSEL_NOMINATION_COUNTERPARTY:
				return isVesselNominationCounterparty();
			case CargoPackage.SLOT__VESSEL_NOMINATION_COMMENT:
				return getVesselNominationComment();
			case CargoPackage.SLOT__PORT_NOMINATION_DATE:
				return getPortNominationDate();
			case CargoPackage.SLOT__PORT_NOMINATION_DONE:
				return isPortNominationDone();
			case CargoPackage.SLOT__PORT_NOMINATION_COUNTERPARTY:
				return isPortNominationCounterparty();
			case CargoPackage.SLOT__PORT_NOMINATION_COMMENT:
				return getPortNominationComment();
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DATE:
				return getPortLoadNominationDate();
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DONE:
				return isPortLoadNominationDone();
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				return isPortLoadNominationCounterparty();
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COMMENT:
				return getPortLoadNominationComment();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CargoPackage.SLOT__NAME:
				setName((String)newValue);
				return;
			case CargoPackage.SLOT__CONTRACT:
				setContract((T)newValue);
				return;
			case CargoPackage.SLOT__COUNTERPARTY:
				setCounterparty((String)newValue);
				return;
			case CargoPackage.SLOT__CN:
				setCn((String)newValue);
				return;
			case CargoPackage.SLOT__PORT:
				setPort((Port)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_START:
				setWindowStart((LocalDate)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_START_TIME:
				setWindowStartTime((Integer)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_SIZE:
				setWindowSize((Integer)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_SIZE_UNITS:
				setWindowSizeUnits((TimePeriod)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_FLEX:
				setWindowFlex((Integer)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_FLEX_UNITS:
				setWindowFlexUnits((TimePeriod)newValue);
				return;
			case CargoPackage.SLOT__DURATION:
				setDuration((Integer)newValue);
				return;
			case CargoPackage.SLOT__VOLUME_LIMITS_UNIT:
				setVolumeLimitsUnit((VolumeUnits)newValue);
				return;
			case CargoPackage.SLOT__MIN_QUANTITY:
				setMinQuantity((Integer)newValue);
				return;
			case CargoPackage.SLOT__MAX_QUANTITY:
				setMaxQuantity((Integer)newValue);
				return;
			case CargoPackage.SLOT__OPERATIONAL_TOLERANCE:
				setOperationalTolerance((Double)newValue);
				return;
			case CargoPackage.SLOT__FULL_CARGO_LOT:
				setFullCargoLot((Boolean)newValue);
				return;
			case CargoPackage.SLOT__OPTIONAL:
				setOptional((Boolean)newValue);
				return;
			case CargoPackage.SLOT__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
				return;
			case CargoPackage.SLOT__CARGO:
				setCargo((Cargo)newValue);
				return;
			case CargoPackage.SLOT__PRICING_EVENT:
				setPricingEvent((PricingEvent)newValue);
				return;
			case CargoPackage.SLOT__PRICING_DATE:
				setPricingDate((LocalDate)newValue);
				return;
			case CargoPackage.SLOT__NOTES:
				setNotes((String)newValue);
				return;
			case CargoPackage.SLOT__DIVERTIBLE:
				setDivertible((Boolean)newValue);
				return;
			case CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION:
				setShippingDaysRestriction((Integer)newValue);
				return;
			case CargoPackage.SLOT__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case CargoPackage.SLOT__RESTRICTED_CONTRACTS:
				getRestrictedContracts().clear();
				getRestrictedContracts().addAll((Collection<? extends Contract>)newValue);
				return;
			case CargoPackage.SLOT__RESTRICTED_PORTS:
				getRestrictedPorts().clear();
				getRestrictedPorts().addAll((Collection<? extends Port>)newValue);
				return;
			case CargoPackage.SLOT__RESTRICTED_SLOTS:
				getRestrictedSlots().clear();
				getRestrictedSlots().addAll((Collection<? extends Slot<?>>)newValue);
				return;
			case CargoPackage.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE:
				setRestrictedSlotsArePermissive((Boolean)newValue);
				return;
			case CargoPackage.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				setRestrictedListsArePermissive((Boolean)newValue);
				return;
			case CargoPackage.SLOT__HEDGES:
				setHedges((Integer)newValue);
				return;
			case CargoPackage.SLOT__MISC_COSTS:
				setMiscCosts((Integer)newValue);
				return;
			case CargoPackage.SLOT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends AVesselSet<Vessel>>)newValue);
				return;
			case CargoPackage.SLOT__CANCELLATION_EXPRESSION:
				setCancellationExpression((String)newValue);
				return;
			case CargoPackage.SLOT__OVERRIDE_RESTRICTIONS:
				setOverrideRestrictions((Boolean)newValue);
				return;
			case CargoPackage.SLOT__NOMINATED_VESSEL:
				setNominatedVessel((Vessel)newValue);
				return;
			case CargoPackage.SLOT__LOCKED:
				setLocked((Boolean)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_DATE:
				setWindowNominationDate((LocalDate)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_IS_DONE:
				setWindowNominationIsDone((Boolean)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_COUNTERPARTY:
				setWindowNominationCounterparty((Boolean)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_COMMENT:
				setWindowNominationComment((String)newValue);
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_DATE:
				setVolumeNominationDate((LocalDate)newValue);
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_DONE:
				setVolumeNominationDone((Boolean)newValue);
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_COUNTERPARTY:
				setVolumeNominationCounterparty((Boolean)newValue);
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_COMMENT:
				setVolumeNominationComment((String)newValue);
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_DATE:
				setVesselNominationDate((LocalDate)newValue);
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_DONE:
				setVesselNominationDone((Boolean)newValue);
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_COUNTERPARTY:
				setVesselNominationCounterparty((Boolean)newValue);
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_COMMENT:
				setVesselNominationComment((String)newValue);
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_DATE:
				setPortNominationDate((LocalDate)newValue);
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_DONE:
				setPortNominationDone((Boolean)newValue);
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_COUNTERPARTY:
				setPortNominationCounterparty((Boolean)newValue);
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_COMMENT:
				setPortNominationComment((String)newValue);
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DATE:
				setPortLoadNominationDate((LocalDate)newValue);
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DONE:
				setPortLoadNominationDone((Boolean)newValue);
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				setPortLoadNominationCounterparty((Boolean)newValue);
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COMMENT:
				setPortLoadNominationComment((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CargoPackage.SLOT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CargoPackage.SLOT__CONTRACT:
				unsetContract();
				return;
			case CargoPackage.SLOT__COUNTERPARTY:
				unsetCounterparty();
				return;
			case CargoPackage.SLOT__CN:
				unsetCn();
				return;
			case CargoPackage.SLOT__PORT:
				setPort((Port)null);
				return;
			case CargoPackage.SLOT__WINDOW_START:
				setWindowStart(WINDOW_START_EDEFAULT);
				return;
			case CargoPackage.SLOT__WINDOW_START_TIME:
				unsetWindowStartTime();
				return;
			case CargoPackage.SLOT__WINDOW_SIZE:
				unsetWindowSize();
				return;
			case CargoPackage.SLOT__WINDOW_SIZE_UNITS:
				unsetWindowSizeUnits();
				return;
			case CargoPackage.SLOT__WINDOW_FLEX:
				setWindowFlex(WINDOW_FLEX_EDEFAULT);
				return;
			case CargoPackage.SLOT__WINDOW_FLEX_UNITS:
				setWindowFlexUnits(WINDOW_FLEX_UNITS_EDEFAULT);
				return;
			case CargoPackage.SLOT__DURATION:
				unsetDuration();
				return;
			case CargoPackage.SLOT__VOLUME_LIMITS_UNIT:
				unsetVolumeLimitsUnit();
				return;
			case CargoPackage.SLOT__MIN_QUANTITY:
				unsetMinQuantity();
				return;
			case CargoPackage.SLOT__MAX_QUANTITY:
				unsetMaxQuantity();
				return;
			case CargoPackage.SLOT__OPERATIONAL_TOLERANCE:
				unsetOperationalTolerance();
				return;
			case CargoPackage.SLOT__FULL_CARGO_LOT:
				unsetFullCargoLot();
				return;
			case CargoPackage.SLOT__OPTIONAL:
				setOptional(OPTIONAL_EDEFAULT);
				return;
			case CargoPackage.SLOT__PRICE_EXPRESSION:
				unsetPriceExpression();
				return;
			case CargoPackage.SLOT__CARGO:
				setCargo((Cargo)null);
				return;
			case CargoPackage.SLOT__PRICING_EVENT:
				unsetPricingEvent();
				return;
			case CargoPackage.SLOT__PRICING_DATE:
				unsetPricingDate();
				return;
			case CargoPackage.SLOT__NOTES:
				setNotes(NOTES_EDEFAULT);
				return;
			case CargoPackage.SLOT__DIVERTIBLE:
				unsetDivertible();
				return;
			case CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION:
				unsetShippingDaysRestriction();
				return;
			case CargoPackage.SLOT__ENTITY:
				unsetEntity();
				return;
			case CargoPackage.SLOT__RESTRICTED_CONTRACTS:
				getRestrictedContracts().clear();
				return;
			case CargoPackage.SLOT__RESTRICTED_PORTS:
				getRestrictedPorts().clear();
				return;
			case CargoPackage.SLOT__RESTRICTED_SLOTS:
				getRestrictedSlots().clear();
				return;
			case CargoPackage.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE:
				setRestrictedSlotsArePermissive(RESTRICTED_SLOTS_ARE_PERMISSIVE_EDEFAULT);
				return;
			case CargoPackage.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				setRestrictedListsArePermissive(RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT);
				return;
			case CargoPackage.SLOT__HEDGES:
				setHedges(HEDGES_EDEFAULT);
				return;
			case CargoPackage.SLOT__MISC_COSTS:
				setMiscCosts(MISC_COSTS_EDEFAULT);
				return;
			case CargoPackage.SLOT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				return;
			case CargoPackage.SLOT__CANCELLATION_EXPRESSION:
				unsetCancellationExpression();
				return;
			case CargoPackage.SLOT__OVERRIDE_RESTRICTIONS:
				setOverrideRestrictions(OVERRIDE_RESTRICTIONS_EDEFAULT);
				return;
			case CargoPackage.SLOT__NOMINATED_VESSEL:
				setNominatedVessel((Vessel)null);
				return;
			case CargoPackage.SLOT__LOCKED:
				setLocked(LOCKED_EDEFAULT);
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_DATE:
				unsetWindowNominationDate();
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_IS_DONE:
				setWindowNominationIsDone(WINDOW_NOMINATION_IS_DONE_EDEFAULT);
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_COUNTERPARTY:
				unsetWindowNominationCounterparty();
				return;
			case CargoPackage.SLOT__WINDOW_NOMINATION_COMMENT:
				setWindowNominationComment(WINDOW_NOMINATION_COMMENT_EDEFAULT);
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_DATE:
				unsetVolumeNominationDate();
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_DONE:
				setVolumeNominationDone(VOLUME_NOMINATION_DONE_EDEFAULT);
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_COUNTERPARTY:
				unsetVolumeNominationCounterparty();
				return;
			case CargoPackage.SLOT__VOLUME_NOMINATION_COMMENT:
				setVolumeNominationComment(VOLUME_NOMINATION_COMMENT_EDEFAULT);
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_DATE:
				unsetVesselNominationDate();
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_DONE:
				setVesselNominationDone(VESSEL_NOMINATION_DONE_EDEFAULT);
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_COUNTERPARTY:
				unsetVesselNominationCounterparty();
				return;
			case CargoPackage.SLOT__VESSEL_NOMINATION_COMMENT:
				setVesselNominationComment(VESSEL_NOMINATION_COMMENT_EDEFAULT);
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_DATE:
				unsetPortNominationDate();
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_DONE:
				setPortNominationDone(PORT_NOMINATION_DONE_EDEFAULT);
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_COUNTERPARTY:
				unsetPortNominationCounterparty();
				return;
			case CargoPackage.SLOT__PORT_NOMINATION_COMMENT:
				setPortNominationComment(PORT_NOMINATION_COMMENT_EDEFAULT);
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DATE:
				unsetPortLoadNominationDate();
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DONE:
				setPortLoadNominationDone(PORT_LOAD_NOMINATION_DONE_EDEFAULT);
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				unsetPortLoadNominationCounterparty();
				return;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COMMENT:
				setPortLoadNominationComment(PORT_LOAD_NOMINATION_COMMENT_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CargoPackage.SLOT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CargoPackage.SLOT__CONTRACT:
				return isSetContract();
			case CargoPackage.SLOT__COUNTERPARTY:
				return isSetCounterparty();
			case CargoPackage.SLOT__CN:
				return isSetCn();
			case CargoPackage.SLOT__PORT:
				return port != null;
			case CargoPackage.SLOT__WINDOW_START:
				return WINDOW_START_EDEFAULT == null ? windowStart != null : !WINDOW_START_EDEFAULT.equals(windowStart);
			case CargoPackage.SLOT__WINDOW_START_TIME:
				return isSetWindowStartTime();
			case CargoPackage.SLOT__WINDOW_SIZE:
				return isSetWindowSize();
			case CargoPackage.SLOT__WINDOW_SIZE_UNITS:
				return isSetWindowSizeUnits();
			case CargoPackage.SLOT__WINDOW_FLEX:
				return windowFlex != WINDOW_FLEX_EDEFAULT;
			case CargoPackage.SLOT__WINDOW_FLEX_UNITS:
				return windowFlexUnits != WINDOW_FLEX_UNITS_EDEFAULT;
			case CargoPackage.SLOT__DURATION:
				return isSetDuration();
			case CargoPackage.SLOT__VOLUME_LIMITS_UNIT:
				return isSetVolumeLimitsUnit();
			case CargoPackage.SLOT__MIN_QUANTITY:
				return isSetMinQuantity();
			case CargoPackage.SLOT__MAX_QUANTITY:
				return isSetMaxQuantity();
			case CargoPackage.SLOT__OPERATIONAL_TOLERANCE:
				return isSetOperationalTolerance();
			case CargoPackage.SLOT__FULL_CARGO_LOT:
				return isSetFullCargoLot();
			case CargoPackage.SLOT__OPTIONAL:
				return optional != OPTIONAL_EDEFAULT;
			case CargoPackage.SLOT__PRICE_EXPRESSION:
				return isSetPriceExpression();
			case CargoPackage.SLOT__CARGO:
				return cargo != null;
			case CargoPackage.SLOT__PRICING_EVENT:
				return isSetPricingEvent();
			case CargoPackage.SLOT__PRICING_DATE:
				return isSetPricingDate();
			case CargoPackage.SLOT__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
			case CargoPackage.SLOT__DIVERTIBLE:
				return isSetDivertible();
			case CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION:
				return isSetShippingDaysRestriction();
			case CargoPackage.SLOT__ENTITY:
				return isSetEntity();
			case CargoPackage.SLOT__RESTRICTED_CONTRACTS:
				return restrictedContracts != null && !restrictedContracts.isEmpty();
			case CargoPackage.SLOT__RESTRICTED_PORTS:
				return restrictedPorts != null && !restrictedPorts.isEmpty();
			case CargoPackage.SLOT__RESTRICTED_SLOTS:
				return restrictedSlots != null && !restrictedSlots.isEmpty();
			case CargoPackage.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE:
				return restrictedSlotsArePermissive != RESTRICTED_SLOTS_ARE_PERMISSIVE_EDEFAULT;
			case CargoPackage.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				return restrictedListsArePermissive != RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT;
			case CargoPackage.SLOT__HEDGES:
				return hedges != HEDGES_EDEFAULT;
			case CargoPackage.SLOT__MISC_COSTS:
				return miscCosts != MISC_COSTS_EDEFAULT;
			case CargoPackage.SLOT__ALLOWED_VESSELS:
				return allowedVessels != null && !allowedVessels.isEmpty();
			case CargoPackage.SLOT__CANCELLATION_EXPRESSION:
				return isSetCancellationExpression();
			case CargoPackage.SLOT__OVERRIDE_RESTRICTIONS:
				return overrideRestrictions != OVERRIDE_RESTRICTIONS_EDEFAULT;
			case CargoPackage.SLOT__NOMINATED_VESSEL:
				return nominatedVessel != null;
			case CargoPackage.SLOT__LOCKED:
				return locked != LOCKED_EDEFAULT;
			case CargoPackage.SLOT__WINDOW_NOMINATION_DATE:
				return isSetWindowNominationDate();
			case CargoPackage.SLOT__WINDOW_NOMINATION_IS_DONE:
				return windowNominationIsDone != WINDOW_NOMINATION_IS_DONE_EDEFAULT;
			case CargoPackage.SLOT__WINDOW_NOMINATION_COUNTERPARTY:
				return isSetWindowNominationCounterparty();
			case CargoPackage.SLOT__WINDOW_NOMINATION_COMMENT:
				return WINDOW_NOMINATION_COMMENT_EDEFAULT == null ? windowNominationComment != null : !WINDOW_NOMINATION_COMMENT_EDEFAULT.equals(windowNominationComment);
			case CargoPackage.SLOT__VOLUME_NOMINATION_DATE:
				return isSetVolumeNominationDate();
			case CargoPackage.SLOT__VOLUME_NOMINATION_DONE:
				return volumeNominationDone != VOLUME_NOMINATION_DONE_EDEFAULT;
			case CargoPackage.SLOT__VOLUME_NOMINATION_COUNTERPARTY:
				return isSetVolumeNominationCounterparty();
			case CargoPackage.SLOT__VOLUME_NOMINATION_COMMENT:
				return VOLUME_NOMINATION_COMMENT_EDEFAULT == null ? volumeNominationComment != null : !VOLUME_NOMINATION_COMMENT_EDEFAULT.equals(volumeNominationComment);
			case CargoPackage.SLOT__VESSEL_NOMINATION_DATE:
				return isSetVesselNominationDate();
			case CargoPackage.SLOT__VESSEL_NOMINATION_DONE:
				return vesselNominationDone != VESSEL_NOMINATION_DONE_EDEFAULT;
			case CargoPackage.SLOT__VESSEL_NOMINATION_COUNTERPARTY:
				return isSetVesselNominationCounterparty();
			case CargoPackage.SLOT__VESSEL_NOMINATION_COMMENT:
				return VESSEL_NOMINATION_COMMENT_EDEFAULT == null ? vesselNominationComment != null : !VESSEL_NOMINATION_COMMENT_EDEFAULT.equals(vesselNominationComment);
			case CargoPackage.SLOT__PORT_NOMINATION_DATE:
				return isSetPortNominationDate();
			case CargoPackage.SLOT__PORT_NOMINATION_DONE:
				return portNominationDone != PORT_NOMINATION_DONE_EDEFAULT;
			case CargoPackage.SLOT__PORT_NOMINATION_COUNTERPARTY:
				return isSetPortNominationCounterparty();
			case CargoPackage.SLOT__PORT_NOMINATION_COMMENT:
				return PORT_NOMINATION_COMMENT_EDEFAULT == null ? portNominationComment != null : !PORT_NOMINATION_COMMENT_EDEFAULT.equals(portNominationComment);
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DATE:
				return isSetPortLoadNominationDate();
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_DONE:
				return portLoadNominationDone != PORT_LOAD_NOMINATION_DONE_EDEFAULT;
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				return isSetPortLoadNominationCounterparty();
			case CargoPackage.SLOT__PORT_LOAD_NOMINATION_COMMENT:
				return PORT_LOAD_NOMINATION_COMMENT_EDEFAULT == null ? portLoadNominationComment != null : !PORT_LOAD_NOMINATION_COMMENT_EDEFAULT.equals(portLoadNominationComment);
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
				case CargoPackage.SLOT__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (derivedFeatureID) {
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
				case MMXCorePackage.NAMED_OBJECT__NAME: return CargoPackage.SLOT__NAME;
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (baseFeatureID) {
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
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (baseOperationID) {
				case TypesPackage.ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE: return CargoPackage.SLOT___GET_TIME_ZONE__EATTRIBUTE;
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_DURATION:
				return getSlotOrDelegateDuration();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY:
				return getSlotOrDelegateMinQuantity();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY:
				return getSlotOrDelegateMaxQuantity();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE:
				return getSlotOrDelegateOperationalTolerance();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT:
				return getSlotOrDelegateVolumeLimitsUnit();
			case CargoPackage.SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME:
				return getWindowEndWithSlotOrPortTime();
			case CargoPackage.SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME:
				return getWindowStartWithSlotOrPortTime();
			case CargoPackage.SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX:
				return getWindowEndWithSlotOrPortTimeWithFlex();
			case CargoPackage.SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX:
				return getWindowStartWithSlotOrPortTimeWithFlex();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_WINDOW_SIZE:
				return getSlotOrDelegateWindowSize();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_WINDOW_SIZE_UNITS:
				return getSlotOrDelegateWindowSizeUnits();
			case CargoPackage.SLOT___GET_WINDOW_SIZE_IN_HOURS:
				return getWindowSizeInHours();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_ENTITY:
				return getSlotOrDelegateEntity();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_RESTRICTED_LISTS_ARE_PERMISSIVE:
				return getSlotOrDelegateRestrictedListsArePermissive();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION:
				return getSlotOrDelegateCancellationExpression();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT:
				return getSlotOrDelegatePricingEvent();
			case CargoPackage.SLOT___GET_PRICING_DATE_AS_DATE_TIME:
				return getPricingDateAsDateTime();
			case CargoPackage.SLOT___GET_SLOT_CONTRACT_PARAMS:
				return getSlotContractParams();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY:
				return getSlotOrDelegateCounterparty();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_CN:
				return getSlotOrDelegateCN();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_DIVERTIBLE:
				return getSlotOrDelegateDivertible();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION:
				return getSlotOrDelegateShippingDaysRestriction();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_PORT_LOAD_NOMINATION_DATE:
				return getSlotOrDelegatePortLoadNominationDate();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_WINDOW_NOMINATION_DATE:
				return getSlotOrDelegateWindowNominationDate();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_VESSEL_NOMINATION_DATE:
				return getSlotOrDelegateVesselNominationDate();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_VOLUME_NOMINATION_DATE:
				return getSlotOrDelegateVolumeNominationDate();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_PORT_NOMINATION_DATE:
				return getSlotOrDelegatePortNominationDate();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_WINDOW_NOMINATION_COUNTERPARTY:
				return getSlotOrDelegateWindowNominationCounterparty();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_VESSEL_NOMINATION_COUNTERPARTY:
				return getSlotOrDelegateVesselNominationCounterparty();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_VOLUME_NOMINATION_COUNTERPARTY:
				return getSlotOrDelegateVolumeNominationCounterparty();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_PORT_NOMINATION_COUNTERPARTY:
				return getSlotOrDelegatePortNominationCounterparty();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_PORT_LOAD_NOMINATION_COUNTERPARTY:
				return getSlotOrDelegatePortLoadNominationCounterparty();
			case CargoPackage.SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT:
				return getSlotOrDelegateFullCargoLot();
			case CargoPackage.SLOT___GET_TIME_ZONE__EATTRIBUTE:
				return getTimeZone((EAttribute)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", counterparty: ");
		if (counterpartyESet) result.append(counterparty); else result.append("<unset>");
		result.append(", cn: ");
		if (cnESet) result.append(cn); else result.append("<unset>");
		result.append(", windowStart: ");
		result.append(windowStart);
		result.append(", windowStartTime: ");
		if (windowStartTimeESet) result.append(windowStartTime); else result.append("<unset>");
		result.append(", windowSize: ");
		if (windowSizeESet) result.append(windowSize); else result.append("<unset>");
		result.append(", windowSizeUnits: ");
		if (windowSizeUnitsESet) result.append(windowSizeUnits); else result.append("<unset>");
		result.append(", windowFlex: ");
		result.append(windowFlex);
		result.append(", windowFlexUnits: ");
		result.append(windowFlexUnits);
		result.append(", duration: ");
		if (durationESet) result.append(duration); else result.append("<unset>");
		result.append(", volumeLimitsUnit: ");
		if (volumeLimitsUnitESet) result.append(volumeLimitsUnit); else result.append("<unset>");
		result.append(", minQuantity: ");
		if (minQuantityESet) result.append(minQuantity); else result.append("<unset>");
		result.append(", maxQuantity: ");
		if (maxQuantityESet) result.append(maxQuantity); else result.append("<unset>");
		result.append(", operationalTolerance: ");
		if (operationalToleranceESet) result.append(operationalTolerance); else result.append("<unset>");
		result.append(", fullCargoLot: ");
		if (fullCargoLotESet) result.append(fullCargoLot); else result.append("<unset>");
		result.append(", optional: ");
		result.append(optional);
		result.append(", priceExpression: ");
		if (priceExpressionESet) result.append(priceExpression); else result.append("<unset>");
		result.append(", pricingEvent: ");
		if (pricingEventESet) result.append(pricingEvent); else result.append("<unset>");
		result.append(", pricingDate: ");
		if (pricingDateESet) result.append(pricingDate); else result.append("<unset>");
		result.append(", notes: ");
		result.append(notes);
		result.append(", divertible: ");
		if (divertibleESet) result.append(divertible); else result.append("<unset>");
		result.append(", shippingDaysRestriction: ");
		if (shippingDaysRestrictionESet) result.append(shippingDaysRestriction); else result.append("<unset>");
		result.append(", restrictedSlotsArePermissive: ");
		result.append(restrictedSlotsArePermissive);
		result.append(", restrictedListsArePermissive: ");
		result.append(restrictedListsArePermissive);
		result.append(", hedges: ");
		result.append(hedges);
		result.append(", miscCosts: ");
		result.append(miscCosts);
		result.append(", cancellationExpression: ");
		if (cancellationExpressionESet) result.append(cancellationExpression); else result.append("<unset>");
		result.append(", overrideRestrictions: ");
		result.append(overrideRestrictions);
		result.append(", locked: ");
		result.append(locked);
		result.append(", windowNominationDate: ");
		if (windowNominationDateESet) result.append(windowNominationDate); else result.append("<unset>");
		result.append(", windowNominationIsDone: ");
		result.append(windowNominationIsDone);
		result.append(", windowNominationCounterparty: ");
		if (windowNominationCounterpartyESet) result.append(windowNominationCounterparty); else result.append("<unset>");
		result.append(", windowNominationComment: ");
		result.append(windowNominationComment);
		result.append(", volumeNominationDate: ");
		if (volumeNominationDateESet) result.append(volumeNominationDate); else result.append("<unset>");
		result.append(", volumeNominationDone: ");
		result.append(volumeNominationDone);
		result.append(", volumeNominationCounterparty: ");
		if (volumeNominationCounterpartyESet) result.append(volumeNominationCounterparty); else result.append("<unset>");
		result.append(", volumeNominationComment: ");
		result.append(volumeNominationComment);
		result.append(", vesselNominationDate: ");
		if (vesselNominationDateESet) result.append(vesselNominationDate); else result.append("<unset>");
		result.append(", vesselNominationDone: ");
		result.append(vesselNominationDone);
		result.append(", vesselNominationCounterparty: ");
		if (vesselNominationCounterpartyESet) result.append(vesselNominationCounterparty); else result.append("<unset>");
		result.append(", vesselNominationComment: ");
		result.append(vesselNominationComment);
		result.append(", portNominationDate: ");
		if (portNominationDateESet) result.append(portNominationDate); else result.append("<unset>");
		result.append(", portNominationDone: ");
		result.append(portNominationDone);
		result.append(", portNominationCounterparty: ");
		if (portNominationCounterpartyESet) result.append(portNominationCounterparty); else result.append("<unset>");
		result.append(", portNominationComment: ");
		result.append(portNominationComment);
		result.append(", portLoadNominationDate: ");
		if (portLoadNominationDateESet) result.append(portLoadNominationDate); else result.append("<unset>");
		result.append(", portLoadNominationDone: ");
		result.append(portLoadNominationDone);
		result.append(", portLoadNominationCounterparty: ");
		if (portLoadNominationCounterpartyESet) result.append(portLoadNominationCounterparty); else result.append("<unset>");
		result.append(", portLoadNominationComment: ");
		result.append(portLoadNominationComment);
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		CargoPackage cargo = CargoPackage.eINSTANCE;
		PortPackage port = PortPackage.eINSTANCE;
		CommercialPackage commercial = CommercialPackage.eINSTANCE;
		if (cargo.getSlot_WindowStartTime() == feature) {
			return new DelegateInformation(cargo.getSlot_Port(), port.getPort_DefaultStartTime(), (Integer) 7);
		} else if (cargo.getSlot_WindowSize() == feature) {
			return new DelegateInformation(cargo.getSlot_Port(), port.getPort_DefaultWindowSize(), (Integer) 6);
		} else if (CargoPackage.eINSTANCE.getSlot_MinQuantity() == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_MinQuantity(), (Integer) 0);
		} else if (CargoPackage.eINSTANCE.getSlot_MaxQuantity() == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_MaxQuantity(), (Integer) Integer.MAX_VALUE);
		} else if (CargoPackage.eINSTANCE.getSlot_OperationalTolerance() == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_OperationalTolerance(), (Double) 0.0);
		} else if (CargoPackage.Literals.SLOT__ENTITY == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_Entity(), null);
		} else if (CargoPackage.Literals.SLOT__COUNTERPARTY == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_Counterparty(), null);
		} else if (cargo.getSlot_Divertible() == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_Divertible(), Boolean.FALSE);
		} else if (cargo.getSlot_ShippingDaysRestriction() == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_ShippingDaysRestriction(), (Integer)0);
		} else if (CargoPackage.Literals.SLOT__CN == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_Cn(), null);
		} else if (CargoPackage.Literals.SLOT__CANCELLATION_EXPRESSION == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_CancellationExpression(), (String)"");
		} else if (CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT == feature) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_VolumeLimitsUnit(), VolumeUnits.M3);
		} else if (CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS == feature) {
			return new DelegateInformation(cargo.getSlot_Port(), port.getPort_DefaultWindowSizeUnits(), TimePeriod.HOURS);
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final Contract contract = (Contract) getContract();
					if (!isOverrideRestrictions() && contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE)) {
							result = contract.eGet(CommercialPackage.Literals.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE);
						}
					}
					if (result == null) {
						return false;
					}
					return result;
					
				}				
			};
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final Contract contract = (Contract) getContract();
					if (!isOverrideRestrictions() && contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS)) {
							result = contract.eGet(CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS);
						}
					}
					return result;
					
				}				
			};
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final Contract contract = (Contract) getContract();
					if (!isOverrideRestrictions() && contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS)) {
							result = contract.eGet(CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS);
						}
					}
					return result;
					
				}				
			};
		} else if (feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE)) {
							final int wnSize = ((Contract) contract).getWindowNominationSize();
							final TimePeriod wnUnits = ((Contract) contract).getWindowNominationSizeUnits();
							if (wnUnits == null) return result;
							result = NominationUtils.computeNewDate(getWindowStart(), wnUnits, -wnSize);
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_DATE) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE)) {
							final int wnSize = ((Contract) contract).getVolumeNominationSize();
							final TimePeriod wnUnits = ((Contract) contract).getVolumeNominationSizeUnits();
							if (wnUnits == null) return result;
							result = NominationUtils.computeNewDate(getWindowStart(), wnUnits, -wnSize);
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_DATE) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE)) {
							final int wnSize = ((Contract) contract).getVesselNominationSize();
							final TimePeriod wnUnits = ((Contract) contract).getVesselNominationSizeUnits();
							if (wnUnits == null) return result;
							result = NominationUtils.computeNewDate(getWindowStart(), wnUnits, -wnSize);
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_DATE) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE)) {
							final int wnSize = ((Contract) contract).getPortNominationSize();
							final TimePeriod wnUnits = ((Contract) contract).getPortNominationSizeUnits();
							if (wnUnits == null) return result;
							result = NominationUtils.computeNewDate(getWindowStart(), wnUnits, -wnSize);
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COUNTERPARTY) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = false;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY)) {
							result = ((Contract) contract).isWindowNominationCounterparty();
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COUNTERPARTY) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = false;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY)) {
							result = ((Contract) contract).isVesselNominationCounterparty();
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COUNTERPARTY) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = false;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY)) {
							result = ((Contract) contract).isVolumeNominationCounterparty();
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_COUNTERPARTY) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = false;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_COUNTERPARTY)) {
							result = ((Contract) contract).isPortNominationCounterparty();
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT);
				}
				
				public Object getValue(final EObject object) {
					Object result = false;
					final Contract contract = (Contract) getContract();
					if (contract != null) {
						if (contract.eIsSet(CommercialPackage.Literals.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY)) {
							result = ((Contract) contract).isPortNominationCounterparty();
						}
					}
					return result;
					
				}	
			};
		} else if (feature == CargoPackage.Literals.SLOT__FULL_CARGO_LOT) {
			return new DelegateInformation(cargo.getSlot_Contract(), commercial.getContract_FullCargoLot(), Boolean.FALSE);
		}
		
		return super.getUnsetValueOrDelegate(feature);
	}	
	
} // end of SlotImpl

// finish type fixing

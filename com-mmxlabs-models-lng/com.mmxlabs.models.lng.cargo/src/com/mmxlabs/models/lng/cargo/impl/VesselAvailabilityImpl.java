/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXObject.DelegateInformation;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#isFleet <em>Fleet</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getCharterNumber <em>Charter Number</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getStartBy <em>Start By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getEndBy <em>End By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getEndHeel <em>End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#isForceHireCostOnlyEndRule <em>Force Hire Cost Only End Rule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getRepositioningFee <em>Repositioning Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getBallastBonusContract <em>Ballast Bonus Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getCharterContract <em>Charter Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getMaxDuration <em>Max Duration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselAvailabilityImpl extends UUIDObjectImpl implements VesselAvailability {
	/**
	 * The default value of the '{@link #isFleet() <em>Fleet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFleet()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FLEET_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFleet() <em>Fleet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFleet()
	 * @generated
	 * @ordered
	 */
	protected boolean fleet = FLEET_EDEFAULT;

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
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The default value of the '{@link #getCharterNumber() <em>Charter Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int CHARTER_NUMBER_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getCharterNumber() <em>Charter Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterNumber()
	 * @generated
	 * @ordered
	 */
	protected int charterNumber = CHARTER_NUMBER_EDEFAULT;

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
	 * The default value of the '{@link #getTimeCharterRate() <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeCharterRate()
	 * @generated
	 * @ordered
	 */
	protected static final String TIME_CHARTER_RATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTimeCharterRate() <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeCharterRate()
	 * @generated
	 * @ordered
	 */
	protected String timeCharterRate = TIME_CHARTER_RATE_EDEFAULT;

	/**
	 * This is true if the Time Charter Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean timeCharterRateESet;

	/**
	 * The cached value of the '{@link #getStartAt() <em>Start At</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAt()
	 * @generated
	 * @ordered
	 */
	protected Port startAt;

	/**
	 * The default value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime START_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime startAfter = START_AFTER_EDEFAULT;

	/**
	 * This is true if the Start After attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startAfterESet;

	/**
	 * The default value of the '{@link #getStartBy() <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBy()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime START_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartBy() <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBy()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime startBy = START_BY_EDEFAULT;

	/**
	 * This is true if the Start By attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startByESet;

	/**
	 * The cached value of the '{@link #getEndAt() <em>End At</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndAt()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> endAt;

	/**
	 * The default value of the '{@link #getEndAfter() <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndAfter()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime END_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndAfter() <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndAfter()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime endAfter = END_AFTER_EDEFAULT;

	/**
	 * This is true if the End After attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endAfterESet;

	/**
	 * The default value of the '{@link #getEndBy() <em>End By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndBy()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime END_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndBy() <em>End By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndBy()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime endBy = END_BY_EDEFAULT;

	/**
	 * This is true if the End By attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endByESet;

	/**
	 * The cached value of the '{@link #getStartHeel() <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartHeel()
	 * @generated
	 * @ordered
	 */
	protected StartHeelOptions startHeel;

	/**
	 * The cached value of the '{@link #getEndHeel() <em>End Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeel()
	 * @generated
	 * @ordered
	 */
	protected EndHeelOptions endHeel;

	/**
	 * The default value of the '{@link #isForceHireCostOnlyEndRule() <em>Force Hire Cost Only End Rule</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isForceHireCostOnlyEndRule()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FORCE_HIRE_COST_ONLY_END_RULE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isForceHireCostOnlyEndRule() <em>Force Hire Cost Only End Rule</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isForceHireCostOnlyEndRule()
	 * @generated
	 * @ordered
	 */
	protected boolean forceHireCostOnlyEndRule = FORCE_HIRE_COST_ONLY_END_RULE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected static final String REPOSITIONING_FEE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected String repositioningFee = REPOSITIONING_FEE_EDEFAULT;

	/**
	 * This is true if the Repositioning Fee attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean repositioningFeeESet;

	/**
	 * The cached value of the '{@link #getBallastBonusContract() <em>Ballast Bonus Contract</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusContract()
	 * @generated
	 * @ordered
	 */
	protected BallastBonusContract ballastBonusContract;

	/**
	 * The cached value of the '{@link #getCharterContract() <em>Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterContract()
	 * @generated
	 * @ordered
	 */
	protected CharterContract charterContract;

	/**
	 * This is true if the Charter Contract reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean charterContractESet;

	/**
	 * The default value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected int minDuration = MIN_DURATION_EDEFAULT;

	/**
	 * This is true if the Min Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minDurationESet;

	/**
	 * The default value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected int maxDuration = MAX_DURATION_EDEFAULT;

	/**
	 * This is true if the Max Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxDurationESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselAvailabilityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.VESSEL_AVAILABILITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFleet() {
		return fleet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFleet(boolean newFleet) {
		boolean oldFleet = fleet;
		fleet = newFleet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__FLEET, oldFleet, fleet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_AVAILABILITY__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTimeCharterRate() {
		return timeCharterRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTimeCharterRate(String newTimeCharterRate) {
		String oldTimeCharterRate = timeCharterRate;
		timeCharterRate = newTimeCharterRate;
		boolean oldTimeCharterRateESet = timeCharterRateESet;
		timeCharterRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE, oldTimeCharterRate, timeCharterRate, !oldTimeCharterRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetTimeCharterRate() {
		String oldTimeCharterRate = timeCharterRate;
		boolean oldTimeCharterRateESet = timeCharterRateESet;
		timeCharterRate = TIME_CHARTER_RATE_EDEFAULT;
		timeCharterRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE, oldTimeCharterRate, TIME_CHARTER_RATE_EDEFAULT, oldTimeCharterRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetTimeCharterRate() {
		return timeCharterRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getStartAt() {
		if (startAt != null && startAt.eIsProxy()) {
			InternalEObject oldStartAt = (InternalEObject)startAt;
			startAt = (Port)eResolveProxy(oldStartAt);
			if (startAt != oldStartAt) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_AVAILABILITY__START_AT, oldStartAt, startAt));
			}
		}
		return startAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetStartAt() {
		return startAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartAt(Port newStartAt) {
		Port oldStartAt = startAt;
		startAt = newStartAt;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__START_AT, oldStartAt, startAt));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getStartAfter() {
		return startAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartAfter(LocalDateTime newStartAfter) {
		LocalDateTime oldStartAfter = startAfter;
		startAfter = newStartAfter;
		boolean oldStartAfterESet = startAfterESet;
		startAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__START_AFTER, oldStartAfter, startAfter, !oldStartAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetStartAfter() {
		LocalDateTime oldStartAfter = startAfter;
		boolean oldStartAfterESet = startAfterESet;
		startAfter = START_AFTER_EDEFAULT;
		startAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__START_AFTER, oldStartAfter, START_AFTER_EDEFAULT, oldStartAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetStartAfter() {
		return startAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getStartBy() {
		return startBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartBy(LocalDateTime newStartBy) {
		LocalDateTime oldStartBy = startBy;
		startBy = newStartBy;
		boolean oldStartByESet = startByESet;
		startByESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__START_BY, oldStartBy, startBy, !oldStartByESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetStartBy() {
		LocalDateTime oldStartBy = startBy;
		boolean oldStartByESet = startByESet;
		startBy = START_BY_EDEFAULT;
		startByESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__START_BY, oldStartBy, START_BY_EDEFAULT, oldStartByESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetStartBy() {
		return startByESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getEndAt() {
		if (endAt == null) {
			endAt = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CargoPackage.VESSEL_AVAILABILITY__END_AT);
		}
		return endAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getEndAfter() {
		return endAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndAfter(LocalDateTime newEndAfter) {
		LocalDateTime oldEndAfter = endAfter;
		endAfter = newEndAfter;
		boolean oldEndAfterESet = endAfterESet;
		endAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__END_AFTER, oldEndAfter, endAfter, !oldEndAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetEndAfter() {
		LocalDateTime oldEndAfter = endAfter;
		boolean oldEndAfterESet = endAfterESet;
		endAfter = END_AFTER_EDEFAULT;
		endAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__END_AFTER, oldEndAfter, END_AFTER_EDEFAULT, oldEndAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetEndAfter() {
		return endAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getEndBy() {
		return endBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndBy(LocalDateTime newEndBy) {
		LocalDateTime oldEndBy = endBy;
		endBy = newEndBy;
		boolean oldEndByESet = endByESet;
		endByESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__END_BY, oldEndBy, endBy, !oldEndByESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetEndBy() {
		LocalDateTime oldEndBy = endBy;
		boolean oldEndByESet = endByESet;
		endBy = END_BY_EDEFAULT;
		endByESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__END_BY, oldEndBy, END_BY_EDEFAULT, oldEndByESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetEndBy() {
		return endByESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StartHeelOptions getStartHeel() {
		if (startHeel != null && startHeel.eIsProxy()) {
			InternalEObject oldStartHeel = (InternalEObject)startHeel;
			startHeel = (StartHeelOptions)eResolveProxy(oldStartHeel);
			if (startHeel != oldStartHeel) {
				InternalEObject newStartHeel = (InternalEObject)startHeel;
				NotificationChain msgs = oldStartHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__START_HEEL, null, null);
				if (newStartHeel.eInternalContainer() == null) {
					msgs = newStartHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__START_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_AVAILABILITY__START_HEEL, oldStartHeel, startHeel));
			}
		}
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StartHeelOptions basicGetStartHeel() {
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartHeel(StartHeelOptions newStartHeel, NotificationChain msgs) {
		StartHeelOptions oldStartHeel = startHeel;
		startHeel = newStartHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__START_HEEL, oldStartHeel, newStartHeel);
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
	public void setStartHeel(StartHeelOptions newStartHeel) {
		if (newStartHeel != startHeel) {
			NotificationChain msgs = null;
			if (startHeel != null)
				msgs = ((InternalEObject)startHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__START_HEEL, null, msgs);
			if (newStartHeel != null)
				msgs = ((InternalEObject)newStartHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__START_HEEL, null, msgs);
			msgs = basicSetStartHeel(newStartHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__START_HEEL, newStartHeel, newStartHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EndHeelOptions getEndHeel() {
		if (endHeel != null && endHeel.eIsProxy()) {
			InternalEObject oldEndHeel = (InternalEObject)endHeel;
			endHeel = (EndHeelOptions)eResolveProxy(oldEndHeel);
			if (endHeel != oldEndHeel) {
				InternalEObject newEndHeel = (InternalEObject)endHeel;
				NotificationChain msgs = oldEndHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__END_HEEL, null, null);
				if (newEndHeel.eInternalContainer() == null) {
					msgs = newEndHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__END_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_AVAILABILITY__END_HEEL, oldEndHeel, endHeel));
			}
		}
		return endHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EndHeelOptions basicGetEndHeel() {
		return endHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEndHeel(EndHeelOptions newEndHeel, NotificationChain msgs) {
		EndHeelOptions oldEndHeel = endHeel;
		endHeel = newEndHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__END_HEEL, oldEndHeel, newEndHeel);
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
	public void setEndHeel(EndHeelOptions newEndHeel) {
		if (newEndHeel != endHeel) {
			NotificationChain msgs = null;
			if (endHeel != null)
				msgs = ((InternalEObject)endHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__END_HEEL, null, msgs);
			if (newEndHeel != null)
				msgs = ((InternalEObject)newEndHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__END_HEEL, null, msgs);
			msgs = basicSetEndHeel(newEndHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__END_HEEL, newEndHeel, newEndHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isForceHireCostOnlyEndRule() {
		return forceHireCostOnlyEndRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setForceHireCostOnlyEndRule(boolean newForceHireCostOnlyEndRule) {
		boolean oldForceHireCostOnlyEndRule = forceHireCostOnlyEndRule;
		forceHireCostOnlyEndRule = newForceHireCostOnlyEndRule;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE, oldForceHireCostOnlyEndRule, forceHireCostOnlyEndRule));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__OPTIONAL, oldOptional, optional));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRepositioningFee() {
		return repositioningFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRepositioningFee(String newRepositioningFee) {
		String oldRepositioningFee = repositioningFee;
		repositioningFee = newRepositioningFee;
		boolean oldRepositioningFeeESet = repositioningFeeESet;
		repositioningFeeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__REPOSITIONING_FEE, oldRepositioningFee, repositioningFee, !oldRepositioningFeeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetRepositioningFee() {
		String oldRepositioningFee = repositioningFee;
		boolean oldRepositioningFeeESet = repositioningFeeESet;
		repositioningFee = REPOSITIONING_FEE_EDEFAULT;
		repositioningFeeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__REPOSITIONING_FEE, oldRepositioningFee, REPOSITIONING_FEE_EDEFAULT, oldRepositioningFeeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetRepositioningFee() {
		return repositioningFeeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BallastBonusContract getBallastBonusContract() {
		if (ballastBonusContract != null && ballastBonusContract.eIsProxy()) {
			InternalEObject oldBallastBonusContract = (InternalEObject)ballastBonusContract;
			ballastBonusContract = (BallastBonusContract)eResolveProxy(oldBallastBonusContract);
			if (ballastBonusContract != oldBallastBonusContract) {
				InternalEObject newBallastBonusContract = (InternalEObject)ballastBonusContract;
				NotificationChain msgs = oldBallastBonusContract.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, null, null);
				if (newBallastBonusContract.eInternalContainer() == null) {
					msgs = newBallastBonusContract.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, oldBallastBonusContract, ballastBonusContract));
			}
		}
		return ballastBonusContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BallastBonusContract basicGetBallastBonusContract() {
		return ballastBonusContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBallastBonusContract(BallastBonusContract newBallastBonusContract, NotificationChain msgs) {
		BallastBonusContract oldBallastBonusContract = ballastBonusContract;
		ballastBonusContract = newBallastBonusContract;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, oldBallastBonusContract, newBallastBonusContract);
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
	public void setBallastBonusContract(BallastBonusContract newBallastBonusContract) {
		if (newBallastBonusContract != ballastBonusContract) {
			NotificationChain msgs = null;
			if (ballastBonusContract != null)
				msgs = ((InternalEObject)ballastBonusContract).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, null, msgs);
			if (newBallastBonusContract != null)
				msgs = ((InternalEObject)newBallastBonusContract).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, null, msgs);
			msgs = basicSetBallastBonusContract(newBallastBonusContract, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, newBallastBonusContract, newBallastBonusContract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCharterNumber() {
		return charterNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterContract getCharterContract() {
		if (charterContract != null && charterContract.eIsProxy()) {
			InternalEObject oldCharterContract = (InternalEObject)charterContract;
			charterContract = (CharterContract)eResolveProxy(oldCharterContract);
			if (charterContract != oldCharterContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT, oldCharterContract, charterContract));
			}
		}
		return charterContract;
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterNumber(int newCharterNumber) {
		int oldCharterNumber = charterNumber;
		charterNumber = newCharterNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__CHARTER_NUMBER, oldCharterNumber, charterNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterContract basicGetCharterContract() {
		return charterContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterContract(CharterContract newCharterContract) {
		CharterContract oldCharterContract = charterContract;
		charterContract = newCharterContract;
		boolean oldCharterContractESet = charterContractESet;
		charterContractESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT, oldCharterContract, charterContract, !oldCharterContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCharterContract() {
		CharterContract oldCharterContract = charterContract;
		boolean oldCharterContractESet = charterContractESet;
		charterContract = null;
		charterContractESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT, oldCharterContract, null, oldCharterContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCharterContract() {
		return charterContractESet;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinDuration() {
		return minDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinDuration(int newMinDuration) {
		int oldMinDuration = minDuration;
		minDuration = newMinDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__MIN_DURATION, oldMinDuration, minDuration, !oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinDuration() {
		int oldMinDuration = minDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDuration = MIN_DURATION_EDEFAULT;
		minDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__MIN_DURATION, oldMinDuration, MIN_DURATION_EDEFAULT, oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinDuration() {
		return minDurationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxDuration(int newMaxDuration) {
		int oldMaxDuration = maxDuration;
		maxDuration = newMaxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__MAX_DURATION, oldMaxDuration, maxDuration, !oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxDuration() {
		int oldMaxDuration = maxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDuration = MAX_DURATION_EDEFAULT;
		maxDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__MAX_DURATION, oldMaxDuration, MAX_DURATION_EDEFAULT, oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxDuration() {
		return maxDurationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getStartByAsDateTime() {
		if (isSetStartBy()) {
			final LocalDateTime ldt = getStartBy();
			if (ldt != null) {
				return ldt.atZone(ZoneId.of("UTC"));
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getStartAfterAsDateTime() {
		if (isSetStartAfter()) {
			final LocalDateTime ldt = getStartAfter();
			if (ldt != null) {
				return ldt.atZone(ZoneId.of("UTC"));
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getEndByAsDateTime() {
		if (isSetEndBy()) {
			final LocalDateTime ldt = getEndBy();
			if (ldt != null) {
				return ldt.atZone(ZoneId.of("UTC"));
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getEndAfterAsDateTime() {
		
		if (isSetEndAfter()) {
			final LocalDateTime ldt = getEndAfter();
			if (ldt != null) {
				return ldt.atZone(ZoneId.of("UTC"));
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BallastBonusContract getCharterOrDelegateBallastBonusContract() {
		if (this.isSetCharterContract()) {
			if (this.getCharterContract() != null && this.getCharterContract() instanceof BallastBonusCharterContract) {
				if (this.getCharterContract() instanceof BallastBonusCharterContract) {
					BallastBonusCharterContract bbcc = (BallastBonusCharterContract) this.getCharterContract();
					if (bbcc.getBallastBonusContract() != null) {
						return bbcc.getBallastBonusContract();
					}
				}
			}
		} else {
			return this.getBallastBonusContract();
		}
		return null;
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getCharterOrDelegateMinDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.VESSEL_AVAILABILITY__MIN_DURATION);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getCharterOrDelegateMaxDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.VESSEL_AVAILABILITY__MAX_DURATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String jsonid() {
		final Vessel vessel = getVessel();
		final String vesselId = vessel == null ? "<unknown vessel>" : vessel.getName();
		return String.format("%s-%d", vesselId, getCharterNumber());
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BaseLegalEntity getCharterOrDelegateEntity() {
		return (BaseLegalEntity) eGetWithDefault(CargoPackage.Literals.VESSEL_AVAILABILITY__ENTITY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getCharterOrDelegateRepositioningFee() {
		return (String) eGetWithDefault(CargoPackage.Literals.VESSEL_AVAILABILITY__REPOSITIONING_FEE);
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_AVAILABILITY__ENTITY, oldEntity, entity));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__ENTITY, oldEntity, entity, !oldEntityESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_AVAILABILITY__ENTITY, oldEntity, null, oldEntityESet));
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.VESSEL_AVAILABILITY__START_HEEL:
				return basicSetStartHeel(null, msgs);
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
				return basicSetEndHeel(null, msgs);
			case CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT:
				return basicSetBallastBonusContract(null, msgs);
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
			case CargoPackage.VESSEL_AVAILABILITY__FLEET:
				return isFleet();
			case CargoPackage.VESSEL_AVAILABILITY__OPTIONAL:
				return isOptional();
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_NUMBER:
				return getCharterNumber();
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				return getTimeCharterRate();
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				if (resolve) return getStartAt();
				return basicGetStartAt();
			case CargoPackage.VESSEL_AVAILABILITY__START_AFTER:
				return getStartAfter();
			case CargoPackage.VESSEL_AVAILABILITY__START_BY:
				return getStartBy();
			case CargoPackage.VESSEL_AVAILABILITY__END_AT:
				return getEndAt();
			case CargoPackage.VESSEL_AVAILABILITY__END_AFTER:
				return getEndAfter();
			case CargoPackage.VESSEL_AVAILABILITY__END_BY:
				return getEndBy();
			case CargoPackage.VESSEL_AVAILABILITY__START_HEEL:
				if (resolve) return getStartHeel();
				return basicGetStartHeel();
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
				if (resolve) return getEndHeel();
				return basicGetEndHeel();
			case CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE:
				return isForceHireCostOnlyEndRule();
			case CargoPackage.VESSEL_AVAILABILITY__REPOSITIONING_FEE:
				return getRepositioningFee();
			case CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT:
				if (resolve) return getBallastBonusContract();
				return basicGetBallastBonusContract();
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT:
				if (resolve) return getCharterContract();
				return basicGetCharterContract();
			case CargoPackage.VESSEL_AVAILABILITY__MIN_DURATION:
				return getMinDuration();
			case CargoPackage.VESSEL_AVAILABILITY__MAX_DURATION:
				return getMaxDuration();
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
			case CargoPackage.VESSEL_AVAILABILITY__FLEET:
				setFleet((Boolean)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__OPTIONAL:
				setOptional((Boolean)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_NUMBER:
				setCharterNumber((Integer)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				setTimeCharterRate((String)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				setStartAt((Port)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_AFTER:
				setStartAfter((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_BY:
				setStartBy((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_AT:
				getEndAt().clear();
				getEndAt().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_AFTER:
				setEndAfter((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_BY:
				setEndBy((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_HEEL:
				setStartHeel((StartHeelOptions)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
				setEndHeel((EndHeelOptions)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE:
				setForceHireCostOnlyEndRule((Boolean)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__REPOSITIONING_FEE:
				setRepositioningFee((String)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT:
				setCharterContract((CharterContract)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__MIN_DURATION:
				setMinDuration((Integer)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__MAX_DURATION:
				setMaxDuration((Integer)newValue);
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
			case CargoPackage.VESSEL_AVAILABILITY__FLEET:
				setFleet(FLEET_EDEFAULT);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__OPTIONAL:
				setOptional(OPTIONAL_EDEFAULT);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				setVessel((Vessel)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_NUMBER:
				setCharterNumber(CHARTER_NUMBER_EDEFAULT);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				unsetEntity();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				unsetTimeCharterRate();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				setStartAt((Port)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_AFTER:
				unsetStartAfter();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_BY:
				unsetStartBy();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_AT:
				getEndAt().clear();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_AFTER:
				unsetEndAfter();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_BY:
				unsetEndBy();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_HEEL:
				setStartHeel((StartHeelOptions)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
				setEndHeel((EndHeelOptions)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE:
				setForceHireCostOnlyEndRule(FORCE_HIRE_COST_ONLY_END_RULE_EDEFAULT);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__REPOSITIONING_FEE:
				unsetRepositioningFee();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT:
				unsetCharterContract();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__MIN_DURATION:
				unsetMinDuration();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__MAX_DURATION:
				unsetMaxDuration();
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
			case CargoPackage.VESSEL_AVAILABILITY__FLEET:
				return fleet != FLEET_EDEFAULT;
			case CargoPackage.VESSEL_AVAILABILITY__OPTIONAL:
				return optional != OPTIONAL_EDEFAULT;
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				return vessel != null;
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_NUMBER:
				return charterNumber != CHARTER_NUMBER_EDEFAULT;
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				return isSetEntity();
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				return isSetTimeCharterRate();
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				return startAt != null;
			case CargoPackage.VESSEL_AVAILABILITY__START_AFTER:
				return isSetStartAfter();
			case CargoPackage.VESSEL_AVAILABILITY__START_BY:
				return isSetStartBy();
			case CargoPackage.VESSEL_AVAILABILITY__END_AT:
				return endAt != null && !endAt.isEmpty();
			case CargoPackage.VESSEL_AVAILABILITY__END_AFTER:
				return isSetEndAfter();
			case CargoPackage.VESSEL_AVAILABILITY__END_BY:
				return isSetEndBy();
			case CargoPackage.VESSEL_AVAILABILITY__START_HEEL:
				return startHeel != null;
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
				return endHeel != null;
			case CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE:
				return forceHireCostOnlyEndRule != FORCE_HIRE_COST_ONLY_END_RULE_EDEFAULT;
			case CargoPackage.VESSEL_AVAILABILITY__REPOSITIONING_FEE:
				return isSetRepositioningFee();
			case CargoPackage.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT:
				return ballastBonusContract != null;
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT:
				return isSetCharterContract();
			case CargoPackage.VESSEL_AVAILABILITY__MIN_DURATION:
				return isSetMinDuration();
			case CargoPackage.VESSEL_AVAILABILITY__MAX_DURATION:
				return isSetMaxDuration();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.VESSEL_AVAILABILITY___GET_START_BY_AS_DATE_TIME:
				return getStartByAsDateTime();
			case CargoPackage.VESSEL_AVAILABILITY___GET_START_AFTER_AS_DATE_TIME:
				return getStartAfterAsDateTime();
			case CargoPackage.VESSEL_AVAILABILITY___GET_END_BY_AS_DATE_TIME:
				return getEndByAsDateTime();
			case CargoPackage.VESSEL_AVAILABILITY___GET_END_AFTER_AS_DATE_TIME:
				return getEndAfterAsDateTime();
			case CargoPackage.VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_BALLAST_BONUS_CONTRACT:
				return getCharterOrDelegateBallastBonusContract();
			case CargoPackage.VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MIN_DURATION:
				return getCharterOrDelegateMinDuration();
			case CargoPackage.VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MAX_DURATION:
				return getCharterOrDelegateMaxDuration();
			case CargoPackage.VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_ENTITY:
				return getCharterOrDelegateEntity();
			case CargoPackage.VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_REPOSITIONING_FEE:
				return getCharterOrDelegateRepositioningFee();
			case CargoPackage.VESSEL_AVAILABILITY___JSONID:
				return jsonid();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (fleet: ");
		result.append(fleet);
		result.append(", optional: ");
		result.append(optional);
		result.append(", charterNumber: ");
		result.append(charterNumber);
		result.append(", timeCharterRate: ");
		if (timeCharterRateESet) result.append(timeCharterRate); else result.append("<unset>");
		result.append(", startAfter: ");
		if (startAfterESet) result.append(startAfter); else result.append("<unset>");
		result.append(", startBy: ");
		if (startByESet) result.append(startBy); else result.append("<unset>");
		result.append(", endAfter: ");
		if (endAfterESet) result.append(endAfter); else result.append("<unset>");
		result.append(", endBy: ");
		if (endByESet) result.append(endBy); else result.append("<unset>");
		result.append(", forceHireCostOnlyEndRule: ");
		result.append(forceHireCostOnlyEndRule);
		result.append(", repositioningFee: ");
		if (repositioningFeeESet) result.append(repositioningFee); else result.append("<unset>");
		result.append(", minDuration: ");
		if (minDurationESet) result.append(minDuration); else result.append("<unset>");
		result.append(", maxDuration: ");
		if (maxDurationESet) result.append(maxDuration); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}
	
	/**
	 * @generated NOT
	 */
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		CargoPackage cargo = CargoPackage.eINSTANCE;
		CommercialPackage commercial = CommercialPackage.eINSTANCE;
		if (cargo.getVesselAvailability_MinDuration() == feature) {
			return new DelegateInformation(cargo.getVesselAvailability_CharterContract(), commercial.getCharterContract_MinDuration(), (Integer) 0);
		} else if (cargo.getVesselAvailability_MaxDuration() == feature) {
			return new DelegateInformation(cargo.getVesselAvailability_CharterContract(), commercial.getCharterContract_MaxDuration(), (Integer) 0);
		} else if (cargo.getVesselAvailability_Entity() == feature) {
			return new DelegateInformation(cargo.getVesselAvailability_CharterContract(), commercial.getBallastBonusCharterContract_Entity(), null);
		} else if (cargo.getVesselAvailability_RepositioningFee() == feature) {
			return new DelegateInformation(cargo.getVesselAvailability_CharterContract(), commercial.getBallastBonusCharterContract_RepositioningFee(), null);
		}
		
		return super.getUnsetValueOrDelegate(feature);
	}	
} //VesselAvailabilityImpl

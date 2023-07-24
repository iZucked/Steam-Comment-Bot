/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CIIEndOptions;
import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Charter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getCharterNumber <em>Charter Number</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getStartBy <em>Start By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getEndBy <em>End By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getEndHeel <em>End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#isForceHireCostOnlyEndRule <em>Force Hire Cost Only End Rule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getContainedCharterContract <em>Contained Charter Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getMaxDuration <em>Max Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#isCharterContractOverride <em>Charter Contract Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getGenericCharterContract <em>Generic Charter Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getCiiStartOptions <em>Cii Start Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl#getCiiEndOptions <em>Cii End Options</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselCharterImpl extends UUIDObjectImpl implements VesselCharter {
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
	 * The cached value of the '{@link #getContainedCharterContract() <em>Contained Charter Contract</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedCharterContract()
	 * @generated
	 * @ordered
	 */
	protected GenericCharterContract containedCharterContract;

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
	 * The default value of the '{@link #isCharterContractOverride() <em>Charter Contract Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCharterContractOverride()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CHARTER_CONTRACT_OVERRIDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCharterContractOverride() <em>Charter Contract Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCharterContractOverride()
	 * @generated
	 * @ordered
	 */
	protected boolean charterContractOverride = CHARTER_CONTRACT_OVERRIDE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGenericCharterContract() <em>Generic Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGenericCharterContract()
	 * @generated
	 * @ordered
	 */
	protected GenericCharterContract genericCharterContract;

	/**
	 * This is true if the Generic Charter Contract reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean genericCharterContractESet;

	/**
	 * The cached value of the '{@link #getCiiStartOptions() <em>Cii Start Options</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCiiStartOptions()
	 * @generated
	 * @ordered
	 */
	protected CIIStartOptions ciiStartOptions;

	/**
	 * The cached value of the '{@link #getCiiEndOptions() <em>Cii End Options</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCiiEndOptions()
	 * @generated
	 * @ordered
	 */
	protected CIIEndOptions ciiEndOptions;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselCharterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.VESSEL_CHARTER;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__VESSEL, oldVessel, vessel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__VESSEL, oldVessel, vessel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__TIME_CHARTER_RATE, oldTimeCharterRate, timeCharterRate, !oldTimeCharterRateESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__TIME_CHARTER_RATE, oldTimeCharterRate, TIME_CHARTER_RATE_EDEFAULT, oldTimeCharterRateESet));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__START_AT, oldStartAt, startAt));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__START_AT, oldStartAt, startAt));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__START_AFTER, oldStartAfter, startAfter, !oldStartAfterESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__START_AFTER, oldStartAfter, START_AFTER_EDEFAULT, oldStartAfterESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__START_BY, oldStartBy, startBy, !oldStartByESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__START_BY, oldStartBy, START_BY_EDEFAULT, oldStartByESet));
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
			endAt = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CargoPackage.VESSEL_CHARTER__END_AT);
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__END_AFTER, oldEndAfter, endAfter, !oldEndAfterESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__END_AFTER, oldEndAfter, END_AFTER_EDEFAULT, oldEndAfterESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__END_BY, oldEndBy, endBy, !oldEndByESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__END_BY, oldEndBy, END_BY_EDEFAULT, oldEndByESet));
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
				NotificationChain msgs = oldStartHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__START_HEEL, null, null);
				if (newStartHeel.eInternalContainer() == null) {
					msgs = newStartHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__START_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__START_HEEL, oldStartHeel, startHeel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__START_HEEL, oldStartHeel, newStartHeel);
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
				msgs = ((InternalEObject)startHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__START_HEEL, null, msgs);
			if (newStartHeel != null)
				msgs = ((InternalEObject)newStartHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__START_HEEL, null, msgs);
			msgs = basicSetStartHeel(newStartHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__START_HEEL, newStartHeel, newStartHeel));
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
				NotificationChain msgs = oldEndHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__END_HEEL, null, null);
				if (newEndHeel.eInternalContainer() == null) {
					msgs = newEndHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__END_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__END_HEEL, oldEndHeel, endHeel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__END_HEEL, oldEndHeel, newEndHeel);
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
				msgs = ((InternalEObject)endHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__END_HEEL, null, msgs);
			if (newEndHeel != null)
				msgs = ((InternalEObject)newEndHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__END_HEEL, null, msgs);
			msgs = basicSetEndHeel(newEndHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__END_HEEL, newEndHeel, newEndHeel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE, oldForceHireCostOnlyEndRule, forceHireCostOnlyEndRule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GenericCharterContract getContainedCharterContract() {
		if (containedCharterContract != null && containedCharterContract.eIsProxy()) {
			InternalEObject oldContainedCharterContract = (InternalEObject)containedCharterContract;
			containedCharterContract = (GenericCharterContract)eResolveProxy(oldContainedCharterContract);
			if (containedCharterContract != oldContainedCharterContract) {
				InternalEObject newContainedCharterContract = (InternalEObject)containedCharterContract;
				NotificationChain msgs = oldContainedCharterContract.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, null, null);
				if (newContainedCharterContract.eInternalContainer() == null) {
					msgs = newContainedCharterContract.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, oldContainedCharterContract, containedCharterContract));
			}
		}
		return containedCharterContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenericCharterContract basicGetContainedCharterContract() {
		return containedCharterContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContainedCharterContract(GenericCharterContract newContainedCharterContract, NotificationChain msgs) {
		GenericCharterContract oldContainedCharterContract = containedCharterContract;
		containedCharterContract = newContainedCharterContract;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, oldContainedCharterContract, newContainedCharterContract);
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
	public void setContainedCharterContract(GenericCharterContract newContainedCharterContract) {
		if (newContainedCharterContract != containedCharterContract) {
			NotificationChain msgs = null;
			if (containedCharterContract != null)
				msgs = ((InternalEObject)containedCharterContract).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, null, msgs);
			if (newContainedCharterContract != null)
				msgs = ((InternalEObject)newContainedCharterContract).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, null, msgs);
			msgs = basicSetContainedCharterContract(newContainedCharterContract, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, newContainedCharterContract, newContainedCharterContract));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__OPTIONAL, oldOptional, optional));
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
	public GenericCharterContract getGenericCharterContract() {
		if (genericCharterContract != null && genericCharterContract.eIsProxy()) {
			InternalEObject oldGenericCharterContract = (InternalEObject)genericCharterContract;
			genericCharterContract = (GenericCharterContract)eResolveProxy(oldGenericCharterContract);
			if (genericCharterContract != oldGenericCharterContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT, oldGenericCharterContract, genericCharterContract));
			}
		}
		return genericCharterContract;
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenericCharterContract basicGetGenericCharterContract() {
		return genericCharterContract;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__CHARTER_NUMBER, oldCharterNumber, charterNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGenericCharterContract(GenericCharterContract newGenericCharterContract) {
		GenericCharterContract oldGenericCharterContract = genericCharterContract;
		genericCharterContract = newGenericCharterContract;
		boolean oldGenericCharterContractESet = genericCharterContractESet;
		genericCharterContractESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT, oldGenericCharterContract, genericCharterContract, !oldGenericCharterContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetGenericCharterContract() {
		GenericCharterContract oldGenericCharterContract = genericCharterContract;
		boolean oldGenericCharterContractESet = genericCharterContractESet;
		genericCharterContract = null;
		genericCharterContractESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT, oldGenericCharterContract, null, oldGenericCharterContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetGenericCharterContract() {
		return genericCharterContractESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CIIStartOptions getCiiStartOptions() {
		if (ciiStartOptions != null && ciiStartOptions.eIsProxy()) {
			InternalEObject oldCiiStartOptions = (InternalEObject)ciiStartOptions;
			ciiStartOptions = (CIIStartOptions)eResolveProxy(oldCiiStartOptions);
			if (ciiStartOptions != oldCiiStartOptions) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__CII_START_OPTIONS, oldCiiStartOptions, ciiStartOptions));
			}
		}
		return ciiStartOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CIIStartOptions basicGetCiiStartOptions() {
		return ciiStartOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCiiStartOptions(CIIStartOptions newCiiStartOptions) {
		CIIStartOptions oldCiiStartOptions = ciiStartOptions;
		ciiStartOptions = newCiiStartOptions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__CII_START_OPTIONS, oldCiiStartOptions, ciiStartOptions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CIIEndOptions getCiiEndOptions() {
		if (ciiEndOptions != null && ciiEndOptions.eIsProxy()) {
			InternalEObject oldCiiEndOptions = (InternalEObject)ciiEndOptions;
			ciiEndOptions = (CIIEndOptions)eResolveProxy(oldCiiEndOptions);
			if (ciiEndOptions != oldCiiEndOptions) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__CII_END_OPTIONS, oldCiiEndOptions, ciiEndOptions));
			}
		}
		return ciiEndOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CIIEndOptions basicGetCiiEndOptions() {
		return ciiEndOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCiiEndOptions(CIIEndOptions newCiiEndOptions) {
		CIIEndOptions oldCiiEndOptions = ciiEndOptions;
		ciiEndOptions = newCiiEndOptions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__CII_END_OPTIONS, oldCiiEndOptions, ciiEndOptions));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__MIN_DURATION, oldMinDuration, minDuration, !oldMinDurationESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__MIN_DURATION, oldMinDuration, MIN_DURATION_EDEFAULT, oldMinDurationESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__MAX_DURATION, oldMaxDuration, maxDuration, !oldMaxDurationESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__MAX_DURATION, oldMaxDuration, MAX_DURATION_EDEFAULT, oldMaxDurationESet));
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
	@Override
	public boolean isCharterContractOverride() {
		if (getContainedCharterContract() != null) {
			return true;
		}
		return charterContractOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterContractOverride(boolean newCharterContractOverride) {
		boolean oldCharterContractOverride = charterContractOverride;
		charterContractOverride = newCharterContractOverride;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE, oldCharterContractOverride, charterContractOverride));
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
	public int getCharterOrDelegateMinDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.VESSEL_CHARTER__MIN_DURATION);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getCharterOrDelegateMaxDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.VESSEL_CHARTER__MAX_DURATION);
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
	@Override
	public GenericCharterContract getCharterOrDelegateCharterContract() {
		if (this.isCharterContractOverride()) {
			return this.getContainedCharterContract();
		} else if (this.isSetGenericCharterContract() && this.getGenericCharterContract() != null) {
			return this.getGenericCharterContract();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BaseLegalEntity getCharterOrDelegateEntity() {
		return (BaseLegalEntity) eGetWithDefault(CargoPackage.Literals.VESSEL_CHARTER__ENTITY);
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_CHARTER__ENTITY, oldEntity, entity));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_CHARTER__ENTITY, oldEntity, entity, !oldEntityESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.VESSEL_CHARTER__ENTITY, oldEntity, null, oldEntityESet));
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
			case CargoPackage.VESSEL_CHARTER__START_HEEL:
				return basicSetStartHeel(null, msgs);
			case CargoPackage.VESSEL_CHARTER__END_HEEL:
				return basicSetEndHeel(null, msgs);
			case CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT:
				return basicSetContainedCharterContract(null, msgs);
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
			case CargoPackage.VESSEL_CHARTER__OPTIONAL:
				return isOptional();
			case CargoPackage.VESSEL_CHARTER__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case CargoPackage.VESSEL_CHARTER__CHARTER_NUMBER:
				return getCharterNumber();
			case CargoPackage.VESSEL_CHARTER__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CargoPackage.VESSEL_CHARTER__TIME_CHARTER_RATE:
				return getTimeCharterRate();
			case CargoPackage.VESSEL_CHARTER__START_AT:
				if (resolve) return getStartAt();
				return basicGetStartAt();
			case CargoPackage.VESSEL_CHARTER__START_AFTER:
				return getStartAfter();
			case CargoPackage.VESSEL_CHARTER__START_BY:
				return getStartBy();
			case CargoPackage.VESSEL_CHARTER__END_AT:
				return getEndAt();
			case CargoPackage.VESSEL_CHARTER__END_AFTER:
				return getEndAfter();
			case CargoPackage.VESSEL_CHARTER__END_BY:
				return getEndBy();
			case CargoPackage.VESSEL_CHARTER__START_HEEL:
				if (resolve) return getStartHeel();
				return basicGetStartHeel();
			case CargoPackage.VESSEL_CHARTER__END_HEEL:
				if (resolve) return getEndHeel();
				return basicGetEndHeel();
			case CargoPackage.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE:
				return isForceHireCostOnlyEndRule();
			case CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT:
				if (resolve) return getContainedCharterContract();
				return basicGetContainedCharterContract();
			case CargoPackage.VESSEL_CHARTER__MIN_DURATION:
				return getMinDuration();
			case CargoPackage.VESSEL_CHARTER__MAX_DURATION:
				return getMaxDuration();
			case CargoPackage.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE:
				return isCharterContractOverride();
			case CargoPackage.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT:
				if (resolve) return getGenericCharterContract();
				return basicGetGenericCharterContract();
			case CargoPackage.VESSEL_CHARTER__CII_START_OPTIONS:
				if (resolve) return getCiiStartOptions();
				return basicGetCiiStartOptions();
			case CargoPackage.VESSEL_CHARTER__CII_END_OPTIONS:
				if (resolve) return getCiiEndOptions();
				return basicGetCiiEndOptions();
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
			case CargoPackage.VESSEL_CHARTER__OPTIONAL:
				setOptional((Boolean)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__CHARTER_NUMBER:
				setCharterNumber((Integer)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__TIME_CHARTER_RATE:
				setTimeCharterRate((String)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__START_AT:
				setStartAt((Port)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__START_AFTER:
				setStartAfter((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__START_BY:
				setStartBy((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__END_AT:
				getEndAt().clear();
				getEndAt().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__END_AFTER:
				setEndAfter((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__END_BY:
				setEndBy((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__START_HEEL:
				setStartHeel((StartHeelOptions)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__END_HEEL:
				setEndHeel((EndHeelOptions)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE:
				setForceHireCostOnlyEndRule((Boolean)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT:
				setContainedCharterContract((GenericCharterContract)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__MIN_DURATION:
				setMinDuration((Integer)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__MAX_DURATION:
				setMaxDuration((Integer)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE:
				setCharterContractOverride((Boolean)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT:
				setGenericCharterContract((GenericCharterContract)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__CII_START_OPTIONS:
				setCiiStartOptions((CIIStartOptions)newValue);
				return;
			case CargoPackage.VESSEL_CHARTER__CII_END_OPTIONS:
				setCiiEndOptions((CIIEndOptions)newValue);
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
			case CargoPackage.VESSEL_CHARTER__OPTIONAL:
				setOptional(OPTIONAL_EDEFAULT);
				return;
			case CargoPackage.VESSEL_CHARTER__VESSEL:
				setVessel((Vessel)null);
				return;
			case CargoPackage.VESSEL_CHARTER__CHARTER_NUMBER:
				setCharterNumber(CHARTER_NUMBER_EDEFAULT);
				return;
			case CargoPackage.VESSEL_CHARTER__ENTITY:
				unsetEntity();
				return;
			case CargoPackage.VESSEL_CHARTER__TIME_CHARTER_RATE:
				unsetTimeCharterRate();
				return;
			case CargoPackage.VESSEL_CHARTER__START_AT:
				setStartAt((Port)null);
				return;
			case CargoPackage.VESSEL_CHARTER__START_AFTER:
				unsetStartAfter();
				return;
			case CargoPackage.VESSEL_CHARTER__START_BY:
				unsetStartBy();
				return;
			case CargoPackage.VESSEL_CHARTER__END_AT:
				getEndAt().clear();
				return;
			case CargoPackage.VESSEL_CHARTER__END_AFTER:
				unsetEndAfter();
				return;
			case CargoPackage.VESSEL_CHARTER__END_BY:
				unsetEndBy();
				return;
			case CargoPackage.VESSEL_CHARTER__START_HEEL:
				setStartHeel((StartHeelOptions)null);
				return;
			case CargoPackage.VESSEL_CHARTER__END_HEEL:
				setEndHeel((EndHeelOptions)null);
				return;
			case CargoPackage.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE:
				setForceHireCostOnlyEndRule(FORCE_HIRE_COST_ONLY_END_RULE_EDEFAULT);
				return;
			case CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT:
				setContainedCharterContract((GenericCharterContract)null);
				return;
			case CargoPackage.VESSEL_CHARTER__MIN_DURATION:
				unsetMinDuration();
				return;
			case CargoPackage.VESSEL_CHARTER__MAX_DURATION:
				unsetMaxDuration();
				return;
			case CargoPackage.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE:
				setCharterContractOverride(CHARTER_CONTRACT_OVERRIDE_EDEFAULT);
				return;
			case CargoPackage.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT:
				unsetGenericCharterContract();
				return;
			case CargoPackage.VESSEL_CHARTER__CII_START_OPTIONS:
				setCiiStartOptions((CIIStartOptions)null);
				return;
			case CargoPackage.VESSEL_CHARTER__CII_END_OPTIONS:
				setCiiEndOptions((CIIEndOptions)null);
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
			case CargoPackage.VESSEL_CHARTER__OPTIONAL:
				return optional != OPTIONAL_EDEFAULT;
			case CargoPackage.VESSEL_CHARTER__VESSEL:
				return vessel != null;
			case CargoPackage.VESSEL_CHARTER__CHARTER_NUMBER:
				return charterNumber != CHARTER_NUMBER_EDEFAULT;
			case CargoPackage.VESSEL_CHARTER__ENTITY:
				return isSetEntity();
			case CargoPackage.VESSEL_CHARTER__TIME_CHARTER_RATE:
				return isSetTimeCharterRate();
			case CargoPackage.VESSEL_CHARTER__START_AT:
				return startAt != null;
			case CargoPackage.VESSEL_CHARTER__START_AFTER:
				return isSetStartAfter();
			case CargoPackage.VESSEL_CHARTER__START_BY:
				return isSetStartBy();
			case CargoPackage.VESSEL_CHARTER__END_AT:
				return endAt != null && !endAt.isEmpty();
			case CargoPackage.VESSEL_CHARTER__END_AFTER:
				return isSetEndAfter();
			case CargoPackage.VESSEL_CHARTER__END_BY:
				return isSetEndBy();
			case CargoPackage.VESSEL_CHARTER__START_HEEL:
				return startHeel != null;
			case CargoPackage.VESSEL_CHARTER__END_HEEL:
				return endHeel != null;
			case CargoPackage.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE:
				return forceHireCostOnlyEndRule != FORCE_HIRE_COST_ONLY_END_RULE_EDEFAULT;
			case CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT:
				return containedCharterContract != null;
			case CargoPackage.VESSEL_CHARTER__MIN_DURATION:
				return isSetMinDuration();
			case CargoPackage.VESSEL_CHARTER__MAX_DURATION:
				return isSetMaxDuration();
			case CargoPackage.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE:
				return charterContractOverride != CHARTER_CONTRACT_OVERRIDE_EDEFAULT;
			case CargoPackage.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT:
				return isSetGenericCharterContract();
			case CargoPackage.VESSEL_CHARTER__CII_START_OPTIONS:
				return ciiStartOptions != null;
			case CargoPackage.VESSEL_CHARTER__CII_END_OPTIONS:
				return ciiEndOptions != null;
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
			case CargoPackage.VESSEL_CHARTER___GET_START_BY_AS_DATE_TIME:
				return getStartByAsDateTime();
			case CargoPackage.VESSEL_CHARTER___GET_START_AFTER_AS_DATE_TIME:
				return getStartAfterAsDateTime();
			case CargoPackage.VESSEL_CHARTER___GET_END_BY_AS_DATE_TIME:
				return getEndByAsDateTime();
			case CargoPackage.VESSEL_CHARTER___GET_END_AFTER_AS_DATE_TIME:
				return getEndAfterAsDateTime();
			case CargoPackage.VESSEL_CHARTER___GET_CHARTER_OR_DELEGATE_MIN_DURATION:
				return getCharterOrDelegateMinDuration();
			case CargoPackage.VESSEL_CHARTER___GET_CHARTER_OR_DELEGATE_MAX_DURATION:
				return getCharterOrDelegateMaxDuration();
			case CargoPackage.VESSEL_CHARTER___GET_CHARTER_OR_DELEGATE_ENTITY:
				return getCharterOrDelegateEntity();
			case CargoPackage.VESSEL_CHARTER___JSONID:
				return jsonid();
			case CargoPackage.VESSEL_CHARTER___GET_CHARTER_OR_DELEGATE_CHARTER_CONTRACT:
				return getCharterOrDelegateCharterContract();
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
		result.append(" (optional: ");
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
		result.append(", minDuration: ");
		if (minDurationESet) result.append(minDuration); else result.append("<unset>");
		result.append(", maxDuration: ");
		if (maxDurationESet) result.append(maxDuration); else result.append("<unset>");
		result.append(", charterContractOverride: ");
		result.append(charterContractOverride);
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
		SpotMarketsPackage spotMarkets = SpotMarketsPackage.eINSTANCE;
		if (cargo.getVesselCharter_MinDuration() == feature) {
			return new DelegateInformation(cargo.getVesselCharter_GenericCharterContract(), commercial.getGenericCharterContract_MinDuration(), (Integer) 0);
		} else if (cargo.getVesselCharter_MaxDuration() == feature) {
			return new DelegateInformation(cargo.getVesselCharter_GenericCharterContract(), commercial.getGenericCharterContract_MaxDuration(), (Integer) 0);
		}
		
		return super.getUnsetValueOrDelegate(feature);
	}	
} //VesselCharterImpl

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl#getVessel <em>Vessel</em>}</li>
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
 * </ul>
 *
 * @generated
 */
public class VesselAvailabilityImpl extends UUIDObjectImpl implements VesselAvailability {
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
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity entity;

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
	 * The cached value of the '{@link #getStartAt() <em>Start At</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAt()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> startAt;

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
	protected HeelOptions startHeel;

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
	public String getTimeCharterRate() {
		return timeCharterRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public boolean isSetTimeCharterRate() {
		return timeCharterRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getStartAt() {
		if (startAt == null) {
			startAt = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CargoPackage.VESSEL_AVAILABILITY__START_AT);
		}
		return startAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDateTime getStartAfter() {
		return startAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public boolean isSetStartAfter() {
		return startAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDateTime getStartBy() {
		return startBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public boolean isSetStartBy() {
		return startByESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public LocalDateTime getEndAfter() {
		return endAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public boolean isSetEndAfter() {
		return endAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDateTime getEndBy() {
		return endBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public boolean isSetEndBy() {
		return endByESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HeelOptions getStartHeel() {
		if (startHeel != null && startHeel.eIsProxy()) {
			InternalEObject oldStartHeel = (InternalEObject)startHeel;
			startHeel = (HeelOptions)eResolveProxy(oldStartHeel);
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
	public HeelOptions basicGetStartHeel() {
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartHeel(HeelOptions newStartHeel, NotificationChain msgs) {
		HeelOptions oldStartHeel = startHeel;
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
	public void setStartHeel(HeelOptions newStartHeel) {
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
	public boolean isForceHireCostOnlyEndRule() {
		return forceHireCostOnlyEndRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setForceHireCostOnlyEndRule(boolean newForceHireCostOnlyEndRule) {
		boolean oldForceHireCostOnlyEndRule = forceHireCostOnlyEndRule;
		forceHireCostOnlyEndRule = newForceHireCostOnlyEndRule;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE, oldForceHireCostOnlyEndRule, forceHireCostOnlyEndRule));
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
	 * @generated
	 */
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
	public void setEntity(BaseLegalEntity newEntity) {
		BaseLegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_AVAILABILITY__ENTITY, oldEntity, entity));
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
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				return getTimeCharterRate();
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				return getStartAt();
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
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				setTimeCharterRate((String)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				getStartAt().clear();
				getStartAt().addAll((Collection<? extends APortSet<Port>>)newValue);
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
				setStartHeel((HeelOptions)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
				setEndHeel((EndHeelOptions)newValue);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE:
				setForceHireCostOnlyEndRule((Boolean)newValue);
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
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				setVessel((Vessel)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				unsetTimeCharterRate();
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				getStartAt().clear();
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
				setStartHeel((HeelOptions)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
				setEndHeel((EndHeelOptions)null);
				return;
			case CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE:
				setForceHireCostOnlyEndRule(FORCE_HIRE_COST_ONLY_END_RULE_EDEFAULT);
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
			case CargoPackage.VESSEL_AVAILABILITY__VESSEL:
				return vessel != null;
			case CargoPackage.VESSEL_AVAILABILITY__ENTITY:
				return entity != null;
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				return isSetTimeCharterRate();
			case CargoPackage.VESSEL_AVAILABILITY__START_AT:
				return startAt != null && !startAt.isEmpty();
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (timeCharterRate: ");
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
		result.append(')');
		return result.toString();
	}

} //VesselAvailabilityImpl

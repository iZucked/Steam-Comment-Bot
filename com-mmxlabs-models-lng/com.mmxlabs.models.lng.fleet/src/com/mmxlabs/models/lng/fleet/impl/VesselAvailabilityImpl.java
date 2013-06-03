/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getStartBy <em>Start By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getEndBy <em>End By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getStartHeel <em>Start Heel</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselAvailabilityImpl extends UUIDObjectImpl implements VesselAvailability {
	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The default value of the '{@link #getTimeCharterRate() <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getTimeCharterRate()
	 * @generated
	 * @ordered
	 */
	protected static final int TIME_CHARTER_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTimeCharterRate() <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getTimeCharterRate()
	 * @generated
	 * @ordered
	 */
	protected int timeCharterRate = TIME_CHARTER_RATE_EDEFAULT;

	/**
	 * This is true if the Time Charter Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * @since 4.0
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
	protected static final Date START_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected Date startAfter = START_AFTER_EDEFAULT;

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
	protected static final Date START_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartBy() <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBy()
	 * @generated
	 * @ordered
	 */
	protected Date startBy = START_BY_EDEFAULT;

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
	protected static final Date END_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndAfter() <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndAfter()
	 * @generated
	 * @ordered
	 */
	protected Date endAfter = END_AFTER_EDEFAULT;

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
	protected static final Date END_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndBy() <em>End By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndBy()
	 * @generated
	 * @ordered
	 */
	protected Date endBy = END_BY_EDEFAULT;

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
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getStartHeel()
	 * @generated
	 * @ordered
	 */
	protected HeelOptions startHeel;

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
		return FleetPackage.Literals.VESSEL_AVAILABILITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getStartAt() {
		if (startAt == null) {
			startAt = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, FleetPackage.VESSEL_AVAILABILITY__START_AT);
		}
		return startAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartAfter() {
		return startAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartAfter(Date newStartAfter) {
		Date oldStartAfter = startAfter;
		startAfter = newStartAfter;
		boolean oldStartAfterESet = startAfterESet;
		startAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__START_AFTER, oldStartAfter, startAfter, !oldStartAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStartAfter() {
		Date oldStartAfter = startAfter;
		boolean oldStartAfterESet = startAfterESet;
		startAfter = START_AFTER_EDEFAULT;
		startAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABILITY__START_AFTER, oldStartAfter, START_AFTER_EDEFAULT, oldStartAfterESet));
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
	public Date getStartBy() {
		return startBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartBy(Date newStartBy) {
		Date oldStartBy = startBy;
		startBy = newStartBy;
		boolean oldStartByESet = startByESet;
		startByESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__START_BY, oldStartBy, startBy, !oldStartByESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStartBy() {
		Date oldStartBy = startBy;
		boolean oldStartByESet = startByESet;
		startBy = START_BY_EDEFAULT;
		startByESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABILITY__START_BY, oldStartBy, START_BY_EDEFAULT, oldStartByESet));
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
			endAt = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, FleetPackage.VESSEL_AVAILABILITY__END_AT);
		}
		return endAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getEndAfter() {
		return endAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndAfter(Date newEndAfter) {
		Date oldEndAfter = endAfter;
		endAfter = newEndAfter;
		boolean oldEndAfterESet = endAfterESet;
		endAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__END_AFTER, oldEndAfter, endAfter, !oldEndAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEndAfter() {
		Date oldEndAfter = endAfter;
		boolean oldEndAfterESet = endAfterESet;
		endAfter = END_AFTER_EDEFAULT;
		endAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABILITY__END_AFTER, oldEndAfter, END_AFTER_EDEFAULT, oldEndAfterESet));
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
	public Date getEndBy() {
		return endBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndBy(Date newEndBy) {
		Date oldEndBy = endBy;
		endBy = newEndBy;
		boolean oldEndByESet = endByESet;
		endByESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__END_BY, oldEndBy, endBy, !oldEndByESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEndBy() {
		Date oldEndBy = endBy;
		boolean oldEndByESet = endByESet;
		endBy = END_BY_EDEFAULT;
		endByESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABILITY__END_BY, oldEndBy, END_BY_EDEFAULT, oldEndByESet));
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
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL_AVAILABILITY__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HeelOptions getStartHeel() {
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartHeel(HeelOptions newStartHeel, NotificationChain msgs) {
		HeelOptions oldStartHeel = startHeel;
		startHeel = newStartHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__START_HEEL, oldStartHeel, newStartHeel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartHeel(HeelOptions newStartHeel) {
		if (newStartHeel != startHeel) {
			NotificationChain msgs = null;
			if (startHeel != null)
				msgs = ((InternalEObject)startHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_AVAILABILITY__START_HEEL, null, msgs);
			if (newStartHeel != null)
				msgs = ((InternalEObject)newStartHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_AVAILABILITY__START_HEEL, null, msgs);
			msgs = basicSetStartHeel(newStartHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__START_HEEL, newStartHeel, newStartHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTimeCharterRate() {
		return timeCharterRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeCharterRate(int newTimeCharterRate) {
		int oldTimeCharterRate = timeCharterRate;
		timeCharterRate = newTimeCharterRate;
		boolean oldTimeCharterRateESet = timeCharterRateESet;
		timeCharterRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE, oldTimeCharterRate, timeCharterRate, !oldTimeCharterRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTimeCharterRate() {
		int oldTimeCharterRate = timeCharterRate;
		boolean oldTimeCharterRateESet = timeCharterRateESet;
		timeCharterRate = TIME_CHARTER_RATE_EDEFAULT;
		timeCharterRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE, oldTimeCharterRate, TIME_CHARTER_RATE_EDEFAULT, oldTimeCharterRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL_AVAILABILITY__START_HEEL:
				return basicSetStartHeel(null, msgs);
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
			case FleetPackage.VESSEL_AVAILABILITY__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case FleetPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				return getTimeCharterRate();
			case FleetPackage.VESSEL_AVAILABILITY__START_AT:
				return getStartAt();
			case FleetPackage.VESSEL_AVAILABILITY__START_AFTER:
				return getStartAfter();
			case FleetPackage.VESSEL_AVAILABILITY__START_BY:
				return getStartBy();
			case FleetPackage.VESSEL_AVAILABILITY__END_AT:
				return getEndAt();
			case FleetPackage.VESSEL_AVAILABILITY__END_AFTER:
				return getEndAfter();
			case FleetPackage.VESSEL_AVAILABILITY__END_BY:
				return getEndBy();
			case FleetPackage.VESSEL_AVAILABILITY__START_HEEL:
				return getStartHeel();
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
			case FleetPackage.VESSEL_AVAILABILITY__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				setTimeCharterRate((Integer)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_AT:
				getStartAt().clear();
				getStartAt().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_AFTER:
				setStartAfter((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_BY:
				setStartBy((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_AT:
				getEndAt().clear();
				getEndAt().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_AFTER:
				setEndAfter((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_BY:
				setEndBy((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_HEEL:
				setStartHeel((HeelOptions)newValue);
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
			case FleetPackage.VESSEL_AVAILABILITY__VESSEL:
				setVessel((Vessel)null);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				unsetTimeCharterRate();
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_AT:
				getStartAt().clear();
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_AFTER:
				unsetStartAfter();
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_BY:
				unsetStartBy();
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_AT:
				getEndAt().clear();
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_AFTER:
				unsetEndAfter();
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_BY:
				unsetEndBy();
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_HEEL:
				setStartHeel((HeelOptions)null);
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
			case FleetPackage.VESSEL_AVAILABILITY__VESSEL:
				return vessel != null;
			case FleetPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
				return isSetTimeCharterRate();
			case FleetPackage.VESSEL_AVAILABILITY__START_AT:
				return startAt != null && !startAt.isEmpty();
			case FleetPackage.VESSEL_AVAILABILITY__START_AFTER:
				return isSetStartAfter();
			case FleetPackage.VESSEL_AVAILABILITY__START_BY:
				return isSetStartBy();
			case FleetPackage.VESSEL_AVAILABILITY__END_AT:
				return endAt != null && !endAt.isEmpty();
			case FleetPackage.VESSEL_AVAILABILITY__END_AFTER:
				return isSetEndAfter();
			case FleetPackage.VESSEL_AVAILABILITY__END_BY:
				return isSetEndBy();
			case FleetPackage.VESSEL_AVAILABILITY__START_HEEL:
				return startHeel != null;
		}
		return super.eIsSet(featureID);
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
		result.append(')');
		return result.toString();
	}

} // end of VesselAvailabilityImpl

// finish type fixing

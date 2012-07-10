/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselAvailability;

import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getStartBy <em>Start By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl#getEndBy <em>End By</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselAvailabilityImpl extends MMXObjectImpl implements VesselAvailability {
	/**
	 * The cached value of the '{@link #getStartAt() <em>Start At</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAt()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> startAt;

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
	protected EList<APortSet> endAt;

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
	public EList<APortSet> getStartAt() {
		if (startAt == null) {
			startAt = new EObjectResolvingEList<APortSet>(APortSet.class, this, FleetPackage.VESSEL_AVAILABILITY__START_AT);
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
	public EList<APortSet> getEndAt() {
		if (endAt == null) {
			endAt = new EObjectResolvingEList<APortSet>(APortSet.class, this, FleetPackage.VESSEL_AVAILABILITY__END_AT);
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
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
			case FleetPackage.VESSEL_AVAILABILITY__START_AT:
				getStartAt().clear();
				getStartAt().addAll((Collection<? extends APortSet>)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_AFTER:
				setStartAfter((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__START_BY:
				setStartBy((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_AT:
				getEndAt().clear();
				getEndAt().addAll((Collection<? extends APortSet>)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_AFTER:
				setEndAfter((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABILITY__END_BY:
				setEndBy((Date)newValue);
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
		result.append(" (startAfter: ");
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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.schedule.SchedulePackage;
import scenario.schedule.VesselEventRevenue;
import scenario.schedule.events.VesselEventVisit;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Vessel Event Revenue</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.impl.VesselEventRevenueImpl#getVesselEventVisit <em>Vessel Event Visit</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselEventRevenueImpl extends BookedRevenueImpl implements VesselEventRevenue {
	/**
	 * The cached value of the '{@link #getVesselEventVisit() <em>Vessel Event Visit</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getVesselEventVisit()
	 * @generated
	 * @ordered
	 */
	protected VesselEventVisit vesselEventVisit;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventRevenueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.VESSEL_EVENT_REVENUE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventVisit getVesselEventVisit() {
		if (vesselEventVisit != null && vesselEventVisit.eIsProxy()) {
			InternalEObject oldVesselEventVisit = (InternalEObject)vesselEventVisit;
			vesselEventVisit = (VesselEventVisit)eResolveProxy(oldVesselEventVisit);
			if (vesselEventVisit != oldVesselEventVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.VESSEL_EVENT_REVENUE__VESSEL_EVENT_VISIT, oldVesselEventVisit, vesselEventVisit));
			}
		}
		return vesselEventVisit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public VesselEventVisit basicGetVesselEventVisit() {
		return vesselEventVisit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselEventVisit(VesselEventVisit newVesselEventVisit) {
		VesselEventVisit oldVesselEventVisit = vesselEventVisit;
		vesselEventVisit = newVesselEventVisit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_REVENUE__VESSEL_EVENT_VISIT, oldVesselEventVisit, vesselEventVisit));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return getVesselEventVisit().getVesselEvent().getId();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.VESSEL_EVENT_REVENUE__VESSEL_EVENT_VISIT:
				if (resolve) return getVesselEventVisit();
				return basicGetVesselEventVisit();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchedulePackage.VESSEL_EVENT_REVENUE__VESSEL_EVENT_VISIT:
				setVesselEventVisit((VesselEventVisit)newValue);
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
			case SchedulePackage.VESSEL_EVENT_REVENUE__VESSEL_EVENT_VISIT:
				setVesselEventVisit((VesselEventVisit)null);
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
			case SchedulePackage.VESSEL_EVENT_REVENUE__VESSEL_EVENT_VISIT:
				return vesselEventVisit != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.VESSEL_EVENT_REVENUE___GET_NAME:
				return getName();
		}
		return super.eInvoke(operationID, arguments);
	}

} // VesselEventRevenueImpl

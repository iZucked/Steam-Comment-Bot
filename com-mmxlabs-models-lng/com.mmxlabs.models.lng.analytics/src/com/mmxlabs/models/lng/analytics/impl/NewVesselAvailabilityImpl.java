/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.NewVesselAvailability;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>New Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.NewVesselAvailabilityImpl#getVesselAvailability <em>Vessel Availability</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NewVesselAvailabilityImpl extends ShippingOptionImpl implements NewVesselAvailability {
	/**
	 * The cached value of the '{@link #getVesselAvailability() <em>Vessel Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAvailability()
	 * @generated
	 * @ordered
	 */
	protected VesselAvailability vesselAvailability;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NewVesselAvailabilityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.NEW_VESSEL_AVAILABILITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailability getVesselAvailability() {
		return vesselAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetVesselAvailability(VesselAvailability newVesselAvailability, NotificationChain msgs) {
		VesselAvailability oldVesselAvailability = vesselAvailability;
		vesselAvailability = newVesselAvailability;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY, oldVesselAvailability, newVesselAvailability);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselAvailability(VesselAvailability newVesselAvailability) {
		if (newVesselAvailability != vesselAvailability) {
			NotificationChain msgs = null;
			if (vesselAvailability != null)
				msgs = ((InternalEObject)vesselAvailability).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY, null, msgs);
			if (newVesselAvailability != null)
				msgs = ((InternalEObject)newVesselAvailability).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY, null, msgs);
			msgs = basicSetVesselAvailability(newVesselAvailability, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY, newVesselAvailability, newVesselAvailability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
				return basicSetVesselAvailability(null, msgs);
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
			case AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
				return getVesselAvailability();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
				setVesselAvailability((VesselAvailability)newValue);
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
			case AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
				setVesselAvailability((VesselAvailability)null);
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
			case AnalyticsPackage.NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
				return vesselAvailability != null;
		}
		return super.eIsSet(featureID);
	}

} //NewVesselAvailabilityImpl

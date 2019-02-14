/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ExistingVesselAvailability;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Existing Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ExistingVesselAvailabilityImpl#getVesselAvailability <em>Vessel Availability</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExistingVesselAvailabilityImpl extends ShippingOptionImpl implements ExistingVesselAvailability {
	/**
	 * The cached value of the '{@link #getVesselAvailability() <em>Vessel Availability</em>}' reference.
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
	protected ExistingVesselAvailabilityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.EXISTING_VESSEL_AVAILABILITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailability getVesselAvailability() {
		if (vesselAvailability != null && vesselAvailability.eIsProxy()) {
			InternalEObject oldVesselAvailability = (InternalEObject)vesselAvailability;
			vesselAvailability = (VesselAvailability)eResolveProxy(oldVesselAvailability);
			if (vesselAvailability != oldVesselAvailability) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY, oldVesselAvailability, vesselAvailability));
			}
		}
		return vesselAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailability basicGetVesselAvailability() {
		return vesselAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselAvailability(VesselAvailability newVesselAvailability) {
		VesselAvailability oldVesselAvailability = vesselAvailability;
		vesselAvailability = newVesselAvailability;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY, oldVesselAvailability, vesselAvailability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
				if (resolve) return getVesselAvailability();
				return basicGetVesselAvailability();
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
			case AnalyticsPackage.EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
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
			case AnalyticsPackage.EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
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
			case AnalyticsPackage.EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY:
				return vesselAvailability != null;
		}
		return super.eIsSet(featureID);
	}

} //ExistingVesselAvailabilityImpl

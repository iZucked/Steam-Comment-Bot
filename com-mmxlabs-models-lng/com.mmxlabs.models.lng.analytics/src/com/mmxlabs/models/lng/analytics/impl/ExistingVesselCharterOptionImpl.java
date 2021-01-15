/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ExistingVesselCharterOptionImpl#getVesselCharter <em>Vessel Charter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExistingVesselCharterOptionImpl extends UUIDObjectImpl implements ExistingVesselCharterOption {
	/**
	 * The cached value of the '{@link #getVesselCharter() <em>Vessel Charter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselCharter()
	 * @generated
	 * @ordered
	 */
	protected VesselAvailability vesselCharter;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExistingVesselCharterOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.EXISTING_VESSEL_CHARTER_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselAvailability getVesselCharter() {
		if (vesselCharter != null && vesselCharter.eIsProxy()) {
			InternalEObject oldVesselCharter = (InternalEObject)vesselCharter;
			vesselCharter = (VesselAvailability)eResolveProxy(oldVesselCharter);
			if (vesselCharter != oldVesselCharter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER, oldVesselCharter, vesselCharter));
			}
		}
		return vesselCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailability basicGetVesselCharter() {
		return vesselCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselCharter(VesselAvailability newVesselCharter) {
		VesselAvailability oldVesselCharter = vesselCharter;
		vesselCharter = newVesselCharter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER, oldVesselCharter, vesselCharter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				if (resolve) return getVesselCharter();
				return basicGetVesselCharter();
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
			case AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				setVesselCharter((VesselAvailability)newValue);
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
			case AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				setVesselCharter((VesselAvailability)null);
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
			case AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				return vesselCharter != null;
		}
		return super.eIsSet(featureID);
	}

} //ExistingVesselAvailabilityImpl

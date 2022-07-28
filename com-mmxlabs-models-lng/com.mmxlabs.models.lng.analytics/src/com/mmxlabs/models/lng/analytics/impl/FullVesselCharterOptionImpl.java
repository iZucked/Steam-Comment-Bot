/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;

import com.mmxlabs.models.lng.cargo.VesselCharter;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>New Vessel Charter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.FullVesselCharterOptionImpl#getVesselCharter <em>Vessel Charter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FullVesselCharterOptionImpl extends UUIDObjectImpl implements FullVesselCharterOption {
	/**
	 * The cached value of the '{@link #getVesselCharter() <em>Vessel Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselCharter()
	 * @generated
	 * @ordered
	 */
	protected VesselCharter vesselCharter;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FullVesselCharterOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.FULL_VESSEL_CHARTER_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselCharter getVesselCharter() {
		return vesselCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetVesselCharter(VesselCharter newVesselCharter, NotificationChain msgs) {
		VesselCharter oldVesselCharter = vesselCharter;
		vesselCharter = newVesselCharter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER, oldVesselCharter, newVesselCharter);
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
	public void setVesselCharter(VesselCharter newVesselCharter) {
		if (newVesselCharter != vesselCharter) {
			NotificationChain msgs = null;
			if (vesselCharter != null)
				msgs = ((InternalEObject)vesselCharter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER, null, msgs);
			if (newVesselCharter != null)
				msgs = ((InternalEObject)newVesselCharter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER, null, msgs);
			msgs = basicSetVesselCharter(newVesselCharter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER, newVesselCharter, newVesselCharter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				return basicSetVesselCharter(null, msgs);
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
			case AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				return getVesselCharter();
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
			case AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				setVesselCharter((VesselCharter)newValue);
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
			case AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				setVesselCharter((VesselCharter)null);
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
			case AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER:
				return vesselCharter != null;
		}
		return super.eIsSet(featureID);
	}

} //NewVesselCharterImpl

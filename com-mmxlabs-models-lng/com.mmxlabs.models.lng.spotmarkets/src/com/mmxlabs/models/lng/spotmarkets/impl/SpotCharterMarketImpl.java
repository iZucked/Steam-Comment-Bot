/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.spotmarkets.SpotCharterMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Charter Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotCharterMarketImpl#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotCharterMarketImpl#getVesselClass <em>Vessel Class</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class SpotCharterMarketImpl extends EObjectImpl implements SpotCharterMarket {
	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = true;
	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;
	/**
	 * The cached value of the '{@link #getVesselClass() <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClass()
	 * @generated
	 * @ordered
	 */
	protected VesselClass vesselClass;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotCharterMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnabled(boolean newEnabled) {
		boolean oldEnabled = enabled;
		enabled = newEnabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_CHARTER_MARKET__ENABLED, oldEnabled, enabled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass getVesselClass() {
		if (vesselClass != null && vesselClass.eIsProxy()) {
			InternalEObject oldVesselClass = (InternalEObject)vesselClass;
			vesselClass = (VesselClass)eResolveProxy(oldVesselClass);
			if (vesselClass != oldVesselClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.SPOT_CHARTER_MARKET__VESSEL_CLASS, oldVesselClass, vesselClass));
			}
		}
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetVesselClass() {
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselClass(VesselClass newVesselClass) {
		VesselClass oldVesselClass = vesselClass;
		vesselClass = newVesselClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_CHARTER_MARKET__VESSEL_CLASS, oldVesselClass, vesselClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__ENABLED:
				return isEnabled();
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
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
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__ENABLED:
				setEnabled((Boolean)newValue);
				return;
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__VESSEL_CLASS:
				setVesselClass((VesselClass)newValue);
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
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__ENABLED:
				setEnabled(ENABLED_EDEFAULT);
				return;
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__VESSEL_CLASS:
				setVesselClass((VesselClass)null);
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
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__ENABLED:
				return enabled != ENABLED_EDEFAULT;
			case SpotMarketsPackage.SPOT_CHARTER_MARKET__VESSEL_CLASS:
				return vesselClass != null;
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
		result.append(" (enabled: ");
		result.append(enabled);
		result.append(')');
		return result.toString();
	}

} //SpotCharterMarketImpl

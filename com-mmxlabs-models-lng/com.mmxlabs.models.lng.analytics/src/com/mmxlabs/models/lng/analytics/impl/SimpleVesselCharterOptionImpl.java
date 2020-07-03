/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fleet Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl#isUseSafetyHeel <em>Use Safety Heel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimpleVesselCharterOptionImpl extends UUIDObjectImpl implements SimpleVesselCharterOption {
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
	 * The default value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected static final String HIRE_COST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected String hireCost = HIRE_COST_EDEFAULT;

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
	 * The default value of the '{@link #isUseSafetyHeel() <em>Use Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseSafetyHeel()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_SAFETY_HEEL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseSafetyHeel() <em>Use Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseSafetyHeel()
	 * @generated
	 * @ordered
	 */
	protected boolean useSafetyHeel = USE_SAFETY_HEEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleVesselCharterOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SIMPLE_VESSEL_CHARTER_OPTION;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__VESSEL, oldVessel, vessel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getHireCost() {
		return hireCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHireCost(String newHireCost) {
		String oldHireCost = hireCost;
		hireCost = newHireCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST, oldHireCost, hireCost));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__ENTITY, oldEntity, entity));
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
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isUseSafetyHeel() {
		return useSafetyHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUseSafetyHeel(boolean newUseSafetyHeel) {
		boolean oldUseSafetyHeel = useSafetyHeel;
		useSafetyHeel = newUseSafetyHeel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL, oldUseSafetyHeel, useSafetyHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST:
				return getHireCost();
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL:
				return isUseSafetyHeel();
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
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST:
				setHireCost((String)newValue);
				return;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL:
				setUseSafetyHeel((Boolean)newValue);
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
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__VESSEL:
				setVessel((Vessel)null);
				return;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST:
				setHireCost(HIRE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL:
				setUseSafetyHeel(USE_SAFETY_HEEL_EDEFAULT);
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
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__VESSEL:
				return vessel != null;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST:
				return HIRE_COST_EDEFAULT == null ? hireCost != null : !HIRE_COST_EDEFAULT.equals(hireCost);
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__ENTITY:
				return entity != null;
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL:
				return useSafetyHeel != USE_SAFETY_HEEL_EDEFAULT;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (hireCost: ");
		result.append(hireCost);
		result.append(", useSafetyHeel: ");
		result.append(useSafetyHeel);
		result.append(')');
		return result.toString();
	}

} //FleetShippingOptionImpl

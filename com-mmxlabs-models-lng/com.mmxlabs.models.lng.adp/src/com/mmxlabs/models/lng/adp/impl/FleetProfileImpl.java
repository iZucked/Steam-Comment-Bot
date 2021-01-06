/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FleetConstraint;
import com.mmxlabs.models.lng.adp.FleetProfile;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fleet Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#getDefaultNominalMarket <em>Default Nominal Market</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FleetProfileImpl extends EObjectImpl implements FleetProfile {
	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<FleetConstraint> constraints;

	/**
	 * The cached value of the '{@link #getDefaultNominalMarket() <em>Default Nominal Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultNominalMarket()
	 * @generated
	 * @ordered
	 */
	protected CharterInMarket defaultNominalMarket;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.FLEET_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FleetConstraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList.Resolving<FleetConstraint>(FleetConstraint.class, this, ADPPackage.FLEET_PROFILE__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterInMarket getDefaultNominalMarket() {
		if (defaultNominalMarket != null && defaultNominalMarket.eIsProxy()) {
			InternalEObject oldDefaultNominalMarket = (InternalEObject)defaultNominalMarket;
			defaultNominalMarket = (CharterInMarket)eResolveProxy(oldDefaultNominalMarket);
			if (defaultNominalMarket != oldDefaultNominalMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET, oldDefaultNominalMarket, defaultNominalMarket));
			}
		}
		return defaultNominalMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarket basicGetDefaultNominalMarket() {
		return defaultNominalMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultNominalMarket(CharterInMarket newDefaultNominalMarket) {
		CharterInMarket oldDefaultNominalMarket = defaultNominalMarket;
		defaultNominalMarket = newDefaultNominalMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET, oldDefaultNominalMarket, defaultNominalMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				return getConstraints();
			case ADPPackage.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET:
				if (resolve) return getDefaultNominalMarket();
				return basicGetDefaultNominalMarket();
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
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends FleetConstraint>)newValue);
				return;
			case ADPPackage.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET:
				setDefaultNominalMarket((CharterInMarket)newValue);
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
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				getConstraints().clear();
				return;
			case ADPPackage.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET:
				setDefaultNominalMarket((CharterInMarket)null);
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
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case ADPPackage.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET:
				return defaultNominalMarket != null;
		}
		return super.eIsSet(featureID);
	}

} //FleetProfileImpl

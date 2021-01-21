/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Max Cargo Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MaxCargoConstraintImpl#getMaxCargoes <em>Max Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MaxCargoConstraintImpl#getIntervalType <em>Interval Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MaxCargoConstraintImpl extends ProfileConstraintImpl implements MaxCargoConstraint {
	/**
	 * The default value of the '{@link #getMaxCargoes() <em>Max Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxCargoes() <em>Max Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCargoes()
	 * @generated
	 * @ordered
	 */
	protected int maxCargoes = MAX_CARGOES_EDEFAULT;

	/**
	 * The default value of the '{@link #getIntervalType() <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntervalType()
	 * @generated
	 * @ordered
	 */
	protected static final IntervalType INTERVAL_TYPE_EDEFAULT = IntervalType.YEARLY;

	/**
	 * The cached value of the '{@link #getIntervalType() <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntervalType()
	 * @generated
	 * @ordered
	 */
	protected IntervalType intervalType = INTERVAL_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MaxCargoConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.MAX_CARGO_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxCargoes() {
		return maxCargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxCargoes(int newMaxCargoes) {
		int oldMaxCargoes = maxCargoes;
		maxCargoes = newMaxCargoes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MAX_CARGO_CONSTRAINT__MAX_CARGOES, oldMaxCargoes, maxCargoes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IntervalType getIntervalType() {
		return intervalType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIntervalType(IntervalType newIntervalType) {
		IntervalType oldIntervalType = intervalType;
		intervalType = newIntervalType == null ? INTERVAL_TYPE_EDEFAULT : newIntervalType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MAX_CARGO_CONSTRAINT__INTERVAL_TYPE, oldIntervalType, intervalType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.MAX_CARGO_CONSTRAINT__MAX_CARGOES:
				return getMaxCargoes();
			case ADPPackage.MAX_CARGO_CONSTRAINT__INTERVAL_TYPE:
				return getIntervalType();
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
			case ADPPackage.MAX_CARGO_CONSTRAINT__MAX_CARGOES:
				setMaxCargoes((Integer)newValue);
				return;
			case ADPPackage.MAX_CARGO_CONSTRAINT__INTERVAL_TYPE:
				setIntervalType((IntervalType)newValue);
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
			case ADPPackage.MAX_CARGO_CONSTRAINT__MAX_CARGOES:
				setMaxCargoes(MAX_CARGOES_EDEFAULT);
				return;
			case ADPPackage.MAX_CARGO_CONSTRAINT__INTERVAL_TYPE:
				setIntervalType(INTERVAL_TYPE_EDEFAULT);
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
			case ADPPackage.MAX_CARGO_CONSTRAINT__MAX_CARGOES:
				return maxCargoes != MAX_CARGOES_EDEFAULT;
			case ADPPackage.MAX_CARGO_CONSTRAINT__INTERVAL_TYPE:
				return intervalType != INTERVAL_TYPE_EDEFAULT;
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
		result.append(" (maxCargoes: ");
		result.append(maxCargoes);
		result.append(", intervalType: ");
		result.append(intervalType);
		result.append(')');
		return result.toString();
	}

} //MaxCargoConstraintImpl

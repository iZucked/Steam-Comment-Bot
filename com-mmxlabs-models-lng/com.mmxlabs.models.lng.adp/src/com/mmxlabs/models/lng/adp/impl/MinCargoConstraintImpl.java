/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.MinCargoConstraint;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Min Cargo Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MinCargoConstraintImpl#getMinCargoes <em>Min Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MinCargoConstraintImpl#getIntervalType <em>Interval Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MinCargoConstraintImpl extends ProfileConstraintImpl implements MinCargoConstraint {
	/**
	 * The default value of the '{@link #getMinCargoes() <em>Min Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinCargoes() <em>Min Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCargoes()
	 * @generated
	 * @ordered
	 */
	protected int minCargoes = MIN_CARGOES_EDEFAULT;

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
	protected MinCargoConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.MIN_CARGO_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinCargoes() {
		return minCargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinCargoes(int newMinCargoes) {
		int oldMinCargoes = minCargoes;
		minCargoes = newMinCargoes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MIN_CARGO_CONSTRAINT__MIN_CARGOES, oldMinCargoes, minCargoes));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MIN_CARGO_CONSTRAINT__INTERVAL_TYPE, oldIntervalType, intervalType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.MIN_CARGO_CONSTRAINT__MIN_CARGOES:
				return getMinCargoes();
			case ADPPackage.MIN_CARGO_CONSTRAINT__INTERVAL_TYPE:
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
			case ADPPackage.MIN_CARGO_CONSTRAINT__MIN_CARGOES:
				setMinCargoes((Integer)newValue);
				return;
			case ADPPackage.MIN_CARGO_CONSTRAINT__INTERVAL_TYPE:
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
			case ADPPackage.MIN_CARGO_CONSTRAINT__MIN_CARGOES:
				setMinCargoes(MIN_CARGOES_EDEFAULT);
				return;
			case ADPPackage.MIN_CARGO_CONSTRAINT__INTERVAL_TYPE:
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
			case ADPPackage.MIN_CARGO_CONSTRAINT__MIN_CARGOES:
				return minCargoes != MIN_CARGOES_EDEFAULT;
			case ADPPackage.MIN_CARGO_CONSTRAINT__INTERVAL_TYPE:
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
		result.append(" (minCargoes: ");
		result.append(minCargoes);
		result.append(", intervalType: ");
		result.append(intervalType);
		result.append(')');
		return result.toString();
	}

} //MinCargoConstraintImpl

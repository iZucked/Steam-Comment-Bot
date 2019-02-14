/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint;

import com.mmxlabs.models.lng.fleet.Vessel;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Target Cargoes On Vessel Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl#getTargetNumberOfCargoes <em>Target Number Of Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl#getIntervalType <em>Interval Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl#getWeight <em>Weight</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TargetCargoesOnVesselConstraintImpl extends EObjectImpl implements TargetCargoesOnVesselConstraint {
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
	 * The default value of the '{@link #getTargetNumberOfCargoes() <em>Target Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetNumberOfCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int TARGET_NUMBER_OF_CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTargetNumberOfCargoes() <em>Target Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetNumberOfCargoes()
	 * @generated
	 * @ordered
	 */
	protected int targetNumberOfCargoes = TARGET_NUMBER_OF_CARGOES_EDEFAULT;

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
	 * The default value of the '{@link #getWeight() <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeight()
	 * @generated
	 * @ordered
	 */
	protected static final int WEIGHT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWeight() <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeight()
	 * @generated
	 * @ordered
	 */
	protected int weight = WEIGHT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TargetCargoesOnVesselConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.TARGET_CARGOES_ON_VESSEL_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL, oldVessel, vessel));
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
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTargetNumberOfCargoes() {
		return targetNumberOfCargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetNumberOfCargoes(int newTargetNumberOfCargoes) {
		int oldTargetNumberOfCargoes = targetNumberOfCargoes;
		targetNumberOfCargoes = newTargetNumberOfCargoes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES, oldTargetNumberOfCargoes, targetNumberOfCargoes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IntervalType getIntervalType() {
		return intervalType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIntervalType(IntervalType newIntervalType) {
		IntervalType oldIntervalType = intervalType;
		intervalType = newIntervalType == null ? INTERVAL_TYPE_EDEFAULT : newIntervalType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE, oldIntervalType, intervalType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWeight(int newWeight) {
		int oldWeight = weight;
		weight = newWeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT, oldWeight, weight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES:
				return getTargetNumberOfCargoes();
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE:
				return getIntervalType();
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT:
				return getWeight();
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
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES:
				setTargetNumberOfCargoes((Integer)newValue);
				return;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE:
				setIntervalType((IntervalType)newValue);
				return;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT:
				setWeight((Integer)newValue);
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
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL:
				setVessel((Vessel)null);
				return;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES:
				setTargetNumberOfCargoes(TARGET_NUMBER_OF_CARGOES_EDEFAULT);
				return;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE:
				setIntervalType(INTERVAL_TYPE_EDEFAULT);
				return;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT:
				setWeight(WEIGHT_EDEFAULT);
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
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL:
				return vessel != null;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES:
				return targetNumberOfCargoes != TARGET_NUMBER_OF_CARGOES_EDEFAULT;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE:
				return intervalType != INTERVAL_TYPE_EDEFAULT;
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT:
				return weight != WEIGHT_EDEFAULT;
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
		result.append(" (targetNumberOfCargoes: ");
		result.append(targetNumberOfCargoes);
		result.append(", intervalType: ");
		result.append(intervalType);
		result.append(", weight: ");
		result.append(weight);
		result.append(')');
		return result.toString();
	}

} //TargetCargoesOnVesselConstraintImpl

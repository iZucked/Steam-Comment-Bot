/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Period Distribution Profile Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionProfileConstraintImpl#getDistributions <em>Distributions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PeriodDistributionProfileConstraintImpl extends ProfileConstraintImpl implements PeriodDistributionProfileConstraint {
	/**
	 * The cached value of the '{@link #getDistributions() <em>Distributions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistributions()
	 * @generated
	 * @ordered
	 */
	protected EList<PeriodDistribution> distributions;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PeriodDistributionProfileConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PeriodDistribution> getDistributions() {
		if (distributions == null) {
			distributions = new EObjectContainmentEList.Resolving<PeriodDistribution>(PeriodDistribution.class, this, ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS);
		}
		return distributions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS:
				return ((InternalEList<?>)getDistributions()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS:
				return getDistributions();
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
			case ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS:
				getDistributions().clear();
				getDistributions().addAll((Collection<? extends PeriodDistribution>)newValue);
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
			case ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS:
				getDistributions().clear();
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
			case ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS:
				return distributions != null && !distributions.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (PeriodDistribution dist : this.distributions) {
			sb.append(dist.toString()).append("\r\n");
		}
		return sb.toString();
	}
	
} //PeriodDistributionProfileConstraintImpl

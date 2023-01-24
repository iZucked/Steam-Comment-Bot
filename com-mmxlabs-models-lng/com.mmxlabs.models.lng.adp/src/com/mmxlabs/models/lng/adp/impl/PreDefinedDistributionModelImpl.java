/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Pre Defined Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl#getDates <em>Dates</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PreDefinedDistributionModelImpl extends DistributionModelImpl implements PreDefinedDistributionModel {
	/**
	 * The cached value of the '{@link #getDates() <em>Dates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDates()
	 * @generated
	 * @ordered
	 */
	protected EList<PreDefinedDate> dates;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PreDefinedDistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.PRE_DEFINED_DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PreDefinedDate> getDates() {
		if (dates == null) {
			dates = new EObjectContainmentEList.Resolving<PreDefinedDate>(PreDefinedDate.class, this, ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES);
		}
		return dates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				return ((InternalEList<?>)getDates()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				return getDates();
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				getDates().clear();
				getDates().addAll((Collection<? extends PreDefinedDate>)newValue);
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				getDates().clear();
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				return dates != null && !dates.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PreDefinedDistributionModelImpl

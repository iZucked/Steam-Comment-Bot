/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.lng.fleet.CIIGradeBoundary;
import com.mmxlabs.models.lng.fleet.CIIReductionFactor;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CII Reference Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIReferenceDataImpl#getCiiGradeBoundaries <em>Cii Grade Boundaries</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIReferenceDataImpl#getFuelEmissions <em>Fuel Emissions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIReferenceDataImpl#getReductionFactors <em>Reduction Factors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CIIReferenceDataImpl extends EObjectImpl implements CIIReferenceData {
	/**
	 * The cached value of the '{@link #getCiiGradeBoundaries() <em>Cii Grade Boundaries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCiiGradeBoundaries()
	 * @generated
	 * @ordered
	 */
	protected EList<CIIGradeBoundary> ciiGradeBoundaries;

	/**
	 * The cached value of the '{@link #getFuelEmissions() <em>Fuel Emissions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelEmissions()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelEmissionReference> fuelEmissions;

	/**
	 * The cached value of the '{@link #getReductionFactors() <em>Reduction Factors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReductionFactors()
	 * @generated
	 * @ordered
	 */
	protected EList<CIIReductionFactor> reductionFactors;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CIIReferenceDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.CII_REFERENCE_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CIIGradeBoundary> getCiiGradeBoundaries() {
		if (ciiGradeBoundaries == null) {
			ciiGradeBoundaries = new EObjectContainmentEList<CIIGradeBoundary>(CIIGradeBoundary.class, this, FleetPackage.CII_REFERENCE_DATA__CII_GRADE_BOUNDARIES);
		}
		return ciiGradeBoundaries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FuelEmissionReference> getFuelEmissions() {
		if (fuelEmissions == null) {
			fuelEmissions = new EObjectContainmentEList<FuelEmissionReference>(FuelEmissionReference.class, this, FleetPackage.CII_REFERENCE_DATA__FUEL_EMISSIONS);
		}
		return fuelEmissions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CIIReductionFactor> getReductionFactors() {
		if (reductionFactors == null) {
			reductionFactors = new EObjectContainmentEList<CIIReductionFactor>(CIIReductionFactor.class, this, FleetPackage.CII_REFERENCE_DATA__REDUCTION_FACTORS);
		}
		return reductionFactors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.CII_REFERENCE_DATA__CII_GRADE_BOUNDARIES:
				return ((InternalEList<?>)getCiiGradeBoundaries()).basicRemove(otherEnd, msgs);
			case FleetPackage.CII_REFERENCE_DATA__FUEL_EMISSIONS:
				return ((InternalEList<?>)getFuelEmissions()).basicRemove(otherEnd, msgs);
			case FleetPackage.CII_REFERENCE_DATA__REDUCTION_FACTORS:
				return ((InternalEList<?>)getReductionFactors()).basicRemove(otherEnd, msgs);
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
			case FleetPackage.CII_REFERENCE_DATA__CII_GRADE_BOUNDARIES:
				return getCiiGradeBoundaries();
			case FleetPackage.CII_REFERENCE_DATA__FUEL_EMISSIONS:
				return getFuelEmissions();
			case FleetPackage.CII_REFERENCE_DATA__REDUCTION_FACTORS:
				return getReductionFactors();
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
			case FleetPackage.CII_REFERENCE_DATA__CII_GRADE_BOUNDARIES:
				getCiiGradeBoundaries().clear();
				getCiiGradeBoundaries().addAll((Collection<? extends CIIGradeBoundary>)newValue);
				return;
			case FleetPackage.CII_REFERENCE_DATA__FUEL_EMISSIONS:
				getFuelEmissions().clear();
				getFuelEmissions().addAll((Collection<? extends FuelEmissionReference>)newValue);
				return;
			case FleetPackage.CII_REFERENCE_DATA__REDUCTION_FACTORS:
				getReductionFactors().clear();
				getReductionFactors().addAll((Collection<? extends CIIReductionFactor>)newValue);
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
			case FleetPackage.CII_REFERENCE_DATA__CII_GRADE_BOUNDARIES:
				getCiiGradeBoundaries().clear();
				return;
			case FleetPackage.CII_REFERENCE_DATA__FUEL_EMISSIONS:
				getFuelEmissions().clear();
				return;
			case FleetPackage.CII_REFERENCE_DATA__REDUCTION_FACTORS:
				getReductionFactors().clear();
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
			case FleetPackage.CII_REFERENCE_DATA__CII_GRADE_BOUNDARIES:
				return ciiGradeBoundaries != null && !ciiGradeBoundaries.isEmpty();
			case FleetPackage.CII_REFERENCE_DATA__FUEL_EMISSIONS:
				return fuelEmissions != null && !fuelEmissions.isEmpty();
			case FleetPackage.CII_REFERENCE_DATA__REDUCTION_FACTORS:
				return reductionFactors != null && !reductionFactors.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CIIReferenceDataImpl
